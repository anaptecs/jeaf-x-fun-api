/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.checks;

import java.util.Collection;

import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.XFunMessages;
import com.anaptecs.jeaf.xfun.api.errorhandling.FailureMessage;

/**
 * Class provides methods to ensure constraints that are defined by the contract that a method implements. There are two
 * types to ensure this contract: assert and check methods. While assert methods are used to check e.g. parameters that
 * are passed to internal methods, check methods should be used within public methods to ensure correct parameters. This
 * class provides assert methods and therefore it is used for internal checks. Check methods are provided through the
 * <code>Check</code> class.
 * 
 * @see com.anaptecs.jeaf.xfun.api.checks.Check
 */
public final class Assert {
  /**
   * Constructor of this class is private in order to ensure that no instances of this class can be created.
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
    // AssertionFailedError is thrown if pObject is null.
    FailureMessage lVerificationFailure = XFun.getVerifier().isNotNull(pObject, pObjectName);
    Assert.handleAssertionResult(lVerificationFailure);
  }

  /**
   * Method asserts whether that the passed object is null.
   * 
   * <br/>
   * This method should only be used within internal methods since it provides an assertion which is a mechanism to
   * ensure the internal functionality of a set of classes. Checks of parameters that are passed to public methods
   * should use the check methods in class <code>Check</code>.
   * 
   * @param pObject Object that should be checked for null. If pObject is not null an AssertionFailedException is
   * thrown.
   * @param pObjectName Name of the object that is checked for null. The parameter may be null.
   * @throws AssertionFailedError if pObject is not null.
   * 
   * @see Check
   */
  public static void assertNull( Object pObject, String pObjectName ) throws AssertionFailedError {
    // AssertionFailedError is thrown if pObject is not null.
    FailureMessage lVerificationFailure = XFun.getVerifier().isNull(pObject, pObjectName);
    Assert.handleAssertionResult(lVerificationFailure);
  }

  /**
   * Method asserts if the passed condition is true.
   * 
   * @param pCondition Condition that should be check if it is true.
   * @param pDescription Description of the condition. The parameter may be null.
   * @throws AssertionFailedError if the condition is not true.
   */
  public static void assertTrue( boolean pCondition, String pDescription ) throws AssertionFailedError {
    // AssertionFailedError is thrown if pObject is not null.
    FailureMessage lVerificationFailure = XFun.getVerifier().isTrue(pCondition, pDescription);
    Assert.handleAssertionResult(lVerificationFailure);
  }

  /**
   * Method asserts if the passed condition is false.
   * 
   * @param pCondition Condition that should be check if it is false.
   * @param pDescription Description of the condition. The parameter may be null.
   * @throws AssertionFailedError if the condition is not false.
   */
  public static void assertFalse( boolean pCondition, String pDescription ) throws AssertionFailedError {
    // AssertionFailedError is thrown if pObject is not null.
    FailureMessage lVerificationFailure = XFun.getVerifier().isFalse(pCondition, pDescription);
    Assert.handleAssertionResult(lVerificationFailure);
  }

  /**
   * Method asserts whether the passed string is a real string. A String object is a "real" string whenever it is not
   * null and its trimmed version consists of at least one character.
   * 
   * <br/>
   * This method should only be used within internal methods since it provides an assertion which is a mechanism to
   * ensure the internal functionality of a set of classes. Checks of parameters that are passed to public methods
   * should use the check method <code>Check.checkIsRealString(...)</code> instead.
   * 
   * @param pString String object that should be checked if it is a real string. The parameter may be null. In this case
   * the assertion fails.
   * @param pStringName Name of the string that is checked. The parameter may be null.
   * @throws AssertionFailedError if pString is not a real string.
   */
  public static void assertIsRealString( String pString, String pStringName ) throws AssertionFailedError {
    // InvalidParameterException is thrown if pObject is null.
    FailureMessage lVerificationFailure = XFun.getVerifier().isRealString(pString, pStringName);
    Assert.handleAssertionResult(lVerificationFailure);
  }

  /**
   * Method asserts whether the passed parameter is is zero or greater or not. If the assertion is negative an
   * AssertionFailedError is thrown.
   * 
   * <br/>
   * This method should only be used within internal methods since it provides an assertion which is a mechanism to
   * ensure the internal functionality of a set of classes. Checks of parameters that are passed to public methods
   * should use the check method <code>Check.checkZeroOrGreater(...)</code> instead.
   * 
   * @param pValue Value that should be check if it is zero or greater.
   * @param pParameterName Name of the value that is checked. The parameter may be null.
   * @throws AssertionFailedError if the parameter pValue is negative.
   * 
   * @see Check#checkIsZeroOrGreater(int, String)
   */
  public static void assertIsZeroOrGreater( int pValue, String pParameterName ) throws AssertionFailedError {
    // AssertionFailedError is thrown if pValue is negative.
    FailureMessage lVerificationFailure = XFun.getVerifier().isZeroOrGreater(pValue, pParameterName);
    Assert.handleAssertionResult(lVerificationFailure);
  }

  /**
   * Method asserts whether the passed parameters define a valid set. A set is valid if the lower bound is less or equal
   * to the upper bound. In all other cases the assertion fails.
   * 
   * <br/>
   * This method should only be used within internal methods since it provides an assertion which is a mechanism to
   * ensure the internal functionality of a set of classes. Checks of parameters that are passed to public methods
   * should use the check method <code>Check.checkIsValidSet(...)</code> instead.
   * 
   * @param pLowerBound Lower bound of the set.
   * @param pUpperBound Upper bound of the set.
   * @throws AssertionFailedError if the assertion fails.
   * 
   * @see Check#checkIsValidSet(int, int)
   */
  public static void assertIsValidSet( int pLowerBound, int pUpperBound ) throws AssertionFailedError {
    // InvalidParameterException is thrown if pLowerBound and pUpperBound do not define a valid set.
    FailureMessage lVerificationFailure = XFun.getVerifier().isValidSet(pLowerBound, pUpperBound);
    Assert.handleAssertionResult(lVerificationFailure);
  }

  /**
   * Method assert whether the passed value is part of the passed set. The assertion fails if the passed value is not
   * within the passed set or if the lower and upper bound do not define a valid set.
   * 
   * <br/>
   * This method should only be used within internal methods since it provides an assertion which is a mechanism to
   * ensure the internal functionality of a set of classes. Checks of parameters that are passed to public methods
   * should use the check method <code>Check.checkIsPartOfSet(...)</code> instead.
   * 
   * @param pLowerBound Lower bound of the set. The parameter must define a valid set together with pUpperBound.
   * @param pUpperBound Upper bound of the set. The parameter must define a valid set together with pLowerBound.
   * @param pValue Value for which is asserted if it is part of the defined set.
   * @throws AssertionFailedError if the assertion fails.
   * 
   * @see Check#checkIsPartOfSet(int, int, int)
   */
  public static void assertIsPartOfSet( int pLowerBound, int pUpperBound, int pValue ) throws AssertionFailedError {
    // InvalidParameterException is thrown if pValue is not part of the defined set.
    FailureMessage lVerificationFailure = XFun.getVerifier().isPartOfSet(pLowerBound, pUpperBound, pValue);
    Assert.handleAssertionResult(lVerificationFailure);
  }

  /**
   * Method assert whether the passed value is not part of the passed set. The assertion fails if the passed value is
   * within the passed set or if the lower and upper bound do not define a valid set.
   * 
   * <br/>
   * This method should only be used within internal methods since it provides an assertion which is a mechanism to
   * ensure the internal functionality of a set of classes. Checks of parameters that are passed to public methods
   * should use the check method <code>Check.checkIsNotPartOfSet(...)</code> instead.
   * 
   * @param pLowerBound Lower bound of the set. The parameter must define a valid set together with pUpperBound.
   * @param pUpperBound Upper bound of the set. The parameter must define a valid set together with pLowerBound.
   * @param pValue Value for which is asserted if it is not part of the defined set.
   * @throws AssertionFailedError if the assertion fails.
   * 
   * @see Check#checkIsNotPartOfSet(int, int, int)
   */
  public static void assertIsNotPartOfSet( int pLowerBound, int pUpperBound, int pValue ) throws AssertionFailedError {
    // InvalidParameterException is thrown if pValue is part of the defined set.
    FailureMessage lVerificationFailure = XFun.getVerifier().isNotPartOfSet(pLowerBound, pUpperBound, pValue);
    Assert.handleAssertionResult(lVerificationFailure);
  }

  /**
   * Method checks whether the passed collection has the passed minimum size.
   * 
   * @param pCollection Collection that should be verified for its minimum size. The parameter must not be null.
   * @param pMinimumSize Minimum size the passed collection must have. The amount of elements within the collection must
   * be equal or greater than this value.
   * @return VerificationFailure Verification failure describing the reason why the verification failed. If no error
   * occurred the method returns null. In all other cases the method returns a VerificationFailure object.
   */
  public static void assertMinimumCollectionSize( Collection<?> pCollection, int pMinimumSize ) {
    FailureMessage lVerificationFailure = XFun.getVerifier().verifyMinimumCollectionSize(pCollection, pMinimumSize);
    Assert.handleAssertionResult(lVerificationFailure);
  }

  /**
   * Method checks whether the passed collection has the passed minimum size.
   * 
   * @param pCollection Collection that should be verified for its maximum size. The parameter must not be null.
   * @param pMaximumSize Maximum size the passed collection must have. The amount of elements within the collection must
   * be less or equal than this value.
   * @return VerificationFailure Verification failure describing the reason why the verification failed. If no error
   * occurred the method returns null. In all other cases the method returns a VerificationFailure object.
   */
  public static void assertMaximumCollectionSize( Collection<?> pCollection, int pMaximumSize ) {
    FailureMessage lVerificationFailure = XFun.getVerifier().verifyMaximumCollectionSize(pCollection, pMaximumSize);
    Assert.handleAssertionResult(lVerificationFailure);
  }

  /**
   * Method can be used to thrown an assertion due to an internal error. An internal error may be something like an
   * unexpected subclass or any other internal error alike. The method will always throw an AssertionFailedError.
   * 
   * @param pMessage Message describing the internal error. The parameter may be null.
   */
  public static void internalError( String pMessage ) {
    String[] lParams = new String[] { pMessage };
    FailureMessage lFailureMessage = new FailureMessage(XFunMessages.INTERNAL_ERROR, lParams, null);
    Assert.handleAssertionResult(lFailureMessage);
  }

  /**
   * Method can be used to thrown an assertion due to an internal error. An internal error may be something like an
   * unexpected subclass or any other internal error alike. The method will always throw an AssertionFailedError.
   * 
   * @param pMessage Message describing the internal error. The parameter may be null.
   * @param pCause Cause for the internal error. The parameter may be null.
   */
  public static void internalError( String pMessage, Throwable pCause ) {
    String[] lParams = new String[] { pMessage };
    FailureMessage lFailureMessage = new FailureMessage(XFunMessages.INTERNAL_ERROR, lParams, pCause);
    Assert.handleAssertionResult(lFailureMessage);
  }

  /**
   * MEthod can be used to throw an assertion in order to indicate that an unexpected enumeration literal had to be
   * processed by some code.
   * 
   * @param pLiteral Unknown literal that could not be processed. The parameter must not be null.
   */
  public static void unexpectedEnumLiteral( Enum<?> pLiteral ) {
    String[] lParams;
    if (pLiteral != null) {
      lParams = new String[] { pLiteral.name(), pLiteral.getClass().getName() };
    }
    else {
      lParams = new String[] { "?", "?" };
    }
    FailureMessage lFailureMessage = new FailureMessage(XFunMessages.UNEXPECTED_ENUM_LITERAL, lParams);
    Assert.handleAssertionResult(lFailureMessage);
  }

  /**
   * Method handles the result of an assertion. An assertion failed if the passed parameter is not null. In this case
   * the method will throw a new AssertionFailedError.
   * 
   * @param pVerificationFailure Result of a performed assertion. If the parameter is not null, this means that the
   * assertion failed. And as a reaction an error will be thrown. If the parameter is null the method returns with no
   * action.
   * @throws AssertionFailedError Error that is thrown as a reaction on the failed assertion.
   */
  private static void handleAssertionResult( FailureMessage pVerificationFailure ) throws AssertionFailedError {
    // Check parameter for null.
    if (pVerificationFailure != null) {
      throw new AssertionFailedError(pVerificationFailure.getMessage());
    }
  }
}