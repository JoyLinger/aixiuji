package com.jt.customer.entity;

import com.jt.customer.util.MyJson;

import java.util.List;

public class EChartsOption {
  private String title;
  private List<String> legend;
  private List<String> xAxis_data;
  private List<EChartsSeries> series;
  private List<ECharts_yAxis> yAxis_data;

  public EChartsOption(String title, List<String> legend, List<String> xAxis_data, List<EChartsSeries> series, List<ECharts_yAxis> yAxis_data) {
    this.title = title;
    this.legend = legend;
    this.xAxis_data = xAxis_data;
    this.series = series;
    this.yAxis_data = yAxis_data;
  }

  @Override
  public String toString() {
    return "{" +
            "'title':'" + title + "'" +
            ", 'legend':[" + MyJson.list2string(legend) + "]" +
            ", 'xAxis_data':[" + MyJson.list2string(xAxis_data) + "]" +
            ", 'series':" + series +
            ", 'yAxis_data':" + yAxis_data +
            '}';
  }
}
