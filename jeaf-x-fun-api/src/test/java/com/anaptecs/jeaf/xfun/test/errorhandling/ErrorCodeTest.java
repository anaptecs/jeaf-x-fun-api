/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.errorhandling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.anaptecs.jeaf.xfun.api.checks.AssertionFailedError;
import com.anaptecs.jeaf.xfun.api.errorhandling.ErrorCode;
import com.anaptecs.jeaf.xfun.api.messages.LocalizedObject;
import com.anaptecs.jeaf.xfun.api.messages.LocalizedString;
import com.anaptecs.jeaf.xfun.api.messages.MessageID;
import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ErrorCodeTest {
  @Test
  @Order(10)
  public void testErrorCodeCreation( ) {
    // Test default behavior
    ErrorCode lErrorCodeID = new ErrorCode(123456, TraceLevel.TRACE);
    assertEquals(123456, lErrorCodeID.getLocalizationID());
    assertEquals(123456, lErrorCodeID.getErrorCodeValue());
    assertEquals(TraceLevel.TRACE, lErrorCodeID.getTraceLevel());

    // Error cases
    // Negative message id
    try {
      new ErrorCode(-20, TraceLevel.ERROR);
    }
    catch (AssertionFailedError e) {
      assertEquals("Assertion failed. pLocalizationID must be zero or greater. Current value: -20", e.getMessage());
    }

    // No trace level
    try {
      new ErrorCode(20, null);
    }
    catch (AssertionFailedError e) {
      assertEquals("Assertion failed. 'pTraceLevel' must not be null.", e.getMessage());
    }
  }

  @Test
  @Order(20)
  public void testToString( ) {
    // Good cases
    ErrorCode lMessageID = new ErrorCode(123456, TraceLevel.TRACE);
    assertEquals("123456", lMessageID.toString());
    assertEquals("123456", lMessageID.toString(Locale.FRENCH));
    assertEquals("123456: 123 456", lMessageID.toString(Locale.ITALIAN, "123", "456"));
    assertEquals("123456: 123 456", lMessageID.toString((Locale) null, "123", "456"));
    assertEquals("123456", lMessageID.toString(Locale.ITALIAN, (String[]) null));
    assertEquals("123456", lMessageID.toTraceString());
    assertEquals("123456", lMessageID.toString((Locale) null));
  }

  @SuppressWarnings("unlikely-arg-type")
  @Test
  @Order(30)
  public void testEquals( ) {
    MessageID lMessageID = new MessageID(4711, TraceLevel.TRACE);
    ErrorCode lErrorCode = new ErrorCode(4711, TraceLevel.DEBUG);

    // Test hash code
    assertEquals(4711, lMessageID.hashCode());
    assertEquals(4711, lErrorCode.hashCode());

    // equals operation is only based on localizationID to ensure that the same id is not used among all subclasses.
    assertTrue(lMessageID.equals(lErrorCode));
    assertFalse(lMessageID.equals(null));
    assertFalse(lMessageID.equals(new LocalizedString(123)));
    assertTrue(lMessageID.equals(new LocalizedString(4711)));

    // Test correct behavior when used in collections
    List<LocalizedObject> lObjects = new ArrayList<>();
    lObjects.add(lMessageID);
    assertTrue(lObjects.contains(lMessageID));
    assertTrue(lObjects.contains(lErrorCode));
  }

  @Test
  @Order(30)
  public void testTraceLevelCheck( ) {
    LogManager lLogManager = LogManager.getLogManager();
    lLogManager.reset();
    Logger lLogger = lLogManager.getLogger("");
    lLogger.setLevel(Level.WARNING);

    ErrorCode lMessageID = new ErrorCode(123456, TraceLevel.WARN);
    assertTrue(lMessageID.isEnabled());

    lLogger.setLevel(Level.SEVERE);
    assertFalse(lMessageID.isEnabled());
  }

}
