package com.mitrais.atm.services;

import com.mitrais.atm.helpers.CsvHelper;
import com.mitrais.atm.models.AccountModel;
import com.mitrais.atm.models.TransactionModel;
import com.mitrais.atm.repository.TransactionRepository;
import java.io.File;
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
    private final TransactionRepository transactionRepo = TransactionRepository.getInstance();
    
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
            
            // add transaction record
            this.transactionRepo.appendTransactionData(transactionModel, this.transactionCsv);
            
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
            
            this.transactionRepo.appendTransactionData(transactionModel, this.transactionCsv);
            
            //Destination
            destinationBalance += amount;
            
            destination.setBalance(destinationBalance);
            
            transactionModel = new TransactionModel(destination.getAccountNumber(), 
                    "Transfer Fund from " + account.getName(), "CR", amount, new Date(), 
                    destinationBalance);
            
            this.transactionRepo.appendTransactionData(transactionModel, this.transactionCsv);
            
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
        List<TransactionModel> transactions;
        transactions = this.transactionRepo.getTransactionHistory(accountNumber, this.transactionCsv);
        
        // predicate for filtering data by account number
        Predicate<TransactionModel> predicate = (data) -> 
                data.getAccountNumber().equals(accountNumber);
        
        // sort transaction history by date descending
        List<TransactionModel> sortTransactionDateDesc = transactions.stream()
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
}
