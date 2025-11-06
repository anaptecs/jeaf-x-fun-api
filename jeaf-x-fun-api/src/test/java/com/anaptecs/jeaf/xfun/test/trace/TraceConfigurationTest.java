/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.trace;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Locale.Category;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.anaptecs.jeaf.xfun.annotations.TraceConfig;
import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.XFunRuntimeException;
import com.anaptecs.jeaf.xfun.api.trace.TraceConfiguration;
import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TraceConfig(
    indentSize = 12,
    indentTrace = false,
    showCurrentUserInTraces = true,
    useApplicationIDAsPrefix = true,
    traceMessageFormat = "abc",
    customTraceLocale = "it",
    traceWithSystemLocale = false,
    objectFormattersResourcePath = "META-INF/TEST/TRACE/OBJECT_FORMATTERS")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TraceConfigurationTest {

  @Test
  @Order(10)
  public void testDefaultTraceConfiguration( ) {
    TraceConfiguration lTraceConfiguration = TraceConfiguration.getInstance();
    assertEquals(TraceConfig.CUSTOM_TRACE_LOCALE, lTraceConfiguration.getCustomTraceLocale().toLanguageTag());
    assertEquals(TraceConfig.DEFAULT_FORMAT, lTraceConfiguration.getTraceMessageFormat());
    assertEquals(TraceConfig.DEFAULT_INDENT_SIZE, lTraceConfiguration.getIndentSize());
    assertEquals(TraceConfig.INDENT_TRACE, lTraceConfiguration.isTraceIndentationEnabled());
    assertEquals(TraceConfig.SHOW_CURRENT_USER_IN_TRACES, lTraceConfiguration.showCurrentUserInTraces());
    assertEquals(TraceConfig.USE_APPLICATION_ID_AS_PREFIX, lTraceConfiguration.useApplicationIDAsPrefix());
    assertEquals(TraceConfig.TRACE_WITH_SYSTEM_LOCALE, lTraceConfiguration.isTraceWithSystemLocaleEnabled());

    TraceConfig lEmptyConfiguration = lTraceConfiguration.getEmptyConfiguration();
    assertEquals(TraceConfig.class, lEmptyConfiguration.annotationType());
    assertEquals(TraceConfig.CUSTOM_TRACE_LOCALE, lEmptyConfiguration.customTraceLocale());
    assertEquals(TraceConfig.DEFAULT_INDENT_SIZE, lEmptyConfiguration.indentSize());
    assertEquals(TraceConfig.INDENT_TRACE, lEmptyConfiguration.indentTrace());
    assertEquals(XFun.X_FUN_BASE_PATH + "/" + TraceConfig.TRACE_CONFIG_RESOURCE_NAME,
        lEmptyConfiguration.objectFormattersResourcePath());
    assertEquals(TraceConfig.DEFAULT_FORMAT, lEmptyConfiguration.traceMessageFormat());
    assertEquals(TraceConfig.TRACE_WITH_SYSTEM_LOCALE, lEmptyConfiguration.traceWithSystemLocale());
    assertEquals(TraceConfig.SHOW_CURRENT_USER_IN_TRACES, lEmptyConfiguration.showCurrentUserInTraces());
    assertEquals(TraceConfig.USE_APPLICATION_ID_AS_PREFIX, lEmptyConfiguration.useApplicationIDAsPrefix());

    // Test tracing of startup info.
    TestHandler lHandler = new TestHandler();
    LogManager lLogManager = LogManager.getLogManager();
    lLogManager.reset();
    Logger lRootLogger = lLogManager.getLogger("");
    lRootLogger.setLevel(Level.ALL);
    lRootLogger.addHandler(lHandler);
    lTraceConfiguration.traceStartupInfo(XFun.getTrace(), TraceLevel.INFO);
    assertEquals("Custom trace locale:      en", lHandler.getLastLogRecord().getMessage());

    assertEquals(XFun.class, lTraceConfiguration.getStartupCompletedEventSource());
  }

  @Test
  @Order(20)
  public void testCustomTraceConfiguration( ) {
    // Good cases
    TraceConfiguration lTraceConfiguration = new TraceConfiguration("CUSTOM_TRACE", "META-INF/TEST/TRACE", true);
    assertEquals(Locale.ITALIAN, lTraceConfiguration.getCustomTraceLocale());
    assertEquals("abc", lTraceConfiguration.getTraceMessageFormat());
    assertEquals(12, lTraceConfiguration.getIndentSize());
    assertEquals(false, lTraceConfiguration.isTraceIndentationEnabled());
    assertEquals(true, lTraceConfiguration.showCurrentUserInTraces());
    assertEquals(true, lTraceConfiguration.useApplicationIDAsPrefix());
    assertEquals(false, lTraceConfiguration.isTraceWithSystemLocaleEnabled());
    assertEquals("META-INF/TEST/TRACE/OBJECT_FORMATTERS", lTraceConfiguration.objectFormattersResourcePath());

    // Error handling
    // Invalid trace locale.
    try {
      new TraceConfiguration("INVALID_TRACE_CONFIG", "META-INF/TEST/TRACE", true);
      fail("Exception expected.");
    }
    catch (XFunRuntimeException e) {
      assertEquals("Found 2 error(s) during analysis of configuration. Please see error log for further details.",
          e.getMessage());
    }

    lTraceConfiguration = new TraceConfiguration("INVALID_TRACE_CONFIG", "META-INF/TEST/TRACE", false);
    assertEquals(Locale.getDefault(Category.DISPLAY), lTraceConfiguration.getCustomTraceLocale());
    assertEquals("abc", lTraceConfiguration.getTraceMessageFormat());
    assertEquals(-12, lTraceConfiguration.getIndentSize());
    assertEquals(false, lTraceConfiguration.isTraceIndentationEnabled());
    assertEquals(true, lTraceConfiguration.showCurrentUserInTraces());
    assertEquals(false, lTraceConfiguration.useApplicationIDAsPrefix());
    assertEquals(false, lTraceConfiguration.isTraceWithSystemLocaleEnabled());
    assertEquals(2, lTraceConfiguration.getConfigurationErrors().size());
    assertEquals("Trace indentation size must be zero or greater.",
        lTraceConfiguration.getConfigurationErrors().get(0));
    assertEquals("Configuration contains invalid locale 'invalid_locale' for tracing.",
        lTraceConfiguration.getConfigurationErrors().get(1));
  }

  @Test
  @Order(20)
  public void testObjectFormatterResolution( ) {
    // Good cases
    TraceConfiguration lTraceConfiguration = new TraceConfiguration("CUSTOM_TRACE", "META-INF/TEST/TRACE", true);
    assertEquals("META-INF/TEST/TRACE/OBJECT_FORMATTERS", lTraceConfiguration.objectFormattersResourcePath());
    assertNull(lTraceConfiguration.getObjectFormatter(this.getClass()));
    assertNull(lTraceConfiguration.getObjectFormatter(Byte.class));

    assertEquals(ObjectFormatterImpl.class, lTraceConfiguration.getObjectFormatter(Integer.class).getClass());
    assertEquals(ObjectFormatterImpl.class, lTraceConfiguration.getObjectFormatter(String.class).getClass());
    assertEquals(FloatingPointFormatter.class, lTraceConfiguration.getObjectFormatter(BigDecimal.class).getClass());

    // Ensure that class hierarchy is also checked. In case that a class does not have a specific formatter then the one
    // of its base class should be used.
    assertEquals(ObjectFormatterImpl.class, lTraceConfiguration.getObjectFormatter(ParentClass.class).getClass());
    assertEquals(ObjectFormatterImpl.class, lTraceConfiguration.getObjectFormatter(ChildClass.class).getClass());

    // Error handling.
    try {
      lTraceConfiguration.getObjectFormatter(null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pType' must not be null.", e.getMessage());
    }

    // Test usage of a trace formatter that can not be created.
    try {
      lTraceConfiguration = new TraceConfiguration("INVALID_FORMATTER_TRACE_CONFIG", "META-INF/TEST/TRACE", true);
    }
    catch (XFunRuntimeException e) {
      assertEquals(
          "Unable to create new ObjectFormatter instance of class com.anaptecs.jeaf.xfun.test.trace.BrokenTraceFormatter",
          e.getMessage());
    }
  }
}
