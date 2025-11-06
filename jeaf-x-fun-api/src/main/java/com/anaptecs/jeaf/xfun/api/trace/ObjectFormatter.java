/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.trace;

import com.anaptecs.jeaf.xfun.annotations.TraceObjectFormatter;

/**
 * Interface defines a mechanism to format objects in traces. Implementations of this class are capable of formatting
 * the passed object into a specific trace output format.
 * 
 * In order to provide the right level of detail the current trace level will be passed when the object is called. It's
 * also possible to implement something like a context depending tracing. However as there is nothing like a generic
 * context what the current this has to solved by the individual implementation itself.
 * 
 * In order that implementation will be used it is required that it is marked by using annotation @TraceObjectFormatter.
 * For further details please refer to the documentation of annotation {@link TraceObjectFormatter}
 */
public interface ObjectFormatter<T> {
  /**
   * Method transforms the passed object into a string representation for tracing. The caller will ensure that the
   * method is only called for objects that match with the supported types that are defined in the annotation
   * {@link TraceObjectFormatter#supportedClasses()}.
   * 
   * The method will only be called if the used trace level for the used logger is enabled (we don't like garbage).
   * 
   * @param pObject Object that should be formatted. The parameter will never be null.
   * @param pTraceLevel In order to support trace level specific tracing the used trace level will passed to this
   * method. The parameter will never be null.
   * @return {@link String} String representation of the passed object. The method must not return null. However an
   * empty string is fine.
   */
  String formatObject( T pObject, TraceLevel pTraceLevel );
}
