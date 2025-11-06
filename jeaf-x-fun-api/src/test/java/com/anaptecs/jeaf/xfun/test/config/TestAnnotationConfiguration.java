/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import com.anaptecs.jeaf.xfun.api.XFunRuntimeException;
import com.anaptecs.jeaf.xfun.api.config.AnnotationBasedConfiguration;

public class TestAnnotationConfiguration extends AnnotationBasedConfiguration<TestAnnotationConfig> {
  public static String DEFAULT_CONFIGURATION_CLASS_NAME;

  public static String CUSTOM_CONFIG_RESOURCE_NAME;

  public static boolean ERROR_IN_CHECK = false;

  public TestAnnotationConfiguration( boolean pExceptionOnError ) {
    super(CUSTOM_CONFIG_RESOURCE_NAME, "META-INF/TEST", pExceptionOnError);
  }

  @Override
  protected Class<TestAnnotationConfig> getAnnotationClass( ) {
    return TestAnnotationConfig.class;
  }

  @Override
  protected String getDefaultConfigurationClass( ) {
    return DEFAULT_CONFIGURATION_CLASS_NAME;
  }

  @Override
  public TestAnnotationConfig getEmptyConfiguration( ) {
    return new TestAnnotationConfig() {

      @Override
      public Class<? extends Annotation> annotationType( ) {
        return TestAnnotationConfig.class;
      }

      @Override
      public boolean booleanValue( ) {
        return false;
      }

      @Override
      public boolean[] booleanValues( ) {
        return null;
      }

      @Override
      public byte byteValue( ) {
        return 0;
      }

      @Override
      public byte[] byteValues( ) {
        return null;
      }

      @Override
      public short shortValue( ) {
        return 0;
      }

      @Override
      public short[] shortValues( ) {
        return null;
      }

      @Override
      public int intValue( ) {
        return 0;
      }

      @Override
      public int[] intValues( ) {
        return null;
      }

      @Override
      public long longValue( ) {
        return 0;
      }

      @Override
      public long[] longValues( ) {
        return null;
      }

      @Override
      public float floatValue( ) {
        return 0;
      }

      @Override
      public float[] floatValues( ) {
        return null;
      }

      @Override
      public double doubleValue( ) {
        return 0;
      }

      @Override
      public double[] doubleValues( ) {
        return null;
      }

      @Override
      public String stringValue( ) {
        return null;
      }

      @Override
      public String[] stringValues( ) {
        return null;
      }

      @Override
      public TestEnum testEnum( ) {
        return null;
      }

      @Override
      public TestEnum[] testEnums( ) {
        return null;
      }

      @Override
      public Class<?> classValue( ) {
        return null;
      }

      @Override
      public Class<?>[] classValues( ) {
        return null;
      }

      @Override
      public Class<? extends TestInterface> classWithDefault( ) {
        return null;
      }
    };
  }

  @Override
  public List<String> checkCustomConfiguration(TestAnnotationConfig pCustomConfiguration) {
    List<String> lFailedChecks;
    if (ERROR_IN_CHECK == true) {
      lFailedChecks = new ArrayList<String>();
      lFailedChecks.add("Check failed.");
    }
    else {
      lFailedChecks = null;
    }
    return lFailedChecks;
  }

  public boolean booleanValue( ) {
    return theConfig.booleanValue();
  }

  public List<Boolean> booleanValues( ) {
    return this.asList(theConfig.booleanValues());
  }

  public byte byteValue( ) {
    return theConfig.byteValue();
  }

  public List<Byte> byteValues( ) {
    return this.asList(theConfig.byteValues());
  }

  public short shortValue( ) {
    return theConfig.shortValue();
  }

  public List<Short> shortValues( ) {
    return this.asList(theConfig.shortValues());
  }

  public int intValue( ) {
    return theConfig.intValue();
  }

  public List<Integer> intValues( ) {
    return this.asList(theConfig.intValues());
  }

  public long longValue( ) {
    return theConfig.longValue();
  }

  public List<Long> longValues( ) {
    return this.asList(theConfig.longValues());
  }

  public float floatValue( ) {
    return theConfig.floatValue();
  }

  public List<Float> floatValues( ) {
    return this.asList(theConfig.floatValues());
  }

  public double doubleValue( ) {
    return theConfig.doubleValue();
  }

  public List<Double> doubleValues( ) {
    return this.asList(theConfig.doubleValues());
  }

  public String stringValue( ) {
    return theConfig.stringValue();
  }

  public List<String> stringValues( ) {
    return this.asList(theConfig.stringValues());
  }

  public TestEnum testEnum( ) {
    return theConfig.testEnum();
  }

  public List<TestEnum> testEnums( ) {
    return this.asList(theConfig.testEnums());
  }

  public Class<?> classValue( ) {
    return theConfig.classValue();
  }

  public List<Class<?>> classValues( ) {
    return this.asList(theConfig.classValues());
  }

  public Class<? extends TestInterface> classWithDefault( ) {
    return theConfig.classWithDefault();
  }

  public TestInterface newInstance( ) {
    return this.newInstance(customConfig.classWithDefault(), defaultConfig.classWithDefault(), false);
  }

  /**
   * Method test {@link #tryNewInstance(Class, Class, List)}
   */
  public <X> void testTryNewInstance( ) {
    // Test nice weather cases.
    List<String> lConfigErrors = new ArrayList<>();
    this.tryNewInstance(DefaultTestInterfaceImpl.class, TestInterface.class, lConfigErrors);
    assertEquals(0, lConfigErrors.size(), "No config error expected.");

    // Test behavior when trying to create instance of 'null'
    this.tryNewInstance(null, TestInterface.class, lConfigErrors);
    assertEquals(1, lConfigErrors.size(), "Config error expected.");
    assertEquals("Unable to create new instance of configured class. Configured class is 'null'.", lConfigErrors.get(0),
        "Wrong error message.");
    lConfigErrors.clear();

    // Test behavior when trying to create instance of an interface
    this.tryNewInstance(TestInterface.class, TestInterface.class, lConfigErrors);
    assertEquals(1, lConfigErrors.size(), "Config error expected.");
    assertEquals(
        "Unable to create new instance of configured class com.anaptecs.jeaf.xfun.test.config.TestInterface. Configured class is an interface.",
        lConfigErrors.get(0), "Wrong error message.");
    lConfigErrors.clear();

    // Try to create new instance of abstract class.
    this.tryNewInstance(AbstractTestInterfaceImpl.class, TestInterface.class, lConfigErrors);
    assertEquals(1, lConfigErrors.size(), "Config error expected.");
    assertEquals(
        "Unable to create new instance of implementation for interface com.anaptecs.jeaf.xfun.test.config.TestInterface. Faulty implemetation class: com.anaptecs.jeaf.xfun.test.config.AbstractTestInterfaceImpl",
        lConfigErrors.get(0), "Wrong error message.");
    lConfigErrors.clear();

    // Try to create new instance of class without a default constructor
    this.tryNewInstance(NoDefaultConstructorTestInterfaceImpl.class, TestInterface.class, lConfigErrors);
    assertEquals(1, lConfigErrors.size(), "Config error expected.");
    assertEquals(
        "Unable to create new instance of implementation for interface com.anaptecs.jeaf.xfun.test.config.TestInterface. Faulty implemetation class: com.anaptecs.jeaf.xfun.test.config.NoDefaultConstructorTestInterfaceImpl",
        lConfigErrors.get(0), "Wrong error message.");
    lConfigErrors.clear();

    // Try to create new instance of class with private default constructor
    this.tryNewInstance(PrivateDefaultConstructorTestInterfaceImpl.class, TestInterface.class, lConfigErrors);
    assertEquals(1, lConfigErrors.size(), "Config error expected.");
    assertEquals(
        "Unable to create new instance of implementation for interface com.anaptecs.jeaf.xfun.test.config.TestInterface. Faulty implemetation class: com.anaptecs.jeaf.xfun.test.config.PrivateDefaultConstructorTestInterfaceImpl",
        lConfigErrors.get(0), "Wrong error message.");
    lConfigErrors.clear();
  }

  public void testNewInstanceOperations( ) {
    // Exception handling: no exception on error
    // Standard case
    boolean lExceptionOnError = false;
    TestInterface lNewInstance = this.newInstance(DefaultTestInterfaceImpl.class, lExceptionOnError);
    assertNotNull(lNewInstance, "New instance expected to be created.");

    // Try to create new instance of null
    lNewInstance = this.newInstance(null, lExceptionOnError);
    assertNull(lNewInstance, "No new instance expected to be created.");

    // Try to create new instance of an interface
    lNewInstance = this.newInstance(TestInterface.class, lExceptionOnError);
    assertNull(lNewInstance, "No new instance expected to be created.");

    // Try to create new instance of an abstract class
    lNewInstance = this.newInstance(AbstractTestInterfaceImpl.class, lExceptionOnError);
    assertNull(lNewInstance, "No new instance expected to be created.");

    // Try to create new instance of a class without default constructor
    lNewInstance = this.newInstance(NoDefaultConstructorTestInterfaceImpl.class, lExceptionOnError);
    assertNull(lNewInstance, "No new instance expected to be created.");

    // Try to create new instance of a class with private default constructor
    lNewInstance = this.newInstance(PrivateDefaultConstructorTestInterfaceImpl.class, lExceptionOnError);
    assertNull(lNewInstance, "No new instance expected to be created.");

    // Exception handling: no exception on error
    // Standard case
    lExceptionOnError = true;

    lNewInstance = this.newInstance(DefaultTestInterfaceImpl.class, lExceptionOnError);
    assertNotNull(lNewInstance, "New instance expected to be created.");

    // Try to create new instance of null
    try {
      this.newInstance(null, lExceptionOnError);
      fail("Expected exception to be thrown");
    }
    catch (XFunRuntimeException e) {
      assertEquals("Unable to create new instance of class 'null'.", e.getMessage(), "Unexpected error message.");
    }

    // Try to create new instance of an interface
    try {
      this.newInstance(TestInterface.class, lExceptionOnError);
      fail("Expected exception to be thrown");
    }
    catch (XFunRuntimeException e) {
      assertEquals("Unable to create new instance of class com.anaptecs.jeaf.xfun.test.config.TestInterface.",
          e.getMessage(), "Unexpected error message.");
    }

    // Try to create new instance of an abstract class
    try {
      this.newInstance(AbstractTestInterfaceImpl.class, lExceptionOnError);
      fail("Expected exception to be thrown");
    }
    catch (XFunRuntimeException e) {
      assertEquals(
          "Unable to create new instance of class com.anaptecs.jeaf.xfun.test.config.AbstractTestInterfaceImpl.",
          e.getMessage(), "Unexpected error message.");
    }

    // Try to create new instance of a class without default constructor
    try {
      this.newInstance(NoDefaultConstructorTestInterfaceImpl.class, lExceptionOnError);
      fail("Expected exception to be thrown");
    }
    catch (XFunRuntimeException e) {
      assertEquals(
          "Unable to create new instance of class com.anaptecs.jeaf.xfun.test.config.NoDefaultConstructorTestInterfaceImpl.",
          e.getMessage(), "Unexpected error message.");
    }

    // Try to create new instance of a class with private default constructor
    try {
      this.newInstance(PrivateDefaultConstructorTestInterfaceImpl.class, lExceptionOnError);
      fail("Expected exception to be thrown");
    }
    catch (XFunRuntimeException e) {
      assertEquals(
          "Unable to create new instance of class com.anaptecs.jeaf.xfun.test.config.PrivateDefaultConstructorTestInterfaceImpl.",
          e.getMessage(), "Unexpected error message.");
    }

    // Try to create new instance of class that throws an error in constructor
    try {
      this.newInstance(ExceptionInConstructorClass.class, lExceptionOnError);
      fail("Expected error to be thrown");
    }
    catch (Error e) {
      assertEquals("I'm expected ;-)", e.getMessage(), "Unexpected error message.");
    }

    ExceptionInConstructorClass lNewObject = this.newInstance(ExceptionInConstructorClass.class, false);
    assertNull(lNewObject);
  }

  public void testNewInstances( ) {
    // Exception handling: no exception on error
    // Standard case
    boolean lExceptionOnError = false;
    List<Class<?>> lClasses = this.asList(new Class<?>[] { String.class, DefaultTestInterfaceImpl.class });
    List<Object> lNewInstances = this.newInstances(lClasses, lExceptionOnError);
    assertNotNull(lNewInstances, "List with new instances expected to be created.");
    assertEquals(String.class, lNewInstances.get(0).getClass(), "Instance with unexpected type.");
    assertEquals(DefaultTestInterfaceImpl.class, lNewInstances.get(1).getClass(), "Instance with unexpected type.");

    // Test handling of null and empty list
    lNewInstances = this.newInstances(null, lExceptionOnError);
    assertEquals(0, lNewInstances.size(), "New instances where created.");
    lNewInstances = this.newInstances(new ArrayList<>(), lExceptionOnError);
    assertEquals(0, lNewInstances.size(), "New instances where created.");
  }

  public void testAsListImplementationWithNullValues( ) {
    assertNull(this.asList((boolean[]) null), "Wrong handling of null values.");
    assertNull(this.asList((byte[]) null), "Wrong handling of null values.");
    assertNull(this.asList((short[]) null), "Wrong handling of null values.");
    assertNull(this.asList((int[]) null), "Wrong handling of null values.");
    assertNull(this.asList((long[]) null), "Wrong handling of null values.");
    assertNull(this.asList((float[]) null), "Wrong handling of null values.");
    assertNull(this.asList((double[]) null), "Wrong handling of null values.");
    assertNull(this.asList((String[]) null), "Wrong handling of null values.");
    assertNull(this.asList((TestEnum[]) null), "Wrong handling of null values.");
    assertNull(this.asList((Class<?>[]) null), "Wrong handling of null values.");
  }
}
