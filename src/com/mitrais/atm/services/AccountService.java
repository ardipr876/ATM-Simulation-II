package com.mitrais.atm.services;

import com.mitrais.atm.models.AccountModel;
import com.mitrais.atm.repository.AccountRepository;
import com.mitrais.atm.services.implement.IAccountService;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Account Service
 * @author Ardi_PR876
 */
public class AccountService implements IAccountService {
    private static AccountService INSTANCE;
    private final AccountRepository accountRepository = AccountRepository.getInstance();
    //private String appDir = System.getProperty("user.dir"); //this.appDir + "\\File\\account.csv";
    
    private AccountService() {
        
    }
    
    /**
     * Singleton Account Service
     * @return AccountService INSTANCE
     */
    public static AccountService getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new AccountService();
        }
        return INSTANCE;
    }
    
    /**
     * get account list from CSV file
     * @param path
     * @return List Account 
     */
    @Override
    public List<AccountModel> getAccountList(String path) {
        return this.accountRepository.getAccountList(path);
    }
    
    @Override
    public List<AccountModel> getDuplicateAccount(List<AccountModel> accounts) {
        return accounts.stream()
            .collect(Collectors.groupingBy(AccountModel::getAccountNumber))
            .entrySet().stream()
            .filter(e->e.getValue().size() > 1)
            .flatMap(e->e.getValue().stream())
            .collect(Collectors.toList());
    }
    
    /**
     * Update Account Data
     * @param accounts 
     */
    @Override
    public void updateAccountData(List<AccountModel> accounts) {
        this.accountRepository.updateAccountData(accounts);
    }
}
