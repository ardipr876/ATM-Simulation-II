package com.mitrais.atm.helpers;

/**
 * Validation Model
 * @author Ardi_PR876
 */
public class ValidationResponse {
    private boolean valid;
    
    private String message;
    
    /**
     * Constructor Validation Model
     */
    public ValidationResponse() {
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
