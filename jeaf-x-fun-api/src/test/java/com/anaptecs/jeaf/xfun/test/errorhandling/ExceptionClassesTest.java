/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.errorhandling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.XFunMessages;
import com.anaptecs.jeaf.xfun.api.errorhandling.ApplicationException;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFApplicationException;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFBootstrapException;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFSystemException;
import com.anaptecs.jeaf.xfun.api.errorhandling.SystemException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ExceptionClassesTest {
  @SuppressWarnings("deprecation")
  @Test
  @Order(10)
  public void testApplicationException( ) {
    // Good cases
    long lBefore = System.currentTimeMillis();
    ApplicationException lException = new TestApplicationException(XFunMessages.FILE_NOT_FOUND);
    long lAfter = System.currentTimeMillis();
    assertEquals(XFunMessages.FILE_NOT_FOUND, lException.getErrorCode());
    assertNull(lException.getMessageParameters());
    assertTrue(lBefore <= lException.getTimestamp().getTime());
    assertTrue(lAfter >= lException.getTimestamp().getTime());
    assertEquals(XFun.getVersionInfo(), lException.getVersionInfo());
    assertEquals("[0 59] 59", lException.getMessage());
    assertNull(lException.getCause());

    lException = new TestApplicationException(XFunMessages.FILE_NOT_FOUND, new String[] { "Hello", "World", "!" });
    assertEquals(XFunMessages.FILE_NOT_FOUND, lException.getErrorCode());
    assertNotNull(lException.getMessageParameters());
    assertEquals(3, lException.getMessageParameters().length);
    assertEquals("Hello", lException.getMessageParameters()[0]);
    assertEquals("World", lException.getMessageParameters()[1]);
    assertEquals("!", lException.getMessageParameters()[2]);
    assertEquals(XFun.getVersionInfo(), lException.getVersionInfo());
    assertEquals("[0 59] 59: Hello World !", lException.getMessage());
    assertNull(lException.getTechnicalDetails());
    assertNull(lException.getCause());

    Throwable lCause = new IllegalArgumentException();
    lException =
        new TestApplicationException(XFunMessages.FILE_NOT_FOUND, new String[] { "Hello", "World", "!" }, lCause);
    assertEquals(XFunMessages.FILE_NOT_FOUND, lException.getErrorCode());
    assertNotNull(lException.getMessageParameters());
    assertEquals(3, lException.getMessageParameters().length);
    assertEquals("Hello", lException.getMessageParameters()[0]);
    assertEquals("World", lException.getMessageParameters()[1]);
    assertEquals("!", lException.getMessageParameters()[2]);
    assertEquals(XFun.getVersionInfo(), lException.getVersionInfo());
    assertEquals("[0 59] 59: Hello World !", lException.getMessage());
    assertNull(lException.getTechnicalDetails());
    assertEquals(lCause, lException.getCause());

    lException = new TestApplicationException(XFunMessages.FILE_NOT_FOUND, lCause);
    assertEquals(XFunMessages.FILE_NOT_FOUND, lException.getErrorCode());
    assertEquals(0, lException.getMessageParameters().length);
    assertEquals(XFun.getVersionInfo(), lException.getVersionInfo());
    assertEquals("[0 59] 59", lException.getMessage());
    assertNull(lException.getTechnicalDetails());
    assertEquals(lCause, lException.getCause());

    lCause = new IllegalArgumentException();
    lException = new TestApplicationException(XFunMessages.FILE_NOT_FOUND, lCause, "Hello", "World", "!");
    assertEquals(XFunMessages.FILE_NOT_FOUND, lException.getErrorCode());
    assertNotNull(lException.getMessageParameters());
    assertEquals(3, lException.getMessageParameters().length);
    assertEquals("Hello", lException.getMessageParameters()[0]);
    assertEquals("World", lException.getMessageParameters()[1]);
    assertEquals("!", lException.getMessageParameters()[2]);
    assertEquals(XFun.getVersionInfo(), lException.getVersionInfo());
    assertEquals("[0 59] 59: Hello World !", lException.getMessage());
    assertNull(lException.getTechnicalDetails());
    assertEquals(lCause, lException.getCause());

    lException = new TestApplicationException(XFunMessages.FILE_NOT_FOUND, null, "Hello", "World", "!");
    assertEquals(XFunMessages.FILE_NOT_FOUND, lException.getErrorCode());
    assertNotNull(lException.getMessageParameters());
    assertEquals(3, lException.getMessageParameters().length);
    assertEquals("Hello", lException.getMessageParameters()[0]);
    assertEquals("World", lException.getMessageParameters()[1]);
    assertEquals("!", lException.getMessageParameters()[2]);
    assertEquals(XFun.getVersionInfo(), lException.getVersionInfo());
    assertEquals("[0 59] 59: Hello World !", lException.getMessage());
    assertNull(lException.getTechnicalDetails());
    assertEquals(null, lException.getCause());

    // Test exception with technical details.
    lException = new TestApplicationException(XFunMessages.FILE_NOT_FOUND, "Some more ugly but helpful details.", null,
        "Hello", "World", "!");
    assertEquals(XFunMessages.FILE_NOT_FOUND, lException.getErrorCode());
    assertNotNull(lException.getMessageParameters());
    assertEquals(3, lException.getMessageParameters().length);
    assertEquals("Hello", lException.getMessageParameters()[0]);
    assertEquals("World", lException.getMessageParameters()[1]);
    assertEquals("!", lException.getMessageParameters()[2]);
    assertEquals(XFun.getVersionInfo(), lException.getVersionInfo());
    assertEquals("[0 59] 59: Hello World !", lException.getMessage());
    assertEquals("Some more ugly but helpful details.", lException.getTechnicalDetails());
    assertEquals(null, lException.getCause());

    // Null handling
    try {
      new TestApplicationException(null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pErrorCode' must not be null.", e.getMessage());
    }

    lException =
        new TestApplicationException(XFunMessages.FILE_NOT_FOUND, new String[] { "Hello", "World", "!" }, null);
    assertEquals(XFunMessages.FILE_NOT_FOUND, lException.getErrorCode());
    assertNotNull(lException.getMessageParameters());
    assertEquals(3, lException.getMessageParameters().length);
    assertEquals("Hello", lException.getMessageParameters()[0]);
    assertEquals("World", lException.getMessageParameters()[1]);
    assertEquals("!", lException.getMessageParameters()[2]);
    assertEquals(XFun.getVersionInfo(), lException.getVersionInfo());
    assertEquals("[0 59] 59: Hello World !", lException.getMessage());
    assertNull(lException.getTechnicalDetails());
    assertNull(lException.getCause());

    lException = new TestApplicationException(XFunMessages.FILE_NOT_FOUND, null, lCause);
    assertEquals(XFunMessages.FILE_NOT_FOUND, lException.getErrorCode());
    assertNull(lException.getMessageParameters());
    assertEquals(XFun.getVersionInfo(), lException.getVersionInfo());
    assertEquals("[0 59] 59", lException.getMessage());
    assertNull(lException.getTechnicalDetails());
    assertEquals(lCause, lException.getCause());
  }

  @SuppressWarnings("deprecation")
  @Test
  @Order(20)
  public void testJEAFApplicationException( ) {
    // Good cases
    long lBefore = System.currentTimeMillis();
    JEAFApplicationException lException = new JEAFApplicationException(XFunMessages.FILE_NOT_FOUND);
    long lAfter = System.currentTimeMillis();
    assertEquals(XFunMessages.FILE_NOT_FOUND, lException.getErrorCode());
    assertNull(lException.getMessageParameters());
    assertTrue(lBefore <= lException.getTimestamp().getTime());
    assertTrue(lAfter >= lException.getTimestamp().getTime());
    assertEquals(XFun.getVersionInfo(), lException.getVersionInfo());
    assertEquals("[0 59] 59", lException.getMessage());
    assertNull(lException.getCause());
    assertNull(lException.getTechnicalDetails());

    lException = new JEAFApplicationException(XFunMessages.FILE_NOT_FOUND, new String[] { "Hello", "World", "!" });
    assertEquals(XFunMessages.FILE_NOT_FOUND, lException.getErrorCode());
    assertNotNull(lException.getMessageParameters());
    assertEquals(3, lException.getMessageParameters().length);
    assertEquals("Hello", lException.getMessageParameters()[0]);
    assertEquals("World", lException.getMessageParameters()[1]);
    assertEquals("!", lException.getMessageParameters()[2]);
    assertEquals(XFun.getVersionInfo(), lException.getVersionInfo());
    assertEquals("[0 59] 59: Hello World !", lException.getMessage());
    assertNull(lException.getCause());
    assertNull(lException.getTechnicalDetails());

    lException = new JEAFApplicationException(XFunMessages.FILE_NOT_FOUND, "Hello", "World", "!");
    assertEquals(XFunMessages.FILE_NOT_FOUND, lException.getErrorCode());
    assertNotNull(lException.getMessageParameters());
    assertEquals(3, lException.getMessageParameters().length);
    assertEquals("Hello", lException.getMessageParameters()[0]);
    assertEquals("World", lException.getMessageParameters()[1]);
    assertEquals("!", lException.getMessageParameters()[2]);
    assertEquals(XFun.getVersionInfo(), lException.getVersionInfo());
    assertEquals("[0 59] 59: Hello World !", lException.getMessage());
    assertNull(lException.getCause());
    assertNull(lException.getTechnicalDetails());

    Throwable lCause = new IllegalArgumentException();
    lException =
        new JEAFApplicationException(XFunMessages.FILE_NOT_FOUND, new String[] { "Hello", "World", "!" }, lCause);
    assertEquals(XFunMessages.FILE_NOT_FOUND, lException.getErrorCode());
    assertNotNull(lException.getMessageParameters());
    assertEquals(3, lException.getMessageParameters().length);
    assertEquals("Hello", lException.getMessageParameters()[0]);
    assertEquals("World", lException.getMessageParameters()[1]);
    assertEquals("!", lException.getMessageParameters()[2]);
    assertEquals(XFun.getVersionInfo(), lException.getVersionInfo());
    assertEquals("[0 59] 59: Hello World !", lException.getMessage());
    assertEquals(lCause, lException.getCause());
    assertNull(lException.getTechnicalDetails());

    lException = new JEAFApplicationException(XFunMessages.FILE_NOT_FOUND, lCause);
    assertEquals(XFunMessages.FILE_NOT_FOUND, lException.getErrorCode());
    assertEquals(0, lException.getMessageParameters().length);
    assertEquals(XFun.getVersionInfo(), lException.getVersionInfo());
    assertEquals("[0 59] 59", lException.getMessage());
    assertEquals(lCause, lException.getCause());
    assertNull(lException.getTechnicalDetails());

    lException =
        new JEAFApplicationException(XFunMessages.FILE_NOT_FOUND, "Some more ugly but helpful details.", lCause);
    assertEquals(XFunMessages.FILE_NOT_FOUND, lException.getErrorCode());
    assertEquals(0, lException.getMessageParameters().length);
    assertEquals(XFun.getVersionInfo(), lException.getVersionInfo());
    assertEquals("[0 59] 59", lException.getMessage());
    assertEquals(lCause, lException.getCause());
    assertEquals("Some more ugly but helpful details.", lException.getTechnicalDetails());
    // Null handling
    try {
      new JEAFApplicationException(null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pErrorCode' must not be null.", e.getMessage());
    }

    lException =
        new JEAFApplicationException(XFunMessages.FILE_NOT_FOUND, new String[] { "Hello", "World", "!" }, null);
    assertEquals(XFunMessages.FILE_NOT_FOUND, lException.getErrorCode());
    assertNotNull(lException.getMessageParameters());
    assertEquals(3, lException.getMessageParameters().length);
    assertEquals("Hello", lException.getMessageParameters()[0]);
    assertEquals("World", lException.getMessageParameters()[1]);
    assertEquals("!", lException.getMessageParameters()[2]);
    assertEquals(XFun.getVersionInfo(), lException.getVersionInfo());
    assertEquals("[0 59] 59: Hello World !", lException.getMessage());
    assertNull(lException.getCause());

    lException = new JEAFApplicationException(XFunMessages.FILE_NOT_FOUND, null, lCause);
    assertEquals(XFunMessages.FILE_NOT_FOUND, lException.getErrorCode());
    assertNull(lException.getMessageParameters());
    assertEquals(XFun.getVersionInfo(), lException.getVersionInfo());
    assertEquals("[0 59] 59", lException.getMessage());
    assertEquals(lCause, lException.getCause());
  }

  @SuppressWarnings("deprecation")
  @Test
  @Order(30)
  public void testSystemException( ) {
    // Good cases
    long lBefore = System.currentTimeMillis();
    SystemException lException = new TestSystemException(XFunMessages.FILE_NOT_FOUND);
    long lAfter = System.currentTimeMillis();
    assertEquals(XFunMessages.FILE_NOT_FOUND, lException.getErrorCode());
    assertNull(lException.getMessageParameters());
    assertTrue(lBefore <= lException.getTimestamp().getTime());
    assertTrue(lAfter >= lException.getTimestamp().getTime());
    assertEquals(XFun.getVersionInfo(), lException.getVersionInfo());
    assertEquals("[0 59] 59", lException.getMessage());
    assertNull(lException.getCause());

    lException = new TestSystemException(XFunMessages.FILE_NOT_FOUND, new String[] { "Hello", "World", "!" });
    assertEquals(XFunMessages.FILE_NOT_FOUND, lException.getErrorCode());
    assertNotNull(lException.getMessageParameters());
    assertEquals(3, lException.getMessageParameters().length);
    assertEquals("Hello", lException.getMessageParameters()[0]);
    assertEquals("World", lException.getMessageParameters()[1]);
    assertEquals("!", lException.getMessageParameters()[2]);
    assertEquals(XFun.getVersionInfo(), lException.getVersionInfo());
    assertEquals("[0 59] 59: Hello World !", lException.getMessage());
    assertNull(lException.getCause());

    lException = new TestSystemException(XFunMessages.FILE_NOT_FOUND, "Hello", "World", "!");
    assertEquals(XFunMessages.FILE_NOT_FOUND, lException.getErrorCode());
    assertNotNull(lException.getMessageParameters());
    assertEquals(3, lException.getMessageParameters().length);
    assertEquals("Hello", lException.getMessageParameters()[0]);
    assertEquals("World", lException.getMessageParameters()[1]);
    assertEquals("!", lException.getMessageParameters()[2]);
    assertEquals(XFun.getVersionInfo(), lException.getVersionInfo());
    assertEquals("[0 59] 59: Hello World !", lException.getMessage());
    assertNull(lException.getCause());

    Throwable lCause = new IllegalArgumentException();
    lException = new TestSystemException(XFunMessages.FILE_NOT_FOUND, new String[] { "Hello", "World", "!" }, lCause);
    assertEquals(XFunMessages.FILE_NOT_FOUND, lException.getErrorCode());
    assertNotNull(lException.getMessageParameters());
    assertEquals(3, lException.getMessageParameters().length);
    assertEquals("Hello", lException.getMessageParameters()[0]);
    assertEquals("World", lException.getMessageParameters()[1]);
    assertEquals("!", lException.getMessageParameters()[2]);
    assertEquals(XFun.getVersionInfo(), lException.getVersionInfo());
    assertEquals("[0 59] 59: Hello World !", lException.getMessage());
    assertEquals(lCause, lException.getCause());

    lException = new TestSystemException(XFunMessages.FILE_NOT_FOUND, lCause);
    assertEquals(XFunMessages.FILE_NOT_FOUND, lException.getErrorCode());
    assertEquals(0, lException.getMessageParameters().length);
    assertEquals(XFun.getVersionInfo(), lException.getVersionInfo());
    assertEquals("[0 59] 59", lException.getMessage());
    assertEquals(lCause, lException.getCause());

    lException = new TestSystemException(XFunMessages.FILE_NOT_FOUND, lCause, "Hello", "World", "!");
    assertEquals(XFunMessages.FILE_NOT_FOUND, lException.getErrorCode());
    assertNotNull(lException.getMessageParameters());
    assertEquals(3, lException.getMessageParameters().length);
    assertEquals("Hello", lException.getMessageParameters()[0]);
    assertEquals("World", lException.getMessageParameters()[1]);
    assertEquals("!", lException.getMessageParameters()[2]);
    assertEquals(XFun.getVersionInfo(), lException.getVersionInfo());
    assertEquals("[0 59] 59: Hello World !", lException.getMessage());
    assertEquals(lCause, lException.getCause());

    lException = new TestSystemException(XFunMessages.FILE_NOT_FOUND, (Exception) null, "Hello", "World", "!");
    assertEquals(XFunMessages.FILE_NOT_FOUND, lException.getErrorCode());
    assertNotNull(lException.getMessageParameters());
    assertEquals(3, lException.getMessageParameters().length);
    assertEquals("Hello", lException.getMessageParameters()[0]);
    assertEquals("World", lException.getMessageParameters()[1]);
    assertEquals("!", lException.getMessageParameters()[2]);
    assertEquals(XFun.getVersionInfo(), lException.getVersionInfo());
    assertEquals("[0 59] 59: Hello World !", lException.getMessage());
    assertEquals(null, lException.getCause());

    // Test exception with technical details.
    lException = new TestSystemException(XFunMessages.FILE_NOT_FOUND, "Some more ugly but helpful details.",
        (Exception) null, "Hello", "World", "!");
    assertEquals(XFunMessages.FILE_NOT_FOUND, lException.getErrorCode());
    assertNotNull(lException.getMessageParameters());
    assertEquals(3, lException.getMessageParameters().length);
    assertEquals("Hello", lException.getMessageParameters()[0]);
    assertEquals("World", lException.getMessageParameters()[1]);
    assertEquals("!", lException.getMessageParameters()[2]);
    assertEquals(XFun.getVersionInfo(), lException.getVersionInfo());
    assertEquals("[0 59] 59: Hello World !", lException.getMessage());
    assertEquals("Some more ugly but helpful details.", lException.getTechnicalDetails());
    assertEquals(null, lException.getCause());

    // Null handling
    try {
      new TestSystemException(null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pErrorCode' must not be null.", e.getMessage());
    }

    lException = new TestSystemException(XFunMessages.FILE_NOT_FOUND, new String[] { "Hello", "World", "!" }, null);
    assertEquals(XFunMessages.FILE_NOT_FOUND, lException.getErrorCode());
    assertNotNull(lException.getMessageParameters());
    assertEquals(3, lException.getMessageParameters().length);
    assertEquals("Hello", lException.getMessageParameters()[0]);
    assertEquals("World", lException.getMessageParameters()[1]);
    assertEquals("!", lException.getMessageParameters()[2]);
    assertEquals(XFun.getVersionInfo(), lException.getVersionInfo());
    assertEquals("[0 59] 59: Hello World !", lException.getMessage());
    assertNull(lException.getCause());

    lException = new TestSystemException(XFunMessages.FILE_NOT_FOUND, null, lCause);
    assertEquals(XFunMessages.FILE_NOT_FOUND, lException.getErrorCode());
    assertNull(lException.getMessageParameters());
    assertEquals(XFun.getVersionInfo(), lException.getVersionInfo());
    assertEquals("[0 59] 59", lException.getMessage());
    assertEquals(lCause, lException.getCause());
  }

  @SuppressWarnings("deprecation")
  @Test
  @Order(40)
  public void testJEAFSystemException( ) {
    // Good cases
    long lBefore = System.currentTimeMillis();
    JEAFSystemException lException = new JEAFSystemException(XFunMessages.FILE_NOT_FOUND);
    long lAfter = System.currentTimeMillis();
    assertEquals(XFunMessages.FILE_NOT_FOUND, lException.getErrorCode());
    assertNull(lException.getMessageParameters());
    assertTrue(lBefore <= lException.getTimestamp().getTime());
    assertTrue(lAfter >= lException.getTimestamp().getTime());
    assertEquals(XFun.getVersionInfo(), lException.getVersionInfo());
    assertEquals("[0 59] 59", lException.getMessage());
    assertNull(lException.getCause());
    assertNull(lException.getTechnicalDetails());

    lException = new JEAFSystemException(XFunMessages.FILE_NOT_FOUND, new String[] { "Hello", "World", "!" });
    assertEquals(XFunMessages.FILE_NOT_FOUND, lException.getErrorCode());
    assertNotNull(lException.getMessageParameters());
    assertEquals(3, lException.getMessageParameters().length);
    assertEquals("Hello", lException.getMessageParameters()[0]);
    assertEquals("World", lException.getMessageParameters()[1]);
    assertEquals("!", lException.getMessageParameters()[2]);
    assertEquals(XFun.getVersionInfo(), lException.getVersionInfo());
    assertEquals("[0 59] 59: Hello World !", lException.getMessage());
    assertNull(lException.getCause());
    assertNull(lException.getTechnicalDetails());

    Throwable lCause = new IllegalArgumentException();
    lException = new JEAFSystemException(XFunMessages.FILE_NOT_FOUND, new String[] { "Hello", "World", "!" }, lCause);
    assertEquals(XFunMessages.FILE_NOT_FOUND, lException.getErrorCode());
    assertNotNull(lException.getMessageParameters());
    assertEquals(3, lException.getMessageParameters().length);
    assertEquals("Hello", lException.getMessageParameters()[0]);
    assertEquals("World", lException.getMessageParameters()[1]);
    assertEquals("!", lException.getMessageParameters()[2]);
    assertEquals(XFun.getVersionInfo(), lException.getVersionInfo());
    assertEquals("[0 59] 59: Hello World !", lException.getMessage());
    assertEquals(lCause, lException.getCause());
    assertNull(lException.getTechnicalDetails());

    lException = new JEAFSystemException(XFunMessages.FILE_NOT_FOUND, lCause);
    assertEquals(XFunMessages.FILE_NOT_FOUND, lException.getErrorCode());
    assertEquals(0, lException.getMessageParameters().length);
    assertEquals(XFun.getVersionInfo(), lException.getVersionInfo());
    assertEquals("[0 59] 59", lException.getMessage());
    assertEquals(lCause, lException.getCause());
    assertNull(lException.getTechnicalDetails());

    lCause = new IllegalArgumentException();
    lException = new JEAFSystemException(XFunMessages.FILE_NOT_FOUND, lCause, "Hello", "World", "!");
    assertEquals(XFunMessages.FILE_NOT_FOUND, lException.getErrorCode());
    assertNotNull(lException.getMessageParameters());
    assertEquals(3, lException.getMessageParameters().length);
    assertEquals("Hello", lException.getMessageParameters()[0]);
    assertEquals("World", lException.getMessageParameters()[1]);
    assertEquals("!", lException.getMessageParameters()[2]);
    assertEquals(XFun.getVersionInfo(), lException.getVersionInfo());
    assertEquals("[0 59] 59: Hello World !", lException.getMessage());
    assertEquals(lCause, lException.getCause());
    assertNull(lException.getTechnicalDetails());

    lException = new JEAFSystemException(XFunMessages.FILE_NOT_FOUND, (Throwable) null, "Hello", "World", "!");
    assertEquals(XFunMessages.FILE_NOT_FOUND, lException.getErrorCode());
    assertNotNull(lException.getMessageParameters());
    assertEquals(3, lException.getMessageParameters().length);
    assertEquals("Hello", lException.getMessageParameters()[0]);
    assertEquals("World", lException.getMessageParameters()[1]);
    assertEquals("!", lException.getMessageParameters()[2]);
    assertEquals(XFun.getVersionInfo(), lException.getVersionInfo());
    assertEquals("[0 59] 59: Hello World !", lException.getMessage());
    assertEquals(null, lException.getCause());
    assertNull(lException.getTechnicalDetails());

    lException = new JEAFSystemException(XFunMessages.FILE_NOT_FOUND, "Some more ugly but helpful details.",
        (Throwable) null, "Hello", "World", "!");
    assertEquals(XFunMessages.FILE_NOT_FOUND, lException.getErrorCode());
    assertNotNull(lException.getMessageParameters());
    assertEquals(3, lException.getMessageParameters().length);
    assertEquals("Hello", lException.getMessageParameters()[0]);
    assertEquals("World", lException.getMessageParameters()[1]);
    assertEquals("!", lException.getMessageParameters()[2]);
    assertEquals(XFun.getVersionInfo(), lException.getVersionInfo());
    assertEquals("[0 59] 59: Hello World !", lException.getMessage());
    assertEquals(null, lException.getCause());
    assertEquals("Some more ugly but helpful details.", lException.getTechnicalDetails());

    // Null handling
    try {
      new JEAFSystemException(null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pErrorCode' must not be null.", e.getMessage());
    }

    lException = new JEAFSystemException(XFunMessages.FILE_NOT_FOUND, new String[] { "Hello", "World", "!" }, null);
    assertEquals(XFunMessages.FILE_NOT_FOUND, lException.getErrorCode());
    assertNotNull(lException.getMessageParameters());
    assertEquals(3, lException.getMessageParameters().length);
    assertEquals("Hello", lException.getMessageParameters()[0]);
    assertEquals("World", lException.getMessageParameters()[1]);
    assertEquals("!", lException.getMessageParameters()[2]);
    assertEquals(XFun.getVersionInfo(), lException.getVersionInfo());
    assertEquals("[0 59] 59: Hello World !", lException.getMessage());
    assertNull(lException.getCause());

    lException = new JEAFSystemException(XFunMessages.FILE_NOT_FOUND, null, lCause);
    assertEquals(XFunMessages.FILE_NOT_FOUND, lException.getErrorCode());
    assertNull(lException.getMessageParameters());
    assertEquals(XFun.getVersionInfo(), lException.getVersionInfo());
    assertEquals("[0 59] 59", lException.getMessage());
    assertEquals(lCause, lException.getCause());
  }

  @Test
  @Order(50)
  public void testJEAFBootstrapException( ) {
    JEAFBootstrapException lBootstrapException = new JEAFBootstrapException("Hello");
    assertEquals("Hello", lBootstrapException.getMessage());
    assertEquals(null, lBootstrapException.getCause());

    IllegalArgumentException lCause = new IllegalArgumentException();
    lBootstrapException = new JEAFBootstrapException("Hello2", lCause);
    assertEquals("Hello2", lBootstrapException.getMessage());
    assertEquals(lCause, lBootstrapException.getCause());

    lBootstrapException = new JEAFBootstrapException(null, null);
    assertEquals(null, lBootstrapException.getMessage());
    assertEquals(null, lBootstrapException.getCause());
  }
}
