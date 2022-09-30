package com.jt.customer.util;

import java.util.List;

public class MyJson {

  public static String list2string(List<String> list) {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < list.size(); i++) {
      String str = list.get(i);
      if (i != 0) stringBuilder.append(',');
      stringBuilder.append('"');
      stringBuilder.append(str);
      stringBuilder.append('"');
    }
    return stringBuilder.toString();
  }

}
