/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.trace;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.anaptecs.jeaf.xfun.api.common.ComponentID;
import com.anaptecs.jeaf.xfun.api.errorhandling.ApplicationException;
import com.anaptecs.jeaf.xfun.api.errorhandling.ErrorCode;
import com.anaptecs.jeaf.xfun.api.errorhandling.SystemException;
import com.anaptecs.jeaf.xfun.api.info.ApplicationInfo;
import com.anaptecs.jeaf.xfun.api.messages.MessageID;
import com.anaptecs.jeaf.xfun.api.trace.ContextStackElement;
import com.anaptecs.jeaf.xfun.api.trace.ObjectFormatter;
import com.anaptecs.jeaf.xfun.api.trace.Trace;
import com.anaptecs.jeaf.xfun.api.trace.TraceConfiguration;
import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;
import com.anaptecs.jeaf.xfun.fallback.trace.FallbackTraceImpl;
import com.anaptecs.jeaf.xfun.fallback.trace.FallbackTraceProviderImpl;
import com.anaptecs.jeaf.xfun.test.errorhandling.TestApplicationException;
import com.anaptecs.jeaf.xfun.test.errorhandling.TestSystemException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FallbackTraceTest {
  private TestHandler handler = new TestHandler();

  private Logger rootLogger;

  private static final MessageID messageID = new MessageID(4711, TraceLevel.TRACE);

  @BeforeEach
  public void setup( ) {
    // reset() will remove all default handlers
    LogManager lLogManager = LogManager.getLogManager();
    lLogManager.reset();
    rootLogger = lLogManager.getLogger("");
    rootLogger.setLevel(Level.ALL);
    rootLogger.addHandler(handler);
  }

  @Test
  @Order(10)
  public void testEmergencyTrace( ) {
    Trace lTrace = this.getFallbackTrace();
    String lMessage = "Hello Fallback-Trace";

    lTrace.writeEmergencyTrace(lMessage, null);
    assertEquals(lMessage, handler.getLastLogRecord().getMessage(), "Wrong trace message");
    handler.clear();
  }

  @Test
  @Order(20)
  public void testInitTrace( ) {
    Trace lTrace = this.getFallbackTrace();
    String lMessage = "Hello Fallback-Trace";

    // Test level TRACE
    lTrace.writeInitInfo(lMessage, TraceLevel.TRACE);
    LogRecord lLastLogRecord = handler.getLastLogRecord();
    assertEquals(lMessage, lLastLogRecord.getMessage(), "Wrong trace message");
    assertEquals(lLastLogRecord.getLevel(), Level.FINEST);
    handler.clear();

    // Test level TRACE
    lTrace.writeInitInfo(lMessage, TraceLevel.DEBUG);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals(lMessage, lLastLogRecord.getMessage(), "Wrong trace message");
    assertEquals(lLastLogRecord.getLevel(), Level.FINE);
    handler.clear();

    // Test level TRACE
    lTrace.writeInitInfo(lMessage, TraceLevel.INFO);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals(lMessage, lLastLogRecord.getMessage(), "Wrong trace message");
    assertEquals(lLastLogRecord.getLevel(), Level.INFO);
    handler.clear();

    // Test level TRACE
    lTrace.writeInitInfo(lMessage, TraceLevel.WARN);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals(lMessage, lLastLogRecord.getMessage(), "Wrong trace message");
    assertEquals(lLastLogRecord.getLevel(), Level.WARNING);
    handler.clear();

    // Test level TRACE
    lTrace.writeInitInfo(lMessage, TraceLevel.ERROR);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals(lMessage, lLastLogRecord.getMessage(), "Wrong trace message");
    assertEquals(lLastLogRecord.getLevel(), Level.SEVERE);
    handler.clear();

    // Test level TRACE
    lTrace.writeInitInfo(lMessage, TraceLevel.FATAL);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals(lMessage, lLastLogRecord.getMessage(), "Wrong trace message");
    assertEquals(lLastLogRecord.getLevel(), Level.SEVERE);
    handler.clear();

    // Test level no trace level
    lTrace.writeInitInfo(lMessage, null);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals(lMessage, lLastLogRecord.getMessage(), "Wrong trace message");
    assertEquals(lLastLogRecord.getLevel(), Level.SEVERE);
    handler.clear();

    lTrace.writeInitInfo(null, TraceLevel.WARN);
    lLastLogRecord = handler.getLastLogRecord();
    assertNull(lLastLogRecord, "No trace message expected.");

    lTrace.writeInitInfo(null, null);
    lLastLogRecord = handler.getLastLogRecord();
    assertNull(lLastLogRecord, "No trace message expected.");
  }

  @Test
  @Order(30)
  public void testTraceLevelTrace( ) {
    Object lObject = new Object();
    Trace lTrace = this.getFallbackTrace();
    String lMessage = "Hello Fallback-Trace";

    // Check level detection.
    Level lCurrentLevel = Level.FINE;
    rootLogger.setLevel(lCurrentLevel);
    assertFalse(lTrace.isTraceEnabled(), "Level TRACE is enabled.");

    lCurrentLevel = Level.FINEST;
    rootLogger.setLevel(lCurrentLevel);
    assertTrue(lTrace.isTraceEnabled(), "Level TRACE is not enabled.");

    // Test writing of traces
    lTrace.trace(lMessage);
    assertEquals(lMessage, handler.getLastLogRecord().getMessage(), "Wrong trace message");
    handler.clear();

    // Test trace object
    TraceableObject lTraceableObject = new TraceableObject();
    rootLogger.setLevel(Level.FINE);
    lTrace.traceObject(lTraceableObject);
    assertNull(handler.getLastLogRecord(), "No log record expected due to log level.");

    rootLogger.setLevel(Level.FINEST);
    lTrace.traceObject(lTraceableObject);
    assertEquals(lTraceableObject.toString(), handler.getLastLogRecord().getMessage(), "Wrong trace message");
    handler.clear();

    // Try to trace null instead of an object.
    lTrace.traceObject(null);
    assertNull(handler.getLastLogRecord(), "No log record expected due to null value that was passed.");

    // Test tracing of exceptions
    IllegalArgumentException lIllegalArgumentException = new IllegalArgumentException("IllegalArgumentException");
    lTrace.trace(lIllegalArgumentException);
    LogRecord lLastLogRecord = handler.getLastLogRecord();
    assertEquals(lIllegalArgumentException.getMessage(), lLastLogRecord.getMessage());
    assertEquals(Level.FINEST, lLastLogRecord.getLevel());
    assertEquals(lIllegalArgumentException, lLastLogRecord.getThrown());
    handler.clear();

    lTrace.trace(lIllegalArgumentException, false);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals(lIllegalArgumentException.getMessage(), lLastLogRecord.getMessage());
    assertEquals(Level.FINEST, lLastLogRecord.getLevel());
    assertEquals(lIllegalArgumentException, lLastLogRecord.getThrown());
    handler.clear();

    lTrace.trace(lIllegalArgumentException, true);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals(lIllegalArgumentException.getMessage(), lLastLogRecord.getMessage());
    assertEquals(Level.FINEST, lLastLogRecord.getLevel());
    assertEquals(null, lLastLogRecord.getThrown());
    handler.clear();

    ApplicationException lAppException = new TestApplicationException(new ErrorCode(123456789, TraceLevel.INFO));
    lTrace.trace(lAppException);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals("Error-Code: 123456789", lLastLogRecord.getMessage());
    assertEquals(Level.FINEST, lLastLogRecord.getLevel());
    assertEquals(lAppException, lLastLogRecord.getThrown());
    handler.clear();

    lTrace.trace(lAppException, false);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals("Error-Code: 123456789", lLastLogRecord.getMessage());
    assertEquals(Level.FINEST, lLastLogRecord.getLevel());
    assertEquals(lAppException, lLastLogRecord.getThrown());
    handler.clear();

    lTrace.trace(lAppException, true);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals("Error-Code: 123456789", lLastLogRecord.getMessage());
    assertEquals(Level.FINEST, lLastLogRecord.getLevel());
    assertEquals(null, lLastLogRecord.getThrown());
    handler.clear();

    // Trace object that has an object formatter.
    ObjectFormatter<?> lObjectFormatter = TraceConfiguration.getInstance().getObjectFormatter(ApplicationInfo.class);
    assertNotNull(lObjectFormatter, "ObjectFormatter for ApplicationInfo expected.");
    lTrace.traceObject(ApplicationInfo.UNKNOWN_APPLICATION);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals(ApplicationInfo.UNKNOWN_APPLICATION.toString(), handler.getLastLogRecord().getMessage(),
        "Wrong trace message");
    handler.clear();

    // Test tracing of exceptions.
    Exception lException = new IllegalArgumentException();
    rootLogger.setLevel(Level.FINE);
    lTrace.trace(lMessage, lException);
    assertNull(handler.getLastLogRecord(), "No log record expected due to log level.");

    rootLogger.setLevel(Level.FINEST);
    lTrace.trace(lMessage, lException);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals(lMessage, handler.getLastLogRecord().getMessage(), "Wrong trace message");
    assertEquals(lException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown");

    lTrace.trace(lMessage, null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals(lMessage, handler.getLastLogRecord().getMessage(), "Wrong trace message");
    assertNull(handler.getLastLogRecord().getThrown(), "null as thrown expected");
    handler.clear();

    lTrace.trace((String) null, (Throwable) null);
    assertNull(handler.getLastLogRecord(), "No log record expected due to null value that was passed.");

    lTrace.trace(null, lException);
    assertNull(handler.getLastLogRecord().getMessage(), "No trace message expected.");
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals(lException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown");
    handler.clear();

    // Test tracing of parameterized messages. In case of fallback traces we do not have a message repo in the back.
    rootLogger.setLevel(Level.FINE);
    lTrace.trace(messageID, "Hello", " ", "World!");
    assertNull(handler.getLastLogRecord(), "No log record expected due to log level.");

    rootLogger.setLevel(Level.FINEST);
    lTrace.trace(messageID, "Hello", " ", "World!");
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [Hello,  , World!]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");

    lTrace.trace(messageID, lObject);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [" + lObject + "]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");

    lTrace.trace(messageID);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");

    lTrace.trace(messageID, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");

    lTrace.trace(null, "Hello", " ", "World!");
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: ? Details: [Hello,  , World!]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");

    lTrace.trace(null, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: ?", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    handler.clear();

    // Test tracing of parameterized messages plus exception
    rootLogger.setLevel(Level.FINE);
    lTrace.trace(messageID, lException, "Hello", " ", "World!");
    assertNull(handler.getLastLogRecord(), "No log record expected due to log level.");

    rootLogger.setLevel(Level.FINEST);
    lTrace.trace(messageID, lException, "Hello", " ", "World!");
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [Hello,  , World!]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");
    assertEquals(lException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.trace(messageID, lException, lObject);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [" + lObject + "]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");
    assertEquals(lException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.trace(messageID, lException, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    assertEquals(lException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.trace(messageID, (Throwable) null, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    assertNull(handler.getLastLogRecord().getThrown(), "No exception expected.");
    handler.clear();

    // Test tracing of parameterized messages plus ApplicationException
    ApplicationException lApplicationException =
        new TestApplicationException(new ErrorCode(123456789, TraceLevel.INFO));
    rootLogger.setLevel(Level.FINE);
    lTrace.trace(messageID, lApplicationException, "Hello", " ", "World!");
    assertNull(handler.getLastLogRecord(), "No log record expected due to log level.");

    rootLogger.setLevel(Level.FINEST);
    lTrace.trace(messageID, lApplicationException, "Hello", " ", "World!");
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [Hello,  , World!]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");
    assertEquals(lApplicationException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.trace(messageID, lApplicationException, lObject);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [" + lObject + "]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");
    assertEquals(lApplicationException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.trace(messageID, lApplicationException, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    assertEquals(lApplicationException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.trace(messageID, (ApplicationException) null, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    assertNull(handler.getLastLogRecord().getThrown(), "No exception expected.");
    handler.clear();

    // Test tracing of parameterized messages plus SystemException
    SystemException lSystemException = new TestSystemException(new ErrorCode(123456789, TraceLevel.INFO));
    rootLogger.setLevel(Level.FINE);
    lTrace.trace(messageID, lSystemException, "Hello", " ", "World!");
    assertNull(handler.getLastLogRecord(), "No log record expected due to log level.");

    rootLogger.setLevel(Level.FINEST);
    lTrace.trace(messageID, lSystemException, "Hello", " ", "World!");
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [Hello,  , World!]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");
    assertEquals(lSystemException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.trace(messageID, lSystemException, lObject);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [" + lObject + "]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");
    assertEquals(lSystemException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.trace(messageID, lSystemException, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    assertEquals(lSystemException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.trace(messageID, (SystemException) null, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    assertNull(handler.getLastLogRecord().getThrown(), "No exception expected.");
    handler.clear();
  }

  @Test
  @Order(31)
  public void testDebugLevelTrace( ) {
    Object lObject = new Object();
    Trace lTrace = this.getFallbackTrace();
    String lMessage = "Hello Fallback-Trace";

    // Check level detection.
    Level lCurrentLevel = Level.INFO;
    rootLogger.setLevel(lCurrentLevel);
    assertFalse(lTrace.isDebugEnabled(), "Level TRACE is enabled.");

    lCurrentLevel = Level.FINE;
    rootLogger.setLevel(lCurrentLevel);
    assertTrue(lTrace.isDebugEnabled(), "Level TRACE is not enabled.");

    // Test writing of traces
    lTrace.debug(lMessage);
    assertEquals(lMessage, handler.getLastLogRecord().getMessage(), "Wrong trace message");
    handler.clear();

    // Test trace object
    TraceableObject lTraceableObject = new TraceableObject();
    rootLogger.setLevel(Level.INFO);
    lTrace.debugObject(lTraceableObject);
    assertNull(handler.getLastLogRecord(), "No log record expected due to log level.");

    rootLogger.setLevel(Level.FINE);
    lTrace.debugObject(lTraceableObject);
    assertEquals(lTraceableObject.toString(), handler.getLastLogRecord().getMessage(), "Wrong trace message");
    handler.clear();

    // Try to trace null instead of an object.
    lTrace.debugObject(null);
    assertNull(handler.getLastLogRecord(), "No log record expected due to null value that was passed.");

    // Test tracing of exceptions
    IllegalArgumentException lIllegalArgumentException = new IllegalArgumentException("IllegalArgumentException");
    lTrace.debug(lIllegalArgumentException);
    LogRecord lLastLogRecord = handler.getLastLogRecord();
    assertEquals(lIllegalArgumentException.getMessage(), lLastLogRecord.getMessage());
    assertEquals(Level.FINE, lLastLogRecord.getLevel());
    assertEquals(lIllegalArgumentException, lLastLogRecord.getThrown());
    handler.clear();

    lTrace.debug(lIllegalArgumentException, false);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals(lIllegalArgumentException.getMessage(), lLastLogRecord.getMessage());
    assertEquals(Level.FINE, lLastLogRecord.getLevel());
    assertEquals(lIllegalArgumentException, lLastLogRecord.getThrown());
    handler.clear();

    lTrace.debug(lIllegalArgumentException, true);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals(lIllegalArgumentException.getMessage(), lLastLogRecord.getMessage());
    assertEquals(Level.FINE, lLastLogRecord.getLevel());
    assertEquals(null, lLastLogRecord.getThrown());
    handler.clear();

    ApplicationException lAppException = new TestApplicationException(new ErrorCode(123456789, TraceLevel.INFO));
    lTrace.debug(lAppException);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals("Error-Code: 123456789", lLastLogRecord.getMessage());
    assertEquals(Level.FINE, lLastLogRecord.getLevel());
    assertEquals(lAppException, lLastLogRecord.getThrown());
    handler.clear();

    lTrace.debug(lAppException, false);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals("Error-Code: 123456789", lLastLogRecord.getMessage());
    assertEquals(Level.FINE, lLastLogRecord.getLevel());
    assertEquals(lAppException, lLastLogRecord.getThrown());
    handler.clear();

    lTrace.debug(lAppException, true);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals("Error-Code: 123456789", lLastLogRecord.getMessage());
    assertEquals(Level.FINE, lLastLogRecord.getLevel());
    assertEquals(null, lLastLogRecord.getThrown());
    handler.clear();

    // Trace object that has an object formatter.
    ObjectFormatter<?> lObjectFormatter = TraceConfiguration.getInstance().getObjectFormatter(ApplicationInfo.class);
    assertNotNull(lObjectFormatter, "ObjectFormatter for ApplicationInfo expected.");
    lTrace.debugObject(ApplicationInfo.UNKNOWN_APPLICATION);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals(ApplicationInfo.UNKNOWN_APPLICATION.toString(), handler.getLastLogRecord().getMessage(),
        "Wrong trace message");
    handler.clear();

    // Test tracing of exceptions.
    Exception lException = new IllegalArgumentException();
    rootLogger.setLevel(Level.INFO);
    lTrace.debug(lMessage, lException);
    assertNull(handler.getLastLogRecord(), "No log record expected due to log level.");

    rootLogger.setLevel(Level.FINE);
    lTrace.debug(lMessage, lException);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals(lMessage, handler.getLastLogRecord().getMessage(), "Wrong trace message");
    assertEquals(lException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown");

    lTrace.debug(lMessage, null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals(lMessage, handler.getLastLogRecord().getMessage(), "Wrong trace message");
    assertNull(handler.getLastLogRecord().getThrown(), "null as thrown expected");
    handler.clear();

    lTrace.debug((String) null, (Throwable) null);
    assertNull(handler.getLastLogRecord(), "No log record expected due to null value that was passed.");

    lTrace.debug(null, lException);
    assertNull(handler.getLastLogRecord().getMessage(), "No trace message expected.");
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals(lException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown");
    handler.clear();

    // Test tracing of parameterized messages. In case of fallback traces we do not have a message repo in the back.
    rootLogger.setLevel(Level.INFO);
    lTrace.debug(messageID, "Hello", " ", "World!");
    assertNull(handler.getLastLogRecord(), "No log record expected due to log level.");

    rootLogger.setLevel(Level.FINE);
    lTrace.debug(messageID, "Hello", " ", "World!");
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [Hello,  , World!]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");

    lTrace.debug(messageID, lObject);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [" + lObject + "]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");

    lTrace.debug(messageID);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");

    lTrace.debug(messageID, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");

    lTrace.debug(null, "Hello", " ", "World!");
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: ? Details: [Hello,  , World!]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");

    lTrace.debug(null, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: ?", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    handler.clear();

    // Test tracing of parameterized messages plus exception
    rootLogger.setLevel(Level.INFO);
    lTrace.debug(messageID, lException, "Hello", " ", "World!");
    assertNull(handler.getLastLogRecord(), "No log record expected due to log level.");

    rootLogger.setLevel(Level.FINE);
    lTrace.debug(messageID, lException, "Hello", " ", "World!");
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [Hello,  , World!]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");
    assertEquals(lException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.debug(messageID, lException, lObject);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [" + lObject + "]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");
    assertEquals(lException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.debug(messageID, lException, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    assertEquals(lException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.debug(messageID, (Throwable) null, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    assertNull(handler.getLastLogRecord().getThrown(), "No exception expected.");
    handler.clear();

    // Test tracing of parameterized messages plus ApplicationException
    ApplicationException lApplicationException =
        new TestApplicationException(new ErrorCode(123456789, TraceLevel.INFO));
    rootLogger.setLevel(Level.INFO);
    lTrace.debug(messageID, lApplicationException, "Hello", " ", "World!");
    assertNull(handler.getLastLogRecord(), "No log record expected due to log level.");

    rootLogger.setLevel(Level.FINE);
    lTrace.debug(messageID, lApplicationException, "Hello", " ", "World!");
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [Hello,  , World!]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");
    assertEquals(lApplicationException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.debug(messageID, lApplicationException, lObject);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [" + lObject + "]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");
    assertEquals(lApplicationException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.debug(messageID, lApplicationException, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    assertEquals(lApplicationException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.debug(messageID, (ApplicationException) null, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    assertNull(handler.getLastLogRecord().getThrown(), "No exception expected.");
    handler.clear();

    // Test tracing of parameterized messages plus SystemException
    SystemException lSystemException = new TestSystemException(new ErrorCode(123456789, TraceLevel.INFO));
    rootLogger.setLevel(Level.INFO);
    lTrace.debug(messageID, lSystemException, "Hello", " ", "World!");
    assertNull(handler.getLastLogRecord(), "No log record expected due to log level.");

    rootLogger.setLevel(Level.FINE);
    lTrace.debug(messageID, lSystemException, "Hello", " ", "World!");
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [Hello,  , World!]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");
    assertEquals(lSystemException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.debug(messageID, lSystemException, lObject);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [" + lObject + "]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");
    assertEquals(lSystemException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.debug(messageID, lSystemException, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    assertEquals(lSystemException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.debug(messageID, (SystemException) null, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    assertNull(handler.getLastLogRecord().getThrown(), "No exception expected.");
    handler.clear();
  }

  @Test
  @Order(32)
  public void testInfoLevelTrace( ) {
    Object lObject = new Object();
    Trace lTrace = this.getFallbackTrace();
    String lMessage = "Hello Fallback-Trace";

    // Check level detection.
    Level lCurrentLevel = Level.WARNING;
    rootLogger.setLevel(lCurrentLevel);
    assertFalse(lTrace.isInfoEnabled(), "Level TRACE is enabled.");

    lCurrentLevel = Level.INFO;
    rootLogger.setLevel(lCurrentLevel);
    assertTrue(lTrace.isInfoEnabled(), "Level TRACE is not enabled.");

    // Test writing of traces
    lTrace.info(lMessage);
    assertEquals(lMessage, handler.getLastLogRecord().getMessage(), "Wrong trace message");
    handler.clear();

    // Test trace object
    TraceableObject lTraceableObject = new TraceableObject();
    rootLogger.setLevel(Level.WARNING);
    lTrace.infoObject(lTraceableObject);
    assertNull(handler.getLastLogRecord(), "No log record expected due to log level.");

    rootLogger.setLevel(Level.INFO);
    lTrace.infoObject(lTraceableObject);
    assertEquals(lTraceableObject.toString(), handler.getLastLogRecord().getMessage(), "Wrong trace message");
    handler.clear();

    // Try to trace null instead of an object.
    lTrace.infoObject(null);
    assertNull(handler.getLastLogRecord(), "No log record expected due to null value that was passed.");

    // Test tracing of exceptions
    IllegalArgumentException lIllegalArgumentException = new IllegalArgumentException("IllegalArgumentException");
    lTrace.info(lIllegalArgumentException);
    LogRecord lLastLogRecord = handler.getLastLogRecord();
    assertEquals(lIllegalArgumentException.getMessage(), lLastLogRecord.getMessage());
    assertEquals(Level.INFO, lLastLogRecord.getLevel());
    assertEquals(lIllegalArgumentException, lLastLogRecord.getThrown());
    handler.clear();

    lTrace.info(lIllegalArgumentException, false);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals(lIllegalArgumentException.getMessage(), lLastLogRecord.getMessage());
    assertEquals(Level.INFO, lLastLogRecord.getLevel());
    assertEquals(lIllegalArgumentException, lLastLogRecord.getThrown());
    handler.clear();

    lTrace.info(lIllegalArgumentException, true);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals(lIllegalArgumentException.getMessage(), lLastLogRecord.getMessage());
    assertEquals(Level.INFO, lLastLogRecord.getLevel());
    assertEquals(null, lLastLogRecord.getThrown());
    handler.clear();

    ApplicationException lAppException = new TestApplicationException(new ErrorCode(123456789, TraceLevel.INFO));
    lTrace.info(lAppException);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals("Error-Code: 123456789", lLastLogRecord.getMessage());
    assertEquals(Level.INFO, lLastLogRecord.getLevel());
    assertEquals(lAppException, lLastLogRecord.getThrown());
    handler.clear();

    lTrace.info(lAppException, false);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals("Error-Code: 123456789", lLastLogRecord.getMessage());
    assertEquals(Level.INFO, lLastLogRecord.getLevel());
    assertEquals(lAppException, lLastLogRecord.getThrown());
    handler.clear();

    lTrace.info(lAppException, true);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals("Error-Code: 123456789", lLastLogRecord.getMessage());
    assertEquals(Level.INFO, lLastLogRecord.getLevel());
    assertEquals(null, lLastLogRecord.getThrown());
    handler.clear();

    // Trace object that has an object formatter.
    ObjectFormatter<?> lObjectFormatter = TraceConfiguration.getInstance().getObjectFormatter(ApplicationInfo.class);
    assertNotNull(lObjectFormatter, "ObjectFormatter for ApplicationInfo expected.");
    lTrace.infoObject(ApplicationInfo.UNKNOWN_APPLICATION);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals(ApplicationInfo.UNKNOWN_APPLICATION.toString(), handler.getLastLogRecord().getMessage(),
        "Wrong trace message");
    handler.clear();

    // Test tracing of exceptions.
    Exception lException = new IllegalArgumentException();
    rootLogger.setLevel(Level.WARNING);
    lTrace.info(lMessage, lException);
    assertNull(handler.getLastLogRecord(), "No log record expected due to log level.");

    rootLogger.setLevel(Level.INFO);
    lTrace.info(lMessage, lException);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals(lMessage, handler.getLastLogRecord().getMessage(), "Wrong trace message");
    assertEquals(lException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown");

    lTrace.info(lMessage, null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals(lMessage, handler.getLastLogRecord().getMessage(), "Wrong trace message");
    assertNull(handler.getLastLogRecord().getThrown(), "null as thrown expected");
    handler.clear();

    lTrace.info((String) null, (Throwable) null);
    assertNull(handler.getLastLogRecord(), "No log record expected due to null value that was passed.");

    lTrace.info(null, lException);
    assertNull(handler.getLastLogRecord().getMessage(), "No trace message expected.");
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals(lException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown");
    handler.clear();

    // Test tracing of parameterized messages. In case of fallback traces we do not have a message repo in the back.
    rootLogger.setLevel(Level.WARNING);
    lTrace.info(messageID, "Hello", " ", "World!");
    assertNull(handler.getLastLogRecord(), "No log record expected due to log level.");

    rootLogger.setLevel(Level.INFO);
    lTrace.info(messageID, "Hello", " ", "World!");
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [Hello,  , World!]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");

    lTrace.info(messageID, lObject);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [" + lObject + "]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");

    lTrace.info(messageID, (Object[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");

    lTrace.info(messageID);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");

    lTrace.info(messageID, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");

    lTrace.info(null, "Hello", " ", "World!");
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: ? Details: [Hello,  , World!]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");

    lTrace.info(null, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: ?", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    handler.clear();

    // Test tracing of parameterized messages plus exception
    rootLogger.setLevel(Level.WARNING);
    lTrace.info(messageID, lException, "Hello", " ", "World!");
    assertNull(handler.getLastLogRecord(), "No log record expected due to log level.");

    rootLogger.setLevel(Level.INFO);
    lTrace.info(messageID, lException, "Hello", " ", "World!");
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [Hello,  , World!]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");
    assertEquals(lException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.info(messageID, lException, lObject);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [" + lObject + "]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");
    assertEquals(lException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.info(messageID, lException, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    assertEquals(lException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.info(messageID, (Throwable) null, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    assertNull(handler.getLastLogRecord().getThrown(), "No exception expected.");
    handler.clear();

    // Test tracing of parameterized messages plus ApplicationException
    ApplicationException lApplicationException =
        new TestApplicationException(new ErrorCode(123456789, TraceLevel.WARN));
    rootLogger.setLevel(Level.WARNING);
    lTrace.info(messageID, lApplicationException, "Hello", " ", "World!");
    assertNull(handler.getLastLogRecord(), "No log record expected due to log level.");

    rootLogger.setLevel(Level.INFO);
    lTrace.info(messageID, lApplicationException, "Hello", " ", "World!");
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [Hello,  , World!]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");
    assertEquals(lApplicationException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.info(messageID, lApplicationException, lObject);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [" + lObject + "]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");
    assertEquals(lApplicationException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.info(messageID, lApplicationException, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    assertEquals(lApplicationException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.info(messageID, (ApplicationException) null, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    assertNull(handler.getLastLogRecord().getThrown(), "No exception expected.");
    handler.clear();

    // Test tracing of parameterized messages plus SystemException
    SystemException lSystemException = new TestSystemException(new ErrorCode(123456789, TraceLevel.WARN));
    rootLogger.setLevel(Level.WARNING);
    lTrace.info(messageID, lSystemException, "Hello", " ", "World!");
    assertNull(handler.getLastLogRecord(), "No log record expected due to log level.");

    rootLogger.setLevel(Level.INFO);
    lTrace.info(messageID, lSystemException, "Hello", " ", "World!");
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [Hello,  , World!]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");
    assertEquals(lSystemException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.info(messageID, lSystemException, lObject);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [" + lObject + "]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");
    assertEquals(lSystemException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.info(messageID, lSystemException, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    assertEquals(lSystemException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.info(messageID, (SystemException) null, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    assertNull(handler.getLastLogRecord().getThrown(), "No exception expected.");
    handler.clear();
  }

  @Test
  @Order(33)
  public void testWarnLevelTrace( ) {
    Object lObject = new Object();
    Trace lTrace = this.getFallbackTrace();
    String lMessage = "Hello Fallback-Trace";

    // Check level detection.
    Level lCurrentLevel = Level.SEVERE;
    rootLogger.setLevel(lCurrentLevel);
    assertFalse(lTrace.isWarnEnabled(), "Level TRACE is enabled.");

    lCurrentLevel = Level.WARNING;
    rootLogger.setLevel(lCurrentLevel);
    assertTrue(lTrace.isWarnEnabled(), "Level TRACE is not enabled.");

    // Test writing of traces
    lTrace.warn(lMessage);
    assertEquals(lMessage, handler.getLastLogRecord().getMessage(), "Wrong trace message");
    handler.clear();

    // Test trace object
    TraceableObject lTraceableObject = new TraceableObject();
    rootLogger.setLevel(Level.SEVERE);
    lTrace.warnObject(lTraceableObject);
    assertNull(handler.getLastLogRecord(), "No log record expected due to log level.");

    rootLogger.setLevel(Level.WARNING);
    lTrace.warnObject(lTraceableObject);
    assertEquals(lTraceableObject.toString(), handler.getLastLogRecord().getMessage(), "Wrong trace message");
    handler.clear();

    // Try to trace null instead of an object.
    lTrace.warnObject(null);
    assertNull(handler.getLastLogRecord(), "No log record expected due to null value that was passed.");

    // Test tracing of exceptions
    IllegalArgumentException lIllegalArgumentException = new IllegalArgumentException("IllegalArgumentException");
    lTrace.warn(lIllegalArgumentException);
    LogRecord lLastLogRecord = handler.getLastLogRecord();
    assertEquals(lIllegalArgumentException.getMessage(), lLastLogRecord.getMessage());
    assertEquals(Level.WARNING, lLastLogRecord.getLevel());
    assertEquals(lIllegalArgumentException, lLastLogRecord.getThrown());
    handler.clear();

    lTrace.warn(lIllegalArgumentException, false);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals(lIllegalArgumentException.getMessage(), lLastLogRecord.getMessage());
    assertEquals(Level.WARNING, lLastLogRecord.getLevel());
    assertEquals(lIllegalArgumentException, lLastLogRecord.getThrown());
    handler.clear();

    lTrace.warn(lIllegalArgumentException, true);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals(lIllegalArgumentException.getMessage(), lLastLogRecord.getMessage());
    assertEquals(Level.WARNING, lLastLogRecord.getLevel());
    assertEquals(null, lLastLogRecord.getThrown());
    handler.clear();

    ApplicationException lAppException = new TestApplicationException(new ErrorCode(123456789, TraceLevel.INFO));
    lTrace.warn(lAppException);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals("Error-Code: 123456789", lLastLogRecord.getMessage());
    assertEquals(Level.WARNING, lLastLogRecord.getLevel());
    assertEquals(lAppException, lLastLogRecord.getThrown());
    handler.clear();

    lTrace.warn(lAppException, false);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals("Error-Code: 123456789", lLastLogRecord.getMessage());
    assertEquals(Level.WARNING, lLastLogRecord.getLevel());
    assertEquals(lAppException, lLastLogRecord.getThrown());
    handler.clear();

    lTrace.warn(lAppException, true);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals("Error-Code: 123456789", lLastLogRecord.getMessage());
    assertEquals(Level.WARNING, lLastLogRecord.getLevel());
    assertEquals(null, lLastLogRecord.getThrown());
    handler.clear();

    // Trace object that has an object formatter.
    ObjectFormatter<?> lObjectFormatter = TraceConfiguration.getInstance().getObjectFormatter(ApplicationInfo.class);
    assertNotNull(lObjectFormatter, "ObjectFormatter for ApplicationInfo expected.");
    lTrace.warnObject(ApplicationInfo.UNKNOWN_APPLICATION);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals(ApplicationInfo.UNKNOWN_APPLICATION.toString(), handler.getLastLogRecord().getMessage(),
        "Wrong trace message");
    handler.clear();

    // Test tracing of exceptions.
    Exception lException = new IllegalArgumentException();
    rootLogger.setLevel(Level.SEVERE);
    lTrace.warn(lMessage, lException);
    assertNull(handler.getLastLogRecord(), "No log record expected due to log level.");

    rootLogger.setLevel(Level.WARNING);
    lTrace.warn(lMessage, lException);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals(lMessage, handler.getLastLogRecord().getMessage(), "Wrong trace message");
    assertEquals(lException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown");

    lTrace.warn(lMessage, null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals(lMessage, handler.getLastLogRecord().getMessage(), "Wrong trace message");
    assertNull(handler.getLastLogRecord().getThrown(), "null as thrown expected");
    handler.clear();

    lTrace.warn((String) null, (Throwable) null);
    assertNull(handler.getLastLogRecord(), "No log record expected due to null value that was passed.");

    lTrace.warn(null, lException);
    assertNull(handler.getLastLogRecord().getMessage(), "No trace message expected.");
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals(lException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown");
    handler.clear();

    // Test tracing of parameterized messages. In case of fallback traces we do not have a message repo in the back.
    rootLogger.setLevel(Level.SEVERE);
    lTrace.warn(messageID, "Hello", " ", "World!");
    assertNull(handler.getLastLogRecord(), "No log record expected due to log level.");

    rootLogger.setLevel(Level.WARNING);
    lTrace.warn(messageID, "Hello", " ", "World!");
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [Hello,  , World!]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");

    lTrace.warn(messageID, lObject);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [" + lObject + "]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");

    lTrace.warn(messageID);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");

    lTrace.warn(messageID, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");

    lTrace.warn(null, "Hello", " ", "World!");
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: ? Details: [Hello,  , World!]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");

    lTrace.warn(null, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: ?", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    handler.clear();

    // Test tracing of parameterized messages plus exception
    rootLogger.setLevel(Level.SEVERE);
    lTrace.warn(messageID, lException, "Hello", " ", "World!");
    assertNull(handler.getLastLogRecord(), "No log record expected due to log level.");

    rootLogger.setLevel(Level.WARNING);
    lTrace.warn(messageID, lException, "Hello", " ", "World!");
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [Hello,  , World!]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");
    assertEquals(lException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.warn(messageID, lException, lObject);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [" + lObject + "]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");
    assertEquals(lException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.warn(messageID, lException, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    assertEquals(lException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.warn(messageID, (Throwable) null, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    assertNull(handler.getLastLogRecord().getThrown(), "No exception expected.");
    handler.clear();

    // Test tracing of parameterized messages plus ApplicationException
    ApplicationException lApplicationException =
        new TestApplicationException(new ErrorCode(123456789, TraceLevel.WARN));
    rootLogger.setLevel(Level.SEVERE);
    lTrace.warn(messageID, lApplicationException, "Hello", " ", "World!");
    assertNull(handler.getLastLogRecord(), "No log record expected due to log level.");

    rootLogger.setLevel(Level.WARNING);
    lTrace.warn(messageID, lApplicationException, "Hello", " ", "World!");
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [Hello,  , World!]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");
    assertEquals(lApplicationException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.warn(messageID, lApplicationException, lObject);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [" + lObject + "]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");
    assertEquals(lApplicationException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.warn(messageID, lApplicationException, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    assertEquals(lApplicationException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.warn(messageID, (ApplicationException) null, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    assertNull(handler.getLastLogRecord().getThrown(), "No exception expected.");
    handler.clear();

    // Test tracing of parameterized messages plus SystemException
    SystemException lSystemException = new TestSystemException(new ErrorCode(123456789, TraceLevel.WARN));
    rootLogger.setLevel(Level.SEVERE);
    lTrace.warn(messageID, lSystemException, "Hello", " ", "World!");
    assertNull(handler.getLastLogRecord(), "No log record expected due to log level.");

    rootLogger.setLevel(Level.WARNING);
    lTrace.warn(messageID, lSystemException, "Hello", " ", "World!");
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [Hello,  , World!]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");
    assertEquals(lSystemException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.warn(messageID, lSystemException, lObject);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [" + lObject + "]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");
    assertEquals(lSystemException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.warn(messageID, lSystemException, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    assertEquals(lSystemException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.warn(messageID, (SystemException) null, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    assertNull(handler.getLastLogRecord().getThrown(), "No exception expected.");
    handler.clear();
  }

  @Test
  @Order(34)
  public void testErrorLevelTrace( ) {
    Object lObject = new Object();
    Trace lTrace = this.getFallbackTrace();
    String lMessage = "Hello Fallback-Trace";

    // Check level detection.
    Level lCurrentLevel = Level.OFF;
    rootLogger.setLevel(lCurrentLevel);
    assertFalse(lTrace.isErrorEnabled(), "Level TRACE is enabled.");

    lCurrentLevel = Level.SEVERE;
    rootLogger.setLevel(lCurrentLevel);
    assertTrue(lTrace.isErrorEnabled(), "Level TRACE is not enabled.");

    // Test writing of traces
    lTrace.error(lMessage);
    assertEquals(lMessage, handler.getLastLogRecord().getMessage(), "Wrong trace message");
    handler.clear();

    // Test trace object
    TraceableObject lTraceableObject = new TraceableObject();
    rootLogger.setLevel(Level.OFF);
    lTrace.errorObject(lTraceableObject);
    assertNull(handler.getLastLogRecord(), "No log record expected due to log level.");

    rootLogger.setLevel(Level.SEVERE);
    lTrace.errorObject(lTraceableObject);
    assertEquals(lTraceableObject.toString(), handler.getLastLogRecord().getMessage(), "Wrong trace message");
    handler.clear();

    // Try to trace null instead of an object.
    lTrace.errorObject(null);
    assertNull(handler.getLastLogRecord(), "No log record expected due to null value that was passed.");

    // Test tracing of exceptions
    IllegalArgumentException lIllegalArgumentException = new IllegalArgumentException("IllegalArgumentException");
    lTrace.error(lIllegalArgumentException);
    LogRecord lLastLogRecord = handler.getLastLogRecord();
    assertEquals(lIllegalArgumentException.getMessage(), lLastLogRecord.getMessage());
    assertEquals(Level.SEVERE, lLastLogRecord.getLevel());
    assertEquals(lIllegalArgumentException, lLastLogRecord.getThrown());
    handler.clear();

    lTrace.error(lIllegalArgumentException, false);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals(lIllegalArgumentException.getMessage(), lLastLogRecord.getMessage());
    assertEquals(Level.SEVERE, lLastLogRecord.getLevel());
    assertEquals(lIllegalArgumentException, lLastLogRecord.getThrown());
    handler.clear();

    lTrace.error(lIllegalArgumentException, true);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals(lIllegalArgumentException.getMessage(), lLastLogRecord.getMessage());
    assertEquals(Level.SEVERE, lLastLogRecord.getLevel());
    assertEquals(null, lLastLogRecord.getThrown());
    handler.clear();

    ApplicationException lAppException = new TestApplicationException(new ErrorCode(123456789, TraceLevel.INFO));
    lTrace.error(lAppException);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals("Error-Code: 123456789", lLastLogRecord.getMessage());
    assertEquals(Level.SEVERE, lLastLogRecord.getLevel());
    assertEquals(lAppException, lLastLogRecord.getThrown());
    handler.clear();

    lTrace.error(lAppException, false);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals("Error-Code: 123456789", lLastLogRecord.getMessage());
    assertEquals(Level.SEVERE, lLastLogRecord.getLevel());
    assertEquals(lAppException, lLastLogRecord.getThrown());
    handler.clear();

    lTrace.error(lAppException, true);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals("Error-Code: 123456789", lLastLogRecord.getMessage());
    assertEquals(Level.SEVERE, lLastLogRecord.getLevel());
    assertEquals(null, lLastLogRecord.getThrown());
    handler.clear();

    // Trace object that has an object formatter.
    ObjectFormatter<?> lObjectFormatter = TraceConfiguration.getInstance().getObjectFormatter(ApplicationInfo.class);
    assertNotNull(lObjectFormatter, "ObjectFormatter for ApplicationInfo expected.");
    lTrace.errorObject(ApplicationInfo.UNKNOWN_APPLICATION);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals(ApplicationInfo.UNKNOWN_APPLICATION.toString(), handler.getLastLogRecord().getMessage(),
        "Wrong trace message");
    handler.clear();

    // Test tracing of exceptions.
    Exception lException = new IllegalArgumentException();
    rootLogger.setLevel(Level.OFF);
    lTrace.error(lMessage, lException);
    assertNull(handler.getLastLogRecord(), "No log record expected due to log level.");

    rootLogger.setLevel(Level.SEVERE);
    lTrace.error(lMessage, lException);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals(lMessage, handler.getLastLogRecord().getMessage(), "Wrong trace message");
    assertEquals(lException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown");

    lTrace.error(lMessage, null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals(lMessage, handler.getLastLogRecord().getMessage(), "Wrong trace message");
    assertNull(handler.getLastLogRecord().getThrown(), "null as thrown expected");
    handler.clear();

    lTrace.error((String) null, (Throwable) null);
    assertNull(handler.getLastLogRecord(), "No log record expected due to null value that was passed.");

    lTrace.error(null, lException);
    assertNull(handler.getLastLogRecord().getMessage(), "No trace message expected.");
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals(lException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown");
    handler.clear();

    // Test tracing of parameterized messages. In case of fallback traces we do not have a message repo in the back.
    rootLogger.setLevel(Level.OFF);
    lTrace.error(messageID, "Hello", " ", "World!");
    assertNull(handler.getLastLogRecord(), "No log record expected due to log level.");

    rootLogger.setLevel(Level.SEVERE);
    lTrace.error(messageID, "Hello", " ", "World!");
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [Hello,  , World!]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");

    lTrace.error(messageID, lObject);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [" + lObject + "]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");

    lTrace.error(messageID);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");

    lTrace.error(messageID, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");

    lTrace.error(null, "Hello", " ", "World!");
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: ? Details: [Hello,  , World!]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");

    lTrace.error(null, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: ?", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    handler.clear();

    // Test tracing of parameterized messages plus exception
    rootLogger.setLevel(Level.OFF);
    lTrace.error(messageID, lException, "Hello", " ", "World!");
    assertNull(handler.getLastLogRecord(), "No log record expected due to log level.");

    rootLogger.setLevel(Level.SEVERE);
    lTrace.error(messageID, lException, "Hello", " ", "World!");
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [Hello,  , World!]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");
    assertEquals(lException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.error(messageID, lException, lObject);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [" + lObject + "]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");
    assertEquals(lException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.error(messageID, lException, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    assertEquals(lException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.error(messageID, (Throwable) null, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    assertNull(handler.getLastLogRecord().getThrown(), "No exception expected.");
    handler.clear();

    // Test tracing of parameterized messages plus ApplicationException
    ApplicationException lApplicationException =
        new TestApplicationException(new ErrorCode(123456789, TraceLevel.WARN));
    rootLogger.setLevel(Level.OFF);
    lTrace.error(messageID, lApplicationException, "Hello", " ", "World!");
    assertNull(handler.getLastLogRecord(), "No log record expected due to log level.");

    rootLogger.setLevel(Level.SEVERE);
    lTrace.error(messageID, lApplicationException, "Hello", " ", "World!");
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [Hello,  , World!]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");
    assertEquals(lApplicationException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.error(messageID, lApplicationException, lObject);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [" + lObject + "]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");
    assertEquals(lApplicationException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.error(messageID, lApplicationException, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    assertEquals(lApplicationException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.error(messageID, (ApplicationException) null, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    assertNull(handler.getLastLogRecord().getThrown(), "No exception expected.");
    handler.clear();

    // Test tracing of parameterized messages plus SystemException
    SystemException lSystemException = new TestSystemException(new ErrorCode(123456789, TraceLevel.WARN));
    rootLogger.setLevel(Level.OFF);
    lTrace.error(messageID, lSystemException, "Hello", " ", "World!");
    assertNull(handler.getLastLogRecord(), "No log record expected due to log level.");

    rootLogger.setLevel(Level.SEVERE);
    lTrace.error(messageID, lSystemException, "Hello", " ", "World!");
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [Hello,  , World!]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");
    assertEquals(lSystemException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.error(messageID, lSystemException, lObject);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [" + lObject + "]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");
    assertEquals(lSystemException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.error(messageID, lSystemException, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    assertEquals(lSystemException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.error(messageID, (SystemException) null, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    assertNull(handler.getLastLogRecord().getThrown(), "No exception expected.");
    handler.clear();
  }

  @Test
  @Order(35)
  public void testFatalLevelTrace( ) {
    Object lObject = new Object();
    Trace lTrace = this.getFallbackTrace();
    String lMessage = "Hello Fallback-Trace";

    // Check level detection.
    Level lCurrentLevel = Level.OFF;
    rootLogger.setLevel(lCurrentLevel);
    assertFalse(lTrace.isFatalEnabled(), "Level TRACE is enabled.");

    lCurrentLevel = Level.SEVERE;
    rootLogger.setLevel(lCurrentLevel);
    assertTrue(lTrace.isFatalEnabled(), "Level TRACE is not enabled.");

    // Test writing of traces
    lTrace.fatal(lMessage);
    assertEquals(lMessage, handler.getLastLogRecord().getMessage(), "Wrong trace message");
    handler.clear();

    // Test trace object
    TraceableObject lTraceableObject = new TraceableObject();
    rootLogger.setLevel(Level.OFF);
    lTrace.fatalObject(lTraceableObject);
    assertNull(handler.getLastLogRecord(), "No log record expected due to log level.");

    rootLogger.setLevel(Level.SEVERE);
    lTrace.fatalObject(lTraceableObject);
    assertEquals(lTraceableObject.toString(), handler.getLastLogRecord().getMessage(), "Wrong trace message");
    handler.clear();

    // Try to trace null instead of an object.
    lTrace.fatalObject(null);
    assertNull(handler.getLastLogRecord(), "No log record expected due to null value that was passed.");

    // Test tracing of exceptions
    IllegalArgumentException lIllegalArgumentException = new IllegalArgumentException("IllegalArgumentException");
    lTrace.fatal(lIllegalArgumentException);
    LogRecord lLastLogRecord = handler.getLastLogRecord();
    assertEquals(lIllegalArgumentException.getMessage(), lLastLogRecord.getMessage());
    assertEquals(Level.SEVERE, lLastLogRecord.getLevel());
    assertEquals(lIllegalArgumentException, lLastLogRecord.getThrown());
    handler.clear();

    lTrace.fatal(lIllegalArgumentException, false);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals(lIllegalArgumentException.getMessage(), lLastLogRecord.getMessage());
    assertEquals(Level.SEVERE, lLastLogRecord.getLevel());
    assertEquals(lIllegalArgumentException, lLastLogRecord.getThrown());
    handler.clear();

    lTrace.fatal(lIllegalArgumentException, true);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals(lIllegalArgumentException.getMessage(), lLastLogRecord.getMessage());
    assertEquals(Level.SEVERE, lLastLogRecord.getLevel());
    assertEquals(null, lLastLogRecord.getThrown());
    handler.clear();

    ApplicationException lAppException = new TestApplicationException(new ErrorCode(123456789, TraceLevel.INFO));
    lTrace.fatal(lAppException);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals("Error-Code: 123456789", lLastLogRecord.getMessage());
    assertEquals(Level.SEVERE, lLastLogRecord.getLevel());
    assertEquals(lAppException, lLastLogRecord.getThrown());
    handler.clear();

    lTrace.fatal(lAppException, false);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals("Error-Code: 123456789", lLastLogRecord.getMessage());
    assertEquals(Level.SEVERE, lLastLogRecord.getLevel());
    assertEquals(lAppException, lLastLogRecord.getThrown());
    handler.clear();

    lTrace.fatal(lAppException, true);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals("Error-Code: 123456789", lLastLogRecord.getMessage());
    assertEquals(Level.SEVERE, lLastLogRecord.getLevel());
    assertEquals(null, lLastLogRecord.getThrown());
    handler.clear();

    // Trace object that has an object formatter.
    ObjectFormatter<?> lObjectFormatter = TraceConfiguration.getInstance().getObjectFormatter(ApplicationInfo.class);
    assertNotNull(lObjectFormatter, "ObjectFormatter for ApplicationInfo expected.");
    lTrace.fatalObject(ApplicationInfo.UNKNOWN_APPLICATION);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals(ApplicationInfo.UNKNOWN_APPLICATION.toString(), handler.getLastLogRecord().getMessage(),
        "Wrong trace message");
    handler.clear();

    // Test tracing of exceptions.
    Exception lException = new IllegalArgumentException();
    rootLogger.setLevel(Level.OFF);
    lTrace.fatal(lMessage, lException);
    assertNull(handler.getLastLogRecord(), "No log record expected due to log level.");

    rootLogger.setLevel(Level.SEVERE);
    lTrace.fatal(lMessage, lException);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals(lMessage, handler.getLastLogRecord().getMessage(), "Wrong trace message");
    assertEquals(lException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown");

    lTrace.fatal(lMessage, null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals(lMessage, handler.getLastLogRecord().getMessage(), "Wrong trace message");
    assertNull(handler.getLastLogRecord().getThrown(), "null as thrown expected");
    handler.clear();

    lTrace.fatal((String) null, (Throwable) null);
    assertNull(handler.getLastLogRecord(), "No log record expected due to null value that was passed.");

    lTrace.fatal(null, lException);
    assertNull(handler.getLastLogRecord().getMessage(), "No trace message expected.");
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals(lException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown");
    handler.clear();

    // Test tracing of parameterized messages. In case of fallback traces we do not have a message repo in the back.
    rootLogger.setLevel(Level.OFF);
    lTrace.fatal(messageID, "Hello", " ", "World!");
    assertNull(handler.getLastLogRecord(), "No log record expected due to log level.");

    rootLogger.setLevel(Level.SEVERE);
    lTrace.fatal(messageID, "Hello", " ", "World!");
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [Hello,  , World!]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");

    lTrace.fatal(messageID, lObject);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [" + lObject + "]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");

    lTrace.fatal(messageID);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");

    lTrace.fatal(messageID, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");

    lTrace.fatal(null, "Hello", " ", "World!");
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: ? Details: [Hello,  , World!]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");

    lTrace.fatal(null, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: ?", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    handler.clear();

    // Test tracing of parameterized messages plus exception
    rootLogger.setLevel(Level.OFF);
    lTrace.fatal(messageID, lException, "Hello", " ", "World!");
    assertNull(handler.getLastLogRecord(), "No log record expected due to log level.");

    rootLogger.setLevel(Level.SEVERE);
    lTrace.fatal(messageID, lException, "Hello", " ", "World!");
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [Hello,  , World!]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");
    assertEquals(lException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.fatal(messageID, lException, lObject);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [" + lObject + "]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");
    assertEquals(lException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.fatal(messageID, lException, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    assertEquals(lException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.fatal(messageID, (Throwable) null, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    assertNull(handler.getLastLogRecord().getThrown(), "No exception expected.");
    handler.clear();

    // Test tracing of parameterized messages plus ApplicationException
    ApplicationException lApplicationException =
        new TestApplicationException(new ErrorCode(123456789, TraceLevel.WARN));
    rootLogger.setLevel(Level.OFF);
    lTrace.fatal(messageID, lApplicationException, "Hello", " ", "World!");
    assertNull(handler.getLastLogRecord(), "No log record expected due to log level.");

    rootLogger.setLevel(Level.SEVERE);
    lTrace.fatal(messageID, lApplicationException, "Hello", " ", "World!");
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [Hello,  , World!]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");
    assertEquals(lApplicationException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.fatal(messageID, lApplicationException, lObject);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [" + lObject + "]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");
    assertEquals(lApplicationException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.fatal(messageID, lApplicationException, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    assertEquals(lApplicationException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.fatal(messageID, (ApplicationException) null, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    assertNull(handler.getLastLogRecord().getThrown(), "No exception expected.");
    handler.clear();

    // Test tracing of parameterized messages plus SystemException
    SystemException lSystemException = new TestSystemException(new ErrorCode(123456789, TraceLevel.WARN));
    rootLogger.setLevel(Level.OFF);
    lTrace.fatal(messageID, lSystemException, "Hello", " ", "World!");
    assertNull(handler.getLastLogRecord(), "No log record expected due to log level.");

    rootLogger.setLevel(Level.SEVERE);
    lTrace.fatal(messageID, lSystemException, "Hello", " ", "World!");
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [Hello,  , World!]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");
    assertEquals(lSystemException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.fatal(messageID, lSystemException, lObject);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711 Details: [" + lObject + "]", handler.getLastLogRecord().getMessage(),
        "Wrong log entry.");
    assertEquals(lSystemException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.fatal(messageID, lSystemException, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    assertEquals(lSystemException, handler.getLastLogRecord().getThrown(), "Unexpected exception as thrown.");
    handler.clear();

    lTrace.fatal(messageID, (SystemException) null, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected due to log level.");
    assertEquals("Error-Code: 4711", handler.getLastLogRecord().getMessage(), "Wrong log entry.");
    assertNull(handler.getLastLogRecord().getThrown(), "No exception expected.");
    handler.clear();
  }

  @Test
  @Order(40)
  public void testWriteTraceOperation( ) {

    // Enable all log levels
    rootLogger.setLevel(Level.ALL);

    this.writeTrace(TraceLevel.TRACE, Level.FINEST);
    this.writeTrace(TraceLevel.DEBUG, Level.FINE);
    this.writeTrace(TraceLevel.INFO, Level.INFO);
    this.writeTrace(TraceLevel.WARN, Level.WARNING);
    this.writeTrace(TraceLevel.ERROR, Level.SEVERE);
    this.writeTrace(TraceLevel.FATAL, Level.SEVERE);
  }

  private void writeTrace( TraceLevel pTraceLevel, Level pLevel ) {
    Trace lTrace = this.getFallbackTrace();
    MessageID lMessageID = new MessageID(1, pTraceLevel);
    Object lObject = new Object();

    lTrace.write(lMessageID, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected.");
    assertEquals(pLevel, handler.getLastLogRecord().getLevel(), "Message written with wrong log level.");
    assertEquals("Error-Code: 1", handler.getLastLogRecord().getMessage(), "Wrong message logged.");
    assertNull(handler.getLastLogRecord().getThrown(), "No expection expected");

    lTrace.write(lMessageID, "Hello", " World!");
    assertNotNull(handler.getLastLogRecord(), "Log record expected.");
    assertEquals(pLevel, handler.getLastLogRecord().getLevel(), "Message written with wrong log level.");
    assertEquals("Error-Code: 1 Details: [Hello,  World!]", handler.getLastLogRecord().getMessage(),
        "Wrong message logged.");
    assertNull(handler.getLastLogRecord().getThrown(), "No expection expected");

    lTrace.write(lMessageID, lObject);
    assertNotNull(handler.getLastLogRecord(), "Log record expected.");
    assertEquals(pLevel, handler.getLastLogRecord().getLevel(), "Message written with wrong log level.");
    assertEquals("Error-Code: 1 Details: [" + lObject + "]", handler.getLastLogRecord().getMessage(),
        "Wrong message logged.");
    assertNull(handler.getLastLogRecord().getThrown(), "No expection expected");

    lTrace.write(lMessageID, (Object) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected.");
    assertEquals(pLevel, handler.getLastLogRecord().getLevel(), "Message written with wrong log level.");
    assertEquals("Error-Code: 1 Details: [null]", handler.getLastLogRecord().getMessage(), "Wrong message logged.");
    assertNull(handler.getLastLogRecord().getThrown(), "No expection expected");

    lTrace.write(null, lObject);
    assertNotNull(handler.getLastLogRecord(), "Log record expected.");
    assertEquals(Level.SEVERE, handler.getLastLogRecord().getLevel(), "Message written with wrong log level.");
    assertEquals("Error-Code: ? Details: [" + lObject + "]", handler.getLastLogRecord().getMessage(),
        "Wrong message logged.");
    assertNull(handler.getLastLogRecord().getThrown(), "No expection expected");

    lTrace.write(null, "Hello", " World!");
    assertNotNull(handler.getLastLogRecord(), "Log record expected.");
    assertEquals(Level.SEVERE, handler.getLastLogRecord().getLevel(), "Message written with wrong log level.");
    assertEquals("Error-Code: ? Details: [Hello,  World!]", handler.getLastLogRecord().getMessage(),
        "Wrong message logged.");

    lTrace.write(null, new TestApplicationException(new ErrorCode(999, pTraceLevel)), "Hello", " World!");
    assertNotNull(handler.getLastLogRecord(), "Log record expected.");
    assertEquals(Level.SEVERE, handler.getLastLogRecord().getLevel(), "Message written with wrong log level.");
    assertEquals("Error-Code: ? Details: [Hello,  World!]", handler.getLastLogRecord().getMessage(),
        "Wrong message logged.");

    // Test tracing of java exceptions.
    Exception lException = new IllegalArgumentException();
    lTrace.write(lMessageID, lException, "Hello", " World!");
    assertNotNull(handler.getLastLogRecord(), "Log record expected.");
    assertEquals(pLevel, handler.getLastLogRecord().getLevel(), "Message written with wrong log level.");
    assertEquals("Error-Code: 1 Details: [Hello,  World!]", handler.getLastLogRecord().getMessage(),
        "Wrong message logged.");
    assertEquals(lException, handler.getLastLogRecord().getThrown(), "Unexpected exception");

    lTrace.write(lMessageID, lException, lObject);
    assertNotNull(handler.getLastLogRecord(), "Log record expected.");
    assertEquals(pLevel, handler.getLastLogRecord().getLevel(), "Message written with wrong log level.");
    assertEquals("Error-Code: 1 Details: [" + lObject + "]", handler.getLastLogRecord().getMessage(),
        "Wrong message logged.");
    assertEquals(lException, handler.getLastLogRecord().getThrown(), "Unexpected exception");

    lTrace.write(lMessageID, (Throwable) null, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected.");
    assertEquals(pLevel, handler.getLastLogRecord().getLevel(), "Message written with wrong log level.");
    assertEquals("Error-Code: 1", handler.getLastLogRecord().getMessage(), "Wrong message logged.");
    assertNull(handler.getLastLogRecord().getThrown(), "No expection expected");

    lTrace.write(null, (Throwable) null, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected.");
    assertEquals(Level.SEVERE, handler.getLastLogRecord().getLevel(), "Message written with wrong log level.");
    assertEquals("Error-Code: ?", handler.getLastLogRecord().getMessage(), "Wrong message logged.");
    assertNull(handler.getLastLogRecord().getThrown(), "No expection expected");

    // Test tracing of application exceptions.
    ApplicationException lApplicationException = new TestApplicationException(new ErrorCode(999, pTraceLevel));
    lTrace.write(lMessageID, lApplicationException, "Hello", " World!");
    assertNotNull(handler.getLastLogRecord(), "Log record expected.");
    assertEquals(pLevel, handler.getLastLogRecord().getLevel(), "Message written with wrong log level.");
    assertEquals("Error-Code: 1 Details: [Hello,  World!]", handler.getLastLogRecord().getMessage(),
        "Wrong message logged.");
    assertEquals(lApplicationException, handler.getLastLogRecord().getThrown(), "Unexpected exception");

    lTrace.write(lMessageID, lApplicationException, lObject);
    assertNotNull(handler.getLastLogRecord(), "Log record expected.");
    assertEquals(pLevel, handler.getLastLogRecord().getLevel(), "Message written with wrong log level.");
    assertEquals("Error-Code: 1 Details: [" + lObject + "]", handler.getLastLogRecord().getMessage(),
        "Wrong message logged.");
    assertEquals(lApplicationException, handler.getLastLogRecord().getThrown(), "Unexpected exception");

    lTrace.write(lMessageID, (Throwable) null, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected.");
    assertEquals(pLevel, handler.getLastLogRecord().getLevel(), "Message written with wrong log level.");
    assertEquals("Error-Code: 1", handler.getLastLogRecord().getMessage(), "Wrong message logged.");
    assertNull(handler.getLastLogRecord().getThrown(), "No expection expected");

    lTrace.write(null, (Throwable) null, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected.");
    assertEquals(Level.SEVERE, handler.getLastLogRecord().getLevel(), "Message written with wrong log level.");
    assertEquals("Error-Code: ?", handler.getLastLogRecord().getMessage(), "Wrong message logged.");
    assertNull(handler.getLastLogRecord().getThrown(), "No expection expected");

    // Test tracing of system exceptions.
    SystemException lSystemException = new TestSystemException(new ErrorCode(999, pTraceLevel));
    lTrace.write(lMessageID, lSystemException, "Hello", " World!");
    assertNotNull(handler.getLastLogRecord(), "Log record expected.");
    assertEquals(pLevel, handler.getLastLogRecord().getLevel(), "Message written with wrong log level.");
    assertEquals("Error-Code: 1 Details: [Hello,  World!]", handler.getLastLogRecord().getMessage(),
        "Wrong message logged.");
    assertEquals(lSystemException, handler.getLastLogRecord().getThrown(), "Unexpected exception");

    lTrace.write(lMessageID, lSystemException, lObject);
    assertNotNull(handler.getLastLogRecord(), "Log record expected.");
    assertEquals(pLevel, handler.getLastLogRecord().getLevel(), "Message written with wrong log level.");
    assertEquals("Error-Code: 1 Details: [" + lObject + "]", handler.getLastLogRecord().getMessage(),
        "Wrong message logged.");
    assertEquals(lSystemException, handler.getLastLogRecord().getThrown(), "Unexpected exception");

    lTrace.write(lMessageID, (Throwable) null, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected.");
    assertEquals(pLevel, handler.getLastLogRecord().getLevel(), "Message written with wrong log level.");
    assertEquals("Error-Code: 1", handler.getLastLogRecord().getMessage(), "Wrong message logged.");
    assertNull(handler.getLastLogRecord().getThrown(), "No expection expected");

    lTrace.write(null, (Throwable) null, (String[]) null);
    assertNotNull(handler.getLastLogRecord(), "Log record expected.");
    assertEquals(Level.SEVERE, handler.getLastLogRecord().getLevel(), "Message written with wrong log level.");
    assertEquals("Error-Code: ?", handler.getLastLogRecord().getMessage(), "Wrong message logged.");
    assertNull(handler.getLastLogRecord().getThrown(), "No expection expected");
  }

  @Test
  @Order(50)
  public void testIsLevelEnabled( ) {
    Trace lTrace = this.getFallbackTrace();
    rootLogger.setLevel(Level.ALL);
    assertEquals(TraceLevel.TRACE, lTrace.getLevel());
    assertTrue(lTrace.isLevelEnabled(TraceLevel.TRACE), "Level TRACE is not enabled.");
    assertTrue(lTrace.isLevelEnabled(TraceLevel.DEBUG), "Level DEBUG is not enabled.");
    assertTrue(lTrace.isLevelEnabled(TraceLevel.INFO), "Level INFO is not enabled.");
    assertTrue(lTrace.isLevelEnabled(TraceLevel.WARN), "Level WARN is not enabled.");
    assertTrue(lTrace.isLevelEnabled(TraceLevel.ERROR), "Level ERROR is not enabled.");
    assertTrue(lTrace.isLevelEnabled(TraceLevel.FATAL), "Level FATAL is not enabled.");
    assertTrue(lTrace.isLevelEnabled(null), "Level 'null' is not enabled.");

    rootLogger.setLevel(Level.FINE);
    assertEquals(TraceLevel.DEBUG, lTrace.getLevel());
    assertFalse(lTrace.isLevelEnabled(TraceLevel.TRACE), "Level TRACE is enabled.");
    assertTrue(lTrace.isLevelEnabled(TraceLevel.DEBUG), "Level DEBUG is not enabled.");
    assertTrue(lTrace.isLevelEnabled(TraceLevel.INFO), "Level INFO is not enabled.");
    assertTrue(lTrace.isLevelEnabled(TraceLevel.WARN), "Level WARN is not enabled.");
    assertTrue(lTrace.isLevelEnabled(TraceLevel.ERROR), "Level ERROR is not enabled.");
    assertTrue(lTrace.isLevelEnabled(TraceLevel.FATAL), "Level FATAL is not enabled.");
    assertTrue(lTrace.isLevelEnabled(null), "Level 'null' is not enabled.");

    rootLogger.setLevel(Level.INFO);
    assertEquals(TraceLevel.INFO, lTrace.getLevel());
    assertFalse(lTrace.isLevelEnabled(TraceLevel.TRACE), "Level TRACE is enabled.");
    assertFalse(lTrace.isLevelEnabled(TraceLevel.DEBUG), "Level DEBUG is enabled.");
    assertTrue(lTrace.isLevelEnabled(TraceLevel.INFO), "Level INFO is not enabled.");
    assertTrue(lTrace.isLevelEnabled(TraceLevel.WARN), "Level WARN is not enabled.");
    assertTrue(lTrace.isLevelEnabled(TraceLevel.ERROR), "Level ERROR is not enabled.");
    assertTrue(lTrace.isLevelEnabled(TraceLevel.FATAL), "Level FATAL is not enabled.");
    assertTrue(lTrace.isLevelEnabled(null), "Level 'null' is not enabled.");

    rootLogger.setLevel(Level.WARNING);
    assertEquals(TraceLevel.WARN, lTrace.getLevel());
    assertFalse(lTrace.isLevelEnabled(TraceLevel.TRACE), "Level TRACE is enabled.");
    assertFalse(lTrace.isLevelEnabled(TraceLevel.DEBUG), "Level DEBUG is enabled.");
    assertFalse(lTrace.isLevelEnabled(TraceLevel.INFO), "Level INFO is enabled.");
    assertTrue(lTrace.isLevelEnabled(TraceLevel.WARN), "Level WARN is not enabled.");
    assertTrue(lTrace.isLevelEnabled(TraceLevel.ERROR), "Level ERROR is not enabled.");
    assertTrue(lTrace.isLevelEnabled(TraceLevel.FATAL), "Level FATAL is not enabled.");
    assertTrue(lTrace.isLevelEnabled(null), "Level 'null' is not enabled.");

    rootLogger.setLevel(Level.SEVERE);
    assertEquals(TraceLevel.ERROR, lTrace.getLevel());
    assertFalse(lTrace.isLevelEnabled(TraceLevel.TRACE), "Level TRACE is enabled.");
    assertFalse(lTrace.isLevelEnabled(TraceLevel.DEBUG), "Level DEBUG is enabled.");
    assertFalse(lTrace.isLevelEnabled(TraceLevel.INFO), "Level INFO is enabled.");
    assertFalse(lTrace.isLevelEnabled(TraceLevel.WARN), "Level WARN is enabled.");
    assertTrue(lTrace.isLevelEnabled(TraceLevel.ERROR), "Level ERROR is not enabled.");
    assertTrue(lTrace.isLevelEnabled(TraceLevel.FATAL), "Level FATAL is not enabled.");
    assertTrue(lTrace.isLevelEnabled(null), "Level 'null' is not enabled.");

    rootLogger.setLevel(Level.OFF);
    assertEquals(null, lTrace.getLevel());
    assertFalse(lTrace.isLevelEnabled(TraceLevel.TRACE), "Level TRACE is enabled.");
    assertFalse(lTrace.isLevelEnabled(TraceLevel.DEBUG), "Level DEBUG is enabled.");
    assertFalse(lTrace.isLevelEnabled(TraceLevel.INFO), "Level INFO is enabled.");
    assertFalse(lTrace.isLevelEnabled(TraceLevel.WARN), "Level WARN is enabled.");
    assertFalse(lTrace.isLevelEnabled(TraceLevel.ERROR), "Level ERROR is enabled.");
    assertFalse(lTrace.isLevelEnabled(TraceLevel.FATAL), "Level FATAL is enabled.");
    assertFalse(lTrace.isLevelEnabled(null), "Level 'null' is enabled.");
  }

  @Test
  @Order(60)
  public void testContextStack( ) {
    // As the fallback trace implementation does not support context stacking the tests here are very simple
    Trace lTrace = this.getFallbackTrace();
    ComponentID lComponentID = new ComponentID("X-Fun", "com.anaptecs.jeaf.xfun.test.trace");
    ContextStackElement lContextStackElement = new ContextStackElement("X-Fun", lComponentID);

    lTrace.newContextStack(lContextStackElement);
    lTrace.pushContextStackElement(lContextStackElement);
    assertNull(lTrace.popContextStackElement());
  }

  private FallbackTraceImpl getFallbackTrace( ) {
    FallbackTraceProviderImpl lProvider = new FallbackTraceProviderImpl();
    Trace lTrace = lProvider.getCurrentTrace();
    return (FallbackTraceImpl) lTrace;
  }

  @Test
  @Order(70)
  public void testWriteOperations( ) {
    Trace lTrace = this.getFallbackTrace();
    // Test tracing of exceptions
    IllegalArgumentException lIllegalArgumentException = new IllegalArgumentException("IllegalArgumentException");
    lTrace.write(lIllegalArgumentException);
    LogRecord lLastLogRecord = handler.getLastLogRecord();
    assertEquals(lIllegalArgumentException.getMessage(), lLastLogRecord.getMessage());
    assertEquals(Level.SEVERE, lLastLogRecord.getLevel());
    assertEquals(lIllegalArgumentException, lLastLogRecord.getThrown());
    handler.clear();

    lTrace.write(lIllegalArgumentException, false);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals(lIllegalArgumentException.getMessage(), lLastLogRecord.getMessage());
    assertEquals(Level.SEVERE, lLastLogRecord.getLevel());
    assertEquals(lIllegalArgumentException, lLastLogRecord.getThrown());
    handler.clear();

    lTrace.write(lIllegalArgumentException, true);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals(lIllegalArgumentException.getMessage(), lLastLogRecord.getMessage());
    assertEquals(Level.SEVERE, lLastLogRecord.getLevel());
    assertEquals(null, lLastLogRecord.getThrown());
    handler.clear();

    ApplicationException lAppException = new TestApplicationException(new ErrorCode(123456789, TraceLevel.INFO));
    lTrace.write(lAppException);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals("Error-Code: 123456789", lLastLogRecord.getMessage());
    assertEquals(Level.INFO, lLastLogRecord.getLevel());
    assertEquals(lAppException, lLastLogRecord.getThrown());
    handler.clear();

    lTrace.write(lAppException, false);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals("Error-Code: 123456789", lLastLogRecord.getMessage());
    assertEquals(Level.INFO, lLastLogRecord.getLevel());
    assertEquals(lAppException, lLastLogRecord.getThrown());
    handler.clear();

    lTrace.write(lAppException, true);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals("Error-Code: 123456789", lLastLogRecord.getMessage());
    assertEquals(Level.INFO, lLastLogRecord.getLevel());
    assertEquals(null, lLastLogRecord.getThrown());
    handler.clear();
  }

  @Test
  public void testLogMethods( ) {
    MyTraceImpl lTrace = new MyTraceImpl("TRACE");
    lTrace.log(TraceLevel.INFO, "Hello");
    LogRecord lLastLogRecord = handler.getLastLogRecord();
    assertEquals("Hello", lLastLogRecord.getMessage());
    assertEquals(Level.INFO, lLastLogRecord.getLevel());
    handler.clear();

    lTrace.log(null, "Hello");
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals("Hello", lLastLogRecord.getMessage());
    assertEquals(Level.SEVERE, lLastLogRecord.getLevel());
    handler.clear();

    ErrorCode lErrorCode = new ErrorCode(123456789, TraceLevel.INFO);
    lTrace.log(TraceLevel.WARN, lErrorCode, new IllegalArgumentException(), "");
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals("Error-Code: 123456789 Details: []", lLastLogRecord.getMessage());
    assertEquals(Level.WARNING, lLastLogRecord.getLevel());
    handler.clear();

    lTrace.log(null, lErrorCode, new IllegalArgumentException(), "");
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals("Error-Code: 123456789 Details: []", lLastLogRecord.getMessage());
    assertEquals(Level.SEVERE, lLastLogRecord.getLevel());
    handler.clear();

    lTrace.log(TraceLevel.WARN, lErrorCode, new IllegalArgumentException(), (Object) null);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals("Error-Code: 123456789 Details: [null]", lLastLogRecord.getMessage());
    assertEquals(Level.WARNING, lLastLogRecord.getLevel());
    handler.clear();

    lTrace.log(null, lErrorCode, new IllegalArgumentException(), (Object) null);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals("Error-Code: 123456789 Details: [null]", lLastLogRecord.getMessage());
    assertEquals(Level.SEVERE, lLastLogRecord.getLevel());
    handler.clear();

    lTrace.log(TraceLevel.WARN, new IllegalArgumentException(), false);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals(null, lLastLogRecord.getMessage());
    assertEquals(Level.WARNING, lLastLogRecord.getLevel());
    handler.clear();

    lTrace.log(null, new IllegalArgumentException("Hello"), true);
    lLastLogRecord = handler.getLastLogRecord();
    assertEquals("Hello", lLastLogRecord.getMessage());
    assertEquals(Level.SEVERE, lLastLogRecord.getLevel());
    handler.clear();
  }

}

class MyTraceImpl extends FallbackTraceImpl {
  MyTraceImpl( String pLoggerName ) {
    super(pLoggerName);
  }

  @Override
  public void log( TraceLevel pTraceLevel, MessageID pMessageID, Throwable pThrowable, String... pMessageParameters ) {
    super.log(pTraceLevel, pMessageID, pThrowable, pMessageParameters);
  }

  @Override
  public void log( TraceLevel pTraceLevel, MessageID pMessageID, Throwable pThrowable, Object... pMessageParameters ) {
    super.log(pTraceLevel, pMessageID, pThrowable, pMessageParameters);
  }

  @Override
  public void log( TraceLevel pTraceLevel, Object pObject ) {
    super.log(pTraceLevel, pObject);
  }

  @Override
  public void log( TraceLevel pTraceLevel, Throwable pThrowable, boolean pSuppressStacktrace ) {
    super.log(pTraceLevel, pThrowable, pSuppressStacktrace);
  }
}
