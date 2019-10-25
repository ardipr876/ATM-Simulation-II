package com.mitrais.atm.services.implement;

import com.mitrais.atm.helpers.CsvHelper;
import com.mitrais.atm.models.Account;
import com.mitrais.atm.models.Transaction;
import com.mitrais.atm.repository.implement.TransactionRepository;
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
    public boolean deductBalance(Account account, float amount, List<Account> database) {
        float balance = account.getBalance();
        
        if (balance < amount) {
            System.out.println("---------------------------------------------------------");
            System.out.println("Insufficient balance $" + amount);
            return false;
        } else {
            balance -= amount;
            
            account.setBalance(balance);
            
            this.accountService.updateAccountData(database);
            
            Transaction transactionModel = new Transaction(account.getAccountNumber(), 
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
    public boolean fundTransfer(Account account, Account destination, float amount, 
            List<Account> database) {
        
        float balance = account.getBalance();
        
        float destinationBalance = destination.getBalance();
        
        if (balance < amount) {
            System.out.println("---------------------------------------------------------");
            System.out.println("Insufficient balance $" + amount);
            return false;
        } else {
            balance -= amount;
            
            account.setBalance(balance);
            
            Transaction transactionModel = new Transaction(account.getAccountNumber(), 
                    "Transfer Fund to " + destination.getName(), "DB", amount, new Date(), 
                    balance);
            
            this.transactionRepo.appendTransactionData(transactionModel, this.transactionCsv);
            
            //Destination
            destinationBalance += amount;
            
            destination.setBalance(destinationBalance);
            
            transactionModel = new Transaction(destination.getAccountNumber(), 
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
    public List<Transaction> getTransactionHistory(String accountNumber) {
        List<Transaction> transactions;
        transactions = this.transactionRepo.getTransactionHistory(accountNumber, this.transactionCsv);
        
        // predicate for filtering data by account number
        Predicate<Transaction> predicate = (data) -> 
                data.getAccountNumber().equals(accountNumber);
        
        // sort transaction history by date descending
        List<Transaction> sortTransactionDateDesc = transactions.stream()
                .filter(predicate)
                .sorted(Comparator.comparing(Transaction::getCreateDate).reversed())
                .collect(Collectors.toList());

        // get top 10 transaction data
        return sortTransactionDateDesc.stream()
                .skip(0).limit(10)
                .collect(Collectors.toCollection(ArrayList::new));
        
//        return transaction.stream()
//                //.sorted(Comparator.comparing(TransactionModel::getCreateDate))
//                .collect(Collectors.toList());
    }
}
