/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.config;

/**
 * Interface defines the way how to get configuration values that are defined as system properties.
 */
public interface SystemPropertiesConfigurationResourceFactory {
  /**
   * Method returns an object to access system properties.
   * 
   * @return {@link Configuration} Object to access JVM's system properties. The method never returns null.
   */
  ConfigurationResource getSystemPropertiesConfigurationResource( );
}
