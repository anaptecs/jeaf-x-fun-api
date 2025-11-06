/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.locale;

import java.util.Locale;

import com.anaptecs.jeaf.xfun.annotations.XFunConfig;
import com.anaptecs.jeaf.xfun.api.XFun;

/**
 * Locale providers are JEAF X-Fun's mechanism to resolve the current locale. Depending on the application this can be
 * the language of the current operating system user the language of the currently logged in user in an web application.
 * 
 * It's strongly recommended that implementations of this interface are thread-safe so that one instance of this object
 * can be shared among all threads of an applications.
 * 
 * Locale provider implementations will be created via a {@link LocaleProviderFactory} implementation that is configured
 * in annotation {@link XFunConfig}
 */
public interface LocaleProvider {
  /**
   * Method returns the configured locale provider.
   * 
   * @return {@link LocaleProvider} Locale provider that is used. The method never returns null.
   */
  static LocaleProvider getLocaleProvider( ) {
    return XFun.getLocaleProvider();
  }

  /**
   * Method returns the current locale. How the current locale is determined depends on the implementation.
   * Implementations may determine the current locale in a context sensitive way as it is required for multi user
   * environments where every user has its own locale.
   * 
   * @return Locale Locale that is appropriate in the current context. The method must not return null.
   */
  Locale getCurrentLocale( );
}
