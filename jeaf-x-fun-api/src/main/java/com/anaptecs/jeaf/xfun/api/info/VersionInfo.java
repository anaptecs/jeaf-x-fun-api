/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.info;

import java.io.Serializable;
import java.util.Date;

import com.anaptecs.jeaf.xfun.bootstrap.Assert;
import com.anaptecs.jeaf.xfun.bootstrap.Check;

/**
 * Class can be used to provide information about the current version of an application or software component. Therefore
 * the class provides a version number that consists of a major version, minor version, bugfix level and hotfix level.
 * In addition the build number, creation date, name and vendor of the product are provided.
 */
public final class VersionInfo implements Serializable {
  /**
   * Constant for fallback version info if the real version info of an application could not be loaded. This version
   * info object is used whenever the real version info could not be loaded.
   */
  public static final VersionInfo UNKNOWN_VERSION = new VersionInfo(0, 0, new Date(), true, true);

  /**
   * Regular expression that defines how a version string is build.
   */
  public static final String VERSION_STRING_PATTERN =
      "^([0-9]+)\\.([0-9]+)(\\.[0-9]+)?(\\.[0-9]+)?(\\.[0-9]+)?(-SNAPSHOT)?";

  /**
   * Constant for Maven snapshot suffix a part of a version string.
   */
  private static final String SNAPSHOT_SUFFIX = "-SNAPSHOT";

  /**
   * Default serial version uid.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Attribute describes the major version of the application or software component. The attribute is always set since
   * this information is read from the property file that contains all version information when the object is created.
   */
  private final int majorVersion;

  /**
   * Attribute describes the minor version of the application or software component. The attribute is always set since
   * this information is read from the property file that contains all version information when the object is created.
   */
  private final int minorVersion;

  /**
   * Attribute describes the bugfix level of the application or software component. The attribute is always set since
   * this information is read from the property file that contains all version information when the object is created.
   */
  private final Integer bugfixLevel;

  /**
   * Attribute describes the hotfix level of the application or software component. The attribute is always set since
   * this information is read from the property file that contains all version information when the object is created.
   */
  private final Integer hotfixLevel;

  /**
   * Attribute contains the build number of the application or software component. The attribute is always set since
   * this information is read from the property file that contains all version information when the object is created.
   */
  private final Integer buildNumber;

  /**
   * Attribute contains a timestamp describing when the represented version was built. The attribute is always set since
   * this information is read from the property file that contains all version information when the object is created.
   */
  private final Date creationDate;

  /**
   * Attribute defines whether the version is a real release or only a snapshot version.
   */
  private final boolean snapshotRelease;

  /**
   * Flag indicates if this object is an dummy for an unknown version or not.
   */
  private final boolean isUnknownVersion;

  /**
   * Method creates a new version info object with the passed parameters.
   * 
   * @param pMajorVersion Major version. The value must be zero or greater.
   * @param pMinorVersion Minor version. The value must be zero or greater.
   * @param pCreationDate Creation date. The parameter must not be null.
   * @param pSnapshotRelease Parameter defines if the version is a snapshot version or not. <code>true</code> means that
   * the version is a snapshot version. <code>false</code> means that is is a real release.
   */
  private VersionInfo( int pMajorVersion, int pMinorVersion, Date pCreationDate, boolean pSnapshotRelease,
      boolean pIsUnknownVersion ) {

    // Assign values.
    majorVersion = pMajorVersion;
    minorVersion = pMinorVersion;
    bugfixLevel = null;
    hotfixLevel = null;
    buildNumber = null;
    creationDate = (Date) pCreationDate.clone();
    snapshotRelease = pSnapshotRelease;
    isUnknownVersion = pIsUnknownVersion;
  }

  /**
   * Method creates a new version info object from the passed version string.
   * 
   * @param pVersionString Version string that describes the version. The value must not be null and must comply with
   * the regular expression {@link #VERSION_STRING_PATTERN}.
   * @param pCreationDate Creation date. The parameter must not be null.
   */
  public VersionInfo( String pVersionString, Date pCreationDate ) {
    // Check all parameters
    Check.checkInvalidParameterNull(pVersionString, "pVersionString");
    Check.checkInvalidParameterNull(pCreationDate, "pCreationDate");

    // Check if version string matches to the defined pattern.
    if (pVersionString.matches(VersionInfo.VERSION_STRING_PATTERN) == true) {
      // Detect snapshot releases.
      snapshotRelease = pVersionString.endsWith(SNAPSHOT_SUFFIX);
      pVersionString = pVersionString.replace(SNAPSHOT_SUFFIX, "");

      // Build version info including build number.
      String[] lVersions = pVersionString.split("\\.");
      int lParts = lVersions.length;

      // Resolve major and minor version
      majorVersion = Integer.parseInt(lVersions[0]);
      minorVersion = Integer.parseInt(lVersions[1]);

      // Resolve bugfix level
      if (lParts >= 3) {
        bugfixLevel = Integer.parseInt(lVersions[2]);
      }
      else {
        bugfixLevel = null;
      }
      // Resolve hotfix level
      if (lParts >= 4) {
        hotfixLevel = Integer.parseInt(lVersions[3]);
      }
      else {
        hotfixLevel = null;
      }
      // Resolve build number
      if (lParts >= 5) {
        buildNumber = Integer.parseInt(lVersions[4]);
      }
      else {
        buildNumber = null;
      }
      creationDate = (Date) pCreationDate.clone();

      // As version is created from a version string it is not unknown.
      isUnknownVersion = false;
    }
    // Maven version has invalid format.
    else {
      throw new IllegalArgumentException("Maven version string '" + pVersionString + "' has invalid format.");
    }
  }

  /**
   * Method returns the major version of the application or software component.
   * 
   * @return int Major version of the application or software component.
   */
  public int getMajorVersion( ) {
    return majorVersion;
  }

  /**
   * Method returns the minor version of the application or software component.
   * 
   * @return int Minor version of the application or software component.
   */
  public int getMinorVersion( ) {
    return minorVersion;
  }

  /**
   * Method returns the bugfix level of the application or software component.
   * 
   * @return {@link Integer} Bugfix level of the application or software component. The method may return null.
   */
  public Integer getBugfixLevel( ) {
    return bugfixLevel;
  }

  /**
   * Method returns the hotfix level of the application or software component.
   * 
   * @return {@link Integer} Hotfix level of the application or software component. The method may return null.
   */
  public Integer getHotfixLevel( ) {
    return hotfixLevel;
  }

  /**
   * Method returns the build number of the application or software component.
   * 
   * @return {@link Integer} Build number of the software component. The method may return null.
   */
  public Integer getBuildNumber( ) {
    return buildNumber;
  }

  /**
   * Method return the creation date of the application or software component.
   * 
   * @return Calendar Creation date of the application or software component. The method never returns null.
   */
  public Date getCreationDate( ) {
    return (Date) creationDate.clone();
  }

  /**
   * Method returns the version information as String. This means that the method returns a string of the following
   * format: "'major version'.'minor version'.'bugfix level'.'hotfix level'
   * 
   * @return String Version of the JEAF framework as String. The method never returns null.
   */
  public String getVersionString( ) {
    StringBuilder lBuilder = new StringBuilder();
    lBuilder.append(majorVersion);
    lBuilder.append(".");
    lBuilder.append(minorVersion);
    if (bugfixLevel != null) {
      lBuilder.append(".");
      lBuilder.append(bugfixLevel);
    }
    if (hotfixLevel != null) {
      lBuilder.append(".");
      lBuilder.append(hotfixLevel);
    }
    if (buildNumber != null) {
      lBuilder.append(".");
      lBuilder.append(buildNumber);
    }
    String lSuffix;
    if (snapshotRelease == true) {
      lSuffix = SNAPSHOT_SUFFIX;
    }
    else {
      lSuffix = "";
    }
    lBuilder.append(lSuffix);
    return lBuilder.toString();
  }

  /**
   * Method returns whether this version object describes a snapshot or a real release.
   * 
   * @return {@link Boolean} <code>true</code> means that the version is a snapshot version. <code>false</code> means
   * that is is a real release.
   */
  public boolean isSnapshotRelease( ) {
    return snapshotRelease;
  }

  /**
   * Method returns if this version info is a dummy for an unknown version or not.
   * 
   * @return Method returns <code>true</code> if this is an unknown version and otherwise <code>false</code>.
   */
  public boolean isUnknownVersion( ) {
    return isUnknownVersion;
  }

  /**
   * Method checks if the passed version is compatible with this one. The method knows two modes. In strict mode both
   * versions have to be identical in major, minor version and in bugfix and hotfix level. In non strict mode then rules
   * of semantic versioning are applied (see https://semver.org/).
   * 
   * According to the concept of SemVer a follow up version must be compatible to previous version e.g. 2.3 is
   * compatible with 2.2 but if you already use 2.3 then it is not ensured that you can also work with 2.2, so its not
   * compatible. As this VersionInfo object is the baseline for the check it means that the passed version must have the
   * same of higher minor version and of course the same major version. All the further details are not relevant in case
   * of SemVer.
   * 
   * @param pVersionInfo Version info that should be checked. The parameter must not be null.
   * @param pVersionCompatibilityMode Depending on the parameter the check either uses the strict or the SemVer variant.
   * @return boolean Method returns true if both versions are compatible and false if not.
   */
  public boolean isCompatible( VersionInfo pVersionInfo, VersionCompatibilityMode pVersionCompatibilityMode ) {
    // Check parameters.
    Check.checkInvalidParameterNull(pVersionInfo, "pVersionInfo");
    Check.checkInvalidParameterNull(pVersionCompatibilityMode, "pVersionCompatibilityMode");

    boolean lCompatible;
    switch (pVersionCompatibilityMode) {
      // Run check in strict mode
      case STRICT:
        lCompatible = this.checkStrictCompatibility(pVersionInfo);
        break;

      // Run check in semantic versioning mode.
      case SEMANTIC_VERSIONING:
        lCompatible = this.checkSemanticVersioningCompatibility(pVersionInfo);
        break;

      // Internal error. Unexpected enumeration literal.
      default:
        Assert.unexpectedEnumLiteral(pVersionCompatibilityMode);
        lCompatible = false;
    }
    return lCompatible;
  }

  /**
   * Method executes compatibility test between this version and the passed one. This version is the baseline for the
   * check with the passed one. Two versions in the check will only be compatible if they are the same on all levels
   * (major version to hotfix level, build numbers are not relevant).
   * 
   * @param pVersionInfo Version that should be compared with this version. The parameter must not be null.
   * @return boolean Method returns true if this version and the passed version are compatible and otherwise false.
   */
  private boolean checkStrictCompatibility( VersionInfo pVersionInfo ) {
    // Compare major, minor version, bugfix and hotfix level for equality
    boolean lCompatible = majorVersion == pVersionInfo.majorVersion;
    lCompatible = lCompatible && (minorVersion == pVersionInfo.minorVersion);

    // Also check bugfix level
    if (lCompatible == true) {
      if (bugfixLevel != null && pVersionInfo.bugfixLevel != null) {
        lCompatible = bugfixLevel.equals(pVersionInfo.bugfixLevel);
      }
      else if (bugfixLevel == null && pVersionInfo.bugfixLevel == null) {
        // Nothing to do.
      }
      else {
        lCompatible = false;
      }
    }

    // Also check hotfix level
    if (lCompatible == true) {
      if (hotfixLevel != null && pVersionInfo.hotfixLevel != null) {
        lCompatible = hotfixLevel.equals(pVersionInfo.hotfixLevel);
      }
      else if (hotfixLevel == null && pVersionInfo.hotfixLevel == null) {
        // Nothing to do.
      }
      else {
        lCompatible = false;
      }
    }
    return lCompatible;

  }

  /**
   * Method executes compatibility test between this version and the passed one. This version is the baseline for the
   * check with the passed one. Two versions in the check will be checked according to the rules of semver.
   * 
   * @param pVersionInfo Version that should be compared with this version. The parameter must not be null.
   * @return boolean Method returns true if this version and the passed version are compatible and otherwise false.
   */
  private boolean checkSemanticVersioningCompatibility( VersionInfo pVersionInfo ) {
    // According to the concept of semantic version differences in the major version are not compatible.
    boolean lCompatible;
    if (majorVersion == pVersionInfo.majorVersion) {
      // According to the concept of SemVer a follow up version must be compatible to previous version e.g. 2.3 is
      // compatible with 2.2 but if you already use 2.3 then it is not ensured that you can also work with 2.2 so its
      // not compatible. As this VersionInfo object is the baseline for the check it means that the passed version
      // must have the same of higher minor version.
      if (minorVersion <= pVersionInfo.minorVersion) {
        lCompatible = true;
      }
      // Minor version of passed version info is smaller and thus incompatible.
      else {
        lCompatible = false;
      }
    }
    // Versions differ in major version and are thus incompatible.
    else {
      lCompatible = false;
    }
    return lCompatible;
  }

  /**
   * Method creates a string representation of this object. This means that the method returns a string of the following
   * format: "'major version'.'minor version'.'bugfix level'.'hotfix level' (Build: 'build number')
   * 
   * @return String Version of the JEAF framework as String. The method never returns null.
   * @see java.lang.Object#toString()
   */
  public String toString( ) {
    return this.getVersionString();
  }
}
