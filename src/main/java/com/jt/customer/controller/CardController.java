package com.jt.customer.controller;

import com.jt.customer.entity.Card;
import com.jt.customer.entity.Vip;
import com.jt.customer.service.CardService;
import com.jt.customer.service.VipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Classname CardController
 * @Description 会员管理
 * @Date 2020/7/10 10:52
 * @Created by LinJi
 */
@Controller
public class CardController {
  private final Class<CardController> clazz = CardController.class;
  private final Logger LOG = LoggerFactory.getLogger(clazz);
  private final String prefix = "filter/";
  @Autowired
  CardService cardService;
  @Autowired
  VipService vipService;

  /**
   * 开卡&绑定
   */
  @RequestMapping("/filter/addCard")
  public String addCard(Integer type, Integer amount, Integer uid, Integer pay_method, Integer bonus1, Integer bonus1times, Integer bonus2, Integer bonus2times, Model model) {

    LOG.info("/filter/addCard,params:type={},amount={},uid={},pay_method={},bonus1={},bonus1times={},bonus2={},bonus2times={}",type,amount,uid,pay_method,bonus1,bonus1times,bonus2,bonus2times);
    //开卡,直接激活:isActive=true
    Card card = cardService.addCard(type, amount, amount, true, uid);
    //记账:第一次开卡属于充值,一定是VIP会员
    cardService.addBill(1, 1, pay_method, amount, 0, card.getId(), "");
    //如果有赠送项目,就插入到Bonus表
    cardService.addBonus(card.getId(), bonus1, bonus1times, bonus2, bonus2times);
    Vip vip = vipService.getVip(uid);
    List<Vip> vips = new ArrayList<>();
    vips.add(vip);
    model.addAttribute("vips", vips);
    return prefix + "showVips";
  }

  /**
   * 显示会员所有卡明细
   *
   * @param uid
   * @param model
   * @return
   */
  @RequestMapping("/filter/manage/showVipCards")
  public String showVipCards(Integer uid, Model model) {
    LOG.info("uid={}", uid);
    List<Card> cards = cardService.getCardsByUid(uid);
    model.addAttribute("cards", cards);
    return prefix + "manage/showVipCards";
  }

  @RequestMapping("/filter/toUpdateCard")
  public String toUpdateCard(Integer cid, Model model) {
    LOG.info("cardId={}", cid);
    Card card = cardService.getCardById(cid);
    model.addAttribute("card", card);
    return prefix + "manage/updateCard";
  }

  @RequestMapping("/filter/updateCard")
  public String updateCard(Integer cid, Integer type, Integer amount, Integer bonus1, Integer bonus1times, Integer bonus2, Integer bonus2times, Model model) {
    LOG.info("cardId={},type={},amount={},bonus1={},bonus1times={},bonus2={},bonus2times={}", cid, type, amount, bonus1, bonus1times, bonus2, bonus2times);
    if (cid != null && type != null && amount != null) {
      cardService.updateCardById(cid, type, amount);
      cardService.updateBillByCardId(cid, amount);
    }
    if (cid != null) {
      cardService.updateBonusByCardId(cid, bonus1, bonus1times, bonus2, bonus2times);
      Card card = cardService.getCardById(cid);
      List<Card> cards = new ArrayList<>();
      cards.add(card);
      model.addAttribute("cards", cards);
    }
    return prefix + "manage/showCards";
  }

  @RequestMapping("/filter/checkCardBalance")
  public String checkBalanceById(Integer cardId, Model model) {
    LOG.info("cardId={}", cardId);
    Card card = cardService.getCardById(cardId);
    model.addAttribute("balance", card.getBalance());
    long calcBalance = cardService.checkBalanceById(cardId);
    model.addAttribute("calcBalance", calcBalance);
    model.addAttribute("cardId", cardId);
    return prefix + "manage/checkCardBalance";
  }

  @RequestMapping("/filter/updateCardBalance")
  public String updateCardBalance(Integer cardId, Integer balance, Integer calcBalance, Model model) {
    LOG.info("cardId={},balance={},calcBalance={}", cardId, balance, calcBalance);
    cardService.updateCardBalanceById(cardId, calcBalance, balance);
    Card card = cardService.getCardById(cardId);
    List<Card> cards = new ArrayList<>();
    cards.add(card);
    model.addAttribute("cards", cards);
    return prefix + "manage/showCards";
  }

  @RequestMapping("/filter/deleteCard")
  public String deleteCard(Integer cardId, Integer vipId, Model model) {
    LOG.info("cardId={},vipId={}", cardId, vipId);
    cardService.deleteCardById(cardId);
    List<Card> cards = cardService.getCardsByUid(vipId);
    model.addAttribute("cards", cards);
    return prefix + "manage/showCards";
  }

}
