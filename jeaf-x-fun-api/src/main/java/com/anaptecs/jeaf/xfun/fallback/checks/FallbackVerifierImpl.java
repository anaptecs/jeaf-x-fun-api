/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.fallback.checks;

import java.util.Collection;
import java.util.Date;

import com.anaptecs.jeaf.xfun.api.XFunRuntimeException;
import com.anaptecs.jeaf.xfun.api.checks.Verifier;
import com.anaptecs.jeaf.xfun.api.errorhandling.FailureMessage;

/**
 * Class implements all checks that can be performed through assert and check methods. The implementation of these
 * verifications is extracted to this class in order to avoid implementing the same behavior twice. Due to this fact the
 * methods of this class only implement the verification but the reaction on an occurred problem has to be implemented
 * by the caller. To indicate that a check failed all methods return a not null object of class FailureMessage
 * containing an error message describing why the verification failed.
 * 
 * Besides the provided verification methods it is also possible to provide additional verifications by creating a
 * subclass of this class that offers additional verify methods.
 * 
 * @author JEAF Development Team
 * @version 1.0
 * 
 * @see com.anaptecs.jeaf.xfun.api.checks.Assert
 * @see com.anaptecs.jeaf.xfun.api.checks.Check
 */
public class FallbackVerifierImpl implements Verifier {
  /**
   * Initialize object. Thereby no actions are performed.
   */
  public FallbackVerifierImpl( ) {
    // Nothing to do.
  }

  /**
   * Method verifies whether the passed object is null or not.
   * 
   * @param pObject Object that should be checked for null. If pObject is null an error is indicated.
   * @param pObjectName Name of the object that is checked for null. The parameter may be null.
   * @return VerificationFailure Verification failure describing the reason why the verification failed. If no error
   * occurred the method returns null. In all error cases the method returns a VerificationFailure object.
   */
  @Override
  public final FailureMessage isNotNull( Object pObject, String pObjectName ) {
    // Check pObject for null.
    FailureMessage lVerificationFailure;
    if (pObject != null) {
      lVerificationFailure = null;
    }
    // An error message is returned if pObject is null.
    else {
      String lMessage = "'" + pObjectName + "' must not be null.";
      throw new IllegalArgumentException(lMessage);
    }
    // Return result of verification.
    return lVerificationFailure;
  }

  /**
   * Method verifies whether the passed parameter is is zero or greater or not.
   * 
   * @param pValue Value that should be check if it is zero or greater.
   * @param pValueName Name of the value that is checked. The parameter may be null.
   * @return VerificationFailure Verification failure describing the reason why the verification failed. If no error
   * occurred the method returns null. In all other cases the method returns a VerificationFailure object.
   */
  @Override
  public final FailureMessage isZeroOrGreater( int pValue, String pValueName ) {
    // Check if pValue is positive including zero.
    FailureMessage lVerificationFailure;
    if (pValue >= 0) {
      lVerificationFailure = null;
    }
    // An error message is returned if pValue is negative.
    else {
      String lMessage = "'" + pValueName + "' must not be zero or greater.";
      throw new IllegalArgumentException(lMessage);
    }
    // Return result of verification.
    return lVerificationFailure;
  }

  /**
   * Method verifies that the passed object is null.
   * 
   * @param pObject Object that should be checked for null. If pObject is not null an error is indicated.
   * @param pObjectName Name of the object that is checked for null. The parameter may be null.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all error cases the method returns a FailureMessage object.
   */
  @Override
  public final FailureMessage isNull( Object pObject, String pObjectName ) {
    // Check pObject for null.
    FailureMessage lFailureMessage;
    if (pObject == null) {
      lFailureMessage = null;
    }
    // An error message is returned if pObject is null.
    else {
      String lMessage = "'" + pObjectName + "' must be null.";
      throw new IllegalArgumentException(lMessage);
    }
    // Return result of verification.
    return lFailureMessage;
  }

  /**
   * Method verifies if the passed condition is true.
   * 
   * @param pCondition Condition that should be check if it is true.
   * @param pDescription Description of the condition. The parameter may be null.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all error cases the method returns a FailureMessage object.
   */
  @Override
  public final FailureMessage isTrue( boolean pCondition, String pDescription ) {
    // Check pObject for null.
    FailureMessage lFailureMessage;
    if (pCondition == true) {
      lFailureMessage = null;
    }
    // An error message is returned if pObject is null.
    else {
      String lMessage = "'" + pDescription + "' must be true.";
      throw new IllegalArgumentException(lMessage);
    }
    // Return result of verification.
    return lFailureMessage;
  }

  /**
   * Method verifies if the passed condition is false.
   * 
   * @param pCondition Condition that should be check if it is false.
   * @param pDescription Description of the condition. The parameter may be null.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all error cases the method returns a FailureMessage object.
   */
  @Override
  public final FailureMessage isFalse( boolean pCondition, String pDescription ) {
    // Check pObject for null.
    FailureMessage lFailureMessage;
    if (pCondition == false) {
      lFailureMessage = null;
    }
    // An error message is returned if pObject is null.
    else {
      String lMessage = "'" + pDescription + "' must be false.";
      throw new IllegalArgumentException(lMessage);
    }
    // Return result of verification.
    return lFailureMessage;
  }

  /**
   * Method verifies whether the passed string is a real string. A String object is a "real" string whenever it is not
   * null and its trimmed version consists of at least one character.
   * 
   * @param pString String object that should be checked if it is a real string. If pString is null an error is
   * indicated.
   * @param pStringName Name of the string that is checked. The parameter may be null.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all other cases the method returns a FailureMessage object.
   */
  @Override
  public final FailureMessage isRealString( String pString, String pStringName ) {
    // Do verification.
    FailureMessage lFailureMessage;
    if (pString != null) {
      // Trim string to see if it is empty.
      String lTrimmedString = pString.trim();
      if (lTrimmedString.length() == 0) {
        String lMessage = "'" + pStringName + "' must be a real string.";
        throw new IllegalArgumentException(lMessage);
      }
      else {
        lFailureMessage = null;
      }
    }
    // String is null.
    else {
      String lMessage = "'" + pStringName + "' must be a real string. NULL is not a real string.";
      throw new IllegalArgumentException(lMessage);
    }
    // Return result of verification.
    return lFailureMessage;
  }

  /**
   * Method verifies whether the passed string is not longer than the passed maximum length.
   * 
   * @param pString String object that should be checked for its length. The parameter must not be null.
   * @param pMaxLenght Maximum length the string may have.
   * @param pStringName Name of the string that is checked. The parameter may be null.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all other cases the method returns a FailureMessage object.
   */
  @Override
  public final FailureMessage verifyMaxStringLength( String pString, int pMaxLenght, String pStringName ) {
    // Check parameters.
    this.isNotNull(pString, "pString");
    this.isZeroOrGreater(pMaxLenght, "pMaxLenght");

    // Check length of string.
    if (pString.length() > pMaxLenght) {
      String lMessage = "'" + pStringName + "' exceeds max string length of " + pMaxLenght + "(value: " + pString + ")";
      throw new IllegalArgumentException(lMessage);
    }
    // Return result of verification.
    else {
      return null;
    }
  }

  /**
   * Method verifies whether the passed parameters define a valid set. A set is valid if the lower bound is less or
   * equal to the upper bound. In all other cases the verification fails.
   * 
   * @param pLowerBound Lower bound of the set.
   * @param pUpperBound Upper bound of the set.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all other cases the method returns a FailureMessage object.
   */
  @Override
  public final FailureMessage isValidSet( int pLowerBound, int pUpperBound ) {
    // Set is valid if lower bound is less or equal to upper bound.
    if (pLowerBound <= pUpperBound) {
      return null;
    }
    // Set is invalid.
    else {
      String lMessage = pLowerBound + " and " + pUpperBound + " do not define a valid set.";
      throw new IllegalArgumentException(lMessage);
    }
  }

  /**
   * Method verifies whether the passed value is part of the passed set. The verification fails if the passed value is
   * not within the passed set or if the lower and upper bound do not define a valid set. The upper and lower both both
   * belong to the set.
   * 
   * @param pLowerBound Lower bound of the set. The parameter must define a valid set together with pUpperBound.
   * @param pUpperBound Upper bound of the set. The parameter must define a valid set together with pLowerBound.
   * @param pValue Value for which is verified if it is part of the defined set.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all other cases the method returns a FailureMessage object.
   */
  @Override
  public final FailureMessage isPartOfSet( int pLowerBound, int pUpperBound, int pValue ) {
    // Check if lower and upper bound define a valid set.
    this.isValidSet(pLowerBound, pUpperBound);

    // Defined set is valid.
    FailureMessage lFailureMessage;
    // pValue must be greater or equal to the lower bound and less or equal to the upper bound.
    if (pLowerBound <= pValue && pValue <= pUpperBound) {
      lFailureMessage = null;
    }
    // pValue is not within the set.
    else {
      String lMessage = pValue + " is not inside set [" + pLowerBound + ", " + pUpperBound + "]";
      throw new IllegalArgumentException(lMessage);
    }
    // Return result of verification.
    return lFailureMessage;
  }

  /**
   * Method verifies whether the passed value is not part of the passed set. The verification fails if the passed value
   * is within the passed set or if the lower and upper bound do not define a valid set.
   * 
   * @param pLowerBound Lower bound of the set. The parameter must define a valid set together with pUpperBound.
   * @param pUpperBound Upper bound of the set. The parameter must define a valid set together with pLowerBound.
   * @param pValue Value for which is verified if it is not part of the defined set.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all other cases the method returns a FailureMessage object.
   */
  @Override
  public final FailureMessage isNotPartOfSet( int pLowerBound, int pUpperBound, int pValue ) {
    // Check if lower and upper bound define a valid set.
    this.isValidSet(pLowerBound, pUpperBound);

    // Defined set is valid.
    FailureMessage lFailureMessage;
    // pValue must be less than the lower bound or greater than the upper bound.
    if (pValue < pLowerBound || pValue > pUpperBound) {
      lFailureMessage = null;
    }
    // pValue is not within the set.
    else {
      String lMessage = pValue + " is part of set [" + pLowerBound + ", " + pUpperBound + "]";
      throw new IllegalArgumentException(lMessage);
    }
    // Return result of verification.
    return lFailureMessage;
  }

  /**
   * Method verifies whether the passed set is a subset. The verification fails if the passed values are not a subset of
   * the passed set or if the the passed values do not define valid sets.
   * 
   * @param pLowerBoundSet Lower bound of the set. The parameter must define a valid set together with pUpperBoundSet.
   * @param pUpperBoundSet Upper bound of the set. The parameter must define a valid set together with pLowerBoundSet.
   * @param pLowerBoundSubset Lower bound of the subset. The parameter must define a valid set together with
   * pUpperBoundSubset.
   * @param pUpperBoundSubset Upper bound of the subset. The parameter must define a valid set together with
   * pLowerBoundSubset.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all other cases the method returns a FailureMessage object.
   */
  @Override
  public final FailureMessage isSubset( int pLowerBoundSet, int pUpperBoundSet, int pLowerBoundSubset,
      int pUpperBoundSubset ) {
    // Check if the passed parameters define valid outer set.
    this.isValidSet(pLowerBoundSet, pUpperBoundSet);
    // Check if passed parameters define valid subset.
    this.isValidSet(pLowerBoundSubset, pUpperBoundSubset);

    // To be a subset of the outer set the lower and the upper bound must be part of the outer set. Since we already
    // know the both sets are valid it is sufficient to compare only the lower and upper bounds with each other.
    FailureMessage lFailureMessage;
    if (pLowerBoundSubset < pLowerBoundSet || pUpperBoundSubset > pUpperBoundSet) {
      String lMessage = "[" + pLowerBoundSubset + ", " + pUpperBoundSubset + "] is not a subset of [" + pLowerBoundSet
          + ", " + pUpperBoundSet + "]";
      throw new IllegalArgumentException(lMessage);
    }
    else {
      lFailureMessage = null;
    }
    // Return result of verification.
    return lFailureMessage;
  }

  /**
   * Method verifies whether the first and the second set have a not null intersection. The verification fails if the
   * two sets do not have a not null intersection or if one of the sets is not a valid set.
   * 
   * @param pLowerBoundFirstSet Lower bound of the first set. The parameter must define a valid set together with
   * pUpperBoundFirstSet.
   * @param pUpperBoundFirstSet Upper bound of the first set. The parameter must define a valid set together with
   * pLowerBoundFirstSet.
   * @param pLowerBoundSecondSet Lower bound of the second set. The parameter must define a valid set together with
   * pUpperBoundSecondSet.
   * @param pUpperBoundSecondSet Upper bound of the second set. The parameter must define a valid set together with
   * pLowerBoundSecondSet.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all other cases the method returns a FailureMessage object.
   */
  @Override
  public final FailureMessage hasIntersection( int pLowerBoundFirstSet, int pUpperBoundFirstSet,
      int pLowerBoundSecondSet, int pUpperBoundSecondSet ) {

    // Check if the parameters for the first set define a valid set.
    this.isValidSet(pLowerBoundFirstSet, pUpperBoundFirstSet);
    // Check if the parameters for the second set define a valid set.
    this.isValidSet(pLowerBoundSecondSet, pUpperBoundSecondSet);

    // Since we already know that both sets are valid we only have to check if the upper bound of the first set is
    // less than the lower bound of the second set and vice versa.
    FailureMessage lFailureMessage;
    if (pUpperBoundFirstSet < pLowerBoundSecondSet || pUpperBoundSecondSet < pLowerBoundFirstSet) {
      String lMessage = "Sets [" + pLowerBoundFirstSet + ", " + pUpperBoundFirstSet + "] and [" + pLowerBoundSecondSet
          + ", " + pUpperBoundSecondSet + "] have no intersection.";
      throw new IllegalArgumentException(lMessage);
    }
    else {
      lFailureMessage = null;
    }
    // Return result of verification.
    return lFailureMessage;
  }

  /**
   * Method verifies whether the first and the second set have a empty intersection. The verification fails if the two
   * sets do not have a empty intersection or if one of the sets is not a valid set.
   * 
   * @param pLowerBoundFirstSet Lower bound of the first set. The parameter must define a valid set together with
   * pUpperBoundFirstSet.
   * @param pUpperBoundFirstSet Upper bound of the first set. The parameter must define a valid set together with
   * pLowerBoundFirstSet.
   * @param pLowerBoundSecondSet Lower bound of the second set. The parameter must define a valid set together with
   * pUpperBoundSecondSet.
   * @param pUpperBoundSecondSet Upper bound of the second set. The parameter must define a valid set together with
   * pLowerBoundSecondSet.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all other cases the method returns a FailureMessage object.
   */
  @Override
  public final FailureMessage hasEmptyIntersection( int pLowerBoundFirstSet, int pUpperBoundFirstSet,
      int pLowerBoundSecondSet, int pUpperBoundSecondSet ) {

    // Check if the parameters for the first set define a valid set.
    this.isValidSet(pLowerBoundFirstSet, pUpperBoundFirstSet);
    // Check if the parameters for the second set define a valid set.
    this.isValidSet(pLowerBoundSecondSet, pUpperBoundSecondSet);

    // Since we already know that both sets are valid we only have to check for intersection
    FailureMessage lFailureMessage;
    if (pUpperBoundFirstSet < pLowerBoundSecondSet || pUpperBoundSecondSet < pLowerBoundFirstSet) {
      lFailureMessage = null;
    }
    else {
      String lMessage = "Sets [" + pLowerBoundFirstSet + ", " + pUpperBoundFirstSet + "] and [" + pLowerBoundSecondSet
          + ", " + pUpperBoundSecondSet + "] have intersection.";
      throw new IllegalArgumentException(lMessage);
    }

    // Return result of verification.
    return lFailureMessage;
  }

  /**
   * Method verifies whether the passed character sequence matches with the passed pattern.
   * 
   * @param pCharacters Character sequence to verify. The parameter must not be null.
   * @param pPattern Regular expression pattern which will be used to verify the passed character sequence. The
   * parameter must not be null.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all other cases the method returns a FailureMessage object.
   */
  @Override
  public final FailureMessage verifyPattern( String pCharacters, String pPattern ) {
    // Fallback verifier does not support checking of patterns.
    throw new XFunRuntimeException("Operation 'verifyPattern(...)' not supported by fallback implementation");
  }

  /**
   * Method verifies whether the passed collection has the passed minimum size.
   * 
   * @param pCollection Collection that should be verified for its minimum size. The parameter must not be null.
   * @param pMinimumSize Minimum size the passed collection must have. The amount of elements within the collection must
   * be equal or greater than this value.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all other cases the method returns a FailureMessage object.
   */
  @Override
  public final FailureMessage verifyMinimumCollectionSize( Collection<?> pCollection, int pMinimumSize ) {
    // Check parameters
    this.isNotNull(pCollection, "pCollection");
    this.isZeroOrGreater(pMinimumSize, "pMinimumSize");

    if (pCollection.size() >= pMinimumSize) {
      return null;
    }
    else {
      String lMessage = "Collection is below its minimum size " + pMinimumSize;
      throw new IllegalArgumentException(lMessage);
    }
  }

  /**
   * Method verifies whether the passed collection has the passed minimum size.
   * 
   * @param pCollection Collection that should be verified for its maximum size. The parameter must not be null.
   * @param pMaximumSize Maximum size the passed collection must have. The amount of elements within the collection must
   * be less or equal than this value.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all other cases the method returns a FailureMessage object.
   */
  @Override
  public final FailureMessage verifyMaximumCollectionSize( Collection<?> pCollection, int pMaximumSize ) {
    // Check parameters
    this.isNotNull(pCollection, "pCollection");
    this.isZeroOrGreater(pMaximumSize, "pMaximumSize");

    if (pCollection.size() <= pMaximumSize) {
      return null;
    }
    else {
      String lMessage = "Collection exceeds it maximum size " + pMaximumSize;
      throw new IllegalArgumentException(lMessage);
    }
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
  @Override
  public final FailureMessage verifyValidPeriod( Date pStart, Date pEnd ) {
    // Fallback verifier does not support checking of periods.
    throw new XFunRuntimeException("Operation 'verifyValidPeriod(...)' not supported by fallback implementation");
  }

  /**
   * Method verifies if the passed string represents a valid email address.
   * 
   * @param pEMailAddress Representation of the email address. The parameter may be null.
   * @return {@link FailureMessage} Verification failure describing the reason why the verification failed. If no error
   * occurred the method returns null. In all other cases the method returns a FailureMessage object.
   */
  @Override
  public final FailureMessage verifyEMailAddress( String pEMailAddress ) {
    // Fallback verifier does not support checking of e-mails.
    throw new XFunRuntimeException("Operation 'verifyEMailAddress(...)' not supported by fallback implementation");
  }
}
