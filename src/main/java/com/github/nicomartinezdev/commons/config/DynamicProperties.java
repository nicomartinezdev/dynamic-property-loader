package com.github.nicomartinezdev.commons.config;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

@Component
public class DynamicProperties {

  private PropertiesConfiguration propertiesConfiguration;
  private static final String DEFAULT_DYNAMIC_PROPERTY_FILE = "dynamic.properties";
  private String dynamicPropertyFile = DEFAULT_DYNAMIC_PROPERTY_FILE;
  private static final long DEFAULT_REFRESH_DELAY = TimeUnit.MINUTES.toMillis(5);
  private long refreshDelayMillis = DEFAULT_REFRESH_DELAY;

  /*
  * @param dynamicPropertyIncludeFile - path to file containing dynamic properties
  * @param refreshDelayMillis - time to wait before checking if properties have been modified (default: 300,000ms or 5 mins)
  * */
  public DynamicProperties(String dynamicPropertyFile, long refreshDelayMillis) throws ConfigurationException {
    this.dynamicPropertyFile = StringUtils.isNotBlank(dynamicPropertyFile) ? dynamicPropertyFile : DEFAULT_DYNAMIC_PROPERTY_FILE;
    this.refreshDelayMillis = refreshDelayMillis > 0 ? refreshDelayMillis : DEFAULT_REFRESH_DELAY;
    init();
  }

  @PostConstruct
  private void init() throws ConfigurationException {
    propertiesConfiguration = new PropertiesConfiguration(dynamicPropertyFile);
    propertiesConfiguration.setReloadingStrategy(fileChangedReloadingStrategy());
  }

  public Object getProperty(String key) {
    return propertiesConfiguration.getProperty(key);
  }

  private FileChangedReloadingStrategy fileChangedReloadingStrategy() {
    FileChangedReloadingStrategy fileChangedReloadingStrategy = new FileChangedReloadingStrategy();
    fileChangedReloadingStrategy.setRefreshDelay(refreshDelayMillis);
    return fileChangedReloadingStrategy;
  }
}
