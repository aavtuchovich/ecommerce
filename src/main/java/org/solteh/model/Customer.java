package org.solteh.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 *  Represents the webuser when he signs in.
 */
@Entity
@Table(name = "customers")
public class Customer implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "userId")
    private int id;
    @Column
    private String address;
    @Column
    private String phone;
    @Column
    private String email;
    @OneToOne(cascade = CascadeType.ALL)
    private ShoppingCart cart;
    @OneToMany(mappedBy="customer")
    private List<Order> orders;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public void setCart(ShoppingCart cart) {
        this.cart = cart;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}

