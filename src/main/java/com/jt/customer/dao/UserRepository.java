package com.jt.customer.dao;

import com.jt.customer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
  /**
   * 验证账号密码
   * 这里@Query中from后面要写类名（区分大小写），而不是表名
   */
  @Query(value = "select role from User where name = :account and password = :password")
  public String validate(@Param("account") String account, @Param("password") String password);
}
