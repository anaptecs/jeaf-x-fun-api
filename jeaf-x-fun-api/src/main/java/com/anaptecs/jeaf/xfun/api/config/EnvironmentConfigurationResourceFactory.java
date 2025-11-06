/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.config;

/**
 * Interface defines the way how to get environment variables of the underlying operating system as configuration.
 */
public interface EnvironmentConfigurationResourceFactory {

  /**
   * Method returns an configuration object for accessing environment variables of the underlying operating system.
   * 
   * @return {@link Configuration} Configuration to access the underlying OS environment variables. The method never
   * returns null.
   */
  ConfigurationResource getEnvironmentConfigurationResource( );
}
