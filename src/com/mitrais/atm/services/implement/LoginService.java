package com.mitrais.atm.services.implement;

import com.mitrais.atm.helpers.ValidationHelper;
import com.mitrais.atm.models.Account;
import com.mitrais.atm.helpers.ValidationResponse;
import com.mitrais.atm.services.ILoginService;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Predicate;

/**
 * Login Service
 * @author Ardi_PR876
 */
public class LoginService implements ILoginService {
    private static LoginService INSTANCE;
    
    private LoginService(){
        
    }
    
    /**
     * Singleton Login Service
     * @return LoginService INSTANCE
     */
    public static LoginService getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new LoginService();
        }
        return INSTANCE;
    }
    
    /**
        * Login Process
        * @param database List of Account
        * @return account Account
    */
    @Override
    public Account login(List<Account> database) {
        Account account = null;
        
        boolean validAccountNumber;
        
        String accountNumber;
        
        String pin;
        
        boolean validPin;
        
        ValidationResponse validationModel;
        
        do {
            System.out.print("Enter Account Number: ");
            Scanner scannerAccountNumber = new Scanner(System. in);
            
            accountNumber = scannerAccountNumber.nextLine();
            
            validationModel = ValidationHelper.loginValidation(accountNumber, "Account Number");
            
            validAccountNumber = validationModel.isValid();
            
            if (!validAccountNumber) {
                System.out.println(validationModel.getMessage());
            }
        } while (!validAccountNumber);
        
        do {
            System.out.print("Enter PIN: ");
            Scanner scannerPin = new Scanner(System. in);
            
            pin = scannerPin.nextLine();
            
            validationModel = ValidationHelper.loginValidation(pin, "PIN");
            
            validPin = validationModel.isValid();
            
            if (!validPin) {
                System.out.println(validationModel.getMessage());
            }
        } while (!validPin);
        
        String finalAccountNumber = accountNumber;
        
        String finalPin = pin;
        
        Predicate<Account> findAccount = p -> 
                p.getAccountNumber().equals(finalAccountNumber) && p.getPin().equals(finalPin);
        
        Optional<Account> matchingAccount = database.stream().filter(findAccount).findFirst();
        
        if (matchingAccount.isPresent()) {
            account = matchingAccount.get();
        } else {
            System.out.println("Invalid Account Number/PIN");
        }
        
        return account;
    }
}
