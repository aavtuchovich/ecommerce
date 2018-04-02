package org.solteh.model;

import org.solteh.entity.Product;

public class ProductInfo {
    private String code;
    private String name;
    private String description;
    private double price;

    public ProductInfo() {
    }

    public ProductInfo(Product product) {
        this.code = product.getCode();
        this.name = product.getName();
        this.price = product.getPrice();
        this.description = product.getDescription();
    }

    // Using in JPA/Hibernate query
    public ProductInfo(String code, String name, double price, String description) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
