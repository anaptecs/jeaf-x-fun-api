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

import com.anaptecs.jeaf.xfun.annotations.StartupInfoConfig;
import com.anaptecs.jeaf.xfun.annotations.StartupInfoWriterImpl;
import com.anaptecs.jeaf.xfun.api.trace.StartupInfoConfiguration;
import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;

@StartupInfoConfig(
    startupInfoTraceLevel = TraceLevel.ERROR,
    traceStartupInfo = false,
    startupInfoWritersResourcePath = "META-INF/TEST/STARTUP-INFO/CUSTOM_WRITERS")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StartupInfoConfigurationTest {

  @Test
  @Order(10)
  public void testDefaultStartupConfiguration( ) {
    StartupInfoConfiguration lConfiguration = new StartupInfoConfiguration();
    assertEquals(StartupInfoConfig.DEFAULT_STARTUP_INFO_TRACE_LEVEL, lConfiguration.getStartupInfoTraceLevel());
    assertEquals(StartupInfoConfig.DEFAULT_TRACE_STARTUP_INFO, lConfiguration.traceStartupInfo());
    assertEquals("META-INF/JEAF/XFun/StartupInfoWriters", lConfiguration.startupInfoWritersResourcePath());
    assertEquals(0, lConfiguration.getStartupInfoWriters().size());

    StartupInfoConfig lEmptyConfiguration = lConfiguration.getEmptyConfiguration();
    assertEquals(StartupInfoConfig.class, lEmptyConfiguration.annotationType());
    assertEquals(StartupInfoConfig.DEFAULT_STARTUP_INFO_TRACE_LEVEL, lEmptyConfiguration.startupInfoTraceLevel());
    assertEquals(StartupInfoWriterImpl.STARTUP_INFO_WRITERS_PATH, lEmptyConfiguration.startupInfoWritersResourcePath());
    assertEquals(StartupInfoConfig.DEFAULT_TRACE_STARTUP_INFO, lEmptyConfiguration.traceStartupInfo());

    // No special test are expected.
    assertEquals(0, lConfiguration.checkCustomConfiguration(lEmptyConfiguration).size());
    assertEquals(0, lConfiguration.checkCustomConfiguration(null).size());
  }

  @Test
  @Order(20)
  public void testCustomStartupConfiguration( ) {
    StartupInfoConfiguration lConfiguration =
        new StartupInfoConfiguration("CUSTOM_STARTUP_INFO", "META-INF/TEST/STARTUP-INFO", false);
    assertEquals(TraceLevel.ERROR, lConfiguration.getStartupInfoTraceLevel());
    assertEquals(false, lConfiguration.traceStartupInfo());
    assertEquals("META-INF/TEST/STARTUP-INFO/CUSTOM_WRITERS", lConfiguration.startupInfoWritersResourcePath());
    assertEquals(2, lConfiguration.getStartupInfoWriters().size());
    assertEquals(StartupInfoWriter1.class, lConfiguration.getStartupInfoWriters().get(0).getClass());
    assertEquals(StartupInfoWriter2.class, lConfiguration.getStartupInfoWriters().get(1).getClass());
  }
}
