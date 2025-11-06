/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.errorhandling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Locale;

import com.anaptecs.jeaf.xfun.api.errorhandling.FailureMessage;
import com.anaptecs.jeaf.xfun.api.messages.MessageID;
import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FailureMessageTest {
  @Test
  @Order(10)
  public void testFailureMessage( ) {
    // Test creation
    MessageID lMessageID = new MessageID(4711, TraceLevel.DEBUG);
    FailureMessage lFailureMessage = new FailureMessage(lMessageID);
    assertEquals(lMessageID, lFailureMessage.getMessageID());
    assertNull(lFailureMessage.getMessageParameters());
    assertNull(lFailureMessage.getCause());
    assertEquals("4711", lFailureMessage.getMessage());
    assertEquals("4711", lFailureMessage.toString());
    assertEquals("4711", lFailureMessage.toString(Locale.CHINESE));
    lFailureMessage.trace();
  }

  @Test
  @Order(20)
  public void testFailureMessageWithParams( ) {
    // Test creation
    MessageID lMessageID = new MessageID(4711, TraceLevel.DEBUG);
    FailureMessage lFailureMessage = new FailureMessage(lMessageID, "abc", "def", "ghi");
    assertEquals(lMessageID, lFailureMessage.getMessageID());
    String[] lMessageParameters = lFailureMessage.getMessageParameters();
    assertEquals(3, lMessageParameters.length);
    assertEquals("abc", lMessageParameters[0]);
    assertEquals("def", lMessageParameters[1]);
    assertEquals("ghi", lMessageParameters[2]);
    assertNull(lFailureMessage.getCause());
    assertEquals("4711: abc def ghi", lFailureMessage.getMessage());
    assertEquals("4711: abc def ghi", lFailureMessage.toString());
  }

  @Test
  @Order(30)
  public void testFailureMessageWithCause( ) {
    // Test creation
    MessageID lMessageID = new MessageID(4711, TraceLevel.INFO);
    Throwable lCause = new NullPointerException();
    FailureMessage lFailureMessage = new FailureMessage(lMessageID, new String[] { "abc", "def", "ghi" }, lCause);
    assertEquals(lMessageID, lFailureMessage.getMessageID());
    String[] lMessageParameters = lFailureMessage.getMessageParameters();
    assertEquals(3, lMessageParameters.length);
    assertEquals("abc", lMessageParameters[0]);
    assertEquals("def", lMessageParameters[1]);
    assertEquals("ghi", lMessageParameters[2]);
    assertEquals(lCause, lFailureMessage.getCause());
    assertEquals("4711: abc def ghi", lFailureMessage.getMessage());
    assertEquals("4711: abc def ghi", lFailureMessage.toString());

    // Test creation
    lMessageID = new MessageID(4711, TraceLevel.INFO);
    lCause = new NullPointerException();
    lFailureMessage = new FailureMessage(lMessageID, null, lCause);
    assertEquals(lMessageID, lFailureMessage.getMessageID());
    assertNull(lFailureMessage.getMessageParameters());
    assertEquals(lCause, lFailureMessage.getCause());
    assertEquals("4711", lFailureMessage.getMessage());
    assertEquals("4711", lFailureMessage.toString());
    lFailureMessage.trace();
  }
}
