/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mitrais.atm.repository;

import com.mitrais.atm.models.Transaction;
import java.util.List;

/**
 * Interface Transaction Repository
 * @author Ardi_PR876
 */
public interface ITransactionRepository {
    List<Transaction> getTransactionHistory(String accountNumber, String csvPath);
    void appendTransactionData(Transaction transaction, String csvPath);
}
