package com.jt.customer.entity;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DataTablePageDto<T> {
  private final Class<DataTablePageDto> clazz = DataTablePageDto.class;
  private final Logger LOG = LoggerFactory.getLogger(clazz);

  private Integer draw;
  private Long recordsTotal;
  private Long recordsFiltered;
  private Integer start;
  private Integer length;
  private String day;
  private String search;
  private Integer project;
  private String dateDim;
  private List<DataTableColumn> columns;
  private List<Order> order;
  private String dataJson;
  private List<T> data;

  public DataTablePageDto() {
  }

  public String getSortColumn(int index) {
    if (this.columns == null || this.columns.size() <= index ||
        this.order == null || this.order.size() <= index) {
      return "id";
    }
    int colIndex = this.order.get(index).getColumn();
    String colName = this.columns.get(colIndex).getData();
    if ("function".equals(colName)) {
      return "id";
    }
    return colName;
  }

  public String getSortDirection(int index) {
    if (this.order == null || this.order.size() <= index) {
      return "desc";
    }
    return this.order.get(index).getDir();
  }

  public String toJson() {
    JSONObject json = new JSONObject();
    json.put("draw", this.draw);
    json.put("recordsTotal", this.recordsTotal);
    json.put("recordsFiltered", this.recordsFiltered);
    json.put("data", JSONObject.parse(this.dataJson));
    return json.toString();
  }

  public Integer getDraw() {
    return draw;
  }

  public void setDraw(Integer draw) {
    this.draw = draw;
  }

  public Long getRecordsTotal() {
    return recordsTotal;
  }

  public void setRecordsTotal(Long recordsTotal) {
    this.recordsTotal = recordsTotal;
  }

  public Long getRecordsFiltered() {
    return recordsFiltered;
  }

  public void setRecordsFiltered(Long recordsFiltered) {
    this.recordsFiltered = recordsFiltered;
  }

  public Integer getStart() {
    return start;
  }

  public void setStart(Integer start) {
    this.start = start;
  }

  public Integer getLength() {
    return length;
  }

  public void setLength(Integer length) {
    this.length = length;
  }

  public String getDay() {
    return day;
  }

  public void setDay(String day) {
    this.day = day;
  }

  public String getSearch() {
    return search;
  }

  public void setSearch(String search) {
    this.search = search;
  }

  public Integer getProject() {
    return project;
  }

  public void setProject(Integer project) {
    this.project = project;
  }

  public String getDateDim() {
    return dateDim;
  }

  public void setDateDim(String dateDim) {
    this.dateDim = dateDim;
  }

  public List<DataTableColumn> getColumns() {
    return columns;
  }

  public void setColumns(List<DataTableColumn> columns) {
    this.columns = columns;
  }

  public List<Order> getOrder() {
    return order;
  }

  public void setOrder(List<Order> order) {
    this.order = order;
  }

  public String getDataJson() {
    return dataJson;
  }

  public void setDataJson(String dataJson) {
    this.dataJson = dataJson;
  }

  public List<T> getData() {
    return data;
  }

  public void setData(List<T> data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return "DataTablePageDto{" +
           "dataJson='" + dataJson + '\'' +
           ", draw=" + draw +
           ", recordsTotal=" + recordsTotal +
           ", recordsFiltered=" + recordsFiltered +
           ", start=" + start +
           ", length=" + length +
           ", day='" + day + '\'' +
           ", search='" + search + '\'' +
           ", project=" + project +
           ", dateDim='" + dateDim + '\'' +
           ", columns=" + columns +
           ", order=" + order +
           '}';
  }
}
