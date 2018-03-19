package org.solteh.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The entity ShoppingCart is related to one Account,
 * one WebUser and references several LineItem.&nbsp;
 */
@Entity
@Table(name = "shoppingcart")
public class ShoppingCart {
    @Id
    @GeneratedValue
    @Column
    private int id;
    @Column
    private Date creationDate;
    @OneToMany(mappedBy = "shoppingcart")
    private List<LineItem> items;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public List<LineItem> getItems() {
        return items;
    }

    public void setItems(List<LineItem> items) {
        this.items = items;
    }
}

