/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.anaptecs.jeaf.xfun.api.XFunRuntimeException;
import com.anaptecs.jeaf.xfun.api.trace.Trace;
import org.junit.jupiter.api.Test;

public class XFunEverythingMissingTest {
  @Test
  void testXFunAccess( ) {
    try {
      Trace.getTrace().info("Hello JEAF X-Fun, where are you?");
      fail();
    }
    catch (ExceptionInInitializerError e) {
      assertEquals(XFunRuntimeException.class, e.getException().getClass());
    }
  }
}
