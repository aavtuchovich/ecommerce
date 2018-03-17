package org.solteh.model;

import java.util.List;

/**
 *  Represents the webuser when he signs in.
 */
public class Customer {
    public String address;
    public String phone;
    public String email;
    public ShoppingCart cart;
    public List<Order> orders;

    public boolean isVIP() {
        // TODO implement me
        return false;
    }

}

