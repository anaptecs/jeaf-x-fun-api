/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.trace;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.anaptecs.jeaf.xfun.api.trace.Trace;
import org.junit.jupiter.api.Test;

public class TraceTest {

  @Test
  void testTraceViaInterface( ) {
    Trace lTrace = Trace.getTrace();
    assertNotNull(lTrace);
    lTrace.info("Hello World!");
  }
}
