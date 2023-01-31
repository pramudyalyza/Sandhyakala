package gui.main;

import code.behavior.ButtonFactory;
import code.constant.FileLocation;
import code.customer.Customer;
import code.file.FileFactory;
import code.product.ProductExt;
import code.store.Store;
import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class MainPage extends JFrame implements ButtonFactory {
    private JButton customerButton;
    private JButton shoppingButton;
    private JButton storeButton;
    private JButton exitButton;
    private JPanel panel;

    private JPasswordField passwordField;
    private JTextField userField;

    private static FileFactory factory = new FileFactory();
    private static Store store = assignStore();

    public static Customer shopper = store.getGuest();
    
    public MainPage() {
        super("Main Page");

        setContentPane(panel);

        if(shopper.getUsername().equals("admin") && shopper.getPassword().equals("pass")){
            toCustomer(this, customerButton);
            toStore(this, storeButton);
            toLogin(this, exitButton);
        } else{
            toShopping(this, shoppingButton);
            toLogin(this, exitButton);
        }
    }

    public static void reWriteCustomer() {
        factory.setPath(FileLocation.CUSTOMER_PATH);
        factory.write(store.getAllCustomer());
    }

    private static ArrayList<ProductExt> assignProduct() {
        ArrayList<ProductExt> temp = new ArrayList<>();
        
        factory.setPath(FileLocation.PRODUCT_PATH);
        String[][] allProduct = factory.read(":");
        
        for (String[] product : allProduct) {
            if (product.length == 6)
                temp.add(new ProductExt(product[0], product[1], product[2], product[3], product[4], product[5]));
            else System.err.println("product text-file error");
        }
        return temp;
    }

    private static ArrayList<Customer> assignCustomer() {
        ArrayList<Customer> temp = new ArrayList<>();
        
        // guest member
        temp.add(new Customer());
        
        factory.setPath(FileLocation.CUSTOMER_PATH);
        String[][] allCustomer = factory.read(":");
        
        for (String[] customer : allCustomer) {
            if (customer.length == 7)
                temp.add(new Customer(customer[0], customer[1], customer[2], customer[3], customer[4], customer[5], customer[6]));
            else System.err.println("customer text-file error");
        }
        Customer.setNumCustomers(temp.size());
        
        return temp;
    }

    private static Store assignStore() {
        ArrayList<ProductExt> productList = assignProduct();
        ArrayList<Customer> customerList = assignCustomer();
        
        Store temp = null;
        factory.setPath(FileLocation.STORE_PATH);
        String[][] informations = factory.read(":");
        for (String[] info : informations) {
            temp = Store.getInstance(productList, customerList, info[0]);
        }
        
        return temp;
    }
    

    public void run(Point point) {
        pack();
        setVisible(true);
        setLocation(point);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(650, 400));
    }
}
