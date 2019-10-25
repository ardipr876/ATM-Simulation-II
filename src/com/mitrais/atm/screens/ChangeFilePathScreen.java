/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mitrais.atm.screens;

import com.mitrais.atm.helpers.CsvHelper;
import com.mitrais.atm.models.Account;
import com.mitrais.atm.services.implement.AccountService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Change File Path Screen
 * @author Ardi_PR876
 */
public class ChangeFilePathScreen {
    private static ChangeFilePathScreen INSTANCE;
    private final AccountService accountService = AccountService.getInstance();
    
    private ChangeFilePathScreen(){
        
    }
    
    /**
     * Singleton Change File PathScreen
     * @return ChangeFilePathScreen INSTANCE
     */
    public static ChangeFilePathScreen getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new ChangeFilePathScreen();
        }
        return INSTANCE;
    }
    
    /**
     * Change File Path Screen
     */
    public void changeFilePath() {
        System.out.println("---------------------------------------------------------");
        System.out.println("Change File Path, Please give your file name with 'account.csv'");
        System.out.println("You can see the template on 'ATM-Simulation-II\\File\\account.csv'");
        System.out.println("");
        System.out.print("Input Your Path (folder where the account.csv file is located): ");
        
        Scanner scannerOption = new Scanner(System.in);
        
        String input = scannerOption.nextLine().trim();
        
        List<Account> listAccount;
        listAccount = this.accountService.getAccountList(input + "\\account.csv");
        
        List<Account> duplicates = this.accountService.getDuplicateAccount(listAccount);
        
        if (!duplicates.isEmpty()) {
            System.out.println("Duplicate!");
            
            duplicates.stream().forEach(account -> 
                    System.out.println(account.getName() + " : " + account.getAccountNumber()));
            
            System.out.print("Press any key to continue. . . ");
            
            input = new Scanner(System.in).nextLine().trim();
        } else {
            String configFilePath = System.getProperty("user.dir") + "\\src\\config.properties";
            
            Map<String, String> map = new HashMap<String, String>();
            
            map.put("directoryCsv", input);
            
            CsvHelper.setPropValue(map, configFilePath);
            
            System.out.print("Press any key to continue. . . ");
            
            input = new Scanner(System.in).nextLine().trim();
        }
    }
}
