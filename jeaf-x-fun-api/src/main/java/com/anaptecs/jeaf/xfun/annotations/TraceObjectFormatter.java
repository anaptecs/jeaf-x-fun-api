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
import com.anaptecs.jeaf.xfun.api.trace.ObjectFormatter;

/**
 * JEAF X-Fun trace component supports so called ObjectFormatter. These classes are responsible for formatting classes
 * for traces.
 * 
 * All classes that are annotated with this annotation must implement the interface {@link ObjectFormatter}.
 * 
 * In order to make configuration simpler JEAF's Maven Plugin can be used. The plugin analysis the projects classpath
 * for classes with this annotations and generates the required configuration file.
 */
@Retention(RUNTIME)
@Target(TYPE)
@Documented
public @interface TraceObjectFormatter {
  /**
   * Name of the resource that contains the names of all classes with the @TraceObjectFormatter annotation.
   */
  String TRACE_OBEJCT_FORMATTERS_RESOURCE_NAME = "TraceObjectFormatters";

  /**
   * Path under which the configuration file is stored.
   */
  String TRACE_OBJECT_FORMATTER_PATH = XFun.X_FUN_BASE_PATH + '/' + TRACE_OBEJCT_FORMATTERS_RESOURCE_NAME;

  /**
   * Property defines all the classes for which the annotated class should be used as formatter. If you have a hierarchy
   * of classes that should all use the same formatter then its sufficient to only define the base class.
   */
  Class<?>[] supportedClasses();
}
