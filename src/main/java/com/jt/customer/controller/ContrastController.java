package com.jt.customer.controller;

import com.jt.customer.entity.ContrastKey;
import com.jt.customer.service.ContrastService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ContrastController {
  private final Class<ContrastController> clazz = ContrastController.class;
  private final Logger LOG = LoggerFactory.getLogger(clazz);
  private final String prefix = "filter/manage/";
  @Autowired
  ContrastService contrastService;

  @RequestMapping("/filter/allContrasts")
  String showContrasts() {
    return prefix + "showContrasts";
  }

//  @RequestMapping("/filter/showContrasts")
//  @ResponseBody
//  Object showContrast(String attr, HttpSession session) {
//    LOG.info("attr={}",attr);
//    Object obj = session.getAttribute(attr);
//    LOG.info("obj={}",obj.toString());
//    return obj.toString();
//  }

  @RequestMapping("/filter/showContrasts")
  String showContrast(Integer key_id, HttpSession session, Model model) {
    for (ContrastKey contrastKey : (List<ContrastKey>) session.getAttribute("contrastKeys")) {
      if (contrastKey.getId() == key_id) {
        Object values = session.getAttribute(contrastKey.getAttr());
        LOG.info("values={}", values.toString());
        model.addAttribute("values", values);
        model.addAttribute("key_id", key_id);
        model.addAttribute("key_name", contrastKey.getName());
        break;
      }
    }
    return prefix + "showContrasts";
  }

  @RequestMapping("/filter/toUpdateContrast")
  String toUpdateContrast(Integer id, String name, Integer value_id, Integer key_id, Model model) {
    model.addAttribute("id", id);
    model.addAttribute("name", name);
    model.addAttribute("value_id", value_id);
    model.addAttribute("key_id", key_id);
    return prefix + "updateContrast";
  }

  @RequestMapping("/filter/updateContrast")
  String toUpdateContrast(Integer id, String name, Integer value_id, Integer key_id, HttpSession session) {
    contrastService.updateContrastValueName(id, name);
    ContrastKey contrastKey = contrastService.getContrastKeyById(key_id);
    session.setAttribute(contrastKey.getAttr(), contrastKey.getValues());
    // 重定向,手动拼接URL
    return "redirect:/filter/showContrasts?key_id=" + key_id;
  }

  @RequestMapping("/filter/toAddContrastValue")
  String addContrastValue(Integer key_id, Model model, HttpSession session) {
    for (ContrastKey contrastKey : (List<ContrastKey>) session.getAttribute("contrastKeys")) {
      if (contrastKey.getId() == key_id) {
        model.addAttribute("key_id", key_id);
        model.addAttribute("attr", contrastKey.getAttr());
        model.addAttribute("name", contrastKey.getName());
        break;
      }
    }
    return prefix + "addContrastValue";
  }

  @RequestMapping("/filter/addContrastValue")
  String addContrastValue(String name, Integer key_id, HttpSession session) {
    int nextValueId = contrastService.getNextValueId(key_id);
    contrastService.addContrastValue(name, nextValueId, key_id);
    ContrastKey contrastKey = contrastService.getContrastKeyById(key_id);
    session.setAttribute(contrastKey.getAttr(), contrastKey.getValues());
    // 重定向,手动拼接URL
    return "redirect:/filter/showContrasts?key_id=" + key_id;
  }

  @RequestMapping("/filter/deleteContrast")
  String deleteContrast(Integer id, Integer key_id, HttpSession session) {
    contrastService.deleteContrastValue(id);
    ContrastKey contrastKey = contrastService.getContrastKeyById(key_id);
    session.setAttribute(contrastKey.getAttr(), contrastKey.getValues());
    // 重定向,手动拼接URL
    return "redirect:/filter/showContrasts?key_id=" + key_id;
  }

}
