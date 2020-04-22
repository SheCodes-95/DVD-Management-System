import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ServerService
{
    private Socket clientSocket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public ServerService()
    {

    }

    public void startServerService() throws IOException
    {
        clientSocket = new Socket("127.0.0.1", 1234);
    }

    public void createStreams() throws IOException
    {
        output = new ObjectOutputStream(clientSocket.getOutputStream());
        output.flush();

        input = new ObjectInputStream(clientSocket.getInputStream());
    }

    public ArrayList<Customer> getCustomersData()
    {
        try
        {
            output.writeObject("get");
            output.flush();
            output.writeObject("customers");
            output.flush();

            ArrayList<Customer> customers = (ArrayList<Customer>)input.readObject();

            return customers;
        }
        catch (IOException | ClassNotFoundException e)
        {
            System.out.println(e);
            return null;
        }
    }

    public ArrayList<DVD> getDvdsData()
    {
        try
        {
            output.writeObject("get");
            output.flush();
            output.writeObject("dvds");
            output.flush();

            ArrayList<DVD> dvds = (ArrayList<DVD>)input.readObject();

            return dvds;
        }
        catch (IOException | ClassNotFoundException e)
        {
            System.out.println(e);
            return null;
        }
    }

    public ArrayList<Rental> getRentalsData()
    {
        try
        {
            output.writeObject("get");
            output.flush();
            output.writeObject("rentals");
            output.flush();

            ArrayList<Rental> rentals = (ArrayList<Rental>)input.readObject();

            return rentals;
        }
        catch (IOException | ClassNotFoundException e)
        {
            System.out.println(e);
            return null;
        }
    }

    public ArrayList<String> getDatesRentedData()
    {
        try
        {
            output.writeObject("get");
            output.flush();
            output.writeObject("datesrented");
            output.flush();

            ArrayList<String> datesRented = (ArrayList<String>)input.readObject();

            return datesRented;
        }
        catch (IOException | ClassNotFoundException e)
        {
            System.out.println(e);
            return null;
        }
    }

    public int addCustomer(Customer customer)
    {
        try
        {
            output.writeObject("add");
            output.flush();
            output.writeObject("customer");
            output.flush();
            output.writeObject(customer);
            output.flush();

            int custNumber = input.readInt();

            return custNumber;
        }
        catch (IOException e)
        {
            System.out.println(e);
            return 0;
        }
    }

    public Boolean phoneNumberExists(String phoneNumber)
    {
        try
        {
            output.writeObject("check");
            output.flush();
            output.writeObject("phonenumber");
            output.flush();
            output.writeObject(phoneNumber);
            output.flush();

            boolean exists = input.readBoolean();

            return exists;
        }
        catch (IOException e)
        {
            System.out.println(e);
            return null;
        }
    }

    public int addDvd(DVD dvd)
    {
        try
        {
            output.writeObject("add");
            output.flush();
            output.writeObject("dvd");
            output.flush();
            output.writeObject(dvd);
            output.flush();

            int dvdNumber = input.readInt();

            return dvdNumber;
        }
        catch (IOException e)
        {
            System.out.println(e);
            return 0;
        }
    }

    public Boolean titleExists(String title)
    {
        try
        {
            output.writeObject("check");
            output.flush();
            output.writeObject("title");
            output.flush();
            output.writeObject(title);
            output.flush();

            boolean exists = input.readBoolean();

            return exists;
        }
        catch (IOException e)
        {
            System.out.println(e);
            return null;
        }
    }

    public void deleteCustomer(int custNumber)
    {
        try
        {
            output.writeObject("delete");
            output.flush();
            output.writeObject("customer");
            output.flush();
            output.writeInt(custNumber);
            output.flush();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    public void deleteDvd(int dvdNumber)
    {
        try
        {
            output.writeObject("delete");
            output.flush();
            output.writeObject("dvd");
            output.flush();
            output.writeInt(dvdNumber);
            output.flush();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    public int rent(int dvdNumber, int custNumber)
    {
        try
        {
            output.writeObject("rent");
            output.flush();
            output.writeInt(dvdNumber);
            output.flush();
            output.writeInt(custNumber);
            output.flush();

            int rentalNumber = input.readInt();

            return rentalNumber;
        }
        catch (IOException e)
        {
            System.out.println(e);
            return 0;
        }
    }

    public Double getCustomerCredit(int custNumber)
    {
        try
        {
            output.writeObject("get");
            output.flush();
            output.writeObject("customercredit");
            output.flush();
            output.writeInt(custNumber);
            output.flush();

            double credit = input.readDouble();

            return credit;
        }
        catch (IOException e)
        {
            System.out.println(e);
            return null;
        }
    }

    public void decreaseCustomerCredit(int custNumber, double amount)
    {
        try
        {
            output.writeObject("update");
            output.flush();
            output.writeObject("decreasecustomercredit");
            output.flush();
            output.writeInt(custNumber);
            output.flush();
            output.writeDouble(amount);
            output.flush();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    public void increaseCustomerCredit(int custNumber, double amount)
    {
        try
        {
            output.writeObject("update");
            output.flush();
            output.writeObject("increasecustomercredit");
            output.flush();
            output.writeInt(custNumber);
            output.flush();
            output.writeDouble(amount);
            output.flush();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    public void returnRental(Rental rental)
    {
        try
        {
            output.writeObject("returnrental");
            output.flush();
            output.writeObject(rental);
            output.flush();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }
}
