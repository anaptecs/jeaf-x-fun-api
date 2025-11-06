/**
 * Copyright 2004 - 2021 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test_default_runtime;

import com.anaptecs.jeaf.xfun.api.config.ConfigurationProvider;
import com.anaptecs.jeaf.xfun.api.config.ConfigurationProviderFactory;

public class ConfigurationProviderFactoryImpl implements ConfigurationProviderFactory {

  @Override
  public ConfigurationProvider getConfigurationProvider( ) {
    return new ConfigurationProviderImpl();
  }

}
