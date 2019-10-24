/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mitrais.atm.repository.implement;

import com.mitrais.atm.models.AccountModel;
import java.util.List;

/**
 * Interface Account Repository
 * @author Ardi_PR876
 */
public interface IAccountRepository {
    List<AccountModel> getAccountList(String path);
    void updateAccountData(List<AccountModel> accounts);
}
