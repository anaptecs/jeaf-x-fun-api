/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.checks.Check;

public class MulitThreadedInitTest {
  public static final int THREAD_COUNT = 2000;

  @Test
  public void testMultiThreadInit( ) throws Exception {
    // Create lot's of threads
    List<Thread> lThreads = new ArrayList<>(THREAD_COUNT);
    for (int i = 0; i < THREAD_COUNT; i++) {
      lThreads.add(new Thread(new XFunInitRunnable(XFunFunction.CONFIG)));
      lThreads.add(new Thread(new XFunInitRunnable(XFunFunction.DATATYPE)));
      lThreads.add(new Thread(new XFunInitRunnable(XFunFunction.INFO)));
      lThreads.add(new Thread(new XFunInitRunnable(XFunFunction.LOCALE)));
      lThreads.add(new Thread(new XFunInitRunnable(XFunFunction.MESSAGE)));
      lThreads.add(new Thread(new XFunInitRunnable(XFunFunction.PRINCIPAL)));
      lThreads.add(new Thread(new XFunInitRunnable(XFunFunction.TRACE)));
      lThreads.add(new Thread(new XFunInitRunnable(XFunFunction.VERIFIER)));
      lThreads.add(new Thread(new XFunInitRunnable(XFunFunction.VERSION)));
    }

    // Run all threads
    // XFun.getTrace().info("Hello XFun");
    for (Thread lNext : lThreads) {
      lNext.start();
    }

    // Wait until all threads completed.
    while (true) {
      boolean lCompleted = true;
      for (Thread lNext : lThreads) {
        if (lNext.isAlive() == true) {
          lCompleted = false;
          break;
        }

      }
      // Wait a little until we check again
      Thread.sleep(30);
      if (lCompleted == true) {
        break;
      }
    }
  }
}

class XFunInitRunnable implements Runnable {
  private final XFunFunction function;

  public XFunInitRunnable( XFunFunction pXFunFunction ) {
    function = pXFunFunction;

  }

  @Override
  public void run( ) {
    // String lName = Thread.currentThread().getName();
    // System.out.println("Hello from thread " + lName);
    Check.checkInvalidParameterNull("", "pValue");

    switch (function) {
      case CONFIG:
        XFun.getConfigurationProvider();
        break;
      case DATATYPE:
        XFun.getDatatypeConverterRegistry();
        break;
      case INFO:
        XFun.getInfoProvider();
        break;
      case LOCALE:
        XFun.getLocaleProvider();
        break;
      case MESSAGE:
        XFun.getMessageRepository();
        break;
      case PRINCIPAL:
        XFun.getPrincipalProvider();
        break;
      case TRACE:
        XFun.getTraceProvider();
        break;
      case VERIFIER:
        XFun.getVerifier();
        break;
      case VERSION:
        XFun.getVersionInfo();
        break;
    }
    // Trace lTrace = XFun.getTrace();
    // lTrace.info("Hello from thread " + lName);
  }
}

enum XFunFunction {
  CONFIG, DATATYPE, INFO, LOCALE, MESSAGE, PRINCIPAL, TRACE, VERIFIER, VERSION;
}
