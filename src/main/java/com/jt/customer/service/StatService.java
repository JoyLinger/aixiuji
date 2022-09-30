package com.jt.customer.service;

import com.jt.customer.dao.BillRepository;
import com.jt.customer.entity.Customer;
import com.jt.customer.entity.CustomerInterface;
import com.jt.customer.entity.Income;
import com.jt.customer.entity.IncomeInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatService {
  private final Class<StatService> clazz = StatService.class;
  private final Logger LOG = LoggerFactory.getLogger(clazz);
  @Autowired
  BillRepository BillRepo;

  public List<Income> income4month() {
    List<Income> incomeList = BillRepo.income4month();
    LOG.info("incomeList={}", incomeList);
    return incomeList;
  }

  public List<Customer> customer4month() {
    List<Customer> customers = BillRepo.customer4month();
    LOG.info("customers={}", customers);
    return customers;
  }

//  public List<Customer> statCustomerByDay() {
//    List<Customer> customers = BillRepo.customerStats("%Y-%m-%d");
//    LOG.info("customers={}", customers);
//    return customers;
//  }
//
//  public List<Customer> statCustomerByMonth() {
//    List<Customer> customers = BillRepo.customerStats("%Y-%m");
//    LOG.info("customers={}", customers);
//    return customers;
//  }
//
//  public List<Customer> statCustomerByYear() {
//    List<Customer> customers = BillRepo.customerStats("%Y");
//    LOG.info("customers={}", customers);
//    return customers;
//  }

//  public List<Customer> statCustomerByDay(String startDate) {
//    List<Customer> customers = BillRepo.customerStats("%Y-%m-%d", startDate);
//    LOG.info("customers={}", customers);
//    return customers;
//  }
//
//  public List<Customer> statCustomerByMonth(String startDate) {
//    List<Customer> customers = BillRepo.customerStats("%Y-%m", startDate);
//    LOG.info("customers={}", customers);
//    return customers;
//  }
//
//  public List<Customer> statCustomerByYear(String startDate) {
//    List<Customer> customers = BillRepo.customerStats("%Y", startDate);
//    LOG.info("customers={}", customers);
//    return customers;
//  }

  public List<CustomerInterface> statCustomerByDay() {
    List<CustomerInterface> customers = BillRepo.customerStats("%Y-%m-%d");
    LOG.info("customers={}", customers);
    return customers;
  }

  public List<CustomerInterface> statCustomerByMonth() {
    List<CustomerInterface> customers = BillRepo.customerStats("%Y-%m");
    LOG.info("customers={}", customers);
    return customers;
  }

  public List<CustomerInterface> statCustomerByYear() {
    List<CustomerInterface> customers = BillRepo.customerStats("%Y");
    LOG.info("customers={}", customers);
    return customers;
  }

  public List<CustomerInterface> statCustomerByDay(String startDate) {
    List<CustomerInterface> customers = BillRepo.customerStats("%Y-%m-%d", startDate);
    LOG.info("customers={}", customers);
    return customers;
  }

  public List<CustomerInterface> statCustomerByMonth(String startDate) {
    List<CustomerInterface> customers = BillRepo.customerStats("%Y-%m", startDate);
    LOG.info("customers={}", customers);
    return customers;
  }

  public List<CustomerInterface> statCustomerByYear(String startDate) {
    List<CustomerInterface> customers = BillRepo.customerStats("%Y", startDate);
    LOG.info("customers={}", customers);
    return customers;
  }

  public List<IncomeInterface> statIncomeByDateFormat(String dateFormat) {
    List<IncomeInterface> incomes = BillRepo.incomeStats(dateFormat);
    LOG.info("incomes={}", incomes);
    return incomes;
  }
}
