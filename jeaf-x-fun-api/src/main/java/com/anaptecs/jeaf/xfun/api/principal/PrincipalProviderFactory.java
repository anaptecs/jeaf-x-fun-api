/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.principal;

import com.anaptecs.jeaf.xfun.annotations.XFunConfig;

/**
 * Interface defines the factory to create a new principal provider implementation. Which factory should be used can be
 * configured using annotation {@link XFunConfig}
 */
public interface PrincipalProviderFactory {
  /**
   * Method creates a new instance of the principal provider repository.
   * 
   * @return {@link PrincipalProvider} New instance of the message repository. The method must not return null.
   */
  PrincipalProvider getPrincipalProvider( );
}
