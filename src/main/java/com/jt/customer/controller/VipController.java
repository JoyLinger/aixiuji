package com.jt.customer.controller;

import com.jt.customer.entity.Vip;
import com.jt.customer.service.BillService;
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
 * @Classname VipController
 * @Description 会员管理
 * @Date 2020/7/10 10:52
 * @Created by LinJi
 */
@Controller
public class VipController {
  private final Class<VipController> clazz = VipController.class;
  private final Logger LOG = LoggerFactory.getLogger(clazz);
  private final String prefix = "filter/";
  @Autowired
  VipService vipService;
  @Autowired
  CardService cardService;
  @Autowired
  BillService billService;

  /**
   * 会员管理
   */
//  @RequestMapping("/customers")
//  public String vipHome(Model model){
//    List<Vip> vips = vipService.getAllVip();
//    model.addAttribute("customers", vips);
//    return "customer";
//  }

  /**
   * 收银
   *
   * @return 符合条件的VIP会员卡
   */
  @RequestMapping("/filter/getVip4Charge")
  public String getVip4Charge(String name, String tel, Model model) {
    List<Vip> vips = vipService.getVip4Charge(name.trim(), tel.trim());
    model.addAttribute("vips", vips);
    return prefix + "chargeVip";
  }

  /**
   * 会员管理
   *
   * @return 符合条件的VIP会员
   */
  @RequestMapping("/filter/getVip4Card")
  public String getVip4Card(String name, String tel, Model model) {
    List<Vip> vips = vipService.getVip(name.trim(), tel.trim());
    model.addAttribute("vips", vips);
    return prefix + "card";
  }

  /**
   * 开户页面
   */
  @RequestMapping("/filter/account")
  public String accountHome() {
    return prefix + "account";
  }

  /**
   * 开卡页面
   */
  @RequestMapping("/filter/card")
  public String cardHome() {
    return prefix + "cardHome";
  }

  /**
   * 开户
   *
   * @param name   姓名
   * @param tel    电话
   * @param remark 备注
   * @param model  返回VIP信息
   * @return 页面
   */
  @RequestMapping("/filter/addVip")
  public String addAccount(String name, String tel, String remark, Model model) {
    List<Vip> vips = new ArrayList<>();
    Vip vip = vipService.addAccount(name, remark, tel);
    vips.add(vip);
    model.addAttribute("vips", vips);
    return prefix + "card";
  }

  /**
   * 会员管理
   */
//  @RequestMapping("/allVips")
//  public String vipHome(Integer pageNum, Integer pageSize, String sortCol, Model model) {
//    LOG.info("/customer: pageNum={},pageSize={},sortCol={}", pageNum, pageSize, sortCol);
//    Page<Vip> pageInfo = vipService.getAllVipPage(pageNum, pageSize, sortCol);
//    model.addAttribute("pageInfo", pageInfo);
//    return "allVips";
//  }
  @RequestMapping("/filter/allVips")
  public String vipHome(Model model) {
    List<Vip> vips = vipService.getAllVips();
    model.addAttribute("vips", vips);
    return prefix + "manage/allVips";
  }

  @RequestMapping("/filter/toUpdateVip")
  public String toUpdateVip(Integer vid, Model model) {
    LOG.info("toUpdateVip(vid={})", vid);
    Vip vip = vipService.getVip(vid);
    model.addAttribute("vip", vip);
    return prefix + "manage/updateVip";
  }

  @RequestMapping("/filter/updateVip")
  public String updateVip(Integer vid, String name, String tel, String remark, Model model) {
    LOG.info("updateVip(vid={},name={},tel={},remark={})", vid,name,tel,remark);
    vipService.updateVip(vid,name,tel,remark);
    Vip vip = vipService.getVip(vid);
    model.addAttribute("vip", vip);
    List<Vip> vips = new ArrayList<>();
    vips.add(vip);
    model.addAttribute("vips", vips);
    return prefix + "showVips";
  }

  /**
   * 删除会员功能
   * 注意:会员卡和账单需要同步删除
   * @param vid
   * @param model
   * @return
   */
  @RequestMapping("/filter/deleteVip")
  public String deleteVip(Integer vid, Model model) {
    LOG.info("deleteVip(vid={})", vid);
    vipService.deleteVipById(vid);
    List<Integer> cardIds = cardService.deleteCardsByUid(vid);
    for(int cid: cardIds) {
      billService.deleteBillsByCid(cid);
    }
    List<Vip> vips = vipService.getAllVips();
    model.addAttribute("vips", vips);
    return prefix + "manage/allVips";
  }
}
