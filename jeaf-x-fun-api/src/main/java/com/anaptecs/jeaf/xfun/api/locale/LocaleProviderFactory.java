/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.locale;

import com.anaptecs.jeaf.xfun.annotations.XFunConfig;

/**
 * Interface defines the factory to create a new locale provider implementation. Which factory should be used can be
 * configured using annotation {@link XFunConfig}
 * 
 * Implementations are required to provide an public default constructor.
 */
public interface LocaleProviderFactory {
  /**
   * Method returns the locale provider that should be used if the factory is configured.
   * 
   * @return {@link LocaleProvider} Implementation of locale provider that should be used. The method must not return
   * null.
   */
  LocaleProvider getLocaleProvider( );
}
