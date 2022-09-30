package com.jt.customer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Reader {
  private final static Class<Reader> clazz = Reader.class;
  private final String DOS_SEPARATOR = "\r\n";
  private final String UNIX_SEPARATOR = "\n";
  private final String MAC_SEPARATOR = "\r";
  private final String[] separators = {DOS_SEPARATOR, UNIX_SEPARATOR, MAC_SEPARATOR};
  private final int SIZE = 4096;
  private final Logger logger = LoggerFactory.getLogger(clazz);

  public Reader() {
    logger.info("Loading " + this.getClass().getName() + " constructor successfully");
  }

  public String[] toStringArray(String path, String charset) {
    String content = toString(path, charset);
    for (String sep : separators) {
      if (content.matches("^.*" + sep + "$")) {
        return content.split(sep);
      }
    }
    return new String[]{content};
  }

  public String toString(String path, String charset) {
    File file = new File(path);
    Long fileLen = file.length();
    byte[] fileContent = new byte[fileLen.intValue()];
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(file);
      fis.read(fileContent);
      return new String(fileContent, charset);
    } catch (UnsupportedEncodingException uee) {
      logger.error("The OS does not support " + charset, uee);
      return null;
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
      return null;
    } finally {
      if (fis != null) {
        try {
          fis.close();
        } catch (IOException e) {
          logger.error(e.getMessage(), e);
        }
      }
    }
  }

  public List<String> toStringList(String path, String charset) {
    try {
      return Files.readAllLines(Paths.get(path), Charset.forName(charset));
    } catch (UnsupportedEncodingException uee) {
      logger.error("The OS does not support " + Charset.forName(charset), uee);
      return null;
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
      return null;
    }
  }

  /**
   * 按字节流读取文件
   *
   * @param path 文件路径
   * @return
   */
  public byte[] toByteArray(String path) {
    byte[] result = null;
    byte[] buf = new byte[SIZE];
    File f = new File(path);
    FileInputStream fis = null;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      fis = new FileInputStream(f);
      int len = 0;
      while ((len = fis.read(buf)) != -1) {
        // 每次读取SIZE长度字节并存入buf变量, 在写入baos
        baos.write(buf, 0, len);
      }
      result = baos.toByteArray();
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    return result;
  }

}
