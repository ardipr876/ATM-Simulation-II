package com.mitrais.atm.helpers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * CSV Helper
 * @author Ardi_PR876
 */
public class CsvHelper {
    /**
     * Read CSV file
     * @param fileName
     * @return List of lines
     */
    public static List<List<String>> readFromCSV(String fileName) {
        List<List<String>> data = new ArrayList<>();
        int lineCounter = 0;
        String line;
        
        // create an instance of BufferedReader
        // using try with resource
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))){
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
            ioe.printStackTrace();
        }

        return data;
    }
}
