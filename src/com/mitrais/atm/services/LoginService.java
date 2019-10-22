package com.mitrais.atm.services;

import com.mitrais.atm.helpers.ValidationHelper;
import com.mitrais.atm.models.AccountModel;
import com.mitrais.atm.models.ValidationModel;
import com.mitrais.atm.services.implement.ILoginService;
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
    public AccountModel login(List<AccountModel> database) {
        AccountModel account = null;
        
        boolean validAccountNumber;
        
        String accountNumber;
        
        String pin;
        
        boolean validPin;
        
        ValidationModel validationModel;
        
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
        
        Predicate<AccountModel> findAccount = p -> 
                p.getAccountNumber().equals(finalAccountNumber) && p.getPin().equals(finalPin);
        
        Optional<AccountModel> matchingAccount = database.stream().filter(findAccount).findFirst();
        
        if (matchingAccount.isPresent()) {
            account = matchingAccount.get();
        } else {
            System.out.println("Invalid Account Number/PIN");
        }
        
        return account;
    }
}
