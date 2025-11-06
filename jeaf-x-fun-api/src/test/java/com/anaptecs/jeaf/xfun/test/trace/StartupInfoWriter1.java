/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.trace;

import com.anaptecs.jeaf.xfun.annotations.StartupInfoWriterImpl;
import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.trace.StartupInfoWriter;
import com.anaptecs.jeaf.xfun.api.trace.Trace;
import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;

@StartupInfoWriterImpl
public class StartupInfoWriter1 implements StartupInfoWriter {

  @Override
  public Class<?> getStartupCompletedEventSource( ) {
    return XFun.class;
  }

  @Override
  public void traceStartupInfo( Trace pTrace, TraceLevel pTraceLevel ) {
    pTrace.writeInitInfo(this.getClass().getName(), pTraceLevel);
  }
}
