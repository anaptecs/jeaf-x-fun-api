/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.checks;

import java.util.Collection;
import java.util.Date;

import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.errorhandling.FailureMessage;

/**
 * Interface defines all the methods that should be offered by verifier implementations. Verifiers provide check
 * implementations that are used for checks and assertions.
 * 
 * {@link Check} {@link Assert}
 */
public interface Verifier {
  /**
   * Method returns the configured verifier.
   * 
   * @return {@link Verifier} Verifier that is used. The method never returns null.
   */
  static Verifier getVerifier( ) {
    return XFun.getVerifier();
  }

  /**
   * Method verifies whether the passed object is null or not.
   * 
   * @param pObject Object that should be checked for null. If pObject is null an error is indicated.
   * @param pObjectName Name of the object that is checked for null. The parameter may be null.
   * @return VerificationFailure Verification failure describing the reason why the verification failed. If no error
   * occurred the method returns null. In all error cases the method returns a VerificationFailure object.
   */
  FailureMessage isNotNull( Object pObject, String pObjectName );

  /**
   * Method verifies whether the passed parameter is is zero or greater or not.
   * 
   * @param pValue Value that should be check if it is zero or greater.
   * @param pValueName Name of the value that is checked. The parameter may be null.
   * @return VerificationFailure Verification failure describing the reason why the verification failed. If no error
   * occurred the method returns null. In all other cases the method returns a VerificationFailure object.
   */
  FailureMessage isZeroOrGreater( int pValue, String pValueName );

  /**
   * Method verifies that the passed object is null.
   * 
   * @param pObject Object that should be checked for null. If pObject is not null an error is indicated.
   * @param pObjectName Name of the object that is checked for null. The parameter may be null.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all error cases the method returns a FailureMessage object.
   */
  FailureMessage isNull( Object pObject, String pObjectName );

  /**
   * Method verifies if the passed condition is true.
   * 
   * @param pCondition Condition that should be check if it is true.
   * @param pDescription Description of the condition. The parameter may be null.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all error cases the method returns a FailureMessage object.
   */
  FailureMessage isTrue( boolean pCondition, String pDescription );

  /**
   * Method verifies if the passed condition is false.
   * 
   * @param pCondition Condition that should be check if it is false.
   * @param pDescription Description of the condition. The parameter may be null.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all error cases the method returns a FailureMessage object.
   */
  FailureMessage isFalse( boolean pCondition, String pDescription );

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
  FailureMessage isRealString( String pString, String pStringName );

  /**
   * Method verifies whether the passed string is not longer than the passed maximum length.
   * 
   * @param pString String object that should be checked for its length. The parameter must not be null.
   * @param pMaxLenght Maximum length the string may have.
   * @param pStringName Name of the string that is checked. The parameter may be null.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all other cases the method returns a FailureMessage object.
   */
  FailureMessage verifyMaxStringLength( String pString, int pMaxLenght, String pStringName );

  /**
   * Method verifies whether the passed parameters define a valid set. A set is valid if the lower bound is less or
   * equal to the upper bound. In all other cases the verification fails.
   * 
   * @param pLowerBound Lower bound of the set.
   * @param pUpperBound Upper bound of the set.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all other cases the method returns a FailureMessage object.
   */
  FailureMessage isValidSet( int pLowerBound, int pUpperBound );

  /**
   * Method verifies whether the passed value is part of the passed set. The verification fails if the passed value is
   * not within the passed set or if the lower and upper bound do not define a valid set.
   * 
   * @param pLowerBound Lower bound of the set. The parameter must define a valid set together with pUpperBound.
   * @param pUpperBound Upper bound of the set. The parameter must define a valid set together with pLowerBound.
   * @param pValue Value for which is verified if it is part of the defined set.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all other cases the method returns a FailureMessage object.
   */
  FailureMessage isPartOfSet( int pLowerBound, int pUpperBound, int pValue );

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
  FailureMessage isNotPartOfSet( int pLowerBound, int pUpperBound, int pValue );

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
  FailureMessage isSubset( int pLowerBoundSet, int pUpperBoundSet, int pLowerBoundSubset, int pUpperBoundSubset );

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
  FailureMessage hasIntersection( int pLowerBoundFirstSet, int pUpperBoundFirstSet, int pLowerBoundSecondSet,
      int pUpperBoundSecondSet );

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
  FailureMessage hasEmptyIntersection( int pLowerBoundFirstSet, int pUpperBoundFirstSet, int pLowerBoundSecondSet,
      int pUpperBoundSecondSet );

  /**
   * Method verifies whether the passed character sequence matches with the passed pattern.
   * 
   * @param pCharacters Character sequence to verify. The parameter must not be null.
   * @param pPattern Regular expression pattern which will be used to verify the passed character sequence. The
   * parameter must not be null.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all other cases the method returns a FailureMessage object.
   */
  FailureMessage verifyPattern( String pCharacters, String pPattern );

  /**
   * Method verifies whether the passed collection has the passed minimum size.
   * 
   * @param pCollection Collection that should be verified for its minimum size. The parameter must not be null.
   * @param pMinimumSize Minimum size the passed collection must have. The amount of elements within the collection must
   * be equal or greater than this value.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all other cases the method returns a FailureMessage object.
   */
  FailureMessage verifyMinimumCollectionSize( Collection<?> pCollection, int pMinimumSize );

  /**
   * Method verifies whether the passed collection has the passed minimum size.
   * 
   * @param pCollection Collection that should be verified for its maximum size. The parameter must not be null.
   * @param pMaximumSize Maximum size the passed collection must have. The amount of elements within the collection must
   * be less or equal than this value.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all other cases the method returns a FailureMessage object.
   */
  FailureMessage verifyMaximumCollectionSize( Collection<?> pCollection, int pMaximumSize );

  /**
   * Method verifies whether the passed start and end dates define a valid period. The verification is fulfilled if the
   * start date is before the end date.
   * 
   * @param pStart Start of the period. The parameter may be null.
   * @param pEnd End of the period. The parameter may be null.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all other cases the method returns a FailureMessage object.
   */
  FailureMessage verifyValidPeriod( Date pStart, Date pEnd );

  /**
   * Method verifies if the passed string represents a valid email address.
   * 
   * @param pEMailAddress Representation of the email address. The parameter may be null.
   * @return {@link FailureMessage} Verification failure describing the reason why the verification failed. If no error
   * occurred the method returns null. In all other cases the method returns a FailureMessage object.
   */
  FailureMessage verifyEMailAddress( String pEMailAddress );

}