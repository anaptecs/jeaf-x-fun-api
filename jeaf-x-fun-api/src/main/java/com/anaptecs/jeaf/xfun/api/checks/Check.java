/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.checks;

import java.util.Collection;
import java.util.Date;

import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.errorhandling.FailureMessage;

/**
 * Class provides methods to check constraints that are defined by the contract that a method implements. There are two
 * types to check this contract: assert and check methods. While assert methods are used to check e.g. parameters that
 * are passed to internal methods, check methods should be used within public methods to ensure correct parameters. This
 * class provides check methods and therefore it is used for parameter checks in public methods. Assert methods are
 * provided through the <code>Assert</code> class.
 * 
 * @see com.anaptecs.jeaf.xfun.api.checks.Assert
 */
public final class Check {
  /**
   * Constructor of this class is private in order to ensure that no instances of this class can be created.
   */
  private Check( ) {
    // Nothing to do.
  }

  /**
   * Method checks whether the passed object is null or not.
   * 
   * <br/>
   * This method should only be used to check parameters that were passed to public methods since it provides a check
   * which is a mechanism to ensure that a caller complies with the contract of a public method. Assertions within
   * internal methods should use the assert method <code>Assert.assertNotNull(...)</code> instead.
   * 
   * @param pObject Parameter that was passed to a public method. If pObject is null an InvalidParameterException will
   * be thrown.
   * @param pParameterName Name of the parameter that is checked for null. The parameter may be null.
   * @throws InvalidParameterException if pObject is null.
   * 
   * @see Assert#assertNotNull(Object, String)
   */
  public static void checkInvalidParameterNull( Object pObject, String pParameterName ) {
    // InvalidParameterException is thrown if pObject is null.
    FailureMessage lFailureMessage = XFun.getVerifier().isNotNull(pObject, pParameterName);
    Check.handleCheckResult(lFailureMessage);
  }

  /**
   * Method checks whether the passed string is a real string. A String object is a "real" string whenever it is not
   * null and its trimmed version consist of at least one character.
   * 
   * <br/>
   * This method should only be used to check parameters that were passed to public methods since it provides a check
   * which is a mechanism to ensure that a caller complies with the contract of a public method. Assertions within
   * internal methods should use the assert method <code>Assert.assertIsRealString(...)</code> instead.
   * 
   * @param pString String object that should be checked if it is a real string. The parameter may be null. In this case
   * the check fails.
   * @param pStringName Name of the string that is checked. The parameter may be null.
   * @throws InvalidParameterException if pString is not a real string.
   * 
   * @see Assert#assertIsRealString(String, String)
   */
  public static void checkIsRealString( String pString, String pStringName ) {
    // InvalidParameterException is thrown if pObject is null.
    FailureMessage lFailureMessage = XFun.getVerifier().isRealString(pString, pStringName);
    Check.handleCheckResult(lFailureMessage);
  }

  /**
   * Method checks whether the passed string is not longer than the passed maximum length.
   * 
   * @param pString String object that should be checked for its length. The parameter must not be null.
   * @param pMaxLenght Maximum length the string may have.
   * @param pStringName Name of the string that is checked. The parameter may be null.
   * @throws InvalidParameterException if pString is not a real string.
   */
  public static void checkMaxStringLength( String pString, int pMaxLenght, String pStringName ) {
    // InvalidParameterException is thrown if pString is too long.
    FailureMessage lFailureMessage = XFun.getVerifier().verifyMaxStringLength(pString, pMaxLenght, pStringName);
    Check.handleCheckResult(lFailureMessage);
  }

  /**
   * Method checks whether the passed parameter is is zero or greater or not. If the check is negative an
   * InvalidParameterException is thrown.
   * 
   * <br/>
   * This method should only be used to check parameters that were passed to public methods since it provides a check
   * which is a mechanism to ensure that a caller complies with the contract of a public method. Assertions within
   * internal methods should use the assert method <code>Assert.assertZeroOrGreater(...)</code> instead.
   * 
   * @param pValue Value that should be check if it is zero or greater.
   * @param pParameterName Name of the parameter that is checked. The parameter may be null.
   * @throws InvalidParameterException if the parameter pValue is negative.
   * 
   * @see Assert#assertIsZeroOrGreater(int, String)
   */
  public static void checkIsZeroOrGreater( int pValue, String pParameterName ) {
    // InvalidParameterException is thrown if pValue is negative.
    FailureMessage lFailureMessage = XFun.getVerifier().isZeroOrGreater(pValue, pParameterName);
    Check.handleCheckResult(lFailureMessage);
  }

  /**
   * Method checks whether the passed parameters define a valid set. A set is valid if the lower bound is less or equal
   * to the upper bound. In all other cases the check fails.
   * 
   * <br/>
   * This method should only be used to check parameters that were passed to public methods since it provides a check
   * which is a mechanism to ensure that a caller complies with the contract of a public method. Assertions within
   * internal methods should use the assert method <code>Assert.assertIsValidSet(...)</code> instead.
   * 
   * @param pLowerBound Lower bound of the set.
   * @param pUpperBound Upper bound of the set.
   * @throws InvalidParameterException if the check fails.
   * 
   * @see Assert#assertIsValidSet(int, int)
   */
  public static void checkIsValidSet( int pLowerBound, int pUpperBound ) {
    // InvalidParameterException is thrown if pLowerBound and pUpperBound do not define a valid set.
    FailureMessage lFailureMessage = XFun.getVerifier().isValidSet(pLowerBound, pUpperBound);
    Check.handleCheckResult(lFailureMessage);
  }

  /**
   * Method checks whether the passed value is part of the passed set. The check fails if the passed value is not within
   * the passed set or if the lower and upper bound do not define a valid set.
   * 
   * <br/>
   * This method should only be used to check parameters that were passed to public methods since it provides a check
   * which is a mechanism to ensure that a caller complies with the contract of a public method. Assertions within
   * internal methods should use the assert method <code>Assert.assertIsPartOfSet(...)</code> instead.
   * 
   * @param pLowerBound Lower bound of the set. The parameter must define a valid set together with pUpperBound.
   * @param pUpperBound Upper bound of the set. The parameter must define a valid set together with pLowerBound.
   * @param pValue Value for which is checked if it is part of the defined set.
   * @throws InvalidParameterException if the check fails.
   * 
   * @see Assert#assertIsPartOfSet(int, int, int)
   */
  public static void checkIsPartOfSet( int pLowerBound, int pUpperBound, int pValue ) {
    // InvalidParameterException is thrown if pValue is not part of the defined set.
    FailureMessage lFailureMessage = XFun.getVerifier().isPartOfSet(pLowerBound, pUpperBound, pValue);
    Check.handleCheckResult(lFailureMessage);
  }

  /**
   * Method checks whether the passed value is not part of the passed set. The check fails if the passed value is within
   * the passed set or if the lower and upper bound do not define a valid set.
   * 
   * <br/>
   * This method should only be used to check parameters that were passed to public methods since it provides a check
   * which is a mechanism to ensure that a caller complies with the contract of a public method. Assertions within
   * internal methods should use the assert method <code>Assert.assertIsNotPartOfSet(...)</code> instead.
   * 
   * @param pLowerBound Lower bound of the set. The parameter must define a valid set together with pUpperBound.
   * @param pUpperBound Upper bound of the set. The parameter must define a valid set together with pLowerBound.
   * @param pValue Value for which is checked if it is not part of the defined set.
   * @throws InvalidParameterException if the check fails.
   * 
   * @see Assert#assertIsNotPartOfSet(int, int, int)
   */
  public static void checkIsNotPartOfSet( int pLowerBound, int pUpperBound, int pValue ) {
    // InvalidParameterException is thrown if pValue is part of the defined set.
    FailureMessage lFailureMessage = XFun.getVerifier().isNotPartOfSet(pLowerBound, pUpperBound, pValue);
    Check.handleCheckResult(lFailureMessage);
  }

  /**
   * Method checks whether the passed character sequence matches with the passed pattern.
   * 
   * @param pCharacters Character sequence to verify. The parameter must not be null.
   * @param pPattern Regular expression pattern which will be used to verify the passed character sequence. The
   * parameter must not be null.
   * @throws InvalidParameterException if the passed character sequence does not match to the passed pattern.
   */
  public static void checkPattern( String pCharacters, String pPattern ) {
    FailureMessage lFailureMessage = XFun.getVerifier().verifyPattern(pCharacters, pPattern);
    Check.handleCheckResult(lFailureMessage);
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
  public static void checkMinimumCollectionSize( Collection<?> pCollection, int pMinimumSize ) {
    FailureMessage lFailureMessage = XFun.getVerifier().verifyMinimumCollectionSize(pCollection, pMinimumSize);
    Check.handleCheckResult(lFailureMessage);
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
  public static void checkMaximumCollectionSize( Collection<?> pCollection, int pMinimumSize ) {
    FailureMessage lFailureMessage = XFun.getVerifier().verifyMaximumCollectionSize(pCollection, pMinimumSize);
    Check.handleCheckResult(lFailureMessage);
  }

  /**
   * Method verifies whether the passed start and end dates define a valid period. The verification is fulfilled if the
   * start date is before the end date.
   * 
   * @param pStart Start of the period. The parameter may be null.
   * @param pEnd End of the period. The parameter may be null.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all other cases the method returns a FailureMessage object.
   */
  public static void checkValidPeriod( Date pStart, Date pEnd ) {
    FailureMessage lFailureMessage = XFun.getVerifier().verifyValidPeriod(pStart, pEnd);
    Check.handleCheckResult(lFailureMessage);
  }

  /**
   * Method verifies whether the passed start and end dates define a valid period. The verification is fulfilled if the
   * start date is before the end date.
   * 
   * @param pStart Start of the period. The parameter may be null.
   * @param pEnd End of the period. The parameter may be null.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all other cases the method returns a FailureMessage object.
   */
  public static void checkValidEMailAddress( String pEMailAddress ) {
    FailureMessage lFailureMessage = XFun.getVerifier().verifyEMailAddress(pEMailAddress);
    Check.handleCheckResult(lFailureMessage);
  }

  /**
   * Method handles the result of a check. A check failed if the passed parameter is not null. In this case the method
   * will throw a new InvalidParameterException.
   * 
   * @param pVerificationFailure Result of a performed check. If the parameter is not null, this means that the check
   * failed. And as a reaction an exception will be thrown. If the parameter is null the method returns with no action.
   * @throws InvalidParameterException Exception that is thrown as a reaction on the failed parameter check.
   */
  private static void handleCheckResult( FailureMessage pVerificationFailure ) {
    if (pVerificationFailure != null) {
      // Throw exception.
      throw new InvalidParameterException(pVerificationFailure.getMessage());
    }
  }
}
