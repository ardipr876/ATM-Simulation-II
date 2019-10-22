package com.mitrais.atm.services.implement;

import com.mitrais.atm.models.AccountModel;
import java.util.List;

/**
 * Interface LoginService
 * @author Ardi_PR876
 */
public interface ILoginService {
    AccountModel login(List<AccountModel> database);
}
