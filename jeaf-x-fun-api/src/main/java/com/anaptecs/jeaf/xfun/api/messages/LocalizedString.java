/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.messages;

/**
 * This class is part of JEAF dynamic multi language support. The class represents a localized string. Depending on the
 * current locale settings the class' toString method returns the appropriate localized string.
 */
public class LocalizedString extends LocalizedObject {
  /**
   * Serial version uid for this class.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Initialize object. Therefore the default value of the string and all of its localizations are required.
   * 
   * @param pDefaultString Default value of the string. This string will be used whenever no localization is available.
   * The parameter must not be null.
   * @param pLocalizedStrings Map contains all localizations of the string. The parameter may be null. In this case no
   * localizations are available.
   */
  public LocalizedString( int pLocalizationID ) {
    super(pLocalizationID);
  }
}
