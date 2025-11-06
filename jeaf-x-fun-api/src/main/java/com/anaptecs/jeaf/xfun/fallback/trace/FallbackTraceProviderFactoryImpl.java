/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.fallback.trace;

import com.anaptecs.jeaf.xfun.api.trace.TraceProvider;
import com.anaptecs.jeaf.xfun.api.trace.TraceProviderFactory;

public class FallbackTraceProviderFactoryImpl implements TraceProviderFactory {
  private final TraceProvider traceProvider = new FallbackTraceProviderImpl();

  @Override
  public TraceProvider getTraceProvider( ) {
    return traceProvider;
  }

}
