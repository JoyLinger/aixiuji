package com.jt.customer.controller;

import com.jt.customer.entity.ContrastKey;
import com.jt.customer.entity.StaticContrast;
import com.jt.customer.service.ContrastService;
import com.jt.customer.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Classname DisplayController
 * @Description 登录
 * @Date 2020/7/10 10:52
 * @Created by LinJi
 */
@Controller
//@SessionAttributes("projects")
public class LoginController {
  private final Class<LoginController> clazz = LoginController.class;
  private final Logger LOG = LoggerFactory.getLogger(clazz);
  @Autowired
  LoginService loginService;
  @Autowired
  ContrastService contrastService;

  /**
   * 登录首页
   */
  @RequestMapping("/loginHome")
  public String loginHome() {
    return "index";
  }

  /**
   * 登录
   *
   * @return index
   */
  @RequestMapping("/index")
  public String index(String account, String password, Model model, HttpSession session) {
    String roleId = loginService.login(account, password);
    roleId = roleId == null ? "" : roleId;
    String html;
    switch (roleId.trim()) {
      case "0":
        // 登录成功，进入首页
        html = "filter/index";
        session.setAttribute("roleId", roleId);
        session.setAttribute("name", account);
        List<ContrastKey> contrasts = contrastService.getAllContrasts();
        for (ContrastKey contrast : contrasts) {
          session.setAttribute(contrast.getAttr(), contrast.getValues());
          switch (contrast.getAttr()) {
            case "projects":
              StaticContrast.projects = contrast.getValues();
              break;
            case "operations":
              StaticContrast.operations = contrast.getValues();
              break;
            case "payMethods":
              StaticContrast.payMethods = contrast.getValues();
              break;
            case "roles":
              StaticContrast.roles = contrast.getValues();
              break;
            case "cardTypes":
              StaticContrast.cardTypes = contrast.getValues();
              break;
            case "bonusTimes":
              StaticContrast.bonusTimes = contrast.getValues();
              break;
          }
        }
        session.setAttribute("contrastKeys", contrasts);
        StaticContrast.contrastKeys = contrasts;
        break;
      case "":
        model.addAttribute("response", "账号或密码错误");
        html = "relogin";
        break;
      case "-1":
        model.addAttribute("response", "权限不足");
        html = "relogin";
        break;
      default:
        model.addAttribute("response", "未知账号");
        html = "relogin";
    }
    model.addAttribute("roleId", roleId);
    //通过thymeleaf映射到对应的index页面
    LOG.info("{}.html", html);
    return html;
  }

  /**
   * 登出
   */
  @RequestMapping("/logout")
  public String logout(HttpSession session) {
    LOG.info("logout():name={}", session.getAttribute("name"));
    //spring-session-jdbc插件会自动删除表中用户登录记录
    session.invalidate();
    return "index";
  }
}
