package com.jt.customer.filter;

import javax.servlet.*;
import java.io.IOException;

//@Component
//@Order(-1)
public class MyFilter implements Filter {
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    System.out.println("-----do Filter-----");
    chain.doFilter(request, response);
  }
}