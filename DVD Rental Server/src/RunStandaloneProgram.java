import java.io.IOException;
import java.sql.SQLException;

public class RunStandaloneProgram
{
    public static void main(String[] args)
    {
        try
        {
            StandaloneProgram runSP = new StandaloneProgram();
            runSP.connectToDatabase();
            System.out.println("connected to dbase.");
            runSP.makeDvdTable();
            System.out.println("dvd table has been created.");
            runSP.makeCustomerTable();
            System.out.println("customer table has been created.");
            runSP.makeRentalTable();
            System.out.println("rental table has been created.");
            runSP.readAndInsertDvdData();
            System.out.println("dvd table data has been inserted.");
            runSP.readAndInsertCustomerData();
            System.out.println("customer table data has been inserted.");
            runSP.readAndInsertRentalData();
            System.out.println("rental table data has been inserted.");
        }
        catch (SQLException | IOException | ClassNotFoundException e)
        {
            System.out.println(e);
        }
    }
}
