/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.info;

import java.io.Serializable;

import com.anaptecs.jeaf.xfun.bootstrap.Check;

/**
 * This class describes information about a running JEAF based application. Therefore every JEAF based application has
 * to provide the information of this class within its JEAF properties.
 */
public final class ApplicationInfo implements Serializable {
  /**
   * Serial version uid of this class.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Constant for fallback application info if the real application info could not be loaded. This object is used
   * whenever the real application info could not be loaded.
   */
  public static final ApplicationInfo UNKNOWN_APPLICATION = new ApplicationInfo("NO_APP_ID", "Unknow Application", null,
      null, ApplicationProvider.UNKNOW_APP_PROVIDER, VersionInfo.UNKNOWN_VERSION, true);

  /**
   * Unique ID of this application. The ID of an application never changes.
   */
  private final String applicationID;

  /**
   * Name of the application. The attribute is never null.
   */
  private final String name;

  /**
   * URL of the website about this application.
   */
  private final String websiteURL;

  /**
   * Optional description of the application.
   */
  private final String description;

  /**
   * Reference to object with information about the provider of the application.
   */
  private final ApplicationProvider applicationProvider;

  /**
   * Version information about this application.
   */
  private final VersionInfo version;

  /**
   * Flag indicates if this object is an dummy for an unknown application or not.
   */
  private final boolean isUnknownApplication;

  /**
   * String representation of this object.
   */
  private transient String asString;

  /**
   * Initialize object.
   * 
   * @param pApplicationID ID of the application. The parameter must not be null.
   * @param pName Name of the application. The parameter must not be null.
   * @param pCreator Name of the company / organization who created this application. The parameter must not be null.
   * @param pCreatorURL URL of the company / owner who created the application. The parameter may be null.
   * @param pWebsiteURL URL of the website about this application. The parameter may be null.
   * @param pDescription Description of the application. The parameter may be null.
   * @param pVersionInfo Additional version information about the application. The parameter must not be null.
   */
  public ApplicationInfo( String pApplicationID, String pName, String pWebsiteURL, String pDescription,
      ApplicationProvider pApplicationProvider, VersionInfo pVersionInfo ) {

    this(pApplicationID, pName, pWebsiteURL, pDescription, pApplicationProvider, pVersionInfo, false);
  }

  /**
   * Initialize object.
   * 
   * @param pApplicationID ID of the application. The parameter must not be null.
   * @param pName Name of the application. The parameter may be null.
   * @param pWebsiteURL URL of the website about this application. The parameter may be null.
   * @param pDescription Description of the application. The parameter may be null.
   * @param pApplicationProvider Information about the provider of the application. The parameter must not be null.
   * @param pVersionInfo Additional version information about the application. The parameter must not be null.
   * @param pIsUnknownApplication Parameter defines if this application is unknown.
   */
  private ApplicationInfo( String pApplicationID, String pName, String pWebsiteURL, String pDescription,
      ApplicationProvider pApplicationProvider, VersionInfo pVersionInfo, boolean pIsUnknownApplication ) {

    // Check parameters.
    Check.checkInvalidParameterNull(pApplicationID, "pApplicationID");
    Check.checkInvalidParameterNull(pApplicationProvider, "pApplicationProvider");
    Check.checkInvalidParameterNull(pVersionInfo, "pVersionInfo");

    applicationID = pApplicationID;
    name = pName;
    websiteURL = pWebsiteURL;
    description = pDescription;
    applicationProvider = pApplicationProvider;
    version = pVersionInfo;
    isUnknownApplication = pIsUnknownApplication;
  }

  /**
   * Method returns the unique ID of this application. The ID of an application may be used to reference the application
   * e.g. within the database.
   * 
   * @return {@link String} Unique ID of this application. The method never returns null.
   */
  public String getApplicationID( ) {
    return applicationID;
  }

  /**
   * Method returns the name of the application.
   * 
   * @return {@link String} Name of the application. The method never returns null.
   */
  public String getName( ) {
    return name;
  }

  /**
   * Method returns the URL of the website about this application.
   * 
   * @return {@link String} URL of the website about this application. The method may return null.
   */
  public String getWebsiteURL( ) {
    return websiteURL;
  }

  /**
   * Method returns the description of the application.
   * 
   * @return {@link String} Description of the application. The method may return null.
   */
  public String getDescription( ) {
    return description;
  }

  /**
   * Method returns the provider of this application.
   * 
   * @return {@link String} Creator of the application. The method never returns null.
   */
  public ApplicationProvider getApplicationProvider( ) {
    return applicationProvider;
  }

  /**
   * Method returns version information about the application.
   * 
   * @return {@link VersionInfo} Version information about the application. The method never returns null.
   */
  public VersionInfo getVersion( ) {
    return version;
  }

  /**
   * Method returns if this application info is a dummy for an unknown application or not.
   * 
   * @return Method returns <code>true</code> if this is an unknown application and otherwise <code>false</code>.
   */
  public boolean isUnknownApplication( ) {
    return isUnknownApplication;
  }

  /**
   * Method returns a string representation of this object.
   * 
   * @return {@link String} String representation of this object.
   */
  @Override
  public String toString( ) {
    if (asString == null) {
      StringBuilder lBuilder = new StringBuilder();
      lBuilder.append(name);
      lBuilder.append(" (");
      if (version.isUnknownVersion() == false) {
        lBuilder.append("Version: ");
        lBuilder.append(version.toString());
        lBuilder.append(", ");
      }
      lBuilder.append("App-ID: ");
      lBuilder.append(applicationID);
      lBuilder.append(")");
      asString = lBuilder.toString();
    }
    return asString;
  }
}
