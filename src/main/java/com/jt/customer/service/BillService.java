package com.jt.customer.service;

import com.jt.customer.dao.BillRepository;
import com.jt.customer.dao.CardRepository;
import com.jt.customer.entity.Bill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Transactional
@Service
public class BillService {
  private final Class<BillService> clazz = BillService.class;
  private final Logger LOG = LoggerFactory.getLogger(clazz);
  private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  //默认访问第一页
  private final int defaultPageNum = 0;
  //默认每页显示20个
  private final int defaultPageSize = 20;
  @Autowired
  CardRepository cardRepo;
  @Autowired
  BillRepository billRepo;

  /**
   * 散户账单
   *
   * @param operation
   * @param role
   * @param pay_method
   * @param pay_amount
   */
  public Bill addBill(Integer operation, Integer role, Integer pay_method, Integer pay_amount, Integer project, String note) {
    LOG.info("operation={},role={},pay_method={},pay_amount={},note={}", operation, role, pay_method, pay_amount,note);
    String date = sdf.format(new Date());
    billRepo.addBill(date, operation, role, pay_method, pay_amount, project, note);
    Bill bill = billRepo.getBillByAll(date, operation, role, pay_method, pay_amount, project);
    LOG.info(bill.toString());
    return bill;
  }

  /**
   * 会员账单
   *
   * @param operation
   * @param role
   * @param pay_method
   * @param pay_amount
   * @param cid
   */
  public Bill addBillAndUpdateCard(Integer operation, Integer role, Integer pay_method, Integer pay_amount, Integer project, Integer cid, Integer times, String note) {
    LOG.info("operation={},pay_method={},pay_amount={},project={},cid={},times={},note={}", operation, pay_method, pay_amount, project, cid, times,note);
    String date = sdf.format(new Date());
    billRepo.addBill(date, operation, role, pay_method, pay_amount, project, cid, note);
    switch (operation) {
      case 1:
        cardRepo.recharge(pay_amount, cid);
        break;
      case 2:
        cardRepo.consume(pay_amount, cid);
        break;
      case 3:
        cardRepo.bonus(project, times, cid);
        break;
      default:
        LOG.error("未知的账单操作:{}", operation);
    }
    Bill bill = billRepo.getBillByAll(date, operation, role, pay_method, pay_amount, project);
    LOG.info("bill={}", bill.toString());
//    Card card = cardRepo.getCardById(cid);
//    LOG.info("card={}", card);
//    card.setBills(null);
//    bill.setCard(card);
//    LOG.info("return bill={}", bill);
    return bill;
  }

  public Page<Bill> getAllBillPage(Integer pageNum, Integer pageSize, String sortCol, String sortDir) {
    pageNum = pageNum == null ? defaultPageNum : pageNum - 1;//前端显示是第N页,注意后台这里下标需要减1
    pageSize = pageSize == null ? defaultPageSize : pageSize;
    // 排序方式，这里是以“id”为标准进行降序
    // 这里的"id"是实体类的主键，记住一定要是实体类的属性，而不能是数据库的字段
    sortCol = sortCol == null ? "id" : sortCol;
    Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortCol);
    // （当前页， 每页记录数， 排序方式）
    Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
    Page<Bill> page = billRepo.getAllBillPage(pageable);
    LOG.info("bills={}", page.getContent());
    return page;
  }

  public Page<Bill> getBillPageByDay(Integer pageNum, Integer pageSize, String sortCol, String sortDir, String day) {
    pageNum = pageNum == null ? defaultPageNum : pageNum - 1;//前端显示是第N页,注意后台这里下标需要减1
    pageSize = pageSize == null ? defaultPageSize : pageSize;
    // 排序方式，这里是以“id”为标准进行降序
    // 这里的"id"是实体类的主键，记住一定要是实体类的属性，而不能是数据库的字段
    sortCol = sortCol == null ? "id" : sortCol;
    // 截取到天
//    day = day.split(" ")[0];
    Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortCol);
    // （当前页， 每页记录数， 排序方式）
    Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
    Page<Bill> page = billRepo.getBillPageByDay(day, pageable);
    LOG.info("bills={}", page.getContent());
    return page;
  }

  public List<Bill> getAllBills() {
    List<Bill> bills = billRepo.getAllBills();
    LOG.info("bills={}", bills);
    return bills;
  }

  public List<Bill> getBillsByDay(String day) {
    List<Bill> bills = billRepo.getBillsByDay(day);
    LOG.info("bills={}", bills);
    return bills;
  }

  public List<Bill> getBillsByCid(int cid) {
    List<Bill> billList = billRepo.getBillsByCid(cid);
    LOG.info("billList={}", billList);
    return billList;
  }

  public Bill getBillById(int billId) {
    Bill bill = billRepo.getBillById(billId);
    LOG.info("bill={}", bill);
    return bill;
  }

  public void updateBillById(int billId, int operation, int project, int pay_amount, int pay_method,String note,int role) {
    billRepo.updateBillById(billId, operation, project, pay_amount, pay_method, note, role);
  }

  public void deleteBillsByCid(int cid) {
    billRepo.deleteBillsByCid(cid);
    billRepo.resetAutoIncrement();
  }

  public Long getRecordsTotal() {
    return billRepo.getRecordsTotal();
  }

  public Long getRecordsTotal(String day) {
    return billRepo.getRecordsTotal(day);
  }

  public Page<Bill> getBillPageByNote(Integer pageNum, Integer pageSize, String sortCol, String sortDir, String note) {
    pageNum = pageNum == null ? defaultPageNum : pageNum - 1;//前端显示是第N页,注意后台这里下标需要减1
    pageSize = pageSize == null ? defaultPageSize : pageSize;
    // 排序方式，这里是以“id”为标准进行降序
    // 这里的"id"是实体类的主键，记住一定要是实体类的属性，而不能是数据库的字段
    sortCol = sortCol == null ? "id" : sortCol;
    // 截取到天
//    day = day.split(" ")[0];
    Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortCol);
    // （当前页， 每页记录数， 排序方式）
    Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
    Page<Bill> page = billRepo.getBillPageByNote(note, pageable);
    LOG.info("bills={}", page.getContent());
    return page;
  }

  public Page<Bill> getBillPageByDayAndNote(Integer pageNum, Integer pageSize, String sortCol, String sortDir, String day, String note) {
    pageNum = pageNum == null ? defaultPageNum : pageNum - 1;//前端显示是第N页,注意后台这里下标需要减1
    pageSize = pageSize == null ? defaultPageSize : pageSize;
    // 排序方式，这里是以“id”为标准进行降序
    // 这里的"id"是实体类的主键，记住一定要是实体类的属性，而不能是数据库的字段
    sortCol = sortCol == null ? "id" : sortCol;
    // 截取到天
//    day = day.split(" ")[0];
    Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortCol);
    // （当前页， 每页记录数， 排序方式）
    Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
    LOG.info("day={}, note={}", day, note);
    Page<Bill> page = billRepo.getBillPageByDateAndNote(day, note, pageable);
    LOG.info("bills={}", page.getContent());
    return page;
  }

//  public int incomeStats(String startDate, String endDate){
//
//  }
}
