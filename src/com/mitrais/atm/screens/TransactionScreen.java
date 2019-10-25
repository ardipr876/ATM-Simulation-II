package com.mitrais.atm.screens;

import com.mitrais.atm.models.Account;
import com.mitrais.atm.screens.enums.ScreenEnum;
import java.util.List;
import java.util.Scanner;

/**
 * Transaction Screen
 * @author Ardi_PR876
 */
public class TransactionScreen {
    private static TransactionScreen INSTANCE;
    
    private TransactionScreen(){
        
    }
    
    /**
     * Singleton Transaction Screen
     * @return TransactionScreen INSTANCE
     */
    public static TransactionScreen getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new TransactionScreen();
        }
        return INSTANCE;
    }
    
    /**
        * Transaction Screen
        * @param account AccountModel
        * @param database List of AccountModel
    */
    public void transaction(Account account, List<Account> database) {
        WithdrawScreen withdrawScreen = WithdrawScreen.getInstance();
        FundTransferScreen fundTransferScreen = FundTransferScreen.getInstance();
        TransactionHistoryScreen historyScreen = TransactionHistoryScreen.getInstance();
                
        boolean repeat = true;
        
        do {
            System.out.println("---------------------------------------------------------");
            System.out.println(String.format("Welcome %s on ATM Transaction Screen", 
                    account.getName()));
            System.out.println("1. Withdraw");
            System.out.println("2. Fund Transfer");
            System.out.println("3. Transaction History");
            System.out.println("4. Exit");
            System.out.print("Please choose option[4]: ");
            
            Scanner scannerOption = new Scanner(System. in);
            
            String option = scannerOption.nextLine().trim();
            
            String goToScreen;
            
            if ("1".equals(option)) {
                goToScreen = withdrawScreen.withdraw(account, database);
                
                if(ScreenEnum.LOGIN.name().equals(goToScreen)) {
                    repeat = false;
                }
            } else if("2".equals(option)) {
                goToScreen = fundTransferScreen.fundTransfer(account, database);
                
                if(ScreenEnum.LOGIN.name().equals(goToScreen)) {
                    repeat = false;
                }
            } else if("3".equals(option)) {
                goToScreen = historyScreen.history(account);
                
                if(ScreenEnum.LOGIN.name().equals(goToScreen)) {
                    repeat = false;
                }
            } else if("4".equals(option) || option == null || option.isEmpty()) {
                repeat = false;
            }
            
        } while (repeat);
    }
}
