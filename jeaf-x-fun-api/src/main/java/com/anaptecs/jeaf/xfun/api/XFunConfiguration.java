/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.anaptecs.jeaf.xfun.annotations.MessageResource;
import com.anaptecs.jeaf.xfun.annotations.XFunConfig;
import com.anaptecs.jeaf.xfun.api.checks.VerifierFactory;
import com.anaptecs.jeaf.xfun.api.config.AnnotationBasedConfiguration;
import com.anaptecs.jeaf.xfun.api.config.ConfigurationProviderFactory;
import com.anaptecs.jeaf.xfun.api.config.ConfigurationReader;
import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverterRegistryFactory;
import com.anaptecs.jeaf.xfun.api.info.InfoProviderFactory;
import com.anaptecs.jeaf.xfun.api.locale.LocaleProviderFactory;
import com.anaptecs.jeaf.xfun.api.messages.MessageRepositoryFactory;
import com.anaptecs.jeaf.xfun.api.principal.PrincipalProviderFactory;
import com.anaptecs.jeaf.xfun.api.trace.TraceProviderFactory;
import com.anaptecs.jeaf.xfun.fallback.info.InfoProviderFactoryImpl;

public final class XFunConfiguration extends AnnotationBasedConfiguration<XFunConfig> {
  /**
   * List contains all messages resources that are configured as JEAF meta information. List of classes will be lazy
   * loaded in order to avoid problems during initialization.
   */
  private List<Class<?>> messageResourceClasses;

  /**
   * Initialize object.
   */
  public XFunConfiguration( ) {
    this(XFunConfig.X_FUN_CONFIG_RESOURCE_NAME, XFun.X_FUN_BASE_PATH, false);
  }

  /**
   * Initialize object. During initialization configurations will be loaded.
   * 
   * @param pCustomConfigurationResourceName Name of the file which contains the class name of the custom configuration
   * class. The parameter must not be null.
   * @param pBasePackagePath Path under which the file should be found in the classpath. The parameter may be null.
   * @param pExceptionOnError If parameter is set to true then an exception will be thrown in case of configuration
   * errors.
   */
  public XFunConfiguration( String pCustomConfigurationResourceName, String pCustomConfigurationBasePackagePath,
      boolean pExceptionOnError ) {

    // Call super class constructor, that's it.
    super(pCustomConfigurationResourceName, pCustomConfigurationBasePackagePath, pExceptionOnError);
  }

  @Override
  protected Class<XFunConfig> getAnnotationClass( ) {
    return XFunConfig.class;
  }

  @Override
  protected String getDefaultConfigurationClass( ) {
    return XFun.DEFAULT_CONFIGURATION_CLASS;
  }

  @Override
  public XFunConfig getEmptyConfiguration( ) {
    return new XFunConfig() {

      @Override
      public Class<? extends Annotation> annotationType( ) {
        return XFunConfig.class;
      }

      @Override
      public Class<? extends MessageRepositoryFactory> messageRepositoryFactory( ) {
        return null;
      }

      @Override
      public Class<? extends VerifierFactory> verifierFactory( ) {
        return null;
      }

      @Override
      public Class<? extends ConfigurationProviderFactory> configurationProviderFactory( ) {
        return null;
      }

      @Override
      public Class<? extends LocaleProviderFactory> localeProviderFactory( ) {
        return null;
      }

      @Override
      public Class<? extends PrincipalProviderFactory> principalProviderFactory( ) {
        return null;
      }

      @Override
      public Class<? extends InfoProviderFactory> infoProviderFactory( ) {
        return null;
      }

      @Override
      public Class<? extends DatatypeConverterRegistryFactory> datatypeConverterRegistryFactory( ) {
        return null;
      }

      @Override
      public Class<? extends TraceProviderFactory> traceProviderFactory( ) {
        return null;
      }

      @Override
      public String messageResourcePath( ) {
        return MessageResource.MESSAGE_RESOURCES_PATH;
      }
    };
  }

  /**
   * Method checks the passed custom configuration. During check we check all params of the configuration as we assume
   * that there is not default configuration.
   * 
   * @param pCustomConfiguration
   * @return String Describing the found problems in the passed custom configuration or null if everything is fine.
   */
  @Override
  public List<String> checkCustomConfiguration( XFunConfig pCustomConfiguration ) {
    // As the JEAF Tools configuration just defines classes of interface implementations we just have to ensure that
    // the defined classes are real classes and not just interfaces.
    List<String> lConfigErrors = new ArrayList<>(0);

    // Check message repository
    this.tryNewInstance(pCustomConfiguration.messageRepositoryFactory(), MessageRepositoryFactory.class, lConfigErrors);

    // Check verifier
    this.tryNewInstance(pCustomConfiguration.verifierFactory(), VerifierFactory.class, lConfigErrors);

    // Check locale provider
    this.tryNewInstance(pCustomConfiguration.localeProviderFactory(), LocaleProviderFactory.class, lConfigErrors);

    // Check principal provider
    this.tryNewInstance(pCustomConfiguration.principalProviderFactory(), PrincipalProviderFactory.class, lConfigErrors);

    // Check principal provider
    this.tryNewInstance(pCustomConfiguration.infoProviderFactory(), InfoProviderFactory.class, lConfigErrors);

    // Check trace
    this.tryNewInstance(pCustomConfiguration.traceProviderFactory(), TraceProviderFactory.class, lConfigErrors);

    // Check if configured message resource path is really available.
    String lMessageResourcePath = pCustomConfiguration.messageResourcePath();
    ConfigurationReader lConfigurationReader = new ConfigurationReader();
    if (lConfigurationReader.isConfigurationAvailable(lMessageResourcePath) == false) {
      lConfigErrors.add("Message resource configuration file " + lMessageResourcePath
          + " is not available through the application's classpath.");
    }

    // Return result of configuration check.
    return lConfigErrors;
  }

  /**
   * Method returns the configured MessageRepositoryFactory.
   * 
   * @return {@link MessageRepositoryFactory} Factory that should be used. The method may return null in case of
   * configuration errors.
   */
  public MessageRepositoryFactory getMessageRepositoryFactory( ) {
    // Resolve impl in a 2-way approach. First we look at the custom configuration afterwards we do the fallback to
    // our default configuration.
    return this.newInstance(customConfig.messageRepositoryFactory(), defaultConfig.messageRepositoryFactory(),
        exceptionOnError);
  }

  /**
   * Method returns the configured VerifierFactory.
   * 
   * @return {@link VerifierFactory} Factory that should be used. The method may return null in case of configuration
   * errors.
   */
  public VerifierFactory getVerifierFactory( ) {
    // Resolve impl in a 2-way approach. First we look at the custom configuration afterwards we do the fallback to
    // our default configuration.
    return this.newInstance(customConfig.verifierFactory(), defaultConfig.verifierFactory(), exceptionOnError);
  }

  public ConfigurationProviderFactory getConfigurationProviderFactory( ) {
    // Resolve impl in a 2-way approach. First we look at the custom configuration afterwards we do the fallback to
    // our default configuration.
    return this.newInstance(customConfig.configurationProviderFactory(), defaultConfig.configurationProviderFactory(),
        exceptionOnError);
  }

  /**
   * Method returns the configured LocaleProviderFactory.
   * 
   * @return {@link LocaleProviderFactory} Factory that should be used. The method may return null in case of
   * configuration errors.
   */
  public LocaleProviderFactory getLocaleProviderFactory( ) {
    // Resolve impl in a 2-way approach. First we look at the custom configuration afterwards we do the fallback to
    // our default configuration.
    return this.newInstance(customConfig.localeProviderFactory(), defaultConfig.localeProviderFactory(),
        exceptionOnError);
  }

  /**
   * Method returns the configured PrincipalProviderFactory.
   * 
   * @return {@link PrincipalProviderFactory} Factory that should be used. The method may return null in case of
   * configuration errors.
   */
  public PrincipalProviderFactory getPrincipalProviderFactory( ) {
    // Resolve impl in a 2-way approach. First we look at the custom configuration afterwards we do the fallback to
    // our default configuration.
    return this.newInstance(customConfig.principalProviderFactory(), defaultConfig.principalProviderFactory(),
        exceptionOnError);
  }

  /**
   * Method returns the configured InfoProviderFactory.
   * 
   * @return {@link InfoProviderFactory} Factory that should be used. The method may return null in case of
   * configuration errors.
   */
  public InfoProviderFactory getInfoProviderFactory( ) {
    // Resolve impl in a 2-way approach. First we look at the custom configuration afterwards we do the fallback to
    // our fallback configuration.
    InfoProviderFactory lInfoProviderFactory;
    try {
      lInfoProviderFactory =
          this.newInstance(customConfig.infoProviderFactory(), defaultConfig.infoProviderFactory(), exceptionOnError);

      if (lInfoProviderFactory == null) {
        lInfoProviderFactory = new InfoProviderFactoryImpl();
      }
    }
    // In case of troubles we will do fallback to our fallback implementation.
    catch (Throwable e) {
      lInfoProviderFactory = new InfoProviderFactoryImpl();
    }
    return lInfoProviderFactory;
  }

  /**
   * Method returns the configured DatatypeConverterRegistryFactory.
   * 
   * @return {@link DatatypeConverterRegistryFactory} Factory that should be used. The method may return null in case of
   * configuration errors.
   */
  public DatatypeConverterRegistryFactory getDatatypeConverterRegistryFactory( ) {
    return this.newInstance(customConfig.datatypeConverterRegistryFactory(),
        defaultConfig.datatypeConverterRegistryFactory(), exceptionOnError);
  }

  /**
   * Method returns the configured TraceProviderFactory.
   * 
   * @return {@link TraceProviderFactory} Factory that should be used. The method may return null in case of
   * configuration errors.
   */
  public TraceProviderFactory getTraceProviderFactory( ) {
    return this.newInstance(customConfig.traceProviderFactory(), defaultConfig.traceProviderFactory(),
        exceptionOnError);
  }

  /**
   * @see XFunConfig#messageResourcePath()
   */
  public String messageResourcePath( ) {
    return theConfig.messageResourcePath();
  }

  /**
   * Method returns all configured message resource classes.
   * 
   * @return {@link List} List with all configured message resource classes. The method never returns null. The returned
   * {@link List} is immutable.
   */
  public List<Class<?>> getMessageResourceClasses( ) {
    if (messageResourceClasses == null) {
      // Read all configured classes
      ConfigurationReader lReader = new ConfigurationReader();
      List<Class<?>> lMessageResourceClasses = lReader.readClassesFromConfigFile(this.messageResourcePath());
      List<Class<?>> lRealMessageResourceClasses = new ArrayList<>(lMessageResourceClasses.size());
      // Check if classes have annotation @MessageResource
      for (Class<?> lNextClass : lMessageResourceClasses) {
        MessageResource lMessageResourceAnnotation = lNextClass.getAnnotation(MessageResource.class);
        if (lMessageResourceAnnotation != null) {
          lRealMessageResourceClasses.add(lNextClass);
        }
        else {
          XFun.getTrace().warn("Configured message resource " + lNextClass.getName()
              + " does not have annotation @MessageResource and is therefore ignored.");
        }
      }

      // Make sure that list can not be modified.
      messageResourceClasses = Collections.unmodifiableList(lRealMessageResourceClasses);
    }
    return messageResourceClasses;
  }
}
