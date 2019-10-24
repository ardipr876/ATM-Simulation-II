package com.mitrais.atm.repository;

import com.mitrais.atm.helpers.CsvHelper;
import com.mitrais.atm.models.TransactionModel;
import com.mitrais.atm.repository.implement.ITransactionRepository;
import com.mitrais.atm.screens.enums.FileTypeEnum;
import com.mitrais.atm.services.TransactionService;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Transaction Repository
 * @author Ardi_PR876
 */
public class TransactionRepository implements ITransactionRepository {
    private static TransactionRepository INSTANCE;
    
    private TransactionRepository() {
        
    }
    
    public static TransactionRepository getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new TransactionRepository();
        }
        return INSTANCE;
    }
    
    /**
     * 
     * @param accountNumber
     * @param csvPath
     * @return 
     */
    @Override
    public List<TransactionModel> getTransactionHistory(String accountNumber, String csvPath) {
        List<TransactionModel> transactionHistory = new ArrayList<>();
        
        List<List<String>> lines;
        lines = CsvHelper.readFromCSV(csvPath, FileTypeEnum.TRANSACTION.name());
        
        // create Transaction object and add it to transaction history
        lines.stream().map((line) -> createTransactionObject(line)).forEach((transaction) -> {
            transactionHistory.add(transaction);
        });
        
        return transactionHistory;
    }
    
    /**
     * Create Transaction object from CSV line
     * @param metadata
     * @return TransactionModel
     */
    private TransactionModel createTransactionObject(List<String> metadata) {
        String accountNumber = metadata.get(0);
        
        String notes = metadata.get(1);
        
        String type = metadata.get(2);
        
        float amount = Float.parseFloat(metadata.get(3));
        
        Date createDate = new Date();
        try {
            createDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a").parse(metadata.get(4));
        } catch (ParseException ex) {
            System.out.println(TransactionService.class.getName() + ": " + ex);
        }
        
        float balance = Float.parseFloat(metadata.get(5));
        
        return new TransactionModel(accountNumber, notes, type, amount, createDate, balance);
    }
    
    /**
     * Append Transaction Data to transaction.CSV
     * @param transaction 
     * @param csvPath 
     */
    @Override
    public void appendTransactionData(TransactionModel transaction, String csvPath) {
        try {
            File file = new File(csvPath);
            FileWriter fw;
            
            if (file.exists())
            {
                fw = new FileWriter(file, true); //if file exists append to file. Works fine.
            }
            else
            {
                file.createNewFile();
                fw = new FileWriter(file);
                
                fw.append("Account Number");
                fw.append(",");
                fw.append("Notes");
                fw.append(",");
                fw.append("Type");
                fw.append(",");
                fw.append("Amount");
                fw.append(",");
                fw.append("Create Date");
                fw.append(",");
                fw.append("Balance");
                fw.append("\n");
            }

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
            
            String strDate = dateFormat.format(transaction.getCreateDate());

            fw.append(transaction.getAccountNumber());
            fw.append(",");
            fw.append(transaction.getNotes());
            fw.append(",");
            fw.append(transaction.getType());
            fw.append(",");
            fw.append(Float.toString(transaction.getAmount()));
            fw.append(",");
            fw.append(strDate);
            fw.append(",");
            fw.append(Float.toString(transaction.getBalance()));
            fw.append("\n");

            fw.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
