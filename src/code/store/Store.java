package code.store;

import code.constant.Shipping;
import code.customer.Customer;
import code.product.ProductExt;

import java.util.*;

public class Store {
    private ArrayList<ProductExt> productList;
    private ArrayList<Customer> customerList;
    private double revenue;
    private boolean restockProduct;
    private ArrayList<Order> historyList = new ArrayList<>();
    
    private static Store store;
    
    public static Store getInstance(ArrayList<ProductExt> productList, ArrayList<Customer> customerList, String revenue) {
        if (store == null) store = new Store(productList, customerList, revenue);
        return store;
    }
    
    public static Store getInstance() {
        return store;
    }
    
    private Store(ArrayList<ProductExt> productList, ArrayList<Customer> customerList, String revenue) {
        this.productList = productList;
        this.customerList = customerList;
        this.revenue = Double.parseDouble(revenue);
        this.restockProduct = false;
    }
    
    public ArrayList<ProductExt> getProductList() {
        return productList;
    }
    
    public void setProductList(ArrayList<ProductExt> productList) {
        this.productList = productList;
    }
    
    public ArrayList<Customer> getCustomerList() {
        return customerList;
    }
    
    public void setCustomerList(ArrayList<Customer> customerList) {
        this.customerList = customerList;
    }
    
    public double getRevenue() {
        return revenue;
    }
    
    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }
    
    public void addRevenue(double revenue) {
        this.revenue += revenue;
    }

    public boolean isRestockProduct() {
        return restockProduct;
    }
    
    public void setRestockProduct(boolean restockProduct) {
        this.restockProduct = restockProduct;
    }
    
    public ArrayList<Order> getHistoryList() {
        return historyList;
    }
    
    public void setHistoryList(ArrayList<Order> historyList) {
        this.historyList = historyList;
    }
    
    public Object[][] getAllProduct(boolean id) {
        int size = productList.get(0).getProductInfo(7, id).length;
        
        Object[][] temp = new Object[productList.size()][size];
        for (int i = 0; i < productList.size(); i++) {
            temp[i] = productList.get(i).getProductInfo(7, id);
        }
        return temp;
    }
    
    public Object[][] getAllCustomer() {
        Object[][] temp = new Object[customerList.size() - 1][7];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = customerList.get(i + 1).getCustomerInfo(7);
        }
        return temp;
    }
    
    public Customer getGuest() {
        return customerList.get(0);
    }
    
    public void addProduct(ProductExt product) {
        productList.add(product);
    }
    
    public void addCustomer(Customer customer) {
        customerList.add(customer);
    }
    
    public ProductExt searchProductID(String productID) {
        for (ProductExt product : productList) {
            if (product.getProductID().equals(productID)) {
                return product;
            }
        }
        return null;
    }

    public boolean searchCustUsernamePassword(String user, String pass){
        for (Customer customer : customerList) {
            if (customer.getUsername().equals(user) && customer.getPassword().equals(pass)) {
                return true;
            }
        }
        return false;
    }
    public Customer searchCustomerUsername(String personUname) {
        for (Customer customer : customerList) {
            if (customer.getUsername().equals(personUname)) {
                return customer;
            }
        }
        return null;
    }
    
    /**
     * remove Customer and return index of that customer
     *
     * @param customer
     *         customer want to remove
     * @param guest
     *         include guest or not
     * @return index of remove customer
     */
    public int removeCustomer(Customer customer, boolean guest) {
        for (int i = 0; i < customerList.size(); i++) {
            if (customerList.get(i).getUsername().equals(customer.getUsername())) {
                customerList.remove(i);
                if (guest) {
                    return i;
                } else {
                    return i - 1;
                }
            }
        }
        return -1;
    }
    
    public void removeProduct(int ProductIndex) {
        productList.remove(ProductIndex);
    }
    
    public void removeProduct(ProductExt Product) {
        productList.remove(Product);
    }
    
    public int checkAvailability(ProductExt product, int num) {
        if (product.getCurrNumStock() < num) {
            return 0;
        }
        return 1;
    }
    
    public void updateStock(OrderElement element) {
        ProductExt customerProduct = element.getProduct();
        
        for (ProductExt product : productList) {
            // check name
            if (product.getName().equals(customerProduct.getName())) {
                // check size
                if (product.getSize().equals(customerProduct.getSize())) {
                    product.withdrawStock(element.getNum());
                }
            }
        }
        
        productList.stream().filter(product -> product.getCurrNumStock() < 3).forEach(product -> {
            product.setRestock(true);
            restockProduct = true;
        });
    }
    
    public void refundStock(OrderElement element) {
        ProductExt customerProduct = element.getProduct();
        
        for (ProductExt product : productList) {
            // check ID
            if (product.getProductID().equals(customerProduct.getProductID())) {
                product.returnStock(element.getNum());
            }
        }
    }
    
    /**
     * Create Order by using parameter and store it into HistoryList <br>
     * Update revenue and expense <br>
     * Clear product in basket
     *
     * @param customer
     *         customer
     * @param shipping
     *         how customer shipping
     */
    public void checkOut(Customer customer, Shipping shipping) {
        Order order = new Order(customer, shipping);
        customer.addToHistoryList(order);
        
        revenue += order.getPayment().getValue();
        
        customer.getBasketList().forEach(this::updateStock);
        
        customer.clearBasket();
    }

    
    public String getProductListString() {
        StringBuilder output = new StringBuilder();
        for (ProductExt product : productList) {
            output.append(product.toString()).append("\n");
        }
        return output.toString();
    }
    
    /**
     * Check product in history list of all customer if it equals keep it in arrayList
     *
     * @param product
     *         product that customer buy
     * @return arrayList of Customer that buy the product
     */
    public ArrayList<Customer> checkProductHistory(ProductExt product) {
        ArrayList<Customer> customers = new ArrayList<>();
        for (Customer customer : customerList) {
            for (int j = 0; j < customer.getHistoryList().size(); j++) {
                for (int k = 0; k < customer.getHistoryList().get(j).getBuyList().size(); k++) {
                    if (customer.getHistoryList().get(j).getBuyList().get(k).getName().equals(product.getName())) {
                        customers.add(customer);
                    }
                }
            }
        }
        
        // remove same customer
        for (int i = customers.size() - 1; i >= 0; i--) {
            if (i != customers.size()) {
                Customer temp = customers.get(i);
                if (i != 0) {
                    for (int j = i - 1; j >= 0; j--) {
                        if (customers.get(j).getUsername().equals(temp.getUsername())) {
                            customers.remove(j);
                        }
                    }
                }
            }
        }
        
        return customers;
    }
    
    public String getCustomerListString() {
        StringBuilder output = new StringBuilder();
        for (Customer customer : customerList) {
            output.append(customer.toString()).append("\n");
        }
        return output.toString();
    }
    
    public String toString() {
        String format = "Revenue: Rp %d";
        return String.format(format, (int) revenue);
    }
}
