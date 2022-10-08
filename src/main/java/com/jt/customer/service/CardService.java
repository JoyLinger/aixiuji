package com.jt.customer.service;

import com.jt.customer.dao.BillRepository;
import com.jt.customer.dao.CardRepository;
import com.jt.customer.dao.ContrastRepository;
import com.jt.customer.dao.VipRepository;
import com.jt.customer.entity.Card;
import com.jt.customer.entity.Vip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Classname CardService
 * @Description 会员管理
 * @Date 2020/7/10 10:54
 * @Created by LinJi
 */
@Transactional  //添加事物管理，不添加的话DAO层可能会报错
@Service
public class CardService {
  private final Class<CardService> clazz = CardService.class;
  private final Logger LOG = LoggerFactory.getLogger(clazz);
  @Autowired
  private CardRepository cardRepo;
  @Autowired
  private BillRepository billRepo;
  @Autowired
  private VipRepository vipRepo;
  @Autowired
  private ContrastRepository contrastRepo;
  private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  /**
   * card
   *
   * @return 所有卡, 包括vip和消费信息
   */
//  public List<Card> getCardList() {
//    return cardRepo.getCardList();
//  }

  /**
   * 开卡
   *
   * @param type    卡类型
   * @param amount  初始金额
   * @param balance 余额
   * @param uid     客户号
   * @return 卡
   */
  public Card addCard(int type, int amount, int balance, boolean isActive, int uid) {
    LOG.info("type={},amount={},balance={},isActive={},uid={}", type, amount, balance, isActive, uid);
    String date = sdf.format(new Date());
    cardRepo.addCard(date, type, amount, balance, isActive, uid);
    Card card = new Card();
    card.setType(type);
    card.setAmount(amount);
    card.setBalance(balance);
    card.setDate(date);
    int cid = cardRepo.getCardIdByUid(date);
    LOG.info("addCard[cid] = {}", cid);
    card.setId(cid);
    Vip vip = vipRepo.getVipByUid(uid);
    card.setVip(vip);
    return card;
  }

  public void addBill(int operation, int role, int pay_method, int pay_amount, int project, int cid,String note) {
    LOG.info("operation={},role={},pay_method={},pay_amount={},project={},cid={}", operation, role, pay_method, pay_amount, project, cid);
    String date = sdf.format(new Date());
    billRepo.addBill(date, operation, role, pay_method, pay_amount, project, cid, note);
  }

  public void addBonus(Integer cid, Integer bonus1, Integer bonus1times, Integer bonus2, Integer bonus2times) {
    if (cid == null || cid <= 0) {
      LOG.error("卡号不合法:cid={}", cid);
      return;
    }
    String date = sdf.format(new Date());
    if (bonus1 != null && bonus1 > 0 && bonus1times != null && bonus1times > 0) {
      cardRepo.addBonus(bonus1, bonus1times, date, cid);
    }
    if (bonus2 != null && bonus2 > 0 && bonus2times != null && bonus2times > 0) {
      cardRepo.addBonus(bonus2, bonus2times, date, cid);
    }
  }

  public List<Card> getCardsByUid(int uid) {
    List<Card> cardList = cardRepo.getCardsByUid(uid);
    LOG.info("cardList={}", cardList);
    return cardList;
  }

  public Card getCardById(int cardId) {
    Card card = cardRepo.getCardById(cardId);
    LOG.info("card={}", card);
    return card;
  }

  public void updateCardById(int cid, int type, int amount) {
    cardRepo.updateCardById(cid, type, amount);
  }

  public void updateBonusByCardId(int cid, Integer bonus1, Integer bonus1times, Integer bonus2, Integer bonus2times) {
    cardRepo.deleteBonusByCardId(cid);
    cardRepo.resetBonusAutoIncrement();
    String date = sdf.format(new Date());
    if (bonus1 != null && bonus1times != null) cardRepo.addBonus(bonus1, bonus1times, date, cid);
    if (bonus2 != null && bonus2times != null) cardRepo.addBonus(bonus2, bonus2times, date, cid);
  }

  public void updateBillByCardId(int cid, int amount) {
    cardRepo.updateBillByCardId(cid, amount);
  }

  public void updateBalanceById(int id, int balance) {
    cardRepo.updateBalanceById(id, balance);
  }

//  public void autoUpdateBalanceById(int cardId) {
//    int balance = billRepo.getTotalChargeByCardId(cardId) - billRepo.getTotalPayByCardId(cardId);
//    LOG.info("卡号:{},实际余额:{}",cardId,balance);
//    cardRepo.updateBalanceById(cardId, (int) balance);
//  }

  public int checkBalanceById(int cardId) {
    int balance = billRepo.getTotalChargeByCardId(cardId) - billRepo.getTotalPayByCardId(cardId);
    LOG.info("卡号:{},根据账单计算所得卡余额:{}", cardId, balance);
    return balance;
  }

  public void updateCardBalanceById(int cardId, int calcBalance, int balance) {
    LOG.info("卡号:{},根据账单计算所得卡余额:{},当前卡余额:{}", cardId, calcBalance, balance);
    cardRepo.updateBalanceById(cardId, calcBalance);
  }

  public void deleteCardById(Integer cardId) {
    cardRepo.deleteCardById(cardId);
    contrastRepo.resetAutoIncrement();
  }

  public List<Integer> deleteCardsByUid(Integer vid) {
    List<Integer> cardIds = cardRepo.getCardIdsByUid(vid);
    for(int cid : cardIds) {
      cardRepo.deleteCardById(cid);
    }
    cardRepo.resetAutoIncrement();
    return cardIds;
  }
}
