/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.trace;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.anaptecs.jeaf.xfun.api.checks.AssertionFailedError;
import com.anaptecs.jeaf.xfun.api.common.ComponentID;
import com.anaptecs.jeaf.xfun.api.trace.ContextStackElement;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContextStackElementTest {
  @Test
  @Order(10)
  public void testContextStackElement( ) {
    // Good case.
    ComponentID lComponentID = new ComponentID("X-Fun Unit Tests", "com.anaptecs.jeaf.xfun.test");
    ContextStackElement lContextStackElement = new ContextStackElement("Context-Name", lComponentID);
    assertEquals(lComponentID, lContextStackElement.getComponentID());
    assertEquals("Context-Name", lContextStackElement.getContextName());

    // Error handling
    try {
      new ContextStackElement(null, lComponentID);
    }
    catch (AssertionFailedError e) {
      assertEquals("Assertion failed. 'pContextName' must not be null.", e.getMessage());
    }

    try {
      new ContextStackElement("Context-Name", null);
    }
    catch (AssertionFailedError e) {
      assertEquals("Assertion failed. 'pComponentID' must not be null.", e.getMessage());
    }

    try {
      new ContextStackElement(null, null);
    }
    catch (AssertionFailedError e) {
      assertEquals("Assertion failed. 'pContextName' must not be null.", e.getMessage());
    }
  }
}
