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

/**
 * Annotation is intended to be used to mark classes as datatype converter. Datatype converters can be used for the
 * conversion of object from one datatype into another one. All converters will be stored in a so called datatype
 * converter registry. s
 * 
 * All classes that are annotated with this annotation must implement the interface
 * {@link com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverter}.
 * 
 * In order to make configuration simpler JEAF's Maven Plugin can be used. The plugin analysis the projects classpath
 * for classes with this annotations and generates the required configuration file.
 * 
 */
@Retention(RUNTIME)
@Target(TYPE)
@Documented
public @interface DatatypeConverterImpl {
  /**
   * Name of the resource that contains the names of all classes with the @DatatypeConverter annotation.
   */
  String DATATYPE_CONVERTERS_RESOURCE_NAME = "DatatypeConverters";

  /**
   * Path under which the configuration file is stored.
   */
  String DATATYPE_CONVERTERS_PATH = XFun.X_FUN_BASE_PATH + '/' + DATATYPE_CONVERTERS_RESOURCE_NAME;
}
