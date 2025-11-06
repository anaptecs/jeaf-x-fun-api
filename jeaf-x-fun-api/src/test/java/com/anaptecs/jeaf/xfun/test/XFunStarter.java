/**
 * Copyright 2004 - 2020 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test;

import com.anaptecs.jeaf.xfun.api.XFun;

public class XFunStarter {

  public static void main( String[] args ) {
    long lStart = System.currentTimeMillis();
    int LOOPS = 10000;
    for (int i = 0; i < LOOPS; i++) {
      XFun.reload();
    }
    long lEnd = System.currentTimeMillis();
    long lDuration = lEnd - lStart;
    double lDurationPerCylce = (double) lDuration / LOOPS;
    System.out.println("Overall: " + lDuration + "ms. Duration per cycle: " + lDurationPerCylce + "ms.");
  }
}
