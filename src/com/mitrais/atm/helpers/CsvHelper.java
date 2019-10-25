package com.mitrais.atm.helpers;

import com.mitrais.atm.screens.enums.FileTypeEnum;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * CSV Helper
 * @author Ardi_PR876
 */
public class CsvHelper {
    /**
     * Read CSV file
     * @param fileName
     * @param type
     * @return List of lines
     */
    public static List<List<String>> readFromCSV(String fileName, String type) {
        List<List<String>> data = new ArrayList<>();
        int lineCounter = 0;
        String line;
        
        try {
            File file = new File(fileName);

            if (!file.exists() && type.equals(FileTypeEnum.TRANSACTION.name())){
                file.createNewFile();
                FileWriter fw = new FileWriter(file);
                
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
                fw.flush();
            }
            
            BufferedReader br = new BufferedReader(new FileReader(fileName));
                    
            // loop until all lines are read
            while ((line = br.readLine()) != null) {
                if (lineCounter > 0) {
                    // use string.split to load a string array with the values from
                    // each line of
                    // the file, using a comma as the delimiter
                    String[] attributes = line.split(",");

                    data.add(Arrays.asList(attributes));
                }
                lineCounter++;
            }
        } catch (IOException ioe) {
            System.out.println("ERROR! File Not Found " + fileName);
            System.exit(0);
        }

        return data;
    }
    
    /**
     * Get Property Value
     * @param type
     * @return 
     */
    public static String getPropValue(String type) {
        String result = "";
        
        String propFileName = "config.properties";
        
        try (InputStream input = CsvHelper.class.getClassLoader().getResourceAsStream(propFileName)) {
            Properties prop = new Properties();

            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
            }

            //load a properties file from class path, inside static method
            prop.load(input);

            result = prop.getProperty(type);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return result;
    }
    
    /**
     * Set Property Value
     * @param map
     * @param propFileName
     */
    public static void setPropValue(Map<String, String> map, String propFileName){
        try (OutputStream output = new FileOutputStream(propFileName)) {

            Properties prop = new Properties();

            // set the properties value
            map.entrySet().stream().forEach((entry) -> {
                prop.setProperty(entry.getKey(), entry.getValue());
            });
            
            // save properties to project root folder
            prop.store(output, null);

            System.out.println(prop);

        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
