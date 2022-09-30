package com.jt.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 注册拦截器
 * 新建配置类实现WebMvcConfigurer类，重写addInterceptors方法
 * 既然要增加自己的拦截器，那当然要得到springboot加入拦截器的入口，然后把我们自己写的拦截器也注册到springboot中让其起作用
 * 需要加入@Configuration注解，在springboot启动的时候就会该配置类就会被扫描并加载，从而将我们的拦截器注册进去。这时候的拦截器已经可以正常工作了
 */
@Configuration
public class LoginConfiguration implements WebMvcConfigurer {
  @Autowired
  private LoginInterceptor loginInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    //添加对用户未登录的拦截器，并添加排除项
//    registry.addInterceptor(loginInterceptor).addPathPatterns("/**")
//            .excludePathPatterns("/js/**","/css/**","/img/**","/css/**","/plugins/**")//排除样式、脚本、图片等资源文件
////            .excludePathPatterns("/login")
//            .excludePathPatterns("/loginHome");

  }

}
