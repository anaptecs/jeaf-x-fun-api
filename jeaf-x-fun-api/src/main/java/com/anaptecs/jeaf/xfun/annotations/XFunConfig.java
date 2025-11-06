/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.checks.Assert;
import com.anaptecs.jeaf.xfun.api.checks.Check;
import com.anaptecs.jeaf.xfun.api.checks.Verifier;
import com.anaptecs.jeaf.xfun.api.checks.VerifierFactory;
import com.anaptecs.jeaf.xfun.api.config.ConfigurationProvider;
import com.anaptecs.jeaf.xfun.api.config.ConfigurationProviderFactory;
import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverter;
import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverterRegistry;
import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverterRegistryFactory;
import com.anaptecs.jeaf.xfun.api.info.InfoProvider;
import com.anaptecs.jeaf.xfun.api.info.InfoProviderFactory;
import com.anaptecs.jeaf.xfun.api.locale.LocaleProvider;
import com.anaptecs.jeaf.xfun.api.locale.LocaleProviderFactory;
import com.anaptecs.jeaf.xfun.api.messages.MessageRepository;
import com.anaptecs.jeaf.xfun.api.messages.MessageRepositoryFactory;
import com.anaptecs.jeaf.xfun.api.principal.PrincipalProvider;
import com.anaptecs.jeaf.xfun.api.principal.PrincipalProviderFactory;
import com.anaptecs.jeaf.xfun.api.trace.TraceProviderFactory;

/**
 * Annotation is used to configure the implementation of X-Fun services that should be used.
 * 
 * In order to make configuration even simpler JEAF's Maven Plugin can be used. The plugin analysis the projects
 * classpath for classes with annotations and generates the required configuration files. There should be only one
 * interface or class with this annotation in your classpath. If multiple usages are detected then the first one will be
 * used according to the order of the classpath.
 * 
 * If you do not use this annotation at all then the default implementations will be used. These default implementations
 * should be sufficient for most cases.
 */
@Retention(RUNTIME)
@Target(TYPE)
@Documented
public @interface XFunConfig {
  /**
   * Name of the resource that contains the name of the class with the @XFunConfig annotation.
   */
  String X_FUN_CONFIG_RESOURCE_NAME = "XFunConfig";

  /**
   * Path under which the configuration file is stored.
   */
  String XFUN_CONFIG_PATH = XFun.X_FUN_BASE_PATH + '/' + X_FUN_CONFIG_RESOURCE_NAME;

  /**
   * Property defines the factory that should be used to create a message repository. By switching factories it can be
   * configured which implementation of a message repository should be used.
   * 
   * {@link MessageRepository}, {@link XFun#getMessageRepository()}
   */
  Class<? extends MessageRepositoryFactory> messageRepositoryFactory() default MessageRepositoryFactory.class;

  /**
   * Property defines the factory that should be used to created a so called verifier. Verifiers provide check
   * implementations. There are only very rare cases when you will need to use your custom verifier implementation,
   * however it is possible.
   * 
   * {@link Verifier}, {@link Check}, {@link Assert}, {@link XFun#getVerifier()}
   */
  Class<? extends VerifierFactory> verifierFactory() default VerifierFactory.class;

  /**
   * Property defines the factory that should be used to create a configuration provider. Configuration providers can be
   * used to access configuration values from different resources like files, resource bundles etc.
   * 
   * {@link ConfigurationProviderFactory}, {@link ConfigurationProvider}, {@link ConfigurationProviderConfig}
   */
  Class<? extends ConfigurationProviderFactory> configurationProviderFactory() default ConfigurationProviderFactory.class;

  /**
   * Property defines the factory that should be used to create the locale provider of the application. Locale providers
   * are required to find out the locale (e.g. language) of the current context. Depending on the application this can
   * simply be the language of the operating systems or something like the language of the current user in an web
   * application.
   * 
   * {@link LocaleProvider}, {@link XFun#getLocaleProvider()}
   */
  Class<? extends LocaleProviderFactory> localeProviderFactory() default LocaleProviderFactory.class;

  /**
   * Property defines the factory that should be used to create the principal provider of the application. The principal
   * provider is responsible to provide information about the current user. Depending on the application this can simply
   * be the current user of the operating systems or something like the current user in an web application.
   * 
   * {@link PrincipalProvider}, {@link XFun#getPrincipalProvider()}
   */
  Class<? extends PrincipalProviderFactory> principalProviderFactory() default PrincipalProviderFactory.class;

  /**
   * Class defines the factory that should be used to create the info provider of the application. the info provider is
   * responsible to provide information about the application itself as well as the runtime environment and the
   * operating system. There are only very rare cases when you will need to use your custom verifier implementation,
   * however it is possible.
   * 
   * {@link InfoProvider}, {@link XFun#getInfoProvider()}
   */
  Class<? extends InfoProviderFactory> infoProviderFactory() default InfoProviderFactory.class;

  /**
   * Class defines the factory that should be used to create the datatype converter registry. The registry is
   * responsible to provide converters for several types of conversions. There are only very rare cases when you will
   * need to use your custom implementation, however it is possible.
   * 
   * However there may be the need to provider your own converters. Therefore you have to implement interface
   * {@link DatatypeConverter}. For further details please refer to the documentation of the interface.
   * 
   * {@link DatatypeConverterRegistry}, {@link DatatypeConverter}, {@link XFun#getInfoProvider()}
   */
  Class<? extends DatatypeConverterRegistryFactory> datatypeConverterRegistryFactory() default DatatypeConverterRegistryFactory.class;

  /**
   * Trace factory class that should be used. If it is not set then the default factory will be used.s
   */
  Class<? extends TraceProviderFactory> traceProviderFactory() default TraceProviderFactory.class;

  /**
   * Property defines the path of the file where all the message resources are be listed.
   */
  String messageResourcePath() default MessageResource.MESSAGE_RESOURCES_PATH;
}
