/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mitrais.atm.services;

import com.mitrais.atm.models.AccountModel;
import com.mitrais.atm.models.TransactionModel;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
                
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            
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
}
