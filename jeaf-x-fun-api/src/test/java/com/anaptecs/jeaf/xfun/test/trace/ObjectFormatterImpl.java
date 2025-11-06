/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.trace;

import com.anaptecs.jeaf.xfun.annotations.TraceObjectFormatter;
import com.anaptecs.jeaf.xfun.api.trace.ObjectFormatter;
import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;

@TraceObjectFormatter(supportedClasses = { String.class, Integer.class, ParentClass.class })
public class ObjectFormatterImpl implements ObjectFormatter<Object> {

  @Override
  public String formatObject( Object pObject, TraceLevel pTraceLevel ) {
    return pObject.toString();
  }
}
