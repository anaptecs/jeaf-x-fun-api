/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.anaptecs.jeaf.xfun.api.XFunRuntimeException;
import com.anaptecs.jeaf.xfun.api.config.ConfigurationReader;
import com.anaptecs.jeaf.xfun.test.config.annotation_reader_test.ClassA;
import com.anaptecs.jeaf.xfun.test.config.annotation_reader_test.InterfaceA;
import com.anaptecs.jeaf.xfun.test.config.annotation_reader_test.InterfaceB;
import com.anaptecs.jeaf.xfun.test.config.annotation_reader_test.MissLeadingAnnotation;
import com.anaptecs.jeaf.xfun.test.config.annotation_reader_test.TestAnnotation;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ConfigurationReaderTest {

  @Test
  @Order(10)
  public void testReadClass( ) {
    ConfigurationReader lReader = new ConfigurationReader();
    String lBasePackage = "META-INF/TEST";

    // Good case
    Class<?> lUntypedClass = lReader.readClassFromConfigFile("SINGLE_CLASS", lBasePackage);
    assertEquals(String.class, lUntypedClass, "Wrong class read.");

    lUntypedClass = lReader.readClassFromConfigFile(lBasePackage + "/" + "SINGLE_CLASS");
    assertEquals(String.class, lUntypedClass, "Wrong class read.");

    // No base package
    lUntypedClass = lReader.readClassFromConfigFile("SINGLE_CLASS_NO_BASE_PACKAGE", (String) null);
    assertEquals(String.class, lUntypedClass, "Wrong class read.");

    lUntypedClass = lReader.readClassFromConfigFile("SINGLE_CLASS_NO_BASE_PACKAGE");
    assertEquals(String.class, lUntypedClass, "Wrong class read.");

    // Good case with typed class
    Class<? extends TestInterface> lTypedClass =
        lReader.readClassFromConfigFile("SINGLE_CLASS_WITH_TYPE", lBasePackage, TestInterface.class);
    assertEquals(DefaultTestInterfaceImpl.class, lTypedClass, "Wrong class read.");

    lTypedClass = lReader.readClassFromConfigFile(lBasePackage + "/" + "SINGLE_CLASS_WITH_TYPE", TestInterface.class);
    assertEquals(DefaultTestInterfaceImpl.class, lTypedClass, "Wrong class read.");

    // No class defined
    lUntypedClass = lReader.readClassFromConfigFile("EMPTY", lBasePackage);
    assertNull(lUntypedClass, "null expected in case that the config file is empty.");

    // Multiple files defined
    try {
      lReader.readClassFromConfigFile("MULTIPLE_CLASSES", lBasePackage);
      fail("Invaliud configuration file should cause exception.");
    }
    catch (XFunRuntimeException e) {
      assertEquals(
          "Configuration file META-INF/TEST/MULTIPLE_CLASSES contains more than one class name. Please correct the file.",
          e.getMessage(), "Wrong exception message.");
    }
    // Wrong type implemented.
    lTypedClass = lReader.readClassFromConfigFile("WRONG_TYPE", lBasePackage, TestInterface.class);
    assertNull(lTypedClass, "Instead of invalid type null should be returned.");

    // File not available on classpath
    lUntypedClass = lReader.readClassFromConfigFile("NOT_EXISTING_CONFIG_FILE", lBasePackage);
    assertNull(lUntypedClass, "null expected in case that the config file does not exist.");

    // Error handling cases.
    try {
      lReader.readClassFromConfigFile(null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pConfigurationFilePath' must not be null.", e.getMessage());
    }

    try {
      lReader.readClassFromConfigFile("pathToFile", (Class<?>) null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pType' must not be null.", e.getMessage());
    }

    try {
      lReader.readClassFromConfigFile(null, null, null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pConfigurationFileName' must not be null.", e.getMessage());
    }

    // TODO TLS: Try to read file, that can not be opened.
  }

  @Test
  @Order(20)
  public void testReadClasses( ) throws IOException {
    ConfigurationReader lReader = new ConfigurationReader();
    String lBasePackage = "META-INF/TEST";

    // Good case, 1 class defined
    List<Class<?>> lUntypedClasses = lReader.readClassesFromConfigFile("SINGLE_CLASS", lBasePackage);
    assertEquals(1, lUntypedClasses.size(), "Wrong amount of classes found.");
    assertEquals(String.class, lUntypedClasses.get(0), "Wrong class read.");

    // Good case multiple classes defined
    lUntypedClasses = lReader.readClassesFromConfigFile("MULTIPLE_CLASSES", lBasePackage);
    assertEquals(2, lUntypedClasses.size(), "Wrong amount of classes found.");
    assertEquals(String.class, lUntypedClasses.get(0), "Wrong class read.");
    assertEquals(Integer.class, lUntypedClasses.get(1), "Wrong class read.");

    lUntypedClasses = lReader.readClassesFromConfigFile(lBasePackage + "/" + "MULTIPLE_CLASSES");
    assertEquals(2, lUntypedClasses.size(), "Wrong amount of classes found.");
    assertEquals(String.class, lUntypedClasses.get(0), "Wrong class read.");
    assertEquals(Integer.class, lUntypedClasses.get(1), "Wrong class read.");

    // Good case, multiple typed classes
    List<Class<? extends TestInterface>> lTypedClasses =
        lReader.readClassesFromConfigFile("MULTIPLE_TYPED_CLASSES", lBasePackage, TestInterface.class);
    assertEquals(3, lTypedClasses.size(), "Wrong amount of classes found.");
    assertEquals(DefaultTestInterfaceImpl.class, lTypedClasses.get(0), "Wrong class read.");
    assertEquals(AbstractTestInterfaceImpl.class, lTypedClasses.get(1), "Wrong class read.");
    assertEquals(CustomTestInterfaceImpl.class, lTypedClasses.get(2), "Wrong class read.");

    lTypedClasses =
        lReader.readClassesFromConfigFile(lBasePackage + "/" + "MULTIPLE_TYPED_CLASSES", TestInterface.class);
    assertEquals(3, lTypedClasses.size(), "Wrong amount of classes found.");
    assertEquals(DefaultTestInterfaceImpl.class, lTypedClasses.get(0), "Wrong class read.");
    assertEquals(AbstractTestInterfaceImpl.class, lTypedClasses.get(1), "Wrong class read.");
    assertEquals(CustomTestInterfaceImpl.class, lTypedClasses.get(2), "Wrong class read.");

    // No base package
    lTypedClasses =
        lReader.readClassesFromConfigFile("MULTIPLE_TYPED_CLASSES_NO_BASE_PACKAGE", null, TestInterface.class);
    assertEquals(3, lTypedClasses.size(), "Wrong amount of classes found.");
    assertEquals(DefaultTestInterfaceImpl.class, lTypedClasses.get(0), "Wrong class read.");
    assertEquals(AbstractTestInterfaceImpl.class, lTypedClasses.get(1), "Wrong class read.");
    assertEquals(CustomTestInterfaceImpl.class, lTypedClasses.get(2), "Wrong class read.");

    // No class defined
    lUntypedClasses = lReader.readClassesFromConfigFile("EMPTY", lBasePackage);
    assertEquals(0, lUntypedClasses.size(), "Expect list to be empty.");

    // Wrong type
    lTypedClasses =
        lReader.readClassesFromConfigFile("MULTIPLE_TYPED_CLASSES_WITH_WRONG_TYPE", lBasePackage, TestInterface.class);
    assertEquals(3, lTypedClasses.size(), "Wrong amount of classes found.");
    assertEquals(DefaultTestInterfaceImpl.class, lTypedClasses.get(0), "Wrong class read.");
    assertEquals(AbstractTestInterfaceImpl.class, lTypedClasses.get(1), "Wrong class read.");
    assertEquals(CustomTestInterfaceImpl.class, lTypedClasses.get(2), "Wrong class read.");

    // File not available on classpath
    lTypedClasses = lReader.readClassesFromConfigFile("NOT_EXISTING_CONFIG_FILE", lBasePackage, TestInterface.class);
    assertEquals(0, lTypedClasses.size(), "Expect list to be empty.");

    // Error handling cases.
    try {
      lReader.readClassesFromConfigFile(null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pConfigurationFilePath' must not be null.", e.getMessage());
    }

    try {
      lReader.readClassesFromConfigFile("pathToFile", (Class<?>) null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pType' must not be null.", e.getMessage());
    }

    try {
      lReader.readClassesFromConfigFile(null, null, null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pConfigurationFileName' must not be null.", e.getMessage());
    }

    // Try to read file with unloadable class.
    Class<?> lReadClass = lReader.readClassFromConfigFile("UnloadableClass");
    assertNull(lReadClass);
  }

  @Test
  @Order(30)
  public void testReadAnnotations( ) {
    // Test good cases.
    ConfigurationReader lReader = new ConfigurationReader();
    List<TestAnnotation> lAnnotations =
        lReader.readAnnotations("META-INF/TEST/CONFIG_READER/AllClassesWithAnnotation", TestAnnotation.class);

    assertNotNull(lAnnotations);
    assertEquals(3, lAnnotations.size());
    assertEquals(123, lAnnotations.get(0).key());
    assertEquals(4711, lAnnotations.get(1).key());
    assertEquals(666, lAnnotations.get(2).key());

    lAnnotations =
        lReader.readAnnotations("META-INF/TEST/CONFIG_READER/SomeClassesWithAnnotation", TestAnnotation.class);

    assertNotNull(lAnnotations);
    assertEquals(3, lAnnotations.size());
    assertEquals(123, lAnnotations.get(0).key());
    assertEquals(4711, lAnnotations.get(1).key());
    assertEquals(666, lAnnotations.get(2).key());

    List<MissLeadingAnnotation> lMissLeadingAnnotations =
        lReader.readAnnotations("META-INF/TEST/CONFIG_READER/SomeClassesWithAnnotation", MissLeadingAnnotation.class);
    assertNotNull(lMissLeadingAnnotations);
    assertEquals(2, lMissLeadingAnnotations.size());

    // Test error handling.
    // In valid parameters null
    try {
      lReader.readAnnotations("META-INF/TEST/CONFIG_READER/SomeClassesWithAnnotation", null);
      fail("Expected exception.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pAnnotation' must not be null.", e.getMessage());
    }
    try {
      lReader.readAnnotations(null, TestAnnotation.class);
      fail("Expected exception.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pConfigurationFilePath' must not be null.", e.getMessage());
    }

    // Missing config file.
    lAnnotations = lReader.readAnnotations("META-INF/TEST/CONFIG_READER/NotExistingFile", TestAnnotation.class);
    assertTrue(lAnnotations.isEmpty());

    // Test cases with split resource name and path.
    lAnnotations =
        lReader.readAnnotations("AllClassesWithAnnotation", "META-INF/TEST/CONFIG_READER", TestAnnotation.class);

    assertNotNull(lAnnotations);
    assertEquals(3, lAnnotations.size());
    assertEquals(123, lAnnotations.get(0).key());
    assertEquals(4711, lAnnotations.get(1).key());
    assertEquals(666, lAnnotations.get(2).key());

    lAnnotations =
        lReader.readAnnotations("SomeClassesWithAnnotation", "META-INF/TEST/CONFIG_READER", TestAnnotation.class);

    assertNotNull(lAnnotations);
    assertEquals(3, lAnnotations.size());
    assertEquals(123, lAnnotations.get(0).key());
    assertEquals(4711, lAnnotations.get(1).key());
    assertEquals(666, lAnnotations.get(2).key());

    lAnnotations = lReader.readAnnotations("OneClassWithAnnotation", null, TestAnnotation.class);

    assertNotNull(lAnnotations);
    assertEquals(1, lAnnotations.size());
    assertEquals(123, lAnnotations.get(0).key());

    // Test error handling
    try {
      lReader.readAnnotations("OneClassWithAnnotation", null, null);
      fail("Expected exception.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pAnnotation' must not be null.", e.getMessage());
    }
    try {
      lReader.readAnnotations(null, null, TestAnnotation.class);
      fail("Expected exception.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pConfigurationFileName' must not be null.", e.getMessage());
    }

    // Missing config file.
    lAnnotations = lReader.readAnnotations("NotExistingFile", "META-INF/TEST/CONFIG_READER/", TestAnnotation.class);
    assertTrue(lAnnotations.isEmpty());
  }

  @Test
  @Order(40)
  public void testReadAnnotationsMap( ) {
    // Test good cases.
    ConfigurationReader lReader = new ConfigurationReader();
    Map<Class<?>, TestAnnotation> lAnnotationsMap =
        lReader.readAnnotationsMap("META-INF/TEST/CONFIG_READER/AllClassesWithAnnotation", TestAnnotation.class);
    assertNotNull(lAnnotationsMap);
    assertEquals(3, lAnnotationsMap.size());
    assertEquals(123, lAnnotationsMap.get(ClassA.class).key());
    assertEquals(666, lAnnotationsMap.get(InterfaceB.class).key());
    assertEquals(4711, lAnnotationsMap.get(InterfaceA.class).key());

    lAnnotationsMap =
        lReader.readAnnotationsMap("META-INF/TEST/CONFIG_READER/SomeClassesWithAnnotation", TestAnnotation.class);
    assertNotNull(lAnnotationsMap);
    assertEquals(3, lAnnotationsMap.size());
    assertEquals(123, lAnnotationsMap.get(ClassA.class).key());
    assertEquals(666, lAnnotationsMap.get(InterfaceB.class).key());
    assertEquals(4711, lAnnotationsMap.get(InterfaceA.class).key());

    List<MissLeadingAnnotation> lMissLeadingAnnotationsMap =
        lReader.readAnnotations("META-INF/TEST/CONFIG_READER/SomeClassesWithAnnotation", MissLeadingAnnotation.class);
    assertNotNull(lMissLeadingAnnotationsMap);
    assertEquals(2, lMissLeadingAnnotationsMap.size());

    // Test error handling.
    // In valid parameters null
    try {
      lReader.readAnnotationsMap("META-INF/TEST/CONFIG_READER/SomeClassesWithAnnotation", null);
      fail("Expected exception.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pAnnotation' must not be null.", e.getMessage());
    }
    try {
      lReader.readAnnotationsMap(null, TestAnnotation.class);
      fail("Expected exception.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pConfigurationFilePath' must not be null.", e.getMessage());
    }

    // Missing config file.
    lAnnotationsMap = lReader.readAnnotationsMap("META-INF/TEST/CONFIG_READER/NotExistingFile", TestAnnotation.class);
    assertTrue(lAnnotationsMap.isEmpty());

    // Test cases with split resource name and path.
    lAnnotationsMap =
        lReader.readAnnotationsMap("AllClassesWithAnnotation", "META-INF/TEST/CONFIG_READER", TestAnnotation.class);

    assertNotNull(lAnnotationsMap);
    assertEquals(3, lAnnotationsMap.size());
    assertEquals(123, lAnnotationsMap.get(ClassA.class).key());
    assertEquals(666, lAnnotationsMap.get(InterfaceB.class).key());
    assertEquals(4711, lAnnotationsMap.get(InterfaceA.class).key());

    lAnnotationsMap =
        lReader.readAnnotationsMap("SomeClassesWithAnnotation", "META-INF/TEST/CONFIG_READER", TestAnnotation.class);

    assertNotNull(lAnnotationsMap);
    assertEquals(3, lAnnotationsMap.size());
    assertEquals(123, lAnnotationsMap.get(ClassA.class).key());
    assertEquals(666, lAnnotationsMap.get(InterfaceB.class).key());
    assertEquals(4711, lAnnotationsMap.get(InterfaceA.class).key());

    lAnnotationsMap = lReader.readAnnotationsMap("OneClassWithAnnotation", null, TestAnnotation.class);

    assertNotNull(lAnnotationsMap);
    assertEquals(1, lAnnotationsMap.size());
    assertEquals(123, lAnnotationsMap.get(ClassA.class).key());

    // Test error handling
    try {
      lReader.readAnnotationsMap("OneClassWithAnnotation", null, null);
      fail("Expected exception.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pAnnotation' must not be null.", e.getMessage());
    }
    try {
      lReader.readAnnotationsMap(null, null, TestAnnotation.class);
      fail("Expected exception.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pConfigurationFileName' must not be null.", e.getMessage());
    }

    // Missing config file.
    lAnnotationsMap =
        lReader.readAnnotationsMap("NotExistingFile", "META-INF/TEST/CONFIG_READER/", TestAnnotation.class);
    assertTrue(lAnnotationsMap.isEmpty());
  }
}
