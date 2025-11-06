/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.errorhandling;

import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.info.VersionInfo;

/**
 * Application exception that is internally used by JEAF in order to indicate application specific problems.
 * 
 * @author JEAF Development Team
 * @version 1.0
 * 
 * @see com.anaptecs.jeaf.xfun.api.errorhandling.ApplicationException
 */
public class JEAFApplicationException extends ApplicationException {
  /**
   * Generated serial version uid for this class.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Constructor initializes this application exception with the passed error code.
   * 
   * @param pErrorCode Error code identifying the occurred problem. Among other things the error code is used to
   * determine the corresponding error message. The parameter must not be null.
   */
  public JEAFApplicationException( ErrorCode pErrorCode ) {
    this(pErrorCode, (Throwable) null, (String[]) null);
  }

  /**
   * Constructor initializes this application exception with the passed error code and message parameters.
   * 
   * @param pErrorCode Error code identifying the occurred problem. Among other things the error code is used to
   * determine the corresponding error message. The parameter must not be null.
   * @param pMessageParameters String array contains all values that are used to create a parameterized error message.
   * The message parameters usually describe the concrete context in which the exception occurred. The parameter may be
   * null.
   */
  public JEAFApplicationException( ErrorCode pErrorCode, String... pMessageParameters ) {
    super(pErrorCode, (Throwable) null, pMessageParameters);
  }

  /**
   * Constructor initializes this application exception with the passed error code and Throwable object that caused this
   * exception.
   * 
   * @param pErrorCode Error code identifying the occurred problem. Among other things the error code is used to
   * determine the corresponding error message. The parameter must not be null.
   * @param pCause Throwable object that caused this exception. The parameter may be null.
   * @param pMessageParameters String array contains all values that are used to create a parameterized error message.
   * The message parameters usually describe the concrete context in which the exception occurred. The parameter may be
   * null.
   */
  public JEAFApplicationException( ErrorCode pErrorCode, Throwable pCause, String... pMessageParameters ) {
    super(pErrorCode, pCause, pMessageParameters);
  }

  /**
   * Constructor initializes this application exception with the passed error code and Throwable object that caused this
   * exception.
   * 
   * @param pErrorCode Error code identifying the occurred problem. Among other things the error code is used to
   * determine the corresponding error message. The parameter must not be null.
   * @param pTechnicalDetails Technical details about an exception. They might be helpful to solve the issue / root
   * cause. This information is not intended to be shown to users but will be added to log files for example.
   * @param pCause Throwable object that caused this exception. The parameter may be null.
   * @param pMessageParameters String array contains all values that are used to create a parameterized error message.
   * The message parameters usually describe the concrete context in which the exception occurred. The parameter may be
   * null.
   */
  public JEAFApplicationException( ErrorCode pErrorCode, String pTechnicalDetails, Throwable pCause,
      String... pMessageParameters ) {
    super(pErrorCode, pTechnicalDetails, pCause, pMessageParameters);
  }

  /**
   * Constructor initializes this application exception with the passed error code, message parameters and Throwable
   * object that caused this exception.
   * 
   * @param pErrorCode Error code identifying the occurred problem. Among other things the error code is used to
   * determine the corresponding error message. The parameter must not be null.
   * @param pMessageParameters String array contains all values that are used to create a parameterized error message.
   * The message parameters usually describe the concrete context in which the exception occurred. The parameter may be
   * null.
   * @param pCause Throwable object that caused this exception. The parameter may be null.
   * 
   * @deprecated Please use {@link JEAFApplicationException#ApplicationException(ErrorCode, Throwable, String...)}
   * instead.
   */
  @Deprecated
  public JEAFApplicationException( ErrorCode pErrorCode, String[] pMessageParameters, Throwable pCause ) {
    super(pErrorCode, pCause, pMessageParameters);
  }

  /**
   * Method resolves the version the JEAF framework.
   * 
   * @return VersionInfo Information about the used JEAF version. The method never returns null.
   */
  protected VersionInfo resolveVersionInfo( ) {
    return XFun.getVersionInfo();
  }
}