package org.solteh.model;

import java.util.Date;
import java.util.List;


/**
 * The entity ShoppingCart is related to one Account,
 * one WebUser and references several LineItem.&nbsp;
 */

public class ShoppingCart {
    public Date creationDate;
    public List<LineItem> items;
}

