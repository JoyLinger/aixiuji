package com.jt.customer.dao;

import com.jt.customer.entity.Vip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChargeRepository extends JpaRepository<Vip, Long> {
  /**
   * 查询所有vip
   * 这里@Query中from后面要写类名（区分大小写），而不是表名
   */
  @Query(value = "select v from Vip v")
  public List<Vip> getAllVip();

  /**
   * 根据姓名查询vip,模糊匹配
   * 这里@Query中from后面要写类名（区分大小写），而不是表名
   */
  @Query(value = "select v from Vip v where v.name like '%:name%'")
  public List<Vip> getVipByName(String name);

  /**
   * 根据电话查询vip,模糊匹配
   * 这里@Query中from后面要写类名（区分大小写），而不是表名
   */
  @Query(value = "select v from Vip v where v.tel like '%:tel%'")
  public List<Vip> getVipByTel(String tel);

  /**
   * 根据姓名和电话查询vip,模糊匹配
   * 这里@Query中from后面要写类名（区分大小写），而不是表名
   */
  @Query(value = "select v from Vip v where v.name like '%:name%' and v.tel like '%:tel%'")
  public List<Vip> getVipByNameAndTel(String name, String tel);
}
