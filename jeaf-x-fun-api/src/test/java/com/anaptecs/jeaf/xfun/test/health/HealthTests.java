/**
 * Copyright 2004 - 2021 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.health;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;

import com.anaptecs.jeaf.xfun.api.errorhandling.FailureMessage;
import com.anaptecs.jeaf.xfun.api.health.CheckLevel;
import com.anaptecs.jeaf.xfun.api.health.HealthCheckResult;
import com.anaptecs.jeaf.xfun.api.health.HealthStatus;
import com.anaptecs.jeaf.xfun.api.messages.MessageID;
import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;
import org.junit.jupiter.api.Test;

public class HealthTests {

  @Test
  public void testCheckLevel( ) {
    // Check all possible variant of check levels.
    assertEquals("INTERNAL_CHECKS_ONLY", CheckLevel.INTERNAL_CHECKS_ONLY.name());
    assertEquals(0, CheckLevel.INTERNAL_CHECKS_ONLY.ordinal());
    assertEquals("INFRASTRUCTURE_CHECKS", CheckLevel.INFRASTRUCTURE_CHECKS.name());
    assertEquals(1, CheckLevel.INFRASTRUCTURE_CHECKS.ordinal());
    assertEquals("EXTERNAL_CHECKS", CheckLevel.EXTERNAL_CHECKS.name());
    assertEquals(2, CheckLevel.EXTERNAL_CHECKS.ordinal());

    assertEquals(3, CheckLevel.values().length);

    assertEquals(CheckLevel.INTERNAL_CHECKS_ONLY, CheckLevel.getDefaultLevel());
  }

  @Test
  public void checkHealthStatus( ) {
    // Check all possible variant of health status.
    assertEquals("OK", HealthStatus.OK.name());
    assertEquals(0, HealthStatus.OK.ordinal());
    assertEquals("DISABLED", HealthStatus.DISABLED.name());
    assertEquals(1, HealthStatus.DISABLED.ordinal());
    assertEquals("WARNING", HealthStatus.WARNING.name());
    assertEquals(2, HealthStatus.WARNING.ordinal());
    assertEquals("ERROR", HealthStatus.ERROR.name());
    assertEquals(3, HealthStatus.ERROR.ordinal());
    assertEquals("UNKNOWN", HealthStatus.UNKNOWN.name());
    assertEquals(4, HealthStatus.UNKNOWN.ordinal());

    assertEquals(5, HealthStatus.values().length);

    assertEquals(true, HealthStatus.OK.isWorking());
    assertEquals(false, HealthStatus.OK.isOutOfService());
    assertEquals(true, HealthStatus.WARNING.isWorking());
    assertEquals(false, HealthStatus.WARNING.isOutOfService());

    assertEquals(false, HealthStatus.DISABLED.isWorking());
    assertEquals(true, HealthStatus.DISABLED.isOutOfService());
    assertEquals(false, HealthStatus.ERROR.isWorking());
    assertEquals(true, HealthStatus.ERROR.isOutOfService());
    assertEquals(false, HealthStatus.UNKNOWN.isWorking());
    assertEquals(true, HealthStatus.UNKNOWN.isOutOfService());
  }

  @Test
  public void testHealthCheckResult( ) {
    MessageID lErrorMessageID = new MessageID(4710, TraceLevel.ERROR);
    FailureMessage lErrorMessage = new FailureMessage(lErrorMessageID);
    MessageID lErrorMessageID2 = new MessageID(4711, TraceLevel.ERROR);
    FailureMessage lErrorMessage2 = new FailureMessage(lErrorMessageID2);

    HealthCheckResult lHealthCheckResult = new HealthCheckResult(HealthStatus.WARNING);
    assertEquals(HealthStatus.WARNING, lHealthCheckResult.getHealthStatus());
    assertEquals(0, lHealthCheckResult.getWarnings().size());
    assertEquals(0, lHealthCheckResult.getErrors().size());

    lHealthCheckResult = new HealthCheckResult(HealthStatus.ERROR, lErrorMessage, null);
    assertEquals(HealthStatus.ERROR, lHealthCheckResult.getHealthStatus());
    assertEquals(1, lHealthCheckResult.getWarnings().size());
    assertEquals(lErrorMessage, lHealthCheckResult.getWarnings().get(0));
    assertEquals(0, lHealthCheckResult.getErrors().size());

    lHealthCheckResult = new HealthCheckResult(HealthStatus.ERROR, null, lErrorMessage);
    assertEquals(HealthStatus.ERROR, lHealthCheckResult.getHealthStatus());
    assertEquals(0, lHealthCheckResult.getWarnings().size());
    assertEquals(1, lHealthCheckResult.getErrors().size());
    assertEquals(lErrorMessage, lHealthCheckResult.getErrors().get(0));

    lHealthCheckResult = new HealthCheckResult(HealthStatus.OK, null, Arrays.asList(lErrorMessage, lErrorMessage2));
    assertEquals(HealthStatus.OK, lHealthCheckResult.getHealthStatus());
    assertEquals(0, lHealthCheckResult.getWarnings().size());
    assertEquals(2, lHealthCheckResult.getErrors().size());
    assertEquals(lErrorMessage, lHealthCheckResult.getErrors().get(0));
    assertEquals(lErrorMessage2, lHealthCheckResult.getErrors().get(1));

    lHealthCheckResult = new HealthCheckResult(HealthStatus.OK, Arrays.asList(lErrorMessage, lErrorMessage2), null);
    assertEquals(HealthStatus.OK, lHealthCheckResult.getHealthStatus());
    assertEquals(2, lHealthCheckResult.getWarnings().size());
    assertEquals(lErrorMessage, lHealthCheckResult.getWarnings().get(0));
    assertEquals(lErrorMessage2, lHealthCheckResult.getWarnings().get(1));
    assertEquals(0, lHealthCheckResult.getErrors().size());

    // Check default result.
    lHealthCheckResult = HealthCheckResult.CHECK_OK;
    assertEquals(HealthStatus.OK, lHealthCheckResult.getHealthStatus());
    assertEquals(0, lHealthCheckResult.getWarnings().size());
    assertEquals(0, lHealthCheckResult.getErrors().size());

    // Check error handling
    try {
      new HealthCheckResult(null);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pStatus' must not be null.", e.getMessage());
    }
  }
}
