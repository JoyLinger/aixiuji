package com.jt.customer.entity;

import java.util.Arrays;
import java.util.List;

public class EChartsSeries {
  // 系列名称
  private String name;
  // 默认使用柱状图
  private String type = "line";
  // 柱状图之间的间隔
  private String barGap = "10%";
  // 数据
  private Integer[] data;
  // 使用的 y 轴的 index，在单个图表实例中存在多个 y轴的时候有用。
  private int yAxisIndex = 0;
  // 是否平滑曲线显示。
  private double smooth = 0.5;

  public EChartsSeries(String name, List<Integer> data) {
    this.name = name;
    Integer[] arr = new Integer[data.size()];
    data.toArray(arr);
    this.data = arr;
  }

  @Override
  public String toString() {
    return "{" +
            "'name':'" + name + "'" +
            ", 'type':'" + type + "'" +
            ", 'barGap':'" + barGap + "'" +
            ", 'yAxisIndex':'" + yAxisIndex + "'" +
            ", 'smooth':'" + smooth + "'" +
            ", 'data':" + Arrays.toString(data) +
            '}';
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public String getBarGap() {
    return barGap;
  }

  public Integer[] getData() {
    return data;
  }

  public int getyAxisIndex() {
    return yAxisIndex;
  }

  public double getSmooth() {
    return smooth;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setBarGap(String barGap) {
    this.barGap = barGap;
  }

  public void setData(Integer[] data) {
    this.data = data;
  }

  public void setyAxisIndex(int yAxisIndex) {
    this.yAxisIndex = yAxisIndex;
  }

  public void setSmooth(double smooth) {
    this.smooth = smooth;
  }
}
