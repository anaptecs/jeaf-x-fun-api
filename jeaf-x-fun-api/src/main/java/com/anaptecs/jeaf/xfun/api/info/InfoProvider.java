/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.info;

import com.anaptecs.jeaf.xfun.api.XFun;

/**
 * Interface defines a so called info provider. Through that interface information about the current application and its
 * runtime environment can be resolved.
 */
public interface InfoProvider {
  /**
   * Method returns the configured info provider.
   * 
   * @return {@link InfoProvider} Info provider that is used. The method never returns null.
   */
  static InfoProvider getInfoProvider( ) {
    return XFun.getInfoProvider();
  }

  /**
   * Method returns information about this application.
   * 
   * @return {@link ApplicationInfo} Information about this application. The method never returns null.
   */
  ApplicationInfo getApplicationInfo( );

  /**
   * Method returns the operating system under which the application is currently executed.
   * 
   * @return {@link OperatingSystem} Operating system and which the application is currently executed. The method never
   * returns null.
   */
  OperatingSystem getOperatingSystem( );

  /**
   * Method returns the runtime environment as defined in the JEAF properties in that the application is currently
   * executed.
   * 
   * @return {@link RuntimeEnvironment} Object describing the defined runtime environment. The method never returns
   * null.
   */
  RuntimeEnvironment getRuntimeEnvironment( );

  /**
   * Method returns information about the current Java Runtime environment such as its major release. These information
   * are derived from the system properties that are provided.
   * 
   * @return {@link JavaRuntimeEnvironment} Information about current Java Runtime Environment.
   */
  JavaRuntimeEnvironment getJavaRuntimeEnvironment( );
}
