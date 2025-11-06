/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.info;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.anaptecs.jeaf.xfun.api.info.VersionCompatibilityMode;
import com.anaptecs.jeaf.xfun.api.info.VersionInfo;

/**
 * Test class to test the functionality of class com.anaptecs.jeaf.common.util.VersionInfo. All version information are
 * read from property file test_version.proeprties.
 * 
 * @author JEAF Development Team
 * @version 1.0
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VersionInfoTest {
  @Test
  @Order(1)
  public void testVersionInfoCreation( ) {
    Date lDate = new GregorianCalendar(2018, 0, 27).getTime();
    VersionInfo lVersionInfo = new VersionInfo("1.2-SNAPSHOT", lDate);
    assertEquals("1.2-SNAPSHOT", lVersionInfo.toString(), "Wrong string representation");
    assertEquals(1, lVersionInfo.getMajorVersion(), "Wrong major version.");
    assertEquals(2, lVersionInfo.getMinorVersion(), "Wrong minor version.");
    assertNull(lVersionInfo.getBugfixLevel(), "Wrong bugfix level.");
    assertNull(lVersionInfo.getHotfixLevel(), "Wrong hotfix level.");
    assertNull(lVersionInfo.getBuildNumber(), "Wrong build number.");
    assertEquals(lDate, lVersionInfo.getCreationDate(), "Wrong release date");
    assertTrue(lVersionInfo.isSnapshotRelease(), "Wrong snapshot info");
    assertFalse(lVersionInfo.isUnknownVersion(), "Wrong unknown version info");
    assertNotNull(lVersionInfo.getCreationDate(), "Version date must not be null.");

    // Test version which also has bugfix level
    lVersionInfo = new VersionInfo("1.2.47", lDate);
    assertEquals("1.2.47", lVersionInfo.toString(), "Wrong string representation");
    assertEquals(1, lVersionInfo.getMajorVersion(), "Wrong major version.");
    assertEquals(2, lVersionInfo.getMinorVersion(), "Wrong minor version.");
    assertEquals(47, lVersionInfo.getBugfixLevel(), "Wrong bugfix level.");
    assertNull(lVersionInfo.getHotfixLevel(), "Wrong hotfix level.");
    assertNull(lVersionInfo.getBuildNumber(), "Wrong build number.");
    assertEquals(lDate, lVersionInfo.getCreationDate(), "Wrong release date");
    assertFalse(lVersionInfo.isSnapshotRelease(), "Wrong snapshot info");
    assertFalse(lVersionInfo.isUnknownVersion(), "Wrong unknown version info");
    assertNotNull(lVersionInfo.getCreationDate(), "Version date must not be null.");

    // Test version which also has hotfix level
    lVersionInfo = new VersionInfo("1.2.47.7", lDate);
    assertEquals("1.2.47.7", lVersionInfo.toString(), "Wrong string representation");
    assertEquals(1, lVersionInfo.getMajorVersion(), "Wrong major version.");
    assertEquals(2, lVersionInfo.getMinorVersion(), "Wrong minor version.");
    assertEquals(47, lVersionInfo.getBugfixLevel(), "Wrong bugfix level.");
    assertEquals(7, lVersionInfo.getHotfixLevel(), "Wrong hotfix level.");
    assertNull(lVersionInfo.getBuildNumber(), "Wrong build number.");
    assertEquals(lDate, lVersionInfo.getCreationDate(), "Wrong release date");
    assertFalse(lVersionInfo.isSnapshotRelease(), "Wrong snapshot info");
    assertFalse(lVersionInfo.isUnknownVersion(), "Wrong unknown version info");
    assertNotNull(lVersionInfo.getCreationDate(), "Version date must not be null.");

    // Test version which also has build number
    lVersionInfo = new VersionInfo("1.2.47.7.1999-SNAPSHOT", lDate);
    assertEquals("1.2.47.7.1999-SNAPSHOT", lVersionInfo.toString(), "Wrong string representation");
    assertEquals(1, lVersionInfo.getMajorVersion(), "Wrong major version.");
    assertEquals(2, lVersionInfo.getMinorVersion(), "Wrong minor version.");
    assertEquals(47, lVersionInfo.getBugfixLevel(), "Wrong bugfix level.");
    assertEquals(7, lVersionInfo.getHotfixLevel(), "Wrong hotfix level.");
    assertEquals(1999, lVersionInfo.getBuildNumber(), "Wrong build number.");
    assertEquals(lDate, lVersionInfo.getCreationDate(), "Wrong release date");
    assertTrue(lVersionInfo.isSnapshotRelease(), "Wrong snapshot info");
    assertFalse(lVersionInfo.isUnknownVersion(), "Wrong unknown version info");
    assertNotNull(lVersionInfo.getCreationDate(), "Version date must not be null.");

    // Test exception handling
    try {
      lVersionInfo = new VersionInfo("1.2.47.7.1999-SNAPSHO", lDate);
      fail("Exception to be thrown.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("Maven version string '1.2.47.7.1999-SNAPSHO' has invalid format.", e.getMessage(),
          "Wrong error message.");
    }

    try {
      lVersionInfo = new VersionInfo("1.", lDate);
      fail("Exception to be thrown.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("Maven version string '1.' has invalid format.", e.getMessage(), "Wrong error message.");
    }

    try {
      lVersionInfo = new VersionInfo("1.2", null);
      fail("Exception to be thrown.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pCreationDate' must not be null.", e.getMessage(), "Wrong error message.");
    }

    try {
      lVersionInfo = new VersionInfo(null, lDate);
      fail("Exception to be thrown.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pVersionString' must not be null.", e.getMessage(), "Wrong error message.");
    }
  }

  /**
   * Method tests JEAF's version info implementation for compatibility
   */
  @Test
  @Order(1)
  public void testVersionInfoCompatibility( ) {
    VersionInfo lVersionInfo = new VersionInfo("1.2.3.4", new Date());
    VersionInfo lSameVersion = new VersionInfo("1.2.3.4", new Date());
    VersionInfo lPreviousBugfixVersion = new VersionInfo("1.2.3.3", new Date());
    VersionInfo lPreviousMinorVersion = new VersionInfo("1.1.4.18", new Date());
    VersionInfo lGrannieVersion = new VersionInfo("1.1.8.9", new Date());
    VersionInfo lOutDatedVersion = new VersionInfo("1.0.2.7", new Date());
    VersionInfo lNewerVersion = new VersionInfo("1.3.0.0", new Date());
    VersionInfo lBrandNewVersion = new VersionInfo("2.0.0.0", new Date());

    // Test strict comparison
    VersionCompatibilityMode lStrict = VersionCompatibilityMode.STRICT;
    assertTrue(lVersionInfo.isCompatible(lSameVersion, lStrict), "Same versions must be compatible");
    assertFalse(lVersionInfo.isCompatible(lPreviousBugfixVersion, lStrict), "Previous version not detected.");
    assertFalse(lVersionInfo.isCompatible(lOutDatedVersion, lStrict), "Out-dated version not detected.");
    assertFalse(lVersionInfo.isCompatible(lGrannieVersion, lStrict), "Grannie version not detected.");
    assertFalse(lVersionInfo.isCompatible(lNewerVersion, lStrict), "Newer version not detected.");
    assertFalse(lVersionInfo.isCompatible(lBrandNewVersion, lStrict), "Brand new version not detected.");

    // Test tolerant comparison
    VersionCompatibilityMode lSemVer = VersionCompatibilityMode.SEMANTIC_VERSIONING;
    assertTrue(lVersionInfo.isCompatible(lSameVersion, lSemVer), "Same versions must be compatible");
    assertTrue(lVersionInfo.isCompatible(lPreviousBugfixVersion, lSemVer),
        "Previous bugfix version must be compatible.");
    assertFalse(lVersionInfo.isCompatible(lPreviousMinorVersion, lSemVer), "Previous version not detected.");
    assertFalse(lVersionInfo.isCompatible(lOutDatedVersion, lSemVer), "Out-dated version not detected.");
    assertFalse(lVersionInfo.isCompatible(lGrannieVersion, lSemVer), "Grannie version not detected.");
    assertTrue(lVersionInfo.isCompatible(lNewerVersion, lSemVer), "Newer version not detected.");
    assertFalse(lVersionInfo.isCompatible(lBrandNewVersion, lSemVer), "Brand new version not detected.");

    // Test also cases were bugfix level and hotfix level are not set.
    VersionInfo lVersion1 = new VersionInfo("1.2.4", new Date());
    VersionInfo lVersion2 = new VersionInfo("1.2.4", new Date());
    assertTrue(lVersion1.isCompatible(lVersion2, lStrict), "Versions are not compatible");

    // Version only differ on the last level
    lVersion1 = new VersionInfo("1.2.4.1", new Date());
    lVersion2 = new VersionInfo("1.2.4.2", new Date());
    assertFalse(lVersion1.isCompatible(lVersion2, lStrict), "Versions must not be compatible");
    lVersion1 = new VersionInfo("1.2.4.1", new Date());
    lVersion2 = new VersionInfo("1.2.4", new Date());
    assertFalse(lVersion1.isCompatible(lVersion2, lStrict), "Versions must not be compatible");
    assertFalse(lVersion2.isCompatible(lVersion1, lStrict), "Versions must not be compatible");

    lVersion1 = new VersionInfo("1.2", new Date());
    lVersion2 = new VersionInfo("1.2", new Date());
    assertTrue(lVersion1.isCompatible(lVersion2, lStrict), "Versions are not compatible");

    lVersion1 = new VersionInfo("1.2", new Date());
    lVersion2 = new VersionInfo("1.2.8", new Date());
    assertFalse(lVersion1.isCompatible(lVersion2, lStrict), "Versions must not be compatible");
    assertFalse(lVersion2.isCompatible(lVersion1, lStrict), "Versions must not be compatible");

    // Test exception handling
    try {
      lVersion1.isCompatible(null, lSemVer);
      fail("Exception to be thrown.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pVersionInfo' must not be null.", e.getMessage(), "Wrong error message.");
    }
    try {
      lVersion1.isCompatible(null, null);
      fail("Exception to be thrown.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pVersionInfo' must not be null.", e.getMessage(), "Wrong error message.");
    }
    try {
      lVersion1.isCompatible(lVersion2, null);
      fail("Exception to be thrown.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pVersionCompatibilityMode' must not be null.", e.getMessage(), "Wrong error message.");
    }
  }
}
