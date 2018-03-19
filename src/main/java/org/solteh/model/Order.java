package org.solteh.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Date;
import java.util.List;
import java.util.Set;


/**
 * When the ShoppingCart with the LineItems
 * are confirmed by the WebUser, an Order is created.&nbsp;
 */
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private int id;
    @Column
    private Date ordered;
    @Column
    private boolean shipped;
    @Column
    private String shipTo;
    @Enumerated(EnumType.ORDINAL)
    @Column
    private OrderStatus status;
    @ManyToOne
    @JoinColumn(name="userId", nullable=false)
    private Customer customer;

    @OneToOne(cascade = CascadeType.ALL)
    private ShoppingCart cart;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getOrdered() {
        return ordered;
    }

    public void setOrdered(Date ordered) {
        this.ordered = ordered;
    }

    public boolean isShipped() {
        return shipped;
    }

    public void setShipped(boolean shipped) {
        this.shipped = shipped;
    }

    public String getShipTo() {
        return shipTo;
    }

    public void setShipTo(String shipTo) {
        this.shipTo = shipTo;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public void setCart(ShoppingCart cart) {
        this.cart = cart;
    }
}

