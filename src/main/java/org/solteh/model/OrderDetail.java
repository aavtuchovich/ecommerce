package org.solteh.model;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.io.*;
import java.util.*;

@Entity
@Table(name = "Order_Details")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class OrderDetail implements Serializable {
	private static final long serialVersionUID = -589747686685428188L;
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@JoinColumn(name = "PRODUCT_ID",
			foreignKey = @ForeignKey(name = "ORDER_DETAIL_PROD_FK"))
	@ElementCollection
	private Map<Product, Long> products = new HashMap<>();

	@Column(name = "Amount")
	private double amount;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Map<Product, Long> getProducts() {
		return products;
	}

	public void setProducts(Map<Product, Long> products) {
		this.products = products;
	}

	public void addProduct(Product product, long quantity) {
		this.products.put(product, quantity);
	}
}
