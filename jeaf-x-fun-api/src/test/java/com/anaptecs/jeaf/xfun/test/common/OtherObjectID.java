/**
 * Copyright 2004 - 2021 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.common;

import com.anaptecs.jeaf.xfun.api.common.AbstractObjectID;
import com.anaptecs.jeaf.xfun.api.common.ObjectIdentity;

public class OtherObjectID extends AbstractObjectID<OtherObjectID> {
  private static final long serialVersionUID = 1L;

  OtherObjectID( ObjectIdentity<?> pObjectID ) {
    super(pObjectID);
  }

  OtherObjectID( String pObjectID, Integer pVersionLabel ) {
    super(pObjectID, pVersionLabel);
  }

  @Override
  public OtherObjectID getUnversionedObjectID( ) {
    return new OtherObjectID(this.getObjectID(), null);
  }

  @Override
  public boolean hasDelimiter( ) {
    return true;
  }

  @Override
  public char getDelimiter( ) {
    return AbstractObjectID.NORMALIZED_DELIMITER;
  }
}
