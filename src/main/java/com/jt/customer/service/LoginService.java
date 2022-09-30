package com.jt.customer.service;

import com.jt.customer.dao.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 *
 * @Classname LoginService
 * @Description 登录页面
 * @Date 2020/7/10 10:54
 * @Created by LinJi
 */
@Transactional  //添加事物管理，不添加的话DAO层可能会报错
@Service
public class LoginService {
  private final Class<LoginService> clazz = LoginService.class;
  private final Logger LOG = LoggerFactory.getLogger(clazz);
  @Autowired
  private UserRepository userRepo;

  /**
   * 登录
   *
   * @param account  账号
   * @param password 密码
   * @return 角色id，为0则是管理员，允许登录
   */
  public String login(String account, String password) {
    return userRepo.validate(account, password);
  }
}
