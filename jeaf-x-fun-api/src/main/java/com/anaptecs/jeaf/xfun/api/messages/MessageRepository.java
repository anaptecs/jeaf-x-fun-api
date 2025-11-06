/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.messages;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.errorhandling.ErrorCode;
import com.anaptecs.jeaf.xfun.api.errorhandling.SystemException;

/**
 * Interface represents a message repository. A message repository is capable of creating a message string based on a
 * localized objects and some optional parameters.
 */
public interface MessageRepository extends Serializable {
  /**
   * Method returns the configured message repository.
   * 
   * @return {@link MessageRepository} Message repository that is used. The method never returns null.
   */
  public static MessageRepository getMessageRepository( ) {
    return XFun.getMessageRepository();
  }

  /**
   * Method loads the message data into the repository that is contained in the resource file with the passed name.
   * 
   * @param pMessageResource Name of the resource file that should be loaded. The parameter must point to a file
   * containing the message data. The parameter must not be null. A message resource must not be loaded twice.
   * @throws SystemException if the message resource pMessageResource was already loaded or an error occurs during the
   * parsing process of the message resource.
   */
  void loadResource( String pMessageResource ) throws SystemException;

  /**
   * Method adds all messages of the passed message repository to this message repository. May be existing messages with
   * the same message id will be overwritten.
   * 
   * @param pMessages List with all message that should be added to the repository. The parameter must not be null.
   */
  void addAllMessages( List<MessageDefinition> pMessages );

  /**
   * Method returns all messages of the repository.
   * 
   * @return {@link List} List with all messages of the repository. The method never returns null.
   */
  List<MessageDefinition> getAllMessages( );

  /**
   * Method returns the MessageID object for the passed message code.
   * 
   * @param pLocalizationID Code of the localized object that should be returned.
   * @return {@link LocalizedObject} Localized object for the passed message code. The method never returns null.
   * @throws SystemException if no localized object exists for the passed id.
   */
  LocalizedObject getLocalizedObject( int pLocalizationID ) throws SystemException;

  /**
   * Method returns the MessageID object for the passed message code.
   * 
   * @param pMessageCode Message code whose message id should be returned.
   * @return {@link MessageID} MessageID object for the passed message code. The method never returns null.
   * @throws SystemException if no MessageID object exists for the passed message code.
   */
  MessageID getMessageID( int pMessageCode ) throws SystemException;

  /**
   * Method checks if a message with the passed message code is known inside inside the message repository.
   * 
   * @param pMessageCode Message code that should be checked.
   * @return boolean Method returns true if a message with the passed message code is known and false otherwise.
   */
  boolean existsMessage( int pMessageCode );

  /**
   * Method returns the ErrorCode object for the passed error code.
   * 
   * @param pErrorCode Error code whose ErrorCode object should be returned.
   * @return {@link ErrorCode} ErrorCode object for the passed error code. The method never returns null.
   * @throws SystemException if no ErrorCode object exists for the passed error code.
   */
  ErrorCode getErrorCode( int pErrorCode ) throws SystemException;

  /**
   * Method returns the LocalizedObject for the passed localization ID.
   * 
   * @param pLocalizationID Localization ID of the object that should be returned.
   * @return {@link LocalizedObject} LocalizedObject for the passed localization ID. The method never returns null.
   * @throws SystemException if no LocalizedObject exists for the passed localization ID.
   */
  LocalizedString getLocalizedString( int pLocalizationID ) throws SystemException;

  /**
   * Method returns a parameterized message for the passed message id. The method uses the current default locale to
   * localize the message text.
   * 
   * @param pLocalizedObject Localized object to identify the parameterized message that should be returned. The
   * parameter must not be null.
   * @param pMessageParameters Parameter values that should be used to create the parameterized message. If the message
   * does not need any parameters pMessageParameters may also be null.
   * @return String Message that was created using the passed message id and the message parameters and the current
   * default locale. The method never returns null.
   * 
   * @see #getMessage(MessageID, Locale, String[])
   */
  String getMessage( LocalizedObject pLocalizedObject, String... pMessageParameters );

  /**
   * Method returns a parameterized message for the passed message id. The method uses the current default locale to
   * localize the message text.
   * 
   * @param pLocalizedObject Localized object to identify the parameterized message that should be returned. The
   * parameter must not be null.
   * @param pLocale Locale in which the message should be returned. The parameter must not be null.
   * @param pMessageParameters Parameter values that should be used to create the parameterized message. If the message
   * does not need any parameters pMessageParameters may also be null.
   * @return String Message that was created using the passed message id and the message parameters and the current
   * default locale. The method never returns null.
   * 
   * @see #getMessage(MessageID, Locale, String[])
   */
  String getMessage( LocalizedObject pLocalizedObject, Locale pLocale, String... pMessageParameters );

  /**
   * Method returns a parameterized trace message for the passed message id. The difference between a "normal" message
   * and a trace message is that in case of the trace message the locale that is defined for tracing will be used. A
   * trace message locale is especially helpful for application with several languages.
   * 
   * @param pLocalizedObject Localized object to identify the parameterized message that should be returned. The
   * parameter must not be null.
   * @param pMessageParameters Parameter values that should be used to create the parameterized message. If the message
   * does not need any parameters pMessageParameters may also be null.
   * @return String Message that was created using the passed message id and the message parameters and the current
   * default locale. The method never returns null.
   * 
   * @see #getMessage(MessageID, Locale, String[])
   */
  String getTraceMessage( LocalizedObject pLocalizedObject, String... pMessageParameters );
}
