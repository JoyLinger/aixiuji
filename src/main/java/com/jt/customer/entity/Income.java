package com.jt.customer.entity;

public class Income {
  // 时间段:年/月
  public String period;
  // 收支
  public long income;
  // 销量
  public long sales;

  public Income(String period, long income, long sales) {
    this.period = period;
    this.income = income;
    this.sales = sales;
  }

  @Override
  public String toString() {
    return "Income{" +
            "period='" + period + '\'' +
            ", income=" + income +
            ", sales=" + sales +
            '}';
  }

  public String getPeriod() {
    return period;
  }

  public void setPeriod(String period) {
    this.period = period;
  }

  public long getIncome() {
    return income;
  }

  public void setIncome(int income) {
    this.income = income;
  }

  public long getSales() {
    return sales;
  }

  public void setSales(int sales) {
    this.sales = sales;
  }
}
