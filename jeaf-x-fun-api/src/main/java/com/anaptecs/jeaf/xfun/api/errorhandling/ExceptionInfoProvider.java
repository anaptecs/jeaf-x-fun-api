/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.errorhandling;

import java.util.Date;

import com.anaptecs.jeaf.xfun.api.info.VersionInfo;

/**
 * Interface defines methods for exception info providers. Classes implementing this interface provide access to all
 * required information about an occurred exception.
 */
public interface ExceptionInfoProvider {
  /**
   * Method returns an ErrorCode object that identifies an occurred exception. The type of an exception can always be
   * identified by its error code.
   * 
   * @return ErrorCode ErrorCode object that identifies the occurred exception. The method never returns null.
   */
  ErrorCode getErrorCode( );

  /**
   * Method returns the parameters that are used to create a parameterized message that describes the occurred
   * exception.
   * 
   * @return String[] Array contains all parameter values for the parameterized message. The method may return null.
   */
  String[] getMessageParameters( );

  /**
   * Method returns an error message that describes the occurred exception. Therefore the passed message parameters are
   * used. The method always returns a message text that is localized using the current default locale.
   * 
   * @return String Error message describing the occurred exception. The method never returns null.
   */
  String getMessage( );

  /**
   * Method returns the Throwable object that caused the occurred exception.
   * 
   * @return Throwable Object that caused the occurred exception. The method may return null.
   */
  Throwable getCause( );

  /**
   * Method returns technical details about an exception. They might be helpful to solve the issue / root cause. This
   * information is not intended to be shown to users but will be added to log files for example.
   * 
   * @return {@link String} Technical details about the occurred exception. The method may return null.
   */
  String getTechnicalDetails( );

  /**
   * Method returns a date object representing the timestamp when the exception occurred.
   * 
   * @return {@link Date} Date object representing the timestamp when the problem occurred. The method never returns
   * null.
   */
  Date getTimestamp( );

  /**
   * Method returns information about the version of the software component which caused this exception.
   * 
   * @return VersionInfo Information about the version of the component that caused this exception. The method must not
   * return null.
   */
  VersionInfo getVersionInfo( );
}
