package com.jt.customer.dao;

import com.jt.customer.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {
  //收入统计(全部数据)
  String INCOME_STATS_SQL = "select\n" +
          "date_format(date, ?1) as date, \n" +
          "sum(IF(pay_method<>4,pay_amount,0)) as income, \n" +
          "count(pay_amount) as population \n" +
          "from bill  \n" +
          "group by date_format(date, ?1) \n" +
          "order by date_format(date, ?1)\n" +
          ";\n";
  //收入统计(只显示最近N天)
  String INCOME_STATS_BY_DAYS_SQL = "select\n" +
          "date_format(date, ?1) as date, \n" +
          "sum(IF(pay_method<>4,pay_amount,0)) as income, \n" +
          "count(pay_amount) as population \n" +
          "from bill  \n" +
          "where date >= ?2 \n" +
          "group by date_format(date, ?1) \n" +
          "order by date_format(date, ?1)\n" +
          ";\n";
  //顾客统计(全部数据)
  String CUSTOMER_STATS_SQL = "SELECT\n" +
          "  DISTINCT src.dt AS date,src.val_id AS role,IFNULL(b2.cnt,0) AS total\n" +
          "FROM (\n" +
          "  SELECT\n" +
          "    b.dt AS dt,cv.value_id AS val_id\n" +
          "  FROM (SELECT DATE_FORMAT(date, ?1) AS dt FROM bill) AS b\n" +
          "  LEFT JOIN (SELECT value_id FROM contrast_value WHERE key_id=4 AND value_id <> 0) AS cv\n" +
          "  ON 1=1\n" +
          ") AS src\n" +
          "LEFT JOIN (SELECT DATE_FORMAT(date, ?1) AS dt, role, COUNT(1) AS cnt FROM bill WHERE operation<>1 GROUP BY dt, role) b2\n" +
          "ON src.dt = b2.dt\n" +
          "AND src.val_id = b2.role";
  //顾客统计(只显示最近N天)
  String CUSTOMER_STATS_BY_DAYS_SQL = "SELECT\n" +
          "  DISTINCT src.dt AS date,src.val_id AS role,IFNULL(b2.cnt,0) AS total\n" +
          "FROM (\n" +
          "  SELECT\n" +
          "    b.dt AS dt,cv.value_id AS val_id\n" +
          "  FROM (SELECT DATE_FORMAT(date, ?1) AS dt FROM bill WHERE date >= ?2) AS b\n" +
          "  LEFT JOIN (SELECT value_id FROM contrast_value WHERE key_id=4 AND value_id <> 0) AS cv\n" +
          "  ON 1=1\n" +
          ") AS src\n" +
          "LEFT JOIN (SELECT DATE_FORMAT(date, ?1) AS dt, role, COUNT(1) AS cnt FROM bill WHERE operation<>1 AND date >= ?2 GROUP BY dt, role) b2\n" +
          "ON src.dt = b2.dt\n" +
          "AND src.val_id = b2.role";
  String CUSTOMER_STATS_SQL2 = "SELECT\n" +
          "  DISTINCT src.dt AS date,src.val_id AS role,IFNULL(b2.cnt,0) AS total\n" +
          "FROM (\n" +
          "  SELECT\n" +
          "    b.dt AS dt,cv.value_id AS val_id\n" +
          "  FROM (SELECT DATE_FORMAT(date, ?1) AS dt FROM bill WHERE date >= ?2) AS b\n" +
          "  LEFT JOIN (SELECT value_id FROM contrast_value WHERE key_id=4 AND value_id <> 0) AS cv\n" +
          "  ON 1=1\n" +
          ") AS src\n" +
          "LEFT JOIN (SELECT DATE_FORMAT(date, ?1) AS dt, role, COUNT(1) AS cnt FROM bill GROUP BY dt, role) b2\n" +
          "ON src.dt = b2.dt\n" +
          "AND src.val_id = b2.role";
  //客流量&收入统计
  String CI_STATS_SQL = "select\n" +
          "date_format(date, ?1) as date, \n" +
          "sum(pay_amount) as income, \n" +
          "count(pay_amount) as population \n" +
          "from bill  \n" +
          "where operation=2 \n" +
          "group by date_format(date, ?1) \n" +
          "order by date_format(date, ?1)\n" +
          ";\n";

  /**
   * 会员账单
   *
   * @param date
   * @param operation
   * @param role
   * @param pay_method
   * @param pay_amount
   * @param cid
   * @return
   */
  @Modifying
  @Query(value = "insert into Bill(id,date,operation,role,pay_method,pay_amount,project,cid,note) values(null,:date,:operation,:role,:pay_method,:pay_amount,:project,:cid,:note)", nativeQuery = true)
  void addBill(String date, int operation, int role, int pay_method, int pay_amount, int project, int cid, String note);

  @Query(value = "select b.* from bill b where b.cid=?1", nativeQuery = true)
  List<Bill> getBillsByCid(int cid);

  /**
   * 散户账单
   *
   * @param date
   * @param operation
   * @param role
   * @param pay_method
   * @param pay_amount
   * @return
   */
  @Modifying
  @Query(value = "insert into Bill(id,date,operation,role,pay_method,pay_amount,project,note) values(null,:date,:operation,:role,:pay_method,:pay_amount,:project,:note)", nativeQuery = true)
  void addBill(String date, int operation, int role, int pay_method, int pay_amount, int project, String note);

  @Query(value = "select b from Bill b where date=?1 and operation=?2 and role=?3 and pay_method=?4 and pay_amount=?5 and project=?6")
  Bill getBillByAll(String date, int operation, int role, int pay_method, int pay_amount, int project);

  /**
   * 后端分页,全部数据
   * @param pageable 分页信息
   * @return 结果
   */
  @Query(value = "SELECT * FROM bill", countQuery = "SELECT COUNT(*) FROM bill", nativeQuery = true)
  Page<Bill> getAllBillPage(Pageable pageable);

  /**
   * 后端分页,并按照日期过滤
   * @param day 日期过滤条件
   * @param pageable 分页信息
   * @return 结果
   */
  @Query(value = "SELECT * FROM bill WHERE substring(date,1,10)=?1", countQuery = "SELECT COUNT(*) FROM bill", nativeQuery = true)
  Page<Bill> getBillPageByDay(String day, Pageable pageable);

  @Query(value = "SELECT * FROM bill", nativeQuery = true)
  List<Bill> getAllBills();

  @Query(value = "SELECT * FROM bill WHERE substring(date,1,10)=?1", nativeQuery = true)
  List<Bill> getBillsByDay(String day);

  @Query(value = "select sum(pay_amount),count(pay_amount) from bill where pay_method<>4 and date>=?1 and date<?2", nativeQuery = true)
  int incomeStats(String startDate, String endDate);

  @Query(value = "select sum(pay_amount),count(pay_amount) from bill where pay_method<>4", nativeQuery = true)
  int incomeStats();

  /**
   * 按月统计收入和顾客量
   * 其中,收入不计入划卡(4)
   * @return 统计结果
   */
  @Query(value = "select date_format(date,'%Y-%m') as period,sum(IF(pay_method<>4,pay_amount,0)) as income,count(pay_amount) as sales from bill group by date_format(date,'%Y-%m') order by date_format(date,'%Y-%m')", nativeQuery = true)
  List<Map<String, Object>> income4month();

  @Query(value = "select new com.jt.customer.entity.Customer(date_format(date,'%Y-%m'),role,count(role)) from Bill group by date_format(date,'%Y-%m'),role order by date_format(date,'%Y-%m'),role")
  List<Customer> customer4month();

//  @Query(value = "select new com.jt.customer.entity.Customer(date_format(date,?1),role,count(role)) from Bill group by date_format(date,?1),role order by date_format(date,?1),role")
//  List<Customer> customerStats(String timeFormat);
  @Query(value = CUSTOMER_STATS_SQL, nativeQuery = true)
  List<CustomerInterface> customerStats(String timeFormat);

//  @Query(value = "select new com.jt.customer.entity.Customer(date_format(date,?1),role,count(role)) from Bill where date >= ?2 group by date_format(date,?1),role order by date_format(date,?1),role")
//  List<Customer> customerStats(String timeFormat, String startDate);

  @Query(value = CUSTOMER_STATS_SQL2, nativeQuery = true)
  List<CustomerInterface> customerStats(String timeFormat, String startDate);

  @Query(value = "select b from Bill b where id=?1")
  Bill getBillById(int billId);

  @Modifying
  @Query(value = "UPDATE Bill SET operation=?2,project=?3,pay_amount=?4,pay_method=?5,note=?6,role=?7 WHERE id=?1", nativeQuery = true)
  void updateBillById(int billId, int operation, int project, int pay_amount, int pay_method, String note, int role);

  /**
   * 获取会员卡总充值金额
   *
   * @param cardId 卡id
   * @return 总充值金额
   */
  @Query(value = "select IFNULL(sum(pay_amount),0) from bill where cid=?1 and operation=1", nativeQuery = true)
  int getTotalChargeByCardId(int cardId);

  /**
   * 获取会员卡总消费金额
   *
   * @param cardId 卡id
   * @return 总消费金额
   */
  @Query(value = "select IFNULL(sum(pay_amount),0) from bill where cid=?1 and operation=2 and pay_method=4", nativeQuery = true)
  int getTotalPayByCardId(int cardId);

  @Query(value = INCOME_STATS_SQL, nativeQuery = true)
  List<IncomeInterface> incomeStats(String timeFormat);

  @Modifying
  @Query(value = "DELETE FROM bill WHERE cid=?1", nativeQuery = true)
  void deleteBillsByCid(int cid);

  @Modifying
  @Query(value = "ALTER TABLE bill AUTO_INCREMENT = 1", nativeQuery = true)
  void resetAutoIncrement();

  @Query(value = INCOME_STATS_BY_DAYS_SQL, nativeQuery = true)
  List<IncomeInterface> incomeStatsByStartDay(String dateFormat, String startDay);

  @Query(value = CUSTOMER_STATS_BY_DAYS_SQL, nativeQuery = true)
  List<CustomerInterface> customerStatsByStartDay(String dateFormat, String startDay);

  @Query(value = "SELECT COUNT(*) FROM bill", nativeQuery = true)
  Long getRecordsTotal();

  @Query(value = "SELECT COUNT(*) FROM bill WHERE date_format(date, '%Y-%m-%d') = ?1", nativeQuery = true)
  Long getRecordsTotal(String day);

  /**
   * 后端分页,并按照备注过滤
   * @param note 备注过滤条件
   * @param pageable 分页信息
   * @return 结果
   */
  @Query(value = "SELECT * FROM bill WHERE note like ?1", countQuery = "SELECT COUNT(*) FROM bill WHERE note like ?1", nativeQuery = true)
  Page<Bill> getBillPageByNote(String note, Pageable pageable);

  /**
   * 后端分页,并按照日期和备注过滤
   * @param day 日期过滤条件
   * @param note 备注过滤条件
   * @param pageable 分页信息
   * @return 结果
   */
  @Query(value = "SELECT * FROM bill WHERE substring(date,1,10)=?1 AND note like ?2", countQuery = "SELECT COUNT(*) FROM bill WHERE substring(date,1,10)=?1 AND note like ?2", nativeQuery = true)
  Page<Bill> getBillPageByDateAndNote(String day, String note, Pageable pageable);
}
