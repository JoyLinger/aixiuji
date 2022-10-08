package com.jt.customer.dao;

import com.jt.customer.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {

  @Modifying
  @Query(value = "insert into Card(id,date,type,amount,balance,active,uid) values(null,?1,?2,?3,?4,?5,?6)", nativeQuery = true)
  void addCard(String date, int type, int amount, int balance, boolean isActive, int uid);

//  @Query(value = "select distinct id from Card where type=:type and amount=:amount and balance=:balance and date=:date and uid=:uid", nativeQuery = true)
//  public Integer getCidByAll(String date, int type, int amount, int balance, int uid);

  //  @Query(value = "select distinct c from Card c left join c.bills bi left join c.bonuses bo where c.uid=?1")
//  @Query(value = "select distinct c.* from Card c left join bill bi on c.id=bi.cid left join Bonus bo on c.id=bo.cid where c.uid=:uid order by c.id desc", nativeQuery = true)
  @Query(value = "select distinct c.* from Card c where c.uid=?1 and active = true", nativeQuery = true)
  List<Card> getCardsByUid(int uid);

//  @Query(value = "select distinct c from Card c join c.bills bi join c.bonuses bo order by c.id desc")
//  List<Card> getCardList();

  @Query(value = "select distinct c.id from Card c where date=:date and active = true order by c.id desc", nativeQuery = true)
  Integer getCardIdByUid(String date);

  /**
   * 充值
   *
   * @param pay_amount 金额
   * @return
   */
  @Modifying
  @Query(value = "update Card set balance=balance+:pay_amount where id=:cid", nativeQuery = true)
  void recharge(int pay_amount, int cid);

  /**
   * 消费
   *
   * @param pay_amount 金额
   * @param cid        卡号
   * @return
   */
  @Modifying
  @Query(value = "update Card set balance=balance-:pay_amount where id=:cid", nativeQuery = true)
  void consume(int pay_amount, int cid);

  /**
   * 使用赠送项目
   *
   * @param project 使用的赠送项目
   * @param times   使用次数
   * @param cid     卡号
   * @return
   */
  @Modifying
  @Query(value = "update Bonus set times=times-:times where cid=:cid and project=:project", nativeQuery = true)
  void bonus(int project, int times, int cid);

  @Modifying
  @Query(value = "insert into Bonus(id,project,times,date,cid) values(null,?1,?2,?3,?4)", nativeQuery = true)
  void addBonus(Integer project, Integer times, String date, int cid);

  @Query(value = "select distinct c from Card c where c.id=?1")
//  @Query(value = "select distinct c.* from Card c left join bill bi on c.id=bi.cid left join Bonus bo on c.id=bo.cid where c.id=?1", nativeQuery = true)
  Card getCardById(int id);

  @Modifying
  @Query(value = "UPDATE card SET type=?2,amount=?3 WHERE id=?1", nativeQuery = true)
  void updateCardById(int cid, int type, int amount);

  @Modifying
  @Query(value = "DELETE FROM bonus WHERE cid=?1", nativeQuery = true)
  void deleteBonusByCardId(int cid);

  @Modifying
  @Query(value = "UPDATE bill SET pay_amount=?2 WHERE id=?1", nativeQuery = true)
  void updateBillByCardId(int cid, int amount);

  @Modifying
  @Query(value = "UPDATE card SET balance=?2 WHERE id=?1", nativeQuery = true)
  void updateBalanceById(int id, int balance);

  @Modifying
  @Query(value = "UPDATE card SET active=false WHERE id=?1", nativeQuery = true)
  void deleteCardById(int cid);

  @Modifying
  @Query(value = "ALTER TABLE card AUTO_INCREMENT = 1", nativeQuery = true)
  void resetAutoIncrement();

  @Modifying
  @Query(value = "ALTER TABLE bonus AUTO_INCREMENT = 1", nativeQuery = true)
  void resetBonusAutoIncrement();

  @Query(value = "select distinct c.id from Card c where uid=:uid and active = true order by c.id desc", nativeQuery = true)
  List<Integer> getCardIdsByUid(int uid);

}
