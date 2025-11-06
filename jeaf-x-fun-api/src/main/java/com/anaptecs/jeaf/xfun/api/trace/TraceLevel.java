/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.trace;

/**
 * Enumeration defines the possible levels of tracing.
 * 
 * Hierarchy of the TraceLevels: TRACE > DEBUG > INFO > WARN > ERROR > FATAL
 */
public enum TraceLevel {
  /**
   * Enumeration literal defining the trace level TRACE.
   */
  TRACE,

  /**
   * Enumeration literal defining the trace level DEBUG.
   */
  DEBUG,

  /**
   * Enumeration literal defining the trace level INFO.
   */
  INFO,

  /**
   * Enumeration literal defining the trace level WARN.
   */
  WARN,

  /**
   * Enumeration literal defining the trace level ERROR.
   */
  ERROR,

  /**
   * Enumeration literal defining the trace level FATAL.
   */
  FATAL;

  /**
   * Method checks if the passed trace level has a higher priority than this level.
   * 
   * @param pTraceLevel Trace level that may have higher priority. The parameter must not be null.
   * @return boolean Method returns true if the priority of the passed trace level is higher than this one.
   */
  public boolean hasHigherPriority( TraceLevel pTraceLevel ) {
    return this.ordinal() < pTraceLevel.ordinal();
  }

  /**
   * Method checks if the passed trace level has a higher or equal priority than this level.
   * 
   * @param pTraceLevel Trace level that may have higher priority. The parameter must not be null.
   * @return boolean Method returns true if the priority of the passed trace level is higher or equal than this one.
   */
  public boolean hasHigherOrEqualPriority( TraceLevel pTraceLevel ) {
    return this.ordinal() <= pTraceLevel.ordinal();
  }

  /**
   * Method returns the trace level with the higher priority.
   * 
   * @param pTraceLevel Trace level. The parameter must not be null.
   * @return {@link TraceLevel} Trace level with the higher priority. The method never returns null.
   */
  public TraceLevel getLevelWithHigherPriority( TraceLevel pTraceLevel ) {
    TraceLevel lTraceLevel;
    if (this.hasHigherPriority(pTraceLevel) == true) {
      lTraceLevel = pTraceLevel;
    }
    else {
      lTraceLevel = this;
    }
    return lTraceLevel;
  }

  /**
   * Method returns the trace level with the lower priority.
   * 
   * @param pTraceLevel Trace level. The parameter must not be null.
   * @return {@link TraceLevel} Trace level with the lower priority. The method never returns null.
   */
  public TraceLevel getLevelWithLowerPriority( TraceLevel pTraceLevel ) {
    TraceLevel lTraceLevel;
    if (this.hasHigherOrEqualPriority(pTraceLevel) == false) {
      lTraceLevel = pTraceLevel;
    }
    else {
      lTraceLevel = this;
    }
    return lTraceLevel;
  }
}
