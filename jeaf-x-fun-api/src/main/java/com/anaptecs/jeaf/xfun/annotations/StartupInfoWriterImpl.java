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
import com.anaptecs.jeaf.xfun.api.trace.StartupInfoWriter;

/**
 * Annotation is intended to be used to mark classes as startup info writer. StartupInfoWriters are called during
 * initialization by X-Fun in order to trace some information e.g. information about the actual configuration
 * parameters.
 * 
 * All classes that are annotated with this annotation must implement the interface {@link StartupInfoWriter}.
 * 
 * In order to make configuration simpler JEAF's Maven Plugin can be used. The plugin analysis the projects classpath
 * for classes with this annotations and generates the required configuration file.
 */
@Retention(RUNTIME)
@Target(TYPE)
@Documented
public @interface StartupInfoWriterImpl {
  /**
   * Name of the resource that contains the names of all classes with the @MessageResource annotation.
   */
  String STARTUP_INFO_WRITERS_RESOURCE_NAME = "StartupInfoWriters";

  /**
   * Path under which the configuration file is stored.
   */
  String STARTUP_INFO_WRITERS_PATH = XFun.X_FUN_BASE_PATH + '/' + STARTUP_INFO_WRITERS_RESOURCE_NAME;
}
