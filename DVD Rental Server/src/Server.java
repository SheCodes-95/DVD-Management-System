import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Server
{
    private ServerSocket listener;
    private Socket client;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

    private Connection connection;
    private PreparedStatement ps;
    private ResultSet rs;

    public Server()
    {

    }

    public void startServer() throws IOException, SQLException
    {
        listener = new ServerSocket(1234, 5);
        connection = DriverManager.getConnection("jdbc:ucanaccess://dbase/dbase.accdb");
    }

    public void listen() throws IOException
    {
        client = listener.accept();
    }

    public void createStreams() throws IOException
    {
        output = new ObjectOutputStream(client.getOutputStream());
        output.flush();

        input = new ObjectInputStream(client.getInputStream());
    }

    public void process()
    {
        try
        {
            String message = "";

            do
            {
                message = (String)input.readObject();

                if(message.equalsIgnoreCase("get"))
                {
                    message = (String)input.readObject();

                    if(message.equalsIgnoreCase("customers"))
                    {
                        ArrayList<Customer> customers = new ArrayList<>();

                        ps = connection.prepareStatement("SELECT * FROM customer");
                        rs = ps.executeQuery();

                        while (rs.next())
                        {
                            Customer customer = new Customer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDouble(5), rs.getBoolean(6));
                            customers.add(customer);
                        }

                        output.writeObject(customers);
                        output.flush();
                    }
                    else if(message.equalsIgnoreCase("dvds"))
                    {
                        ArrayList<DVD> dvds = new ArrayList<>();

                        ps = connection.prepareStatement("SELECT * FROM dvd");
                        rs = ps.executeQuery();

                        while (rs.next())
                        {
                            DVD dvd = new DVD(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getBoolean(5), rs.getBoolean(6));
                            dvds.add(dvd);
                        }

                        output.writeObject(dvds);
                        output.flush();
                    }
                    else if(message.equalsIgnoreCase("rentals"))
                    {
                        ArrayList<Rental> rentals = new ArrayList<>();

                        ps = connection.prepareStatement("SELECT * FROM rental");
                        rs = ps.executeQuery();

                        while (rs.next())
                        {
                            Rental rental = new Rental(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5));
                            rentals.add(rental);
                        }

                        output.writeObject(rentals);
                        output.flush();
                    }
                    else if(message.equalsIgnoreCase("datesrented"))
                    {
                        ArrayList<String> datesRented = new ArrayList<>();

                        ps = connection.prepareStatement("SELECT DISTINCT date_rented FROM rental");
                        rs = ps.executeQuery();

                        while (rs.next())
                        {
                            datesRented.add(rs.getString(1));
                        }

                        output.writeObject(datesRented);
                        output.flush();
                    }
                    else if(message.equalsIgnoreCase("customercredit"))
                    {
                        int custNumber = input.readInt();

                        double credit = 0;

                        ps = connection.prepareStatement("SELECT credit FROM customer WHERE cust_number = ?");
                        ps.setInt(1, custNumber);
                        rs = ps.executeQuery();

                        if(rs.next())
                        {
                            credit = rs.getDouble(1);
                        }

                        output.writeDouble(credit);
                        output.flush();
                    }
                }
                else if(message.equalsIgnoreCase("add"))
                {
                    message = (String)input.readObject();

                    if(message.equalsIgnoreCase("customer"))
                    {
                        Customer customer = (Customer)input.readObject();

                        ps = connection.prepareStatement("SELECT cust_number FROM customer ORDER BY cust_number DESC");
                        rs = ps.executeQuery();

                        int newCustNumber = 0;

                        if(rs.next())
                        {
                            newCustNumber = rs.getInt(1) + 100;

                            ps = connection.prepareStatement("INSERT INTO customer (cust_number, first_name, surname, phone_number, credit, can_rent) VALUES (?,?,?,?,?,?)");
                            ps.setInt(1, newCustNumber);
                            ps.setString(2, customer.getName());
                            ps.setString(3, customer.getSurname());
                            ps.setString(4, customer.getPhoneNum());
                            ps.setDouble(5, customer.getCredit());
                            ps.setBoolean(6, customer.canRent());
                            ps.executeUpdate();
                        }

                        output.writeInt(newCustNumber);
                        output.flush();
                    }
                    else if(message.equalsIgnoreCase("dvd"))
                    {
                        DVD dvd = (DVD)input.readObject();

                        ps = connection.prepareStatement("SELECT dvd_number FROM dvd ORDER BY dvd_number DESC");
                        rs = ps.executeQuery();

                        int newDvdNumber = 0;

                        if(rs.next())
                        {
                            newDvdNumber = rs.getInt(1) + 1;

                            ps = connection.prepareStatement("INSERT INTO dvd (dvd_number, title, category, price, new_release, available_for_rent) VALUES (?,?,?,?,?,?)");
                            ps.setInt(1, newDvdNumber);
                            ps.setString(2, dvd.getTitle());
                            ps.setString(3, dvd.getCategory());
                            ps.setDouble(4, dvd.getPrice());
                            ps.setBoolean(5, dvd.isNewRelease());
                            ps.setBoolean(6, dvd.isAvailable());
                            ps.executeUpdate();
                        }

                        output.writeInt(newDvdNumber);
                        output.flush();
                    }
                }
                else if(message.equalsIgnoreCase("update"))
                {
                    message = (String)input.readObject();

                    if(message.equalsIgnoreCase("decreasecustomercredit"))
                    {
                        int custNumber = input.readInt();
                        double amount = input.readDouble();

                        ps = connection.prepareStatement("UPDATE customer SET credit = credit - ? WHERE cust_number = ?");
                        ps.setDouble(1, amount);
                        ps.setInt(2, custNumber);
                        ps.executeUpdate();
                    }
                    else if(message.equalsIgnoreCase("increasecustomercredit"))
                    {
                        int custNumber = input.readInt();
                        double amount = input.readDouble();

                        ps = connection.prepareStatement("UPDATE customer SET credit = credit + ? WHERE cust_number = ?");
                        ps.setDouble(1, amount);
                        ps.setInt(2, custNumber);
                        ps.executeUpdate();
                    }
                }
                else if(message.equalsIgnoreCase("check"))
                {
                    message = (String)input.readObject();

                    if(message.equalsIgnoreCase("phonenumber"))
                    {
                        String phoneNumber = (String)input.readObject();

                        ps = connection.prepareStatement("SELECT phone_number FROM customer WHERE phone_number = ?");
                        ps.setString(1, phoneNumber);
                        rs = ps.executeQuery();

                        if(rs.next())
                        {
                            output.writeBoolean(true);
                            output.flush();
                        }
                        else
                        {
                            output.writeBoolean(false);
                            output.flush();
                        }
                    }
                    else if(message.equalsIgnoreCase("title"))
                    {
                        String title = (String)input.readObject();

                        ps = connection.prepareStatement("SELECT title FROM dvd WHERE title = ?");
                        ps.setString(1, title);
                        rs = ps.executeQuery();

                        if(rs.next())
                        {
                            output.writeBoolean(true);
                            output.flush();
                        }
                        else
                        {
                            output.writeBoolean(false);
                            output.flush();
                        }
                    }
                }
                else if(message.equalsIgnoreCase("delete"))
                {
                    message = (String)input.readObject();

                    if(message.equalsIgnoreCase("customer"))
                    {
                        int custNumber = input.readInt();

                        ps = connection.prepareStatement("DELETE FROM rental WHERE cust_number = ?");
                        ps.setInt(1, custNumber);
                        ps.executeUpdate();

                        ps = connection.prepareStatement("DELETE FROM customer WHERE cust_number = ?");
                        ps.setInt(1, custNumber);
                        ps.executeUpdate();
                    }
                    else if(message.equalsIgnoreCase("dvd"))
                    {
                        int dvdNumber = input.readInt();

                        ps = connection.prepareStatement("DELETE FROM rental WHERE dvd_number = ?");
                        ps.setInt(1, dvdNumber);
                        ps.executeUpdate();

                        ps = connection.prepareStatement("DELETE FROM dvd WHERE dvd_number = ?");
                        ps.setInt(1, dvdNumber);
                        ps.executeUpdate();
                    }
                }
                else if(message.equalsIgnoreCase("rent"))
                {
                    System.out.println("start rent");

                    int dvdNumber = input.readInt();
                    int custNumber = input.readInt();

                    ps = connection.prepareStatement("SELECT rental_number FROM rental ORDER BY rental_number DESC");
                    rs = ps.executeQuery();

                    int rentalNumber = 0;

                    System.out.println("yo");

                    if(rs.next())
                    {
                        rentalNumber = rs.getInt(1) + 1;

                        Rental rental = new Rental(rentalNumber, formatter.format(new Date()), "NA", custNumber, dvdNumber);

                        ps = connection.prepareStatement("INSERT INTO rental (rental_number, date_rented, date_returned, cust_number, dvd_number, total_penalty_cost) VALUES (?,?,?,?,?,?)");
                        ps.setInt(1, rental.getRentalNumber());
                        ps.setString(2, rental.getDateRented());
                        ps.setString(3, rental.getDateReturned());
                        ps.setInt(4, rental.getCustNumber());
                        ps.setInt(5, rental.getDvdNumber());
                        ps.setDouble(6, rental.getTotalPenaltyCost());
                        ps.executeUpdate();

                        ps = connection.prepareStatement("UPDATE customer SET can_rent = false WHERE cust_number = ?");
                        ps.setInt(1, custNumber);
                        ps.executeUpdate();

                        ps = connection.prepareStatement("UPDATE dvd SET available_for_rent = false WHERE dvd_number = ?");
                        ps.setInt(1, dvdNumber);
                        ps.executeUpdate();
                    }

                    output.writeInt(rentalNumber);
                    output.flush();
                }
                else if(message.equalsIgnoreCase("returnrental"))
                {
                    Rental rental = (Rental)input.readObject();

                    ps = connection.prepareStatement("UPDATE rental SET date_returned = ?, total_penalty_cost = ? WHERE rental_number = ?");
                    ps.setString(1, rental.getDateReturned());
                    ps.setDouble(2, rental.getTotalPenaltyCost());
                    ps.setInt(3, rental.getRentalNumber());
                    ps.executeUpdate();

                    ps = connection.prepareStatement("UPDATE customer SET can_rent = true WHERE cust_number = ?");
                    ps.setInt(1, rental.getCustNumber());
                    ps.executeUpdate();

                    ps = connection.prepareStatement("UPDATE dvd SET available_for_rent = true WHERE dvd_number = ?");
                    ps.setInt(1, rental.getDvdNumber());
                    ps.executeUpdate();
                }

            }while (!message.equalsIgnoreCase("terminate!"));
        }
        catch (SQLException | IOException | ClassNotFoundException e)
        {
            System.out.println(e);
        }
    }
}
