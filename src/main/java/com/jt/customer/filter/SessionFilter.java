package com.jt.customer.filter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @Author wuwei
 * @Date 2019/12/15 1:09 下午
 */

@WebFilter(urlPatterns = {"/filter/*"})
public class SessionFilter implements Filter {
  private final Class<SessionFilter> clazz = SessionFilter.class;
  private final Logger LOG = LoggerFactory.getLogger(clazz);

  @Override
  public void destroy() {
    LOG.info("filter destroy");
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    LOG.info("filter init");
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest servlet = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;
    //取出session里面的name属性,如果name为空, 就重定向到index界面
    String name = (String) servlet.getSession().getAttribute("name");
    LOG.debug("doFilter():name={}", name);
    if (Objects.isNull(name) || "null".equalsIgnoreCase(name)) {
      response.sendRedirect("/loginHome");
      return;
    }
    //name属性存在, 即会话没有过期, 那么允许本次请求
    filterChain.doFilter(servletRequest, servletResponse);
  }
}
