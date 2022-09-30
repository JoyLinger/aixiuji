package com.jt.customer.controller;

import com.jt.customer.service.ChargeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by IntelliJ IDEA.
 *
 * @Classname ChargeController
 * @Description 前台收银
 * @Date 2020/7/10 10:52
 * @Created by LinJi
 */
@Controller
public class ChargeController {
  private final Class<ChargeController> clazz = ChargeController.class;
  private final Logger LOG = LoggerFactory.getLogger(clazz);
  private final String prefix = "filter/";
  @Autowired
  ChargeService chargeService;

  /**
   * 会员收银
   */
  @RequestMapping("/filter/chargeVipHome")
  public String chargeVipHome() {
    return prefix + "chargeVipHome";
  }

  /**
   * 散户收银
   */
  @RequestMapping("/filter/charge")
  public String chargeHome() {
    return prefix + "charge";
  }
}
