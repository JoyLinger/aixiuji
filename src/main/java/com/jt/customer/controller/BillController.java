package com.jt.customer.controller;

import com.alibaba.fastjson.JSONArray;
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

  /**
   * 消费查询
   * @param session
   * @param model
   * @return
   */
  @RequestMapping("/filter/toBillPage")
  String toBillPage(HttpSession session, Model model){
    model.addAttribute("projects", session.getAttribute("projects"));
    return prefix + "manage/allBills";
  }

  /**
   * 后端分页
   * 使用@RequestBody注解接收前端POST请求传递的参数
   * @param dataTablePage 使用实例DataTablePageDto接收前端POST请求传递的参数
   * @param session 从HttpSession中获取会话参数，用于数据格式化
   * @return 结果数据
   */
  @RequestMapping("/filter/showBillPage")
  @PostMapping("/filter/showBillPage")
  @ResponseBody
  Object showBillPage(@RequestBody DataTablePageDto dataTablePage, HttpSession session) {
    try {
      LOG.info("data={}", dataTablePage.toString());
      LOG.info("day={}, project={}, search={}, dateDim={}", dataTablePage.getDay(), dataTablePage.getProject(), dataTablePage.getSearch(), dataTablePage.getDateDim());
      
      String searchStr = dataTablePage.getSearch();
      int pageNum = dataTablePage.getStart() / dataTablePage.getLength() + 1;
      org.springframework.data.domain.Page<Bill> bills = null;
      
      String sortCol = dataTablePage.getSortColumn(0);
      String sortDirection = dataTablePage.getSortDirection(0);
      String dateDim = dataTablePage.getDateDim();
      if (dateDim == null) {
        dateDim = "day";
      }
      
      if (dataTablePage.getDay() == null || dataTablePage.getDay().equals("")) {
        if(searchStr == null || searchStr.equals("")) {
          if(dataTablePage.getProject() == null || dataTablePage.getProject() == -1) {
            LOG.info("查询所有账单");
            bills = billService.getAllBillPage(pageNum, dataTablePage.getLength(), sortCol, sortDirection);
          } else {
            LOG.info("按project过滤: {}", dataTablePage.getProject());
            bills = billService.getBillPageByProject(pageNum, dataTablePage.getLength(), sortCol, sortDirection, dataTablePage.getProject());
          }
        } else {
          if(dataTablePage.getProject() == null || dataTablePage.getProject() == -1) {
            LOG.info("按search和project过滤: {}, {}", searchStr, dataTablePage.getProject());
            bills = billService.getBillPageByNoteAndProject(pageNum, dataTablePage.getLength(), sortCol, sortDirection, '%' + searchStr + '%', dataTablePage.getProject());
          } else {
            LOG.info("按day和project过滤: {}, {}", dataTablePage.getDay(), dataTablePage.getProject());
            bills = billService.getBillPageByDayAndProject(pageNum, dataTablePage.getLength(), sortCol, sortDirection, dataTablePage.getDay(), dataTablePage.getProject());
          }
        }
      } else {
        if(searchStr == null || searchStr.equals("")) {
          if(dataTablePage.getProject() == null || dataTablePage.getProject() == -1) {
            LOG.info("按dateDim={}过滤: {}", dateDim, dataTablePage.getDay());
            switch(dateDim) {
              case "day":
                bills = billService.getBillPageByDay(pageNum, dataTablePage.getLength(), sortCol, sortDirection, dataTablePage.getDay());
                break;
              case "month":
                bills = billService.getBillPageByMonth(pageNum, dataTablePage.getLength(), sortCol, sortDirection, dataTablePage.getDay());
                break;
              case "year":
                bills = billService.getBillPageByYear(pageNum, dataTablePage.getLength(), sortCol, sortDirection, dataTablePage.getDay());
                break;
              case "quarter":
                bills = billService.getBillPageByQuarter(pageNum, dataTablePage.getLength(), sortCol, sortDirection, dataTablePage.getDay());
                break;
              default:
                bills = billService.getBillPageByDay(pageNum, dataTablePage.getLength(), sortCol, sortDirection, dataTablePage.getDay());
                break;
            }
          } else {
            LOG.info("按dateDim={}和project过滤: {}, {}", dateDim, dataTablePage.getDay(), dataTablePage.getProject());
            switch(dateDim) {
              case "day":
                bills = billService.getBillPageByDayAndProject(pageNum, dataTablePage.getLength(), sortCol, sortDirection, dataTablePage.getDay(), dataTablePage.getProject());
                break;
              case "month":
                bills = billService.getBillPageByMonthAndProject(pageNum, dataTablePage.getLength(), sortCol, sortDirection, dataTablePage.getDay(), dataTablePage.getProject());
                break;
              case "year":
                bills = billService.getBillPageByYearAndProject(pageNum, dataTablePage.getLength(), sortCol, sortDirection, dataTablePage.getDay(), dataTablePage.getProject());
                break;
              case "quarter":
                bills = billService.getBillPageByQuarterAndProject(pageNum, dataTablePage.getLength(), sortCol, sortDirection, dataTablePage.getDay(), dataTablePage.getProject());
                break;
              default:
                bills = billService.getBillPageByDayAndProject(pageNum, dataTablePage.getLength(), sortCol, sortDirection, dataTablePage.getDay(), dataTablePage.getProject());
                break;
            }
          }
        } else {
          if(dataTablePage.getProject() == null || dataTablePage.getProject() == -1) {
            LOG.info("按dateDim={}和search过滤: {}, {}", dateDim, dataTablePage.getDay(), searchStr);
            switch(dateDim) {
              case "day":
                bills = billService.getBillPageByDayAndNote(pageNum, dataTablePage.getLength(), sortCol, sortDirection, dataTablePage.getDay(), '%' + searchStr + '%');
                break;
              case "month":
                bills = billService.getBillPageByMonthAndNote(pageNum, dataTablePage.getLength(), sortCol, sortDirection, dataTablePage.getDay(), '%' + searchStr + '%');
                break;
              case "year":
                bills = billService.getBillPageByYearAndNote(pageNum, dataTablePage.getLength(), sortCol, sortDirection, dataTablePage.getDay(), '%' + searchStr + '%');
                break;
              case "quarter":
                bills = billService.getBillPageByQuarterAndNote(pageNum, dataTablePage.getLength(), sortCol, sortDirection, dataTablePage.getDay(), '%' + searchStr + '%');
                break;
              default:
                bills = billService.getBillPageByDayAndNote(pageNum, dataTablePage.getLength(), sortCol, sortDirection, dataTablePage.getDay(), '%' + searchStr + '%');
                break;
            }
          } else {
            LOG.info("按dateDim={}、search和project过滤: {}, {}, {}", dateDim, dataTablePage.getDay(), searchStr, dataTablePage.getProject());
            switch(dateDim) {
              case "day":
                bills = billService.getBillPageByDayNoteAndProject(pageNum, dataTablePage.getLength(), sortCol, sortDirection, dataTablePage.getDay(), '%' + searchStr + '%', dataTablePage.getProject());
                break;
              case "month":
                bills = billService.getBillPageByMonthNoteAndProject(pageNum, dataTablePage.getLength(), sortCol, sortDirection, dataTablePage.getDay(), '%' + searchStr + '%', dataTablePage.getProject());
                break;
              case "year":
                bills = billService.getBillPageByYearNoteAndProject(pageNum, dataTablePage.getLength(), sortCol, sortDirection, dataTablePage.getDay(), '%' + searchStr + '%', dataTablePage.getProject());
                break;
              case "quarter":
                bills = billService.getBillPageByQuarterNoteAndProject(pageNum, dataTablePage.getLength(), sortCol, sortDirection, dataTablePage.getDay(), '%' + searchStr + '%', dataTablePage.getProject());
                break;
              default:
                bills = billService.getBillPageByDayNoteAndProject(pageNum, dataTablePage.getLength(), sortCol, sortDirection, dataTablePage.getDay(), '%' + searchStr + '%', dataTablePage.getProject());
                break;
            }
          }
        }
      }
      
      // 构建响应对象
      JSONObject result = new JSONObject();
      result.put("draw", dataTablePage.getDraw());
      result.put("recordsTotal", bills.getTotalElements());
      result.put("recordsFiltered", bills.getTotalElements());
      
      // 构建数据数组 - 先使用Bill类的toJson方法（更稳定
      JSONArray dataArray = new JSONArray();
      for (Bill b : bills) {
        try {
          dataArray.add(JSONObject.parse(b.toJson(session)));
        } catch (Exception e) {
          LOG.error("处理账单数据转换异常", e);
          JSONObject billJson = new JSONObject();
          billJson.put("id", b.getId());
          billJson.put("date", b.getDate());
          billJson.put("day", b.getDay());
          billJson.put("operation", b.getOperation());
          billJson.put("role", b.getRole());
          billJson.put("pay_method", b.getPay_method());
          billJson.put("pay_amount", b.getPay_amount());
          billJson.put("project", b.getProject());
          billJson.put("note", b.getNote() != null ? b.getNote() : "");
          billJson.put("card", "-");
          billJson.put("vip", "-");
          dataArray.add(billJson);
        }
      }
      
      result.put("data", dataArray);
      LOG.info("result size={}", dataArray.size());
      return result;
    } catch (Exception e) {
      LOG.error("查询异常", e);
      return JSONObject.parseObject("{\"draw\":1,\"recordsTotal\":0,\"recordsFiltered\":0,\"data\":[]}");
    }
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
