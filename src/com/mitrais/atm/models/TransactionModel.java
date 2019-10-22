/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mitrais.atm.models;

import java.util.Date;

/**
 *
 * @author Ardi_PR876
 */
public class TransactionModel {
    private String AccountNumber;
    
    private String Notes;
    
    private String Type;
    
    private float Amount;
    
    private Date CreateDate;
    
    private float Balance;
    
    public TransactionModel(String AccountNumber, String Notes, String Type, float Amount,
            Date CreateDate, float Balance){
        
        this.AccountNumber = AccountNumber;
        this.Notes = Notes;
        this.Type = Type;
        this.Amount = Amount;
        this.CreateDate = CreateDate;
        this.Balance = Balance;
    }

    public String getAccountNumber() {
        return AccountNumber;
    }

    public void setAccountNumber(String AccountNumber) {
        this.AccountNumber = AccountNumber;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String Notes) {
        this.Notes = Notes;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public float getAmount() {
        return Amount;
    }

    public void setAmount(float Amount) {
        this.Amount = Amount;
    }

    public Date getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(Date CreateDate) {
        this.CreateDate = CreateDate;
    }

    public float getBalance() {
        return Balance;
    }

    public void setBalance(float Balance) {
        this.Balance = Balance;
    }
    
    
}
