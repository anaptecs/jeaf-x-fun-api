/**
 * Copyright 2004 - 2021 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.common;

import com.anaptecs.jeaf.xfun.api.common.AbstractObjectID;
import com.anaptecs.jeaf.xfun.api.common.ObjectIdentity;

public class MyObjectID extends AbstractObjectID<MyObjectID> {
  private static final long serialVersionUID = 1L;

  MyObjectID( ObjectIdentity<?> pObjectID ) {
    super(pObjectID);
  }

  MyObjectID( String pObjectID, Integer pVersionLabel ) {
    super(pObjectID, pVersionLabel);
  }

  @Override
  public MyObjectID getUnversionedObjectID( ) {
    return new MyObjectID(this.getObjectID(), null);
  }

  @Override
  public boolean hasDelimiter( ) {
    return true;
  }

  @Override
  public char getDelimiter( ) {
    return '?';
  }
}
