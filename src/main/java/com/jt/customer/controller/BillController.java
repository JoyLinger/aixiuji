package com.jt.customer.controller;

import com.alibaba.fastjson.JSONObject;
import com.jt.customer.entity.Bill;
import com.jt.customer.entity.DataTablePageDto;
import com.jt.customer.service.BillService;
import com.jt.customer.service.CardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Iterator;
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

  /**
   * 查询全部数据,分页交给前端datatable处理,效率低
   * @param day
   * @param model
   * @return
   */
  @Deprecated
  @RequestMapping("/filter/showBills")
  String showAllBills(String day, Model model) {
    LOG.info("day={}", day);
    List<Bill> bills;
    if (day == null || day.equals("")) {
      // day参数为空时,查询所有账单
      bills = billService.getAllBills(); // 查询全部,页面分页,效率低
    } else {
      // day参数有值时,查询账单日期等于day的账单
      bills = billService.getBillsByDay(day);
    }
    model.addAttribute("bills", bills);
    return prefix + "manage/allBills";
  }

  @RequestMapping("/filter/toBillPage")
  String toBillPage(){
    return prefix + "manage/billPage";
  }

  /**
   * 后端分页
   * 使用@RequestBody注解接收前端POST请求传递的参数
   * @param dataTablePage 使用实例DataTablePageDto接收前端POST请求传递的参数
   * @param session 从HttpSession中获取会话参数,用于数据格式化
   * @return 结果数据
   */
//  @RequestMapping("/filter/showBillPage")
  @PostMapping("/filter/showBillPage") //接收post请求
  @ResponseBody
  Object showBillPage(@RequestBody DataTablePageDto dataTablePage, HttpSession session) {
    LOG.info("data={}", dataTablePage.toString());
    DataTablePageDto<Bill> billPage = new DataTablePageDto<>();//分页对象
    billPage.setStart(dataTablePage.getStart());//设置起始查询页
    billPage.setLength(dataTablePage.getLength()); //设置查询条数
    billPage.setDraw(dataTablePage.getDraw());//绘制次数
    billPage.setRecordsTotal(billService.getRecordsTotal());//总行数
    // 目前需求:只过滤note备注字段
    String searchStr = dataTablePage.getSearch(); //过滤
    LOG.info("searchStr={}", searchStr);
    long recordsTotal = 0;
    int pageNo = dataTablePage.getStart() / dataTablePage.getLength() + 1; //页码
    Page<Bill> bills;
    // get order info
    String sortCol = dataTablePage.getSortColumn(0);
    String sortDirection = dataTablePage.getSortDirection(0);
    if (dataTablePage.getDay() == null || dataTablePage.getDay().equals("")) {
      if(searchStr == null || searchStr.equals("")) {
        // day和search参数均为空时,查询所有账单
        bills = billService.getAllBillPage(pageNo, dataTablePage.getLength(), sortCol, sortDirection); // 分页查询
      }else{
        // day参数为空,search参数不为空时,按照search过滤账单
        bills = billService.getBillPageByNote(pageNo, dataTablePage.getLength(), sortCol, sortDirection, '%' + searchStr + '%'); // 分页查询
      }
    } else {
      if(searchStr == null || searchStr.equals("")) {
        // day参数不为空,search参数为空时,按照day过滤账单
        bills = billService.getBillPageByDay(pageNo, dataTablePage.getLength(), sortCol, sortDirection, dataTablePage.getDay()); // 分页查询
      }else{
        // day和search参数均不为空时,按照day和search过滤账单
        bills = billService.getBillPageByDayAndNote(pageNo, dataTablePage.getLength(), sortCol, sortDirection, dataTablePage.getDay(), '%' + searchStr + '%'); // 分页查询
      }
    }
    billPage.setRecordsFiltered(bills.getTotalElements());//过滤后的行数
    // bills账单list转json array要用[]进行包裹
    StringBuilder jsonArrSB = new StringBuilder("[");
    for (Bill b : bills) {
      if (jsonArrSB.length() <= 1) {
        jsonArrSB.append(b.toJson(session));
      } else {
        jsonArrSB.append(",").append(b.toJson(session));
      }
    }
    jsonArrSB.append("]");
    System.out.println("jsonArrSB="+jsonArrSB);
    billPage.setData(jsonArrSB.toString());
    System.out.println("billPage.toJson()="+billPage.toJson());
//    String result = JSON.toJSONString(billPage);
//    LOG.info("result={}", result);
//    JSONObject jsonObj = JSONObject.parseObject(result);
    JSONObject jsonObj = JSONObject.parseObject(billPage.toJson());
//    JSONArray jsonObj = JSONObject.parseArray(billPage.toJson());
    LOG.info("jsonObj={}", jsonObj);
    return jsonObj;
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
  public String updateBill(Integer billId, Integer operation, Integer project, Integer pay_amount, Integer pay_method, Integer oldPayAmount, Integer oldPayMethod,String note,Integer role, Model model) {
    LOG.info("billId={},operation={},project={},pay_amount={},pay_method={},oldPayAmount={},oldPayMethod={},note={},role={}", billId, operation, project, pay_amount, pay_method, oldPayAmount, oldPayMethod, note, role);
    if (billId != null && operation != null && project != null && pay_amount != null && pay_method != null && oldPayAmount != null && note != null && role != null) {
      // 更新账单
      billService.updateBillById(billId, operation, project, pay_amount, pay_method, note, role);
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
