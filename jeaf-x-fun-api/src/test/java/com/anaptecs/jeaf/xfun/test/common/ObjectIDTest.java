/**
 * Copyright 2004 - 2021 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import com.anaptecs.jeaf.xfun.api.common.AbstractObjectID;
import com.anaptecs.jeaf.xfun.api.common.ObjectID;
import org.junit.jupiter.api.Test;

public class ObjectIDTest {
  @SuppressWarnings("unlikely-arg-type")
  @Test
  void testAbstractObjectID( ) {
    MyObjectID lObjectID = new MyObjectID("12345", null);
    assertEquals("12345", lObjectID.getObjectID());
    assertEquals(null, lObjectID.getVersionLabel());
    assertEquals("12345", lObjectID.toString());
    assertEquals(false, lObjectID.isVersioned());
    assertEquals(true, lObjectID.isUnversioned());

    lObjectID = new MyObjectID("987654?123", 4711);
    assertEquals("987654?123", lObjectID.getObjectID());
    assertEquals(4711, lObjectID.getVersionLabel());
    assertEquals(true, lObjectID.equals(lObjectID));
    assertEquals("987654?123", lObjectID.toString());
    assertEquals(true, lObjectID.isVersioned());
    assertEquals(false, lObjectID.isUnversioned());

    MyObjectID lObjectID2 = new MyObjectID(lObjectID);
    assertEquals("987654?123", lObjectID2.getObjectID());
    assertEquals(4711, lObjectID2.getVersionLabel());
    assertEquals(true, lObjectID.equals(lObjectID2));

    lObjectID2 = new MyObjectID("987654?666", 4);
    assertEquals("987654?666", lObjectID2.getObjectID());
    assertEquals(4, lObjectID2.getVersionLabel());
    assertEquals(false, lObjectID.equals(lObjectID2));

    OtherObjectID lOtherObjectID = new OtherObjectID(lObjectID);
    assertEquals("987654#123", lOtherObjectID.getObjectID());
    assertEquals(4711, lOtherObjectID.getVersionLabel());
    assertEquals(true, lObjectID.equals(lOtherObjectID));

    ObjectIDWithoutDelimiter lObjectIDWithoutDelimiter = new ObjectIDWithoutDelimiter(lObjectID);
    assertEquals("987654?123", lObjectIDWithoutDelimiter.getObjectID());
    assertEquals(4711, lObjectIDWithoutDelimiter.getVersionLabel());
    assertEquals(false, lObjectIDWithoutDelimiter.equals(lOtherObjectID));

    MyObjectID lObjectID4711 = new MyObjectID(lObjectIDWithoutDelimiter);
    assertEquals("987654?123", lObjectID4711.getObjectID());
    assertEquals(4711, lObjectID4711.getVersionLabel());
    assertEquals(true, lObjectID4711.equals(lOtherObjectID));
    assertEquals(false, lObjectID4711.equals(lObjectIDWithoutDelimiter));
    assertEquals(false, lObjectID4711.equals(new MyObjectID("12345", null)));

    assertEquals(false, lObjectID.equals("Hello"));
    assertEquals(false, lObjectID.equals(null));

    // Test equals() / hashCode() implementation.
    assertEquals(lObjectID.hashCode(), lOtherObjectID.hashCode());

    // Test behavior in hash tables.
    Set<AbstractObjectID<?>> lSet = new HashSet<>();
    assertEquals(true, lSet.add(lObjectID));
    assertEquals(false, lSet.add(lObjectID));
    assertEquals(false, lSet.add(lOtherObjectID));
  }

  @Test
  void testObjectID( ) {
    ObjectID lObjectID = new ObjectID("12345-0815", 4711);
    assertEquals("12345-0815", lObjectID.getObjectID());
    assertEquals(4711, lObjectID.getVersionLabel());
    ObjectID lUnversionedObjectID = lObjectID.getUnversionedObjectID();
    assertEquals("12345-0815", lUnversionedObjectID.getObjectID());
    assertEquals(null, lUnversionedObjectID.getVersionLabel());
    assertTrue(lUnversionedObjectID == lUnversionedObjectID.getUnversionedObjectID());

    OtherObjectID lOtherObjectID = new OtherObjectID("4455#0815", null);
    lObjectID = new ObjectID(lOtherObjectID);
    assertEquals("4455-0815", lObjectID.getObjectID());

    // Test ObjectID without version label
    lObjectID = new ObjectID("12345-0815");
    assertEquals("12345-0815", lObjectID.getObjectID());
    assertEquals(null, lObjectID.getVersionLabel());
    assertEquals(lUnversionedObjectID, lObjectID);
  }
}
