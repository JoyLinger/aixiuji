package com.jt.customer.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class ContrastKey implements Serializable {
  // 自增主键,value id
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  // 名称
  private String name;
  // session中的变量名
  private String attr;
  // fetch = FetchType.EAGER关闭懒加载,解决报错failed to lazily initialize a collection of role
  @OneToMany(mappedBy = "key", fetch = FetchType.EAGER) //映射ContrastValue类中的key属性
  @OrderBy("id ASC")
  private List<ContrastValue> values;

  public ContrastKey() {
  }

  public ContrastKey(int id, String attr, String name) {
    this.id = id;
    this.attr = attr;
    this.name = name;
  }

  public String toStr() {
    return "ContrastKey{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", attr='" + attr + '\'' +
            ", values=" + values +
            '}';
  }

  @Override
  public String toString() {
    String str = "{" +
            "\"id\":\"" + id + '\"' +
            ", \"name\":\"" + name + '\"' +
            ", \"attr\":\"" + attr + '\"' +
            '}';
    return str.replace("'", "\"");
  }

  public String getAttr() {
    return attr;
  }

  public void setAttr(String attr) {
    this.attr = attr;
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

  public List<ContrastValue> getValues() {
    return values;
  }

  public void setValues(List<ContrastValue> values) {
    this.values = values;
  }

}
