/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.anaptecs.jeaf.xfun.api.common.ComponentID;

public class ComponentIDTest {
  @SuppressWarnings("unlikely-arg-type")
  @Test
  public void testComponentID( ) {
    // Good cases
    ComponentID lComponentID = new ComponentID("X-Fun Test Component", "com.anaptecs.jeaf.xfun.test.common");
    assertEquals("X-Fun Test Component", lComponentID.getComponentName());
    assertEquals("com.anaptecs.jeaf.xfun.test.common", lComponentID.getBasePackage());
    assertEquals("com.anaptecs.jeaf.xfun.test.common.X-Fun Test Component", lComponentID.getFullyQualifiedName());
    assertEquals("X-Fun Test Component", lComponentID.toString());

    // Test equals and hashCode
    ComponentID lSameComponentID = new ComponentID("X-Fun Test Component", "com.anaptecs.jeaf.xfun.test.common");
    ComponentID lOtherComponentID = new ComponentID("X-Fun Other Test Component", "com.anaptecs.jeaf.xfun.test.common");
    ComponentID lVeryComponentID = new ComponentID("X-Fun Other Test Component", "com.anaptecs.jeaf.xfun.test.other");
    ComponentID lCloseComponentID = new ComponentID("X-Fun Test Component", "com.anaptecs.jeaf.xfun.test.other");

    assertEquals(lComponentID, lSameComponentID);
    assertFalse(lComponentID.equals("Hello"));
    assertFalse(lComponentID.equals(lOtherComponentID));
    assertFalse(lComponentID.equals(lVeryComponentID));
    assertFalse(lComponentID.equals(lCloseComponentID));
    assertFalse(lComponentID.equals(null));

    Set<ComponentID> lSet = new HashSet<>();
    lSet.add(lComponentID);
    assertFalse(lSet.contains("Hello"));
    assertFalse(lSet.contains(lOtherComponentID));
    assertFalse(lSet.contains(lVeryComponentID));
    assertFalse(lSet.contains(lCloseComponentID));
  }
}
