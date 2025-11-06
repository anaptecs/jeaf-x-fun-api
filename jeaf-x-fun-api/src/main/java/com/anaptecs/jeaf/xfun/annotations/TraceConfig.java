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
import java.util.Locale;

import com.anaptecs.jeaf.xfun.api.XFun;

/**
 * Annotation can be used configure tracing.
 * 
 * In order to make configuration even simpler JEAF's Maven Plugin can be used. The plugin analysis the projects
 * classpath for classes with annotations and generates the required configuration files. There should be only one
 * interface or class with this annotation in your classpath. If multiple usages are detected then the first one will be
 * used according to the order of the classpath.
 * 
 * If you do not use this annotation at all then the default settings will be used. These default settings should be
 * sufficient for most cases.
 */
@Retention(RUNTIME)
@Target({ TYPE })
@Documented
public @interface TraceConfig {
  /**
   * Name of the resource that contains the name of the class with the @TraceConfig annotation.
   */
  String TRACE_CONFIG_RESOURCE_NAME = "TraceConfig";

  /**
   * Path under which the configuration file is stored.
   */
  String TRACE_CONFIG_PATH = XFun.X_FUN_BASE_PATH + '/' + TRACE_CONFIG_RESOURCE_NAME;

  /**
   * Constant for name of default logger
   */
  String DEFAULT_LOGGER_NAME = "JEAF_DEFAULT_LOGGER";

  /**
   * Constant for default message format.
   */
  String DEFAULT_FORMAT = "[%1$5d] %3$-20s %2$s";

  /**
   * Default value for a custom trace locale.
   */
  String CUSTOM_TRACE_LOCALE = "en";

  /**
   * By default the system locale is used for traces.
   */
  boolean TRACE_WITH_SYSTEM_LOCALE = true;

  /**
   * By default traces are indented.
   */
  boolean INDENT_TRACE = true;

  /**
   * Default indentation size.
   */
  int DEFAULT_INDENT_SIZE = 4;

  /**
   * Due to data privacy protection by default the current user is not shown in traces.
   */
  boolean SHOW_CURRENT_USER_IN_TRACES = false;

  /**
   * By default application id is not used as prefix for loggers. This might be helpful in application server
   * environments where multiple apps are run and all make use of JEAF X-Fun.
   */
  boolean USE_APPLICATION_ID_AS_PREFIX = false;

  /**
   * Name of the default logger.
   */
  String defaultLoggerName() default DEFAULT_LOGGER_NAME;

  /**
   * Parameter defines if the system default language is used for traces or not.
   */
  boolean traceWithSystemLocale() default TRACE_WITH_SYSTEM_LOCALE;

  /**
   * Parameter can be used to customize the format of messages that are written to the trace files.
   * 
   * In addition to the trace message itself and standard information like timestamp, thread name etc. also additional
   * information from the application can be added. Currently the following information are available and can be
   * referenced by the following indices:
   * <ol>
   * <li>MessageID / ErrorCode of the message</li>
   * <li>The message itself</li>
   * <li>Login name of current user</li>
   * </ol>
   * 
   * By default the following format will be used: {@value #DEFAULT_FORMAT} <br/>
   * This will result in a trace message like this {@code "[20001] donald.duck         The actual trace message"}
   */
  String traceMessageFormat() default DEFAULT_FORMAT;

  /**
   * Trace locale that should be used. The locale ha to be defined as IETF BCP 47 language tag string. For further
   * details please refer to {@link Locale#forLanguageTag(String)}
   */
  String customTraceLocale() default CUSTOM_TRACE_LOCALE;

  /**
   * Parameter defines if traces are indented or not.
   */
  boolean indentTrace() default INDENT_TRACE;

  /**
   * Parameter defines the indentation size.
   */
  int indentSize() default DEFAULT_INDENT_SIZE;

  /**
   * Parameter defines if all trace instances should be exposed via JMX so that they can be adjusted during runtime.
   */
  boolean exposeLoggersViaJMX() default true;

  /**
   * Parameter defines if the current user should be shown in traces. Due to data privacy regulations this value is
   * disabled by default.
   */
  boolean showCurrentUserInTraces() default SHOW_CURRENT_USER_IN_TRACES;

  /**
   * Parameter defines if the ID of the application should be used as prefix for loggers. This might be helpful in
   * application server environments when multiple apps using JEAF X-Fun are run. By default the prefix is not used.
   */
  boolean useApplicationIDAsPrefix() default USE_APPLICATION_ID_AS_PREFIX;

  /**
   * Property defines the path of the resource file where all the object formatters are be listed.
   */
  String objectFormattersResourcePath() default TraceObjectFormatter.TRACE_OBJECT_FORMATTER_PATH;
}
