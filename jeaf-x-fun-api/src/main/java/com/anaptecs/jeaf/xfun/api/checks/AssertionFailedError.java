/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.checks;

/**
 * Whenever an assertion fails as reaction this error will be thrown.
 */
public class AssertionFailedError extends Error {
  /**
   * Generated serial version uid for this class.
   */
  private static final long serialVersionUID = 1;

  /**
   * Constant for prefix to which all messages that describe which assertion failed and why are appended. This means
   * that the error message for all AssertionFailedErrors start with a String that is equal to this constant.
   */
  private static final String ASSERTION_FAILED_MESSAGE = "Assertion failed.";

  /**
   * Initialize created error object with default message. This constructor should only be used in rare cases since the
   * thrown error does not provide any useful information about the cause.
   */
  public AssertionFailedError( ) {
    super(ASSERTION_FAILED_MESSAGE);
  }

  /**
   * Initialize created error object with the passed message.
   * 
   * @param pMessage Description of the assertion that failed and so caused this error object to be thrown. The
   * parameter may be null. In order to provide detailed and useful error information pMessage should always describe
   * which assertion failed and why.
   */
  public AssertionFailedError( String pMessage ) {
    super(ASSERTION_FAILED_MESSAGE + " " + pMessage);
  }
}