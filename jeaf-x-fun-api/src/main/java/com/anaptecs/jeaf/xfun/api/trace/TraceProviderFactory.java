/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.trace;

import com.anaptecs.jeaf.xfun.annotations.XFunConfig;

/**
 * Interface defines the factory to create a new trace provider implementation. Which factory should be used can be
 * configured using annotation {@link XFunConfig}
 */
public interface TraceProviderFactory {
  /**
   * Method creates a new instance of the trace provider implementation.
   * 
   * @return {@link TraceProvider} New instance of the message repository. The method must not return null.
   */
  TraceProvider getTraceProvider( );
}
