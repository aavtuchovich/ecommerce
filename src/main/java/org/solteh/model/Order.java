package org.solteh.model;

import javax.persistence.*;
import java.util.Date;


/**
 * When the ShoppingCart with the LineItems
 * are confirmed by the WebUser, an Order is created.&nbsp;
 */
@Entity
@Table(name = "Orders")
public class Order {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "Order_Date", nullable = false)
    private Date orderDate;

    @Column(name = "Amount", nullable = false)
    private double amount;

    @Column(name = "Customer_Name")
    private String customerName;

    @Column(name = "Customer_Address")
    private String customerAddress;

    @Column(name = "Customer_Email", length = 128)
    private String customerEmail;

    @Column(name = "Customer_Phone", length = 128)
    private String customerPhone;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_DETAILS_ID", nullable = false, //
            foreignKey = @ForeignKey(name = "ORDER_DETAILS_FK"))
    private OrderDetail details;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false, //
            foreignKey = @ForeignKey(name = "ORDER_USR_FK"))
    private User user;

    public Order() {
        details = new OrderDetail();
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public OrderDetail getDetails() {
        return details;
    }

    public void setDetails(OrderDetail details) {
        this.details = details;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

