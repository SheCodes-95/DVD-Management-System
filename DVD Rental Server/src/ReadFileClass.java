
/**
 * Write a description of class sales here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

import java.util.*;
import javax.swing.*;
import java.io.*;

public class ReadFileClass
{
    private double credit;
    private int custNumber;
    private String firstName, surname, phoneNumber, creditStr;
    boolean can;
    private Scanner input;
    private String inputLine;
    private ObjectOutputStream outFile;
    Customer customerObj;
    
    public void openFiles()
    {
        try
        {
            input = new Scanner(new File("txtfiles/Customers.txt"));
            outFile = new ObjectOutputStream(new FileOutputStream("Customers.ser"));
        }
        catch(FileNotFoundException fnf)
        {
            System.out.println("File not found...");
            System.exit(1);
        }
        catch(IOException ioe){
            System.out.println("Err opening output file");
        }
        
        
    }
    
    public void readAndWrite()
    {
        String[] arraySplit1 = new String[6];
        while(input.hasNext())
        {
            try
            {
                inputLine = input.nextLine();
                arraySplit1= inputLine.split("#");
                custNumber = Integer.parseInt(arraySplit1[0]);
                firstName = arraySplit1[1];
                surname = arraySplit1[2];
                phoneNumber = arraySplit1[3];
                creditStr = arraySplit1[4];                
                can = Boolean.parseBoolean(arraySplit1[5]);
                System.out.println(arraySplit1[0]+" "+arraySplit1[1]+" "+arraySplit1[2]+" "+arraySplit1[3]+" "+arraySplit1[4]+" "+arraySplit1[5]);
                credit = Double.parseDouble(creditStr);
                customerObj = new Customer(custNumber, firstName, surname, phoneNumber, credit, can);
                outFile.writeObject(customerObj);
            }
            catch(InputMismatchException ime)
            {
                System.out.print("ERROR...");
            }
            catch(IOException ioe){
                System.out.println("Err writing to file");
            }
             
        }
        
    }
    
    public void closeFiles()
    {
        try{
            input.close();  
            outFile.close();
        }
        catch(IOException ioe){
            System.out.println("Err closing file");
        }
        
    }
   
    
}