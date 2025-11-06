/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.messages;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.anaptecs.jeaf.xfun.bootstrap.Check;

/**
 * Class represents the localizations that belong to a localized object. Such message definitions consist of a default
 * message and may be existing loclizations.
 */
public class MessageDefinition implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   * Reference to localized object itself. Its localization id is used to identify the message texts.
   */
  private final LocalizedObject localizedObject;

  /**
   * Message format object containing the default message. The default message is used whenever no specific
   * localizations are applicable.
   */
  private final MessageFormat defaultMessage;

  /**
   * Map contains all localizations for the localized object.
   */
  private final Map<Locale, MessageFormat> localizedMessages;

  /**
   * Initialize object.
   * 
   * @param pLocalizedObject Localized object to which the message definition belongs to. The parameter must not be
   * null.
   * @param pDefaultMessage Default message for the localized object. The parameter must not be null.
   * @param pLocalizedMessages Map with all localizations. may be null.
   */
  public MessageDefinition( LocalizedObject pLocalizedObject, MessageFormat pDefaultMessage,
      Map<Locale, MessageFormat> pLocalizedMessages ) {

    // Check parameters.
    Check.checkInvalidParameterNull(pLocalizedObject, "pLocalizedObject");
    Check.checkInvalidParameterNull(pDefaultMessage, "pDefaultMessage");

    // Assign parameters to attributes.
    localizedObject = pLocalizedObject;
    defaultMessage = pDefaultMessage;
    if (pLocalizedMessages != null) {
      localizedMessages = Collections.unmodifiableMap(new HashMap<>(pLocalizedMessages));
    }
    else {
      localizedMessages = Collections.emptyMap();
    }
  }

  /**
   * Method returns the localized object to which this message definition belongs to.
   * 
   * @return {@link LocalizedObject} Localized object to whichb this message definition belongs to. The method never
   * returns null.
   */
  public LocalizedObject getLocalizedObject( ) {
    return localizedObject;
  }

  /**
   * Method returns the default message format of this message definition.
   * 
   * @return {@link MessageFormat} Default message format. The method never returns null.
   */
  public MessageFormat getDefaultMessage( ) {
    return defaultMessage;
  }

  /**
   * Method returns all localizations that belong to the localized object.
   * 
   * @return {@link Map} Map with all localizations. The method never returns null. The map is immutable.
   */
  public Map<Locale, MessageFormat> getLocalizedMessages( ) {
    return localizedMessages;
  }
}
