/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.anaptecs.jeaf.xfun.annotations.MessageResource;
import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.XFunConfiguration;
import com.anaptecs.jeaf.xfun.api.XFunMessages;
import com.anaptecs.jeaf.xfun.fallback.info.InfoProviderFactoryImpl;
import com.anaptecs.jeaf.xfun.test.trace.TestHandler;
import com.anaptecs.jeaf.xfun.test_default_runtime.DatatypeConverterRegistryFactoryImpl;
import com.anaptecs.jeaf.xfun.test_default_runtime.LocaleProviderFactoryImpl;
import com.anaptecs.jeaf.xfun.test_default_runtime.MessageRepositoryFactoryImpl;
import com.anaptecs.jeaf.xfun.test_default_runtime.PrincipalProviderFactoryImpl;
import com.anaptecs.jeaf.xfun.test_default_runtime.VerifierFactoryImpl;
import com.anaptecs.jeaf.xfun.test_default_runtime.XFunConfig;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class XFunConfigurationTest {

  @Test
  @Order(10)
  public void testXFunDefaultConfiguration( ) {
    XFunConfiguration lConfiguration = new XFunConfiguration();
    assertEquals(XFunConfig.class, lConfiguration.getConfigurationClass());
    assertEquals(0, lConfiguration.getConfigurationErrors().size());
    assertEquals(VerifierFactoryImpl.class, lConfiguration.getVerifierFactory().getClass());
    assertEquals(InfoProviderFactoryImpl.class, lConfiguration.getInfoProviderFactory().getClass());
    assertEquals(MessageRepositoryFactoryImpl.class, lConfiguration.getMessageRepositoryFactory().getClass());
    assertEquals(LocaleProviderFactoryImpl.class, lConfiguration.getLocaleProviderFactory().getClass());
    assertEquals(PrincipalProviderFactoryImpl.class, lConfiguration.getPrincipalProviderFactory().getClass());
    assertEquals(DatatypeConverterRegistryFactoryImpl.class,
        lConfiguration.getDatatypeConverterRegistryFactory().getClass());
    assertEquals(0, lConfiguration.getMessageResourceClasses().size());

    com.anaptecs.jeaf.xfun.annotations.XFunConfig lEmptyConfiguration = lConfiguration.getEmptyConfiguration();
    assertEquals(com.anaptecs.jeaf.xfun.annotations.XFunConfig.class, lEmptyConfiguration.annotationType());
    assertEquals(MessageResource.MESSAGE_RESOURCES_PATH, lEmptyConfiguration.messageResourcePath());
  }

  @Test
  @Order(20)
  public void testXFunCustomConfiguration( ) {
    XFunConfiguration lConfiguration =
        new XFunConfiguration("EMPTY_MESSAGE_RESOURCES_CONFIG", "META-INF/TEST/X-Fun/config", false);
    assertEquals(EmptyMessageResources.class, lConfiguration.getConfigurationClass());
    List<String> lConfigurationErrors = lConfiguration.getConfigurationErrors();
    assertEquals(6, lConfigurationErrors.size());
    assertEquals(
        "Unable to create new instance of configured class com.anaptecs.jeaf.xfun.api.messages.MessageRepositoryFactory. Configured class is an interface.",
        lConfigurationErrors.get(0));
    assertEquals(
        "Unable to create new instance of configured class com.anaptecs.jeaf.xfun.api.checks.VerifierFactory. Configured class is an interface.",
        lConfigurationErrors.get(1));
    assertEquals(
        "Unable to create new instance of configured class com.anaptecs.jeaf.xfun.api.locale.LocaleProviderFactory. Configured class is an interface.",
        lConfigurationErrors.get(2));
    assertEquals(
        "Unable to create new instance of configured class com.anaptecs.jeaf.xfun.api.principal.PrincipalProviderFactory. Configured class is an interface.",
        lConfigurationErrors.get(3));
    assertEquals(
        "Unable to create new instance of configured class com.anaptecs.jeaf.xfun.api.info.InfoProviderFactory. Configured class is an interface.",
        lConfigurationErrors.get(4));
    assertEquals(
        "Unable to create new instance of configured class com.anaptecs.jeaf.xfun.api.trace.TraceProviderFactory. Configured class is an interface.",
        lConfigurationErrors.get(5));

    assertEquals("META-INF/TEST/X-Fun/message_resources/NO_MESSAGE_RESOURCES", lConfiguration.messageResourcePath());
    // lConfiguration.getMessageResourceClasses() is called twice for full code coverage
    assertEquals(0, lConfiguration.getMessageResourceClasses().size());
    assertEquals(0, lConfiguration.getMessageResourceClasses().size());

    // As we have configuration errors, it's only expected to receive an fallback implementation of the info provider
    // factory.
    assertEquals(InfoProviderFactoryImpl.class, lConfiguration.getInfoProviderFactory().getClass());
    assertNull(lConfiguration.getVerifierFactory());
    assertNull(lConfiguration.getMessageRepositoryFactory());
    assertNull(lConfiguration.getLocaleProviderFactory());
    assertNull(lConfiguration.getPrincipalProviderFactory());
    assertNull(lConfiguration.getDatatypeConverterRegistryFactory());

    TestHandler lHandler = new TestHandler();
    LogManager lLogManager = LogManager.getLogManager();
    lLogManager.reset();
    Logger lRootLogger = lLogManager.getLogger("");
    lRootLogger.setLevel(Level.ALL);
    lRootLogger.addHandler(lHandler);
    XFun.getTrace().info("Hello World!");
    assertEquals("Hello World!", lHandler.getLastLogRecord().getMessage());
  }

  @Test
  @Order(30)
  public void testMessageResourceResolution( ) {
    // Good case.
    XFunConfiguration lConfiguration =
        new XFunConfiguration("CUSTOM_X_FUN_CONFIG", "META-INF/TEST/X-Fun/config", false);
    assertEquals(MessageResourcesAvailable.class, lConfiguration.getConfigurationClass());
    assertEquals("META-INF/TEST/X-Fun/message_resources/MESSAGE_RESOURCES", lConfiguration.messageResourcePath());
    assertEquals(2, lConfiguration.getMessageResourceClasses().size());
    assertEquals(XFunMessages.class, lConfiguration.getMessageResourceClasses().get(0));
    assertEquals(TestMessageResource.class, lConfiguration.getMessageResourceClasses().get(1));

    // Configuration contains classes that do not have annotation @MessageResource
    lConfiguration = new XFunConfiguration("INVALID_MESSAGE_RESOURCES_CONFIG", "META-INF/TEST/X-Fun/config", false);
    assertEquals(InvalidMessageResource.class, lConfiguration.getConfigurationClass());
    assertEquals("META-INF/TEST/X-Fun/message_resources/INVALID_MESSAGE_RESOURCES",
        lConfiguration.messageResourcePath());

    assertEquals(2, lConfiguration.getMessageResourceClasses().size());
    assertEquals(TestMessageResource.class, lConfiguration.getMessageResourceClasses().get(0));
    assertEquals(XFunMessages.class, lConfiguration.getMessageResourceClasses().get(1));

    // Test annotation with invalid message resource path.
    lConfiguration = new XFunConfiguration("INVALID_MESSAGE_RESOURCE_PATH_CONFIG", "META-INF/TEST/X-Fun/config", false);
    assertEquals(InvalidMessageResourcePath.class, lConfiguration.getConfigurationClass());
    assertEquals("META-INF/TEST/X-Fun/invalid_path/MESSAGE_RESOURCES", lConfiguration.messageResourcePath());

    List<String> lConfigurationErrors = lConfiguration.getConfigurationErrors();
    assertEquals(7, lConfigurationErrors.size());
    assertEquals(
        "Unable to create new instance of configured class com.anaptecs.jeaf.xfun.api.messages.MessageRepositoryFactory. Configured class is an interface.",
        lConfigurationErrors.get(0));
    assertEquals(
        "Unable to create new instance of configured class com.anaptecs.jeaf.xfun.api.checks.VerifierFactory. Configured class is an interface.",
        lConfigurationErrors.get(1));
    assertEquals(
        "Unable to create new instance of configured class com.anaptecs.jeaf.xfun.api.locale.LocaleProviderFactory. Configured class is an interface.",
        lConfigurationErrors.get(2));
    assertEquals(
        "Unable to create new instance of configured class com.anaptecs.jeaf.xfun.api.principal.PrincipalProviderFactory. Configured class is an interface.",
        lConfigurationErrors.get(3));
    assertEquals(
        "Unable to create new instance of configured class com.anaptecs.jeaf.xfun.api.info.InfoProviderFactory. Configured class is an interface.",
        lConfigurationErrors.get(4));
    assertEquals(
        "Unable to create new instance of configured class com.anaptecs.jeaf.xfun.api.trace.TraceProviderFactory. Configured class is an interface.",
        lConfigurationErrors.get(5));
    assertEquals(
        "Message resource configuration file META-INF/TEST/X-Fun/invalid_path/MESSAGE_RESOURCES is not available through the application's classpath.",
        lConfigurationErrors.get(6));
    assertEquals(0, lConfiguration.getMessageResourceClasses().size());
  }
}
