/**
 * Copyright 2004 - 2020 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.info;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Date;

import com.anaptecs.jeaf.xfun.api.info.ApplicationInfo;
import com.anaptecs.jeaf.xfun.api.info.ApplicationProvider;
import com.anaptecs.jeaf.xfun.api.info.VersionInfo;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApplicationInfoTest {
  @Test
  @Order(10)
  public void testApplicationInfo( ) {
    // Test unknown application
    ApplicationInfo lApplicationInfo = ApplicationInfo.UNKNOWN_APPLICATION;
    assertEquals("NO_APP_ID", lApplicationInfo.getApplicationID());
    assertEquals(ApplicationProvider.UNKNOW_APP_PROVIDER, lApplicationInfo.getApplicationProvider());
    assertEquals("Unknow Application", lApplicationInfo.getName());
    assertNull(lApplicationInfo.getDescription());
    assertEquals(VersionInfo.UNKNOWN_VERSION, lApplicationInfo.getVersion());
    assertNull(lApplicationInfo.getWebsiteURL());
    assertEquals(true, lApplicationInfo.isUnknownApplication());
    assertEquals("Unknow Application (App-ID: NO_APP_ID)", lApplicationInfo.toString());

    // Test custom application.
    ApplicationProvider lApplicationProvider = new ApplicationProvider("anaptecs GmbH", "https://www.anaptecs.de");
    assertEquals("anaptecs GmbH", lApplicationProvider.getCreator());
    assertEquals("https://www.anaptecs.de", lApplicationProvider.getCreatorURL());
    VersionInfo lVersionInfo = new VersionInfo("2.3.1", new Date());
    lApplicationInfo = new ApplicationInfo("my.app.id", "My Test Application", "https://my.app",
        "This is a wonderful test application.", lApplicationProvider, lVersionInfo);
    assertEquals("my.app.id", lApplicationInfo.getApplicationID());
    assertEquals(lApplicationProvider, lApplicationInfo.getApplicationProvider());
    assertEquals("My Test Application", lApplicationInfo.getName());
    assertEquals(false, lApplicationInfo.isUnknownApplication());
    assertEquals("This is a wonderful test application.", lApplicationInfo.getDescription());
    assertEquals(lVersionInfo, lApplicationInfo.getVersion());
    assertEquals(lApplicationInfo.getWebsiteURL(), lApplicationInfo.getWebsiteURL());
    assertEquals("My Test Application (Version: 2.3.1, App-ID: my.app.id)", lApplicationInfo.toString());

    // Test exception handling
    lApplicationInfo = new ApplicationInfo("APP_ID", null, null, null, lApplicationProvider, lVersionInfo);
    assertEquals("APP_ID", lApplicationInfo.getApplicationID());
    assertEquals(null, lApplicationInfo.getName());
    assertEquals(null, lApplicationInfo.getDescription());
    assertEquals(lApplicationProvider, lApplicationInfo.getApplicationProvider());
    assertEquals(false, lApplicationInfo.isUnknownApplication());
    assertEquals(lVersionInfo, lApplicationInfo.getVersion());
    assertEquals(lApplicationInfo.getWebsiteURL(), lApplicationInfo.getWebsiteURL());

    try {
      new ApplicationInfo("APP_ID", null, null, null, null, lVersionInfo);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pApplicationProvider' must not be null.", e.getMessage());
    }

    try {
      new ApplicationInfo("APP_ID", null, null, null, lApplicationProvider, null);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pVersionInfo' must not be null.", e.getMessage());
    }
    try {
      new ApplicationInfo(null, null, null, null, lApplicationProvider, lVersionInfo);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pApplicationID' must not be null.", e.getMessage());
    }
  }
}
