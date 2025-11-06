/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.info;

import java.io.Serializable;

/**
 * This class describes information about the provider of a JEAF-based application. Therefore every JEAF based
 * application has to provide the information of this class within its JEAF properties.
 */
public final class ApplicationProvider implements Serializable {
  /**
   * Serial version uid of this class.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Constant for an unknown application provider.
   */
  public static final ApplicationProvider UNKNOW_APP_PROVIDER = new ApplicationProvider("Unkown Provider", null);

  /**
   * Name of the company / owner who created the application.
   */
  private final String creator;

  /**
   * URL of the company / owner who created the application.
   */
  private final String creatorURL;

  /**
   * Initialize object.
   * 
   * @param pCreator Name of the company / organization who created this application. The parameter must not be null.
   * @param pCreatorURL URL of the company / owner who created the application. The parameter may be null.
   */
  public ApplicationProvider( String pCreator, String pCreatorURL ) {
    creator = pCreator;
    creatorURL = pCreatorURL;
  }

  /**
   * Method returns the name of the company / organization who create the application.
   * 
   * @return {@link String} Creator of the application. The method never returns null.
   */
  public String getCreator( ) {
    return creator;
  }

  /**
   * Method returns the URL of the company / organization who create the application.
   * 
   * @return {@link String} URL of the creator of the application. The method may return null.
   */
  public String getCreatorURL( ) {
    return creatorURL;
  }
}
