package com.mitrais.atm.screens;

import com.mitrais.atm.helpers.ValidationHelper;
import com.mitrais.atm.models.Account;
import com.mitrais.atm.helpers.ValidationResponse;
import com.mitrais.atm.screens.enums.ScreenEnum;
import com.mitrais.atm.services.implement.TransactionService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * Withdraw Screen
 * @author Ardi_PR876
 */
public class WithdrawScreen {
    private static WithdrawScreen INSTANCE;
    private final TransactionService transactionService = TransactionService.getInstance();
    
    private WithdrawScreen(){
        
    }
    
    /**
     * Singleton Withdraw Screen
     * @return WithdrawScreen INSTANCE
     */
    public static WithdrawScreen getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new WithdrawScreen();
        }
        return INSTANCE;
    }
    
    /**
     * Withdraw Screen
     * @param account
     * @param database
     * @return String
     */
    public String withdraw(Account account, List<Account> database) {
        // Choice on summary screen, go to Transaction page or Exit (Login page)
        String goToScreen = "";
        
        boolean succeed;
        
        float balance = account.getBalance();
        
        System.out.println("---------------------------------------------------------");
        System.out.println("Withdraw Screen");
        System.out.println("1. $10");
        System.out.println("2. $50");
        System.out.println("3. $100");
        System.out.println("4. Other");
        System.out.println("5. Back");
        System.out.print("Please choose option[5]: ");

        Scanner scannerOption = new Scanner(System. in);
        
        String option = scannerOption.nextLine().trim();
        
        switch (option) {
            case "1":
                succeed = this.transactionService.deductBalance(account, 10, database);
                
                if (succeed) {
                    balance -= 10;
                    goToScreen = summary(balance, 10);
                }
                break;
            case "2":
                succeed = this.transactionService.deductBalance(account, 50, database);
                
                if (succeed) {
                    balance -= 50;
                    goToScreen = summary(balance, 50);
                }
                break;
            case "3":
                succeed = this.transactionService.deductBalance(account, 100, database);
                
                if (succeed) {
                    balance -= 100;
                    goToScreen = summary(balance, 100);
                }
                break; 
            case "4":
                goToScreen = otherWithdraw(account, database);
                break;
        }
        
        return goToScreen;
    }
    
    /**
     * Other Withdraw Screen
     * @param account
     * @param database
     * @return 
     */
    public String otherWithdraw(Account account, List<Account> database) {
        String goToScreen = ScreenEnum.TRANSACTION.name();
        
        boolean succeed;
        
        float balance = account.getBalance();
        
        ValidationResponse validationModel;
        
        System.out.println("---------------------------------------------------------");
        System.out.println("Other Withdraw (Multiple $10)");
        System.out.print("Enter amount to withdraw: ");
        
        Scanner scannerAmount = new Scanner(System. in);
        
        String value = scannerAmount.nextLine().trim();
        
        validationModel = ValidationHelper.withdrawValidation(value);
        
        if (validationModel.isValid()) {
            float amount = Float.parseFloat(value);
            
            succeed = this.transactionService.deductBalance(account, amount, database);
            
            if (succeed) {
                goToScreen = summary(balance, amount);
            }
        } else {
            System.out.println("---------------------------------------------------------");
            System.out.println(validationModel.getMessage());
        }
        
        return goToScreen;
    }
    
    /**
     * Summary Screen
     * @param balance
     * @param amount
     * @return 
     */
    public String summary(float balance, float amount){
        LocalDateTime date = LocalDateTime.now();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
        
        String formatDateTime = date.format(formatter);
        
        System.out.println("---------------------------------------------------------");
        System.out.println("Summary");
        System.out.println("Date : " + formatDateTime);
        System.out.println("Withdraw : $" + amount);
        System.out.println("Balance : $" + balance);
        
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
