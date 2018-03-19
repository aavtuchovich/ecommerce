package org.solteh.model;


import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * Differentiated from Product to introduce quantity.
 */
@Entity
@Table(name = "items")
public class LineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int id;
    @Column
    private Integer quantity;
    @ManyToOne
    @JoinColumn(name = "scart_id")
    private ShoppingCart cart;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public void setCart(ShoppingCart cart) {
        this.cart = cart;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

