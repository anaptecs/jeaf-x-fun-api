/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;

@Retention(RUNTIME)
@Target(TYPE)
@Documented
public @interface StartupInfoConfig {
  /**
   * Name of the resource that contains the name of the class with the @AppInfo annotation.
   */
  String STARTUP_INFO_CONFIG_RESOURCE_NAME = "StartupInfoConfig";

  /**
   * Path under which the configuration file is stored.
   */
  String STARTUP_INFO_CONFIG_PATH = XFun.X_FUN_BASE_PATH + '/' + STARTUP_INFO_CONFIG_RESOURCE_NAME;

  /**
   * Default trace level for startup info.
   */
  TraceLevel DEFAULT_STARTUP_INFO_TRACE_LEVEL = TraceLevel.INFO;

  /**
   * By default startup info of X-Fun will be traced.
   */
  boolean DEFAULT_TRACE_STARTUP_INFO = true;

  /**
   * Property defines if information about the X-Fun configuration should be traced on startup or not. By default traces
   * are enabled.
   */
  boolean traceStartupInfo() default DEFAULT_TRACE_STARTUP_INFO;

  /**
   * Property defines the trace level that should be used for startup info. By default info will be used.
   */
  TraceLevel startupInfoTraceLevel() default TraceLevel.INFO;

  /**
   * Property defines the path of the resource file where all the startup info writers are listed.
   */
  String startupInfoWritersResourcePath() default XFun.X_FUN_BASE_PATH + "/"
      + StartupInfoWriterImpl.STARTUP_INFO_WRITERS_RESOURCE_NAME;
}
