/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.trace;

import com.anaptecs.jeaf.xfun.annotations.TraceObjectFormatter;
import com.anaptecs.jeaf.xfun.api.trace.ObjectFormatter;
import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;

@TraceObjectFormatter(supportedClasses = { Object.class })
public class BrokenTraceFormatter implements ObjectFormatter<Object> {
  /**
   * Constructor is private in order to cause trouble when trying to load formatters.
   */
  private BrokenTraceFormatter( ) {
  }

  @Override
  public String formatObject( Object pObject, TraceLevel pTraceLevel ) {
    return pObject.toString();
  }
}
