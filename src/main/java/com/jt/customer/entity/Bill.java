package com.jt.customer.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jt.customer.controller.BillController;
import com.jt.customer.util.MyJson;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

/**
 * 账单表
 */
@Entity
public class Bill {
  @Transient //当表中不存在实体类中的某个属性的时候,使用@Transient注解来忽略该属性
  private final Class<Bill> clazz = Bill.class;
  @Transient //当表中不存在实体类中的某个属性的时候,使用@Transient注解来忽略该属性
  private final Logger LOG = LoggerFactory.getLogger(clazz);
  // 自增主键，无具体含义
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  // 日期
  private String date;
  // 操作：充值/消费
  private int operation;
  // 角色：VIP|散户
  private int role;
  // 付款方式:微信/支付宝/现金/划卡
  private int pay_method;
  // 付款金额
  private int pay_amount;
  // 美发项目
  private int project;
  // 会员卡
  @ManyToOne
  @JoinColumn(name = "cid") //外键
  private Card card;
  // 备注
  private String note;

  public static void main(String[] args) {
    String jsonStr = "{'id':1688,'date':'2023-05-03 19:58:24','operation':消费,'role':老客户,'pay_method':微信,'pay_amount':20,'project':剪发,'note':'','card':'-','vip':'-','opas_update':'      <div class=\"nav-item dropdown\">\n" +
            "        <a aria-expanded=\"false\" class=\"nav-link dropdown-toggle no-padding\" data-bs-toggle=\"dropdown\" href=\"#\"\n" +
            "           id=\"dropdown01\">更多操作</a>\n" +
            "        <ul aria-labelledby=\"dropdown01\" class=\"dropdown-menu\">\n" +
            "          <li><a class=\"dropdown-item\" href=\"/filter/toUpdateBill?billId=1688\">修改账单信息</a></li>\n" +
            "        </ul>\n" +
            "      </div>'}";
    JSONObject jsonObject = JSONObject.parseObject(jsonStr);
    System.out.println("toJSONString = " + jsonObject.toJSONString());
    System.out.println("0 = " + jsonObject.getString("recordsFiltered"));
    System.out.println("1 = " + jsonObject.getString("data"));
  }

  /**
   * Generate html label
   * @return html label
   */
  public String html() {
    return "      <div class=\"nav-item dropdown\">\n" +
            "        <a aria-expanded=\"false\" class=\"nav-link dropdown-toggle no-padding\" data-bs-toggle=\"dropdown\" href=\"#\"\n" +
            "           id=\"dropdown01\">更多操作</a>\n" +
            "        <ul aria-labelledby=\"dropdown01\" class=\"dropdown-menu\">\n" +
            "          <li><a class=\"dropdown-item\" href=\"/filter/toUpdateBill?billId="+id+"\">修改账单信息</a></li>\n" +
            "        </ul>\n" +
            "      </div>";
  }

  /**
   * 获取session中的页面数据,并将id转为具体名称
   * @param session HttpSession obj
   * @param attr attr in contrast_key
   * @param valueId value_id in contrast_value
   * @return
   */
  public String getNameById(HttpSession session, String attr, int valueId){
    List<ContrastValue> contrastValueList = (List<ContrastValue>) session.getAttribute(attr);
    for(ContrastValue cv : contrastValueList){
      if(cv.getValue_id() == valueId){
        return cv.getName();
      }
    }
    LOG.warn("HttpSession中未找到attr={},valueId={}",attr, valueId);
    return "-";
  }
  public String toJson(HttpSession session){
    return "{" +
      "'id':" + id + "" +
      ",'date':'" + date + "'" +
      ",'operation':'" + getNameById(session, "operations", operation) + "'" +
      ",'role':'" + getNameById(session, "roles", role) + "'" +
      ",'pay_method':'" + getNameById(session, "payMethods", pay_method) + "'" +
      ",'pay_amount':" + pay_amount + "" +
      ",'project':'" + getNameById(session, "projects", project) + "'" +
      ",'note':'" + (note == null ? "" : note) + "'" +
            (card == null ?
                    ",'card':'-','vip':'-'" :
                    ",'card':'" + card.html() + "'" + ",'vip':'" + card.getVip().html() + "'") +
//      ",'opas_update':'"+html()+"'" +
      '}';
  }

  public Bill() {
  }

  public Bill(int id, String date, int operation, int role, int pay_method, int pay_amount, int project, String note) {
    this.id = id;
    this.date = date;
    this.operation = operation;
    this.role = role;
    this.pay_method = pay_method;
    this.pay_amount = pay_amount;
    this.project = project;
    this.note = note;
  }

  @Override
  public String toString() {
    return "Bill{" +
            "id=" + id +
            ", date='" + date + '\'' +
            ", operation=" + operation +
            ", role=" + role +
            ", pay_method=" + pay_method +
            ", pay_amount=" + pay_amount +
            ", project=" + project +
            ", note=" + note +
            (card == null ? "" : ", vip=" + card.vip2string()) +
            (card == null ? "" : ", card=" + card.summary() + "," + card.detailBonus()) +
            '}';
  }

  public String detail() {
    StringBuilder summary = new StringBuilder("支付方式:");
    for (ContrastValue cv : StaticContrast.payMethods) {
      if (cv.getValue_id() == pay_method) {
        summary.append(cv.getName());
        break;
      }
    }
    summary.append(",金额:").append(pay_amount).append(",美发项目:");
    for (ContrastValue cv : StaticContrast.projects) {
      if (cv.getValue_id() == project) {
        summary.append(cv.getName());
        break;
      }
    }
    summary.append(",账单日期:").append(date);
    summary.append(",备注:").append(note);
    return summary.toString();
  }

  public String summary() {
    StringBuilder summary = new StringBuilder("账单[" + id + "]:" + pay_amount + "元(");
    for (ContrastValue cv : StaticContrast.roles) {
      if (cv.getValue_id() == role) {
        summary.append(cv.getName());
        break;
      }
    }
    for (ContrastValue cv : StaticContrast.operations) {
      if (cv.getValue_id() == operation) {
        summary.append(cv.getName());
        break;
      }
    }
    summary.append(")");
    return summary.toString();
  }

  public String parsePayMethod() {
    for (ContrastValue cv : StaticContrast.payMethods) {
      if (cv.getValue_id() == pay_method) return cv.getName();
    }
    return "NaN";
  }

  public String parseProject() {
    for (ContrastValue cv : StaticContrast.projects) {
      if (cv.getValue_id() == project) return cv.getName();
    }
    return "NaN";
  }

  public String parseRole() {
    for (ContrastValue cv : StaticContrast.roles) {
      if (cv.getValue_id() == role) return cv.getName();
    }
    return "NaN";
  }

  public String parseOperation() {
    for (ContrastValue cv : StaticContrast.operations) {
      if (cv.getValue_id() == operation) return cv.getName();
    }
    return "NaN";
  }

  public String getDay() {
    return date.split(" ")[0];
  }

  public String omitNote() {
    if(note == null) return null;
    return note.length() <= 6 ? note : StringUtils.substring(note, 0, 6) + "...";
  }

  public int getProject() {
    return project;
  }

  public void setProject(int project) {
    this.project = project;
  }

  public Card getCard() {
    return card;
  }

  public void setCard(Card card) {
    this.card = card;
  }

  public int getPay_amount() {
    return pay_amount;
  }

  public void setPay_amount(int pay_amount) {
    this.pay_amount = pay_amount;
  }

  public int getPay_method() {
    return pay_method;
  }

  public void setPay_method(int pay_method) {
    this.pay_method = pay_method;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public int getOperation() {
    return operation;
  }

  public void setOperation(int operation) {
    this.operation = operation;
  }

  public int getRole() {
    return role;
  }

  public void setRole(int role) {
    this.role = role;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }
}
