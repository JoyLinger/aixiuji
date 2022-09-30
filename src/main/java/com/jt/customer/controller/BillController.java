package com.jt.customer.controller;

import com.jt.customer.entity.Bill;
import com.jt.customer.service.BillService;
import com.jt.customer.service.CardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BillController {
  private final Class<BillController> clazz = BillController.class;
  private final Logger LOG = LoggerFactory.getLogger(clazz);
  private final String prefix = "filter/";
  @Autowired
  BillService billService;
  @Autowired
  CardService cardService;

  @RequestMapping("/filter/addBill")
  String addBill(Integer operation, Integer pay_method, Integer pay_amount, Integer project, Integer role, String note, Model model) {
    Bill bill = billService.addBill(operation, role, pay_method, pay_amount, project, note);
    List<Bill> bills = new ArrayList<>();
    bills.add(bill);
    model.addAttribute("bills", bills);
    return prefix + "showBills";
  }

  @RequestMapping("/filter/addBillAndUpdateCard")
  String addBillAndUpdateCard(Integer operation, Integer pay_method, Integer pay_amount, Integer project, Integer cid, Integer times, String note, Model model) {
    pay_method = pay_method == null ? 0 : pay_method;
    pay_amount = pay_amount == null ? 0 : pay_amount;
    times = times == null ? 0 : times;
    LOG.info("operation={},pay_method={},pay_amount={},project={},cid={},times={}", operation, pay_method, pay_amount, project, cid, times);
    Bill bill = billService.addBillAndUpdateCard(operation, 1, pay_method, pay_amount, project, cid, times, note);
    List<Bill> bills = new ArrayList<>();
    bills.add(bill);
    model.addAttribute("bills", bills);
    return prefix + "showBills";
  }

  //  @RequestMapping("/bill")
//  String showAllBills(Integer pageNum, Integer pageSize, String sortCol, String startDate, Model model){
//    LOG.info("pageNum={},pageSize={},sortCol={},startDate={}", pageNum,pageSize,sortCol,startDate);
//    Page<Bill> billsPage;
//    if(startDate == null || startDate.equals("")){
//      // startDate参数为空时,查询所有账单
//      billsPage = billService.getAllBillPage(pageNum,pageSize,sortCol);
//    }else {
//      // startDate参数有值时,查询账单日期大于startDate的账单
//      billsPage = billService.getBillPageByStartDate(pageNum, pageSize, sortCol, startDate);
//    }
//    model.addAttribute("pageInfo",billsPage);
//    return "allBills";
//  }
  @RequestMapping("/filter/showBills")
  String showAllBills(String startDate, Model model) {
    LOG.info("startDate={}", startDate);
    List<Bill> bills;
    if (startDate == null || startDate.equals("")) {
      // startDate参数为空时,查询所有账单
      bills = billService.getAllBills();
    } else {
      // startDate参数有值时,查询账单日期大于startDate的账单
      bills = billService.getBillsByStartDate(startDate);
    }
    model.addAttribute("bills", bills);
    return prefix + "manage/allBills";
  }

  /**
   * 显示会员卡所有账单明细
   *
   * @param cid
   * @param model
   * @return
   */
  @RequestMapping("/filter/showCardBills")
  public String showCardBills(Integer cid, Model model) {
    LOG.info("cid={}", cid);
    List<Bill> bills = billService.getBillsByCid(cid);
    model.addAttribute("bills", bills);
    return prefix + "showCardBills";
  }

//  @RequestMapping("/filter/dataTableDemo")
//  String dataTableDemo() {
//    return prefix + "dataTableDemo";
//  }

//  @RequestMapping("/filter/income")
//  String incomeStats(String year, String month, Model model) {
//    LOG.info("year={},month={}", year, month);
//
//  }

  @RequestMapping("/filter/toUpdateBill")
  public String toUpdateBill(Integer billId, Model model) {
    LOG.info("billId={}", billId);
    Bill bill = billService.getBillById(billId);
    model.addAttribute("bill", bill);
    return prefix + "manage/updateBill";
  }

  @RequestMapping("/filter/updateBill")
  public String updateBill(Integer billId, Integer operation, Integer project, Integer pay_amount, Integer pay_method, Integer oldPayAmount, Integer oldPayMethod,String note, Model model) {
    LOG.info("billId={},operation={},project={},pay_amount={},pay_method={},oldPayAmount={},oldPayMethod={},note={}", billId, operation, project, pay_amount, pay_method, oldPayAmount, oldPayMethod, note);
    if (billId != null && operation != null && project != null && pay_amount != null && pay_method != null && oldPayAmount != null && note != null) {
      // 更新账单
      billService.updateBillById(billId, operation, project, pay_amount, pay_method, note);
      Bill bill = billService.getBillById(billId);
      // 以下情况需要同时更新卡余额
      if (bill.getCard() != null) {
        int newCardBalance = bill.getCard().getBalance();
        // 1.账单金额修改了,且支付方式没变仍为划卡
        if (!oldPayAmount.equals(pay_amount) && pay_method.equals(4) && pay_method.equals(oldPayMethod)) {
          // 新的卡余额 = 原卡余额 + 原账单金额 - 新账单金额
          newCardBalance = bill.getCard().getBalance() + oldPayAmount - pay_amount;
          LOG.info("新的卡余额 = 原卡余额 + 原账单金额 - 新账单金额 = {} + {} - {} = {}", bill.getCard().getBalance(), oldPayAmount, pay_amount, newCardBalance);
        }
        // 2.支付方式变为划卡
        if (pay_method.equals(4) && !oldPayMethod.equals(4)) {
          // 新的卡余额 = 原卡余额 - 新账单金额
          newCardBalance = bill.getCard().getBalance() - pay_amount;
          LOG.info("新的卡余额 = 原卡余额 - 新账单金额 = {} - {} = {}", bill.getCard().getBalance(), pay_amount, newCardBalance);
        }
        // 3.支付方式从划卡变为其他
        if (!pay_method.equals(4) && oldPayMethod.equals(4)) {
          // 新的卡余额 = 原卡余额 + 原账单金额
          newCardBalance = bill.getCard().getBalance() + oldPayAmount;
          LOG.info("新的卡余额 = 原卡余额 + 原账单金额 = {} + {} = {}", bill.getCard().getBalance(), oldPayAmount, newCardBalance);
        }
        LOG.info("原卡余额={},现卡余额={}", bill.getCard().getBalance(), newCardBalance);
        if (newCardBalance != bill.getCard().getBalance()) {
          LOG.info("需要更新卡余额");
          cardService.updateBalanceById(bill.getCard().getId(), newCardBalance);
          bill.getCard().setBalance(newCardBalance);
        }
      }
      List<Bill> bills = new ArrayList<>();
      bills.add(bill);
      model.addAttribute("bills", bills);
      return prefix + "showBills";
    } else {
      LOG.error("Cannot update when any param is null");
      return prefix + "errorPage";
    }
  }

}
