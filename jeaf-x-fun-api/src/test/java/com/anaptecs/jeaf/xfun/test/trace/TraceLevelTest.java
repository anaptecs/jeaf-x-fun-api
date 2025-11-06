/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.trace;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TraceLevelTest {

  @Test
  @Order(10)
  public void testHasHigherPriority( ) {
    // TRACE
    assertFalse(TraceLevel.TRACE.hasHigherPriority(TraceLevel.TRACE));
    assertTrue(TraceLevel.TRACE.hasHigherPriority(TraceLevel.DEBUG));
    assertTrue(TraceLevel.TRACE.hasHigherPriority(TraceLevel.INFO));
    assertTrue(TraceLevel.TRACE.hasHigherPriority(TraceLevel.WARN));
    assertTrue(TraceLevel.TRACE.hasHigherPriority(TraceLevel.ERROR));
    assertTrue(TraceLevel.TRACE.hasHigherPriority(TraceLevel.FATAL));

    // DEBUG
    assertFalse(TraceLevel.DEBUG.hasHigherPriority(TraceLevel.TRACE));
    assertFalse(TraceLevel.DEBUG.hasHigherPriority(TraceLevel.DEBUG));
    assertTrue(TraceLevel.DEBUG.hasHigherPriority(TraceLevel.INFO));
    assertTrue(TraceLevel.DEBUG.hasHigherPriority(TraceLevel.WARN));
    assertTrue(TraceLevel.DEBUG.hasHigherPriority(TraceLevel.ERROR));
    assertTrue(TraceLevel.DEBUG.hasHigherPriority(TraceLevel.FATAL));

    // INFO
    assertFalse(TraceLevel.INFO.hasHigherPriority(TraceLevel.TRACE));
    assertFalse(TraceLevel.INFO.hasHigherPriority(TraceLevel.DEBUG));
    assertFalse(TraceLevel.INFO.hasHigherPriority(TraceLevel.INFO));
    assertTrue(TraceLevel.INFO.hasHigherPriority(TraceLevel.WARN));
    assertTrue(TraceLevel.INFO.hasHigherPriority(TraceLevel.ERROR));
    assertTrue(TraceLevel.INFO.hasHigherPriority(TraceLevel.FATAL));

    // WARN
    assertFalse(TraceLevel.WARN.hasHigherPriority(TraceLevel.TRACE));
    assertFalse(TraceLevel.WARN.hasHigherPriority(TraceLevel.DEBUG));
    assertFalse(TraceLevel.WARN.hasHigherPriority(TraceLevel.INFO));
    assertFalse(TraceLevel.WARN.hasHigherPriority(TraceLevel.WARN));
    assertTrue(TraceLevel.WARN.hasHigherPriority(TraceLevel.ERROR));
    assertTrue(TraceLevel.WARN.hasHigherPriority(TraceLevel.FATAL));

    // ERROR
    assertFalse(TraceLevel.ERROR.hasHigherPriority(TraceLevel.TRACE));
    assertFalse(TraceLevel.ERROR.hasHigherPriority(TraceLevel.DEBUG));
    assertFalse(TraceLevel.ERROR.hasHigherPriority(TraceLevel.INFO));
    assertFalse(TraceLevel.ERROR.hasHigherPriority(TraceLevel.WARN));
    assertFalse(TraceLevel.ERROR.hasHigherPriority(TraceLevel.ERROR));
    assertTrue(TraceLevel.ERROR.hasHigherPriority(TraceLevel.FATAL));

    // FATAL
    assertFalse(TraceLevel.FATAL.hasHigherPriority(TraceLevel.TRACE));
    assertFalse(TraceLevel.FATAL.hasHigherPriority(TraceLevel.DEBUG));
    assertFalse(TraceLevel.FATAL.hasHigherPriority(TraceLevel.INFO));
    assertFalse(TraceLevel.FATAL.hasHigherPriority(TraceLevel.WARN));
    assertFalse(TraceLevel.FATAL.hasHigherPriority(TraceLevel.ERROR));
    assertFalse(TraceLevel.FATAL.hasHigherPriority(TraceLevel.FATAL));
  }

  @Test
  @Order(10)
  public void testHasHigherOrEqualPriority( ) {
    // TRACE
    assertTrue(TraceLevel.TRACE.hasHigherOrEqualPriority(TraceLevel.TRACE));
    assertTrue(TraceLevel.TRACE.hasHigherOrEqualPriority(TraceLevel.DEBUG));
    assertTrue(TraceLevel.TRACE.hasHigherOrEqualPriority(TraceLevel.INFO));
    assertTrue(TraceLevel.TRACE.hasHigherOrEqualPriority(TraceLevel.WARN));
    assertTrue(TraceLevel.TRACE.hasHigherOrEqualPriority(TraceLevel.ERROR));
    assertTrue(TraceLevel.TRACE.hasHigherOrEqualPriority(TraceLevel.FATAL));

    // DEBUG
    assertFalse(TraceLevel.DEBUG.hasHigherOrEqualPriority(TraceLevel.TRACE));
    assertTrue(TraceLevel.DEBUG.hasHigherOrEqualPriority(TraceLevel.DEBUG));
    assertTrue(TraceLevel.DEBUG.hasHigherOrEqualPriority(TraceLevel.INFO));
    assertTrue(TraceLevel.DEBUG.hasHigherOrEqualPriority(TraceLevel.WARN));
    assertTrue(TraceLevel.DEBUG.hasHigherOrEqualPriority(TraceLevel.ERROR));
    assertTrue(TraceLevel.DEBUG.hasHigherOrEqualPriority(TraceLevel.FATAL));

    // INFO
    assertFalse(TraceLevel.INFO.hasHigherOrEqualPriority(TraceLevel.TRACE));
    assertFalse(TraceLevel.INFO.hasHigherOrEqualPriority(TraceLevel.DEBUG));
    assertTrue(TraceLevel.INFO.hasHigherOrEqualPriority(TraceLevel.INFO));
    assertTrue(TraceLevel.INFO.hasHigherOrEqualPriority(TraceLevel.WARN));
    assertTrue(TraceLevel.INFO.hasHigherOrEqualPriority(TraceLevel.ERROR));
    assertTrue(TraceLevel.INFO.hasHigherOrEqualPriority(TraceLevel.FATAL));

    // WARN
    assertFalse(TraceLevel.WARN.hasHigherOrEqualPriority(TraceLevel.TRACE));
    assertFalse(TraceLevel.WARN.hasHigherOrEqualPriority(TraceLevel.DEBUG));
    assertFalse(TraceLevel.WARN.hasHigherOrEqualPriority(TraceLevel.INFO));
    assertTrue(TraceLevel.WARN.hasHigherOrEqualPriority(TraceLevel.WARN));
    assertTrue(TraceLevel.WARN.hasHigherOrEqualPriority(TraceLevel.ERROR));
    assertTrue(TraceLevel.WARN.hasHigherOrEqualPriority(TraceLevel.FATAL));

    // ERROR
    assertFalse(TraceLevel.ERROR.hasHigherOrEqualPriority(TraceLevel.TRACE));
    assertFalse(TraceLevel.ERROR.hasHigherOrEqualPriority(TraceLevel.DEBUG));
    assertFalse(TraceLevel.ERROR.hasHigherOrEqualPriority(TraceLevel.INFO));
    assertFalse(TraceLevel.ERROR.hasHigherOrEqualPriority(TraceLevel.WARN));
    assertTrue(TraceLevel.ERROR.hasHigherOrEqualPriority(TraceLevel.ERROR));
    assertTrue(TraceLevel.ERROR.hasHigherOrEqualPriority(TraceLevel.FATAL));

    // FATAL
    assertFalse(TraceLevel.FATAL.hasHigherOrEqualPriority(TraceLevel.TRACE));
    assertFalse(TraceLevel.FATAL.hasHigherOrEqualPriority(TraceLevel.DEBUG));
    assertFalse(TraceLevel.FATAL.hasHigherOrEqualPriority(TraceLevel.INFO));
    assertFalse(TraceLevel.FATAL.hasHigherOrEqualPriority(TraceLevel.WARN));
    assertFalse(TraceLevel.FATAL.hasHigherOrEqualPriority(TraceLevel.ERROR));
    assertTrue(TraceLevel.FATAL.hasHigherOrEqualPriority(TraceLevel.FATAL));
  }

  @Test
  @Order(30)
  public void testGetLevelWithHigherPriority( ) {
    // TRACE
    assertEquals(TraceLevel.TRACE, TraceLevel.TRACE.getLevelWithHigherPriority(TraceLevel.TRACE));
    assertEquals(TraceLevel.DEBUG, TraceLevel.TRACE.getLevelWithHigherPriority(TraceLevel.DEBUG));
    assertEquals(TraceLevel.INFO, TraceLevel.TRACE.getLevelWithHigherPriority(TraceLevel.INFO));
    assertEquals(TraceLevel.WARN, TraceLevel.TRACE.getLevelWithHigherPriority(TraceLevel.WARN));
    assertEquals(TraceLevel.ERROR, TraceLevel.TRACE.getLevelWithHigherPriority(TraceLevel.ERROR));
    assertEquals(TraceLevel.FATAL, TraceLevel.TRACE.getLevelWithHigherPriority(TraceLevel.FATAL));

    // DEBUG
    assertEquals(TraceLevel.DEBUG, TraceLevel.DEBUG.getLevelWithHigherPriority(TraceLevel.TRACE));
    assertEquals(TraceLevel.DEBUG, TraceLevel.DEBUG.getLevelWithHigherPriority(TraceLevel.DEBUG));
    assertEquals(TraceLevel.INFO, TraceLevel.DEBUG.getLevelWithHigherPriority(TraceLevel.INFO));
    assertEquals(TraceLevel.WARN, TraceLevel.DEBUG.getLevelWithHigherPriority(TraceLevel.WARN));
    assertEquals(TraceLevel.ERROR, TraceLevel.DEBUG.getLevelWithHigherPriority(TraceLevel.ERROR));
    assertEquals(TraceLevel.FATAL, TraceLevel.DEBUG.getLevelWithHigherPriority(TraceLevel.FATAL));

    // INFO
    assertEquals(TraceLevel.INFO, TraceLevel.INFO.getLevelWithHigherPriority(TraceLevel.TRACE));
    assertEquals(TraceLevel.INFO, TraceLevel.INFO.getLevelWithHigherPriority(TraceLevel.DEBUG));
    assertEquals(TraceLevel.INFO, TraceLevel.INFO.getLevelWithHigherPriority(TraceLevel.INFO));
    assertEquals(TraceLevel.WARN, TraceLevel.INFO.getLevelWithHigherPriority(TraceLevel.WARN));
    assertEquals(TraceLevel.ERROR, TraceLevel.INFO.getLevelWithHigherPriority(TraceLevel.ERROR));
    assertEquals(TraceLevel.FATAL, TraceLevel.INFO.getLevelWithHigherPriority(TraceLevel.FATAL));

    // WARN
    assertEquals(TraceLevel.WARN, TraceLevel.WARN.getLevelWithHigherPriority(TraceLevel.TRACE));
    assertEquals(TraceLevel.WARN, TraceLevel.WARN.getLevelWithHigherPriority(TraceLevel.DEBUG));
    assertEquals(TraceLevel.WARN, TraceLevel.WARN.getLevelWithHigherPriority(TraceLevel.INFO));
    assertEquals(TraceLevel.WARN, TraceLevel.WARN.getLevelWithHigherPriority(TraceLevel.WARN));
    assertEquals(TraceLevel.ERROR, TraceLevel.WARN.getLevelWithHigherPriority(TraceLevel.ERROR));
    assertEquals(TraceLevel.FATAL, TraceLevel.WARN.getLevelWithHigherPriority(TraceLevel.FATAL));

    // ERROR
    assertEquals(TraceLevel.ERROR, TraceLevel.ERROR.getLevelWithHigherPriority(TraceLevel.TRACE));
    assertEquals(TraceLevel.ERROR, TraceLevel.ERROR.getLevelWithHigherPriority(TraceLevel.DEBUG));
    assertEquals(TraceLevel.ERROR, TraceLevel.ERROR.getLevelWithHigherPriority(TraceLevel.INFO));
    assertEquals(TraceLevel.ERROR, TraceLevel.ERROR.getLevelWithHigherPriority(TraceLevel.WARN));
    assertEquals(TraceLevel.ERROR, TraceLevel.ERROR.getLevelWithHigherPriority(TraceLevel.ERROR));
    assertEquals(TraceLevel.FATAL, TraceLevel.ERROR.getLevelWithHigherPriority(TraceLevel.FATAL));

    // FATAL
    assertEquals(TraceLevel.FATAL, TraceLevel.FATAL.getLevelWithHigherPriority(TraceLevel.TRACE));
    assertEquals(TraceLevel.FATAL, TraceLevel.FATAL.getLevelWithHigherPriority(TraceLevel.DEBUG));
    assertEquals(TraceLevel.FATAL, TraceLevel.FATAL.getLevelWithHigherPriority(TraceLevel.INFO));
    assertEquals(TraceLevel.FATAL, TraceLevel.FATAL.getLevelWithHigherPriority(TraceLevel.WARN));
    assertEquals(TraceLevel.FATAL, TraceLevel.FATAL.getLevelWithHigherPriority(TraceLevel.ERROR));
    assertEquals(TraceLevel.FATAL, TraceLevel.FATAL.getLevelWithHigherPriority(TraceLevel.FATAL));
  }

  @Test
  @Order(30)
  public void testGetLevelWithLowerPriority( ) {
    // TRACE
    assertEquals(TraceLevel.TRACE, TraceLevel.TRACE.getLevelWithLowerPriority(TraceLevel.TRACE));
    assertEquals(TraceLevel.TRACE, TraceLevel.TRACE.getLevelWithLowerPriority(TraceLevel.DEBUG));
    assertEquals(TraceLevel.TRACE, TraceLevel.TRACE.getLevelWithLowerPriority(TraceLevel.INFO));
    assertEquals(TraceLevel.TRACE, TraceLevel.TRACE.getLevelWithLowerPriority(TraceLevel.WARN));
    assertEquals(TraceLevel.TRACE, TraceLevel.TRACE.getLevelWithLowerPriority(TraceLevel.ERROR));
    assertEquals(TraceLevel.TRACE, TraceLevel.TRACE.getLevelWithLowerPriority(TraceLevel.FATAL));

    // DEBUG
    assertEquals(TraceLevel.TRACE, TraceLevel.DEBUG.getLevelWithLowerPriority(TraceLevel.TRACE));
    assertEquals(TraceLevel.DEBUG, TraceLevel.DEBUG.getLevelWithLowerPriority(TraceLevel.DEBUG));
    assertEquals(TraceLevel.DEBUG, TraceLevel.DEBUG.getLevelWithLowerPriority(TraceLevel.INFO));
    assertEquals(TraceLevel.DEBUG, TraceLevel.DEBUG.getLevelWithLowerPriority(TraceLevel.WARN));
    assertEquals(TraceLevel.DEBUG, TraceLevel.DEBUG.getLevelWithLowerPriority(TraceLevel.ERROR));
    assertEquals(TraceLevel.DEBUG, TraceLevel.DEBUG.getLevelWithLowerPriority(TraceLevel.FATAL));

    // INFO
    assertEquals(TraceLevel.TRACE, TraceLevel.INFO.getLevelWithLowerPriority(TraceLevel.TRACE));
    assertEquals(TraceLevel.DEBUG, TraceLevel.INFO.getLevelWithLowerPriority(TraceLevel.DEBUG));
    assertEquals(TraceLevel.INFO, TraceLevel.INFO.getLevelWithLowerPriority(TraceLevel.INFO));
    assertEquals(TraceLevel.INFO, TraceLevel.INFO.getLevelWithLowerPriority(TraceLevel.WARN));
    assertEquals(TraceLevel.INFO, TraceLevel.INFO.getLevelWithLowerPriority(TraceLevel.ERROR));
    assertEquals(TraceLevel.INFO, TraceLevel.INFO.getLevelWithLowerPriority(TraceLevel.FATAL));

    // WARN
    assertEquals(TraceLevel.TRACE, TraceLevel.WARN.getLevelWithLowerPriority(TraceLevel.TRACE));
    assertEquals(TraceLevel.DEBUG, TraceLevel.WARN.getLevelWithLowerPriority(TraceLevel.DEBUG));
    assertEquals(TraceLevel.INFO, TraceLevel.WARN.getLevelWithLowerPriority(TraceLevel.INFO));
    assertEquals(TraceLevel.WARN, TraceLevel.WARN.getLevelWithLowerPriority(TraceLevel.WARN));
    assertEquals(TraceLevel.WARN, TraceLevel.WARN.getLevelWithLowerPriority(TraceLevel.ERROR));
    assertEquals(TraceLevel.WARN, TraceLevel.WARN.getLevelWithLowerPriority(TraceLevel.FATAL));

    // ERROR
    assertEquals(TraceLevel.TRACE, TraceLevel.ERROR.getLevelWithLowerPriority(TraceLevel.TRACE));
    assertEquals(TraceLevel.DEBUG, TraceLevel.ERROR.getLevelWithLowerPriority(TraceLevel.DEBUG));
    assertEquals(TraceLevel.INFO, TraceLevel.ERROR.getLevelWithLowerPriority(TraceLevel.INFO));
    assertEquals(TraceLevel.WARN, TraceLevel.ERROR.getLevelWithLowerPriority(TraceLevel.WARN));
    assertEquals(TraceLevel.ERROR, TraceLevel.ERROR.getLevelWithLowerPriority(TraceLevel.ERROR));
    assertEquals(TraceLevel.ERROR, TraceLevel.ERROR.getLevelWithLowerPriority(TraceLevel.FATAL));

    // FATAL
    assertEquals(TraceLevel.TRACE, TraceLevel.FATAL.getLevelWithLowerPriority(TraceLevel.TRACE));
    assertEquals(TraceLevel.DEBUG, TraceLevel.FATAL.getLevelWithLowerPriority(TraceLevel.DEBUG));
    assertEquals(TraceLevel.INFO, TraceLevel.FATAL.getLevelWithLowerPriority(TraceLevel.INFO));
    assertEquals(TraceLevel.WARN, TraceLevel.FATAL.getLevelWithLowerPriority(TraceLevel.WARN));
    assertEquals(TraceLevel.ERROR, TraceLevel.FATAL.getLevelWithLowerPriority(TraceLevel.ERROR));
    assertEquals(TraceLevel.FATAL, TraceLevel.FATAL.getLevelWithLowerPriority(TraceLevel.FATAL));
  }
}
