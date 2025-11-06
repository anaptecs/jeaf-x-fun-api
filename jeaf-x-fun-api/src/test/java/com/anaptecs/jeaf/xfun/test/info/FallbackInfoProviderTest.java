/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.info;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.anaptecs.jeaf.xfun.api.info.InfoProvider;
import com.anaptecs.jeaf.xfun.api.info.InfoProviderFactory;
import com.anaptecs.jeaf.xfun.api.info.JavaRelease;
import com.anaptecs.jeaf.xfun.api.info.JavaRuntimeEnvironment;
import com.anaptecs.jeaf.xfun.api.info.OperatingSystem;
import com.anaptecs.jeaf.xfun.api.info.RuntimeEnvironment;
import com.anaptecs.jeaf.xfun.fallback.info.InfoProviderFactoryImpl;
import com.anaptecs.jeaf.xfun.fallback.info.InfoProviderImpl;

public class FallbackInfoProviderTest {
  @Test
  public void testFallbackInfoProvider( ) {
    InfoProviderFactory lFactory = new InfoProviderFactoryImpl();
    InfoProvider lInfoProvider = lFactory.getInfoProvider();
    assertEquals(InfoProviderImpl.class, lInfoProvider.getClass());

    // Test OS detection
    String lRealOS = System.getProperty("os.name");
    try {
      System.setProperty("os.name", "Windows 10");
      assertEquals(OperatingSystem.WINDOWS, lInfoProvider.getOperatingSystem());
      System.setProperty("os.name", "Linux");
      assertEquals(OperatingSystem.LINUX, lInfoProvider.getOperatingSystem());
      System.setProperty("os.name", "z/OS");
      assertEquals(OperatingSystem.OTHER, lInfoProvider.getOperatingSystem());
      System.setProperty("os.name", "OS390");
      assertEquals(OperatingSystem.OTHER, lInfoProvider.getOperatingSystem());
      System.setProperty("os.name", "Mac OS X");
      assertEquals(OperatingSystem.MAC, lInfoProvider.getOperatingSystem());
      System.setProperty("os.name", "IBM AIX 7.3");
      assertEquals(OperatingSystem.UNIX, lInfoProvider.getOperatingSystem());
      System.setProperty("os.name", "Oracle Solaris 11");
      assertEquals(OperatingSystem.UNIX, lInfoProvider.getOperatingSystem());
      System.setProperty("os.name", "Oracle Solaris 11");
      assertEquals(OperatingSystem.UNIX, lInfoProvider.getOperatingSystem());
      System.setProperty("os.name", "HP-UX 11i");
      assertEquals(OperatingSystem.UNIX, lInfoProvider.getOperatingSystem());
      System.setProperty("os.name", "Sun SunOS 5.x");
      assertEquals(OperatingSystem.UNIX, lInfoProvider.getOperatingSystem());
      System.setProperty("os.name", "SCO UNIX");
      assertEquals(OperatingSystem.UNIX, lInfoProvider.getOperatingSystem());
    }
    finally {
      System.setProperty("os.name", lRealOS);
    }

    assertEquals(RuntimeEnvironment.UNKNOWN, lInfoProvider.getRuntimeEnvironment());
  }

  @Test
  void testJREDetection( ) {
    // java.version=1.8.0_172
    final String lRealRuntimeName = System.getProperty("java.runtime.name");
    final String lRealVendor = System.getProperty("java.vm.vendor");
    final String lRealVersion = System.getProperty("java.version");

    try {
      // "My Java Runtime", "Crazy Java Company", "17.1.2.4.5"
      System.setProperty("java.runtime.name", "My Java Runtime");
      System.setProperty("java.vm.vendor", "Crazy Java Company");
      System.setProperty("java.version", "17.1.2.4.5");

      InfoProviderImpl lInfoProvider = new InfoProviderImpl();
      JavaRuntimeEnvironment lJRE = lInfoProvider.getJavaRuntimeEnvironment();
      assertEquals("My Java Runtime", lJRE.getRuntimeName());
      assertEquals("Crazy Java Company", lJRE.getVendor());
      assertEquals(JavaRelease.JAVA_17, lJRE.getJavaRelease());
      assertEquals("17.1.2.4.5", lJRE.getVersion());
    }
    finally {
      System.setProperty("java.runtime.name", lRealRuntimeName);
      System.setProperty("java.vm.vendor", lRealVendor);
      System.setProperty("java.version", lRealVersion);
    }

  }
}
