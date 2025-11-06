/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.errorhandling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.errorhandling.ErrorCode;
import com.anaptecs.jeaf.xfun.api.errorhandling.ExceptionInfo;
import com.anaptecs.jeaf.xfun.api.info.VersionInfo;
import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ExceptionInfoTest {
  @Test
  @Order(10)
  public void testExceptionInfo( ) {
    // Create exception info with minimum input.
    ErrorCode lErrorCode = new ErrorCode(12345, TraceLevel.ERROR);
    VersionInfo lVersion = XFun.getInfoProvider().getApplicationInfo().getVersion();
    ExceptionInfo lExceptionInfo = new ExceptionInfo(lErrorCode, null, null, null, lVersion);
    assertEquals(lErrorCode, lExceptionInfo.getErrorCode());
    assertNull(lExceptionInfo.getMessageParameters());
    assertNull(lExceptionInfo.getCause());
    assertNull(lExceptionInfo.getTechnicalDetails());
    assertEquals(lVersion, lExceptionInfo.getVersionInfo());
    assertEquals("[0 12345] 12345", lExceptionInfo.getMessage());
    long lCurrentTimeMillis = System.currentTimeMillis();
    long lTimeDifference = lCurrentTimeMillis - lExceptionInfo.getTimestamp().getTime();
    assertTrue(lTimeDifference >= 0);
    assertTrue(lTimeDifference < 50);

    // ExceptionInfo with parameters
    lExceptionInfo = new ExceptionInfo(lErrorCode, new String[] { "abc", "def", "ghi" }, null,
        "Some ugly technical details", lVersion);
    assertEquals(lErrorCode, lExceptionInfo.getErrorCode());
    assertEquals(3, lExceptionInfo.getMessageParameters().length);
    assertEquals("abc", lExceptionInfo.getMessageParameters()[0]);
    assertEquals("def", lExceptionInfo.getMessageParameters()[1]);
    assertEquals("ghi", lExceptionInfo.getMessageParameters()[2]);
    assertNull(lExceptionInfo.getCause());
    assertEquals("Some ugly technical details", lExceptionInfo.getTechnicalDetails());
    assertEquals(lVersion, lExceptionInfo.getVersionInfo());
    assertEquals("[0 12345] 12345: abc def ghi", lExceptionInfo.getMessage());
    lCurrentTimeMillis = System.currentTimeMillis();
    lTimeDifference = lCurrentTimeMillis - lExceptionInfo.getTimestamp().getTime();
    assertTrue(lTimeDifference >= 0);
    assertTrue(lTimeDifference < 50);

    // ExceptionInfo with cause.
    NullPointerException lCause = new NullPointerException();
    lExceptionInfo = new ExceptionInfo(lErrorCode, new String[] { "abc", "def", "ghi" }, lCause,
        "Some ugly technical details", lVersion);
    assertEquals(lErrorCode, lExceptionInfo.getErrorCode());
    assertEquals(3, lExceptionInfo.getMessageParameters().length);
    assertEquals("abc", lExceptionInfo.getMessageParameters()[0]);
    assertEquals("def", lExceptionInfo.getMessageParameters()[1]);
    assertEquals("ghi", lExceptionInfo.getMessageParameters()[2]);
    assertEquals(lCause, lExceptionInfo.getCause());
    assertEquals("Some ugly technical details", lExceptionInfo.getTechnicalDetails());
    assertEquals(lVersion, lExceptionInfo.getVersionInfo());
    assertEquals("[0 12345] 12345: abc def ghi", lExceptionInfo.getMessage());
    lCurrentTimeMillis = System.currentTimeMillis();
    lTimeDifference = lCurrentTimeMillis - lExceptionInfo.getTimestamp().getTime();
    assertTrue(lTimeDifference >= 0);
    assertTrue(lTimeDifference < 50);

  }
}
