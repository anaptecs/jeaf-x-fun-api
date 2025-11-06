/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.XFunConfiguration;
import com.anaptecs.jeaf.xfun.api.XFunRuntimeException;
import com.anaptecs.jeaf.xfun.api.checks.Verifier;
import com.anaptecs.jeaf.xfun.api.config.ConfigurationProvider;
import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverterRegistry;
import com.anaptecs.jeaf.xfun.api.info.InfoProvider;
import com.anaptecs.jeaf.xfun.api.locale.LocaleProvider;
import com.anaptecs.jeaf.xfun.api.messages.MessageRepository;
import com.anaptecs.jeaf.xfun.api.principal.PrincipalProvider;
import com.anaptecs.jeaf.xfun.api.trace.Trace;
import com.anaptecs.jeaf.xfun.api.trace.TraceProvider;
import com.anaptecs.jeaf.xfun.fallback.checks.FallbackVerifierImpl;
import com.anaptecs.jeaf.xfun.fallback.info.InfoProviderImpl;
import com.anaptecs.jeaf.xfun.fallback.trace.FallbackTraceImpl;
import com.anaptecs.jeaf.xfun.fallback.trace.FallbackTraceProviderImpl;
import com.anaptecs.jeaf.xfun.test_default_runtime.AnotherXFunConfig;
import com.anaptecs.jeaf.xfun.test_default_runtime.ConfigurationProviderImpl;
import com.anaptecs.jeaf.xfun.test_default_runtime.DatatypeConverterRegistryImpl;
import com.anaptecs.jeaf.xfun.test_default_runtime.LocaleProviderImpl;
import com.anaptecs.jeaf.xfun.test_default_runtime.MessageRepositoryImpl;
import com.anaptecs.jeaf.xfun.test_default_runtime.PrincipalProviderImpl;
import com.anaptecs.jeaf.xfun.test_default_runtime.XFunConfig;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class XFunEntryPointTest {
  /**
   * Method test reloading functionality of JEAF X-Fun.
   */
  @Test
  public void testReloading( ) {
    // Ensure that standard version runs in default mode.
    assertNull(System.getProperty(XFun.X_FUN_CONFIG_RESOURCE_NAME));
    assertNull(System.getProperty(XFun.X_FUN_CONFIG_RESOURCE_PATH));
    assertNull(System.getProperty(XFun.X_FUN_CONFIG_EXCEPTION_ON_ERROR));

    // Test access via static interface methods.
    assertEquals(ConfigurationProviderImpl.class, ConfigurationProvider.getConfigurationProvider().getClass());
    assertEquals(DatatypeConverterRegistryImpl.class,
        DatatypeConverterRegistry.getDatatypeConverterRegistry().getClass());
    assertEquals(InfoProviderImpl.class, InfoProvider.getInfoProvider().getClass());
    assertEquals(LocaleProviderImpl.class, LocaleProvider.getLocaleProvider().getClass());
    assertEquals(MessageRepositoryImpl.class, MessageRepository.getMessageRepository().getClass());
    assertEquals(PrincipalProviderImpl.class, PrincipalProvider.getPrincipalProvider().getClass());
    assertEquals(FallbackTraceImpl.class, Trace.getTrace().getClass());
    assertEquals(FallbackTraceProviderImpl.class, TraceProvider.getTraceProvider().getClass());
    assertEquals(FallbackVerifierImpl.class, Verifier.getVerifier().getClass());

    XFunConfiguration lConfiguration = XFun.getConfiguration();
    assertEquals(XFunConfig.class, lConfiguration.getConfigurationClass());
    assertEquals(ConfigurationProviderImpl.class, XFun.getConfigurationProvider().getClass());
    assertEquals(DatatypeConverterRegistryImpl.class, XFun.getDatatypeConverterRegistry().getClass());
    assertEquals(InfoProviderImpl.class, XFun.getInfoProvider().getClass());
    assertEquals(LocaleProviderImpl.class, XFun.getLocaleProvider().getClass());
    assertEquals(MessageRepositoryImpl.class, XFun.getMessageRepository().getClass());
    assertEquals(PrincipalProviderImpl.class, XFun.getPrincipalProvider().getClass());
    assertEquals(FallbackTraceImpl.class, XFun.getTrace().getClass());
    assertEquals(FallbackTraceProviderImpl.class, XFun.getTraceProvider().getClass());
    assertEquals(FallbackVerifierImpl.class, XFun.getVerifier().getClass());

    // Reload but run without new configuration values
    XFun.reload();
    XFunConfiguration lOldConfiguration = lConfiguration;
    lConfiguration = XFun.getConfiguration();
    assertNotEquals(lConfiguration, lOldConfiguration);
    assertEquals(XFunConfig.class, lConfiguration.getConfigurationClass());
    assertEquals(ConfigurationProviderImpl.class, XFun.getConfigurationProvider().getClass());
    assertEquals(DatatypeConverterRegistryImpl.class, XFun.getDatatypeConverterRegistry().getClass());
    assertEquals(InfoProviderImpl.class, XFun.getInfoProvider().getClass());
    assertEquals(LocaleProviderImpl.class, XFun.getLocaleProvider().getClass());
    assertEquals(MessageRepositoryImpl.class, XFun.getMessageRepository().getClass());
    assertEquals(PrincipalProviderImpl.class, XFun.getPrincipalProvider().getClass());
    assertEquals(FallbackTraceImpl.class, XFun.getTrace().getClass());
    assertEquals(FallbackTraceProviderImpl.class, XFun.getTraceProvider().getClass());
    assertEquals(FallbackVerifierImpl.class, XFun.getVerifier().getClass());

    // Test reloading with changed configuration class by modifing system properties.
    System.setProperty(XFun.X_FUN_CONFIG_RESOURCE_NAME, "AnotherXFunConfig");
    System.setProperty(XFun.X_FUN_CONFIG_RESOURCE_PATH, "META-INF/TEST/X-Fun/config");
    System.setProperty(XFun.X_FUN_CONFIG_EXCEPTION_ON_ERROR, "true");

    XFun.reload();
    lOldConfiguration = lConfiguration;
    lConfiguration = XFun.getConfiguration();
    assertNotEquals(lConfiguration, lOldConfiguration);
    assertEquals(AnotherXFunConfig.class, lConfiguration.getConfigurationClass());
    assertEquals(ConfigurationProviderImpl.class, XFun.getConfigurationProvider().getClass());
    assertEquals(DatatypeConverterRegistryImpl.class, XFun.getDatatypeConverterRegistry().getClass());
    assertEquals(InfoProviderImpl.class, XFun.getInfoProvider().getClass());
    assertEquals(LocaleProviderImpl.class, XFun.getLocaleProvider().getClass());
    assertEquals(MessageRepositoryImpl.class, XFun.getMessageRepository().getClass());
    assertEquals(PrincipalProviderImpl.class, XFun.getPrincipalProvider().getClass());
    assertEquals(FallbackTraceImpl.class, XFun.getTrace().getClass());
    assertEquals(FallbackTraceProviderImpl.class, XFun.getTraceProvider().getClass());
    assertEquals(FallbackVerifierImpl.class, XFun.getVerifier().getClass());

    // Check exeption handling
    System.setProperty(XFun.X_FUN_CONFIG_RESOURCE_NAME, "NotExistingConfig");
    try {
      XFun.reload();
    }
    catch (XFunRuntimeException e) {
      String lMessage =
          "Configuration error. Default configuration class 'com.anaptecs.jeaf.xfun.impl.DefaultXFunConfiguration' is not in classpath AND no custom configuration could be loaded from resource file 'META-INF/TEST/X-Fun/config/NotExistingConfig'. Please check your custom configuration file and your classpath.";
      assertEquals(lMessage, e.getMessage());
    }
  }
}
