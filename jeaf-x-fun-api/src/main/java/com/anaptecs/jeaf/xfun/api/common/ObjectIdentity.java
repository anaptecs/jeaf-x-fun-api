/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */

package com.anaptecs.jeaf.xfun.api.common;

import java.io.Serializable;

/**
 * Class represents an identifier for an object. Using objects of this class a object can be identified.
 * 
 * In addition to the object ID itself this interface also defines an optional mechanism for version labels that is
 * intended to be used to determine optimistic lock conflicts. Please be aware that a version label mechanism is not
 * required to be provided by implementations.
 * 
 * Further more there are also cases for versioned object IDs where the version does not play any role. In this case
 * {@link #getUnversionedObjectID()} can be used.
 * 
 * @author JEAF Development Team
 * @version 1.0
 */
public interface ObjectIdentity<T extends ObjectIdentity<?>> extends Serializable {
  /**
   * Default serial version uid. Whenever this class changes in a way that it will be incompatible to Java's
   * serialization mechanism special implementation of method <code>readObject(...)</code> is required.
   * 
   * @see Serializable
   */
  static final long serialVersionUID = 1L;

  /**
   * Method returns a string representation of the object ID that can be used to identify an object.
   * 
   * @return String Object ID value as String. The method never returns null.
   */
  String getObjectID( );

  /**
   * Method returns the version label of the object ID. The version label will be used to determine optimistic lock
   * conflicts. Please be aware that a version label mechanism is not required to be provided by implementations.
   * 
   * Further more there are also cases for versioned object IDs where the version does not play any role. In this case
   * {@link #getUnversionedObjectID()} can be used.
   * 
   * @return Version label of the service object. The method may return null.
   */
  Integer getVersionLabel( );

  /**
   * Method checks if this object id is versioned.
   * 
   * @return boolean Method returns true if this object ID is versioned and false otherwise.
   */
  boolean isVersioned( );

  /**
   * Method checks if this object id is unversioned.
   * 
   * @return boolean Method returns true if this object ID is unversioned and false otherwise.
   */
  boolean isUnversioned( );

  /**
   * Method returns whether the represented object id has a delimiter or not.
   * 
   * @return boolean Method returns true if the object id has a delimiter and otherwise false.
   */
  boolean hasDelimiter( );

  /**
   * Method returns the delimiter in case that the object id contains delimiters.
   * 
   * @return char Delimiter of the object id or null if the object id does not contain a delimiter.
   */
  char getDelimiter( );

  /**
   * Method returns a unversioned variant of this object id. In opposite to an version object id an unversioned one can
   * be stored as it does not contain any version information about an object.
   * 
   * @return {@link T} Unversioned variant of this object ID. The method never returns null. The method may return the
   * existing instance if it is already unversioned.
   */
  T getUnversionedObjectID( );
}
