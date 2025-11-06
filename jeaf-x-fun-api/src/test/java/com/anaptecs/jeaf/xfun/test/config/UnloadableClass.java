/**
 * Copyright 2004 - 2020 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.config;

public class UnloadableClass {
  static {
    if (true) {
      System.out.println("I'm not loadable");
      throw new RuntimeException("I'm not loadable");
    }
  }
}
