/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.trace;

import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.common.ComponentID;

/**
 * Interface defines the methods of a trace provider. A trace provider is responsible to return the trace object for a
 * specific context.
 */
public interface TraceProvider {
  /**
   * Method returns the configured trace provider.
   * 
   * @return {@link TraceProvider} Trace provider that is used. The method never returns null.
   */
  public static TraceProvider getTraceProvider( ) {
    return XFun.getTraceProvider();
  }

  /**
   * Method returns the trace object for the logger with the passed name.
   * 
   * @param pLoggerName Name of the logger for which a trace object should be returned. The parameter must not be null.
   * @return {@link Trace} Trace object that should be used. The method never returns null.
   */
  Trace getTrace( String pLoggerName );

  /**
   * Method returns the trace object for the passed class.
   * 
   * @param pClass Class for which the trace object should be returned. The parameter must not be null.
   * @return {@link Trace} Trace object that should be used. The method never returns null.
   */
  Trace getTrace( Class<?> pClass );

  /**
   * Method returns the trace object for the component with the passed ID.
   * 
   * @param pComponentID ID of the component for which a trace object should be returned. The parameter must not be
   * null.
   * @return {@link Trace} Trace object that should be used. The method never returns null.
   */
  Trace getTrace( ComponentID pComponentID );

  /**
   * Method returns the current trace object that should be used.
   * 
   * @return {@link Trace} Trace object that should be used. The method never returns null.
   */
  Trace getCurrentTrace( );
}
