/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mitrais.atm.repository.implement;

import com.mitrais.atm.models.TransactionModel;
import java.util.List;

/**
 * Interface Transaction Repository
 * @author Ardi_PR876
 */
public interface ITransactionRepository {
    List<TransactionModel> getTransactionHistory(String accountNumber, String csvPath);
    void appendTransactionData(TransactionModel transaction, String csvPath);
}
