/**
 * Copyright 2004 - 2021 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.config;

import com.anaptecs.jeaf.xfun.annotations.XFunConfig;

/**
 * Interface defines the factory to create a new locale provider implementation. Which factory should be used can be
 * configured using annotation {@link XFunConfig}
 * 
 * Implementations are required to provide an public default constructor.
 */
public interface ConfigurationProviderFactory {
  /**
   * Method returns the configuration provider that should be used if the factory is configured.
   * 
   * @return {@link ConfigurationProvider} Configuration provider that should be used. The method must not return null.
   */
  ConfigurationProvider getConfigurationProvider( );
}
