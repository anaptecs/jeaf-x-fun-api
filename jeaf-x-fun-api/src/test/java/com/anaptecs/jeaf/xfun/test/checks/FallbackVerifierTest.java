/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.List;

import com.anaptecs.jeaf.xfun.api.XFunRuntimeException;
import com.anaptecs.jeaf.xfun.api.checks.Verifier;
import com.anaptecs.jeaf.xfun.api.errorhandling.FailureMessage;
import com.anaptecs.jeaf.xfun.fallback.checks.FallbackVerifierImpl;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FallbackVerifierTest {
  private Verifier verifier = new FallbackVerifierImpl();

  @Test
  @Order(10)
  public void isNotNull( ) {
    // Good cases
    FailureMessage lResult = verifier.isNotNull("", "hello");
    assertNull(lResult, "Expected null in case that everything is ok.");
    lResult = verifier.isNotNull("", null);
    assertNull(lResult, "Expected null in case that everything is ok.");

    // Exception
    try {
      verifier.isNotNull(null, "param");
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'param' must not be null.", e.getMessage(), "Unexpected error message.");
    }

    try {
      verifier.isNotNull(null, null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'null' must not be null.", e.getMessage(), "Unexpected error message.");
    }
  }

  @Test
  @Order(20)
  public void isZeroOrGreater( ) {
    // Good cases
    assertNull(verifier.isZeroOrGreater(0, "pValue"));
    assertNull(verifier.isZeroOrGreater(0, null));
    assertNull(verifier.isZeroOrGreater(1, "pValue"));
    assertNull(verifier.isZeroOrGreater(1, null));
    assertNull(verifier.isZeroOrGreater(Integer.MAX_VALUE, "pValue"));
    assertNull(verifier.isZeroOrGreater(Integer.MAX_VALUE, null));

    // Error cases.
    try {
      verifier.isZeroOrGreater(-1, "pValue");
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pValue' must not be zero or greater.", e.getMessage());
    }
    try {
      verifier.isZeroOrGreater(-1, null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'null' must not be zero or greater.", e.getMessage());
    }
    try {
      verifier.isZeroOrGreater(Integer.MIN_VALUE, "pValue");
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pValue' must not be zero or greater.", e.getMessage());
    }
    try {
      verifier.isZeroOrGreater(Integer.MIN_VALUE, null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'null' must not be zero or greater.", e.getMessage());
    }
  }

  @Test
  @Order(30)
  public void isNull( ) {
    // Good cases
    assertNull(verifier.isNull(null, "pParam"));
    assertNull(verifier.isNull(null, null));

    // Error cases
    try {
      verifier.isNull("2", "pParam");
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pParam' must be null.", e.getMessage());
    }
    try {
      verifier.isNull(3, null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'null' must be null.", e.getMessage());
    }
  }

  @Test
  @Order(40)
  public void isTrue( ) {
    // Good cases
    assertNull(verifier.isTrue(true, "condition"));
    assertNull(verifier.isTrue(true, null));

    // Error cases.
    try {
      verifier.isTrue(false, "condition");
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'condition' must be true.", e.getMessage());
    }
    try {
      verifier.isTrue(false, null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'null' must be true.", e.getMessage());
    }
  }

  @Test
  @Order(50)
  public void isFalse( ) {
    // Good cases
    assertNull(verifier.isFalse(false, "condition"));
    assertNull(verifier.isFalse(false, null));

    // Error cases.
    try {
      verifier.isFalse(true, "condition");
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'condition' must be false.", e.getMessage());
    }
    try {
      verifier.isFalse(true, null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'null' must be false.", e.getMessage());
    }
  }

  @Test
  @Order(60)
  public void isRealString( ) {
    // Good cases
    assertNull(verifier.isRealString("abcs", "theString"));
    assertNull(verifier.isRealString("abcs", null));
    assertNull(verifier.isRealString("\nabcscdsvgadsgadr    qfeg   ", "theString"));

    // Error cases
    // Empty string
    try {
      verifier.isRealString("", null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'null' must be a real string.", e.getMessage());
    }
    // String with special characters only
    try {
      verifier.isRealString("\n\t", "theString");
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'theString' must be a real string.", e.getMessage());
    }
    // null
    try {
      verifier.isRealString(null, "theString");
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'theString' must be a real string. NULL is not a real string.", e.getMessage());
    }
  }

  @Test
  @Order(70)
  public void verifyMaxStringLength( ) {
    // Good cases
    String lString = "Hello World!";
    assertNull(verifier.verifyMaxStringLength(lString, lString.length(), "theString"));
    assertNull(verifier.verifyMaxStringLength(lString, lString.length() + 1, "theString"));
    assertNull(verifier.verifyMaxStringLength(lString, lString.length() + 1, null));
    assertNull(verifier.verifyMaxStringLength("", 0, null));

    // Error cases
    // String too long.
    try {
      verifier.verifyMaxStringLength(lString, lString.length() - 1, "theString");
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'theString' exceeds max string length of 11(value: Hello World!)", e.getMessage());
    }
    // negative length
    try {
      verifier.verifyMaxStringLength(lString, -1, "theString");
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pMaxLenght' must not be zero or greater.", e.getMessage());
    }

    // null
    try {
      verifier.verifyMaxStringLength(null, 1, "theString");
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pString' must not be null.", e.getMessage());
    }
  }

  @Test
  @Order(80)
  public void isValidSet( ) {
    // Good cases
    assertNull(verifier.isValidSet(0, 0));
    assertNull(verifier.isValidSet(0, 1));
    assertNull(verifier.isValidSet(4711, 4711));
    assertNull(verifier.isValidSet(0, 2));
    assertNull(verifier.isValidSet(4711, Integer.MAX_VALUE));
    assertNull(verifier.isValidSet(-13, 0));
    assertNull(verifier.isValidSet(Integer.MIN_VALUE, Integer.MAX_VALUE));

    // Error cases
    try {
      verifier.isValidSet(0, -1);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("0 and -1 do not define a valid set.", e.getMessage());
    }

    try {
      verifier.isValidSet(-10, -11);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("-10 and -11 do not define a valid set.", e.getMessage());
    }

    try {
      verifier.isValidSet(33, 20);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("33 and 20 do not define a valid set.", e.getMessage());
    }
  }

  @Test
  @Order(90)
  public void isPartOfSet( ) {
    // Good cases
    assertNull(verifier.isPartOfSet(0, 10, 9));
    assertNull(verifier.isPartOfSet(0, 10, 0));
    assertNull(verifier.isPartOfSet(0, 10, 10));
    assertNull(verifier.isPartOfSet(-10, 10, -10));
    assertNull(verifier.isPartOfSet(0, 0, 0));

    // Error cases
    try {
      verifier.isPartOfSet(-10, 0, 1);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("1 is not inside set [-10, 0]", e.getMessage());
    }
    try {
      verifier.isPartOfSet(-10, 0, -11);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("-11 is not inside set [-10, 0]", e.getMessage());
    }
  }

  @Test
  @Order(100)
  public void isNotPartOfSet( ) {
    // Good cases
    assertNull(verifier.isNotPartOfSet(0, 9, 10));
    assertNull(verifier.isNotPartOfSet(0, 9, -1));

    // Error cases
    try {
      verifier.isNotPartOfSet(0, 10, 10);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("10 is part of set [0, 10]", e.getMessage());
    }
    try {
      verifier.isNotPartOfSet(0, 10, 0);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("0 is part of set [0, 10]", e.getMessage());
    }
    try {
      verifier.isNotPartOfSet(0, -1, 0);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("0 and -1 do not define a valid set.", e.getMessage());
    }
  }

  @Test
  @Order(110)
  public void isSubset( ) {
    // Good cases
    assertNull(verifier.isSubset(0, 10, 1, 9));
    assertNull(verifier.isSubset(0, 10, 0, 10));
    assertNull(verifier.isSubset(-10, 10, -9, 9));

    // Error cases
    try {
      verifier.isSubset(0, 10, 0, 11);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("[0, 11] is not a subset of [0, 10]", e.getMessage());
    }
    try {
      verifier.isSubset(1, 10, 0, 10);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("[0, 10] is not a subset of [1, 10]", e.getMessage());
    }

    try {
      verifier.isSubset(1, 0, 0, 10);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("1 and 0 do not define a valid set.", e.getMessage());
    }
    try {
      verifier.isSubset(0, 10, 22, 10);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("22 and 10 do not define a valid set.", e.getMessage());
    }
  }

  @Test
  @Order(120)
  public void hasIntersection( ) {
    // Good cases
    assertNull(verifier.hasIntersection(0, 10, 1, 11));
    assertNull(verifier.hasIntersection(0, 10, 0, 10));
    assertNull(verifier.hasIntersection(0, 10, 10, 20));
    assertNull(verifier.hasIntersection(5, 10, 1, 20));

    // Error cases
    try {
      verifier.hasIntersection(0, 10, 11, 20);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("Sets [0, 10] and [11, 20] have no intersection.", e.getMessage());
    }
    try {
      verifier.hasIntersection(5, 10, 0, 4);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("Sets [5, 10] and [0, 4] have no intersection.", e.getMessage());
    }
    try {
      verifier.hasIntersection(0, 10, 22, 10);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("22 and 10 do not define a valid set.", e.getMessage());
    }
    try {
      verifier.hasIntersection(0, -10, 2, 10);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("0 and -10 do not define a valid set.", e.getMessage());
    }
  }

  @Test
  @Order(130)
  public void hasEmptyIntersection( ) {
    // Good cases
    assertNull(verifier.hasEmptyIntersection(0, 10, 11, 11));
    assertNull(verifier.hasEmptyIntersection(0, 10, 11, 20));
    assertNull(verifier.hasEmptyIntersection(5, 10, 0, 4));

    // Error cases
    try {
      verifier.hasEmptyIntersection(5, 10, 0, 5);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("Sets [5, 10] and [0, 5] have intersection.", e.getMessage());
    }

    try {
      verifier.hasEmptyIntersection(5, 10, 10, 11);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("Sets [5, 10] and [10, 11] have intersection.", e.getMessage());
    }
    try {
      verifier.hasEmptyIntersection(0, 10, 22, 10);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("22 and 10 do not define a valid set.", e.getMessage());
    }
    try {
      verifier.hasEmptyIntersection(0, -10, 2, 10);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("0 and -10 do not define a valid set.", e.getMessage());
    }
  }

  @Test
  @Order(140)
  public void verifyPattern( ) {
    try {
      verifier.verifyPattern(null, null);
      fail("Exception expected.");
    }
    catch (XFunRuntimeException e) {
      assertEquals("Operation 'verifyPattern(...)' not supported by fallback implementation", e.getMessage());
    }
  }

  @Test
  @Order(150)
  public void verifyMinimumCollectionSize( ) {
    // Good cases
    List<String> lStrings = Arrays.asList(new String[] { "A", "B", "C", "D" });
    assertNull(verifier.verifyMinimumCollectionSize(lStrings, 0));
    assertNull(verifier.verifyMinimumCollectionSize(lStrings, lStrings.size()));

    // Error cases.
    try {
      verifier.verifyMinimumCollectionSize(lStrings, lStrings.size() + 1);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("Collection is below its minimum size 5", e.getMessage());
    }
    try {
      verifier.verifyMinimumCollectionSize(lStrings, -1);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pMinimumSize' must not be zero or greater.", e.getMessage());
    }
    try {
      verifier.verifyMinimumCollectionSize(null, 2);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pCollection' must not be null.", e.getMessage());
    }
  }

  @Test
  @Order(160)
  public void verifyMaximumCollectionSize( ) {
    // Good cases
    List<String> lStrings = Arrays.asList(new String[] { "A", "B", "C", "D" });
    assertNull(verifier.verifyMaximumCollectionSize(lStrings, lStrings.size()));
    assertNull(verifier.verifyMaximumCollectionSize(lStrings, lStrings.size() + 1));

    // Error cases.
    try {
      verifier.verifyMaximumCollectionSize(lStrings, lStrings.size() - 1);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("Collection exceeds it maximum size 3", e.getMessage());
    }
    try {
      verifier.verifyMaximumCollectionSize(lStrings, -1);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pMaximumSize' must not be zero or greater.", e.getMessage());
    }
    try {
      verifier.verifyMaximumCollectionSize(null, 2);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pCollection' must not be null.", e.getMessage());
    }
  }

  @Test
  @Order(170)
  public void verifyValidPeriod( ) {
    try {
      verifier.verifyValidPeriod(null, null);
      fail("Exception expected.");
    }
    catch (XFunRuntimeException e) {
      assertEquals("Operation 'verifyValidPeriod(...)' not supported by fallback implementation", e.getMessage());
    }
  }

  @Test
  @Order(180)
  public void verifyEMailAddress( ) {
    try {
      verifier.verifyEMailAddress(null);
      fail("Exception expected.");
    }
    catch (XFunRuntimeException e) {
      assertEquals("Operation 'verifyEMailAddress(...)' not supported by fallback implementation", e.getMessage());
    }
  }
}
