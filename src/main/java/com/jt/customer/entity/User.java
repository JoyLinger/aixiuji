package com.jt.customer.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 登录用户表
 */
@Entity
public class User {
  // 账号id,自增主键
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long uid;
  // 账号名
  private String name;
  // 密码
  private String password;
  // 角色：管理员|普通用户
  private String role;
  // 备注
  private String remark;

  public User(Long uid, String name, String password, String role, String remark) {
    this.uid = uid;
    this.name = name;
    this.password = password;
    this.role = role;
    this.remark = remark;
  }

  public User() {
  }

  public boolean isAdmin() {
    return "管理员".equals(role);
  }

  public long getUid() {
    return uid;
  }

  public void setUid(Long uid) {
    this.uid = uid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

}
