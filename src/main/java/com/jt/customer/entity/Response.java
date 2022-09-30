package com.jt.customer.entity;

/**
 * 向前端返回信息封装
 */
public class Response<T> {
  // 返回信息
  private String msg;
  // 数据请求是否正常
  private boolean success;
  // 返回数据（对象）
  private T obj;
}
