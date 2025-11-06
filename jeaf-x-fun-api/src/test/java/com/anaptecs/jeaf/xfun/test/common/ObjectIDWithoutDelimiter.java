/**
 * Copyright 2004 - 2021 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.common;

import com.anaptecs.jeaf.xfun.api.common.AbstractObjectID;
import com.anaptecs.jeaf.xfun.api.common.ObjectIdentity;

public class ObjectIDWithoutDelimiter extends AbstractObjectID<ObjectIDWithoutDelimiter> {
  private static final long serialVersionUID = 1L;

  ObjectIDWithoutDelimiter( ObjectIdentity<?> pObjectID ) {
    super(pObjectID);
  }

  ObjectIDWithoutDelimiter( String pObjectID, Integer pVersionLabel ) {
    super(pObjectID, pVersionLabel);
  }

  @Override
  public ObjectIDWithoutDelimiter getUnversionedObjectID( ) {
    return new ObjectIDWithoutDelimiter(this.getObjectID(), null);
  }

  @Override
  public boolean hasDelimiter( ) {
    return false;
  }

  @Override
  public char getDelimiter( ) {
    return '+';
  }
}
