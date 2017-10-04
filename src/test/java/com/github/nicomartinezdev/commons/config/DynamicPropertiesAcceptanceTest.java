package com.github.nicomartinezdev.commons.config;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class DynamicPropertiesAcceptanceTest {

  @Rule
  public TemporaryFolder tempFolder = new TemporaryFolder();
  private static final String TEST_PROPERTY_FILE = "test.properties";
  private static final long TEST_REFRESH_RATE = 1;

  private File propertyFile;

  @Before
  public void setUp() throws IOException {
    propertyFile = createPropertyFileWithContent(TEST_PROPERTY_FILE, "testKey = testValue");
    makeLastModifiedSufficientlyOldToTriggerPropertyReload(propertyFile);
  }

  @Test
  public void initializesWithTheValuesInPropertyFile() throws ConfigurationException {
    DynamicProperties properties = new DynamicProperties(propertyFile.getPath(), TEST_REFRESH_RATE);
    assertEquals(properties.getProperty("testKey"), "testValue");
  }

  @Test
  public void returnsUpdatedValuesIfPropertyFileIsModified() throws ConfigurationException, IOException {
    DynamicProperties properties = new DynamicProperties(propertyFile.getPath(), TEST_REFRESH_RATE);
    overwritePropertyFile(propertyFile, "testKey = newTestValue");
    assertEquals(properties.getProperty("testKey"), "newTestValue");
  }

  @Test
  public void propertyFileCanIncludePropertiesFromOtherPropertyFiles() throws ConfigurationException, IOException {
    DynamicProperties properties = new DynamicProperties(propertyFile.getPath(), TEST_REFRESH_RATE);

    File anotherPropertyFile = createPropertyFileWithContent("other.properties", "otherKey = otherValue");
    overwritePropertyFile(propertyFile, "include = " + anotherPropertyFile.getPath());

    assertEquals(properties.getProperty("otherKey"), "otherValue");
  }

  private File createPropertyFileWithContent(String name, String content) throws IOException {
    File tmp = tempFolder.newFile(name);
    FileWriter writer = new FileWriter(tmp);
    writer.write(content);
    writer.close();
    return tmp;
  }

  private void overwritePropertyFile(File file, String newContent) throws IOException {
    FileWriter writer = new FileWriter(file);
    writer.write(newContent);
    writer.close();
  }

  private void makeLastModifiedSufficientlyOldToTriggerPropertyReload(File propertyFile) {
    propertyFile.setLastModified(1000000000);
  }
}
