import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ReadRentalFileClass
{
    private int rentalNumber, custNumber, dvdNumber;
    private String dateRented, dateReturned;
    private double totalPenaltyCost;
    private Scanner input;
    private String inputLine;
    private ObjectOutputStream outFile;
    Rental Rentalobj;

    public void openFiles()
    {
        try
        {
            input = new Scanner(new File("txtfiles/Rentals.txt"));
            outFile = new ObjectOutputStream(new FileOutputStream("rental.ser"));
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
                rentalNumber = Integer.parseInt(arraySplit1[0]);
                dateRented = arraySplit1[1];
                dateReturned = arraySplit1[2];
                totalPenaltyCost = Double.parseDouble(arraySplit1[3]);
                custNumber = Integer.parseInt(arraySplit1[4]);
                dvdNumber = Integer.parseInt(arraySplit1[5]);
                Rentalobj = new Rental(rentalNumber, dateRented, dateReturned, custNumber, dvdNumber);
                outFile.writeObject(Rentalobj);
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
