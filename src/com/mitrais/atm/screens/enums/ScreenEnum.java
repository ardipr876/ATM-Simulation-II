package com.mitrais.atm.screens.enums;

/**
 * Screen Type
 * @author Ardi_PR876
 */
public enum ScreenEnum {
    TRANSACTION(99),
    LOGIN(98),
    FUNDTRANSFER(97);
    
    private final int code;
    
    ScreenEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
    
}
