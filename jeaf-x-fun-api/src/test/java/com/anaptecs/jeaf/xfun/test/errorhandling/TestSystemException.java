/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.errorhandling;

import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.errorhandling.ErrorCode;
import com.anaptecs.jeaf.xfun.api.errorhandling.SystemException;
import com.anaptecs.jeaf.xfun.api.info.VersionInfo;

public class TestSystemException extends SystemException {
  private static final long serialVersionUID = 1L;

  public TestSystemException( ErrorCode pErrorCode ) {
    super(pErrorCode);
  }

  public TestSystemException( ErrorCode pErrorCode, String... pMessageParameters ) {
    super(pErrorCode, pMessageParameters);
  }

  public TestSystemException( ErrorCode pErrorCode, Throwable pCause, String... pMessageParameters ) {
    super(pErrorCode, pCause, pMessageParameters);
  }

  public TestSystemException( ErrorCode pErrorCode, String pTechnicalDetails, Throwable pCause,
      String... pMessageParameters ) {
    super(pErrorCode, pTechnicalDetails, pCause, pMessageParameters);
  }

  @Deprecated
  public TestSystemException( ErrorCode pErrorCode, String[] pMessageParameters, Throwable pCause ) {
    super(pErrorCode, pMessageParameters, pCause);
  }

  @Override
  protected VersionInfo resolveVersionInfo( ) {
    return XFun.getVersionInfo();
  }
}
