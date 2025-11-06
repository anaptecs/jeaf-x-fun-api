/**
 * Copyright 2004 - 2021 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.bootstrap;

/**
 * Class provides checks that can already be used during initialization of JEAF X-Fun.
 * 
 * Most likely this class is only used by JEAF X-Fun implementations but not by regular application.
 * 
 * @author JEAF Development Team
 */
public class Check {
  /**
   * Private constructor is only used to hide public default constructor.
   */
  private Check( ) {
    // Nothing to do.
  }

  /**
   * Method checks whether the passed object is null or not.
   * 
   * This method should only be used to check parameters that were passed to public methods since it provides a check
   * which is a mechanism to ensure that a caller complies with the contract of a public method. Assertions within
   * internal methods should use the assert method <code>Assert.assertNotNull(...)</code> instead.
   * 
   * @param pObject Parameter that was passed to a public method. If pObject is null an InvalidParameterException will
   * be thrown.
   * @param pParameterName Name of the parameter that is checked for null. The parameter may be null.
   * @throws IllegalArgumentException if pObject is null.
   * 
   * @see Assert#assertNotNull(Object, String)
   */
  public static void checkInvalidParameterNull( Object pParameter, String pParameterName ) {
    if (pParameter == null) {
      throw new IllegalArgumentException("'" + pParameterName + "' must not be null.");
    }
  }

}
