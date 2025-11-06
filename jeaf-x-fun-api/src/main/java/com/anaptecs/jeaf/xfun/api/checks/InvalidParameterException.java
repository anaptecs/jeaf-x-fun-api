/**
 * Copyright 2004 - 2020 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.checks;

/**
 * Whenever a check of a parameter fails as reaction this exception is thrown.
 * 
 * @author JEAF Development Team
 * @version 1.0
 */
public final class InvalidParameterException extends IllegalArgumentException {
  /**
   * Generated serial version uid for this class.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Constant for prefix which will be appended to all messages that describe which check failed and why. This means
   * that the exception message for all InvalidParameterExceptions start with a String that is equal to this constant.
   */
  private static final String CHECK_FAILED_MESSAGE = "Check failed.";

  /**
   * Initialize created exception object with the passed message.
   * 
   * @param pMessage Description of the check that failed and so caused this exception object to be thrown. The
   * parameter may be null. In order to provide detailed and useful error information pMessage should always describe
   * which check failed and why.
   */
  public InvalidParameterException( String pMessage ) {
    super(CHECK_FAILED_MESSAGE + " " + pMessage);
  }
}