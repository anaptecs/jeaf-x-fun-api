/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.info;

import com.anaptecs.jeaf.xfun.annotations.XFunConfig;

/**
 * Interface defines the factory to create a new info provider implementation. Which factory should be used can be
 * configured using annotation {@link XFunConfig}
 */
public interface InfoProviderFactory {
  /**
   * Method creates a new instance of the info provider.
   * 
   * @return {@link InfoProvider} New instance of the provider. The method must not return null.
   */
  InfoProvider getInfoProvider( );
}
