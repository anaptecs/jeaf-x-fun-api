/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.trace;

import com.anaptecs.jeaf.xfun.annotations.TraceObjectFormatter;
import com.anaptecs.jeaf.xfun.api.errorhandling.ApplicationException;
import com.anaptecs.jeaf.xfun.api.errorhandling.ErrorCode;
import com.anaptecs.jeaf.xfun.api.errorhandling.ExceptionInfoProvider;
import com.anaptecs.jeaf.xfun.api.errorhandling.SystemException;
import com.anaptecs.jeaf.xfun.api.messages.MessageID;
import com.anaptecs.jeaf.xfun.bootstrap.Assert;

/**
 * This class intended to be used as base class for implementations of interface {@link Trace}. It provides
 * implementations of all methods that can be implemented independent of a concrete tracing framework.
 * 
 * @author JEAF Development Team
 * @version 1.0
 */
public abstract class AbstractTraceImpl implements Trace {
  /**
   * Initialize a new Trace-Object for the passed component.
   */
  protected AbstractTraceImpl( ) {
  }

  /**
   * Method logs the passed string and / or exception with the passed trace level.
   * 
   * @param pTraceLevel Trace level of the log entry. The parameter must not be null.
   * @param pMessage Message that should be traced. The parameter may be null.
   * @param pThrowable Exception that is related to the message. The parameter may be null.
   */
  protected abstract void log( TraceLevel pTraceLevel, String pMessage, Throwable pThrowable );

  /**
   * Method creates a new message based on the locale to use for tracing and the passed parameters.
   * 
   * @param pMessageID ID of the message that should be traced. The parameter may be null. In this case no message will
   * be created.
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   * @return String Created message. The method may return null.
   */
  protected abstract String getMessage( MessageID pMessageID, String[] pMessageParameters );

  /**
   * Method logs the passed message.
   * 
   * @param pTraceLevel Trace level that should be used for the created log entry. The parameter must not be null.
   * @param pMessageID MessageID of the message that should be written. The parameter may be null, but should as this
   * will result in incomplete log messages.
   * @param pThrowable Exception that should be logged. The parameter may be null.
   * @param pMessageParameters Message parameters that should be used to parameterize the message. The parameter may be
   * null.
   */
  protected void log( TraceLevel pTraceLevel, MessageID pMessageID, Throwable pThrowable,
      String... pMessageParameters ) {

    // Check parameters.
    if (pTraceLevel == null) {
      pTraceLevel = TraceLevel.ERROR;
    }

    // Ensure that trace level is enabled.
    if (this.isLevelEnabled(pTraceLevel) == true) {
      String lMessage = this.getMessage(pMessageID, pMessageParameters);
      this.log(pTraceLevel, lMessage, pThrowable);
    }
  }

  /**
   * Method logs the passed message.
   * 
   * @param pTraceLevel Trace level that should be used for the created log entry. The parameter must not be null.
   * @param pMessageID MessageID of the message that should be written. The parameter may be null, but should as this
   * will result in incomplete log messages.
   * @param pThrowable Exception that should be logged. The parameter may be null.
   * @param pMessageParameters Objects that are used to parameterize the trace message. The matching
   * {@link ObjectFormatter} will be used to convert the passed objects into Strings. The parameter may be null.
   */
  protected void log( TraceLevel pTraceLevel, MessageID pMessageID, Throwable pThrowable,
      Object... pMessageParameters ) {

    // Check parameters.
    if (pTraceLevel == null) {
      pTraceLevel = TraceLevel.ERROR;
    }

    // In order to avoid garbage we ensure that log level is really enabled.
    if (this.isLevelEnabled(pTraceLevel) == true) {
      // Convert objects to String.
      String[] lStrings = this.toStrings(pMessageParameters, pTraceLevel);
      this.log(pTraceLevel, pMessageID, pThrowable, lStrings);
    }
  }

  /**
   * Method logs the passed message. The trace level will be resolved from the passed message ID.
   * 
   * @param pMessageID MessageID of the message that should be written. The parameter may be null, but should as this
   * will result in incomplete log messages.
   * @param pThrowable Exception that should be logged. The parameter may be null.
   * @param pMessageParameters Message parameters that should be used to parameterize the message. The parameter may be
   * null.
   */
  protected void log( MessageID pMessageID, Throwable pThrowable, String... pMessageParameters ) {
    // Try to resolve trace level from passed MessageID.
    TraceLevel lTraceLevel;
    if (pMessageID != null) {
      lTraceLevel = pMessageID.getTraceLevel();
    }
    // As no MessageID was passed, we use ERROR.
    else {
      lTraceLevel = TraceLevel.ERROR;
    }

    // Log message.
    this.log(lTraceLevel, pMessageID, pThrowable, pMessageParameters);
  }

  /**
   * Method logs the passed message. The trace level will be resolved from the passed message ID.
   * 
   * @param pMessageID MessageID of the message that should be written. The parameter may be null, but should as this
   * will result in incomplete log messages.
   * @param pThrowable Exception that should be logged. The parameter may be null.
   * @param pMessageParameters Objects that are used to parameterize the trace message. The matching
   * {@link ObjectFormatter} will be used to convert the passed objects into Strings. The parameter may be null.
   */
  protected void log( MessageID pMessageID, Throwable pThrowable, Object... pMessageParameters ) {
    // Try to resolve trace level from passed MessageID.
    TraceLevel lTraceLevel;
    if (pMessageID != null) {
      lTraceLevel = pMessageID.getTraceLevel();
    }
    // As no MessageID was passed, we use ERROR.
    else {
      lTraceLevel = TraceLevel.ERROR;
    }

    // Log message.
    this.log(lTraceLevel, pMessageID, pThrowable, pMessageParameters);
  }

  /**
   * 
   * @param pTraceLevel Trace level that should be used for the created log entry. The parameter must not be null.
   * @param pObject Object that should be logged. The matching {@link ObjectFormatter} will be used to convert the
   * passed object into a String. The parameter may be null.
   */
  protected void log( TraceLevel pTraceLevel, Object pObject ) {
    // Check parameters.
    if (pTraceLevel == null) {
      pTraceLevel = TraceLevel.ERROR;
    }

    // Ensure that trace level is enabled.
    if (pObject != null && this.isLevelEnabled(pTraceLevel) == true) {
      String lString = this.toString(pObject, pTraceLevel);
      this.log(pTraceLevel, lString, null);
    }
  }

  /**
   * Method traces the passed exception.
   * 
   * @param pTraceLevel Trace level that should be used.
   * @param pThrowable Exception that should be logged. The parameter may be null. In this case no trace will be done.
   * @param pSuppressStacktrace Parameter defines if the stack trace should be suppressed or not.
   */
  protected void log( TraceLevel pTraceLevel, Throwable pThrowable, boolean pSuppressStacktrace ) {
    // Check parameters.
    if (pTraceLevel == null) {
      pTraceLevel = TraceLevel.ERROR;
    }
    if (pThrowable != null) {
      // We have some details about the exception
      if (pThrowable instanceof ExceptionInfoProvider) {
        // Gather information about the exception.
        ExceptionInfoProvider lExceptionInfoProvider = (ExceptionInfoProvider) pThrowable;
        ErrorCode lErrorCode = lExceptionInfoProvider.getErrorCode();
        String[] lMessageParameters = lExceptionInfoProvider.getMessageParameters();

        // Trace information without stack trace.
        if (pSuppressStacktrace == true) {
          this.log(pTraceLevel, lErrorCode, null, lMessageParameters);
        }
        // Trace information including stack trace.
        else {
          this.log(pTraceLevel, lErrorCode, pThrowable, lMessageParameters);
        }
      }
      // Let's write standard error message to log.
      else {
        // Trace information without stack trace.
        if (pSuppressStacktrace == true) {
          this.log(pTraceLevel, pThrowable.getMessage(), null);
        }
        // Trace information including stack trace.
        else {
          this.log(pTraceLevel, pThrowable.getMessage(), pThrowable);
        }
      }
    }
  }

  /**
   * Method transforms the passed object into a string using the matching {@link ObjectFormatter}.
   * 
   * @param pObject Object that should be converted into a String. The parameter may be null.
   * @param pTraceLevel Trace level that should be used to convert the object into a String. The parameter must not be
   * null.
   * @return {@link String} String that was created out of the passed object. The method returns null if null is passed.
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  private String toString( Object pObject, TraceLevel pTraceLevel ) {
    // Check parameter. Only trace level needs to be checked.
    Assert.assertNotNull(pTraceLevel, "pTraceLevel");

    String lString;
    if (pObject != null) {
      // Try to find object formatter.
      ObjectFormatter lObjectFormatter = TraceConfiguration.getInstance().getObjectFormatter(pObject.getClass());

      // Use found formatter
      if (lObjectFormatter != null) {
        lString = lObjectFormatter.formatObject(pObject, pTraceLevel);
      }
      // Use toString() as fallback.
      else {
        lString = pObject.toString();
      }
    }
    // Passed object is null, so we will return null.
    else {
      lString = null;
    }
    return lString;
  }

  /**
   * Method transforms the passed object array into a string array using the matching {@link ObjectFormatter}.
   * 
   * @param pObject Object that should be converted into a String. The parameter may be null.
   * @param pTraceLevel Trace level that should be used to convert the object into a String. The parameter must not be
   * null.
   * @return {@link String} String array that was created out of the passed objects. The method never returns null. If
   * an element of the array is null then the string array will also have null on this index.
   */
  private String[] toStrings( Object[] pObjects, TraceLevel pTraceLevel ) {
    String[] lStrings;
    if (pObjects != null) {
      lStrings = new String[pObjects.length];
      for (int i = 0; i < pObjects.length; i++) {
        lStrings[i] = this.toString(pObjects[i], pTraceLevel);
      }
    }
    else {
      lStrings = null;
    }
    // Return created strings
    return lStrings;
  }

  /**
   * Method writes an so called emergency trace. Emergency traces are needed in case that we run into fatal problems
   * during initialization. Thus this method should be implemented in a way that it requires no environment be be set
   * up.
   * 
   * @param pMessage Message that should be written. The parameter may be null.
   * @param pThrowable Exception that occurred. The parameter may be null.
   */
  @Override
  public void writeEmergencyTrace( String pMessage, Throwable pThrowable ) {
    this.writeEmergencyTrace(pMessage, pThrowable, TraceLevel.FATAL);
  }

  /**
   * Method writes a so called init info. Init info means that this message will be written during the very first steps
   * of initialization. This means the implementations must not require or execute any of them.
   * 
   * @param pMessage Message that should be written. The parameter may be null. In this case no message will be written.
   * @param pTraceLevel Level that should be used for tracing. The parameter may be null. In this case
   * {@link TraceLevel#FATAL} will be used.
   */
  @Override
  public void writeInitInfo( String pMessage, TraceLevel pTraceLevel ) {
    if (pMessage != null) {
      if (pTraceLevel == null) {
        pTraceLevel = TraceLevel.FATAL;
      }

      this.log(pTraceLevel, pMessage, null);
    }
  }

  /**
   * Method checks whether the trace level "TRACE" is enabled.
   * 
   * @return boolean The method returns true if the trace level "TRACE" is enabled otherwise false.
   */
  @Override
  public boolean isTraceEnabled( ) {
    return this.isLevelEnabled(TraceLevel.TRACE);
  }

  /**
   * Method traces the passed message through the underlying tracing framework with trace level TRACE. In order to
   * support internationalization of trace messages please use method
   * <code>trace(MessageID pMessageID, String[] pMessageParameters)</code> instead.
   * 
   * @param pMessage Message that should be traced with trace level TRACE. The parameter may be null. In this case no
   * trace information will be written.
   * @see #trace(MessageID, String[])
   */
  @Override
  public void trace( String pMessage ) {
    this.log(TraceLevel.TRACE, pMessage, null);
  }

  /**
   * Method traces the passed object through the underlying tracing framework with trace level TRACE. The object will be
   * formatted according to the defined {@link TraceObjectFormatter}. If no specific formatter is defined then
   * {@link Object#toString()} will be used.
   * 
   * @param pObject Object that should be traced with trace level TRACE. The parameter may be null. In this case no
   * trace information will be written.
   * @see TraceObjectFormatter#formatObject(Object, TraceLevel)
   */
  @Override
  public void traceObject( Object pObject ) {
    this.log(TraceLevel.TRACE, pObject);
  }

  /**
   * Method traces the passed exception through the underlying tracing frameworks with trace level TRACE.
   * 
   * @param pThrowable Exception that should be traced. The parameter may be null. In this case nothing will be traced.
   */
  @Override
  public void trace( Throwable pThrowable ) {
    this.trace(pThrowable, false);
  }

  /**
   * Method traces the passed exception through the underlying tracing frameworks with trace level TRACE.
   * 
   * @param pThrowable Exception that should be traced. The parameter may be null. In this case nothing will be traced.
   * @param pSuppressStacktrace Parameter controls if the stack trace of the exception should be suppressed or not.
   */
  @Override
  public void trace( Throwable pThrowable, boolean pSuppressStacktrace ) {
    this.log(TraceLevel.TRACE, pThrowable, pSuppressStacktrace);
  }

  /**
   * Method traces the passed Throwable object through the underlying tracing framework with trace level TRACE. In order
   * to support internationalization of trace messages please use method
   * <code>trace(MessageID, String[], Throwable)</code> or
   * <code>trace(MessageID, String[], ExceptionInfoProvider)</code> instead.
   * 
   * @param pMessage Message that should be traced with trace level TRACE. The parameter may be null. In this case no
   * trace information will be written.
   * @param pThrowable Throwable that should be traced with trace level TRACE. The parameter may be null. In this case
   * no trace information will be written.
   * @see #trace(MessageID, Throwable, String[])
   * @see #trace(MessageID, ApplicationException, String[])
   */
  @Override
  public void trace( String pMessage, Throwable pThrowable ) {
    this.log(TraceLevel.TRACE, pMessage, pThrowable);
  }

  /**
   * Method traces a message with trace level TRACE that will be created from the passed message id and the passed
   * parameters.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  @Override
  public void trace( MessageID pMessageID, String... pMessageParameters ) {
    this.log(TraceLevel.TRACE, pMessageID, null, pMessageParameters);
  }

  /**
   * Method traces a message with trace level TRACE that will be created from the passed message id and the passed
   * parameters.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pMessageParameters Objects that are used to parameterize the trace message. The matching
   * {@link ObjectFormatter} will be used to convert the passed objects into Strings. The parameter may be null.
   */
  @Override
  public void trace( MessageID pMessageID, Object... pMessageParameters ) {
    this.log(TraceLevel.TRACE, pMessageID, null, pMessageParameters);
  }

  /**
   * Method traces a message with trace level TRACE that will be created from the passed message id and the passed
   * parameters together with the passed Throwable object. If the passed message id and the Throwable object are null
   * then no message will be traced, since this would result in an empty message.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pThrowable Throwable object that should be traced together with the passed message. The parameter may be
   * null.
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  @Override
  public void trace( MessageID pMessageID, Throwable pThrowable, String... pMessageParameters ) {
    this.log(TraceLevel.TRACE, pMessageID, pThrowable, pMessageParameters);
  }

  /**
   * Method traces a message with trace level TRACE that will be created from the passed message id and the passed
   * parameters together with the passed Throwable object. If the passed message id and the Throwable object are null
   * then no message will be traced, since this would result in an empty message.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pThrowable Throwable object that should be traced together with the passed message. The parameter may be
   * null.
   * @param pMessageParameters Objects that are used to parameterize the trace message. The matching
   * {@link ObjectFormatter} will be used to convert the passed objects into Strings. The parameter may be null.
   */
  @Override
  public void trace( MessageID pMessageID, Throwable pThrowable, Object... pMessageParameters ) {
    this.log(TraceLevel.TRACE, pMessageID, pThrowable, pMessageParameters);
  }

  /**
   * Method traces a message with trace level TRACE that will be created from the passed message id and the passed
   * parameters together with the passed ApplicationException. If the passed message id and the application exception
   * are null then no message will be traced, since this would result in an empty message.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pApplicationException Application exception that should be traced together with the passed message. The
   * parameter may be null.
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  @Override
  public void trace( MessageID pMessageID, ApplicationException pApplicationException, String... pMessageParameters ) {
    this.log(TraceLevel.TRACE, pMessageID, pApplicationException, pMessageParameters);
  }

  /**
   * Method traces a message with trace level TRACE that will be created from the passed message id and the passed
   * parameters together with the passed ApplicationException. If the passed message id and the application exception
   * are null then no message will be traced, since this would result in an empty message.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pApplicationException Application exception that should be traced together with the passed message. The
   * parameter may be null.
   * @param pMessageParameters Objects that are used to parameterize the trace message. The matching
   * {@link ObjectFormatter} will be used to convert the passed objects into Strings. The parameter may be null.
   */
  @Override
  public void trace( MessageID pMessageID, ApplicationException pApplicationException, Object... pMessageParameters ) {
    this.log(TraceLevel.TRACE, pMessageID, pApplicationException, pMessageParameters);
  }

  /**
   * Method traces a message with trace level TRACE that will be created from the passed message id and the passed
   * parameters together with the passed SystemException. If the passed message id and the system exception are null
   * then no message will be traced, since this would result in an empty message.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pSystemException System exception that should be traced together with the passed message. The parameter may
   * be null.
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  @Override
  public void trace( MessageID pMessageID, SystemException pSystemException, String... pMessageParameters ) {
    this.log(TraceLevel.TRACE, pMessageID, pSystemException, pMessageParameters);
  }

  /**
   * Method traces a message with trace level TRACE that will be created from the passed message id and the passed
   * parameters together with the passed SystemException. If the passed message id and the system exception are null
   * then no message will be traced, since this would result in an empty message.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pSystemException System exception that should be traced together with the passed message. The parameter may
   * be null.
   * @param pMessageParameters Objects that are used to parameterize the trace message. The matching
   * {@link ObjectFormatter} will be used to convert the passed objects into Strings. The parameter may be null.
   */
  @Override
  public void trace( MessageID pMessageID, SystemException pSystemException, Object... pMessageParameters ) {
    this.log(TraceLevel.TRACE, pMessageID, pSystemException, pMessageParameters);
  }

  /**
   * Method checks whether the trace level "DEBUG" is enabled.
   * 
   * @return boolean The method returns true if the trace level "DEBUG" is enabled otherwise false.
   */
  @Override
  public boolean isDebugEnabled( ) {
    return this.isLevelEnabled(TraceLevel.DEBUG);
  }

  /**
   * Method traces the passed message through the underlying tracing framework with trace level DEBUG. In order to
   * support internationalization of trace messages please use method
   * <code>debug(MessageID pMessageID, String[] pMessageParameters)</code> instead.
   * 
   * @param pMessage Message that should be traced with trace level DEBUG. The parameter may be null. In this case no
   * trace information will be written.
   * @see #debug(MessageID, String[])
   */
  @Override
  public void debug( String pMessage ) {
    this.log(TraceLevel.DEBUG, pMessage, null);
  }

  /**
   * Method traces the passed object through the underlying tracing framework with trace level DEBUG. The object will be
   * formatted according to the defined {@link TraceObjectFormatter}. If no specific formatter is defined then
   * {@link Object#toString()} will be used.
   * 
   * @param pObject Object that should be traced with trace level DEBUG. The parameter may be null. In this case no
   * trace information will be written.
   * @see TraceObjectFormatter#formatObject(Object, TraceLevel)
   */
  @Override
  public void debugObject( Object pObject ) {
    this.log(TraceLevel.DEBUG, pObject);
  }

  /**
   * Method traces the passed exception through the underlying tracing frameworks with trace level DEBUG.
   * 
   * @param pThrowable Exception that should be traced. The parameter may be null. In this case nothing will be traced.
   */
  @Override
  public void debug( Throwable pThrowable ) {
    this.debug(pThrowable, false);
  }

  /**
   * Method traces the passed exception through the underlying tracing frameworks with trace level DEBUG.
   * 
   * @param pThrowable Exception that should be traced. The parameter may be null. In this case nothing will be traced.
   * @param pSuppressStacktrace Parameter controls if the stack trace of the exception should be suppressed or not.
   */
  @Override
  public void debug( Throwable pThrowable, boolean pSuppressStacktrace ) {
    this.log(TraceLevel.DEBUG, pThrowable, pSuppressStacktrace);
  }

  /**
   * Method traces the passed Throwable object through the underlying tracing framework with trace level DEBUG. In order
   * to support internationalization of trace messages please use method
   * <code>debug(MessageID, String[], Throwable)</code> or
   * <code>debug(MessageID, String[], ExceptionInfoProvider)</code> instead.
   * 
   * @param pMessage Message that should be traced with trace level DEBUG. The parameter may be null. In this case no
   * trace information will be written.
   * @param pThrowable Throwable that should be traced with trace level DEBUG. The parameter may be null. In this case
   * no trace information will be written.
   * @see #debug(MessageID, Throwable, String[])
   * @see #debug(MessageID, ApplicationException, String[])
   */
  @Override
  public void debug( String pMessage, Throwable pThrowable ) {
    this.log(TraceLevel.DEBUG, pMessage, pThrowable);
  }

  /**
   * Method traces a message with trace level DEBUG that will be created from the passed message id and the passed
   * parameters.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  @Override
  public void debug( MessageID pMessageID, String... pMessageParameters ) {
    this.log(TraceLevel.DEBUG, pMessageID, null, pMessageParameters);
  }

  /**
   * Method traces a message with trace level DEBUG that will be created from the passed message id and the passed
   * parameters.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pMessageParameters Objects that are used to parameterize the trace message. The matching
   * {@link ObjectFormatter} will be used to convert the passed objects into Strings. The parameter may be null.
   */
  @Override
  public void debug( MessageID pMessageID, Object... pMessageParameters ) {
    this.log(TraceLevel.DEBUG, pMessageID, null, pMessageParameters);
  }

  /**
   * Method traces a message with trace level DEBUG that will be created from the passed message id and the passed
   * parameters together with the passed Throwable object. If the passed message id and the Throwable object are null
   * then no message will be traced, since this would result in an empty message.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pThrowable Throwable object that should be traced together with the passed message. The parameter may be
   * null.
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  @Override
  public void debug( MessageID pMessageID, Throwable pThrowable, String... pMessageParameters ) {
    this.log(TraceLevel.DEBUG, pMessageID, pThrowable, pMessageParameters);
  }

  /**
   * Method traces a message with trace level DEBUG that will be created from the passed message id and the passed
   * parameters together with the passed Throwable object. If the passed message id and the Throwable object are null
   * then no message will be traced, since this would result in an empty message.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pThrowable Throwable object that should be traced together with the passed message. The parameter may be
   * null.
   * @param pMessageParameters Objects that are used to parameterize the trace message. The matching
   * {@link ObjectFormatter} will be used to convert the passed objects into Strings. The parameter may be null.
   */
  @Override
  public void debug( MessageID pMessageID, Throwable pThrowable, Object... pMessageParameters ) {
    this.log(TraceLevel.DEBUG, pMessageID, pThrowable, pMessageParameters);
  }

  /**
   * Method traces a message with trace level DEBUG that will be created from the passed message id and the passed
   * parameters together with the passed ApplicationException. If the passed message id and the application exception
   * are null then no message will be traced, since this would result in an empty message.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pApplicationException Application exception that should be traced together with the passed message. The
   * parameter may be null.
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  @Override
  public void debug( MessageID pMessageID, ApplicationException pApplicationException, String... pMessageParameters ) {
    this.log(TraceLevel.DEBUG, pMessageID, pApplicationException, pMessageParameters);
  }

  /**
   * Method traces a message with trace level DEBUG that will be created from the passed message id and the passed
   * parameters together with the passed ApplicationException. If the passed message id and the application exception
   * are null then no message will be traced, since this would result in an empty message.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pApplicationException Application exception that should be traced together with the passed message. The
   * parameter may be null.
   * @param pMessageParameters Objects that are used to parameterize the trace message. The matching
   * {@link ObjectFormatter} will be used to convert the passed objects into Strings. The parameter may be null.
   */
  @Override
  public void debug( MessageID pMessageID, ApplicationException pApplicationException, Object... pMessageParameters ) {
    this.log(TraceLevel.DEBUG, pMessageID, pApplicationException, pMessageParameters);
  }

  /**
   * Method traces a message with trace level DEBUG that will be created from the passed message id and the passed
   * parameters together with the passed SystemException. If the passed message id and the system exception are null
   * then no message will be traced, since this would result in an empty message.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pSystemException System exception that should be traced together with the passed message. The parameter may
   * be null.
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  @Override
  public void debug( MessageID pMessageID, SystemException pSystemException, String... pMessageParameters ) {
    this.log(TraceLevel.DEBUG, pMessageID, pSystemException, pMessageParameters);
  }

  /**
   * Method traces a message with trace level DEBUG that will be created from the passed message id and the passed
   * parameters together with the passed SystemException. If the passed message id and the system exception are null
   * then no message will be traced, since this would result in an empty message.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pSystemException System exception that should be traced together with the passed message. The parameter may
   * be null.
   * @param pMessageParameters Objects that are used to parameterize the trace message. The matching
   * {@link ObjectFormatter} will be used to convert the passed objects into Strings. The parameter may be null.
   */
  @Override
  public void debug( MessageID pMessageID, SystemException pSystemException, Object... pMessageParameters ) {
    this.log(TraceLevel.DEBUG, pMessageID, pSystemException, pMessageParameters);
  }

  /**
   * Method checks whether the trace level "INFO" is enabled.
   * 
   * @return boolean The method returns true if the trace level "INFO" is enabled otherwise false.
   */
  @Override
  public boolean isInfoEnabled( ) {
    return this.isLevelEnabled(TraceLevel.INFO);
  }

  /**
   * Method traces the passed message through the underlying tracing framework with trace level INFO. In order to
   * support internationalization of trace messages please use method
   * <code>info(MessageID pMessageID, String[] pMessageParameters)</code> instead.
   * 
   * @param pMessage Message that should be traced with trace level INFO. The parameter may be null. In this case no
   * trace information will be written.
   * @see #info(MessageID, String[])
   */
  @Override
  public void info( String pMessage ) {
    this.log(TraceLevel.INFO, pMessage, null);
  }

  /**
   * Method traces the passed object through the underlying tracing framework with trace level INFO. The object will be
   * formatted according to the defined {@link TraceObjectFormatter}. If no specific formatter is defined then
   * {@link Object#toString()} will be used.
   * 
   * @param pObject Object that should be traced with trace level INFO. The parameter may be null. In this case no trace
   * information will be written.
   * @see TraceObjectFormatter#formatObject(Object, TraceLevel)
   */
  @Override
  public void infoObject( Object pObject ) {
    this.log(TraceLevel.INFO, pObject);
  }

  /**
   * Method traces the passed exception through the underlying tracing frameworks with trace level INFO.
   * 
   * @param pThrowable Exception that should be traced. The parameter may be null. In this case nothing will be traced.
   */
  @Override
  public void info( Throwable pThrowable ) {
    this.info(pThrowable, false);
  }

  /**
   * Method traces the passed exception through the underlying tracing frameworks with trace level INFO.
   * 
   * @param pThrowable Exception that should be traced. The parameter may be null. In this case nothing will be traced.
   * @param pSuppressStacktrace Parameter controls if the stack trace of the exception should be suppressed or not.
   */
  @Override
  public void info( Throwable pThrowable, boolean pSuppressStacktrace ) {
    this.log(TraceLevel.INFO, pThrowable, pSuppressStacktrace);
  }

  /**
   * Method traces the passed Throwable object through the underlying tracing framework with trace level INFO. In order
   * to support internationalization of trace messages please use method
   * <code>info(MessageID, String[], Throwable)</code> or <code>info(MessageID, String[], ExceptionInfoProvider)</code>
   * instead.
   * 
   * @param pMessage Message that should be traced with trace level INFO. The parameter may be null. In this case no
   * trace information will be written.
   * @param pThrowable Throwable that should be traced with trace level INFO. The parameter may be null. In this case no
   * trace information will be written.
   * @see #info(MessageID, Throwable, String[])
   * @see #info(MessageID, ApplicationException, String[])
   */
  @Override
  public void info( String pMessage, Throwable pThrowable ) {
    this.log(TraceLevel.INFO, pMessage, pThrowable);
  }

  /**
   * Method traces a message with trace level INFO that will be created from the passed message id and the passed
   * parameters.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  @Override
  public void info( MessageID pMessageID, String... pMessageParameters ) {
    this.log(TraceLevel.INFO, pMessageID, null, pMessageParameters);
  }

  /**
   * Method traces a message with trace level INFO that will be created from the passed message id and the passed
   * parameters.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pMessageParameters Objects that are used to parameterize the trace message. The matching
   * {@link ObjectFormatter} will be used to convert the passed objects into Strings. The parameter may be null.
   */
  @Override
  public void info( MessageID pMessageID, Object... pMessageParameters ) {
    this.log(TraceLevel.INFO, pMessageID, null, pMessageParameters);
  }

  /**
   * Method traces a message with trace level INFO that will be created from the passed message id and the passed
   * parameters together with the passed Throwable object. If the passed message id and the Throwable object are null
   * then no message will be traced, since this would result in an empty message.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pThrowable Throwable object that should be traced together with the passed message. The parameter may be
   * null.
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  @Override
  public void info( MessageID pMessageID, Throwable pThrowable, String... pMessageParameters ) {
    this.log(TraceLevel.INFO, pMessageID, pThrowable, pMessageParameters);
  }

  /**
   * Method traces a message with trace level INFO that will be created from the passed message id and the passed
   * parameters together with the passed Throwable object. If the passed message id and the Throwable object are null
   * then no message will be traced, since this would result in an empty message.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pThrowable Throwable object that should be traced together with the passed message. The parameter may be
   * null.
   * @param pMessageParameters Objects that are used to parameterize the trace message. The matching
   * {@link ObjectFormatter} will be used to convert the passed objects into Strings. The parameter may be null.
   */
  @Override
  public void info( MessageID pMessageID, Throwable pThrowable, Object... pMessageParameters ) {
    this.log(TraceLevel.INFO, pMessageID, pThrowable, pMessageParameters);
  }

  /**
   * Method traces a message with trace level INFO that will be created from the passed message id and the passed
   * parameters together with the passed ApplicationException. If the passed message id and the application exception
   * are null then no message will be traced, since this would result in an empty message.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pApplicationException Application exception that should be traced together with the passed message. The
   * parameter may be null.
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  @Override
  public void info( MessageID pMessageID, ApplicationException pApplicationException, String... pMessageParameters ) {
    this.log(TraceLevel.INFO, pMessageID, pApplicationException, pMessageParameters);
  }

  /**
   * Method traces a message with trace level INFO that will be created from the passed message id and the passed
   * parameters together with the passed ApplicationException. If the passed message id and the application exception
   * are null then no message will be traced, since this would result in an empty message.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pApplicationException Application exception that should be traced together with the passed message. The
   * parameter may be null.
   * @param pMessageParameters Objects that are used to parameterize the trace message. The matching
   * {@link ObjectFormatter} will be used to convert the passed objects into Strings. The parameter may be null.
   */
  @Override
  public void info( MessageID pMessageID, ApplicationException pApplicationException, Object... pMessageParameters ) {
    this.log(TraceLevel.INFO, pMessageID, pApplicationException, pMessageParameters);
  }

  /**
   * Method traces a message with trace level INFO that will be created from the passed message id and the passed
   * parameters together with the passed SystemException. If the passed message id and the system exception are null
   * then no message will be traced, since this would result in an empty message.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pSystemException System exception that should be traced together with the passed message. The parameter may
   * be null.
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  @Override
  public void info( MessageID pMessageID, SystemException pSystemException, String... pMessageParameters ) {
    this.log(TraceLevel.INFO, pMessageID, pSystemException, pMessageParameters);
  }

  /**
   * Method traces a message with trace level INFO that will be created from the passed message id and the passed
   * parameters together with the passed SystemException. If the passed message id and the system exception are null
   * then no message will be traced, since this would result in an empty message.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pSystemException System exception that should be traced together with the passed message. The parameter may
   * be null.
   * @param pMessageParameters Objects that are used to parameterize the trace message. The matching
   * {@link ObjectFormatter} will be used to convert the passed objects into Strings. The parameter may be null.
   */
  @Override
  public void info( MessageID pMessageID, SystemException pSystemException, Object... pMessageParameters ) {
    this.log(TraceLevel.INFO, pMessageID, pSystemException, pMessageParameters);
  }

  /**
   * Method checks whether the trace level "WARN" is enabled.
   * 
   * @return boolean The method returns true if the trace level "WARN" is enabled otherwise false.
   */
  @Override
  public boolean isWarnEnabled( ) {
    return this.isLevelEnabled(TraceLevel.WARN);
  }

  /**
   * Method traces the passed message through the underlying tracing framework with trace level WARN. In order to
   * support internationalization of trace messages please use method
   * <code>warn(MessageID pMessageID, String[] pMessageParameters)</code> instead.
   * 
   * @param pMessage Message that should be traced with trace level WARN. The parameter may be null. In this case no
   * trace information will be written.
   * @see #warn(MessageID, String[])
   */
  @Override
  public void warn( String pMessage ) {
    this.log(TraceLevel.WARN, pMessage, null);
  }

  /**
   * Method traces the passed object through the underlying tracing framework with trace level WARN. The object will be
   * formatted according to the defined {@link TraceObjectFormatter}. If no specific formatter is defined then
   * {@link Object#toString()} will be used.
   * 
   * @param pObject Object that should be traced with trace level WARN. The parameter may be null. In this case no trace
   * information will be written.
   * @see TraceObjectFormatter#formatObject(Object, TraceLevel)
   */
  @Override
  public void warnObject( Object pObject ) {
    this.log(TraceLevel.WARN, pObject);
  }

  /**
   * Method traces the passed exception through the underlying tracing frameworks with trace level WARN.
   * 
   * @param pThrowable Exception that should be traced. The parameter may be null. In this case nothing will be traced.
   */
  @Override
  public void warn( Throwable pThrowable ) {
    this.warn(pThrowable, false);
  }

  /**
   * Method traces the passed exception through the underlying tracing frameworks with trace level WARN.
   * 
   * @param pThrowable Exception that should be traced. The parameter may be null. In this case nothing will be traced.
   * @param pSuppressStacktrace Parameter controls if the stack trace of the exception should be suppressed or not.
   */
  @Override
  public void warn( Throwable pThrowable, boolean pSuppressStacktrace ) {
    this.log(TraceLevel.WARN, pThrowable, pSuppressStacktrace);
  }

  /**
   * Method traces the passed Throwable object through the underlying tracing framework with trace level WARN. In order
   * to support internationalization of trace messages please use method
   * <code>warn(MessageID, String[], Throwable)</code> or <code>warn(MessageID, String[], ExceptionInfoProvider)</code>
   * instead.
   * 
   * @param pMessage Message that should be traced with trace level WARN. The parameter may be null. In this case no
   * trace information will be written.
   * @param pThrowable Throwable that should be traced with trace level WARN. The parameter may be null. In this case no
   * trace information will be written.
   * @see #warn(MessageID, Throwable, String[])
   * @see #warn(MessageID, ApplicationException, String[])
   */
  @Override
  public void warn( String pMessage, Throwable pThrowable ) {
    this.log(TraceLevel.WARN, pMessage, pThrowable);
  }

  /**
   * Method traces a message with trace level WARN that will be created from the passed message id and the passed
   * parameters.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  @Override
  public void warn( MessageID pMessageID, String... pMessageParameters ) {
    this.log(TraceLevel.WARN, pMessageID, null, pMessageParameters);
  }

  /**
   * Method traces a message with trace level WARN that will be created from the passed message id and the passed
   * parameters.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pMessageParameters Objects that are used to parameterize the trace message. The matching
   * {@link ObjectFormatter} will be used to convert the passed objects into Strings. The parameter may be null.
   */
  @Override
  public void warn( MessageID pMessageID, Object... pMessageParameters ) {
    this.log(TraceLevel.WARN, pMessageID, null, pMessageParameters);
  }

  /**
   * Method traces a message with trace level WARN that will be created from the passed message id and the passed
   * parameters together with the passed Throwable object. If the passed message id and the Throwable object are null
   * then no message will be traced, since this would result in an empty message.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pThrowable Throwable object that should be traced together with the passed message. The parameter may be
   * null.
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  @Override
  public void warn( MessageID pMessageID, Throwable pThrowable, String... pMessageParameters ) {
    this.log(TraceLevel.WARN, pMessageID, pThrowable, pMessageParameters);
  }

  /**
   * Method traces a message with trace level WARN that will be created from the passed message id and the passed
   * parameters together with the passed Throwable object. If the passed message id and the Throwable object are null
   * then no message will be traced, since this would result in an empty message.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pThrowable Throwable object that should be traced together with the passed message. The parameter may be
   * null.
   * @param pMessageParameters Objects that are used to parameterize the trace message. The matching
   * {@link ObjectFormatter} will be used to convert the passed objects into Strings. The parameter may be null.
   */
  @Override
  public void warn( MessageID pMessageID, Throwable pThrowable, Object... pMessageParameters ) {
    this.log(TraceLevel.WARN, pMessageID, pThrowable, pMessageParameters);
  }

  /**
   * Method traces a message with trace level WARN that will be created from the passed message id and the passed
   * parameters together with the passed ApplicationException. If the passed message id and the application exception
   * are null then no message will be traced, since this would result in an empty message.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pApplicationException Application exception that should be traced together with the passed message. The
   * parameter may be null.
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  @Override
  public void warn( MessageID pMessageID, ApplicationException pApplicationException, String... pMessageParameters ) {
    this.log(TraceLevel.WARN, pMessageID, pApplicationException, pMessageParameters);
  }

  /**
   * Method traces a message with trace level WARN that will be created from the passed message id and the passed
   * parameters together with the passed ApplicationException. If the passed message id and the application exception
   * are null then no message will be traced, since this would result in an empty message.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pApplicationException Application exception that should be traced together with the passed message. The
   * parameter may be null.
   * @param pMessageParameters Objects that are used to parameterize the trace message. The matching
   * {@link ObjectFormatter} will be used to convert the passed objects into Strings. The parameter may be null.
   */
  @Override
  public void warn( MessageID pMessageID, ApplicationException pApplicationException, Object... pMessageParameters ) {
    this.log(TraceLevel.WARN, pMessageID, pApplicationException, pMessageParameters);
  }

  /**
   * Method traces a message with trace level WARN that will be created from the passed message id and the passed
   * parameters together with the passed SystemException. If the passed message id and the system exception are null
   * then no message will be traced, since this would result in an empty message.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pSystemException System exception that should be traced together with the passed message. The parameter may
   * be null.
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  @Override
  public void warn( MessageID pMessageID, SystemException pSystemException, String... pMessageParameters ) {
    this.log(TraceLevel.WARN, pMessageID, pSystemException, pMessageParameters);
  }

  /**
   * Method traces a message with trace level WARN that will be created from the passed message id and the passed
   * parameters together with the passed SystemException. If the passed message id and the system exception are null
   * then no message will be traced, since this would result in an empty message.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pSystemException System exception that should be traced together with the passed message. The parameter may
   * be null.
   * @param pMessageParameters Objects that are used to parameterize the trace message. The matching
   * {@link ObjectFormatter} will be used to convert the passed objects into Strings. The parameter may be null.
   */
  @Override
  public void warn( MessageID pMessageID, SystemException pSystemException, Object... pMessageParameters ) {
    this.log(TraceLevel.WARN, pMessageID, pSystemException, pMessageParameters);
  }

  /**
   * Method checks whether the trace level "ERROR" is enabled.
   * 
   * @return boolean The method returns true if the trace level "ERROR" is enabled otherwise false.
   */
  @Override
  public boolean isErrorEnabled( ) {
    return this.isLevelEnabled(TraceLevel.ERROR);
  }

  /**
   * Method traces the passed message through the underlying tracing framework with trace level ERROR. In order to
   * support internationalization of trace messages please use method
   * <code>error(MessageID pMessageID, String[] pMessageParameters)</code> instead.
   * 
   * @param pMessage Message that should be traced with trace level ERROR. The parameter may be null. In this case no
   * trace information will be written.
   * @see #error(MessageID, String[])
   */
  @Override
  public void error( String pMessage ) {
    this.log(TraceLevel.ERROR, pMessage, null);
  }

  /**
   * Method traces the passed object through the underlying tracing framework with trace level ERROR. The object will be
   * formatted according to the defined {@link TraceObjectFormatter}. If no specific formatter is defined then
   * {@link Object#toString()} will be used.
   * 
   * @param pObject Object that should be traced with trace level ERROR. The parameter may be null. In this case no
   * trace information will be written.
   * @see TraceObjectFormatter#formatObject(Object, TraceLevel)
   */
  @Override
  public void errorObject( Object pObject ) {
    this.log(TraceLevel.ERROR, pObject);
  }

  /**
   * Method traces the passed exception through the underlying tracing frameworks with trace level ERROR.
   * 
   * @param pThrowable Exception that should be traced. The parameter may be null. In this case nothing will be traced.
   */
  @Override
  public void error( Throwable pThrowable ) {
    this.error(pThrowable, false);
  }

  /**
   * Method traces the passed exception through the underlying tracing frameworks with trace level ERROR.
   * 
   * @param pThrowable Exception that should be traced. The parameter may be null. In this case nothing will be traced.
   * @param pSuppressStacktrace Parameter controls if the stack trace of the exception should be suppressed or not.
   */
  @Override
  public void error( Throwable pThrowable, boolean pSuppressStacktrace ) {
    this.log(TraceLevel.ERROR, pThrowable, pSuppressStacktrace);
  }

  /**
   * Method traces the passed Throwable object through the underlying tracing framework with trace level ERROR. In order
   * to support internationalization of trace messages please use method
   * <code>error(MessageID, String[], Throwable)</code> or
   * <code>error(MessageID, String[], ExceptionInfoProvider)</code> instead.
   * 
   * @param pMessage Message that should be traced with trace level ERROR. The parameter may be null. In this case no
   * trace information will be written.
   * @param pThrowable Throwable that should be traced with trace level ERROR. The parameter may be null. In this case
   * no trace information will be written.
   * @see #error(MessageID, Throwable, String[])
   * @see #error(MessageID, ApplicationException, String[])
   */
  @Override
  public void error( String pMessage, Throwable pThrowable ) {
    this.log(TraceLevel.ERROR, pMessage, pThrowable);
  }

  /**
   * Method traces a message with trace level ERROR that will be created from the passed message id and the passed
   * parameters.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  @Override
  public void error( MessageID pMessageID, String... pMessageParameters ) {
    this.log(TraceLevel.ERROR, pMessageID, null, pMessageParameters);
  }

  /**
   * Method traces a message with trace level ERROR that will be created from the passed message id and the passed
   * parameters.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pMessageParameters Objects that are used to parameterize the trace message. The matching
   * {@link ObjectFormatter} will be used to convert the passed objects into Strings. The parameter may be null.
   */
  @Override
  public void error( MessageID pMessageID, Object... pMessageParameters ) {
    this.log(TraceLevel.ERROR, pMessageID, null, pMessageParameters);
  }

  /**
   * Method traces a message with trace level ERROR that will be created from the passed message id and the passed
   * parameters together with the passed Throwable object. If the passed message id and the Throwable object are null
   * then no message will be traced, since this would result in an empty message.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pThrowable Throwable object that should be traced together with the passed message. The parameter may be
   * null.
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  @Override
  public void error( MessageID pMessageID, Throwable pThrowable, String... pMessageParameters ) {
    this.log(TraceLevel.ERROR, pMessageID, pThrowable, pMessageParameters);
  }

  /**
   * Method traces a message with trace level ERROR that will be created from the passed message id and the passed
   * parameters together with the passed Throwable object. If the passed message id and the Throwable object are null
   * then no message will be traced, since this would result in an empty message.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pThrowable Throwable object that should be traced together with the passed message. The parameter may be
   * null.
   * @param pMessageParameters Objects that are used to parameterize the trace message. The matching
   * {@link ObjectFormatter} will be used to convert the passed objects into Strings. The parameter may be null.
   */
  @Override
  public void error( MessageID pMessageID, Throwable pThrowable, Object... pMessageParameters ) {
    this.log(TraceLevel.ERROR, pMessageID, pThrowable, pMessageParameters);
  }

  /**
   * Method traces a message with trace level ERROR that will be created from the passed message id and the passed
   * parameters together with the passed ApplicationException. If the passed message id and the application exception
   * are null then no message will be traced, since this would result in an empty message.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pApplicationException Application exception that should be traced together with the passed message. The
   * parameter may be null.
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  @Override
  public void error( MessageID pMessageID, ApplicationException pApplicationException, String... pMessageParameters ) {
    this.log(TraceLevel.ERROR, pMessageID, pApplicationException, pMessageParameters);
  }

  /**
   * Method traces a message with trace level ERROR that will be created from the passed message id and the passed
   * parameters together with the passed ApplicationException. If the passed message id and the application exception
   * are null then no message will be traced, since this would result in an empty message.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pApplicationException Application exception that should be traced together with the passed message. The
   * parameter may be null.
   * @param pMessageParameters Objects that are used to parameterize the trace message. The matching
   * {@link ObjectFormatter} will be used to convert the passed objects into Strings. The parameter may be null.
   */
  @Override
  public void error( MessageID pMessageID, ApplicationException pApplicationException, Object... pMessageParameters ) {
    this.log(TraceLevel.ERROR, pMessageID, pApplicationException, pMessageParameters);
  }

  /**
   * Method traces a message with trace level ERROR that will be created from the passed message id and the passed
   * parameters together with the passed SystemException. If the passed message id and the system exception are null
   * then no message will be traced, since this would result in an empty message.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pSystemException System exception that should be traced together with the passed message. The parameter may
   * be null.
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  @Override
  public void error( MessageID pMessageID, SystemException pSystemException, String... pMessageParameters ) {
    this.log(TraceLevel.ERROR, pMessageID, pSystemException, pMessageParameters);
  }

  /**
   * Method traces a message with trace level ERROR that will be created from the passed message id and the passed
   * parameters together with the passed SystemException. If the passed message id and the system exception are null
   * then no message will be traced, since this would result in an empty message.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pSystemException System exception that should be traced together with the passed message. The parameter may
   * be null.
   * @param pMessageParameters Objects that are used to parameterize the trace message. The matching
   * {@link ObjectFormatter} will be used to convert the passed objects into Strings. The parameter may be null.
   */
  @Override
  public void error( MessageID pMessageID, SystemException pSystemException, Object... pMessageParameters ) {
    this.log(TraceLevel.ERROR, pMessageID, pSystemException, pMessageParameters);
  }

  /**
   * Method checks whether the trace level "FATAL" is enabled.
   * 
   * @return boolean The method returns true if the trace level "FATAL" is enabled otherwise false.
   */
  @Override
  public boolean isFatalEnabled( ) {
    return this.isLevelEnabled(TraceLevel.FATAL);
  }

  /**
   * Method traces the passed message through the underlying tracing framework with trace level FATAL. In order to
   * support internationalization of trace messages please use method
   * <code>fatal(MessageID pMessageID, String[] pMessageParameters)</code> instead.
   * 
   * @param pMessage Message that should be traced with trace level FATAL. The parameter may be null. In this case no
   * trace information will be written.
   * @see #fatal(MessageID, String[])
   */
  @Override
  public void fatal( String pMessage ) {
    this.log(TraceLevel.FATAL, pMessage, null);
  }

  /**
   * Method traces the passed object through the underlying tracing framework with trace level FATAL. The object will be
   * formatted according to the defined {@link TraceObjectFormatter}. If no specific formatter is defined then
   * {@link Object#toString()} will be used.
   * 
   * @param pObject Object that should be traced with trace level FATAL. The parameter may be null. In this case no
   * trace information will be written.
   * @see TraceObjectFormatter#formatObject(Object, TraceLevel)
   */
  @Override
  public void fatalObject( Object pObject ) {
    this.log(TraceLevel.FATAL, pObject);
  }

  /**
   * Method traces the passed exception through the underlying tracing frameworks with trace level FATAL.
   * 
   * @param pThrowable Exception that should be traced. The parameter may be null. In this case nothing will be traced.
   */
  @Override
  public void fatal( Throwable pThrowable ) {
    this.fatal(pThrowable, false);
  }

  /**
   * Method traces the passed exception through the underlying tracing frameworks with trace level FATAL.
   * 
   * @param pThrowable Exception that should be traced. The parameter may be null. In this case nothing will be traced.
   * @param pSuppressStacktrace Parameter controls if the stack trace of the exception should be suppressed or not.
   */
  @Override
  public void fatal( Throwable pThrowable, boolean pSuppressStacktrace ) {
    this.log(TraceLevel.FATAL, pThrowable, pSuppressStacktrace);
  }

  /**
   * Method traces the passed Throwable object through the underlying tracing framework with trace level FATAL. In order
   * to support internationalization of trace messages please use method
   * <code>fatal(MessageID, String[], Throwable)</code> or
   * <code>fatal(MessageID, String[], ExceptionInfoProvider)</code> instead.
   * 
   * @param pMessage Message that should be traced with trace level FATAL. The parameter may be null. In this case no
   * trace information will be written.
   * @param pThrowable Throwable that should be traced with trace level FATAL. The parameter may be null. In this case
   * no trace information will be written.
   * @see #fatal(MessageID, Throwable, String[])
   * @see #fatal(MessageID, ApplicationException, String[])
   */
  @Override
  public void fatal( String pMessage, Throwable pThrowable ) {
    this.log(TraceLevel.FATAL, pMessage, pThrowable);
  }

  /**
   * Method traces a message with trace level FATAL that will be created from the passed message id and the passed
   * parameters.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  @Override
  public void fatal( MessageID pMessageID, String... pMessageParameters ) {
    this.log(TraceLevel.FATAL, pMessageID, null, pMessageParameters);
  }

  /**
   * Method traces a message with trace level FATAL that will be created from the passed message id and the passed
   * parameters.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pMessageParameters Objects that are used to parameterize the trace message. The matching
   * {@link ObjectFormatter} will be used to convert the passed objects into Strings. The parameter may be null.
   */
  @Override
  public void fatal( MessageID pMessageID, Object... pMessageParameters ) {
    this.log(TraceLevel.FATAL, pMessageID, null, pMessageParameters);
  }

  /**
   * Method traces a message with trace level FATAL that will be created from the passed message id and the passed
   * parameters together with the passed Throwable object. If the passed message id and the Throwable object are null
   * then no message will be traced, since this would result in an empty message.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pThrowable Throwable object that should be traced together with the passed message. The parameter may be
   * null.
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  @Override
  public void fatal( MessageID pMessageID, Throwable pThrowable, String... pMessageParameters ) {
    this.log(TraceLevel.FATAL, pMessageID, pThrowable, pMessageParameters);
  }

  /**
   * Method traces a message with trace level FATAL that will be created from the passed message id and the passed
   * parameters together with the passed Throwable object. If the passed message id and the Throwable object are null
   * then no message will be traced, since this would result in an empty message.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pThrowable Throwable object that should be traced together with the passed message. The parameter may be
   * null.
   * @param pMessageParameters Objects that are used to parameterize the trace message. The matching
   * {@link ObjectFormatter} will be used to convert the passed objects into Strings. The parameter may be null.
   */
  @Override
  public void fatal( MessageID pMessageID, Throwable pThrowable, Object... pMessageParameters ) {
    this.log(TraceLevel.FATAL, pMessageID, pThrowable, pMessageParameters);
  }

  /**
   * Method traces a message with trace level FATAL that will be created from the passed message id and the passed
   * parameters together with the passed ApplicationException. If the passed message id and the application exception
   * are null then no message will be traced, since this would result in an empty message.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pApplicationException Application exception that should be traced together with the passed message. The
   * parameter may be null.
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  @Override
  public void fatal( MessageID pMessageID, ApplicationException pApplicationException, String... pMessageParameters ) {
    this.log(TraceLevel.FATAL, pMessageID, pApplicationException, pMessageParameters);
  }

  /**
   * Method traces a message with trace level FATAL that will be created from the passed message id and the passed
   * parameters together with the passed ApplicationException. If the passed message id and the application exception
   * are null then no message will be traced, since this would result in an empty message.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pApplicationException Application exception that should be traced together with the passed message. The
   * parameter may be null.
   * @param pMessageParameters Objects that are used to parameterize the trace message. The matching
   * {@link ObjectFormatter} will be used to convert the passed objects into Strings. The parameter may be null.
   */
  @Override
  public void fatal( MessageID pMessageID, ApplicationException pApplicationException, Object... pMessageParameters ) {
    this.log(TraceLevel.FATAL, pMessageID, pApplicationException, pMessageParameters);
  }

  /**
   * Method traces a message with trace level FATAL that will be created from the passed message id and the passed
   * parameters together with the passed SystemException. If the passed message id and the system exception are null
   * then no message will be traced, since this would result in an empty message.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pSystemException System exception that should be traced together with the passed message. The parameter may
   * be null.
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  @Override
  public void fatal( MessageID pMessageID, SystemException pSystemException, String... pMessageParameters ) {
    this.log(TraceLevel.FATAL, pMessageID, pSystemException, pMessageParameters);
  }

  /**
   * Method traces a message with trace level FATAL that will be created from the passed message id and the passed
   * parameters together with the passed SystemException. If the passed message id and the system exception are null
   * then no message will be traced, since this would result in an empty message.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pSystemException System exception that should be traced together with the passed message. The parameter may
   * be null.
   * @param pMessageParameters Objects that are used to parameterize the trace message. The matching
   * {@link ObjectFormatter} will be used to convert the passed objects into Strings. The parameter may be null.
   */
  @Override
  public void fatal( MessageID pMessageID, SystemException pSystemException, Object... pMessageParameters ) {
    this.log(TraceLevel.FATAL, pMessageID, pSystemException, pMessageParameters);
  }

  /**
   * Method traces the passed exception / error. By default level error will be used.
   * 
   * @param pThrowable Throwable that should be traced. The parameter may be null. In this case nothing will be done.
   */
  @Override
  public void write( Throwable pThrowable ) {
    this.write(pThrowable, false);
  }

  /**
   * Method traces the passed exception / error. By default level error will be used.
   * 
   * @param pThrowable Throwable that should be traced. The parameter may be null. In this case nothing will be done.
   * @param pSuppressStacktrace Parameter controls if the stacktrace of the exception should be suppressed or not.
   */
  @Override
  public void write( Throwable pThrowable, boolean pSuppressStacktrace ) {
    if (pThrowable != null) {
      // We have some details abo
      TraceLevel lTraceLevel;
      if (pThrowable instanceof ExceptionInfoProvider) {
        ExceptionInfoProvider lExceptionInfoProvider = (ExceptionInfoProvider) pThrowable;
        lTraceLevel = lExceptionInfoProvider.getErrorCode().getTraceLevel();
      }
      // As we do not have any further information let's use trace level ERROR.
      else {
        lTraceLevel = TraceLevel.ERROR;
      }
      this.log(lTraceLevel, pThrowable, pSuppressStacktrace);
    }
  }

  /**
   * Method writes the message with the passed message id using its defined trace level.
   * 
   * @param pMessageID Id of the message that should be traced together with the passed message parameters. The
   * parameter may be null.
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  @Override
  public void write( MessageID pMessageID, String... pMessageParameters ) {
    this.log(pMessageID, null, pMessageParameters);
  }

  /**
   * Method writes the message with the passed message id using its defined trace level.
   * 
   * @param pMessageID Id of the message that should be traced together with the passed message parameters. The
   * parameter may be null.
   * @param pMessageParameters Objects that are used to parameterize the trace message. The matching
   * {@link ObjectFormatter} will be used to convert the passed objects into Strings. The parameter may be null.
   */
  @Override
  public void write( MessageID pMessageID, Object... pMessageParameters ) {
    this.log(pMessageID, null, pMessageParameters);
  }

  /**
   * Method writes the message with the passed message id using its defined trace level.
   * 
   * @param pMessageID Id of the message that should be traced together with the passed message parameters. The
   * parameter may be null.
   * @param pThrowable gets forwarded to the specific method which process the Trace. For Example error(), warn(),
   * fatal()
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  @Override
  public void write( MessageID pMessageID, Throwable pThrowable, String... pMessageParameters ) {
    this.log(pMessageID, pThrowable, pMessageParameters);
  }

  /**
   * Method writes the message with the passed message id using its defined trace level.
   * 
   * @param pMessageID Id of the message that should be traced together with the passed message parameters. The
   * parameter may be null.
   * @param pThrowable gets forwarded to the specific method which process the Trace. For Example error(), warn(),
   * fatal()
   * @param pMessageParameters Objects that are used to parameterize the trace message. The matching
   * {@link ObjectFormatter} will be used to convert the passed objects into Strings. The parameter may be null.
   */
  @Override
  public void write( MessageID pMessageID, Throwable pThrowable, Object... pMessageParameters ) {
    this.log(pMessageID, pThrowable, pMessageParameters);
  }

  /**
   * Method writes the message with the passed message id using its defined trace level.
   * 
   * @param pMessageID Id of the message that should be traced together with the passed message parameters. The
   * parameter may be null.
   * @param pApplicationException get forwarded to the specific method which process the Trace. For Example to error(),
   * warn(), fatal()
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  @Override
  public void write( MessageID pMessageID, ApplicationException pApplicationException, String... pMessageParameters ) {
    this.log(pMessageID, pApplicationException, pMessageParameters);
  }

  /**
   * Method writes the message with the passed message id using its defined trace level.
   * 
   * @param pMessageID Id of the message that should be traced together with the passed message parameters. The
   * parameter may be null.
   * @param pApplicationException get forwarded to the specific method which process the Trace. For Example to error(),
   * warn(), fatal()
   * @param pMessageParameters Objects that are used to parameterize the trace message. The matching
   * {@link ObjectFormatter} will be used to convert the passed objects into Strings. The parameter may be null.
   */
  @Override
  public void write( MessageID pMessageID, ApplicationException pApplicationException, Object... pMessageParameters ) {
    this.log(pMessageID, pApplicationException, pMessageParameters);
  }

  /**
   * Method writes the message with the passed message id using its defined trace level.
   * 
   * @param pMessageID Id of the message that should be traced together with the passed message parameters. The
   * parameter may be null.
   * @param pSystemException get forwarded to the specific method which process the Trace. For Example to error(),
   * warn(), fatal()
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  @Override
  public void write( MessageID pMessageID, SystemException pSystemException, String... pMessageParameters ) {
    this.log(pMessageID, pSystemException, pMessageParameters);
  }

  /**
   * Method writes the message with the passed message id using its defined trace level.
   * 
   * @param pMessageID Id of the message that should be traced together with the passed message parameters. The
   * parameter may be null.
   * @param pSystemException get forwarded to the specific method which process the Trace. For Example to error(),
   * warn(), fatal()
   * @param pMessageParameters Objects that are used to parameterize the trace message. The matching
   * {@link ObjectFormatter} will be used to convert the passed objects into Strings. The parameter may be null.
   */
  @Override
  public void write( MessageID pMessageID, SystemException pSystemException, Object... pMessageParameters ) {
    this.log(pMessageID, pSystemException, pMessageParameters);
  }
}
