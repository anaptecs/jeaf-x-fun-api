/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */

package com.anaptecs.jeaf.xfun.api.common;

import java.io.Serializable;

import com.anaptecs.jeaf.xfun.api.checks.Check;

/**
 * Class implements a base class for object identities. Intention of this class is to simplify their implementation.
 * 
 * @author JEAF Development Team
 * @version 1.0
 */
public abstract class AbstractObjectID<T extends AbstractObjectID<?>> implements ObjectIdentity<T> {
  /**
   * Constant for delimiter that is used in normalized representation of an object ID.
   */
  protected static final char NORMALIZED_DELIMITER = '#';

  /**
   * Default serial version uid. Whenever this class changes in a way that it will be incompatible to Java's
   * serialization mechanism special implementation of method <code>readObject(...)</code> is required.
   * 
   * @see Serializable
   */
  private static final long serialVersionUID = 1L;

  /**
   * Object id value that is used to identify an object. The attribute is never null.
   */
  private final String objectID;

  /**
   * Version label of the object ID. The version label will be used to determine optimistic lock conflicts. In case of
   * unversioned object IDs the version label is <code>null</code>.
   */
  private final Integer versionLabel;

  /**
   * Normalized ID is used in order to compare multiple representations of an object ID. As we want to reduce the
   * overall memory consumption by object IDs the normalized representation is only created when needed.
   */
  private transient String normalizedID;

  /**
   * Initialize object. Therefore the value of the ID has to be passed to the constructor.
   * 
   * @param pObjectID Value which can be used to identify a service object. The parameter must not be null.
   * @param pVersionLabel Version label of the service object. The parameter may be null.
   */
  protected AbstractObjectID( String pObjectID, Integer pVersionLabel ) {
    // Check parameter for null.
    Check.checkInvalidParameterNull(pObjectID, "pObjectID");

    // Assign object id value.
    objectID = pObjectID;
    versionLabel = pVersionLabel;
  }

  /**
   * Initialize object. Therefore the values of passed object ID are taken. If the this object ID and the passed one use
   * different delimiters then the id will be modified accordingly.
   * 
   * @param pObjectID Object id that is used to create a new one. The parameter must not be null.
   */
  protected AbstractObjectID( ObjectIdentity<?> pObjectID ) {
    // Check parameter for null.
    Check.checkInvalidParameterNull(pObjectID, "pObjectID");

    // Assign object id value.
    // This ID and the passed one make use of delimiters
    if (this.hasDelimiter() && pObjectID.hasDelimiter()) {
      if (this.getDelimiter() != pObjectID.getDelimiter()) {
        objectID = pObjectID.getObjectID().replace(pObjectID.getDelimiter(), this.getDelimiter());
      }
      else {
        objectID = pObjectID.getObjectID();
      }
    }
    else {
      objectID = pObjectID.getObjectID();
    }
    // Also copy version label.
    versionLabel = pObjectID.getVersionLabel();
  }

  /**
   * Method returns a normalized object ID representation of this object. If the normalized ID was not already
   * calculated then it will be generated. This method is intentionally not synchronized as there are only very rare
   * conditions when it will be called from more then one thread in parallel and in this case we can live with creating
   * a normalized object ID twice.
   * 
   * @return {@link String} Normalized representation of this object ID.
   */
  private String getNormalizedID( ) {
    // Check if normalized Id was already calculated.
    String lNormalizedObjectID;
    if (normalizedID == null) {
      // We need to create a new normalized ID
      if (this.hasDelimiter()) {
        // In order to avoid unnecessary object we ensure that we really need to create a new object.
        char lDelimiter = this.getDelimiter();
        if (lDelimiter != NORMALIZED_DELIMITER) {
          lNormalizedObjectID = this.getObjectID().replace(lDelimiter, NORMALIZED_DELIMITER);
        }
        // Delimiter for the default representation and the normalized representation are the same so we can keep the
        // existing id.
        else {
          lNormalizedObjectID = this.getObjectID();
        }
      }
      else {
        lNormalizedObjectID = this.getObjectID();
      }
      // Cache normalized ID.
      normalizedID = lNormalizedObjectID;
    }
    // Normalized ID was already calculated
    else {
      lNormalizedObjectID = normalizedID;
    }
    // Return normalized representation of object ID.
    return lNormalizedObjectID;
  }

  /**
   * Method returns the value that can be used to identify a object.
   * 
   * @return String Object id value as String. The method never returns null.
   */
  @Override
  public final String getObjectID( ) {
    return objectID;
  }

  /**
   * Method returns the version label of the object. The version label will be used to determine optimistic lock
   * conflicts.
   * 
   * @return Version label of the object. The method may return null.
   */
  @Override
  public final Integer getVersionLabel( ) {
    return versionLabel;
  }

  /**
   * Method checks if this object id is versioned.
   * 
   * @return boolean Method returns true if this object ID is versioned and false otherwise.
   */
  @Override
  public final boolean isVersioned( ) {
    return versionLabel != null;
  }

  /**
   * Method checks if this object id is unversioned.
   * 
   * @return boolean Method returns true if this object ID is unversioned and false otherwise.
   */
  @Override
  public final boolean isUnversioned( ) {
    return versionLabel == null;
  }

  /**
   * Method compares this object with the passed object for equality. Two object IDs are equal in the following cases:
   * <ul>
   * <li>Both objects are instance of the same class and their <code>objectID</code> is equal.</li>
   * <li>Both objects are subclasses of class {@link AbstractObjectID} and their normalized object ID is equal (@see
   * {@link AbstractObjectID#getNormalizedID()}</li>
   * </ul>
   * 
   * <b>Note: </b>This method does not care about the version label ({@link #versionLabel}). The version label is only
   * used to determine lock conflicts.
   * 
   * @param pObject Object that should be compared with this instance. The parameter may be null. In this case the
   * method returns false.
   * @return boolean The method returns true if and only if the passed object and this object are equal according to the
   * list above and false in all other cases.
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public final boolean equals( Object pObject ) {
    boolean lIsEqual;
    // Check for same instance.
    if (this == pObject) {
      lIsEqual = true;
    }
    else if (pObject == null) {
      lIsEqual = false;
    }
    // Compare object id values if both objects are instance of the same class
    else if (this.getClass().equals(pObject.getClass())) {
      AbstractObjectID<?> lObjectID = (AbstractObjectID<?>) pObject;
      lIsEqual = objectID.equals(lObjectID.getObjectID());
    }
    // Both objects are instance of class AbstractObjectID but not the same implementation.
    else if (pObject instanceof AbstractObjectID) {
      // In this case we compare the object by its normalized id.
      AbstractObjectID<?> lObjectID = (AbstractObjectID<?>) pObject;
      lIsEqual = this.getNormalizedID().equals(lObjectID.getNormalizedID());
    }
    // Passed object is not an instance of same class as this instance.
    else {
      lIsEqual = false;
    }
    // Return result of comparison.
    return lIsEqual;
  }

  /**
   * Method returns the hash code of this object as it is defined by the Java Language Specification.
   * 
   * @return int Hash code of this object.
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public final int hashCode( ) {
    return this.getNormalizedID().hashCode();
  }

  /**
   * Method returns a string representation of this object. This is just the object id value.
   * 
   * @return String Object id value as string. The method never returns null.
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public final String toString( ) {
    return objectID;
  }
}
