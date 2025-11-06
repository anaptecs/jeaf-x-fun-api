/**
 * Copyright 2004 - 2020 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.errorhandling;

/**
 * Class implements a an exception class that is intended to only be used during the bootstrapping of JEAF X-Fun. For
 * this case an own exception class is needed as JEAF's exception handling mechanism is only available after
 * bootstrapping of JEAF X-Fun.
 * 
 * @author JEAF Development Team
 */
public class JEAFBootstrapException extends RuntimeException {
  /**
   * Default serial version UID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Initialize exception.
   * 
   * @param pMessage Description of the problem that occurred. Parameter may be null, but please ensure that you also
   * provide a meaningful exception message.
   */
  public JEAFBootstrapException( String pMessage ) {
    super(pMessage);
  }

  /**
   * Initialize exception.
   * 
   * @param pMessage Description of the problem that occurred. Parameter may be null, but please ensure that you also
   * provide a meaningful exception message.
   * @param pThrowable Throwable that caused this exception to be thrown. The parameter may be null.
   */
  public JEAFBootstrapException( String pMessage, Throwable pThrowable ) {
    super(pMessage, pThrowable);
  }
}
