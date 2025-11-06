/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api;

import com.anaptecs.jeaf.xfun.api.errorhandling.SystemException;

/**
 * Class provides a runtime exception for JEAF X-Fun. Class is intended to be used only in cases where we can not throw
 * a X-Fun {@link SystemException} as some environment may not be set up yet.
 * 
 * @author JEAF Development Team
 */
public class XFunRuntimeException extends RuntimeException {
  /**
   * Default serial version UID.
   */
  private static final long serialVersionUID = 1L;

  public XFunRuntimeException( ) {
    super();
  }

  public XFunRuntimeException( String pMessage, Throwable pCause ) {
    super(pMessage, pCause);
  }

  public XFunRuntimeException( String pMessage ) {
    super(pMessage);
  }

  public XFunRuntimeException( Throwable pCause ) {
    super(pCause);
  }

}
