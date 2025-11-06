/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.errorhandling;

import com.anaptecs.jeaf.xfun.api.messages.MessageID;
import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;

/**
 * Error codes are used to identify an occurred error. An error code describes exactly one specific problem that led to
 * an exception or error. If two exceptions occurred due to the same reason they have the same error code.
 */
public final class ErrorCode extends MessageID {
  /**
   * Generated serial version uid for this class.
   * 
   * Since this version of the class (JEAF Release 1.2 and higher) is incompatible to its previous version from release
   * 1.1.x a serialization between them is not possible.
   */
  private static final long serialVersionUID = 2L;

  /**
   * Initialize ErrorCode object. Since the constructor is private the method <code>createErrorCode</code> has to be
   * used to create an new instance of this class.
   * 
   * @param pMessageID Message id that belongs to errors with this error code. The message id is used to compare two
   * ErrorCode objects for equality and to identify the error message that belongs to errors of this type.
   * @param pTraceLevel Level of the Message ID object that should be created. The TraceLevel can't be null
   */
  public ErrorCode( int pErrorCodeValue, TraceLevel pTraceLevel ) {
    super(pErrorCodeValue, pTraceLevel);
  }

  /**
   * Method return a integer value that describes this ErrorCode object. The returned value is equal to the passed
   * integer that was used to create this object.
   * 
   * @return int Integer value describing the error code.
   */
  public int getErrorCodeValue( ) {
    return this.getLocalizationID();
  }
}
