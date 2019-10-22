package com.mitrais.atm.models;

/**
 * Validation Model
 * @author Ardi_PR876
 */
public class ValidationModel {
    private boolean valid;
    
    private String message;
    
    /**
     * Constructor Validation Model
     */
    public ValidationModel() {
        this.valid = false;
        this.message = "";
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
