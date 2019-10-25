package com.mitrais.atm.services.implement;

import com.mitrais.atm.models.Account;
import com.mitrais.atm.repository.implement.AccountRepository;
import com.mitrais.atm.services.IAccountService;
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
    public List<Account> getAccountList(String path) {
        return this.accountRepository.getAccountList(path);
    }
    
    /**
     * Get Duplicate Account
     * @param accounts
     * @return 
     */
    public List<Account> getDuplicateAccount(List<Account> accounts) {
        return accounts.stream()
            .collect(Collectors.groupingBy(Account::getAccountNumber))
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
    public void updateAccountData(List<Account> accounts) {
        this.accountRepository.updateAccountData(accounts);
    }
}
