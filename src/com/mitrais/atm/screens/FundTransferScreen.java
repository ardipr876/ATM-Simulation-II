package com.mitrais.atm.screens;

import com.mitrais.atm.helpers.ValidationHelper;
import com.mitrais.atm.models.Account;
import com.mitrais.atm.helpers.ValidationResponse;
import com.mitrais.atm.screens.enums.ScreenEnum;
import com.mitrais.atm.services.implement.TransactionService;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Predicate;

/**
 * Fund Transfer Screen
 * @author Ardi_PR876
 */
public class FundTransferScreen {
    private static FundTransferScreen INSTANCE;
    private final TransactionService transactionService = TransactionService.getInstance();
    
    private FundTransferScreen(){
        
    }
    
    /**
     * Singleton Fund Transfer Screen
     * @return FundTransferScreen INSTANCE
     */
    public static FundTransferScreen getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new FundTransferScreen();
        }
        return INSTANCE;
    }
    
    /**
        * Fund Transfer Screen
        * @param account Account
        * @param database List of Account
        * @return goToScreen String
    */
    public String fundTransfer(Account account, List<Account> database) {
        boolean repeat = false;
        
        String goToScreen = ScreenEnum.TRANSACTION.name();
        
        boolean validDestination;

        do {
            System.out.println("---------------------------------------------------------");
            System.out.println("Fund Transfer");
            System.out.println("Please enter destination account and press enter to continue ");
            System.out.println("or press enter to go back to Transaction Screen: ");
            System.out.print("Destination: ");

            Scanner scannerDestination = new Scanner(System. in);
            
            String destination = scannerDestination.nextLine().trim();
            
            validDestination = ValidationHelper.onlyNumberValidation(destination);

            if (validDestination) {
                Predicate<Account> predicate = p -> p.getAccountNumber().equals(destination);
                
                Optional<Account> destinationAccount = database.stream().filter(predicate).
                        findFirst();

                try {
                    Account destinationAcc = destinationAccount.get();
                    
                    ValidationResponse amountValidation = amountScreen(account);
                    
                    if (amountValidation.isValid()) {
                        float amount = Float.parseFloat(amountValidation.getMessage());
                        
                        ValidationResponse confirmationValidation = confirmationScreen(account, 
                                destinationAcc, amount, database);
                        
                        boolean valid = confirmationValidation.isValid();
                    
                        goToScreen = confirmationValidation.getMessage();

                        if (valid && goToScreen.equals(ScreenEnum.FUNDTRANSFER.name())) {
                            repeat = true;
                        } else if (valid) {
                            repeat = false;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("--------------------------------------------------------");
                    System.out.println("Invalid account");
                    repeat = false;
                }
            } else if (!validDestination && !destination.isEmpty()) {
                System.out.println("---------------------------------------------------------");
                System.out.println("Invalid account");
                repeat = false;
            }
        } while (repeat);
        
        return goToScreen;
    }
    
    /**
        * Transfer Amount Screen
        * @param account
        * @return 
    */
    private ValidationResponse amountScreen(Account account) {
        ValidationResponse validationModel = new ValidationResponse();
        
        System.out.println("---------------------------------------------------------");
        System.out.println("Please enter transfer amount and press enter to continue");
        System.out.println("or press enter to go back to Transaction: ");
        System.out.print("Amount: ");
        
        Scanner scannerAmount = new Scanner(System. in);

        String strAmount = scannerAmount.nextLine().trim();

        boolean validAmount = ValidationHelper.onlyNumberValidation(strAmount);
        
        float balance = account.getBalance();

        if (validAmount) {
            float amount = Float.parseFloat(strAmount);
            
            if (amount > 1000) {
                System.out.println("---------------------------------------------------------");
                System.out.println("Maximum amount to withdraw is $1000");
            } else if (amount < 1) {
                System.out.println("---------------------------------------------------------");
                System.out.println("Minimum amount to withdraw is $1");
            } else if (amount > balance) {
                System.out.println("---------------------------------------------------------");
                System.out.println("Insufficient balance $" + amount);
            } else {
                validationModel.setValid(true);
                validationModel.setMessage(strAmount);
            }
        } else {
            System.out.println("---------------------------------------------------------");
            System.out.println("Invalid Amount: should only contains numbers");
        }
        
        return validationModel;
    }
    
    /**
        * Transfer Confirmation Screen
        * @param account
        * @param destinationAcc
        * @param amount
        * @return ValidationHelper
    */
    private ValidationResponse confirmationScreen(Account account, Account destinationAcc, 
            float amount, List<Account> database) {
        
        ValidationResponse validationModel = new ValidationResponse();
        
        String destinationNumber = destinationAcc.getAccountNumber();
        
        String destinationName = destinationAcc.getName();
        
        String refNumber = getRandomString();
        
        System.out.println("---------------------------------------------------------");
        System.out.print("Reference Number: " + refNumber);
        System.out.println(" (This is an autogenerated number)");
        System.out.print("press enter to continue ");
        
        String scannerRefNumber = new Scanner(System. in).nextLine().trim();

        System.out.println("---------------------------------------------------------");
        System.out.println("Transfer Confirmation");
        System.out.println("Destination Account \t : " + destinationNumber);
        System.out.println("Destination Name \t : " + destinationName);
        System.out.println("Transfer Amount \t : $" + amount);
        System.out.println("Reference Number \t : " + refNumber);
        System.out.println("");
        System.out.println("1. Confirm Transaction");
        System.out.println("2. Cancel Transaction");
        System.out.print("Choose Option[2]: ");
        
        String scannerConfirmTransaction = new Scanner(System. in).nextLine().trim();

        if (scannerConfirmTransaction.equals("1")) {
            boolean transferSucceed = this.transactionService.fundTransfer(account, destinationAcc, 
                    amount, database);

            if (transferSucceed) {
                String goToScreen = summary(account, destinationAcc, amount, refNumber);
                
                validationModel.setValid(true);
                validationModel.setMessage(goToScreen);
            }
        } else if (scannerConfirmTransaction.equals("2") || scannerConfirmTransaction.isEmpty()) {
            String goToScreen = ScreenEnum.FUNDTRANSFER.name();
            validationModel.setValid(true);
            validationModel.setMessage(goToScreen);
        }
        
        return validationModel;
    }
    
    /**
        * Summary Screen
        * @param account Account
        * @param destination Account
        * @param amount Double
        * @param refNumber String
        * @return ScreenEnum String
    */
    private String summary(Account account, Account destination, float amount, 
            String refNumber) {
        
        String destinationNumber = destination.getAccountNumber();
        
        String destinationName = destination.getName();
        
        float balance = account.getBalance();

        System.out.println("---------------------------------------------------------");
        System.out.println("Fund Transfer Summary");
        System.out.println("Destination Account \t : " + destinationNumber);
        System.out.println("Destination Name \t : " + destinationName);
        System.out.println("Transfer Amount \t : $" + amount);
        System.out.println("Reference Number \t : " + refNumber);
        System.out.println("Balance \t\t : $" + balance);
        System.out.println("");
        System.out.println("1. Transaction");
        System.out.println("2. Exit");
        System.out.print("Choose Option[2]: ");
        
        String scannerConfirmTransaction = new Scanner(System. in).nextLine().trim();
        
        if (scannerConfirmTransaction.equals("1")) {
            return ScreenEnum.TRANSACTION.name();
        } else {
            return ScreenEnum.LOGIN.name();
        }
    }
    
    /**
        * Get 6 Digits Random String
        * @return Random String
    */
    private String getRandomString() {
        SecureRandom random = new SecureRandom();
        
        String DATA_FOR_RANDOM_STRING = "0123456789";
        
        StringBuilder sb = new StringBuilder(6);
        
        for (int i = 0; i < 6; i++) {
            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);
            sb.append(rndChar);
        }
        
        return sb.toString();
    }
}
