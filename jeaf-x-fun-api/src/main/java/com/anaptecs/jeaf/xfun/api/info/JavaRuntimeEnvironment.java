/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.info;

import com.anaptecs.jeaf.xfun.bootstrap.Check;

/**
 * Class provides information about the Java Runtime Environment.
 * 
 * @author JEAF Development Team
 */
public class JavaRuntimeEnvironment {
  /**
   * Name of the runtime environment.
   */
  private final String runtimeName;

  /**
   * Vendor of the Java runtime environment.
   */
  private final String vendor;

  /**
   * Java release.
   */
  private final JavaRelease javaRelease;

  /**
   * Java version.
   */
  private final String version;

  /**
   * Initialize object.
   * 
   * @param pRuntimeName Name of the runtime environment. The parameter must not be null.
   * @param pVendor Name of the JVM vendor. The parameter must not be null.
   * @param pVersion JVM version information as defined in Java's system properties.
   */
  public JavaRuntimeEnvironment( String pRuntimeName, String pVendor, String pVersion ) {
    // Check parameters
    Check.checkInvalidParameterNull(pRuntimeName, "pRuntimeName");
    Check.checkInvalidParameterNull(pVendor, "pVendor");
    Check.checkInvalidParameterNull(pVersion, "pVersion");

    runtimeName = pRuntimeName;
    vendor = pVendor;
    javaRelease = JavaRelease.getJavaRelease(pVersion);
    version = pVersion;
  }

  /**
   * Method returns the name of the Java Runtime Environment.
   * 
   * @return {@link String} Name of the runtime environment. The method never returns null.
   */
  public String getRuntimeName( ) {
    return runtimeName;
  }

  /**
   * Method returns the name of the JVM vendor.
   * 
   * @return {@link String} Name of the JVM vendor. The method never returns null.
   */
  public String getVendor( ) {
    return vendor;
  }

  /**
   * Method returns the major Java release. This value is derived from the overall JVM version.
   * 
   * @return {@link JavaRelease} Java Release of the JVM. The method never returns null.
   */
  public JavaRelease getJavaRelease( ) {
    return javaRelease;
  }

  /**
   * Method checks if this Java release is lower than the passed one.
   * 
   * Please be aware that comparisons is only possible if both compared versions are real versions. Literal
   * {@link JavaRelease#UNKNOWN} can not be used in comparisons.
   * 
   * @param pRelease Release that should be compared. That parameter must not be null.
   * @return boolean Method returns true is this version is lower than the passed one and false otherwise.
   */
  public boolean isLower( JavaRelease pRelease ) {
    return javaRelease.isLower(pRelease);
  }

  /**
   * Method checks if this Java release is lower or equals to the passed one.
   * 
   * Please be aware that comparisons is only possible if both compared versions are real versions. Literal
   * {@link JavaRelease#UNKNOWN} can not be used in comparisons.
   * 
   * @param pRelease Release that should be compared. That parameter must not be null.
   * @return boolean Method returns true is this version is lower or equal to the passed one and false otherwise.
   */
  public boolean isEqualOrLower( JavaRelease pRelease ) {
    return javaRelease.isEqualOrLower(pRelease);
  }

  /**
   * Method checks if this Java release is higher than the passed one.
   * 
   * Please be aware that comparisons is only possible if both compared versions are real versions. Literal
   * {@link JavaRelease#UNKNOWN} can not be used in comparisons.
   * 
   * @param pRelease Release that should be compared. That parameter must not be null.
   * @return boolean Method returns true is this version is higher than the passed one and false otherwise.
   */
  public boolean isHigher( JavaRelease pRelease ) {
    return javaRelease.isHigher(pRelease);
  }

  /**
   * Method checks if this Java release is higher or equal to the passed one.
   * 
   * Please be aware that comparisons is only possible if both compared versions are real versions. Literal
   * {@link JavaRelease#UNKNOWN} can not be used in comparisons.
   * 
   * @param pRelease Release that should be compared. That parameter must not be null.
   * @return boolean Method returns true is this version is higher or equal compared to the passed one and false
   * otherwise.
   */
  public boolean isEqualOrHigher( JavaRelease pRelease ) {
    return javaRelease.isEqualOrHigher(pRelease);
  }

  /**
   * Method returns the full version of the JVM.
   * 
   * @return {@link String} Full version of the JVM. The method never returns null.
   */
  public String getVersion( ) {
    return version;
  }
}
