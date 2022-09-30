package com.jt.customer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginInterceptor implements HandlerInterceptor {

  private final Class<HandlerInterceptor> clazz = HandlerInterceptor.class;
  private final Logger LOG = LoggerFactory.getLogger(clazz);

  //在请求处理之前进行调用（Controller方法调用之前）
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler) throws IOException {

    String roleId = (String) request.getSession().getAttribute("roleId");
    LOG.info("preHandle(): roleId={}", roleId);
    //管理员才允许登录
    if (null == roleId) {
      //跳到登录界面
      response.sendRedirect("/loginHome");
      //返回false取消当前请求
      return false;
    } else if ("0".equals(roleId)) {
      // 只有返回true才会继续向下执行
      return true;
    } else {
      //跳到登录界面,显示提示
      response.sendRedirect("/login");
      //返回false取消当前请求
      return false;
    }
  }

}
