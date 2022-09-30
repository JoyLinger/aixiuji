package com.jt.customer.dao;

import com.jt.customer.entity.ContrastKey;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContrastRepository extends CrudRepository<ContrastKey, Long> {
  @Query(value = "SELECT key FROM ContrastKey key")
  List<ContrastKey> getAllContrast();

  @Query(value = "SELECT key FROM ContrastKey key WHERE key.id = ?1")
  ContrastKey getContrastKeyById(int keyId);

  @Modifying
  @Query(value = "UPDATE ContrastValue SET name = ?2 WHERE id = ?1")
  void updateContrastValueName(int id, String name);

  @Query(value = "SELECT MAX(value_id)+1 FROM ContrastValue WHERE key_id = ?1")
  int getNextValueId(int keyId);

  @Modifying
  @Query(value = "INSERT INTO contrast_value(key_id,value_id,name) VALUES(?3,?2,?1)", nativeQuery = true)
  void addContrastValue(String name, int nextValueId, Integer key_id);

  @Modifying
  @Query(value = "DELETE FROM contrast_value WHERE id=?1", nativeQuery = true)
  void deleteContrastValue(int id);
}
