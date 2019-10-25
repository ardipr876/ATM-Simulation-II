package com.mitrais.atm.models;

import java.util.Date;

/**
 * Transaction Model
 * @author Ardi_PR876
 */
public class Transaction {
    private String accountNumber;
    
    private String notes;
    
    private String type;
    
    private float amount;
    
    private Date createDate;
    
    private float balance;
    
    public Transaction(String accountNumber, String notes, String type, float amount,
            Date createDate, float balance){
        
        this.accountNumber = accountNumber;
        this.notes = notes;
        this.type = type;
        this.amount = amount;
        this.createDate = createDate;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
    
    
}
