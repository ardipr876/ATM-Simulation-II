package com.mitrais.atm.services;

import com.mitrais.atm.models.Account;
import java.util.List;

/**
 * Interface LoginService
 * @author Ardi_PR876
 */
public interface ILoginService {
    Account login(List<Account> database);
}
