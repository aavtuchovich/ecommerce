package org.solteh.model;

public class CartLineInfo {
    private Product product;
    private int quantity;

    public CartLineInfo() {
        this.quantity = 0;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getAmount() {
        return this.product.getPrice() * this.quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
