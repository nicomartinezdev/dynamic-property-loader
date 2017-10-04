# Dynamic Property Loader

## Installation

For Maven Projects use:

```
 <dependency>
    <groupId>com.github.nicomartinezdev</groupId>
    <artifactId>dynamic-property-loader</artifactId>
    <version>1.0</version>
  </dependency>  
```

## Usage

This project wraps the Apache commons `PropertiesConfiguration` which allows for dynamic reloading of
properties by checking the *last-modified* timestamp of the property file. Using it is just a matter
of instantiating new DynamicProperties with the path to a property file and a refresh delay in
milliseconds. The refresh delay ensures that you're not re-reading the property file every time you
attempt to access a value, instead it only does so if the refresh time has elapsed and the property
file has been modified. To better understand the Apache
[PropertiesConfiguration](https://commons.apache.org/proper/commons-configuration/apidocs/org/apache/commons/configuration2/PropertiesConfiguration.html)
and the
[FileChangedReloadingStrategy](https://commons.apache.org/proper/commons-configuration/javadocs/v1.10/apidocs/org/apache/commons/configuration/reloading/FileChangedReloadingStrategy.html)
take a look at the documentation linked here.

## Example

```java

import static com.google.common.base.Preconditions.checkNotNull;

public class SomeHttpClient

  private final DynamicProperties dynamicProps;
  private final String host;
  private final int port;

  public SomeHttpClient(String host, int port, DynamicProperties dynamicProps) {
    this.dynamicProps = checkNotNull(dynamicProps);
    this.host = host;
    this.port = port;
  }

  public HttpResponse get(String path) {
   // Some logic goes here
  }

  private int getConnectionTimeout() {
    return (int) getProperty("connectionTimeoutMillis");
  }

  private int getResponseTimeout() {
    return (int) getProperty("responseTimeoutMillis");
  }

  private int getMaxConnections() {
    return (int) getProperty("maxConnections");
  }

  private Object getProperty(String key) {
    return dynamicProps.getProperty(key);
  }

```

And when building your client you would pass DynamicProperties like so:

```java
  ...
  DynamicProperties dynamicHttpClientProperties = new DynamicProperties("httpClient.properties", TimeUnit.MINUTES.toMillis(5));
  SomeHttpClient client = new SomeHttpClient("someHost", 8888, dynamicHttpClientProperties);
  ...
```
