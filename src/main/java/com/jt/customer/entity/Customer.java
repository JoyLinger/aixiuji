package com.jt.customer.entity;


public class Customer {
  private String date;
  private int role;
  private long total;

  public Customer(String date, int role, long total) {
    this.date = date;
    this.role = role;
    this.total = total;
  }

  public String parseRole() {
    for (ContrastValue cv : StaticContrast.roles) {
      if (cv.getValue_id() == role) return cv.getName();
    }
    return "NaN";
  }

  @Override
  public String toString() {
    return "Customer{" +
            "date='" + date + '\'' +
            ", type=" + role +
            ", total=" + total +
            '}';
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public int getRole() {
    return role;
  }

  public void setRole(int role) {
    this.role = role;
  }

  public long getTotal() {
    return total;
  }

  public void setTotal(long total) {
    this.total = total;
  }
}
