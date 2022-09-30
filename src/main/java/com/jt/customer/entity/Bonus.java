package com.jt.customer.entity;

import javax.persistence.*;

@Entity
public class Bonus {
  // 自增主键，无具体含义
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  // 赠送项目
  private int project;
  // 赠送项目的次数
  private int times;
  // 赠送日期
  private String date;
  // vip会员
  @ManyToOne
  @JoinColumn(name = "cid") //外键
  private Card card;

  @Override
  public String toString() {
    return "Bonus{" +
            "id=" + id +
            ", project=" + project +
            ", times=" + times +
            ", date='" + date + '\'' +
//            ", card=" + card +
            '}';
  }

  public String show() {
    StringBuilder summary = new StringBuilder();
    for (ContrastValue cv : StaticContrast.projects) {
      if (cv.getValue_id() == project) {
        summary.append(cv.getName());
        break;
      }
    }
    for (ContrastValue cv : StaticContrast.bonusTimes) {
      if (cv.getValue_id() == times) {
        summary.append(cv.getName());
        break;
      }
    }
    summary.append("次");
    return summary.toString();
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public Card getCard() {
    return card;
  }

  public void setCard(Card card) {
    this.card = card;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getProject() {
    return project;
  }

  public void setProject(int project) {
    this.project = project;
  }

  public int getTimes() {
    return times;
  }

  public void setTimes(int times) {
    this.times = times;
  }
}
