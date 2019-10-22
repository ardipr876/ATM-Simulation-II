package com.mitrais.atm;

import com.mitrais.atm.models.AccountModel;
import com.mitrais.atm.screens.TransactionScreen;
import com.mitrais.atm.services.AccountService;
import com.mitrais.atm.services.LoginService;
import java.util.List;

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
        
        List<AccountModel> database = accountService.getAccountList();
        
        do {
            System.out.println("---------------------------------------------------------");
            System.out.println("Welcome To ATM Simulation - 2");
            System.out.println("---------------------------------------------------------");
            
            AccountModel account = loginService.login(database);
            
            if(account != null) {
                transactionScreen.transaction(account, database);
            }
            
        } while (true);
    }
}
