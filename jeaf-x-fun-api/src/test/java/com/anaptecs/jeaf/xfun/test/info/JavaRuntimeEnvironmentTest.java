/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.info;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.anaptecs.jeaf.xfun.api.XFunMessages;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFSystemException;
import com.anaptecs.jeaf.xfun.api.info.JavaRelease;
import com.anaptecs.jeaf.xfun.api.info.JavaRuntimeEnvironment;
import org.junit.jupiter.api.Test;

public class JavaRuntimeEnvironmentTest {
  @Test
  void testJRE( ) {
    JavaRuntimeEnvironment lJRE = new JavaRuntimeEnvironment("My Java Runtime", "Crazy Java Company", "17.1.2.4.5");
    assertEquals("My Java Runtime", lJRE.getRuntimeName());
    assertEquals("Crazy Java Company", lJRE.getVendor());
    assertEquals(JavaRelease.JAVA_17, lJRE.getJavaRelease());
    assertEquals("17.1.2.4.5", lJRE.getVersion());

    // Test exception handling.
    lJRE = new JavaRuntimeEnvironment("My Java Runtime", "Crazy Java Company", "47.11");
    assertEquals("My Java Runtime", lJRE.getRuntimeName());
    assertEquals("Crazy Java Company", lJRE.getVendor());
    assertEquals(JavaRelease.UNKNOWN, lJRE.getJavaRelease());
    assertEquals("47.11", lJRE.getVersion());

    // Test real exceptions
    try {
      new JavaRuntimeEnvironment(null, "Crazy Java Company", "17.1.2.4.5");
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pRuntimeName' must not be null.", e.getMessage());
    }
    try {
      new JavaRuntimeEnvironment("My Java Runtime", null, "17.1.2.4.5");
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pVendor' must not be null.", e.getMessage());
    }
    try {
      new JavaRuntimeEnvironment("My Java Runtime", "Crazy Java Company", null);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pVersion' must not be null.", e.getMessage());
    }
  }

  @Test
  void testJavaRelease( ) {
    assertEquals(JavaRelease.JAVA_5, JavaRelease.getJavaRelease("1.5.1_123"));
    assertEquals(JavaRelease.JAVA_6, JavaRelease.getJavaRelease("1.6"));
    assertEquals(JavaRelease.JAVA_7, JavaRelease.getJavaRelease("1.7.0.0.0.0.1"));
    assertEquals(JavaRelease.JAVA_8, JavaRelease.getJavaRelease("1.8."));
    assertEquals(JavaRelease.JAVA_9, JavaRelease.getJavaRelease("9.1.2.3.4.5"));
    assertEquals(JavaRelease.JAVA_10, JavaRelease.getJavaRelease("10"));
    assertEquals(JavaRelease.JAVA_11, JavaRelease.getJavaRelease("11."));
    assertEquals(JavaRelease.JAVA_12, JavaRelease.getJavaRelease("12.5.1"));
    assertEquals(JavaRelease.JAVA_13, JavaRelease.getJavaRelease("13.1.0_4711"));
    assertEquals(JavaRelease.JAVA_14, JavaRelease.getJavaRelease("14.0.1"));
    assertEquals(JavaRelease.JAVA_15, JavaRelease.getJavaRelease("15.1"));
    assertEquals(JavaRelease.JAVA_16, JavaRelease.getJavaRelease("16.7_199"));
    assertEquals(JavaRelease.JAVA_17, JavaRelease.getJavaRelease("17.5.1"));
    assertEquals(JavaRelease.JAVA_18, JavaRelease.getJavaRelease("18."));
    assertEquals(JavaRelease.JAVA_19, JavaRelease.getJavaRelease("19"));
    assertEquals(JavaRelease.JAVA_20, JavaRelease.getJavaRelease("20"));

    // Test unknown releases
    assertEquals(JavaRelease.UNKNOWN, JavaRelease.getJavaRelease("1.4.5.1_123"));
    assertEquals(JavaRelease.UNKNOWN, JavaRelease.getJavaRelease("21.0.1"));
    assertEquals(JavaRelease.UNKNOWN, JavaRelease.getJavaRelease("999.0.1"));
    assertEquals(JavaRelease.UNKNOWN, JavaRelease.getJavaRelease("MyVersion"));
  }

  @Test
  void testJavaReleaseLiterals( ) {
    assertEquals(5, JavaRelease.JAVA_5.getMajorVersionNumber());
    assertEquals(true, JavaRelease.JAVA_5.isLTSVersion());
    assertEquals(false, JavaRelease.JAVA_5.isUnknow());

    assertEquals(6, JavaRelease.JAVA_6.getMajorVersionNumber());
    assertEquals(true, JavaRelease.JAVA_6.isLTSVersion());
    assertEquals(false, JavaRelease.JAVA_6.isUnknow());

    assertEquals(7, JavaRelease.JAVA_7.getMajorVersionNumber());
    assertEquals(true, JavaRelease.JAVA_7.isLTSVersion());
    assertEquals(false, JavaRelease.JAVA_7.isUnknow());

    assertEquals(8, JavaRelease.JAVA_8.getMajorVersionNumber());
    assertEquals(true, JavaRelease.JAVA_8.isLTSVersion());
    assertEquals(false, JavaRelease.JAVA_8.isUnknow());

    assertEquals(9, JavaRelease.JAVA_9.getMajorVersionNumber());
    assertEquals(false, JavaRelease.JAVA_9.isLTSVersion());
    assertEquals(false, JavaRelease.JAVA_9.isUnknow());

    assertEquals(10, JavaRelease.JAVA_10.getMajorVersionNumber());
    assertEquals(false, JavaRelease.JAVA_10.isLTSVersion());
    assertEquals(false, JavaRelease.JAVA_10.isUnknow());

    assertEquals(11, JavaRelease.JAVA_11.getMajorVersionNumber());
    assertEquals(true, JavaRelease.JAVA_11.isLTSVersion());
    assertEquals(false, JavaRelease.JAVA_11.isUnknow());

    assertEquals(12, JavaRelease.JAVA_12.getMajorVersionNumber());
    assertEquals(false, JavaRelease.JAVA_12.isLTSVersion());
    assertEquals(false, JavaRelease.JAVA_12.isUnknow());

    assertEquals(13, JavaRelease.JAVA_13.getMajorVersionNumber());
    assertEquals(false, JavaRelease.JAVA_13.isLTSVersion());
    assertEquals(false, JavaRelease.JAVA_13.isUnknow());

    assertEquals(14, JavaRelease.JAVA_14.getMajorVersionNumber());
    assertEquals(false, JavaRelease.JAVA_14.isLTSVersion());
    assertEquals(false, JavaRelease.JAVA_14.isUnknow());

    assertEquals(15, JavaRelease.JAVA_15.getMajorVersionNumber());
    assertEquals(false, JavaRelease.JAVA_15.isLTSVersion());
    assertEquals(false, JavaRelease.JAVA_15.isUnknow());

    assertEquals(16, JavaRelease.JAVA_16.getMajorVersionNumber());
    assertEquals(false, JavaRelease.JAVA_16.isLTSVersion());
    assertEquals(false, JavaRelease.JAVA_16.isUnknow());

    assertEquals(17, JavaRelease.JAVA_17.getMajorVersionNumber());
    assertEquals(true, JavaRelease.JAVA_17.isLTSVersion());
    assertEquals(false, JavaRelease.JAVA_17.isUnknow());

    assertEquals(18, JavaRelease.JAVA_18.getMajorVersionNumber());
    assertEquals(false, JavaRelease.JAVA_18.isLTSVersion());
    assertEquals(false, JavaRelease.JAVA_18.isUnknow());

    assertEquals(19, JavaRelease.JAVA_19.getMajorVersionNumber());
    assertEquals(false, JavaRelease.JAVA_19.isLTSVersion());
    assertEquals(false, JavaRelease.JAVA_19.isUnknow());

    assertEquals(20, JavaRelease.JAVA_20.getMajorVersionNumber());
    assertEquals(false, JavaRelease.JAVA_20.isLTSVersion());
    assertEquals(false, JavaRelease.JAVA_20.isUnknow());

    assertEquals(-1, JavaRelease.UNKNOWN.getMajorVersionNumber());
    assertEquals(false, JavaRelease.UNKNOWN.isLTSVersion());
    assertEquals(true, JavaRelease.UNKNOWN.isUnknow());
  }

  @Test
  void testJavaReleaseVersionComparision( ) {
    assertEquals(true, JavaRelease.JAVA_11.isEqualOrHigher(JavaRelease.JAVA_11));
    assertEquals(true, JavaRelease.JAVA_11.isEqualOrHigher(JavaRelease.JAVA_10));
    assertEquals(false, JavaRelease.JAVA_11.isEqualOrHigher(JavaRelease.JAVA_12));
    assertEquals(false, JavaRelease.JAVA_11.isHigher(JavaRelease.JAVA_12));
    assertEquals(true, JavaRelease.JAVA_11.isHigher(JavaRelease.JAVA_10));
    assertEquals(false, JavaRelease.JAVA_11.isHigher(JavaRelease.JAVA_11));

    assertEquals(false, JavaRelease.JAVA_11.isEqualOrLower(JavaRelease.JAVA_10));
    assertEquals(true, JavaRelease.JAVA_11.isEqualOrLower(JavaRelease.JAVA_12));
    assertEquals(true, JavaRelease.JAVA_11.isEqualOrLower(JavaRelease.JAVA_20));
    assertEquals(false, JavaRelease.JAVA_11.isLower(JavaRelease.JAVA_11));
    assertEquals(true, JavaRelease.JAVA_11.isLower(JavaRelease.JAVA_12));
    assertEquals(false, JavaRelease.JAVA_11.isLower(JavaRelease.JAVA_10));

    // Test handling of unknown versions.
    try {
      JavaRelease.JAVA_11.isEqualOrHigher(JavaRelease.UNKNOWN);
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(XFunMessages.UNABLE_TO_COMPARE_UNKNOWN_JAVA_RELEASES, e.getErrorCode());
    }

    try {
      JavaRelease.UNKNOWN.isHigher(JavaRelease.JAVA_15);
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(XFunMessages.UNABLE_TO_COMPARE_UNKNOWN_JAVA_RELEASES, e.getErrorCode());
    }
    try {
      JavaRelease.UNKNOWN.isEqualOrHigher(JavaRelease.UNKNOWN);
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(XFunMessages.UNABLE_TO_COMPARE_UNKNOWN_JAVA_RELEASES, e.getErrorCode());
    }

    JavaRuntimeEnvironment lJRE = new JavaRuntimeEnvironment("My Java Runtime", "Crazy Java Company", "11.0.2");
    assertEquals(11, lJRE.getJavaRelease().getMajorVersionNumber());

    assertEquals(true, lJRE.isEqualOrHigher(JavaRelease.JAVA_11));
    assertEquals(true, lJRE.isEqualOrHigher(JavaRelease.JAVA_10));
    assertEquals(false, lJRE.isEqualOrHigher(JavaRelease.JAVA_12));
    assertEquals(false, lJRE.isHigher(JavaRelease.JAVA_12));
    assertEquals(true, lJRE.isHigher(JavaRelease.JAVA_10));
    assertEquals(false, lJRE.isHigher(JavaRelease.JAVA_11));

    assertEquals(false, lJRE.isEqualOrLower(JavaRelease.JAVA_10));
    assertEquals(true, lJRE.isEqualOrLower(JavaRelease.JAVA_12));
    assertEquals(true, lJRE.isEqualOrLower(JavaRelease.JAVA_20));
    assertEquals(false, lJRE.isLower(JavaRelease.JAVA_11));
    assertEquals(true, lJRE.isLower(JavaRelease.JAVA_12));
    assertEquals(false, lJRE.isLower(JavaRelease.JAVA_10));
  }
}
