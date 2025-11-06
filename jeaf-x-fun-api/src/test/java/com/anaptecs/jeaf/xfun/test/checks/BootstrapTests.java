/**
 * Copyright 2004 - 2021 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.anaptecs.jeaf.xfun.api.checks.AssertionFailedError;
import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;
import com.anaptecs.jeaf.xfun.bootstrap.Assert;
import com.anaptecs.jeaf.xfun.bootstrap.Check;
import org.junit.jupiter.api.Test;

public class BootstrapTests {
  @Test
  public void testBootstrapCheck( ) {
    // Expecting nothing to happen
    Check.checkInvalidParameterNull("String", "pObject");
    Check.checkInvalidParameterNull("String", null);

    // Check exception handling
    try {
      Check.checkInvalidParameterNull(null, "pObject");
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      // Nothing to do.
    }
  }

  @Test
  public void testBootstrapAssert( ) {
    //
    // Test Assert.assertIsZeroOrGreater(...)
    //

    // Expecting nothing to happen
    Assert.assertIsZeroOrGreater(0, "pParameter");
    Assert.assertIsZeroOrGreater(1, "pParameter");
    Assert.assertIsZeroOrGreater(4711, "pParameter");

    Assert.assertIsZeroOrGreater(0, null);
    Assert.assertIsZeroOrGreater(1, null);
    Assert.assertIsZeroOrGreater(4711, null);

    // Test exception handling
    try {
      Assert.assertIsZeroOrGreater(-1, "pParameter");
      fail("Exception expected.");
    }
    catch (AssertionFailedError e) {
      // Nothing to do.
    }

    try {
      Assert.assertIsZeroOrGreater(-222, "pParameter");
      fail("Exception expected.");
    }
    catch (AssertionFailedError e) {
      // Nothing to do.
    }

    //
    // Test Assert.assertNotNull(...)
    //

    // Expecting nothing to happen
    Assert.assertNotNull("String", "pParameter");
    Assert.assertNotNull("String", null);

    // Test exception handling
    try {
      Assert.assertNotNull(null, "pParameter");
      fail("Exception expected.");
    }
    catch (AssertionFailedError e) {
      // Nothing to do.
    }
    try {
      Assert.assertNotNull(null, null);
      fail("Exception expected.");
    }
    catch (AssertionFailedError e) {
      // Nothing to do.
    }

    //
    //
    //

    // Expecting nothing to happen
    try {
      Assert.unexpectedEnumLiteral(null);
      fail("Exception expected.");
    }
    catch (AssertionFailedError e) {
      // Nothing to do.
    }

    try {
      Assert.unexpectedEnumLiteral(TraceLevel.FATAL);
      fail("Exception expected.");
    }
    catch (AssertionFailedError e) {
      assertEquals(
          "Assertion failed. Unexpected enumeration literal 'FATAL' of enum 'com.anaptecs.jeaf.xfun.api.trace.TraceLevel'.",
          e.getMessage());
    }

  }
}
