/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.messages;

import java.net.URL;

import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFBootstrapException;

/**
 * Class only defines constants for all elements and attributes that are defined within the DTD MessageData.dtd. However
 * the constants for the DTD elements are not part of the api and may change without notice.
 * 
 * @author JEAF Development Team
 * @version 1.0
 */
public final class MessageDataDTD {
  /**
   * Constructor of this class is private in order to ensure that no instances of this class can be created.
   */
  private MessageDataDTD( ) {
    // Nothing to do.
  }

  /**
   * Name of the DTD file in which all elements of this class are defined.
   */
  public static final String DTD_NAME = "MessageData.dtd";

  /**
   * System id of the DTD MessageData.dtd. The constant contains the location of the DTD file "MessageData.dtd" that
   * defines the structure of the XML files that contain the messages that are loaded to the message repository. The
   * system id is needed by the XML parser to resolve the DTD of an XML document during the paring process if the
   * DOCTYPE directive uses a relative URI to point to its DTD.
   */
  public static final String SYSTEM_ID = MessageDataDTD.locateFile(DTD_NAME);

  /**
   * Method is intended to be used to locate URL of system id of MessageData.dtd from applications classpath. For better
   * testability is available as static operation.
   * 
   * @param pFileName Name of the file that should be located. The parameter must not be null.
   * @return
   */
  public static String locateFile( String pFileName ) {
    // Locate DTD that defines structure of message resources.
    ClassLoader lClassLoader = MessageDataDTD.class.getClassLoader();
    URL lResourceURL = lClassLoader.getResource(pFileName);
    if (lResourceURL != null) {
      return lResourceURL.toExternalForm();
    }
    // Resource could not be found within the application class path.
    else {
      String lMessage = "Resource '" + pFileName + "' could not be found within the application class path.";
      throw new JEAFBootstrapException(lMessage);
    }
  }

  /**
   * String constant for root element of XML document that contains all configuration information for error messages
   * that are managed by this repository class.
   */
  public static final String ROOT = "Root";

  //
  // Constants for element "ClassInfo" and its attributes.
  //

  /**
   * String constant for element that contains all information that are required to generate a java class with exception
   * ids.
   */
  public static final String CLASS_INFO = "ClassInfo";

  /**
   * String constant for attribute of element CLASS_INFO. The attribute contains the name of the class that should be
   * generated.
   */
  public static final String CLASS_NAME = "className";

  /**
   * String constant for attribute of element CLASS_INFO. The attribute contains the name of the packge of the generated
   * class.
   */
  public static final String PACKAGE = "package";

  /**
   * String constant for attribute of element CLASS_INFO. The attribute contains a javadoc description of the generated
   * class.
   */
  public static final String CLASS_DESCRIPTION = "description";

  /**
   * String constant for attribute of element CLASS_INFO. The attribute contains the name for the javadoc author tag of
   * the generated class.
   */
  public static final String AUTHOR = "author";

  /**
   * String constant for attribute of element CLASS_INFO. The attribute contains the value for the javadoc version tag
   * of the generated class.
   */
  public static final String VERSION = "version";

  //
  // Constant for element "MessageFolder" and its attributes.
  //

  /**
   * String constant for element that can be used to group messages logically.
   */
  public static final String MESSAGE_FOLDER = "MessageFolder";

  /**
   * String constant for attribute of element MESSAGE_FOLDER. The attribute contains the name of the folder.
   */
  public static final String FOLDER_NAME = "name";

  //
  // Constant for element "Message" and its attributes.
  //

  /**
   * String constant for element that contains all data for one certain message.
   */
  public static final String MESSAGE = "Message";

  /**
   * String constant for attribute of element MESSAGE. The attribute contains the id of the defined message.
   */
  public static final String MESSAGE_ID = "messageID";

  /**
   * String constant for attribute of element MESSAGE. The attribute contains the type of the defined message.
   */
  public static final String TYPE = "type";

  /**
   * String constant for value of attribute TYPE in case of type INFO.
   */
  public static final String TYPE_INFO = "INFO";

  /**
   * String constant for value of attribute TYPE in case of type ERROR.
   */
  public static final String TYPE_ERROR = "ERROR";

  /**
   * String constant for value of attribute TYPE in case of type LOCALIZED_TEXT.
   */
  public static final String TYPE_LOCALIZED_STRING = "LOCALIZED_STRING";

  /**
   * String constant for attribute of element MESSAGE. The attribute contains the default text of the defined message.
   */
  public static final String MESSAGE_DEFAULT_TEXT = "defaultText";

  /**
   * String constant for attribute of element MESSAGE. The attribute contains the description of the defined message.
   */
  public static final String MESSAGE_DESCRIPTION = "description";

  /**
   * String constant for attribute of element MESSAGE. The attribute contains the trace level of the defined message.
   */
  public static final String MESSAGE_TRACE_LEVEL = "traceLevel";

  //
  // Constant for element "LocalizedText" and its attributes
  //

  /**
   * String constant for element that contains a localized message text for a specific locale.
   */
  public static final String LOCALIZED_MESSAGE = "LocalizedMessage";

  /**
   * String constant for attribute of element LOCALIZED_MESSAGE. The attribute contains the language for which the
   * locale specific message text should be used.
   */
  public static final String LANGUAGE = "language";

  /**
   * String constant for attribute of element LOCALIZED_MESSAGE. The attribute contains the country for which the locale
   * specific message text should be used.
   */
  public static final String COUNTRY = "country";

  /**
   * String constant for attribute of element LOCALIZED_MESSAGE. The attribute contains the variant of the locale for
   * which the specific message text should be used.
   */
  public static final String VARIANT = "variant";

  /**
   * String constant for attribute of element LOCALIZED_MESSAGE. The attribute contains the message text that will be
   * used for this specific locale.
   */
  public static final String LOCALIZED_TEXT = "localizedText";
}