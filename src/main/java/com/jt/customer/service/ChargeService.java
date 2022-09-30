package com.jt.customer.service;

import com.jt.customer.dao.ChargeRepository;
import com.jt.customer.entity.Vip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Classname ChargeService
 * @Description 会员管理
 * @Date 2020/7/10 10:54
 * @Created by LinJi
 */
@Transactional  //添加事物管理，不添加的话DAO层可能会报错
@Service
public class ChargeService {
  private final Class<ChargeService> clazz = ChargeService.class;
  private final Logger LOG = LoggerFactory.getLogger(clazz);
  @Autowired
  private ChargeRepository chargeRepo;

  /**
   * 会员管理
   *
   * @return 所有VIP会员
   */
  public List<Vip> getAllVip() {
    return chargeRepo.getAllVip();
  }

  /**
   * 收银/会员管理
   *
   * @return 符合条件的VIP会员
   */
  public List<Vip> getVip(String name, String tel) {
    if ("".equals(name) && "".equals(tel)) {
      return null;
    } else if ("".equals(name)) {
      return chargeRepo.getVipByTel(tel);
    } else if ("".equals(tel)) {
      return chargeRepo.getVipByName(name);
    } else {
      return chargeRepo.getVipByNameAndTel(name, tel);
    }
  }
}
