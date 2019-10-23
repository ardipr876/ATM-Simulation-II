package com.mitrais.atm;

import com.mitrais.atm.helpers.CsvHelper;
import com.mitrais.atm.models.AccountModel;
import com.mitrais.atm.screens.ChangeFilePathScreen;
import com.mitrais.atm.screens.TransactionScreen;
import com.mitrais.atm.services.AccountService;
import com.mitrais.atm.services.LoginService;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

/**
 * Main Class
 * @author Ardi_PR876
 */
public class Main {
    
    /**
     * Main Method
     * @param args 
     */
    public static void main(String[] args) {
        AccountService accountService = AccountService.getInstance();
        
        LoginService loginService = LoginService.getInstance();
        
        TransactionScreen transactionScreen = TransactionScreen.getInstance();
        
        ChangeFilePathScreen changeFilePathScreen = ChangeFilePathScreen.getInstance();

        do {
            System.out.println("---------------------------------------------------------");
            System.out.println("Welcome To ATM Simulation - 2");
            System.out.println("---------------------------------------------------------");
            System.out.println("Choose account list source");
            System.out.println("1. Use Existing CSV File");
            System.out.println("2. Use Your CSV File");
            System.out.println("");
            System.out.print("Your Choose (1 / 2) : ");
            
            Scanner scannerOption = new Scanner(System. in);
            
            String option = scannerOption.nextLine().trim();
            
            if ("1".equals(option)) {
                System.out.println("---------------------------------------------------------");
                List<AccountModel> database = accountService.getAccountList("");
                
                AccountModel account = loginService.login(database);
            
                if(account != null) {
                    transactionScreen.transaction(account, database);
                }
            } else if ("2".equals(option)){
                changeFilePathScreen.changeFilePath();
            }
            
        } while (true);
    }
    
    
}
