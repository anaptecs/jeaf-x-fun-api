/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.anaptecs.jeaf.xfun.api.XFunRuntimeException;
import org.junit.jupiter.api.Test;

public class XFunRuntimeExceptionTest {
  @Test
  public void testXFunRuntimeException( ) {
    XFunRuntimeException lException = new XFunRuntimeException();
    assertNull(lException.getMessage());
    assertNull(lException.getCause());

    lException = new XFunRuntimeException("Ooops something went wrong.");
    assertEquals("Ooops something went wrong.", lException.getMessage());
    assertNull(lException.getCause());

    IllegalArgumentException lCause = new IllegalArgumentException("Illegal parameter null.");
    lException = new XFunRuntimeException("Ooops something went wrong.", lCause);
    assertEquals("Ooops something went wrong.", lException.getMessage());
    assertEquals(lCause, lException.getCause());

    lException = new XFunRuntimeException(lCause);
    assertEquals("java.lang.IllegalArgumentException: Illegal parameter null.", lException.getMessage());
    assertEquals(lCause, lException.getCause());
  }
}
