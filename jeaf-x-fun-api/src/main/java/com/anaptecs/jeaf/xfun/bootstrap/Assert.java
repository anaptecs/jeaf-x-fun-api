/**
 * Copyright 2004 - 2021 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.bootstrap;

import com.anaptecs.jeaf.xfun.api.checks.AssertionFailedError;

/**
 * Class provides assertions that can already be used during initialization of JEAF X-Fun.
 * 
 * Most likely this class is only used by JEAF X-Fun implementations but not by regular application.
 * 
 * @author JEAF Development Team
 */
public class Assert {
  /**
   * Private constructor is only used to hide public default constructor.
   */
  private Assert( ) {
    // Nothing to do.
  }

  /**
   * Method asserts whether the passed object is null or not.
   * 
   * <br/>
   * This method should only be used within internal methods since it provides an assertion which is a mechanism to
   * ensure the internal functionality of a set of classes. Checks of parameters that are passed to public methods
   * should use the check method <code>Check.checkInvalidParameterNull(...)</code> instead.
   * 
   * @param pObject Object that should be checked for null. If pObject is null an AssertionFailedException is thrown.
   * @param pObjectName Name of the object that is checked for null. The parameter may be null.
   * @throws AssertionFailedError if pObject is null.
   * 
   * @see Check#checkInvalidParameterNull(Object, String)
   */
  public static void assertNotNull( Object pObject, String pObjectName ) throws AssertionFailedError {
    if (pObject == null) {
      throw new AssertionFailedError("'" + pObjectName + "' must not be null.");
    }
  }

  /**
   * Method can be used to throw an assertion in order to indicate that an unexpected enumeration literal had to be
   * processed by some code.
   * 
   * @param pLiteral Unknown literal that could not be processed. The parameter must not be null.
   */
  public static void unexpectedEnumLiteral( Enum<?> pLiteral ) {
    if (pLiteral != null) {
      throw new AssertionFailedError(
          "Unexpected enumeration literal '" + pLiteral + "' of enum '" + pLiteral.getClass().getName() + "'.");
    }
    else {
      throw new AssertionFailedError("Unexpected enumeration literal (no further details provided ;-( ).");
    }
  }

  public static void assertIsZeroOrGreater( int pValue, String pParameterName ) throws AssertionFailedError {
    if (pValue < 0) {
      throw new AssertionFailedError(pParameterName + " must be zero or greater. Current value: " + pValue);
    }
  }
}
