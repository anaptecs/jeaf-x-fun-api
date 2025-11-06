/**
 * Copyright 2004 - 2020 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.config;

public class ExceptionInConstructorClass {
  public ExceptionInConstructorClass( ) {
    throw new Error("I'm expected ;-)");
  }
}
