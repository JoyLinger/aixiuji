package com.jt.customer.entity;

import java.util.List;

public class StaticContrast {
  public static List<ContrastValue> projects;
  public static List<ContrastValue> operations;
  public static List<ContrastValue> payMethods;
  public static List<ContrastValue> roles;
  public static List<ContrastValue> cardTypes;
  public static List<ContrastValue> bonusTimes;
  public static List<ContrastKey> contrastKeys;

  public static String parseRole(int role) {
    for (ContrastValue cv : StaticContrast.roles) {
      if (cv.getValue_id() == role) return cv.getName();
    }
    return "NaN";
  }
}
