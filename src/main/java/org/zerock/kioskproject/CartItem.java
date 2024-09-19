package org.zerock.kioskproject;

public class CartItem {
  private String name;
  private int quantity;
  private int price;

  public CartItem(String name, int quantity, int price) {
    this.name = name;
    this.quantity = quantity;
    this.price = price;
  }

  public String getName() {
    return name;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public int getTotalPrice() {
    return quantity * price;
  }
}

