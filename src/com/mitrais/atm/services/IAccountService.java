package com.mitrais.atm.services;

import com.mitrais.atm.models.Account;
import java.util.List;

/**
 * Interface AccountService
 * @author Ardi_PR876
 */
public interface IAccountService {
    List<Account> getAccountList(String path);
    void updateAccountData(List<Account> accounts);
//    boolean deductBalance(AccountModel account, float amount);
//    boolean fundTransfer(AccountModel account, AccountModel destination, float amount);
}
