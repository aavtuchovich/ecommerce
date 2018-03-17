package org.solteh.model;

import java.util.HashSet;
import java.util.Date;
import java.util.List;
import java.util.Set;


/**
 * When the ShoppingCart with the LineItems
 * are confirmed by the WebUser, an Order is created.&nbsp;
 */

public class Order {
    private int id;
    private Date ordered;
    private boolean shipped;
    private String shipTo;
    private OrderStatus status;
    private ShoppingCart cart;
}

