/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.trace;

import com.anaptecs.jeaf.xfun.annotations.TraceObjectFormatter;
import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.errorhandling.ApplicationException;
import com.anaptecs.jeaf.xfun.api.errorhandling.SystemException;
import com.anaptecs.jeaf.xfun.api.messages.MessageID;

/**
 * This interface defines JEAF's tracing facilities. In order to provide a flexible tracing theses are build on top of
 * Apache Commons-Logging which provides an abstraction layer for several logging frameworks like Log4J or the JDK
 * logging features. The trace levels are ordered from most detailed level TRACE up to FATAL and as defined by Apache's
 * Commons-Logging the levels are TRACE, DEBUG, INFO, WARN, ERROR, FATAL.
 * 
 * @author JEAF Development Team
 * @version JEAF Release 1.2
 */
public interface Trace {
  /**
   * Methods returns the trace object that should be used in the current context.
   * 
   * <b>Remark: </b>As the trace object depends on the current context it's not recommended to cache the result of this
   * call.
   * 
   * @return {@link Trace} Trace object for the current context. The method never returns null.
   */
  static Trace getTrace( ) {
    return XFun.getTrace();
  }

  /**
   * In order to know the current context JEAF's trace mechanism supports a so called context stack. Based on this
   * context stack we are able to know the current context and can provide the current trace object.
   * 
   * Method creates a new context stack with the passed object as first element on it.
   * 
   * <b>Remark: </b>This method is intended to be used by frameworks that want to provide the current context. In
   * standard business application code there is no reason to use these methods
   * 
   * @param pContextStackElement First element for new context stack. The parameter must not be null.
   */
  void newContextStack( ContextStackElement pContextStackElement );

  /**
   * In order to know the current context JEAF's trace mechanism supports a so called context stack. Based on this
   * context stack we are able to know the current context and can provide the current trace object.
   * 
   * Method pushes the passed object to the existing context stack.
   * 
   * <b>Remark: </b>This method is intended to be used by frameworks that want to provide the current context. In
   * standard business application code there is no reason to use these methods
   * 
   * @param pContextStackElement New element for the context stack. The parameter must not be null.
   */
  void pushContextStackElement( ContextStackElement pContextStackElement );

  /**
   * In order to know the current context JEAF's trace mechanism supports a so called context stack. Based on this
   * context stack we are able to know the current context and can provide the current trace object.
   * 
   * Method pops the top element of the context stack.
   * 
   * <b>Remark: </b>This method is intended to be used by frameworks that want to provide the current context. In
   * standard business application code there is no reason to use these methods
   * 
   * @return {@link ContextStackElement} Object that was the top element on the context stack or null if the stack is
   * empty.
   */
  ContextStackElement popContextStackElement( );

  /**
   * Method writes an so called emergency trace. Emergency traces are needed in case that we run into fatal problems
   * during initialization. Thus this method should be implemented in a way that it requires no environment be be set
   * up.
   * 
   * @param pMessage Message that should be written. The parameter may be null.
   * @param pThrowable Exception that occurred. The parameter may be null.
   */
  void writeEmergencyTrace( String pMessage, Throwable pThrowable );

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
  void writeEmergencyTrace( String pMessage, Throwable pThrowable, TraceLevel pTraceLevel );

  /**
   * Method writes a so called init info. Init info means that this message will be written during the very first steps
   * of initialization. This means that the implementations must not require or execute any of them.
   * 
   * @param pMessage Message that should be written. The parameter may be null. In this case no message will be written.
   * @param pTraceLevel Level that should be used for tracing. The parameter may be null. In this case
   * {@link TraceLevel#FATAL} will be used.
   */
  void writeInitInfo( String pMessage, TraceLevel pTraceLevel );

  /**
   * Method checks whether the trace level "TRACE" is enabled.
   * 
   * @return boolean The method returns true if the trace level "TRACE" is enabled otherwise false.
   */
  boolean isTraceEnabled( );

  /**
   * Method traces the passed message through the underlying tracing framework with trace level TRACE. In order to
   * support internationalization of trace messages please use method
   * <code>trace(MessageID pMessageID, String[] pMessageParameters)</code> instead.
   * 
   * @param pMessage Message that should be traced with trace level TRACE. The parameter may be null. In this case no
   * trace information will be written.
   * @see #trace(MessageID, String[])
   */
  void trace( String pMessage );

  /**
   * Method traces the passed object through the underlying tracing framework with trace level TRACE. The object will be
   * formatted according to the defined {@link TraceObjectFormatter}. If no specific formatter is defined then
   * {@link Object#toString()} will be used.
   * 
   * @param pObject Object that should be traced with trace level TRACE. The parameter may be null. In this case no
   * trace information will be written.
   * @see TraceObjectFormatter#formatObject(Object, TraceLevel)
   */
  void traceObject( Object pObject );

  /**
   * Method traces the passed exception through the underlying tracing frameworks with trace level TRACE.
   * 
   * @param pThrowable Exception that should be traced. The parameter may be null. In this case nothing will be traced.
   */
  void trace( Throwable pThrowable );

  /**
   * Method traces the passed exception through the underlying tracing frameworks with trace level TRACE.
   * 
   * @param pThrowable Exception that should be traced. The parameter may be null. In this case nothing will be traced.
   * @param pNoStacktrace Parameter defines if the stack trace of the exception should be suppressed or not.
   */
  void trace( Throwable pThrowable, boolean pNoStacktrace );

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
  void trace( String pMessage, Throwable pThrowable );

  /**
   * Method traces a message with trace level TRACE that will be created from the passed message id and the passed
   * parameters.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  void trace( MessageID pMessageID, String... pMessageParameters );

  /**
   * Method traces a message with trace level TRACE that will be created from the passed message id and the passed
   * parameters.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pMessageParameters Objects that are used to parameterize the trace message. The matching
   * {@link ObjectFormatter} will be used to convert the passed objects into Strings. The parameter may be null.
   */
  void trace( MessageID pMessageID, Object... pMessageParameters );

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
  void trace( MessageID pMessageID, Throwable pThrowable, String... pMessageParameters );

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
  void trace( MessageID pMessageID, Throwable pThrowable, Object... pMessageParameters );

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
  void trace( MessageID pMessageID, ApplicationException pApplicationException, String... pMessageParameters );

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
  void trace( MessageID pMessageID, ApplicationException pApplicationException, Object... pMessageParameters );

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
  void trace( MessageID pMessageID, SystemException pSystemException, String... pMessageParameters );

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
  void trace( MessageID pMessageID, SystemException pSystemException, Object... pMessageParameters );

  /**
   * Method checks whether the trace level "DEBUG" is enabled.
   * 
   * @return boolean The method returns true if the trace level "DEBUG" is enabled otherwise false.
   */
  boolean isDebugEnabled( );

  /**
   * Method traces the passed message through the underlying tracing framework with trace level DEBUG. In order to
   * support internationalization of trace messages please use method
   * <code>debug(MessageID pMessageID, String[] pMessageParameters)</code> instead.
   * 
   * @param pMessage Message that should be traced with trace level DEBUG. The parameter may be null. In this case no
   * trace information will be written.
   * @see #debug(MessageID, String[])
   */
  void debug( String pMessage );

  /**
   * Method traces the passed object through the underlying tracing framework with trace level DEBUG. The object will be
   * formatted according to the defined {@link TraceObjectFormatter}. If no specific formatter is defined then
   * {@link Object#toString()} will be used.
   * 
   * @param pObject Object that should be traced with trace level DEBUG. The parameter may be null. In this case no
   * trace information will be written.
   * @see TraceObjectFormatter#formatObject(Object, TraceLevel)
   */
  void debugObject( Object pObject );

  /**
   * Method traces the passed exception through the underlying tracing frameworks with trace level DEBUG.
   * 
   * @param pThrowable Exception that should be traced. The parameter may be null. In this case nothing will be traced.
   */
  void debug( Throwable pThrowable );

  /**
   * Method traces the passed exception through the underlying tracing frameworks with trace level DEBUG.
   * 
   * @param pThrowable Exception that should be traced. The parameter may be null. In this case nothing will be traced.
   * @param pNoStacktrace Parameter defines if the stack trace of the exception should be suppressed or not.
   */
  void debug( Throwable pThrowable, boolean pNoStacktrace );

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
  void debug( String pMessage, Throwable pThrowable );

  /**
   * Method traces a message with trace level DEBUG that will be created from the passed message id and the passed
   * parameters.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  void debug( MessageID pMessageID, String... pMessageParameters );

  /**
   * Method traces a message with trace level DEBUG that will be created from the passed message id and the passed
   * parameters.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pMessageParameters Objects that are used to parameterize the trace message. The matching
   * {@link ObjectFormatter} will be used to convert the passed objects into Strings. The parameter may be null.
   */
  void debug( MessageID pMessageID, Object... pMessageParameters );

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
  void debug( MessageID pMessageID, Throwable pThrowable, String... pMessageParameters );

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
  void debug( MessageID pMessageID, Throwable pThrowable, Object... pMessageParameters );

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
  void debug( MessageID pMessageID, ApplicationException pApplicationException, String... pMessageParameters );

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
  void debug( MessageID pMessageID, ApplicationException pApplicationException, Object... pMessageParameters );

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
  void debug( MessageID pMessageID, SystemException pSystemException, String... pMessageParameters );

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
  void debug( MessageID pMessageID, SystemException pSystemException, Object... pMessageParameters );

  /**
   * Method checks whether the trace level "INFO" is enabled.
   * 
   * @return boolean The method returns true if the trace level "INFO" is enabled otherwise false.
   */
  boolean isInfoEnabled( );

  /**
   * Method traces the passed message through the underlying tracing framework with trace level INFO. In order to
   * support internationalization of trace messages please use method
   * <code>info(MessageID pMessageID, String[] pMessageParameters)</code> instead.
   * 
   * @param pMessage Message that should be traced with trace level INFO. The parameter may be null. In this case no
   * trace information will be written.
   * @see #info(MessageID, String[])
   */
  void info( String pMessage );

  /**
   * Method traces the passed object through the underlying tracing framework with trace level INFO. The object will be
   * formatted according to the defined {@link TraceObjectFormatter}. If no specific formatter is defined then
   * {@link Object#toString()} will be used.
   * 
   * @param pObject Object that should be traced with trace level INFO. The parameter may be null. In this case no trace
   * information will be written.
   * @see TraceObjectFormatter#formatObject(Object, TraceLevel)
   */
  void infoObject( Object pObject );

  /**
   * Method traces the passed exception through the underlying tracing frameworks with trace level INFO.
   * 
   * @param pThrowable Exception that should be traced. The parameter may be null. In this case nothing will be traced.
   */
  void info( Throwable pThrowable );

  /**
   * Method traces the passed exception through the underlying tracing frameworks with trace level INFO.
   * 
   * @param pThrowable Exception that should be traced. The parameter may be null. In this case nothing will be traced.
   * @param pNoStacktrace Parameter defines if the stack trace of the exception should be suppressed or not.
   */
  void info( Throwable pThrowable, boolean pNoStacktrace );

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
  void info( String pMessage, Throwable pThrowable );

  /**
   * Method traces a message with trace level INFO that will be created from the passed message id and the passed
   * parameters.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  void info( MessageID pMessageID, String... pMessageParameters );

  /**
   * Method traces a message with trace level INFO that will be created from the passed message id and the passed
   * parameters.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pMessageParameters Objects that are used to parameterize the trace message. The matching
   * {@link ObjectFormatter} will be used to convert the passed objects into Strings. The parameter may be null.
   */
  void info( MessageID pMessageID, Object... pMessageParameters );

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
  void info( MessageID pMessageID, Throwable pThrowable, String... pMessageParameters );

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
  void info( MessageID pMessageID, Throwable pThrowable, Object... pMessageParameters );

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
  void info( MessageID pMessageID, ApplicationException pApplicationException, String... pMessageParameters );

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
  void info( MessageID pMessageID, ApplicationException pApplicationException, Object... pMessageParameters );

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
  void info( MessageID pMessageID, SystemException pSystemException, String... pMessageParameters );

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
  void info( MessageID pMessageID, SystemException pSystemException, Object... pMessageParameters );

  /**
   * Method checks whether the trace level "WARN" is enabled.
   * 
   * @return boolean The method returns true if the trace level "WARN" is enabled otherwise false.
   */
  boolean isWarnEnabled( );

  /**
   * Method traces the passed message through the underlying tracing framework with trace level WARN. In order to
   * support internationalization of trace messages please use method
   * <code>warn(MessageID pMessageID, String[] pMessageParameters)</code> instead.
   * 
   * @param pMessage Message that should be traced with trace level WARN. The parameter may be null. In this case no
   * trace information will be written.
   * @see #warn(MessageID, String[])
   */
  void warn( String pMessage );

  /**
   * Method traces the passed object through the underlying tracing framework with trace level WARN. The object will be
   * formatted according to the defined {@link TraceObjectFormatter}. If no specific formatter is defined then
   * {@link Object#toString()} will be used.
   * 
   * @param pObject Object that should be traced with trace level WARN. The parameter may be null. In this case no trace
   * information will be written.
   * @see TraceObjectFormatter#formatObject(Object, TraceLevel)
   */
  void warnObject( Object pObject );

  /**
   * Method traces the passed exception through the underlying tracing frameworks with trace level WARN.
   * 
   * @param pThrowable Exception that should be traced. The parameter may be null. In this case nothing will be traced.
   */
  void warn( Throwable pThrowable );

  /**
   * Method traces the passed exception through the underlying tracing frameworks with trace level WARN.
   * 
   * @param pThrowable Exception that should be traced. The parameter may be null. In this case nothing will be traced.
   * @param pNoStacktrace Parameter defines if the stack trace of the exception should be suppressed or not.
   */
  void warn( Throwable pThrowable, boolean pNoStacktrace );

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
  void warn( String pMessage, Throwable pThrowable );

  /**
   * Method traces a message with trace level WARN that will be created from the passed message id and the passed
   * parameters.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  void warn( MessageID pMessageID, String... pMessageParameters );

  /**
   * Method traces a message with trace level WARN that will be created from the passed message id and the passed
   * parameters.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pMessageParameters Objects that are used to parameterize the trace message. The matching
   * {@link ObjectFormatter} will be used to convert the passed objects into Strings. The parameter may be null.
   */
  void warn( MessageID pMessageID, Object... pMessageParameters );

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
  void warn( MessageID pMessageID, Throwable pThrowable, String... pMessageParameters );

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
  void warn( MessageID pMessageID, Throwable pThrowable, Object... pMessageParameters );

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
  void warn( MessageID pMessageID, ApplicationException pApplicationException, String... pMessageParameters );

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
  void warn( MessageID pMessageID, ApplicationException pApplicationException, Object... pMessageParameters );

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
  void warn( MessageID pMessageID, SystemException pSystemException, String... pMessageParameters );

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
  void warn( MessageID pMessageID, SystemException pSystemException, Object... pMessageParameters );

  /**
   * Method checks whether the trace level "ERROR" is enabled.
   * 
   * @return boolean The method returns true if the trace level "ERROR" is enabled otherwise false.
   */
  boolean isErrorEnabled( );

  /**
   * Method traces the passed message through the underlying tracing framework with trace level ERROR. In order to
   * support internationalization of trace messages please use method
   * <code>error(MessageID pMessageID, String[] pMessageParameters)</code> instead.
   * 
   * @param pMessage Message that should be traced with trace level ERROR. The parameter may be null. In this case no
   * trace information will be written.
   * @see #error(MessageID, String[])
   */
  void error( String pMessage );

  /**
   * Method traces the passed object through the underlying tracing framework with trace level ERROR. The object will be
   * formatted according to the defined {@link TraceObjectFormatter}. If no specific formatter is defined then
   * {@link Object#toString()} will be used.
   * 
   * @param pObject Object that should be traced with trace level ERROR. The parameter may be null. In this case no
   * trace information will be written.
   * @see TraceObjectFormatter#formatObject(Object, TraceLevel)
   */
  void errorObject( Object pObject );

  /**
   * Method traces the passed exception through the underlying tracing frameworks with trace level ERROR.
   * 
   * @param pThrowable Exception that should be traced. The parameter may be null. In this case nothing will be traced.
   */
  void error( Throwable pThrowable );

  /**
   * Method traces the passed exception through the underlying tracing frameworks with trace level ERROR.
   * 
   * @param pThrowable Exception that should be traced. The parameter may be null. In this case nothing will be traced.
   * @param pNoStacktrace Parameter defines if the stack trace of the exception should be suppressed or not.
   */
  void error( Throwable pThrowable, boolean pNoStacktrace );

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
  void error( String pMessage, Throwable pThrowable );

  /**
   * Method traces a message with trace level ERROR that will be created from the passed message id and the passed
   * parameters.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  void error( MessageID pMessageID, String... pMessageParameters );

  /**
   * Method traces a message with trace level ERROR that will be created from the passed message id and the passed
   * parameters.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pMessageParameters Objects that are used to parameterize the trace message. The matching
   * {@link ObjectFormatter} will be used to convert the passed objects into Strings. The parameter may be null.
   */
  void error( MessageID pMessageID, Object... pMessageParameters );

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
  void error( MessageID pMessageID, Throwable pThrowable, String... pMessageParameters );

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
  void error( MessageID pMessageID, Throwable pThrowable, Object... pMessageParameters );

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
  void error( MessageID pMessageID, ApplicationException pApplicationException, String... pMessageParameters );

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
  void error( MessageID pMessageID, ApplicationException pApplicationException, Object... pMessageParameters );

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
  void error( MessageID pMessageID, SystemException pSystemException, String... pMessageParameters );

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
  void error( MessageID pMessageID, SystemException pSystemException, Object... pMessageParameters );

  /**
   * Method checks whether the trace level "FATAL" is enabled.
   * 
   * @return boolean The method returns true if the trace level "FATAL" is enabled otherwise false.
   */
  boolean isFatalEnabled( );

  /**
   * Method traces the passed message through the underlying tracing framework with trace level FATAL. In order to
   * support internationalization of trace messages please use method
   * <code>fatal(MessageID pMessageID, String[] pMessageParameters)</code> instead.
   * 
   * @param pMessage Message that should be traced with trace level FATAL. The parameter may be null. In this case no
   * trace information will be written.
   * @see #fatal(MessageID, String[])
   */
  void fatal( String pMessage );

  /**
   * Method traces the passed object through the underlying tracing framework with trace level FATAL. The object will be
   * formatted according to the defined {@link TraceObjectFormatter}. If no specific formatter is defined then
   * {@link Object#toString()} will be used.
   * 
   * @param pObject Object that should be traced with trace level FATAL. The parameter may be null. In this case no
   * trace information will be written.
   * @see TraceObjectFormatter#formatObject(Object, TraceLevel)
   */
  void fatalObject( Object pObject );

  /**
   * Method traces the passed exception through the underlying tracing frameworks with trace level FATAL.
   * 
   * @param pThrowable Exception that should be traced. The parameter may be null. In this case nothing will be traced.
   */
  void fatal( Throwable pThrowable );

  /**
   * Method traces the passed exception through the underlying tracing frameworks with trace level FATAL.
   * 
   * @param pThrowable Exception that should be traced. The parameter may be null. In this case nothing will be traced.
   * @param pNoStacktrace Parameter defines if the stack trace of the exception should be suppressed or not.
   */
  void fatal( Throwable pThrowable, boolean pNoStacktrace );

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
  void fatal( String pMessage, Throwable pThrowable );

  /**
   * Method traces a message with trace level FATAL that will be created from the passed message id and the passed
   * parameters.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  void fatal( MessageID pMessageID, String... pMessageParameters );

  /**
   * Method traces a message with trace level FATAL that will be created from the passed message id and the passed
   * parameters.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no trace message
   * will be written.
   * @param pMessageParameters Objects that are used to parameterize the trace message. The matching
   * {@link ObjectFormatter} will be used to convert the passed objects into Strings. The parameter may be null.
   */
  void fatal( MessageID pMessageID, Object... pMessageParameters );

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
  void fatal( MessageID pMessageID, Throwable pThrowable, String... pMessageParameters );

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
  void fatal( MessageID pMessageID, Throwable pThrowable, Object... pMessageParameters );

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
  void fatal( MessageID pMessageID, ApplicationException pApplicationException, String... pMessageParameters );

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
  void fatal( MessageID pMessageID, ApplicationException pApplicationException, Object... pMessageParameters );

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
  void fatal( MessageID pMessageID, SystemException pSystemException, String... pMessageParameters );

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
  void fatal( MessageID pMessageID, SystemException pSystemException, Object... pMessageParameters );

  /**
   * Method traces the passed exception / error. By default level error will be used.
   * 
   * @param pThrowable Throwable that should be traced. The parameter may be null. In this case nothing will be done.
   */
  void write( Throwable pThrowable );

  /**
   * Method traces the passed exception / error. By default level error will be used.
   * 
   * @param pThrowable Throwable that should be traced. The parameter may be null. In this case nothing will be done.
   * @param pNoStacktrace Parameter defines if the stack trace of the exception should be suppressed or not.
   */
  void write( Throwable pThrowable, boolean pNoStacktrace );

  /**
   * Method writes the message with the passed message id using its defined trace level.
   * 
   * @param pMessageID Id of the message that should be traced together with the passed message parameters. The
   * parameter may be null.
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  void write( MessageID pMessageID, String... pMessageParameters );

  /**
   * Method writes the message with the passed message id using its defined trace level.
   * 
   * @param pMessageID Id of the message that should be traced together with the passed message parameters. The
   * parameter may be null.
   * @param pMessageParameters Objects that are used to parameterize the trace message. The matching
   * {@link ObjectFormatter} will be used to convert the passed objects into Strings. The parameter may be null.
   */
  void write( MessageID pMessageID, Object... pMessageParameters );

  /**
   * Method writes the message with the passed message id using its defined trace level.
   * 
   * @param pMessageID Id of the message that should be traced together with the passed message parameters. The
   * parameter may be null.
   * @param pThrowable gets forwarded to the specific method which process the Trace. For Example error(), warn(),
   * fatal()
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  void write( MessageID pMessageID, Throwable pThrowable, String... pMessageParameters );

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
  void write( MessageID pMessageID, Throwable pThrowable, Object... pMessageParameters );

  /**
   * Method writes the message with the passed message id using its defined trace level.
   * 
   * @param pMessageID Id of the message that should be traced together with the passed message parameters. The
   * parameter may be null.
   * @param pApplicationException get forwarded to the specific method which process the Trace. For Example to error(),
   * warn(), fatal()
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  void write( MessageID pMessageID, ApplicationException pApplicationException, String... pMessageParameters );

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
  void write( MessageID pMessageID, ApplicationException pApplicationException, Object... pMessageParameters );

  /**
   * Method writes the message with the passed message id using its defined trace level.
   * 
   * @param pMessageID Id of the message that should be traced together with the passed message parameters. The
   * parameter may be null.
   * @param pSystemException get forwarded to the specific method which process the Trace. For Example to error(),
   * warn(), fatal()
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   */
  void write( MessageID pMessageID, SystemException pSystemException, String... pMessageParameters );

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
  void write( MessageID pMessageID, SystemException pSystemException, Object... pMessageParameters );

  /**
   * Method check if the passed trace level is enabled on this trace object.
   * 
   * @param pTraceLevel Trace level that should be checked. The parameter must not be null.
   * @return boolean The method returns true if the passed trace level is enabled on this trace object and false in all
   * other cases.
   */
  boolean isLevelEnabled( TraceLevel pTraceLevel );

  /**
   * Method returns the trace level that is currently enabled.
   * 
   * @return {@link TraceLevel} Trace level that is currently enabled. If tracing is completly disabled then this method
   * returns null.
   */
  TraceLevel getLevel( );
}
