import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Gui extends JFrame implements ActionListener, DocumentListener
{
    private JToggleButton buttCustomerList = new JToggleButton("List of Customers");
    private JToggleButton buttDvdList = new JToggleButton("List of Dvds");
    private JToggleButton buttRentalList = new JToggleButton("List of Rentals");

    private ButtonGroup buttonGroup = new ButtonGroup();

    private MyTableModel myTModelCustomersList = new MyTableModel();
    private TableRowSorter<MyTableModel> tabelSorterCustomersList = new TableRowSorter<>(myTModelCustomersList);
    private JTable jTableCustomersList = new JTable(myTModelCustomersList);
    private JTextField textSearchCustomer = new JTextField();
    private JTextField textFirstName = new JTextField();
    private JTextField textSurname = new JTextField();
    private JTextField textPhoneNumber = new JTextField();
    private JComboBox<Double> comboCredit = new JComboBox<>(new Double[]{100.0,150.0,200.0,250.0,300.0,350.0,400.0,450.0,500.0});
    private JButton buttAddCustomer = new JButton("Add Customer");
    private JButton buttDeleteCustomer = new JButton("Delete Customer");

    private MyTableModel myTModelDvdsList = new MyTableModel();
    private TableRowSorter<MyTableModel> tabelSorterDvdsList = new TableRowSorter<>(myTModelDvdsList);
    private JTable jTableDvdsList = new JTable(myTModelDvdsList);
    private JTextField textSearchDvd = new JTextField();
    private JComboBox<String> comboCustomer = new JComboBox<>();
    private JButton buttRentDvd = new JButton("Rent Dvd");
    private JTextField textTitle = new JTextField();
    private JComboBox<String> comboCategory = new JComboBox<>(new String[]{"Choose", "Horror", "Sci-Fi", "Drama", "Romance", "Comedy", "Action", "Cartoon"});
    private JComboBox<String> comboReleaseType = new JComboBox<>(new String[]{"Choose", "New Release", "Old Release"});
    private JButton buttAddDvd = new JButton("Add DVD");
    private JButton buttDeleteDvd = new JButton("Delete DVD");

    private MyTableModel myTModelRentalsList = new MyTableModel();
    private TableRowSorter<MyTableModel> tabelSorterRentalsList = new TableRowSorter<>(myTModelRentalsList);
    private JTable jTableRentalsList = new JTable(myTModelRentalsList);
    private JTextField textSearchRental = new JTextField();
    private JComboBox<String> comboDateRentedFilter = new JComboBox<>();
    private JComboBox<String> comboDateReturnedFilter = new JComboBox<>(new String[]{"All", "Outstanding", "Returned"});
    private JButton buttReturnDvd = new JButton("Return Dvd");

    private JPanel panCustomersList = new JPanel(new BorderLayout());
    private JPanel panDvdsList = new JPanel(new BorderLayout());
    private JPanel panRentalsList = new JPanel(new BorderLayout());

    private CardLayout cLayout = new CardLayout();
    private JPanel panCardholder = new JPanel(cLayout);

    private ServerService serverService = new ServerService();

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

    public Gui()
    {
       try
       {
           serverService.startServerService();
           serverService.createStreams();

           setFrame();
           setWestNavigation();
           setCardHolder();
           listeners();
           setCustomersListPanel();
           setDvdListPanel();
           setRentalsListPanel();

           ArrayList<Customer> customers = serverService.getCustomersData();

           comboCustomer.removeAllItems();
           comboCustomer.addItem("Choose");
           myTModelCustomersList.setRowCount(0);

           for (Customer customer : customers)
           {
               if(customer.canRent())
               {
                   comboCustomer.addItem(String.valueOf(customer.getCustNumber()));
               }
               myTModelCustomersList.addRow(customer.getTableRowData());
           }

           ArrayList<DVD> dvds = serverService.getDvdsData();

           myTModelDvdsList.setRowCount(0);

           for (DVD dvd : dvds)
           {
               myTModelDvdsList.addRow(dvd.getTableRowData());
           }

           ArrayList<Rental> rentals = serverService.getRentalsData();

           myTModelRentalsList.setRowCount(0);

           for (Rental rental : rentals)
           {
               myTModelRentalsList.addRow(rental.getTableRowData());
           }

           ArrayList<String> datesRented = serverService.getDatesRentedData();

           comboDateRentedFilter.removeAllItems();
           comboDateRentedFilter.addItem("All");

           for (String date : datesRented)
           {
               comboDateRentedFilter.addItem(date);
           }

           display();
       }
       catch (IOException e)
       {
           System.out.println(e);
       }
    }

    private void setFrame()
    {
        this.setTitle("DVD RENTALS");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(1000, 600));
        this.setVisible(false);
    }

    public void display()
    {
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void setWestNavigation()
    {
        buttonGroup.add(buttCustomerList);
        buttonGroup.add(buttDvdList);
        buttonGroup.add(buttRentalList);

        JPanel panWestNavigation = new JPanel(new GridLayout(3, 1));
        panWestNavigation.setPreferredSize(new Dimension(150,0));
        panWestNavigation.add(buttDvdList);
        panWestNavigation.add(buttCustomerList);
        panWestNavigation.add(buttRentalList);

        buttDvdList.setSelected(true);

        this.add(panWestNavigation, BorderLayout.WEST);
    }

    public void setCardHolder()
    {
        panCardholder.add(panCustomersList, "CustomersList");
        panCardholder.add(panDvdsList, "DvdsList");
        panCardholder.add(panRentalsList, "RentalsList");

        cLayout.show(panCardholder, "DvdsList");

        this.add(panCardholder, BorderLayout.CENTER);
    }

    public void setCustomersListPanel()
    {
        myTModelCustomersList.addColumn("cust_number");
        myTModelCustomersList.addColumn("first_name");
        myTModelCustomersList.addColumn("surname");
        myTModelCustomersList.addColumn("phone_number");
        myTModelCustomersList.addColumn("credit");
        myTModelCustomersList.addColumn("can_rent");

        jTableCustomersList.setRowSorter(tabelSorterCustomersList);

        panCustomersList.add(new JScrollPane(jTableCustomersList), BorderLayout.CENTER);

        textSearchCustomer.setPreferredSize(new Dimension(130,25));
        textFirstName.setPreferredSize(new Dimension(130,25));
        textSurname.setPreferredSize(new Dimension(130,25));
        textPhoneNumber.setPreferredSize(new Dimension(130,25));
        comboCredit.setPreferredSize(new Dimension(130,25));
        buttAddCustomer.setPreferredSize(new Dimension(130,25));
        buttDeleteCustomer.setPreferredSize(new Dimension(130,25));

        JPanel panOptions = new JPanel(new FlowLayout());
        panOptions.setPreferredSize(new Dimension(150,0));
        panOptions.add(new JLabel("Search:"));
        panOptions.add(textSearchCustomer);
        panOptions.add(new JLabel("--------------------------------"));
        panOptions.add(new JLabel("First Name:"));
        panOptions.add(textFirstName);
        panOptions.add(new JLabel("Surname:"));
        panOptions.add(textSurname);
        panOptions.add(new JLabel("Phone Number:"));
        panOptions.add(textPhoneNumber);
        panOptions.add(new JLabel("Credit:"));
        panOptions.add(comboCredit);
        panOptions.add(buttAddCustomer);
        panOptions.add(buttDeleteCustomer);

        panCustomersList.add(panOptions, BorderLayout.EAST);
    }

    public void setDvdListPanel()
    {
        myTModelDvdsList.addColumn("dvd_number");
        myTModelDvdsList.addColumn("title");
        myTModelDvdsList.addColumn("category");
        myTModelDvdsList.addColumn("price");
        myTModelDvdsList.addColumn("new_release");
        myTModelDvdsList.addColumn("available_for_rent");

        jTableDvdsList.setRowSorter(tabelSorterDvdsList);

        panDvdsList.add(new JScrollPane(jTableDvdsList), BorderLayout.CENTER);

        textSearchDvd.setPreferredSize(new Dimension(130,25));
        comboCustomer.setPreferredSize(new Dimension(130,25));
        buttRentDvd.setPreferredSize(new Dimension(130,25));
        textTitle.setPreferredSize(new Dimension(130,25));
        comboCategory.setPreferredSize(new Dimension(130,25));
        comboReleaseType.setPreferredSize(new Dimension(130,25));
        buttAddDvd.setPreferredSize(new Dimension(130,25));
        buttDeleteDvd.setPreferredSize(new Dimension(130,25));

        JPanel panOptions = new JPanel(new FlowLayout());
        panOptions.setPreferredSize(new Dimension(150,0));
        panOptions.add(new JLabel("Search:"));
        panOptions.add(textSearchDvd);
        panOptions.add(new JLabel("--------------------------------"));
        panOptions.add(new JLabel("Customer:"));
        panOptions.add(comboCustomer);
        panOptions.add(buttRentDvd);
        panOptions.add(new JLabel("--------------------------------"));
        panOptions.add(new JLabel("Title:"));
        panOptions.add(textTitle);
        panOptions.add(new JLabel("Category:"));
        panOptions.add(comboCategory);
        panOptions.add(new JLabel("Release Type:"));
        panOptions.add(comboReleaseType);
        panOptions.add(buttAddDvd);
        panOptions.add(buttDeleteDvd);

        panDvdsList.add(panOptions, BorderLayout.EAST);
    }

    public void setRentalsListPanel()
    {
        myTModelRentalsList.addColumn("rental_number");
        myTModelRentalsList.addColumn("date_rented");
        myTModelRentalsList.addColumn("date_returned");
        myTModelRentalsList.addColumn("cust_number");
        myTModelRentalsList.addColumn("dvd_number");
        myTModelRentalsList.addColumn("total_penalty_cost");

        jTableRentalsList.setRowSorter(tabelSorterRentalsList);

        panRentalsList.add(new JScrollPane(jTableRentalsList), BorderLayout.CENTER);

        textSearchRental.setPreferredSize(new Dimension(130,25));
        comboDateRentedFilter.setPreferredSize(new Dimension(130,25));
        comboDateReturnedFilter.setPreferredSize(new Dimension(130,25));
        buttReturnDvd.setPreferredSize(new Dimension(130,25));

        JPanel panOptions = new JPanel(new FlowLayout());
        panOptions.setPreferredSize(new Dimension(150,0));
        panOptions.add(new JLabel("Search:"));
        panOptions.add(textSearchRental);
        panOptions.add(new JLabel("--------------------------------"));
        panOptions.add(new JLabel("Date Rented Filter:"));
        panOptions.add(comboDateRentedFilter);
        panOptions.add(new JLabel("Date Returned Filter:"));
        panOptions.add(comboDateReturnedFilter);
        panOptions.add(buttReturnDvd);

        panRentalsList.add(panOptions, BorderLayout.EAST);
    }

    public void listeners()
    {
        buttCustomerList.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                cLayout.show(panCardholder, "CustomersList");
            }
        });
        buttDvdList.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                cLayout.show(panCardholder, "DvdsList");
            }
        });
        buttRentalList.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                cLayout.show(panCardholder, "RentalsList");
            }
        });

        textSearchCustomer.getDocument().addDocumentListener(this);
        textSearchDvd.getDocument().addDocumentListener(this);
        textSearchRental.getDocument().addDocumentListener(this);

        comboDateRentedFilter.addActionListener(this);
        comboDateReturnedFilter.addActionListener(this);

        buttAddCustomer.addActionListener(this);
        buttAddDvd.addActionListener(this);

        buttDeleteCustomer.addActionListener(this);
        buttDeleteDvd.addActionListener(this);

        buttRentDvd.addActionListener(this);
        buttReturnDvd.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        //Filtering
        if(e.getSource() == comboDateRentedFilter)
        {
           if(comboDateRentedFilter.getSelectedItem() != null)
           {
               int selectedIndex = comboDateRentedFilter.getSelectedIndex();

               if(selectedIndex == 0)
               {
                   tabelSorterRentalsList.setRowFilter(RowFilter.regexFilter("",1));
               }
               else
               {
                   comboDateReturnedFilter.setSelectedIndex(0);
                   tabelSorterRentalsList.setRowFilter(RowFilter.regexFilter((String)comboDateRentedFilter.getSelectedItem(),1));
               }
           }
        }
        else if(e.getSource() == comboDateReturnedFilter)
        {
            if(comboDateReturnedFilter.getSelectedItem() != null)
            {
                int selectedIndex = comboDateReturnedFilter.getSelectedIndex();

                if(selectedIndex == 0)
                {
                    tabelSorterRentalsList.setRowFilter(RowFilter.regexFilter("",2));
                }
                else if(selectedIndex == 1)
                {
                    comboDateRentedFilter.setSelectedIndex(0);
                    tabelSorterRentalsList.setRowFilter(RowFilter.regexFilter("NA",2));
                }
                else if(selectedIndex == 2)
                {
                    comboDateRentedFilter.setSelectedIndex(0);
                    tabelSorterRentalsList.setRowFilter(RowFilter.regexFilter("^(?!NA)",2));
                }
            }
        }

        //Adding
        else if(e.getSource() == buttAddCustomer)
        {
            String firstName = textFirstName.getText();
            String surname = textSurname.getText();
            String phoneNumber = textPhoneNumber.getText();
            double credit = (double)comboCredit.getSelectedItem();

            boolean hasErrors = false;
            String errors = "";

            if(firstName.isEmpty())
            {
                hasErrors = true;
                errors += "Please enter a first name.\n";
            }
            if(surname.isEmpty())
            {
                hasErrors = true;
                errors += "Please enter a surname.\n";
            }
            if(phoneNumber.isEmpty())
            {
                hasErrors = true;
                errors += "Please enter a phone number.\n";
            }
            else if(!phoneNumber.matches("[0-9]{10}"))
            {
                hasErrors = true;
                errors += "Please enter a valid phone number.\n";
            }
            else if(serverService.phoneNumberExists(phoneNumber))
            {
                hasErrors = true;
                errors += "Phone number already exists.\n";
            }

            if(hasErrors)
            {
                JOptionPane.showMessageDialog(this,errors);
            }
            else
            {
                Customer customerToAdd = new Customer();
                customerToAdd.setName(firstName);
                customerToAdd.setSurname(surname);
                customerToAdd.setPhoneNum(phoneNumber);
                customerToAdd.setCredit(credit);
                customerToAdd.setCanRent(true);

                int custNumber = serverService.addCustomer(customerToAdd);

                if(custNumber == 0)
                {
                    JOptionPane.showMessageDialog(this,"Sorry an error has occured.");
                }
                else
                {
                    JOptionPane.showMessageDialog(this, "Customer has been added. Customer number is " + custNumber);
                    textFirstName.setText("");
                    textSurname.setText("");
                    textPhoneNumber.setText("");
                    comboCredit.setSelectedIndex(0);

                    ArrayList<Customer> customers = serverService.getCustomersData();

                    comboCustomer.removeAllItems();
                    comboCustomer.addItem("Choose");
                    myTModelCustomersList.setRowCount(0);

                    for (Customer customer : customers)
                    {
                        if(customer.canRent())
                        {
                            comboCustomer.addItem(String.valueOf(customer.getCustNumber()));
                        }
                        myTModelCustomersList.addRow(customer.getTableRowData());
                    }
                }
            }
        }
        else if(e.getSource() == buttAddDvd)
        {
            String title = textTitle.getText();
            int selectedCategory = comboCategory.getSelectedIndex();
            int selectedReleaseType = comboReleaseType.getSelectedIndex();

            boolean hasErrors = false;
            String errors = "";

            if(title.isEmpty())
            {
                hasErrors = true;
                errors += "Please enter a title.\n";
            }
            else if(serverService.titleExists(title))
            {
                hasErrors = true;
                errors += "Title already exists.\n";
            }
            if(selectedCategory == 0)
            {
                hasErrors = true;
                errors += "Please select a category.\n";
            }
            if(selectedReleaseType == 0)
            {
                hasErrors = true;
                errors += "Please select a release type.\n";
            }

            if(hasErrors)
            {
                JOptionPane.showMessageDialog(this,errors);
            }
            else
            {
                DVD dvdToAdd = new DVD();
                dvdToAdd.setTitle(title);
                dvdToAdd.setCategory(selectedCategory);
                dvdToAdd.setAvailable(true);

                if(selectedReleaseType == 1)
                {
                    dvdToAdd.setRelease(true);
                }
                else
                {
                    dvdToAdd.setRelease(false);
                }

                int dvdNumber = serverService.addDvd(dvdToAdd);

                if(dvdNumber == 0)
                {
                    JOptionPane.showMessageDialog(this,"Sorry an error has occured.");
                }
                else
                {
                    JOptionPane.showMessageDialog(this, "Dvd has been added. Dvd number is " + dvdNumber);
                    textTitle.setText("");
                    comboCategory.setSelectedIndex(0);
                    comboReleaseType.setSelectedIndex(0);

                    ArrayList<DVD> dvds = serverService.getDvdsData();

                    myTModelDvdsList.setRowCount(0);

                    for (DVD dvd : dvds)
                    {
                        myTModelDvdsList.addRow(dvd.getTableRowData());
                    }
                }
            }
        }

        //Deleting
        else if(e.getSource() == buttDeleteCustomer)
        {
            if(jTableCustomersList.getSelectedRow() < 0)
            {
                JOptionPane.showMessageDialog(this, "Please select a customer from the table to delete.");
            }
            else
            {
                int custNumber = (int)jTableCustomersList.getValueAt(jTableCustomersList.getSelectedRow(),0);

                serverService.deleteCustomer(custNumber);

                ArrayList<Customer> customers = serverService.getCustomersData();

                comboCustomer.removeAllItems();
                comboCustomer.addItem("Choose");
                myTModelCustomersList.setRowCount(0);

                for (Customer customer : customers)
                {
                    if(customer.canRent())
                    {
                        comboCustomer.addItem(String.valueOf(customer.getCustNumber()));
                    }
                    myTModelCustomersList.addRow(customer.getTableRowData());
                }


                ArrayList<Rental> rentals = serverService.getRentalsData();

                myTModelRentalsList.setRowCount(0);

                for (Rental rental : rentals)
                {
                    myTModelRentalsList.addRow(rental.getTableRowData());
                }

                ArrayList<String> datesRented = serverService.getDatesRentedData();

                comboDateRentedFilter.removeAllItems();
                comboDateRentedFilter.addItem("Choose");

                for (String date : datesRented)
                {
                    comboDateRentedFilter.addItem(date);
                }
            }
        }
        else if(e.getSource() == buttDeleteDvd)
        {
            if(jTableDvdsList.getSelectedRow() < 0)
            {
                JOptionPane.showMessageDialog(this, "Please select a dvd from the table to delete.");
            }
            else
            {
                int dvdNumber = (int)jTableDvdsList.getValueAt(jTableDvdsList.getSelectedRow(),0);

                serverService.deleteDvd(dvdNumber);

                ArrayList<DVD> dvds = serverService.getDvdsData();

                myTModelDvdsList.setRowCount(0);

                for (DVD dvd : dvds)
                {
                    myTModelDvdsList.addRow(dvd.getTableRowData());
                }

                ArrayList<Rental> rentals = serverService.getRentalsData();

                myTModelRentalsList.setRowCount(0);

                for (Rental rental : rentals)
                {
                    myTModelRentalsList.addRow(rental.getTableRowData());
                }

                ArrayList<String> datesRented = serverService.getDatesRentedData();

                comboDateRentedFilter.removeAllItems();
                comboDateRentedFilter.addItem("Choose");

                for (String date : datesRented)
                {
                    comboDateRentedFilter.addItem(date);
                }
            }
        }

        //Renting
        else if(e.getSource() == buttRentDvd)
        {
            if(jTableDvdsList.getSelectedRow() < 0)
            {
                JOptionPane.showMessageDialog(this, "Please select a dvd from the table to rent");
            }
            else if(!(boolean)jTableDvdsList.getValueAt(jTableDvdsList.getSelectedRow(), 5))
            {
                JOptionPane.showMessageDialog(this, "Selected dvd is not available for rent.");
            }
            else if(comboCustomer.getSelectedIndex() == 0 || comboCustomer.getSelectedItem() == null)
            {
                JOptionPane.showMessageDialog(this, "Please select a customer from the dropdown to rent.");
            }
            else
            {
                int dvdNumber = (int)jTableDvdsList.getValueAt(jTableDvdsList.getSelectedRow(), 0);
                double price = (double)jTableDvdsList.getValueAt(jTableDvdsList.getSelectedRow(), 3);
                int custNumber = Integer.parseInt((String)comboCustomer.getSelectedItem());
                double credit = serverService.getCustomerCredit(custNumber);

                if(credit >= price)
                {
                    serverService.decreaseCustomerCredit(custNumber, price);
                    int rentalNumber = serverService.rent(dvdNumber, custNumber);

                    if(rentalNumber == 0)
                    {
                        JOptionPane.showMessageDialog(this,"Sorry an error has occured.");
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(this, "Dvd has been rented. Rental number is " + rentalNumber);

                        ArrayList<Customer> customers = serverService.getCustomersData();

                        comboCustomer.removeAllItems();
                        comboCustomer.addItem("Choose");
                        myTModelCustomersList.setRowCount(0);

                        for (Customer customer : customers)
                        {
                            if(customer.canRent())
                            {
                                comboCustomer.addItem(String.valueOf(customer.getCustNumber()));
                            }
                            myTModelCustomersList.addRow(customer.getTableRowData());
                        }

                        ArrayList<DVD> dvds = serverService.getDvdsData();

                        myTModelDvdsList.setRowCount(0);

                        for (DVD dvd : dvds)
                        {
                            myTModelDvdsList.addRow(dvd.getTableRowData());
                        }

                        ArrayList<Rental> rentals = serverService.getRentalsData();

                        myTModelRentalsList.setRowCount(0);

                        for (Rental rental : rentals)
                        {
                            myTModelRentalsList.addRow(rental.getTableRowData());
                        }

                        ArrayList<String> datesRented = serverService.getDatesRentedData();

                        comboDateRentedFilter.removeAllItems();
                        comboDateRentedFilter.addItem("Choose");

                        for (String date : datesRented)
                        {
                            comboDateRentedFilter.addItem(date);
                        }
                    }
                }
                else
                {
                    int selection = JOptionPane.showOptionDialog(null, "Customer cannot pay dvd price of R" + price, "Not Enough Credit!",JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,null, new String[]{"Load R100 credit","Pay R"+price + " cash", "Cancel"},0);

                    if(selection == 0)
                    {
                        serverService.increaseCustomerCredit(custNumber, 100);
                        serverService.decreaseCustomerCredit(custNumber, price);
                        int rentalNumber = serverService.rent(dvdNumber, custNumber);

                        if(rentalNumber == 0)
                        {
                            JOptionPane.showMessageDialog(this,"Sorry an error has occured.");
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(this, "Dvd has been rented. Rental number is " + rentalNumber);

                            ArrayList<Customer> customers = serverService.getCustomersData();

                            comboCustomer.removeAllItems();
                            comboCustomer.addItem("Choose");
                            myTModelCustomersList.setRowCount(0);

                            for (Customer customer : customers)
                            {
                                if(customer.canRent())
                                {
                                    comboCustomer.addItem(String.valueOf(customer.getCustNumber()));
                                }
                                myTModelCustomersList.addRow(customer.getTableRowData());
                            }

                            ArrayList<DVD> dvds = serverService.getDvdsData();

                            myTModelDvdsList.setRowCount(0);

                            for (DVD dvd : dvds)
                            {
                                myTModelDvdsList.addRow(dvd.getTableRowData());
                            }

                            ArrayList<Rental> rentals = serverService.getRentalsData();

                            myTModelRentalsList.setRowCount(0);

                            for (Rental rental : rentals)
                            {
                                myTModelRentalsList.addRow(rental.getTableRowData());
                            }

                            ArrayList<String> datesRented = serverService.getDatesRentedData();

                            comboDateRentedFilter.removeAllItems();
                            comboDateRentedFilter.addItem("Choose");

                            for (String date : datesRented)
                            {
                                comboDateRentedFilter.addItem(date);
                            }
                        }
                    }
                    else if(selection == 1)
                    {
                        int rentalNumber = serverService.rent(dvdNumber, custNumber);

                        if(rentalNumber == 0)
                        {
                            JOptionPane.showMessageDialog(this,"Sorry an error has occured.");
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(this, "Dvd has been rented. Rental number is " + rentalNumber);

                            ArrayList<Customer> customers = serverService.getCustomersData();

                            comboCustomer.removeAllItems();
                            comboCustomer.addItem("Choose");
                            myTModelCustomersList.setRowCount(0);

                            for (Customer customer : customers)
                            {
                                if(customer.canRent())
                                {
                                    comboCustomer.addItem(String.valueOf(customer.getCustNumber()));
                                }
                                myTModelCustomersList.addRow(customer.getTableRowData());
                            }

                            ArrayList<DVD> dvds = serverService.getDvdsData();

                            myTModelDvdsList.setRowCount(0);

                            for (DVD dvd : dvds)
                            {
                                myTModelDvdsList.addRow(dvd.getTableRowData());
                            }

                            ArrayList<Rental> rentals = serverService.getRentalsData();

                            myTModelRentalsList.setRowCount(0);

                            for (Rental rental : rentals)
                            {
                                myTModelRentalsList.addRow(rental.getTableRowData());
                            }

                            ArrayList<String> datesRented = serverService.getDatesRentedData();

                            comboDateRentedFilter.removeAllItems();
                            comboDateRentedFilter.addItem("Choose");

                            for (String date : datesRented)
                            {
                                comboDateRentedFilter.addItem(date);
                            }
                        }
                    }
                }
            }
        }

        //Returning
        else if(e.getSource() == buttReturnDvd)
        {
            if(jTableRentalsList.getSelectedRow() < 0)
            {
                JOptionPane.showMessageDialog(this, "Please select a rental from the table to return a dvd");
            }
            else if(!((String)jTableRentalsList.getValueAt(jTableRentalsList.getSelectedRow(), 2)).equalsIgnoreCase("NA"))
            {
                JOptionPane.showMessageDialog(this, "Selected rental has been returned already.");
            }
            else
            {
                int rental_number = (int)jTableRentalsList.getValueAt(jTableRentalsList.getSelectedRow(), 0);
                String dateRented = (String)jTableRentalsList.getValueAt(jTableRentalsList.getSelectedRow(), 1);
                int custNumber = (int)jTableRentalsList.getValueAt(jTableRentalsList.getSelectedRow(), 3);
                int dvdNumber = (int)jTableRentalsList.getValueAt(jTableRentalsList.getSelectedRow(), 4);
                double credit = serverService.getCustomerCredit(custNumber);

                Rental rentalToAdd = new Rental(rental_number, dateRented, formatter.format(new Date()), custNumber, dvdNumber);

                if(rentalToAdd.getTotalPenaltyCost() == 0)
                {
                    serverService.returnRental(rentalToAdd);

                    ArrayList<Customer> customers = serverService.getCustomersData();

                    comboCustomer.removeAllItems();
                    comboCustomer.addItem("Choose");
                    myTModelCustomersList.setRowCount(0);

                    for (Customer customer : customers)
                    {
                        if(customer.canRent())
                        {
                            comboCustomer.addItem(String.valueOf(customer.getCustNumber()));
                        }
                        myTModelCustomersList.addRow(customer.getTableRowData());
                    }

                    ArrayList<DVD> dvds = serverService.getDvdsData();

                    myTModelDvdsList.setRowCount(0);

                    for (DVD dvd : dvds)
                    {
                        myTModelDvdsList.addRow(dvd.getTableRowData());
                    }

                    ArrayList<Rental> rentals = serverService.getRentalsData();

                    myTModelRentalsList.setRowCount(0);

                    for (Rental rental : rentals)
                    {
                        myTModelRentalsList.addRow(rental.getTableRowData());
                    }

                    ArrayList<String> datesRented = serverService.getDatesRentedData();

                    comboDateRentedFilter.removeAllItems();
                    comboDateRentedFilter.addItem("Choose");

                    for (String date : datesRented)
                    {
                        comboDateRentedFilter.addItem(date);
                    }
                }
                else if(credit >= rentalToAdd.getTotalPenaltyCost())
                {
                    serverService.decreaseCustomerCredit(custNumber, rentalToAdd.getTotalPenaltyCost());
                    serverService.returnRental(rentalToAdd);

                    ArrayList<Customer> customers = serverService.getCustomersData();

                    comboCustomer.removeAllItems();
                    comboCustomer.addItem("Choose");
                    myTModelCustomersList.setRowCount(0);

                    for (Customer customer : customers)
                    {
                        if(customer.canRent())
                        {
                            comboCustomer.addItem(String.valueOf(customer.getCustNumber()));
                        }
                        myTModelCustomersList.addRow(customer.getTableRowData());
                    }

                    ArrayList<DVD> dvds = serverService.getDvdsData();

                    myTModelDvdsList.setRowCount(0);

                    for (DVD dvd : dvds)
                    {
                        myTModelDvdsList.addRow(dvd.getTableRowData());
                    }

                    ArrayList<Rental> rentals = serverService.getRentalsData();

                    myTModelRentalsList.setRowCount(0);

                    for (Rental rental : rentals)
                    {
                        myTModelRentalsList.addRow(rental.getTableRowData());
                    }

                    ArrayList<String> datesRented = serverService.getDatesRentedData();

                    comboDateRentedFilter.removeAllItems();
                    comboDateRentedFilter.addItem("Choose");

                    for (String date : datesRented)
                    {
                        comboDateRentedFilter.addItem(date);
                    }
                }
                else
                {
                    int selection = JOptionPane.showOptionDialog(null, "Customer cannot pay rental penalty cost of R" + rentalToAdd.getTotalPenaltyCost(), "Not Enough Credit!",JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,null, new String[]{"Load difference of R" + (rentalToAdd.getTotalPenaltyCost() - credit) + " credit","Pay R"+rentalToAdd.getTotalPenaltyCost() + " cash", "Cancel"},0);

                    if(selection == 0)
                    {
                        serverService.increaseCustomerCredit(custNumber, (rentalToAdd.getTotalPenaltyCost() - credit));
                        serverService.decreaseCustomerCredit(custNumber, rentalToAdd.getTotalPenaltyCost());
                        serverService.returnRental(rentalToAdd);

                        ArrayList<Customer> customers = serverService.getCustomersData();

                        comboCustomer.removeAllItems();
                        comboCustomer.addItem("Choose");
                        myTModelCustomersList.setRowCount(0);

                        for (Customer customer : customers)
                        {
                            if(customer.canRent())
                            {
                                comboCustomer.addItem(String.valueOf(customer.getCustNumber()));
                            }
                            myTModelCustomersList.addRow(customer.getTableRowData());
                        }

                        ArrayList<DVD> dvds = serverService.getDvdsData();

                        myTModelDvdsList.setRowCount(0);

                        for (DVD dvd : dvds)
                        {
                            myTModelDvdsList.addRow(dvd.getTableRowData());
                        }

                        ArrayList<Rental> rentals = serverService.getRentalsData();

                        myTModelRentalsList.setRowCount(0);

                        for (Rental rental : rentals)
                        {
                            myTModelRentalsList.addRow(rental.getTableRowData());
                        }

                        ArrayList<String> datesRented = serverService.getDatesRentedData();

                        comboDateRentedFilter.removeAllItems();
                        comboDateRentedFilter.addItem("Choose");

                        for (String date : datesRented)
                        {
                            comboDateRentedFilter.addItem(date);
                        }
                    }
                    else if(selection == 1)
                    {
                        serverService.returnRental(rentalToAdd);

                        ArrayList<Customer> customers = serverService.getCustomersData();

                        comboCustomer.removeAllItems();
                        comboCustomer.addItem("Choose");
                        myTModelCustomersList.setRowCount(0);

                        for (Customer customer : customers)
                        {
                            if(customer.canRent())
                            {
                                comboCustomer.addItem(String.valueOf(customer.getCustNumber()));
                            }
                            myTModelCustomersList.addRow(customer.getTableRowData());
                        }

                        ArrayList<DVD> dvds = serverService.getDvdsData();

                        myTModelDvdsList.setRowCount(0);

                        for (DVD dvd : dvds)
                        {
                            myTModelDvdsList.addRow(dvd.getTableRowData());
                        }

                        ArrayList<Rental> rentals = serverService.getRentalsData();

                        myTModelRentalsList.setRowCount(0);

                        for (Rental rental : rentals)
                        {
                            myTModelRentalsList.addRow(rental.getTableRowData());
                        }

                        ArrayList<String> datesRented = serverService.getDatesRentedData();

                        comboDateRentedFilter.removeAllItems();
                        comboDateRentedFilter.addItem("Choose");

                        for (String date : datesRented)
                        {
                            comboDateRentedFilter.addItem(date);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e)
    {
        if(e.getDocument() == textSearchCustomer.getDocument())
        {
            tabelSorterCustomersList.setRowFilter(RowFilter.regexFilter("(?i)^" + textSearchCustomer.getText(), 1));
        }
        else if(e.getDocument() == textSearchDvd.getDocument())
        {
            tabelSorterDvdsList.setRowFilter(RowFilter.regexFilter("(?i)^" + textSearchDvd.getText(), 1));
        }
        else if(e.getDocument() == textSearchRental.getDocument())
        {
            tabelSorterRentalsList.setRowFilter(RowFilter.regexFilter("(?i)^" + textSearchRental.getText(), 0));
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e)
    {
        if(e.getDocument() == textSearchCustomer.getDocument())
        {
            tabelSorterCustomersList.setRowFilter(RowFilter.regexFilter("(?i)^" + textSearchCustomer.getText(), 1));
        }
        else if(e.getDocument() == textSearchDvd.getDocument())
        {
            tabelSorterDvdsList.setRowFilter(RowFilter.regexFilter("(?i)^" + textSearchDvd.getText(), 1));
        }
        else if(e.getDocument() == textSearchRental.getDocument())
        {
            tabelSorterRentalsList.setRowFilter(RowFilter.regexFilter("(?i)^" + textSearchRental.getText(), 0));
        }
    }

    @Override
    public void changedUpdate(DocumentEvent e)
    {
        if(e.getDocument() == textSearchCustomer.getDocument())
        {
            tabelSorterCustomersList.setRowFilter(RowFilter.regexFilter("(?i)^" + textSearchCustomer.getText(), 1));
        }
        else if(e.getDocument() == textSearchDvd.getDocument())
        {
            tabelSorterDvdsList.setRowFilter(RowFilter.regexFilter("(?i)^" + textSearchDvd.getText(), 1));
        }
        else if(e.getDocument() == textSearchRental.getDocument())
        {
            tabelSorterRentalsList.setRowFilter(RowFilter.regexFilter("(?i)^" + textSearchRental.getText(), 0));
        }
    }
}
