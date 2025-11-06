/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.trace;

import static com.anaptecs.jeaf.xfun.fallback.trace.FallbackTraceProviderImpl.EMERGENCY_TRACE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.common.ComponentID;
import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;
import com.anaptecs.jeaf.xfun.fallback.trace.FallbackTraceProviderImpl;;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FallbackTraceProviderTest {
  @Test
  @Order(10)
  public void testTraceProvider( ) {
    FallbackTraceProviderImpl lTraceProviderImpl = new FallbackTraceProviderImpl();

    assertEquals(EMERGENCY_TRACE, lTraceProviderImpl.getCurrentTrace(), "Expected emergency trace.");

    // Get trace by name
    assertEquals(EMERGENCY_TRACE, lTraceProviderImpl.getTrace("MyLogger"), "Expected emergency trace.");
    assertEquals(EMERGENCY_TRACE, lTraceProviderImpl.getTrace((String) null), "Expected emergency trace.");

    // Get trace by class
    assertEquals(EMERGENCY_TRACE, lTraceProviderImpl.getTrace(TraceableObject.class), "Expected emergency trace.");
    assertEquals(EMERGENCY_TRACE, lTraceProviderImpl.getTrace((Class<?>) null), "Expected emergency trace.");

    // Get trace by component Id
    assertEquals(EMERGENCY_TRACE, lTraceProviderImpl.getTrace(new ComponentID("Test", "x.y.z")),
        "Expected emergency trace.");
    assertEquals(EMERGENCY_TRACE, lTraceProviderImpl.getTrace((ComponentID) null), "Expected emergency trace.");
    assertEquals(XFun.class, lTraceProviderImpl.getStartupCompletedEventSource());
  }

  @Test
  @Order(20)
  public void testStartupInfo( ) {
    TestHandler lHandler = new TestHandler();
    LogManager lLogManager = LogManager.getLogManager();
    lLogManager.reset();
    Logger lRootLogger = lLogManager.getLogger("");
    lRootLogger.setLevel(Level.ALL);
    lRootLogger.addHandler(lHandler);

    FallbackTraceProviderImpl lFallbackTraceProviderImpl = new FallbackTraceProviderImpl();
    lFallbackTraceProviderImpl.traceStartupInfo(lFallbackTraceProviderImpl.getCurrentTrace(), TraceLevel.INFO);
    assertNotNull(lHandler.getLastLogRecord(), "Message was not traced.");
    assertEquals(lHandler.getLastLogRecord().getLevel(), Level.INFO, "Message has wrong trace level");
    assertEquals("Using emergency trace implementation due to configuration problems.",
        lHandler.getLastLogRecord().getMessage(), "Unexpected message was traced.");

  }
}
