
/**
 * Write a description of class sales here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

import java.util.*;
import javax.swing.*;
import java.io.*;

public class ReadDvdFileClass
{
    private int dvdNumber, category;
    private String title, categoryStr, releaseStr, availableStr;
    private boolean release, available;
    private Scanner input;
    private String inputLine;
    private ObjectOutputStream outFile;
    DVD DvdObj;
    
    public void openFiles()
    {
        try
        {
            input = new Scanner(new File("txtfiles/Movies.txt"));
            outFile = new ObjectOutputStream(new FileOutputStream("Movies.ser"));
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
        String[] arraySplit1 = new String[4];
        while(input.hasNext())
        {
            try
            {
                inputLine = input.nextLine();
                arraySplit1= inputLine.split("#");
                dvdNumber = Integer.parseInt(arraySplit1[0]);
                title = arraySplit1[1];
                categoryStr = arraySplit1[2];
                releaseStr = arraySplit1[3];
                availableStr = arraySplit1[4];
                System.out.println(arraySplit1[0]+" "+arraySplit1[1]+" "+arraySplit1[2]+" "+arraySplit1[3]+" "+arraySplit1[4]); 
                release = Boolean.parseBoolean(releaseStr);
                available = Boolean.parseBoolean(availableStr);
                category = Integer.parseInt(categoryStr);
                DvdObj = new DVD(dvdNumber, title, category, release, available);
                outFile.writeObject(DvdObj);              
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