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
  // 柱状图宽度
  private String barWidth;
  // 数据
  private Integer[] data;
  // 使用的 y 轴的 index，在单个图表实例中存在多个 y轴的时候有用。
  private int yAxisIndex = 0;
  // 是否平滑曲线显示。
  private double smooth = 0.5;
  // 堆叠
  private String stack;
  // 标签
  private Label label;
  // 样式
  private ItemStyle itemStyle;
  
  // Label内部类
  public static class Label {
    private boolean show = false;
    private String position;
    private String formatter;
    private Integer fontSize;
    private String fontWeight;
    
    public Label() {}
    
    public boolean isShow() {
      return show;
    }
    public void setShow(boolean show) {
      this.show = show;
    }
    public String getPosition() {
      return position;
    }
    public void setPosition(String position) {
      this.position = position;
    }
    public String getFormatter() {
      return formatter;
    }
    public void setFormatter(String formatter) {
      this.formatter = formatter;
    }
    public Integer getFontSize() {
      return fontSize;
    }
    public void setFontSize(Integer fontSize) {
      this.fontSize = fontSize;
    }
    public String getFontWeight() {
      return fontWeight;
    }
    public void setFontWeight(String fontWeight) {
      this.fontWeight = fontWeight;
    }
    
    @Override
    public String toString() {
      return "{" +
              "'show':" + show +
              (position != null ? ", 'position':'" + position + "'" : "") +
              (formatter != null ? ", 'formatter':'" + formatter + "'" : "") +
              (fontSize != null ? ", 'fontSize':" + fontSize : "") +
              (fontWeight != null ? ", 'fontWeight':'" + fontWeight + "'" : "") +
              '}';
    }
  }
  
  // ItemStyle内部类
  public static class ItemStyle {
    private Double opacity;
    
    public ItemStyle() {}
    
    public Double getOpacity() {
      return opacity;
    }
    public void setOpacity(Double opacity) {
      this.opacity = opacity;
    }
    
    @Override
    public String toString() {
      return "{" +
              (opacity != null ? "'opacity':" + opacity : "") +
              '}';
    }
  }

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
            (barWidth != null ? ", 'barWidth':'" + barWidth + "'" : "") +
            ", 'yAxisIndex':'" + yAxisIndex + "'" +
            ", 'smooth':'" + smooth + "'" +
            (stack != null ? ", 'stack':'" + stack + "'" : "") +
            (label != null ? ", 'label':" + label : "") +
            (itemStyle != null ? ", 'itemStyle':" + itemStyle : "") +
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

  public String getBarWidth() {
    return barWidth;
  }

  public void setBarWidth(String barWidth) {
    this.barWidth = barWidth;
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

  public String getStack() {
    return stack;
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

  public void setStack(String stack) {
    this.stack = stack;
  }

  public Label getLabel() {
    return label;
  }

  public void setLabel(Label label) {
    this.label = label;
  }

  public ItemStyle getItemStyle() {
    return itemStyle;
  }

  public void setItemStyle(ItemStyle itemStyle) {
    this.itemStyle = itemStyle;
  }
}
