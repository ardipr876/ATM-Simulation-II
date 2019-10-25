/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mitrais.atm.helpers;

/**
 * Validation Helper
 * @author Ardi_PR876
 */
public class ValidationHelper {
    
    /**
        * Login Validation
        * @param value String
        * @param type String
        * @return ValidationModel
    */
    public static ValidationResponse loginValidation(String value, String type) {
        ValidationResponse validationModel = new ValidationResponse();
        
        if (value.length() == 6) {
            if (!onlyNumberValidation(value)) {
                validationModel.setValid(false);
                validationModel.setMessage(type + " should only contains numbers");
            } else {
                validationModel.setValid(true);
            }
        } else {
            validationModel.setValid(false);
            validationModel.setMessage(type + " should have 6 digits length");
        } 
        
        return validationModel;
    }
    
    /**
        * Withdraw Validation
        * @param value String
        * @return ValidationModel
    */
    public static ValidationResponse withdrawValidation(String value) {
        ValidationResponse validationModel = new ValidationResponse();
        
        if (!onlyNumberValidation(value)) {
            validationModel.setValid(false);
            validationModel.setMessage("Ammount should only contains numbers");
            return validationModel;
        }
        
        double amount = Double.parseDouble(value);
        
        if (amount > 1000) {
            validationModel.setValid(false);
            validationModel.setMessage("Maximum amount to withdraw is $1000");
        } else if(amount % 10 != 0) {
            validationModel.setValid(false);
            validationModel.setMessage("Invalid ammount");
        } else {
            validationModel.setValid(true);
        }
        
        return validationModel;
    }
    
    /**
        * Only Number Validation
        * @param value
        * @return boolean
    */
    public static boolean onlyNumberValidation(String value) {
        String regex = "[0-9]+";
        
        return value.matches(regex);
    }
}
