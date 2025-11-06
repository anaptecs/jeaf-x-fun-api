/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.fallback.trace;

import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.common.ComponentID;
import com.anaptecs.jeaf.xfun.api.trace.StartupInfoWriter;
import com.anaptecs.jeaf.xfun.api.trace.Trace;
import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;
import com.anaptecs.jeaf.xfun.api.trace.TraceProvider;

public class FallbackTraceProviderImpl implements TraceProvider, StartupInfoWriter {
  /**
   * Reference to trace instance for JEAF.
   */
  public static final Trace EMERGENCY_TRACE;

  static {
    // Try to load tracing class from default implementation as fallback.
    Trace lTrace = null;
    try {
      @SuppressWarnings("unchecked")
      Class<? extends Trace> lClass =
          (Class<? extends Trace>) Class.forName("com.anaptecs.jeaf.xfun.impl.trace.DefaultTrace");
      lTrace = lClass.newInstance();
    }
    // Provided class could not be loaded.
    catch (ReflectiveOperationException e) {
      lTrace = new FallbackTraceImpl("XFUN_EMERGENCY_TRACE");
    }
    finally {
      EMERGENCY_TRACE = lTrace;
    }
  }

  @Override
  public Trace getTrace( String pLoggerName ) {
    return EMERGENCY_TRACE;
  }

  @Override
  public Trace getTrace( Class<?> pClass ) {
    return EMERGENCY_TRACE;
  }

  @Override
  public Trace getTrace( ComponentID pComponentID ) {
    return EMERGENCY_TRACE;
  }

  @Override
  public Trace getCurrentTrace( ) {
    return EMERGENCY_TRACE;
  }

  @Override
  public Class<?> getStartupCompletedEventSource( ) {
    return XFun.class;
  }

  @Override
  public void traceStartupInfo( Trace pTrace, TraceLevel pTraceLevel ) {
    pTrace.writeInitInfo("Using emergency trace implementation due to configuration problems.", pTraceLevel);
  }
}
