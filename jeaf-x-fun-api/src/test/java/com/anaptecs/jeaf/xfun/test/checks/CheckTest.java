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

import com.anaptecs.jeaf.xfun.api.XFunRuntimeException;
import com.anaptecs.jeaf.xfun.api.checks.Check;
import com.anaptecs.jeaf.xfun.api.checks.InvalidParameterException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CheckTest {

  @Test
  @Order(10)
  public void testAssertNotNull( ) {
    // Good case
    Check.checkInvalidParameterNull("", "hello");
    Check.checkInvalidParameterNull("", null);

    // Error case
    try {
      Check.checkInvalidParameterNull(null, "hello");
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'hello' must not be null.", e.getMessage());
    }
    try {
      Check.checkInvalidParameterNull(null, null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'null' must not be null.", e.getMessage());
    }
  }

  @Test
  @Order(20)
  public void testValidEMail( ) {
    try {
      Check.checkValidEMailAddress(null);
      fail("Exception expected.");
    }
    catch (XFunRuntimeException e) {
      assertEquals("Operation 'verifyEMailAddress(...)' not supported by fallback implementation", e.getMessage());
    }
  }

  @Test
  @Order(30)
  public void testValidPeriod( ) {
    try {
      Check.checkValidPeriod(null, null);
      fail("Exception expected.");
    }
    catch (XFunRuntimeException e) {
      assertEquals("Operation 'verifyValidPeriod(...)' not supported by fallback implementation", e.getMessage());
    }
  }

  @Test
  @Order(40)
  public void testMaxStringLength( ) {
    String lString = "Hello World!";
    Check.checkMaxStringLength(lString, lString.length(), "theString");
    Check.checkMaxStringLength(lString, lString.length() + 1, "theString");
    Check.checkMaxStringLength(lString, lString.length() + 1, null);
    Check.checkMaxStringLength("", 0, null);

    // Error cases
    // String too long.
    try {
      Check.checkMaxStringLength(lString, lString.length() - 1, "theString");
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'theString' exceeds max string length of 11(value: Hello World!)", e.getMessage());
    }
    // negative length
    try {
      Check.checkMaxStringLength(lString, -1, "theString");
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pMaxLenght' must not be zero or greater.", e.getMessage());
    }

    // null
    try {
      Check.checkMaxStringLength(null, 1, "theString");
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pString' must not be null.", e.getMessage());
    }
  }

  @Test
  @Order(50)
  public void testAssertIsRealString( ) {
    // Good cases
    Check.checkIsRealString("abcs", "theString");
    Check.checkIsRealString("abcs", null);
    Check.checkIsRealString("\nabcscdsvgadsgadr    qfeg   ", "theString");

    // Error cases
    // Empty string
    try {
      Check.checkIsRealString("", null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'null' must be a real string.", e.getMessage());
    }
    // String with special characters only
    try {
      Check.checkIsRealString("\n\t", "theString");
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'theString' must be a real string.", e.getMessage());
    }
    // null
    try {
      Check.checkIsRealString(null, "theString");
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
    Check.checkIsZeroOrGreater(0, "pValue");
    Check.checkIsZeroOrGreater(0, null);
    Check.checkIsZeroOrGreater(1, "pValue");
    Check.checkIsZeroOrGreater(1, null);
    Check.checkIsZeroOrGreater(Integer.MAX_VALUE, "pValue");
    Check.checkIsZeroOrGreater(Integer.MAX_VALUE, null);

    // Error cases.
    try {
      Check.checkIsZeroOrGreater(-1, "pValue");
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pValue' must not be zero or greater.", e.getMessage());
    }
    try {
      Check.checkIsZeroOrGreater(-1, null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'null' must not be zero or greater.", e.getMessage());
    }
    try {
      Check.checkIsZeroOrGreater(Integer.MIN_VALUE, "pValue");
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pValue' must not be zero or greater.", e.getMessage());
    }
    try {
      Check.checkIsZeroOrGreater(Integer.MIN_VALUE, null);
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
    Check.checkIsValidSet(0, 0);
    Check.checkIsValidSet(0, 1);
    Check.checkIsValidSet(4711, 4711);
    Check.checkIsValidSet(0, 2);
    Check.checkIsValidSet(4711, Integer.MAX_VALUE);
    Check.checkIsValidSet(-13, 0);
    Check.checkIsValidSet(Integer.MIN_VALUE, Integer.MAX_VALUE);

    // Error cases
    try {
      Check.checkIsValidSet(0, -1);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("0 and -1 do not define a valid set.", e.getMessage());
    }

    try {
      Check.checkIsValidSet(-10, -11);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("-10 and -11 do not define a valid set.", e.getMessage());
    }

    try {
      Check.checkIsValidSet(33, 20);
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
    Check.checkIsPartOfSet(0, 10, 9);
    Check.checkIsPartOfSet(0, 10, 0);
    Check.checkIsPartOfSet(0, 10, 10);
    Check.checkIsPartOfSet(-10, 10, -10);
    Check.checkIsPartOfSet(0, 0, 0);

    // Error cases
    try {
      Check.checkIsPartOfSet(-10, 0, 1);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("1 is not inside set [-10, 0]", e.getMessage());
    }
    try {
      Check.checkIsPartOfSet(-10, 0, -11);
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
    Check.checkIsNotPartOfSet(0, 9, 10);
    Check.checkIsNotPartOfSet(0, 9, -1);

    // Error cases
    try {
      Check.checkIsNotPartOfSet(0, 10, 10);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("10 is part of set [0, 10]", e.getMessage());
    }
    try {
      Check.checkIsNotPartOfSet(0, 10, 0);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("0 is part of set [0, 10]", e.getMessage());
    }
    try {
      Check.checkIsNotPartOfSet(0, -1, 0);
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
    Check.checkMinimumCollectionSize(lStrings, 0);
    Check.checkMinimumCollectionSize(lStrings, lStrings.size());

    // Error cases.
    try {
      Check.checkMinimumCollectionSize(lStrings, lStrings.size() + 1);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("Collection is below its minimum size 5", e.getMessage());
    }
    try {
      Check.checkMinimumCollectionSize(lStrings, -1);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pMinimumSize' must not be zero or greater.", e.getMessage());
    }
    try {
      Check.checkMinimumCollectionSize(null, 2);
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
    Check.checkMaximumCollectionSize(lStrings, lStrings.size());
    Check.checkMaximumCollectionSize(lStrings, lStrings.size() + 1);

    // Error cases.
    try {
      Check.checkMaximumCollectionSize(lStrings, lStrings.size() - 1);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("Collection exceeds it maximum size 3", e.getMessage());
    }
    try {
      Check.checkMaximumCollectionSize(lStrings, -1);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pMaximumSize' must not be zero or greater.", e.getMessage());
    }
    try {
      Check.checkMaximumCollectionSize(null, 2);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pCollection' must not be null.", e.getMessage());
    }
  }

  @Test
  @Order(120)
  public void testPatternCheck( ) {
    try {
      Check.checkPattern(null, null);
      fail("Exception expected.");
    }
    catch (XFunRuntimeException e) {
      assertEquals("Operation 'verifyPattern(...)' not supported by fallback implementation", e.getMessage());
    }
  }

  @Test
  @Order(130)
  public void testInvalidParameterException( ) {
    InvalidParameterException lException = new InvalidParameterException("Hello");
    assertEquals("Check failed. Hello", lException.getMessage());
  }
}
