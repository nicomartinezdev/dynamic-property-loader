package com.github.nicomartinezdev.commons.config.examples;

import com.github.nicomartinezdev.commons.config.DynamicProperties;

public class DynamicEmployee {

  private final String name;
  private final DynamicProperties dynamicEmployeeProperties;

  public DynamicEmployee(String name, DynamicProperties dynamicEmployeeProperties) {
    this.name = name;
    this.dynamicEmployeeProperties = dynamicEmployeeProperties;
  }

  public String getName() {
    return name;
  }

  public String getSalary() {
    return (String) dynamicEmployeeProperties.getProperty("salary");
  }

}
