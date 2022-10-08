package com.jt.customer.dao;

import com.jt.customer.entity.Vip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VipRepository extends JpaRepository<Vip, Integer> {
  String select_all_vip = "select distinct * from Vip v " +
          "left join Card c on v.uid = v.uid and c.active = true " +
          "left join Bill bi on c.id = bi.cid " +
          "left join Bonus bo on c.id = bo.cid";

  /**
   * 查询所有vip
   * 这里@Query中from后面要写类名（区分大小写），而不是表名
   */
  @Query(value = "select v from Vip v")
//  @Query(value = "select distinct v from Vip v left join v.cards c on c.active = true left join c.bills b")
//  @Query(value = select_all_vip, nativeQuery = true)
  List<Vip> getAllVips();

  @Query(value = "SELECT * FROM vip", countQuery = "SELECT COUNT(*) FROM vip", nativeQuery = true)
  Page<Vip> getAllVipPage(Pageable pageable);

  /**
   * 根据姓名查询vip,模糊匹配
   * 这里@Query中from后面要写类名（区分大小写），而不是表名
   */
  @Query(value = "select distinct v from Vip v where v.name like %:name%")
  public List<Vip> getVipByName(String name);

  /**
   * 根据电话查询vip,模糊匹配
   * 这里@Query中from后面要写类名（区分大小写），而不是表名
   */
  @Query(value = "select distinct v from Vip v where v.tel like %:tel%")
  public List<Vip> getVipByTel(String tel);

  /**
   * 根据姓名和电话查询vip,模糊匹配
   * 这里@Query中from后面要写类名（区分大小写），而不是表名
   */
  @Query(value = "select distinct v from Vip v where v.name like %:name% and v.tel like %:tel%")
  public List<Vip> getVipByNameAndTel(String name, String tel);

  /**
   * 根据姓名和电话查询vip,模糊匹配
   * 这里@Query中from后面要写类名（区分大小写），而不是表名
   */
  @Modifying
  @Query(value = "insert into Vip (uid,name,remark,tel,date) values (null,:name,:remark,:tel,:date)", nativeQuery = true)
  public void addAccount(@Param("name") String name, @Param("remark") String remark, @Param("tel") String tel, @Param("date") String date);

  @Query(value = "select distinct uid from Vip where name=:name and remark=:remark and tel=:tel and date=:date", nativeQuery = true)
  public Integer getUidByAll(@Param("name") String name, @Param("remark") String remark, @Param("tel") String tel, @Param("date") String date);

  @Query(value = "select distinct * from Vip where name=:name and remark=:remark and tel=:tel and date=:date", nativeQuery = true)
  public Vip getVipByAll(@Param("name") String name, @Param("remark") String remark, @Param("tel") String tel, @Param("date") String date);

  //  @Query(value = "select distinct * from Vip where uid = :uid", nativeQuery = true)
  @Query(value = "select v from Vip v where v.uid=?1")
  Vip getVipByUid(int uid);

  /**
   * 根据uid更新会员信息表vip
   */
  @Modifying
  @Query(value = "update Vip set name=:name,tel=:tel,remark=:remark where uid=:uid", nativeQuery = true)
  void updateVip(Integer uid, String name, String tel, String remark);

  @Modifying
  @Query(value = "ALTER TABLE vip AUTO_INCREMENT = 1", nativeQuery = true)
  void resetAutoIncrement();
}
