/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.fallback.trace;

import java.util.Arrays;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.anaptecs.jeaf.xfun.api.messages.MessageID;
import com.anaptecs.jeaf.xfun.api.trace.AbstractTraceImpl;
import com.anaptecs.jeaf.xfun.api.trace.ContextStackElement;
import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;
import com.anaptecs.jeaf.xfun.bootstrap.Assert;

/**
 * Class implements a Fallback trace implementation based on the JDK Logger implementation (java.util.logging). As the
 * name says this implementation is only supposed to be used as a fallback if the standard logging implementation can
 * not be used for any reason.
 * 
 * @author JEAF Development Team
 * @version 1.0
 */
public class FallbackTraceImpl extends AbstractTraceImpl {
  /**
   * List of trace levels as they need to be order to find out the current level.
   */
  private static final TraceLevel[] TRACE_LEVELS = new TraceLevel[] { TraceLevel.FATAL, TraceLevel.ERROR,
    TraceLevel.WARN, TraceLevel.INFO, TraceLevel.DEBUG, TraceLevel.TRACE };

  /**
   * Reference to JDK Logger implementation. The reference is never null since it is set in the class' constructor.
   */
  private final Logger logger;

  /**
   * Initialize a new Trace-Object for the passed component.
   * 
   * @param pLoggerName Name of the logger for which the new Trace instance will be created. The parameter must not be
   * null.
   */
  protected FallbackTraceImpl( String pLoggerName ) {
    // Get logger implementation.
    System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT.%1$tL] [%4$-7s] %5$s %6$s%n");
    ConsoleHandler lConsoleHandler = new ConsoleHandler();
    lConsoleHandler.setLevel(Level.FINEST);
    lConsoleHandler.setFormatter(new SimpleFormatter());
    logger = Logger.getLogger(pLoggerName);
    logger.setLevel(Level.FINEST);
  }

  /**
   * Method logs the passed string and / or exception with the passed trace level using JDK logging implementation.
   * 
   * @param pTraceLevel Trace level of the log entry. The parameter must not be null.
   * @param pMessage Message that should be traced. The parameter may be null.
   * @param pThrowable Exception that is related to the message. The parameter may be null.
   */
  protected void log( TraceLevel pTraceLevel, String pMessage, Throwable pThrowable ) {
    // Check parameters.
    Assert.assertNotNull(pTraceLevel, "pTraceLevel");

    // Ensure that trace level is enabled.
    if (this.isLevelEnabled(pTraceLevel) == true) {
      if (pMessage != null || pThrowable != null) {
        logger.log(this.toLevel(pTraceLevel), pMessage, pThrowable);
      }
    }
  }

  /**
   * Method creates a new message based on the locale to use for tracing and the passed parameters.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no message will
   * be created.
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   * @return String Created message. The method never returns null.
   */
  protected String getMessage( MessageID pMessageID, String[] pMessageParameters ) {
    StringBuilder lBuilder = new StringBuilder();
    lBuilder.append("Error-Code: ");
    if (pMessageID != null) {
      lBuilder.append(pMessageID.getLocalizationID());
    }
    else {
      lBuilder.append("?");
    }

    if (pMessageParameters != null && pMessageParameters.length > 0) {
      lBuilder.append(" Details: ");
      lBuilder.append(Arrays.toString(pMessageParameters));
    }
    return lBuilder.toString();
  }

  /**
   * In order to support to know the current context JEAF's trace mechanism supports a so called context stack. Based on
   * this context stack we are able to know the current context and can provide the current trace object.
   * 
   * Method creates a new context stack with the passed object as first element on it.
   * 
   * @param pContextStackElement First element for new context stack. The parameter must not be null.
   */
  @Override
  public void newContextStack( ContextStackElement pContextStackElement ) {
    // Nothing to do in case of fallback implementation.
  }

  /**
   * In order to support to know the current context JEAF's trace mechanism supports a so called context stack. Based on
   * this context stack we are able to know the current context and can provide the current trace object.
   * 
   * Method pushes the passed object to the existing context stack.
   * 
   * @param pContextStackElement New element for the context stack. The parameter must not be null.
   */
  @Override
  public void pushContextStackElement( ContextStackElement pContextStackElement ) {
    // Nothing to do in case of fallback implementation.
  }

  /**
   * In order to support to know the current context JEAF's trace mechanism supports a so called context stack. Based on
   * this context stack we are able to know the current context and can provide the current trace object.
   * 
   * Method pops the top element of the context stack.
   * 
   * @return {@link ContextStackElement} Object that was the top element on the context stack or null if the stack is
   * empty.
   */
  @Override
  public ContextStackElement popContextStackElement( ) {
    return null;
  }

  /**
   * Method writes an so called emergency trace. Emergency traces are needed in case that we run into fatal problems
   * during initialization. Thus this method should be implemented in a way that it requires no environment be be set
   * up.
   * 
   * @param pMessage Message that should be written. The parameter may be null.
   * @param pThrowable Exception that occurred. The parameter may be null.
   * @param pTraceLevel Level with which the message should be traced. The parameter may be null. In this case
   * {@link TraceLevel#FATAL} will be used.
   */
  @Override
  public void writeEmergencyTrace( String pMessage, Throwable pThrowable, TraceLevel pTraceLevel ) {
    Logger lLogger = Logger.getLogger("JEAF_EMERGENCY");
    Level lLevel = this.toLevel(pTraceLevel);
    lLogger.log(lLevel, pMessage, pThrowable);
  }

  /**
   * Method check if the passed trace level is enabled on this trace object.
   * 
   * @param pTraceLevel Trace level that should be checked. The parameter must not be null.
   * @return boolean The method returns true if the passed trace level is enabled on this trace object and false in all
   * other cases.
   */
  @Override
  public boolean isLevelEnabled( TraceLevel pTraceLevel ) {
    Level lLevel = this.toLevel(pTraceLevel);
    return logger.isLoggable(lLevel);
  }

  /**
   * Method transforms the passed JEAF X-Fun trace level into the matching level from JDK logging.
   * 
   * @param pTraceLevel Trace level that should be converted. The parameter may be null. In this case
   * {@link Level#SEVERE} will be returned.
   * @return {@link Level} Matching log level from JDK. The method never returns null.
   */
  private Level toLevel( TraceLevel pTraceLevel ) {
    Level lLevel;
    if (pTraceLevel == null) {
      pTraceLevel = TraceLevel.FATAL;
    }

    // Convert X-Fun TraceLevel into JDK Level
    switch (pTraceLevel) {
      case TRACE:
        lLevel = Level.FINEST;
        break;

      case DEBUG:
        lLevel = Level.FINE;
        break;

      case INFO:
        lLevel = Level.INFO;
        break;

      case WARN:
        lLevel = Level.WARNING;
        break;

      case ERROR:
        lLevel = Level.SEVERE;
        break;

      // Level FATAL is the same as SEVERE when using JDK logging.
      case FATAL:
        lLevel = Level.SEVERE;
        break;

      default:
        lLevel = Level.SEVERE;
        break;
    }
    return lLevel;
  }

  /**
   * Method returns the trace level that is currently enabled.
   * 
   * @return {@link TraceLevel} Trace level that is currently enabled. If tracing is completely disabled then this
   * method returns null.
   */
  @Override
  public TraceLevel getLevel( ) {
    // Test all trace levels until we find one that is enabled.
    TraceLevel lCurrentLevel = null;
    for (TraceLevel lNext : TRACE_LEVELS) {
      if (this.isLevelEnabled(lNext) == true) {
        lCurrentLevel = lNext;
      }
    }

    // Method returns null if tracing is disabled completely.
    return lCurrentLevel;
  }
}
