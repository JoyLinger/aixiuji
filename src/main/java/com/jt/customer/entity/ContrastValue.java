package com.jt.customer.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class ContrastValue implements Serializable {
  // 自增主键,value id
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  // value id
  private int value_id;
  // 名称
  private String name;
  // vip会员
  @ManyToOne
  @JoinColumn(name = "key_id") //外键
  private ContrastKey key;

  @Override
  public String toString() {
    String str = "{" +
            "\"id\":\"" + id + '\"' +
            ", \"value_id\":\"" + value_id + '\"' +
            ", \"name\":\"" + name + '\"' +
            '}';
    return str.replace("'", "\"");
  }

  public int getValue_id() {
    return value_id;
  }

  public void setValue_id(int value_id) {
    this.value_id = value_id;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ContrastKey getKey() {
    return key;
  }

  public void setKey(ContrastKey key) {
    this.key = key;
  }

}
