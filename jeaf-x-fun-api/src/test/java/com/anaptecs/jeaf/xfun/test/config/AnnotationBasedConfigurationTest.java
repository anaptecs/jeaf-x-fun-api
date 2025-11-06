/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import com.anaptecs.jeaf.xfun.api.XFunRuntimeException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AnnotationBasedConfigurationTest {
  @Test
  @Order(10)
  public void testConfigurationLoading( ) {
    // Try to load configuration where neither default nor custom configuration is available.
    try {
      new TestAnnotationConfiguration(false);
    }
    catch (XFunRuntimeException e) {
      assertEquals(
          "Configuration error. Default configuration class 'null' is not in classpath AND no custom configuration could be loaded from resource file 'META-INF/TEST/null'. Please check your custom configuration file and your classpath.",
          e.getMessage(), "Unexpected error message in exception.");
    }

    // Try to read all values from default configuration
    TestAnnotationConfiguration.DEFAULT_CONFIGURATION_CLASS_NAME = DefaultConfiguration.class.getName();
    TestAnnotationConfiguration lConfiguration = new TestAnnotationConfiguration(false);
    assertTrue(lConfiguration.isDefaultConfigurationAvailable(), "Default configuration not found");
    assertFalse(lConfiguration.isCustomConfigurationAvailable(), "Custom configuration found");
    assertEquals(DefaultConfiguration.class, lConfiguration.getConfigurationClass(),
        "Configuration was read from wrong source.");

    // Try to load custom configuration that has errors.
    try {
      TestAnnotationConfiguration.DEFAULT_CONFIGURATION_CLASS_NAME = null;
      TestAnnotationConfiguration.CUSTOM_CONFIG_RESOURCE_NAME = "CUSTOM_TEST_ANNOTATION";
      TestAnnotationConfiguration.ERROR_IN_CHECK = true;
      lConfiguration = new TestAnnotationConfiguration(true);
      fail("Invalid custom configuration must cause exception.");
    }
    catch (XFunRuntimeException e) {
      TestAnnotationConfiguration.ERROR_IN_CHECK = false;
      assertEquals("Found 1 error(s) during analysis of configuration. Please see error log for further details.",
          e.getMessage());
    }
    // Try to load custom configuration that has errors but this time they should be ignored.
    TestAnnotationConfiguration.DEFAULT_CONFIGURATION_CLASS_NAME = null;
    TestAnnotationConfiguration.CUSTOM_CONFIG_RESOURCE_NAME = "CUSTOM_TEST_ANNOTATION";
    TestAnnotationConfiguration.ERROR_IN_CHECK = true;
    lConfiguration = new TestAnnotationConfiguration(false);
    assertFalse(lConfiguration.isDefaultConfigurationAvailable(), "Default configuration found");
    assertTrue(lConfiguration.isCustomConfigurationAvailable(), "Custom configuration not found");
    assertEquals(CustomConfiguration.class, lConfiguration.getConfigurationClass(),
        "Configuration was read from wrong source.");

    TestAnnotationConfiguration.DEFAULT_CONFIGURATION_CLASS_NAME =
        "com.anaptecs.jeaf.xfun.test.config.ClassWithoutAnnotation";
    TestAnnotationConfiguration.CUSTOM_CONFIG_RESOURCE_NAME = "CUSTOM_TEST_ANNOTATION";
    TestAnnotationConfiguration.ERROR_IN_CHECK = false;
    lConfiguration = new TestAnnotationConfiguration(false);
    assertFalse(lConfiguration.isDefaultConfigurationAvailable(), "Default configuration found");
    assertTrue(lConfiguration.isCustomConfigurationAvailable(), "Custom configuration not found");
    assertEquals(CustomConfiguration.class, lConfiguration.getConfigurationClass(),
        "Configuration was read from wrong source.");
  }

  @Test
  @Order(20)
  public void testDefaultConfigurationOnlyAccess( ) {
    // Try to read all values from default configuration
    TestAnnotationConfiguration.DEFAULT_CONFIGURATION_CLASS_NAME = DefaultConfiguration.class.getName();
    TestAnnotationConfiguration.CUSTOM_CONFIG_RESOURCE_NAME = "CUSTOM_CONFIG_RESOURCE";
    TestAnnotationConfiguration lConfiguration = new TestAnnotationConfiguration(false);
    assertTrue(lConfiguration.isDefaultConfigurationAvailable(), "Default configuration not found");
    assertFalse(lConfiguration.isCustomConfigurationAvailable(), "Custom configuration found");

    // Try to read all values from default configuration
    TestAnnotationConfiguration.DEFAULT_CONFIGURATION_CLASS_NAME = DefaultConfiguration.class.getName();
    TestAnnotationConfiguration.CUSTOM_CONFIG_RESOURCE_NAME = null;
    lConfiguration = new TestAnnotationConfiguration(false);
    assertTrue(lConfiguration.isDefaultConfigurationAvailable(), "Default configuration not found");
    assertFalse(lConfiguration.isCustomConfigurationAvailable(), "Custom configuration found");

    // Test handling of null values
    lConfiguration.testAsListImplementationWithNullValues();

    // Test access to default configuration values.

    // Booleans
    assertEquals(true, lConfiguration.booleanValue());
    List<Boolean> lBooleanValues = lConfiguration.booleanValues();
    assertEquals(3, lBooleanValues.size(), "List has unexpected size.");
    assertEquals(true, lBooleanValues.get(0).booleanValue());
    assertEquals(false, lBooleanValues.get(1).booleanValue());
    assertEquals(true, lBooleanValues.get(2).booleanValue());

    // Bytes
    assertEquals(127, lConfiguration.byteValue());
    List<Byte> lByteValues = lConfiguration.byteValues();
    assertEquals(3, lByteValues.size(), "List has unexpected size.");
    assertEquals(0, lByteValues.get(0).byteValue());
    assertEquals(1, lByteValues.get(1).byteValue());
    assertEquals(2, lByteValues.get(2).byteValue());

    // Shorts, not in meanings of pants ;-)
    assertEquals(-1, lConfiguration.shortValue());
    List<Short> lShortValues = lConfiguration.shortValues();
    assertEquals(4, lShortValues.size(), "List has unexpected size.");
    assertEquals(-1, lShortValues.get(0).shortValue());
    assertEquals(-2, lShortValues.get(1).shortValue());
    assertEquals(-3, lShortValues.get(2).shortValue());
    assertEquals(-4, lShortValues.get(3).shortValue());

    // Ints
    assertEquals(-12, lConfiguration.intValue());
    List<Integer> lIntValues = lConfiguration.intValues();
    assertEquals(4, lIntValues.size(), "List has unexpected size.");
    assertEquals(-1, lIntValues.get(0).intValue());
    assertEquals(1, lIntValues.get(1).intValue());
    assertEquals(-2, lIntValues.get(2).intValue());
    assertEquals(2, lIntValues.get(3).intValue());

    // Longs
    assertEquals(88889999, lConfiguration.longValue());
    List<Long> lLongValues = lConfiguration.longValues();
    assertEquals(4, lLongValues.size(), "List has unexpected size.");
    assertEquals(1111, lLongValues.get(0).longValue());
    assertEquals(2222, lLongValues.get(1).longValue());
    assertEquals(3333, lLongValues.get(2).longValue());
    assertEquals(4444, lLongValues.get(3).longValue());

    // Floats
    assertEquals(0.889f, lConfiguration.floatValue());
    List<Float> lFloatValues = lConfiguration.floatValues();
    assertEquals(2, lFloatValues.size(), "List has unexpected size.");
    assertEquals(3.1f, lFloatValues.get(0).floatValue());
    assertEquals(3.14f, lFloatValues.get(1).floatValue());

    // Double
    assertEquals(47.11, lConfiguration.doubleValue());
    List<Double> lDoubleValues = lConfiguration.doubleValues();
    assertEquals(3, lDoubleValues.size(), "List has unexpected size.");
    assertEquals(1.0, lDoubleValues.get(0).doubleValue());
    assertEquals(2.1, lDoubleValues.get(1).doubleValue());
    assertEquals(3.2, lDoubleValues.get(2).doubleValue());

    // Strings
    assertEquals("Hello", lConfiguration.stringValue());
    List<String> lStringValues = lConfiguration.stringValues();
    assertEquals(2, lStringValues.size(), "List has unexpected size.");
    assertEquals("Hello", lStringValues.get(0));
    assertEquals("World", lStringValues.get(1));

    // Enums
    assertEquals(TestEnum.YES, lConfiguration.testEnum());
    List<TestEnum> lTestEnums = lConfiguration.testEnums();
    assertEquals(3, lTestEnums.size(), "List has unexpected size.");
    assertEquals(TestEnum.YES, lTestEnums.get(0));
    assertEquals(TestEnum.NO, lTestEnums.get(1));
    assertEquals(TestEnum.MAYBE, lTestEnums.get(2));

    // Classes
    assertEquals(Object.class, lConfiguration.classValue());
    List<Class<?>> lClassValues = lConfiguration.classValues();
    assertEquals(0, lClassValues.size(), "List has unexpected size.");

    // Classes with default
    assertEquals(DefaultTestInterfaceImpl.class, lConfiguration.classWithDefault(),
        "Default value for class not used.");
  }

  @Test
  @Order(30)
  public void testCustomConfigurationOnlyAccess( ) {
    // Try to read all values from default configuration
    TestAnnotationConfiguration.DEFAULT_CONFIGURATION_CLASS_NAME = null;
    TestAnnotationConfiguration.CUSTOM_CONFIG_RESOURCE_NAME = "CUSTOM_TEST_ANNOTATION";
    TestAnnotationConfiguration lConfiguration = new TestAnnotationConfiguration(false);
    assertFalse(lConfiguration.isDefaultConfigurationAvailable(), "Default configuration found");
    assertTrue(lConfiguration.isCustomConfigurationAvailable(), "Custom configuration not found");

    // Test access to default configuration values.

    // Booleans
    assertEquals(false, lConfiguration.booleanValue());
    List<Boolean> lBooleanValues = lConfiguration.booleanValues();
    assertEquals(3, lBooleanValues.size(), "List has unexpected size.");
    assertEquals(false, lBooleanValues.get(0).booleanValue());
    assertEquals(true, lBooleanValues.get(1).booleanValue());
    assertEquals(false, lBooleanValues.get(2).booleanValue());

    // Bytes
    assertEquals(-10, lConfiguration.byteValue());
    List<Byte> lByteValues = lConfiguration.byteValues();
    assertEquals(3, lByteValues.size(), "List has unexpected size.");
    assertEquals(10, lByteValues.get(0).byteValue());
    assertEquals(100, lByteValues.get(1).byteValue());
    assertEquals(127, lByteValues.get(2).byteValue());

    // Shorts, not in meanings of pants ;-)
    assertEquals(-100, lConfiguration.shortValue());
    List<Short> lShortValues = lConfiguration.shortValues();
    assertEquals(4, lShortValues.size(), "List has unexpected size.");
    assertEquals(-10, lShortValues.get(0).shortValue());
    assertEquals(-20, lShortValues.get(1).shortValue());
    assertEquals(-30, lShortValues.get(2).shortValue());
    assertEquals(-40, lShortValues.get(3).shortValue());

    // Ints
    assertEquals(-12111, lConfiguration.intValue());
    List<Integer> lIntValues = lConfiguration.intValues();
    assertEquals(4, lIntValues.size(), "List has unexpected size.");
    assertEquals(-10, lIntValues.get(0).intValue());
    assertEquals(10, lIntValues.get(1).intValue());
    assertEquals(-200, lIntValues.get(2).intValue());
    assertEquals(200, lIntValues.get(3).intValue());

    // Longs
    assertEquals(888899997, lConfiguration.longValue());
    List<Long> lLongValues = lConfiguration.longValues();
    assertEquals(4, lLongValues.size(), "List has unexpected size.");
    assertEquals(11110, lLongValues.get(0).longValue());
    assertEquals(22220, lLongValues.get(1).longValue());
    assertEquals(33330, lLongValues.get(2).longValue());
    assertEquals(44440, lLongValues.get(3).longValue());

    // Floats
    assertEquals(0.99889f, lConfiguration.floatValue());
    List<Float> lFloatValues = lConfiguration.floatValues();
    assertEquals(2, lFloatValues.size(), "List has unexpected size.");
    assertEquals(30.1f, lFloatValues.get(0).floatValue());
    assertEquals(30.14f, lFloatValues.get(1).floatValue());

    // Double
    assertEquals(47.12, lConfiguration.doubleValue());
    List<Double> lDoubleValues = lConfiguration.doubleValues();
    assertEquals(3, lDoubleValues.size(), "List has unexpected size.");
    assertEquals(-1.0, lDoubleValues.get(0).doubleValue());
    assertEquals(-2.1, lDoubleValues.get(1).doubleValue());
    assertEquals(-3.2, lDoubleValues.get(2).doubleValue());

    // Strings
    assertEquals("Goodbye", lConfiguration.stringValue());
    List<String> lStringValues = lConfiguration.stringValues();
    assertEquals(2, lStringValues.size(), "List has unexpected size.");
    assertEquals("Goodbye", lStringValues.get(0));
    assertEquals("Moon", lStringValues.get(1));

    // Enums
    assertEquals(TestEnum.MAYBE, lConfiguration.testEnum());
    List<TestEnum> lTestEnums = lConfiguration.testEnums();
    assertEquals(1, lTestEnums.size(), "List has unexpected size.");
    assertEquals(TestEnum.NO, lTestEnums.get(0));

    // Classes
    assertEquals(String.class, lConfiguration.classValue());
    List<Class<?>> lClassValues = lConfiguration.classValues();
    assertEquals(3, lClassValues.size(), "List has unexpected size.");
    assertEquals(Boolean.class, lClassValues.get(0));
    assertEquals(Byte.class, lClassValues.get(1));
    assertEquals(Short.class, lClassValues.get(2));

    // Classes with default
    assertEquals(TestInterface.class, lConfiguration.classWithDefault(), "Default value for class not used.");

  }

  @Test
  @Order(40)
  public void testFallbackToDefaultForClasses( ) {
    TestAnnotationConfiguration.DEFAULT_CONFIGURATION_CLASS_NAME = DefaultConfiguration.class.getName();
    TestAnnotationConfiguration.CUSTOM_CONFIG_RESOURCE_NAME = "CUSTOM_TEST_ANNOTATION";
    TestAnnotationConfiguration lConfiguration = new TestAnnotationConfiguration(false);

    assertEquals(TestInterface.class, lConfiguration.classWithDefault(), "Default value for class not used.");
    TestInterface lInstance = lConfiguration.newInstance();
    assertEquals(DefaultTestInterfaceImpl.class, lInstance.getClass(), "Expected default implementation class.");
  }

  @Test
  @Order(50)
  public void testUsageOfInvalidAnnotation( ) {
    // Try to load load invalid configuration (invalid custom, no default).
    TestAnnotationConfiguration.DEFAULT_CONFIGURATION_CLASS_NAME = null;
    TestAnnotationConfiguration.CUSTOM_CONFIG_RESOURCE_NAME = "CLASS_WITHOUT_ANNOTATION";
    try {
      new TestAnnotationConfiguration(false);
      fail("Invalid configuration is expected to cause XFunRuntimeException");
    }
    catch (XFunRuntimeException e) {
      assertEquals(
          "Configuration error. Default configuration class 'null' is not in classpath AND no custom configuration could be loaded from resource file 'META-INF/TEST/CLASS_WITHOUT_ANNOTATION'. Please check your custom configuration file and your classpath.",
          e.getMessage());
    }

    // Try to load load invalid configuration (invalid custom, default not available in classpath).
    TestAnnotationConfiguration.DEFAULT_CONFIGURATION_CLASS_NAME = "a.b.c.D";
    TestAnnotationConfiguration.CUSTOM_CONFIG_RESOURCE_NAME = "CLASS_WITHOUT_ANNOTATION";
    try {
      new TestAnnotationConfiguration(false);
      fail("Invalid configuration is expected to cause XFunRuntimeException");
    }
    catch (XFunRuntimeException e) {
      assertEquals(
          "Configuration error. Default configuration class 'a.b.c.D' is not in classpath AND no custom configuration could be loaded from resource file 'META-INF/TEST/CLASS_WITHOUT_ANNOTATION'. Please check your custom configuration file and your classpath.",
          e.getMessage());
    }

    // Try to load configuration from class that does not have annotation.
    // Try to read all values from default configuration
    TestAnnotationConfiguration.DEFAULT_CONFIGURATION_CLASS_NAME = DefaultConfiguration.class.getName();
    TestAnnotationConfiguration.CUSTOM_CONFIG_RESOURCE_NAME = "CLASS_WITHOUT_ANNOTATION";
    TestAnnotationConfiguration lConfiguration = new TestAnnotationConfiguration(false);
    assertTrue(lConfiguration.isDefaultConfigurationAvailable(), "Default configuration not found");
    assertFalse(lConfiguration.isCustomConfigurationAvailable(), "Custom configuration found");

    // Test usage of invalid annotation type
    try {
      new InvalidAnnotationConfiguration(true);
    }
    catch (XFunRuntimeException e) {
      assertEquals(
          "The annotation com.anaptecs.jeaf.xfun.test.config.InvalidAnnotation does not have retention RUNTIME. Please correction the annotation by adding '@Retention(RetentionPolicy.RUNTIME)'.",
          e.getMessage());
    }
  }

  @Test
  @Order(60)
  public void testNewInstanceOperations( ) {
    TestAnnotationConfiguration.DEFAULT_CONFIGURATION_CLASS_NAME = DefaultConfiguration.class.getName();
    TestAnnotationConfiguration.CUSTOM_CONFIG_RESOURCE_NAME = "CUSTOM_TEST_ANNOTATION";
    TestAnnotationConfiguration.ERROR_IN_CHECK = false;
    TestAnnotationConfiguration lConfiguration = new TestAnnotationConfiguration(true);

    // Test try to create new instances
    lConfiguration.testTryNewInstance();

    // Test newInstance(...) operations.
    lConfiguration.testNewInstanceOperations();

    // Test newInstances(...) operation.
    lConfiguration.testNewInstances();
  }
}
