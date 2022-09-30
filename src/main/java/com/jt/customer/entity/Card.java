package com.jt.customer.entity;

import javax.persistence.*;
import java.util.List;

/**
 * 会员卡表
 */
@Entity
public class Card {
  // 自增主键，无具体含义
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  // 办卡日期
  private String date;
  // 卡类型:充值卡/剪发卡
  private int type;
  // 卡初始金额
  private int amount;
  // 卡余额(元)
  private int balance;
  // 是否激活,默认true(1:true;0:false)
  @Column(name = "active", columnDefinition = "boolean NOT NULL DEFAULT TRUE")
  private boolean active;
  // vip会员
  @ManyToOne
  @JoinColumn(name = "uid") //外键
  private Vip vip;
  //  private int uid;
  // 消费记录
  @OneToMany(mappedBy = "card")
  @OrderBy("id DESC")
  private List<Bill> bills;
  // 赠送项目
  @OneToMany(mappedBy = "card")
  @OrderBy("id ASC")
  private List<Bonus> bonuses;
//  // 赠送项目1
//  private int bonus1;
//  // 赠送项目1的次数
//  private int bonus1times;
//  // 赠送项目2
//  private int bonus2;
//  // 赠送项目2的次数
//  private int bonus2times;


  @Override
  public String toString() {
    return "Card{" +
            "id=" + id +
            ", date='" + date + '\'' +
            ", type=" + type +
            ", amount=" + amount +
            ", balance=" + balance +
            ", active=" + active +
            ", vip=" + vip2string() +
            ", bills=" + bills +
            ", bonuses=" + bonuses +
            '}';
  }

  public String vip2string() {
    return vip.toString4Card();
  }

  public String detailBonus() {
    return "初始金额:" + amount +
            ",开卡日期:" + date +
            ",剩余赠送项目:" + bonuses2String()
            ;
  }

  public String detail() {
    return "余额:¥" + balance +
            ",初始金额:¥" + amount +
            ",开卡日期:" + date +
            ",剩余赠送项目:" + bonuses2String()
            ;
  }

  public String bonuses2String() {
    StringBuilder sb = new StringBuilder();
    for (Bonus b : bonuses) {
      if (sb.length() == 0) {
        sb.append(b.show());
      } else {
        sb.append(",").append(b.show());
      }
    }
    return "".equals(sb.toString()) ? "无" : sb.toString();
  }

  public String summary() {
    StringBuilder summary = new StringBuilder();
    for (ContrastValue cv : StaticContrast.cardTypes) {
      if (cv.getValue_id() == type) {
        summary.append(cv.getName());
        break;
      }
    }
    summary.append(":余额").append(balance).append("元");
    return summary.toString();
  }

  public String parseType() {
    StringBuilder summary = new StringBuilder();
    for (ContrastValue cv : StaticContrast.cardTypes) {
      if (cv.getValue_id() == type) {
        summary.append(cv.getName());
        break;
      }
    }
    return summary.toString();
  }

  public List<Bonus> getBonuses() {
    return bonuses;
  }

  public void setBonuses(List<Bonus> bonuses) {
    this.bonuses = bonuses;
  }

  public Vip getVip() {
    return vip;
  }

  public void setVip(Vip vip) {
    this.vip = vip;
  }

  public List<Bill> getBills() {
    return bills;
  }

  public void setBills(List<Bill> bills) {
    this.bills = bills;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  public int getBalance() {
    return balance;
  }

  public void setBalance(int balance) {
    this.balance = balance;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }
}
