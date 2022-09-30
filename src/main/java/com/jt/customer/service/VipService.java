package com.jt.customer.service;

import com.jt.customer.dao.BillRepository;
import com.jt.customer.dao.CardRepository;
import com.jt.customer.dao.VipRepository;
import com.jt.customer.entity.Bill;
import com.jt.customer.entity.Card;
import com.jt.customer.entity.Vip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Classname VipService
 * @Description 会员管理
 * @Date 2020/7/10 10:54
 * @Created by LinJi
 */
@Transactional  //添加事物管理，不添加的话DAO层可能会报错
@Service
public class VipService {
  private final Class<VipService> clazz = VipService.class;
  private final Logger LOG = LoggerFactory.getLogger(clazz);
  private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  //默认访问第一页
  private final int defaultPageNum = 0;
  //默认每页显示20个
  private final int defaultPageSize = 20;
  @Autowired
  private VipRepository vipRepo;
  @Autowired
  private CardRepository cardRepo;
  @Autowired
  private BillRepository billRepo;

  /**
   * 前台收银
   *
   * @return 所有符合条件的VIP会员
   */
  public List<Vip> getVip4Charge(String name, String tel) {
    List<Vip> vips = new ArrayList<>();
    List<Vip> vipList = new ArrayList<>();
    if ("".equals(name) && "".equals(tel)) {
      LOG.error("name and tel are all ''");
      return null;
    } else if ("".equals(name)) {
      vipList = vipRepo.getVipByTel(tel);
    } else if ("".equals(tel)) {
      vipList = vipRepo.getVipByName(name);
    } else {
      vipList = vipRepo.getVipByNameAndTel(name, tel);
    }
    LOG.info("vipList = {}", vipList);
    return vipList;
  }

  public List<Vip> getVip4ChargeBak(String name, String tel) {
    List<Vip> vips = new ArrayList<>();
    List<Vip> vipList = new ArrayList<>();
    if ("".equals(name) && "".equals(tel)) {
      LOG.error("name and tel are all ''");
      return null;
    } else if ("".equals(name)) {
      vipList = vipRepo.getVipByTel(tel);
    } else if ("".equals(tel)) {
      vipList = vipRepo.getVipByName(name);
    } else {
      vipList = vipRepo.getVipByNameAndTel(name, tel);
    }
    LOG.info("vipList = {}", vipList);
    for (Vip v : vipList) {
      List<Card> cardList = cardRepo.getCardsByUid(v.getUid());
      LOG.info("cardList = {}", cardList);
      // 问题:HibernateException: Found shared references to a collection
      // 原因:两个实体不可以共享同一个集合,参考https://blog.csdn.net/yucharlie/article/details/75646053
      // 解决:新建一个集合finalBillsTemp，将原来的集合元素添加进去，并赋值给另一个实体
      List<Bill> finalBillsTemp = new ArrayList<>();
      for (Card c : cardList) {
        finalBillsTemp.addAll(c.getBills());
      }
//      v.setBills(finalBillsTemp);
      vips.add(v);
      cardList.clear();
    }
    vipList.clear();
    return vips;
  }

  /**
   * 会员管理
   *
   * @return 所有VIP会员
   */
  public Page<Vip> getAllVipPage(Integer pageNum, Integer pageSize, String sortCol) {
    pageNum = pageNum == null ? defaultPageNum : pageNum - 1;//前端显示是第N页,注意后台这里下标需要减1
    pageSize = pageSize == null ? defaultPageSize : pageSize;
    sortCol = sortCol == null ? "uid" : sortCol;
//    Sort.Order order = Sort.Order.asc(sortCol);
    Page<Vip> page = vipRepo.getAllVipPage(PageRequest.of(pageNum, pageSize, Sort.by(sortCol)));
    LOG.info("page={}", page);
    return page;
  }

  public List<Vip> getAllVips() {
    List<Vip> vipList = vipRepo.getAllVips();
    LOG.info("vipList = {}", vipList);
    return vipList;
  }

  public List<Vip> getAllVipBak() {
    List<Vip> vips = new ArrayList<>();
//    List<Card> cardList = cardRepo.getCardList();
//    LOG.info("cardList = {}", cardList);
    List<Vip> vipList = vipRepo.getAllVips();
    LOG.info("vipList = {}", vipList);
//    for(Vip v : vipList){
    // 问题:HibernateException: Found shared references to a collection
    // 原因:两个实体不可以共享同一个集合,参考https://blog.csdn.net/yucharlie/article/details/75646053
    // 解决:新建一个集合finalBillsTemp，将原来的集合元素添加进去，并赋值给另一个实体
//      List<Bill> finalBillsTemp = new ArrayList<>();
//      for(Card c : cardList){
//        if(v.getUid() == c.getVip().getUid()){
//          finalBillsTemp.addAll(c.getBills());
//        }
//      }
//      v.setBills(finalBillsTemp);
//      vips.add(v);
//    }
//    cardList.clear();
//    vipList.clear();
//    return vips;
    return vipList;
  }

  /**
   * 会员管理
   *
   * @return 符合条件的VIP会员
   */
  public List<Vip> getVip(String name, String tel) {
    List<Vip> vipList = new ArrayList<>();
    if ("".equals(name) && "".equals(tel)) {
      LOG.error("name and tel are all ''");
      return null;
    } else if ("".equals(name)) {
      vipList = vipRepo.getVipByTel(tel);
    } else if ("".equals(tel)) {
      vipList = vipRepo.getVipByName(name);
    } else {
      vipList = vipRepo.getVipByNameAndTel(name, tel);
    }
    LOG.info("vipList={}", vipList);
    return vipList;
  }

  /**
   * 开户
   *
   * @param name   姓名
   * @param remark 备注
   * @param tel    电话
   * @return vip
   */
  public Vip addAccount(String name, String remark, String tel) {
    String date = sdf.format(new Date());
    vipRepo.addAccount(name, remark, tel, date);
    int uid = vipRepo.getUidByAll(name, remark, tel, date);
    Vip vip = new Vip();
    vip.setUid(uid);
    vip.setName(name);
    vip.setRemark(remark);
    vip.setTel(tel);
    vip.setDate(date);
    return vip;
  }

  public Vip getVip(int uid) {
    Vip vip = vipRepo.getVipByUid(uid);
    LOG.info("vip={}", vip.toString());
    return vip;
  }
}
