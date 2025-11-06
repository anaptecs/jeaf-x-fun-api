/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.errorhandling;

import java.io.Serializable;
import java.util.Date;

import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.info.VersionInfo;
import com.anaptecs.jeaf.xfun.api.messages.LocalizedString;
import com.anaptecs.jeaf.xfun.api.messages.MessageRepository;
import com.anaptecs.jeaf.xfun.bootstrap.Assert;

/**
 * Class ExceptionInfo contains all information about an occurred exception. This class is intended to be used by
 * exceptions that want to provide detailed information about a problem and why it occurred.
 */
public final class ExceptionInfo implements ExceptionInfoProvider, Serializable {
  /**
   * Generated serial version uid for this class.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Constant for generic error.
   */
  public static final LocalizedString ERROR = XFun.getMessageRepository().getLocalizedString(0);

  /**
   * The error code that describes the occurred problem. The attribute is never null, since it has to be set to a non
   * null value in the class' constructor.
   */
  private final ErrorCode errorCode;

  /**
   * String array contains all values that are provided to create a parameterized exception messages. If an exception
   * does not need to be parameterized the attribute is null.
   */
  private final String[] messageParameters;

  /**
   * Throwable object that caused the exception that uses this object to hold the exception information.
   */
  private final Throwable cause;

  /**
   * Technical details about the occurred problem that might be helpful to solve the issue / root cause. This
   * information is not intended to be shown to users but will be added to log files for example. The attribute may be
   * null.
   */
  private final String technicalDetails;

  /**
   * Date object representing the timestamp when the problem occurred
   */
  private final long timestamp;

  /**
   * Version information about the software component that caused the exception that is described by this object. The
   * reference is never null, since it has to be set to a non null value in the class' constructor.
   */
  private final VersionInfo versionInfo;

  /**
   * Initialize object with the passed error code, message parameters and the Throwable object that caused an occurred
   * problem.
   * 
   * @param pErrorCode Object representing the occurred error that caused the exception that uses this object to provide
   * detailed error information. The parameter must not be null.
   * @param pMessageParameters The parameter contains all values that are required to create a parameterized message
   * that describes the occurred problem in detail. The parameter may be null. In this case, no parameterized message
   * can be created.
   * @param pCause Throwable object that caused the exception that uses this object. The parameter may be null, if the
   * problem was not caused by a Throwable object.
   * @param pVersionInfo Information about the version of the component that caused this exception. The parameter must
   * not return null.
   */
  public ExceptionInfo( ErrorCode pErrorCode, String[] pMessageParameters, Throwable pCause, String pTechnicalDetails,
      VersionInfo pVersionInfo ) {
    // Check parameter pErrorCode and pVersionInfo for null. All other parameters may be null.
    Assert.assertNotNull(pErrorCode, "pErrorCode");
    Assert.assertNotNull(pVersionInfo, "pVersionInfo");

    // Assign passed objects to the class' attributes.
    errorCode = pErrorCode;
    if (pMessageParameters != null) {
      messageParameters = pMessageParameters.clone();
    }
    else {
      messageParameters = null;
    }
    cause = pCause;
    technicalDetails = pTechnicalDetails;
    timestamp = System.currentTimeMillis();
    versionInfo = pVersionInfo;
  }

  /**
   * Method returns an error code that describes the occurred problem.
   * 
   * @return ErrorCode ErrorCode object that represent the occurred problem. The method never returns null.
   */
  @Override
  public ErrorCode getErrorCode( ) {
    return errorCode;
  }

  /**
   * Method returns the parameters that are used to create a parameterized message describing the occured problem.
   * 
   * @return String[] Parameter values for a parameterized message. The method returns null if no parameters were
   * provided when the object was created.
   */
  @Override
  public String[] getMessageParameters( ) {
    String[] lMessageParameters;
    if (messageParameters != null) {
      lMessageParameters = messageParameters.clone();
    }
    else {
      lMessageParameters = null;
    }
    return lMessageParameters;
  }

  /**
   * Method returns a message that describes the occurred problem. Therefore the provided message parameters and the
   * error code are used. The method always returns a message text that is localized using the current default locale.
   * 
   * @return String Message describing the occurred problem. The method never returns null.
   */
  @Override
  public String getMessage( ) {
    // Get instance of message repository.
    MessageRepository lRepository = XFun.getMessageRepository();
    // Get message from repository and add error code as suffix.
    String lMessage = lRepository.getMessage(errorCode, messageParameters);
    return "[" + ERROR.toString() + " " + errorCode.getErrorCodeValue() + "] " + lMessage;
  }

  /**
   * Method returns the Throwable object that caused the problem that is described by this object.
   * 
   * @return Throwable Throwable object that caused the problem that is described by this object. The method returns
   * null if the problem was not caused by a Throwable object.
   */
  @Override
  public Throwable getCause( ) {
    return cause;
  }

  /**
   * Method returns technical details about an exception. They might be helpful to solve the issue / root cause. This
   * information is not intended to be shown to users but will be added to log files for example.
   * 
   * @return {@link String} Technical details about the occurred exception. The method may return null.
   */
  @Override
  public String getTechnicalDetails( ) {
    return technicalDetails;
  }

  /**
   * Method returns a date object representing the timestamp when the exception occurred.
   * 
   * @return {@link Date} Date object representing the timestamp when the problem occurred. The method never returns
   * null.
   */
  @Override
  public Date getTimestamp( ) {
    return new Date(timestamp);
  }

  /**
   * Method returns the version of the software component that cause the described exception.
   * 
   * @return VersionInfo Information about the version of the component that caused this exception. The method must not
   * return null.
   */
  @Override
  public VersionInfo getVersionInfo( ) {
    return versionInfo;
  }
}