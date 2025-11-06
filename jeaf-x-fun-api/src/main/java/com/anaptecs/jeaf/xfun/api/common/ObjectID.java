/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */

package com.anaptecs.jeaf.xfun.api.common;

import java.io.Serializable;

/**
 * Class represents an identifier for an object. Using objects of this class an object can be identified.
 * 
 * @author JEAF Development Team
 * @version 1.0
 */
public final class ObjectID extends AbstractObjectID<ObjectID> {
  /**
   * Default serial version uid. Whenever this class changes in a way that it will be incompatible to Java's
   * serialization mechanism special implementation of method <code>readObject(...)</code> is required.
   * 
   * @see Serializable
   */
  private static final long serialVersionUID = 1L;

  /**
   * Initialize object. Therefore the value of the id has to be passed to the constructor. Using this constructor no
   * version label will be used.
   * 
   * @param pObjectID Value which can be used to identify a service object. The parameter must not be null.
   */
  public ObjectID( String pObjectID ) {
    super(pObjectID, null);
  }

  /**
   * Initialize object. Therefore the value of the id has to be passed to the constructor.
   * 
   * @param pObjectID Value which can be used to identify a service object. The parameter must not be null.
   * @param pVersionLabel Version label of the service object. The parameter may be null.
   */
  public ObjectID( String pObjectID, Integer pVersionLabel ) {
    super(pObjectID, pVersionLabel);
  }

  /**
   * Initialize with the passed object id.
   * 
   * @param pObjectID Abstract object id that is used to create a new one. The parameter must not be null.
   */
  public ObjectID( ObjectIdentity<?> pObjectID ) {
    super(pObjectID);
  }

  /**
   * Method returns whether the represented object id has a delimiter or not.
   * 
   * @return boolean Method returns true if the object id has a delimiter and otherwise false.
   */
  @Override
  public boolean hasDelimiter( ) {
    return true;
  }

  /**
   * Method returns the delimiter in case that the object id contains delimiters.
   * 
   * @return char Delimiter of the object id or null if the object id does not contain a delimiter.
   */
  @Override
  public char getDelimiter( ) {
    return '-';
  }

  /**
   * Method returns a unversioned variant of this object id. In opposite to an version object id an unversioned one can
   * be stored as it does not contain any version information about an object.
   * 
   * @return {@link ObjectID} Unversioned variant of this object ID. The method never returns null.
   */
  @Override
  public ObjectID getUnversionedObjectID( ) {
    ObjectID lUnversionedObjectID;
    if (this.isVersioned() == true) {
      lUnversionedObjectID = new ObjectID(this.getObjectID(), null);
    }
    else {
      lUnversionedObjectID = this;
    }
    return lUnversionedObjectID;
  }

}
