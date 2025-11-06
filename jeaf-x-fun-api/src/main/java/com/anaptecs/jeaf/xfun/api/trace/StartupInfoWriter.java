/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.trace;

import com.anaptecs.jeaf.xfun.annotations.StartupInfoWriterImpl;

/**
 * Generic interface for all classes that are capable of tracing startup info.
 * 
 * see {@link StartupInfoWriterImpl}
 */
public interface StartupInfoWriter {
  /**
   * Method returns the class whose initialization must haven been completed so that startup info of this writer should
   * be written. This is the trigger that this startup info writer will be called.
   * 
   * @return {@link Class} Class which must have finished its initialization.
   */
  Class<?> getStartupCompletedEventSource( );

  /**
   * Method will be called during startup and request to write what ever startup info is provided by the implementing
   * class.
   * 
   * This method will only be called in case that the used trace level in enabled.
   * 
   * @param pTrace Trace object that should be used for writing. The parameter is never null.
   * @param pTraceLevel Trace level may be used to control the level of detail that should be traced.
   * 
   * {@see Trace#writeInitInfo(String, TraceLevel)}
   */
  void traceStartupInfo( Trace pTrace, TraceLevel pTraceLevel );

}
