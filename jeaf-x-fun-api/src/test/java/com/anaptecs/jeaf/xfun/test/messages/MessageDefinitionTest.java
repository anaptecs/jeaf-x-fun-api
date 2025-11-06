/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFBootstrapException;
import com.anaptecs.jeaf.xfun.api.messages.LocalizedString;
import com.anaptecs.jeaf.xfun.api.messages.MessageDataDTD;
import com.anaptecs.jeaf.xfun.api.messages.MessageDefinition;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MessageDefinitionTest {
  @Test
  @Order(10)
  public void testMessageDefinition( ) {
    MessageFormat lDefaultMessage = new MessageFormat("Hello World!");
    Map<Locale, MessageFormat> lLocalizedMessages = new HashMap<>();
    lLocalizedMessages.put(Locale.GERMAN, new MessageFormat("Hallo Welt!"));
    lLocalizedMessages.put(Locale.FRENCH, new MessageFormat("Bonjour le monde!"));
    LocalizedString lLocalizedObject = new LocalizedString(789);
    MessageDefinition lMessageDefinition = new MessageDefinition(lLocalizedObject, lDefaultMessage, lLocalizedMessages);

    assertEquals(lLocalizedObject, lMessageDefinition.getLocalizedObject());
    assertEquals(789, lMessageDefinition.getLocalizedObject().getLocalizationID());
    assertEquals(lDefaultMessage, lMessageDefinition.getDefaultMessage());
    assertEquals(lLocalizedMessages, lMessageDefinition.getLocalizedMessages());

    lMessageDefinition = new MessageDefinition(lLocalizedObject, lDefaultMessage, null);
    assertEquals(lLocalizedObject, lMessageDefinition.getLocalizedObject());
    assertEquals(789, lMessageDefinition.getLocalizedObject().getLocalizationID());
    assertEquals(lDefaultMessage, lMessageDefinition.getDefaultMessage());
    assertNotNull(lMessageDefinition.getLocalizedMessages());
    assertEquals(0, lMessageDefinition.getLocalizedMessages().size());

    // Test error cases.
    try {
      new MessageDefinition(null, lDefaultMessage, null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pLocalizedObject' must not be null.", e.getMessage());
    }
    try {
      new MessageDefinition(lLocalizedObject, null, null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pDefaultMessage' must not be null.", e.getMessage());
    }
  }

  @Test
  @Order(20)
  public void testMessageDataDTD( ) {
    String lDTDLocation = MessageDataDTD.locateFile(MessageDataDTD.DTD_NAME);
    assertNotNull(lDTDLocation);

    try {
      MessageDataDTD.locateFile("unknown.xml");
      fail("Exception expected.");
    }
    catch (JEAFBootstrapException e) {
      assertEquals("Resource 'unknown.xml' could not be found within the application class path.", e.getMessage());
    }

    assertEquals("author", MessageDataDTD.AUTHOR);
    assertEquals("description", MessageDataDTD.CLASS_DESCRIPTION);
    assertEquals("ClassInfo", MessageDataDTD.CLASS_INFO);
    assertEquals("className", MessageDataDTD.CLASS_NAME);
    assertEquals("country", MessageDataDTD.COUNTRY);
    assertEquals("MessageData.dtd", MessageDataDTD.DTD_NAME);
    assertEquals("name", MessageDataDTD.FOLDER_NAME);
    assertEquals("language", MessageDataDTD.LANGUAGE);
    assertEquals("LocalizedMessage", MessageDataDTD.LOCALIZED_MESSAGE);
    assertEquals("localizedText", MessageDataDTD.LOCALIZED_TEXT);
    assertEquals("Message", MessageDataDTD.MESSAGE);
    assertEquals("defaultText", MessageDataDTD.MESSAGE_DEFAULT_TEXT);
    assertEquals("description", MessageDataDTD.MESSAGE_DESCRIPTION);
    assertEquals("MessageFolder", MessageDataDTD.MESSAGE_FOLDER);
    assertEquals("messageID", MessageDataDTD.MESSAGE_ID);
    assertEquals("traceLevel", MessageDataDTD.MESSAGE_TRACE_LEVEL);
    assertEquals("package", MessageDataDTD.PACKAGE);
    assertEquals("Root", MessageDataDTD.ROOT);
    assertEquals(lDTDLocation, MessageDataDTD.SYSTEM_ID);
    assertEquals("type", MessageDataDTD.TYPE);
    assertEquals("ERROR", MessageDataDTD.TYPE_ERROR);
    assertEquals("INFO", MessageDataDTD.TYPE_INFO);
    assertEquals("LOCALIZED_STRING", MessageDataDTD.TYPE_LOCALIZED_STRING);
    assertEquals("variant", MessageDataDTD.VARIANT);
    assertEquals("version", MessageDataDTD.VERSION);
  }
}
