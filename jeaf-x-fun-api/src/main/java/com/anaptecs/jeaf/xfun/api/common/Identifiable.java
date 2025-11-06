/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.common;

/**
 * Interface is intended to be used by classes whose objects can be identified using an ID.
 * 
 * @author JEAF Development Team
 */
public interface Identifiable<T extends ObjectIdentity<?>> {
  /**
   * Method returns the ID of this object.
   * 
   * @return {@link T} ID of this object. Since identifiable objects do not have to have an ID in all cases the method
   * may also return null.
   */
  T getID( );

  /**
   * Method returns the unversioned ID of this object.
   * 
   * @return {@link T} ID of this object. Since identifiable objects do not have to have an ID in all cases the method
   * may also return null.
   */
  T getUnversionedID( );
}
