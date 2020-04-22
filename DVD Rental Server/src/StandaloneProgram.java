import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StandaloneProgram
{
    private Connection connection;
    private PreparedStatement ps;

    public StandaloneProgram()
    {

    }

    public void connectToDatabase() throws SQLException
    {
        connection = DriverManager.getConnection("jdbc:ucanaccess://dbase/dbase.accdb");
    }

    public void makeDvdTable() throws SQLException
    {
        ps = connection.prepareStatement("CREATE TABLE dvd (dvd_number INT NOT NULL PRIMARY KEY, title VARCHAR(80), category VARCHAR(80), price CURRENCY, new_release BOOLEAN, available_for_rent BOOLEAN)");
        ps.executeUpdate();
    }

    public void makeCustomerTable() throws SQLException
    {
        ps = connection.prepareStatement("CREATE TABLE customer (cust_number INT NOT NULL PRIMARY KEY, first_name VARCHAR(80), surname VARCHAR(80), phone_number VARCHAR(10), credit CURRENCY, can_rent BOOLEAN)");
        ps.executeUpdate();
    }

    public void makeRentalTable() throws SQLException
    {
        ps = connection.prepareStatement("CREATE TABLE rental (rental_number INT NOT NULL PRIMARY KEY, date_rented VARCHAR(10), date_returned VARCHAR(10), cust_number INT NOT NULL FOREIGN KEY REFERENCES customer(cust_number), dvd_number INT NOT NULL FOREIGN KEY REFERENCES dvd(dvd_number), total_penalty_cost CURRENCY)");
        ps.executeUpdate();
    }

    public void readAndInsertDvdData() throws SQLException, IOException, ClassNotFoundException
    {
        try
        {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream("Movies.ser"));

            while (true)
            {
                DVD dvd = (DVD)input.readObject();

                ps = connection.prepareStatement("INSERT INTO dvd (dvd_number, title, category, price, new_release, available_for_rent) VALUES (?,?,?,?,?,?)");
                ps.setInt(1, dvd.getDvdNumber());
                ps.setString(2, dvd.getTitle());
                ps.setString(3, dvd.getCategory());
                ps.setDouble(4, dvd.getPrice());
                ps.setBoolean(5, dvd.isNewRelease());
                ps.setBoolean(6, dvd.isAvailable());
                ps.executeUpdate();
            }
        }
        catch (EOFException e)
        {

        }
    }

    public void readAndInsertCustomerData() throws SQLException, IOException, ClassNotFoundException
    {
        try
        {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream("Customers.ser"));

            while (true)
            {
                Customer customer = (Customer)input.readObject();

                ps = connection.prepareStatement("INSERT INTO customer (cust_number, first_name, surname, phone_number, credit, can_rent) VALUES (?,?,?,?,?,?)");
                ps.setInt(1, customer.getCustNumber());
                ps.setString(2, customer.getName());
                ps.setString(3, customer.getSurname());
                ps.setString(4, customer.getPhoneNum());
                ps.setDouble(5, customer.getCredit());
                ps.setBoolean(6, customer.canRent());
                ps.executeUpdate();
            }
        }
        catch (EOFException e)
        {

        }
    }

    public void readAndInsertRentalData() throws SQLException, IOException, ClassNotFoundException
    {
        try
        {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream("rental.ser"));

            while (true)
            {
                Rental rental = (Rental)input.readObject();

                ps = connection.prepareStatement("INSERT INTO rental (rental_number, date_rented, date_returned, cust_number, dvd_number, total_penalty_cost) VALUES (?,?,?,?,?,?)");
                ps.setInt(1, rental.getRentalNumber());
                ps.setString(2, rental.getDateRented());
                ps.setString(3, rental.getDateReturned());
                ps.setInt(4, rental.getCustNumber());
                ps.setInt(5, rental.getDvdNumber());
                ps.setDouble(6, rental.getTotalPenaltyCost());
                ps.executeUpdate();
            }
        }
        catch (EOFException e)
        {

        }
    }
}
