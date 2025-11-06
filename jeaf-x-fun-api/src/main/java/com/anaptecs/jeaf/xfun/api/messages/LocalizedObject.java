/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.messages;

import java.io.Serializable;
import java.util.Locale;

import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.errorhandling.SystemException;
import com.anaptecs.jeaf.xfun.bootstrap.Assert;

/**
 * This class is part of JEAF dynamic multi language support. This class is the base class for all localized objects.
 * Depending on the current locale settings the class' toString() method returns the appropriate localized string.
 */
public abstract class LocalizedObject implements Serializable {
  /**
   * Serial version uid for this class.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Localization ID of this localized object.
   */
  private final int localizationID;

  /**
   * Initialize created localized object. The class ensures that every LocalizedObject has a unique localization ID and
   * so for each localization ID only one object can be created.
   * 
   * @param pLocalizationID localization ID of the localized object that should be created. The parameter must be zero
   * or greater.
   * @throws SystemException if the passed localization ID was already used to create an LocalizedObject.
   */
  protected LocalizedObject( int pLocalizationID ) throws SystemException {
    // Check if pLocalizationID is a valid parameter.
    Assert.assertIsZeroOrGreater(pLocalizationID, "pLocalizationID");

    localizationID = pLocalizationID;
  }

  /**
   * Method returns the localization ID of this LocalizedObject.
   * 
   * @return int Localization ID of this object. The returned integer value is always greater or equal zero.
   */
  public int getLocalizationID( ) {
    return localizationID;
  }

  /**
   * Method overrides the equals method from java.lang.Object in a way as it is defined there. The method returns true
   * if the passed object is instance of this class and has the same localization IDs.
   * 
   * @param pObject Object that should be compared with this object.
   * @return boolean Method returns true if the passed object is not null, is a instance of class LocalizedObject and
   * has the same localization ID.
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public final boolean equals( Object pObject ) {
    // Check for null and class of the passed object
    boolean lIsEqual;
    if (pObject instanceof LocalizedObject) {
      // Objects have the same localization ID so they are equal.
      LocalizedObject lLocalizedObject = (LocalizedObject) pObject;
      if (localizationID == lLocalizedObject.localizationID) {
        lIsEqual = true;
      }
      // Objects do not have the same localization ID
      else {
        lIsEqual = false;
      }
    }
    // pObject is not the same localization ID.
    else {
      lIsEqual = false;
    }
    // Return result of comparison.
    return lIsEqual;
  }

  /**
   * Method returns the hash code of the object. Since the localization ID is used to compare localized objects for
   * equality this method returns the localization ID of the object to fulfill the contract of the hashCode() method as
   * it is defined in class java.lang.Object.
   * 
   * @return int The localization ID of this object is used as hash code.
   * 
   * @see java.lang.Object#hashCode()
   */
  public final int hashCode( ) {
    return this.getLocalizationID();
  }

  /**
   * Method returns a localized representation of this string. The method uses JEAF Locale Provider to determine the
   * language to use.
   * 
   * @return String Localized string that is represented by this object. The method never returns null.
   */
  public final String toString( ) {
    return this.toString((String[]) null);
  }

  /**
   * Method returns a localized representation of this string. The method uses JEAF Locale Provider to determine the
   * language to use.
   * 
   * @param pParams Dynamic parameters that should be used to build up the localized string. The parameter may be null.
   * @return String Localized string that is represented by this object. The method never returns null.
   */
  public String toString( String... pParams ) {
    return XFun.getMessageRepository().getMessage(this, pParams);
  }

  /**
   * Method returns a localized representation of this string. The method uses JEAF Locale Provider to determine the
   * language to use.
   * 
   * @param pLocale Locale in which the message should be returned. The parameter must not be null.
   * @return String Localized string that is represented by this object. The method never returns null.
   */
  public String toString( Locale pLocale ) {
    return this.toString(pLocale, (String[]) null);
  }

  /**
   * Method returns a localized representation of this string. The method uses JEAF Locale Provider to determine the
   * language to use.
   * 
   * @param pLocale Locale in which the message should be returned. The parameter must not be null.
   * @param pParams Dynamic parameters that should be used to build up the localized string. The parameter may be null.
   * @return String Localized string that is represented by this object. The method never returns null.
   */
  public String toString( Locale pLocale, String... pParams ) {
    return XFun.getMessageRepository().getMessage(this, pLocale, pParams);
  }

  /**
   * Method returns a representation of this string for tracing. In difference to <code>toString(...)</code> here the
   * configured tracing locale is used.
   * 
   * @return String string for tracing that is represented by this object. The method never returns null.
   */
  public String toTraceString( ) {
    return XFun.getMessageRepository().getTraceMessage(this);
  }
}