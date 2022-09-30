package com.jt.customer.entity;

import java.io.Serializable;

public class Page implements Serializable {
  /**
   * 分页页码，默认页码为1
   */
  protected int page;
  /**
   * 每页最多20条
   */
  protected int size;
  /**
   * 排序名称，默认为id
   */
  protected String sort = "id";

  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public String getSort() {
    return sort;
  }

  public void setSort(String sort) {
    this.sort = sort;
  }
}
