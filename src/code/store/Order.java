package code.store;

import code.constant.Shipping;
import code.customer.Customer;
import code.payment.Payment;

import java.util.ArrayList;

public class Order {
	private ArrayList<OrderElement> buyList = new ArrayList<>();
	private Customer customer;
	private Shipping shipping;
	private Payment payment;
	private int orderID;
	private static int numOrders;

	public Order(Customer customer, Shipping shipping) {
		numOrders++;
		this.buyList.addAll(customer.getBasketList());
		this.customer = customer;
		this.shipping = shipping;

		payment = new Payment(customer.getPrice(), customer.getMemberClass().getDiscount(), shipping.getPrice());

		orderID = numOrders;
	}

	public ArrayList<OrderElement> getBuyList() {
		return buyList;
	}

	public Customer getCustomer() {
		return customer;
	}

	public Shipping getShipping() {
		return shipping;
	}

	public Payment getPayment() {
		return payment;
	}

	public int getOrderID() {
		return orderID;
	}

	public static int getNumOrders() {
		return numOrders;
	}

	public void setBuyList(ArrayList<OrderElement> buyList) {
		this.buyList = buyList;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void setShipping(Shipping shipping) {
		this.shipping = shipping;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public static void setNumOrders(int numOrders) {
		Order.numOrders = numOrders;
	}

	public Object[] informationArray() {
		Object[] temp = new Object[7];
		temp[0] = orderID; // order id
		temp[1] = ""; // white space
		temp[2] = ""; // white space
		temp[3] = String.format("Price: %,.0f", payment.getTotalPrice()); // price
		temp[4] = String.format("Discount: %,.0f", payment.getDiscount()); // discount
		temp[5] = String.format("Shipping Fee: %,.0f", payment.getShippingFee()); // shipping
		temp[6] = String.format("Total Price: %,.0f", payment.getValue()); // total price
		return temp;
	}

	public Order clone() {
		numOrders--;
		return new Order(customer.clone(), shipping);
	}

	public String toString() {
		return payment.toString();
	}
}