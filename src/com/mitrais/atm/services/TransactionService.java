package com.mitrais.atm.services;

import com.mitrais.atm.helpers.CsvHelper;
import com.mitrais.atm.models.AccountModel;
import com.mitrais.atm.models.TransactionModel;
import java.io.File;
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
import java.util.stream.Collectors;

/**
 * Transaction Service
 * @author Ardi_PR876
 */
public class TransactionService {
    private static TransactionService INSTANCE;
    //private final String appDir = System.getProperty("user.dir");
    private final String transactionCsv;
    private final AccountService accountService = AccountService.getInstance();
    
    private TransactionService(){
        this.transactionCsv = CsvHelper.getPropValue("directoryCsv") + 
                File.separator + "transaction.csv";
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
    
    /**
     * Append Transaction Data to transaction.CSV
     * @param transaction 
     */
    private void appendTransactionData(TransactionModel transaction) {
        try {
            File file = new File(this.transactionCsv);
            FileWriter fw;
            
            if (file.exists())
            {
                fw = new FileWriter(file, true); //if file exists append to file. Works fine.
            }
            else
            {
                file.createNewFile();
                fw = new FileWriter(file);
                
                fw.append("Account Number");
                fw.append(",");
                fw.append("Notes");
                fw.append(",");
                fw.append("Type");
                fw.append(",");
                fw.append("Amount");
                fw.append(",");
                fw.append("Create Date");
                fw.append(",");
                fw.append("Balance");
                fw.append("\n");
            }

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
            
            String strDate = dateFormat.format(transaction.getCreateDate());

            fw.append(transaction.getAccountNumber());
            fw.append(",");
            fw.append(transaction.getNotes());
            fw.append(",");
            fw.append(transaction.getType());
            fw.append(",");
            fw.append(Float.toString(transaction.getAmount()));
            fw.append(",");
            fw.append(strDate);
            fw.append(",");
            fw.append(Float.toString(transaction.getBalance()));
            fw.append("\n");

            fw.flush();
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
     * @param accountNumber
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
            createDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a").parse(metadata.get(4));
        } catch (ParseException ex) {
            System.out.println(TransactionService.class.getName() + ": " + ex);
        }
        
        float balance = Float.parseFloat(metadata.get(5));
        
        return new TransactionModel(accountNumber, notes, type, amount, createDate, balance);
    }
}
