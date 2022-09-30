package com.jt.customer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Created by IntelliJ IDEA.
 *
 * @Classname ComparisonApp
 * @Description 程序入口
 * 美发项目更新: Bill.java card.html charge.html chargeVip.html
 */
@SpringBootApplication
@ServletComponentScan //为application加上注解@ServletComponentScan, 以便spring扫描到filter
public class MainApp extends SpringBootServletInitializer {
  private static Class clazz = MainApp.class;
  private static Logger LOG = LoggerFactory.getLogger(clazz);

  public static void main(String[] args) {
    LOG.info("{}启动", clazz);
    SpringApplication.run(clazz, args);
  }
}
