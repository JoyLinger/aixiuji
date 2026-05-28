package com.jt.customer.entity;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Income {
  // 时间段:年/月
  public String period;
  // 收支
  public BigDecimal income;
  // 销量
  public BigInteger sales;

  public Income() {
  }

  public Income(String period, BigDecimal income, BigInteger sales) {
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

  public BigDecimal getIncome() {
    return income;
  }

  public void setIncome(BigDecimal income) {
    this.income = income;
  }

  public BigInteger getSales() {
    return sales;
  }

  public void setSales(BigInteger sales) {
    this.sales = sales;
  }
}
