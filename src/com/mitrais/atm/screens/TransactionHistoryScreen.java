/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mitrais.atm.screens;

import com.mitrais.atm.models.AccountModel;
import com.mitrais.atm.models.TransactionModel;
import com.mitrais.atm.screens.enums.ScreenEnum;
import com.mitrais.atm.services.TransactionService;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Ardi_PR876
 */
public class TransactionHistoryScreen {
    private static TransactionHistoryScreen INSTANCE;
    private final TransactionService transactionService = TransactionService.getInstance();
    
    private TransactionHistoryScreen(){
        
    }
    
    /**
     * Singleton Transaction History Screen
     * @return TransactionHistoryScreen INSTANCE
     */
    public static TransactionHistoryScreen getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new TransactionHistoryScreen();
        }
        return INSTANCE;
    }
    
    public String history(AccountModel account){
        System.out.println("---------------------------------------------------------");
        System.out.println("Transaction History");
        System.out.println("Name \t\t : " + account.getName());
        System.out.println("Balance \t : $" + account.getBalance());
        System.out.println("");
        
        List<TransactionModel> transactionHistory = transactionService.getTransactionHistory();
        
        transactionHistory.stream().forEach(transaction -> {
            System.out.println("Note : " + transaction.getNotes());
        });
        
        System.out.println("");
        System.out.println("1. Transaction");
        System.out.println("2. Exit");
        System.out.print("Choose Option[2]: ");
        
        Scanner scannerOption = new Scanner(System. in);
        
        String option = scannerOption.nextLine().trim();
        
        if (option.equals("1")) {
            return ScreenEnum.TRANSACTION.name();
        } else {
            return ScreenEnum.LOGIN.name();
        }
    }
}
