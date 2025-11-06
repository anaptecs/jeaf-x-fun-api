/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.principal;

import java.security.Principal;

import com.anaptecs.jeaf.xfun.api.XFun;

/**
 * Interface defines the methods of a principal provider. A principal provider is responsible to provide information
 * about the current user. Depending on the application this can simply be the current user of the operating systems or
 * something like the current user in an web application.
 */
public interface PrincipalProvider {
  /**
   * Method returns the configured principal provider.
   * 
   * @return {@link PrincipalProvider} Principal provider that is used. The method never returns null.
   */
  static PrincipalProvider getPrincipalProvider( ) {
    return XFun.getPrincipalProvider();
  }

  /**
   * Method returns the current principal object. Its up to the implementation who to find the current principal object
   * since this highly depends on the runtime environment.
   * 
   * @return {@link Principal} Current principal object. The method may return null.
   */
  Principal getCurrentPrincipal( );
}
