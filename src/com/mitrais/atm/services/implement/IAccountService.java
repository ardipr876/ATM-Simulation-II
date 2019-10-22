package com.mitrais.atm.services.implement;

import com.mitrais.atm.models.AccountModel;
import java.util.List;

/**
 * Interface AccountService
 * @author Ardi_PR876
 */
public interface IAccountService {
    List<AccountModel> getAccountList();
//    boolean deductBalance(AccountModel account, float amount);
//    boolean fundTransfer(AccountModel account, AccountModel destination, float amount);
}