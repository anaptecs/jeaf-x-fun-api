/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.XFunMessages;
import com.anaptecs.jeaf.xfun.api.checks.AssertionFailedError;
import com.anaptecs.jeaf.xfun.api.checks.VerificationResult;
import com.anaptecs.jeaf.xfun.api.errorhandling.ExceptionInfoProvider;
import com.anaptecs.jeaf.xfun.api.errorhandling.FailureMessage;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFSystemException;
import com.anaptecs.jeaf.xfun.api.info.OperatingSystem;
import com.anaptecs.jeaf.xfun.api.messages.MessageID;
import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class XFunChecksTest {
  @Test
  @Order(10)
  public void testVerificationResult( ) {
    // Error messages
    MessageID lErrorMessageID = new MessageID(4710, TraceLevel.ERROR);
    FailureMessage lErrorMessage = new FailureMessage(lErrorMessageID);
    MessageID lErrorMessageID2 = new MessageID(4711, TraceLevel.ERROR);
    FailureMessage lErrorMessage2 = new FailureMessage(lErrorMessageID2);

    // Warning messages
    MessageID lWarningMessageID = new MessageID(4712, TraceLevel.WARN);
    FailureMessage lWarningMessage = new FailureMessage(lWarningMessageID);
    MessageID lWarningMessageID2 = new MessageID(4713, TraceLevel.WARN);
    FailureMessage lWarningMessage2 = new FailureMessage(lWarningMessageID2);
    MessageID lWarningMessageID3 = new MessageID(4714, TraceLevel.WARN);
    FailureMessage lWarningMessage3 = new FailureMessage(lWarningMessageID3);

    // Check empty verification result
    VerificationResult lVerificationResult = new VerificationResult();
    assertFalse(lVerificationResult.containsErrors());
    assertFalse(lVerificationResult.containsWarnings());
    assertFalse(lVerificationResult.containsFailures());
    assertEquals(Integer.toString(XFunMessages.VERIFICATION_SUCCESSFUL.getLocalizationID()),
        lVerificationResult.getMessage());
    assertNotNull(lVerificationResult.getErrors());
    assertNotNull(lVerificationResult.getWarnings());

    // Check empty verification result
    lVerificationResult = new VerificationResult(null);
    assertFalse(lVerificationResult.containsErrors());
    assertFalse(lVerificationResult.containsWarnings());
    assertFalse(lVerificationResult.containsFailures());
    assertEquals(Integer.toString(XFunMessages.VERIFICATION_SUCCESSFUL.getLocalizationID()),
        lVerificationResult.getMessage());
    assertNotNull(lVerificationResult.getErrors());
    assertNotNull(lVerificationResult.getWarnings());

    // Ensure that list of errors and warning can not be modified directly
    try {
      lVerificationResult.getErrors().add(lErrorMessage);
      fail("Exception expected");
    }
    catch (UnsupportedOperationException e) {
      // Nothing to do. Exception has to be thrown.
    }

    try {
      lVerificationResult.getWarnings().add(lErrorMessage);
      fail("Exception expected");
    }
    catch (UnsupportedOperationException e) {
      // Nothing to do. Exception has to be thrown.
    }

    // Try to add error and warning message after object was created.
    lVerificationResult.addError(lErrorMessage);
    assertTrue(lVerificationResult.containsErrors());
    assertFalse(lVerificationResult.containsWarnings());
    assertTrue(lVerificationResult.containsFailures());
    lVerificationResult.addWarning(lWarningMessage);

    assertTrue(lVerificationResult.containsErrors());
    assertTrue(lVerificationResult.containsWarnings());
    assertTrue(lVerificationResult.containsFailures());
    OperatingSystem lOperatingSystem = XFun.getInfoProvider().getOperatingSystem();
    if (lOperatingSystem == OperatingSystem.WINDOWS) {
      assertEquals("29: 1 1 \r\n" + "- 4710 \r\n" + "- 4712", lVerificationResult.getMessage());
    }
    assertEquals(1, lVerificationResult.getErrors().size());
    assertEquals(1, lVerificationResult.getWarnings().size());
    assertTrue(lVerificationResult.getErrors().contains(lErrorMessage));
    assertTrue(lVerificationResult.getWarnings().contains(lWarningMessage));

    // Add another warning
    lVerificationResult.addWarning(lWarningMessage2);
    assertTrue(lVerificationResult.containsErrors());
    assertTrue(lVerificationResult.containsWarnings());
    assertTrue(lVerificationResult.containsFailures());
    if (lOperatingSystem == OperatingSystem.WINDOWS) {
      assertEquals("29: 1 2 \r\n" + "- 4710 \r\n" + "- 4712\r\n" + "- 4713", lVerificationResult.getMessage());
    }
    assertEquals(1, lVerificationResult.getErrors().size());
    assertEquals(2, lVerificationResult.getWarnings().size());
    assertTrue(lVerificationResult.getErrors().contains(lErrorMessage));
    assertTrue(lVerificationResult.getWarnings().contains(lWarningMessage));
    assertTrue(lVerificationResult.getWarnings().contains(lWarningMessage2));

    // Add verification result to another verification result.
    VerificationResult lVerificationResult2 = new VerificationResult(lErrorMessage2, lWarningMessage3);
    assertTrue(lVerificationResult2.containsErrors());
    assertTrue(lVerificationResult2.containsWarnings());
    assertTrue(lVerificationResult2.containsFailures());

    // Add verification result.
    lVerificationResult.addVerificationResult(lVerificationResult2);
    assertTrue(lVerificationResult.containsErrors());
    assertTrue(lVerificationResult.containsWarnings());
    assertTrue(lVerificationResult.containsFailures());
    if (lOperatingSystem == OperatingSystem.WINDOWS) {
      assertEquals("29: 2 3 \r\n" + "- 4710\r\n" + "- 4711 \r\n" + "- 4712\r\n" + "- 4713\r\n" + "- 4714",
          lVerificationResult.getMessage());
    }
    assertEquals(2, lVerificationResult.getErrors().size());
    assertEquals(3, lVerificationResult.getWarnings().size());
    assertTrue(lVerificationResult.getErrors().contains(lErrorMessage));
    assertTrue(lVerificationResult.getErrors().contains(lErrorMessage2));
    assertTrue(lVerificationResult.getWarnings().contains(lWarningMessage));
    assertTrue(lVerificationResult.getWarnings().contains(lWarningMessage2));
    assertTrue(lVerificationResult.getWarnings().contains(lWarningMessage3));

    lVerificationResult.addVerificationResult(null);

    // Add null as warning and error
    lVerificationResult.addError((FailureMessage) null);
    lVerificationResult.addWarning((FailureMessage) null);
    lVerificationResult.addError((ExceptionInfoProvider) null);
    lVerificationResult.addWarning((ExceptionInfoProvider) null);

    VerificationResult lVerificationResult3 = new VerificationResult(null, null);
    assertFalse(lVerificationResult3.containsErrors());
    assertFalse(lVerificationResult3.containsWarnings());
    assertFalse(lVerificationResult3.containsFailures());

    lVerificationResult3 = new VerificationResult(lErrorMessage, null);
    assertTrue(lVerificationResult3.containsErrors());
    assertFalse(lVerificationResult3.containsWarnings());
    assertTrue(lVerificationResult3.containsFailures());

    lVerificationResult3 = new VerificationResult(lErrorMessage);
    assertTrue(lVerificationResult3.containsErrors());
    assertFalse(lVerificationResult3.containsWarnings());
    assertTrue(lVerificationResult3.containsFailures());

    lVerificationResult3 = new VerificationResult(null, lWarningMessage);
    assertFalse(lVerificationResult3.containsErrors());
    assertTrue(lVerificationResult3.containsWarnings());
    assertTrue(lVerificationResult3.containsFailures());

    // Also test usage of JEAF exceptions as basis for failure messages.
    ExceptionInfoProvider lException = new JEAFSystemException(XFunMessages.INTERNAL_ERROR, "Hello", "World!");
    lVerificationResult = new VerificationResult();
    lVerificationResult.addError(lException);
    assertTrue(lVerificationResult.containsErrors());
    assertTrue(lVerificationResult.containsFailures());
    assertFalse(lVerificationResult.containsWarnings());
    List<FailureMessage> lErrors = lVerificationResult.getErrors();
    assertEquals(1, lErrors.size());
    FailureMessage lFailureMessage = lErrors.get(0);
    assertEquals(XFunMessages.INTERNAL_ERROR, lFailureMessage.getMessageID());
    assertEquals("Hello", lFailureMessage.getMessageParameters()[0]);
    assertEquals("World!", lFailureMessage.getMessageParameters()[1]);

    lVerificationResult = new VerificationResult();
    lVerificationResult.addError(XFunMessages.INTERNAL_ERROR, "Hello", "World!");
    assertTrue(lVerificationResult.containsErrors());
    assertTrue(lVerificationResult.containsFailures());
    assertFalse(lVerificationResult.containsWarnings());
    lErrors = lVerificationResult.getErrors();
    assertEquals(1, lErrors.size());
    lFailureMessage = lErrors.get(0);
    assertEquals(XFunMessages.INTERNAL_ERROR, lFailureMessage.getMessageID());
    assertEquals("Hello", lFailureMessage.getMessageParameters()[0]);
    assertEquals("World!", lFailureMessage.getMessageParameters()[1]);

    lVerificationResult = new VerificationResult();
    lVerificationResult.addWarning(lException);
    assertFalse(lVerificationResult.containsErrors());
    assertTrue(lVerificationResult.containsFailures());
    assertTrue(lVerificationResult.containsWarnings());
    List<FailureMessage> lWarnings = lVerificationResult.getWarnings();
    assertEquals(1, lWarnings.size());
    lFailureMessage = lWarnings.get(0);
    assertEquals(XFunMessages.INTERNAL_ERROR, lFailureMessage.getMessageID());
    assertEquals("Hello", lFailureMessage.getMessageParameters()[0]);
    assertEquals("World!", lFailureMessage.getMessageParameters()[1]);

    lVerificationResult = new VerificationResult();
    lVerificationResult.addWarning(XFunMessages.INTERNAL_ERROR, "Hello", "World!");
    assertFalse(lVerificationResult.containsErrors());
    assertTrue(lVerificationResult.containsFailures());
    assertTrue(lVerificationResult.containsWarnings());
    lWarnings = lVerificationResult.getWarnings();
    assertEquals(1, lWarnings.size());
    lFailureMessage = lWarnings.get(0);
    assertEquals(XFunMessages.INTERNAL_ERROR, lFailureMessage.getMessageID());
    assertEquals("Hello", lFailureMessage.getMessageParameters()[0]);
    assertEquals("World!", lFailureMessage.getMessageParameters()[1]);

  }

  @Test
  @Order(20)
  public void testAssertionFailedError( ) {
    AssertionFailedError lError = new AssertionFailedError();
    assertEquals("Assertion failed.", lError.getMessage());

    lError = new AssertionFailedError("This is the message.");
    assertEquals("Assertion failed. This is the message.", lError.getMessage());

    lError = new AssertionFailedError(null);
    assertEquals("Assertion failed. null", lError.getMessage());
  }
}
