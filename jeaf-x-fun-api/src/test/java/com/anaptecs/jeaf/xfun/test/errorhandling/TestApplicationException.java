/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.errorhandling;

import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.errorhandling.ApplicationException;
import com.anaptecs.jeaf.xfun.api.errorhandling.ErrorCode;
import com.anaptecs.jeaf.xfun.api.info.VersionInfo;

public class TestApplicationException extends ApplicationException {

  private static final long serialVersionUID = 1L;

  public TestApplicationException( ErrorCode pErrorCode ) {
    super(pErrorCode);
  }

  public TestApplicationException( ErrorCode pErrorCode, String[] pMessageParameters ) {
    super(pErrorCode, pMessageParameters);
  }

  @Deprecated
  public TestApplicationException( ErrorCode pErrorCode, String[] pMessageParameters, Throwable pCause ) {
    super(pErrorCode, pMessageParameters, pCause);
  }

  public TestApplicationException( ErrorCode pErrorCode, Throwable pCause, String... pMessageParameters ) {
    super(pErrorCode, pCause, pMessageParameters);
  }

  public TestApplicationException( ErrorCode pErrorCode, String pTechnicalDetails, Throwable pCause,
      String... pMessageParameters ) {
    super(pErrorCode, pTechnicalDetails, pCause, pMessageParameters);
  }

  @Override
  protected VersionInfo resolveVersionInfo( ) {
    return XFun.getVersionInfo();
  }
}
