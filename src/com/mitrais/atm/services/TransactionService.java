package com.mitrais.atm.services;

import com.mitrais.atm.helpers.CsvHelper;
import com.mitrais.atm.models.AccountModel;
import com.mitrais.atm.models.TransactionModel;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Transaction Service
 * @author Ardi_PR876
 */
public class TransactionService {
    private static TransactionService INSTANCE;
    private final String userDir = System.getProperty("user.dir");
    private final String transactionCsv = this.userDir + "\\File\\transaction.csv";
    private final AccountService accountService = AccountService.getInstance();
    
    private TransactionService(){
        
    }
    
    /**
     * Singleton Transaction Service
     * @return TransactionService INSTANCE
     */
    public static TransactionService getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new TransactionService();
        }
        return INSTANCE;
    }
    
    private void appendTransactionData(TransactionModel transaction) {
        try (FileWriter pw = new FileWriter(this.transactionCsv, true)) {
            if (transaction == null) {
                System.out.println("Empty");
            }
                
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            
            String strDate = dateFormat.format(transaction.getCreateDate());

            pw.append(transaction.getAccountNumber());
            pw.append(",");
            pw.append(transaction.getNotes());
            pw.append(",");
            pw.append(transaction.getType());
            pw.append(",");
            pw.append(Float.toString(transaction.getAmount()));
            pw.append(",");
            pw.append(strDate);
            pw.append(",");
            pw.append(Float.toString(transaction.getBalance()));
            pw.append("\n");

            pw.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    /**
    * Deduct Balance Method, used for deducting balance when withdrawing fund
     * @param account
     * @param amount
     * @return boolean
    */
    public boolean deductBalance(AccountModel account, float amount, List<AccountModel> database) {
        float balance = account.getBalance();
        
        if (balance < amount) {
            System.out.println("---------------------------------------------------------");
            System.out.println("Insufficient balance $" + amount);
            return false;
        } else {
            balance -= amount;
            
            account.setBalance(balance);
            
            this.accountService.updateAccountData(database);
            
            TransactionModel transactionModel = new TransactionModel(account.getAccountNumber(), 
                    "Withdraw", "DB", amount, new Date(), balance);
            
            appendTransactionData(transactionModel);
            
            return true;
        }
    }
    
    /**
    * Fund Transfer Method, used on transfer fund transaction
     * @param account
     * @param destination
     * @param amount
     * @param database
     * @return boolean
    */
    public boolean fundTransfer(AccountModel account, AccountModel destination, float amount, 
            List<AccountModel> database) {
        
        float balance = account.getBalance();
        
        float destinationBalance = destination.getBalance();
        
        if (balance < amount) {
            System.out.println("---------------------------------------------------------");
            System.out.println("Insufficient balance $" + amount);
            return false;
        } else {
            balance -= amount;
            
            account.setBalance(balance);
            
            TransactionModel transactionModel = new TransactionModel(account.getAccountNumber(), 
                    "Transfer Fund to " + destination.getName(), "DB", amount, new Date(), 
                    balance);
            
            appendTransactionData(transactionModel);
            
            //Destination
            destinationBalance += amount;
            
            destination.setBalance(destinationBalance);
            
            transactionModel = new TransactionModel(destination.getAccountNumber(), 
                    "Transfer Fund from " + account.getName(), "CR", amount, new Date(), 
                    destinationBalance);
            
            appendTransactionData(transactionModel);
            
            this.accountService.updateAccountData(database);
            
            return true;
        }
    }
    
    /**
     * Get Transaction History
     * @return List of TransactionModel
     */
    public List<TransactionModel> getTransactionHistory(String accountNumber) {
        List<TransactionModel> transactionHistory = new ArrayList<>();
        
        // get lines of transaction from CSV file
        List<List<String>> lines = CsvHelper.readFromCSV(this.transactionCsv);
        
        // create Transaction object and add it to transaction history
        lines.stream().map((line) -> createTransactionObject(line)).forEach((transaction) -> {
            transactionHistory.add(transaction);
        });
        
        // predicate for filtering data by account number
        Predicate<TransactionModel> predicate = (data) -> 
                data.getAccountNumber().equals(accountNumber);
        
        // sort transaction history by date descending
        List<TransactionModel> sortTransactionDateDesc = transactionHistory.stream()
                .filter(predicate)
                .sorted(Comparator.comparing(TransactionModel::getCreateDate).reversed())
                .collect(Collectors.toList());

        // get top 10 transaction data
        List<TransactionModel> transaction = sortTransactionDateDesc.stream()
                .skip(0).limit(10)
                .collect(Collectors.toCollection(ArrayList::new));
        
        return transaction.stream()
                .sorted(Comparator.comparing(TransactionModel::getCreateDate))
                .collect(Collectors.toList());
    }
    
    /**
     * Create Transaction object from CSV line
     * @param metadata
     * @return TransactionModel
     */
    private TransactionModel createTransactionObject(List<String> metadata) {
        String accountNumber = metadata.get(0);
        
        String notes = metadata.get(1);
        
        String type = metadata.get(2);
        
        float amount = Float.parseFloat(metadata.get(3));
        
        Date createDate = new Date();
        try {
            createDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(metadata.get(4));
        } catch (ParseException ex) {
            System.out.println(TransactionService.class.getName() + ": " + ex);
        }
        
        float balance = Float.parseFloat(metadata.get(5));
        
        return new TransactionModel(accountNumber, notes, type, amount, createDate, balance);
    }
}
