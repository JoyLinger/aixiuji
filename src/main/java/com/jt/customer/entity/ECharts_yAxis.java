package com.jt.customer.entity;

public class ECharts_yAxis {
  // y坐标轴名称
  private String name;
  // y坐标轴类型
  //可选：
  //        'value' 数值轴，适用于连续数据。
  //        'category' 类目轴，适用于离散的类目数据。为该类型时类目数据可自动从 series.data 或 dataset.source 中取，或者可通过 yAxis.data 设置类目数据。
  //        'time' 时间轴，适用于连续的时序数据，与数值轴相比时间轴带有时间的格式化，在刻度计算上也有所不同，例如会根据跨度的范围来决定使用月，星期，日还是小时范围的刻度。
  //        'log' 对数轴。适用于对数数据。
  private String type = "value";
  // 柱状图之间的间隔
  private String position= "left";
  // 坐标轴名称显示位置
  private String nameLocation = "end";
  // 单位
  private String unit;
  // 坐标轴刻度标签的相关设置
  private String axisLabel;

  public ECharts_yAxis(String name, String position, String unit) {
    this.name = name;
    this.position = position;
    this.unit = unit;
    this.axisLabel = "{formatter: '{value} " + unit + "'}";
  }

  @Override
  public String toString() {
    return "{" +
            "'name':'" + name + "'" +
            ", 'type':'" + type + "'" +
            ", 'position':'" + position + "'" +
            ", 'nameLocation':'" + nameLocation + "'" +
            ", 'axisLabel':" + axisLabel +
            '}';
  }
}
