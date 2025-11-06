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
import com.anaptecs.jeaf.xfun.api.info.InfoProvider;

/**
 * Annotation is intended to be used to provide application info.
 * 
 * In order to make configuration even simpler JEAF's Maven Plugin can be used. The plugin analysis the projects
 * classpath for classes with annotations and generates the required configuration files. There should be only one
 * interface or class with this annotation in your classpath. If multiple usages are detected then the first one will be
 * used according to the order of the classpath.
 * 
 * If you do not use this annotation at all then the default implementations will be used. These default implementations
 * should be sufficient for most cases.
 * 
 * {@link InfoProvider#getApplicationInfo()}
 */
@Retention(RUNTIME)
@Target(TYPE)
@Documented
public @interface AppInfo {
  /**
   * Name of the resource that contains the name of the class with the @AppInfo annotation.
   */
  String APP_INFO_RESOURCE_NAME = "ApplicationInfo";

  /**
   * Path under which the configuration file is stored.
   */
  String APP_INFO_PATH = XFun.X_FUN_BASE_PATH + '/' + AppInfo.APP_INFO_RESOURCE_NAME;

  /**
   * Name of the resource that contains the name of the class with the @AppInfo annotation.
   */
  String VERSION_INFO_RESOURCE_NAME = "VersionInfo";

  /**
   * Path under which the configuration file is stored.
   */
  String VERSION_INFO_PATH = XFun.X_FUN_BASE_PATH + '/' + AppInfo.VERSION_INFO_RESOURCE_NAME;

  /**
   * ID of the application.
   */
  String applicationID();

  /**
   * Human readable name of the application.
   */
  String applicationName();

  /**
   * Optional description of the application.
   */
  String applicationDescription() default "";

  /**
   * Optional URL of the website of the application.
   */
  String applicationWebsiteURL() default "unknown";

  /**
   * Optional name of the creator of the application-
   */
  String applicationCreator() default "unknown";

  /**
   * Optional URL of the website of the application creator.
   */
  String applicationCreatorURL() default "unkown";

  /**
   * Name of the property file which contains the version info of the app. The value must point to a property file that
   * is resolved via the classpath.
   */
  String versionInfoPropertyFileName() default VERSION_INFO_PATH;
}
