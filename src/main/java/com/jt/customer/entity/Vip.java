package com.jt.customer.entity;

import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

/**
 * VIP贵宾信息表
 */
@Entity
public class Vip {
  // 客户号,自增主键
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int uid;
  // 客户姓名
  private String name;
  // 余额(元)
//    private int balance;
  // 备注
  private String remark;
  // 手机号
  private String tel;
  // 开户日期
  private String date;
  // 会员卡
  @OneToMany(mappedBy = "vip") //映射Card类中的vip属性
  @OrderBy("id ASC")
  @Where(clause = "active = true")
  private List<Card> cards;
//    // 消费记录
//    @OneToMany(mappedBy = "card")
//    private List<Bill> bills;
//    // 赠送项目
//    @OneToMany(mappedBy = "card")
//    private List<Bonus> bonuses;

  public Vip() {
  }

  @Override
  public String toString() {
    return "Vip{" +
            "uid=" + uid +
            ", name='" + name + '\'' +
            ", remark='" + remark + '\'' +
            ", tel='" + tel + '\'' +
            ", date='" + date + '\'' +
            ", cards=" + cards +
//                ", bills=" + bills +
//                ", bonuses=" + bonuses +
            '}';
  }

  public String toString4Card() {
    return "Vip{" +
            "uid=" + uid +
            ", name='" + name + '\'' +
            ", remark='" + remark + '\'' +
            ", tel='" + tel + '\'' +
            ", date='" + date + '\'' +
            '}';
  }

  public String detail() {
    return "手机:" + tel +
            (remark == null || remark.equals("") ? "" : ",备注:" + remark) +
            ",开户日期:" + date;
  }

  //  public String summary() {
//    return "客户[" + uid + "]:" + name;
//  }
  public String summary() {
    return name + "[" + tel + "]";
  }

  public List<Card> getCards() {
    return cards;
  }

  public void setCards(List<Card> cards) {
    this.cards = cards;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getTel() {
    return tel;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  public int getUid() {
    return uid;
  }

  public void setUid(int uid) {
    this.uid = uid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

}
