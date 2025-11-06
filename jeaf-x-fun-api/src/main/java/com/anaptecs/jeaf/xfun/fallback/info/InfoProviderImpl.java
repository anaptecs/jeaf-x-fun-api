/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.fallback.info;

import com.anaptecs.jeaf.xfun.api.info.ApplicationInfo;
import com.anaptecs.jeaf.xfun.api.info.InfoProvider;
import com.anaptecs.jeaf.xfun.api.info.JavaRuntimeEnvironment;
import com.anaptecs.jeaf.xfun.api.info.OperatingSystem;
import com.anaptecs.jeaf.xfun.api.info.RuntimeEnvironment;

public class InfoProviderImpl implements InfoProvider {
  /**
   * Information about current Java Runtime Environment. As this information is valid until the JVM exits we can resolve
   * it once and then keep it.
   */
  private JavaRuntimeEnvironment javaRuntimeEnvironment;

  @Override
  public ApplicationInfo getApplicationInfo( ) {
    return ApplicationInfo.UNKNOWN_APPLICATION;
  }

  /**
   * Method returns the operating system under which the application is currently executed.
   * 
   * @return {@link OperatingSystem} Operating system und which the application is currently executed. The method never
   * returns null.
   */
  @Override
  public OperatingSystem getOperatingSystem( ) {
    // Get current operating system name from system properties.
    String lOSName = System.getProperty("os.name").toLowerCase();

    // Any kind of Windows operating systems
    OperatingSystem lOperatingSystem;
    if (lOSName.indexOf("win") >= 0) {
      lOperatingSystem = OperatingSystem.WINDOWS;
    }
    // Mac OS
    else if (lOSName.indexOf("mac") >= 0) {
      lOperatingSystem = OperatingSystem.MAC;
    }
    // Any UNIX
    else if (lOSName.indexOf("nix") >= 0 || lOSName.indexOf("aix") >= 0 || lOSName.indexOf("sunos") >= 0
        || lOSName.indexOf("solaris") >= 0 || lOSName.indexOf("hp-ux") >= 0) {
      lOperatingSystem = OperatingSystem.UNIX;
    }
    // Linux
    else if (lOSName.indexOf("nux") >= 0) {
      lOperatingSystem = OperatingSystem.LINUX;
    }
    // Any other operating system.
    else {
      lOperatingSystem = OperatingSystem.OTHER;

    }
    return lOperatingSystem;
  }

  @Override
  public RuntimeEnvironment getRuntimeEnvironment( ) {
    return RuntimeEnvironment.UNKNOWN;
  }

  @Override
  public JavaRuntimeEnvironment getJavaRuntimeEnvironment( ) {
    // Java Runtime was not yet analyzed.
    if (javaRuntimeEnvironment == null) {
      String lRuntimeName = System.getProperty("java.runtime.name");
      String lVersion = System.getProperty("java.version");
      String lVendor = System.getProperty("java.vm.vendor");
      javaRuntimeEnvironment = new JavaRuntimeEnvironment(lRuntimeName, lVendor, lVersion);
    }
    return javaRuntimeEnvironment;
  }

}
