/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.List;

import com.anaptecs.jeaf.xfun.api.checks.Assert;
import com.anaptecs.jeaf.xfun.api.checks.AssertionFailedError;
import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AssertTest {

  @Test
  @Order(10)
  public void testAssertNotNull( ) {
    // Good case
    Assert.assertNotNull("", "hello");
    Assert.assertNotNull("", null);

    // Error case
    try {
      Assert.assertNotNull(null, "hello");
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'hello' must not be null.", e.getMessage());
    }
    try {
      Assert.assertNotNull(null, null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'null' must not be null.", e.getMessage());
    }
  }

  @Test
  @Order(20)
  public void testAssertNull( ) {
    // Good case
    Assert.assertNull(null, "hello");
    Assert.assertNull(null, null);

    // Error case
    try {
      Assert.assertNull("abc", "hello");
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'hello' must be null.", e.getMessage());
    }
    try {
      Assert.assertNull(new Object(), null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'null' must be null.", e.getMessage());
    }
  }

  @Test
  @Order(30)
  public void testAssertTrue( ) {
    // Good case
    Assert.assertTrue(true, "true");
    Assert.assertTrue(true, null);

    // Error cases
    try {
      Assert.assertTrue(false, "param");
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'param' must be true.", e.getMessage());
    }
    try {
      Assert.assertTrue(false, null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'null' must be true.", e.getMessage());
    }
  }

  @Test
  @Order(40)
  public void testAssertFalse( ) {
    // Good case
    Assert.assertFalse(false, "false");
    Assert.assertFalse(false, null);

    // Error cases
    try {
      Assert.assertFalse(true, "param");
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'param' must be false.", e.getMessage());
    }
    try {
      Assert.assertFalse(true, null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'null' must be false.", e.getMessage());
    }
  }

  @Test
  @Order(50)
  public void testAssertIsRealString( ) {
    // Good cases
    Assert.assertIsRealString("abcs", "theString");
    Assert.assertIsRealString("abcs", null);
    Assert.assertIsRealString("\nabcscdsvgadsgadr    qfeg   ", "theString");

    // Error cases
    // Empty string
    try {
      Assert.assertIsRealString("", null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'null' must be a real string.", e.getMessage());
    }
    // String with special characters only
    try {
      Assert.assertIsRealString("\n\t", "theString");
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'theString' must be a real string.", e.getMessage());
    }
    // null
    try {
      Assert.assertIsRealString(null, "theString");
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'theString' must be a real string. NULL is not a real string.", e.getMessage());
    }
  }

  @Test
  @Order(60)
  public void testAssertIsZeroOrGreater( ) {
    // Good cases
    Assert.assertIsZeroOrGreater(0, "pValue");
    Assert.assertIsZeroOrGreater(0, null);
    Assert.assertIsZeroOrGreater(1, "pValue");
    Assert.assertIsZeroOrGreater(1, null);
    Assert.assertIsZeroOrGreater(Integer.MAX_VALUE, "pValue");
    Assert.assertIsZeroOrGreater(Integer.MAX_VALUE, null);

    // Error cases.
    try {
      Assert.assertIsZeroOrGreater(-1, "pValue");
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pValue' must not be zero or greater.", e.getMessage());
    }
    try {
      Assert.assertIsZeroOrGreater(-1, null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'null' must not be zero or greater.", e.getMessage());
    }
    try {
      Assert.assertIsZeroOrGreater(Integer.MIN_VALUE, "pValue");
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pValue' must not be zero or greater.", e.getMessage());
    }
    try {
      Assert.assertIsZeroOrGreater(Integer.MIN_VALUE, null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'null' must not be zero or greater.", e.getMessage());
    }
  }

  @Test
  @Order(70)
  public void testAssertIsValidSet( ) {
    // Good cases
    Assert.assertIsValidSet(0, 0);
    Assert.assertIsValidSet(0, 1);
    Assert.assertIsValidSet(4711, 4711);
    Assert.assertIsValidSet(0, 2);
    Assert.assertIsValidSet(4711, Integer.MAX_VALUE);
    Assert.assertIsValidSet(-13, 0);
    Assert.assertIsValidSet(Integer.MIN_VALUE, Integer.MAX_VALUE);

    // Error cases
    try {
      Assert.assertIsValidSet(0, -1);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("0 and -1 do not define a valid set.", e.getMessage());
    }

    try {
      Assert.assertIsValidSet(-10, -11);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("-10 and -11 do not define a valid set.", e.getMessage());
    }

    try {
      Assert.assertIsValidSet(33, 20);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("33 and 20 do not define a valid set.", e.getMessage());
    }
  }

  @Test
  @Order(80)
  public void testAssertIsPartOfSet( ) {
    // Good cases
    Assert.assertIsPartOfSet(0, 10, 9);
    Assert.assertIsPartOfSet(0, 10, 0);
    Assert.assertIsPartOfSet(0, 10, 10);
    Assert.assertIsPartOfSet(-10, 10, -10);
    Assert.assertIsPartOfSet(0, 0, 0);

    // Error cases
    try {
      Assert.assertIsPartOfSet(-10, 0, 1);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("1 is not inside set [-10, 0]", e.getMessage());
    }
    try {
      Assert.assertIsPartOfSet(-10, 0, -11);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("-11 is not inside set [-10, 0]", e.getMessage());
    }
  }

  @Test
  @Order(90)
  public void testAssertIsNotPartOfSet( ) {
    // Good cases
    Assert.assertIsNotPartOfSet(0, 9, 10);
    Assert.assertIsNotPartOfSet(0, 9, -1);

    // Error cases
    try {
      Assert.assertIsNotPartOfSet(0, 10, 10);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("10 is part of set [0, 10]", e.getMessage());
    }
    try {
      Assert.assertIsNotPartOfSet(0, 10, 0);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("0 is part of set [0, 10]", e.getMessage());
    }
    try {
      Assert.assertIsNotPartOfSet(0, -1, 0);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("0 and -1 do not define a valid set.", e.getMessage());
    }
  }

  @Test
  @Order(100)
  public void testAssertMinimumCollectionSize( ) {
    // Good cases
    List<String> lStrings = Arrays.asList(new String[] { "A", "B", "C", "D" });
    Assert.assertMinimumCollectionSize(lStrings, 0);
    Assert.assertMinimumCollectionSize(lStrings, lStrings.size());

    // Error cases.
    try {
      Assert.assertMinimumCollectionSize(lStrings, lStrings.size() + 1);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("Collection is below its minimum size 5", e.getMessage());
    }
    try {
      Assert.assertMinimumCollectionSize(lStrings, -1);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pMinimumSize' must not be zero or greater.", e.getMessage());
    }
    try {
      Assert.assertMinimumCollectionSize(null, 2);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pCollection' must not be null.", e.getMessage());
    }
  }

  @Test
  @Order(110)
  public void testAssertMaximumCollectionSize( ) {
    // Good cases
    List<String> lStrings = Arrays.asList(new String[] { "A", "B", "C", "D" });
    Assert.assertMaximumCollectionSize(lStrings, lStrings.size());
    Assert.assertMaximumCollectionSize(lStrings, lStrings.size() + 1);

    // Error cases.
    try {
      Assert.assertMaximumCollectionSize(lStrings, lStrings.size() - 1);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("Collection exceeds it maximum size 3", e.getMessage());
    }
    try {
      Assert.assertMaximumCollectionSize(lStrings, -1);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pMaximumSize' must not be zero or greater.", e.getMessage());
    }
    try {
      Assert.assertMaximumCollectionSize(null, 2);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pCollection' must not be null.", e.getMessage());
    }
  }

  @Test
  @Order(120)
  public void internalError( ) {
    try {
      Assert.internalError("Error-Message");
      fail("Assertion expected.");
    }
    catch (AssertionFailedError e) {
      assertEquals("Assertion failed. 43: Error-Message", e.getMessage());
    }

    try {
      Assert.internalError(null);
      fail("Assertion expected.");
    }
    catch (AssertionFailedError e) {
      assertEquals("Assertion failed. 43: null", e.getMessage());
    }

    try {
      Assert.internalError("Error-Message", new NullPointerException());
      fail("Assertion expected.");
    }
    catch (AssertionFailedError e) {
      assertEquals("Assertion failed. 43: Error-Message", e.getMessage());
    }

    try {
      Assert.internalError(null, new NullPointerException());
      fail("Assertion expected.");
    }
    catch (AssertionFailedError e) {
      assertEquals("Assertion failed. 43: null", e.getMessage());
    }

    try {
      Assert.internalError(null, null);
      fail("Assertion expected.");
    }
    catch (AssertionFailedError e) {
      assertEquals("Assertion failed. 43: null", e.getMessage());
    }
  }

  @Test
  @Order(130)
  public void unexpectedEnumLiteral( ) {
    try {
      Assert.unexpectedEnumLiteral(TraceLevel.ERROR);
      fail("Assertion expected.");
    }
    catch (AssertionFailedError e) {
      assertEquals("Assertion failed. 47: ERROR com.anaptecs.jeaf.xfun.api.trace.TraceLevel", e.getMessage());
    }
    try {
      Assert.unexpectedEnumLiteral(null);
      fail("Assertion expected.");
    }
    catch (AssertionFailedError e) {
      assertEquals("Assertion failed. 47: ? ?", e.getMessage());
    }

  }
}
