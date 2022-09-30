package com.jt.customer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class configLoader {
  private final static Class<configLoader> clazz = configLoader.class;
  private final Logger logger = LoggerFactory.getLogger(clazz);

  public Properties loadConf(Properties prop, String conf) throws IOException {
    if (new File(conf).exists() && new File(conf).isFile()) {
      // Directly reading local OS file 'prop'
      logger.info("Directly reading local OS file '" + conf + "'");
      FileInputStream fis = new FileInputStream(conf);
      prop.load(fis);
    } else {
      // Searching for 'prop' in CLASSPATH
      logger.info("Searching for '" + conf + "' in CLASSPATH");
      prop.load(clazz.getResourceAsStream(conf));
    }
    return prop;
  }

}
