package com.jt.customer.service;

import com.jt.customer.dao.ContrastRepository;
import com.jt.customer.entity.ContrastKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class ContrastService {
  private final Class<ContrastService> clazz = ContrastService.class;
  private final Logger LOG = LoggerFactory.getLogger(clazz);
  @Autowired
  ContrastRepository contrastRepo;

  public List<ContrastKey> getAllContrasts() {
    List<ContrastKey> contrasts = contrastRepo.getAllContrast();
    LOG.info("contrasts={}", contrasts);
    return contrasts;
  }

  public void updateContrastValueName(int id, String name) {
    contrastRepo.updateContrastValueName(id, name);
  }

  public ContrastKey getContrastKeyById(int keyId) {
    ContrastKey contrastKey = contrastRepo.getContrastKeyById(keyId);
    LOG.info("contrastKey={}", contrastKey);
    return contrastKey;
  }

  public int getNextValueId(int keyId) {
    int nextValueId = contrastRepo.getNextValueId(keyId);
    LOG.info("nextValueId={}", nextValueId);
    return nextValueId;
  }

  public void addContrastValue(String name, int nextValueId, Integer key_id) {
    contrastRepo.addContrastValue(name, nextValueId, key_id);
  }

  public void deleteContrastValue(int id) {
    contrastRepo.deleteContrastValue(id);
    contrastRepo.resetAutoIncrement();
  }
//
//  public List<Contrast> getAllPayMethods() {
//    List<Contrast> payMethods = contrastRepo.getAllPayMethods();
//    LOG.info("payMethods={}", payMethods);
//    return payMethods;
//  }
}
