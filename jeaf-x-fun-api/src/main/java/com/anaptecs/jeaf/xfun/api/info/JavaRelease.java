/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.info;

import com.anaptecs.jeaf.xfun.api.XFunMessages;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFSystemException;
import com.anaptecs.jeaf.xfun.bootstrap.Check;

/**
 * Enum represents the different type of Java Major versions. Please be aware that this enum will be extended from time
 * to time (based on the Java Community Release Process when a new JDK version will be release). So it's highly
 * recommended to make your code aware for the fact that there will be new literals of this enum in the future.
 * 
 * @author JEAF Development Team
 */
public enum JavaRelease {
  JAVA_5(5, true),

  JAVA_6(6, true),

  JAVA_7(7, true),

  JAVA_8(8, true),

  JAVA_9(9, false),

  JAVA_10(10, false),

  JAVA_11(11, true),

  JAVA_12(12, false),

  JAVA_13(13, false),

  JAVA_14(14, false),

  JAVA_15(15, false),

  JAVA_16(16, false),

  JAVA_17(17, true),

  JAVA_18(18, false),

  JAVA_19(19, false),

  JAVA_20(20, false),

  /**
   * In case that the major version is unknown then this literal will be used.
   */
  UNKNOWN(-1, false);

  /**
   * Major version number. It is mainly used to compare versions.
   */
  private final int majorVersionNumber;

  /**
   * Attribute defines if the represented Java release is an LTS (Long-term Support) release.
   */
  private final boolean ltsVersion;

  /**
   * Initialize object.
   * 
   * @param pMajorVersionNumber Major version number.
   * @param pLTSVersion Flag indicates if the version is an LTS version.
   */
  private JavaRelease( int pMajorVersionNumber, boolean pLTSVersion ) {
    majorVersionNumber = pMajorVersionNumber;
    ltsVersion = pLTSVersion;
  }

  /**
   * Method returns the major version as int representation.
   * 
   * @return int Numeric representation of the major release.
   */
  public int getMajorVersionNumber( ) {
    return majorVersionNumber;
  }

  /**
   * Method returns if the Java release is a release with long-term support (LTS).
   * 
   * @return boolean Method returns true if the version has long-term support and otherwise false.
   */
  public boolean isLTSVersion( ) {
    return ltsVersion;
  }

  /**
   * Method returns if this Java release is a unknown one.
   * 
   * @return boolean Method returns true if this Java release is a unknown one and false otherwise.
   */
  public boolean isUnknow( ) {
    return this == UNKNOWN;
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
    // Check for unknown releases. This also ensure that the passed release is not null.
    this.checkForUnknownReleases(this, pRelease);

    // Compare releases and return result.
    return majorVersionNumber < pRelease.majorVersionNumber;
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
    // Check for unknown releases. This also ensure that the passed release is not null.
    this.checkForUnknownReleases(this, pRelease);

    // Compare releases and return result.
    return majorVersionNumber <= pRelease.majorVersionNumber;
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
    // Check for unknown releases. This also ensure that the passed release is not null.
    this.checkForUnknownReleases(this, pRelease);

    // Compare releases and return result.
    return majorVersionNumber > pRelease.majorVersionNumber;
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
    // Check for unknown releases. This also ensure that the passed release is not null.
    this.checkForUnknownReleases(this, pRelease);

    // Compare releases and return result.
    return majorVersionNumber >= pRelease.majorVersionNumber;
  }

  /**
   * Method checks that none of the passed releases are unknown releases. If at least one of them represents an unknown
   * release then an exception will be thrown.
   * 
   * @param pFirstRelease
   * @param pSecondRelease
   */
  private void checkForUnknownReleases( JavaRelease pFirstRelease, JavaRelease pSecondRelease ) {
    Check.checkInvalidParameterNull(pFirstRelease, "pFirstRelease");
    Check.checkInvalidParameterNull(pSecondRelease, "pSecondRelease");

    if (pFirstRelease.isUnknow() || pSecondRelease.isUnknow()) {
      throw new JEAFSystemException(XFunMessages.UNABLE_TO_COMPARE_UNKNOWN_JAVA_RELEASES, pFirstRelease.name(),
          pSecondRelease.name());
    }
  }

  /**
   * Method returns the Java release that matches to the passed version string.
   * 
   * @param pVersion String representation of the version. The parameter must not be null.
   * @return {@link JavaRelease} Java release that matches to the passed version. The method never returns null.
   */
  public static JavaRelease getJavaRelease( String pVersion ) {
    Check.checkInvalidParameterNull(pVersion, "pVersion");

    // Java versions 1.8 and below are using pattern 1.x
    String lMajorVersionString;
    if (pVersion.startsWith("1.")) {
      lMajorVersionString = pVersion.substring(2, 3);
    }
    // Starting from Java 9 pattern X.y.z is used.
    else {
      int lFirstSeparator = pVersion.indexOf(".");
      if (lFirstSeparator > 0) {
        lMajorVersionString = pVersion.substring(0, lFirstSeparator);
      }
      // passed version does not match to expected patterns
      else {
        lMajorVersionString = pVersion;
      }
    }

    // Try to resolve Java release
    JavaRelease lMajorRelease;
    try {
      int lMajorVersion = Integer.parseInt(lMajorVersionString);
      switch (lMajorVersion) {
        // Java 5
        case 5:
          lMajorRelease = JAVA_5;
          break;

        // Java 6
        case 6:
          lMajorRelease = JAVA_6;
          break;

        // Java 7
        case 7:
          lMajorRelease = JAVA_7;
          break;

        // Java 8
        case 8:
          lMajorRelease = JAVA_8;
          break;

        // Java 9
        case 9:
          lMajorRelease = JAVA_9;
          break;

        // Java 10
        case 10:
          lMajorRelease = JAVA_10;
          break;

        // Java 11
        case 11:
          lMajorRelease = JAVA_11;
          break;

        // Java 12
        case 12:
          lMajorRelease = JAVA_12;
          break;

        // Java 13
        case 13:
          lMajorRelease = JAVA_13;
          break;

        // Java 14
        case 14:
          lMajorRelease = JAVA_14;
          break;

        // Java 15
        case 15:
          lMajorRelease = JAVA_15;
          break;

        // Java 16
        case 16:
          lMajorRelease = JAVA_16;
          break;

        // Java 17
        case 17:
          lMajorRelease = JAVA_17;
          break;

        // Java 18
        case 18:
          lMajorRelease = JAVA_18;
          break;

        // Java 19
        case 19:
          lMajorRelease = JAVA_19;
          break;

        // Java 20
        case 20:
          lMajorRelease = JAVA_20;
          break;

        // Java release not known
        default:
          lMajorRelease = UNKNOWN;
      }
    }
    catch (NumberFormatException e) {
      lMajorRelease = UNKNOWN;
    }
    return lMajorRelease;
  }
}
