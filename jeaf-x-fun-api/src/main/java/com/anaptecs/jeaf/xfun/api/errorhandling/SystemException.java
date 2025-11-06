/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.errorhandling;

import java.util.Date;

import com.anaptecs.jeaf.xfun.api.info.VersionInfo;
import com.anaptecs.jeaf.xfun.bootstrap.Assert;
import com.anaptecs.jeaf.xfun.bootstrap.Check;

/**
 * Class is base class for so called system exceptions. System exceptions are used to indicate technical problems like a
 * broken network connection. System exception must only be used in cases where technical problems arise and never in
 * the context of application specific problems.
 * 
 * @see com.anaptecs.jeaf.xfun.api.errorhandling.ApplicationException
 */
public abstract class SystemException extends RuntimeException implements ExceptionInfoProvider {
  /**
   * Generated serial version uid for this class.
   */
  private static final long serialVersionUID = 1;

  /**
   * Referenced ExceptionInfo object contains all information about this exception. Since the object is created whenever
   * an object of this class is instantiated the reference is never null.
   */
  private final ExceptionInfo exceptionInfo;

  /**
   * Constructor initializes this system exception with the passed error code.
   * 
   * @param pErrorCode Error code identifying the occurred problem. Among other things the error code is used to
   * determine the corresponding error message. The parameter must not be null.
   */
  protected SystemException( ErrorCode pErrorCode ) {
    this(pErrorCode, (Throwable) null, (String[]) null);
  }

  /**
   * Constructor initializes this system exception with the passed error code and message parameters.
   * 
   * @param pErrorCode Error code identifying the occurred problem. Among other things the error code is used to
   * determine the corresponding error message. The parameter must not be null.
   * @param pMessageParameters String array contains all values that are used to create a parameterized error message.
   * The message parameters usually describe the concrete context in which the exception occurred. The parameter may be
   * null.
   */
  protected SystemException( ErrorCode pErrorCode, String... pMessageParameters ) {
    this(pErrorCode, (Throwable) null, pMessageParameters);
  }

  /**
   * Constructor initializes this system exception with the passed error code and Throwable object that caused this
   * exception.
   * 
   * @param pErrorCode Error code identifying the occurred problem. Among other things the error code is used to
   * determine the corresponding error message. The parameter must not be null.
   * @param pCause Throwable object that caused this exception. The parameter may be null.
   * @param pMessageParameters String array contains all values that are used to create a parameterized error message.
   * The message parameters usually describe the concrete context in which the exception occurred. The parameter may be
   * null.
   */
  protected SystemException( ErrorCode pErrorCode, Throwable pCause, String... pMessageParameters ) {
    this(pErrorCode, null, pCause, pMessageParameters);
  }

  /**
   * Constructor initializes this system exception with the passed error code and Throwable object that caused this
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
  protected SystemException( ErrorCode pErrorCode, String pTechnicalDetails, Throwable pCause,
      String... pMessageParameters ) {
    // Call empty constructor of base class.
    super();
    // Check pErrorCode for null.
    Check.checkInvalidParameterNull(pErrorCode, "pErrorCode");

    // Create new ExceptionInfo object that contains all information about the exception.
    VersionInfo lVersionInfo = this.resolveVersionInfo();
    Assert.assertNotNull(lVersionInfo, "lVersionInfo");
    exceptionInfo = new ExceptionInfo(pErrorCode, pMessageParameters, pCause, pTechnicalDetails, lVersionInfo);
  }

  /**
   * Constructor initializes this system exception with the passed error code, message parameters and Throwable object
   * that caused this exception.
   * 
   * @param pErrorCode Error code identifying the occurred problem. Among other things the error code is used to
   * determine the corresponding error message. The parameter must not be null.
   * @param pMessageParameters String array contains all values that are used to create a parameterized error message.
   * The message parameters usually describe the concrete context in which the exception occurred. The parameter may be
   * null.
   * @param pCause Throwable object that caused this exception. The parameter may be null.
   * 
   * @deprecated Please use {@link SystemException#ApplicationException(ErrorCode, Throwable, String...)} instead.
   * 
   */
  @Deprecated
  protected SystemException( ErrorCode pErrorCode, String[] pMessageParameters, Throwable pCause ) {
    this(pErrorCode, pCause, pMessageParameters);
  }

  /**
   * Method returns the ErrorCode object that identifies an occurred exception. An exception can always be identified
   * using its error code.
   * 
   * @return ErrorCode ErrorCode object that identifies the occurred exception. The method never returns null.
   */
  @Override
  public final ErrorCode getErrorCode( ) {
    return exceptionInfo.getErrorCode();
  }

  /**
   * Method returns the parameters that are used to create a parameterized message that describes the occurred
   * exception.
   * 
   * @return String[] Array contains all parameter values for the parameterized message. The method may return null.
   */
  @Override
  public final String[] getMessageParameters( ) {
    return exceptionInfo.getMessageParameters();
  }

  /**
   * Method returns an error message that describes the occurred exception.
   * 
   * @return String Error message describing the occurred exception. The method never returns null.
   */
  @Override
  public final String getMessage( ) {
    return exceptionInfo.getMessage();
  }

  /**
   * Method returns the Throwable object that caused the occurred exception.
   * 
   * @return Throwable Object that caused the occurred exception. The method may return null.
   */
  @Override
  public final synchronized Throwable getCause( ) {
    return exceptionInfo.getCause();
  }

  /**
   * Method returns technical details about an exception. They might be helpful to solve the issue / root cause. This
   * information is not intended to be shown to users but will be added to log files for example.
   * 
   * @return {@link String} Technical details about the occurred exception. The method may return null.
   */
  @Override
  public final String getTechnicalDetails( ) {
    return exceptionInfo.getTechnicalDetails();
  }

  /**
   * Method returns a date object representing the timestamp when the exception occurred.
   * 
   * @return {@link Date} Date object representing the timestamp when the problem occurred. The method never returns
   * null.
   */
  @Override
  public final Date getTimestamp( ) {
    return exceptionInfo.getTimestamp();
  }

  /**
   * Method returns information about the version of the software component which caused this exception.
   * 
   * @return VersionInfo Information about the version of the component that caused this exception. The method must not
   * return null.
   */
  @Override
  public final VersionInfo getVersionInfo( ) {
    return exceptionInfo.getVersionInfo();
  }

  /**
   * Method resolves the version of the software component to which the SystemException subclass belongs.
   * 
   * @return VersionInfo Information about the version of the software component to which the exception belongs. The
   * method must not return null.
   */
  protected abstract VersionInfo resolveVersionInfo( );
}