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
import com.anaptecs.jeaf.xfun.api.config.ComponentConfigurationResourceFactory;
import com.anaptecs.jeaf.xfun.api.config.ConfigurationProvider;
import com.anaptecs.jeaf.xfun.api.config.EnvironmentConfigurationResourceFactory;
import com.anaptecs.jeaf.xfun.api.config.FileConfigurationResourceFactory;
import com.anaptecs.jeaf.xfun.api.config.ResourceBundleConfigurationResourceFactory;
import com.anaptecs.jeaf.xfun.api.config.ResourceConfigurationResourceFactory;
import com.anaptecs.jeaf.xfun.api.config.SystemPropertiesConfigurationResourceFactory;

/**
 * Annotation can be used to configure JEAF X-Fun's configuration mechanism.
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
public @interface ConfigurationProviderConfig {
  /**
   * Name of the resource that contains the name of the class with the @ConfigurationProviderConfig annotation.
   */
  String CONFIG_PROVIDER_CONFIG_RESOURCE_NAME = "ConfigurationProviderConfig";

  /**
   * Path under which the configuration file is stored.
   */
  String CONFIGURATION_PROVIDER_CONFIG_PATH = XFun.X_FUN_BASE_PATH + '/' + CONFIG_PROVIDER_CONFIG_RESOURCE_NAME;

  /**
   * Property defines the configuration provider implementation that is used to lookup component specific configuration
   * parameters. This mechanism will only be used if an application uses standard JEAF components or has own ones.
   * 
   * {@link ConfigurationProvider#getComponentResourceAccessProvider(com.anaptecs.jeaf.xfun.api.common.ComponentID)}
   */
  Class<? extends ComponentConfigurationResourceFactory> componentConfigurationResourceFactory() default ComponentConfigurationResourceFactory.class;

  /**
   * Property defines the configuration provider implementation that should be used to resolve environment
   * configurations from the operating system e.g. environment variables.
   * 
   * {@link ConfigurationProvider#getEnvironmentResourceAccessProvider()}
   */
  Class<? extends EnvironmentConfigurationResourceFactory> environmentConfigurationResourceFactory() default EnvironmentConfigurationResourceFactory.class;

  /**
   * Property defines the configuration provider implementation that should be used to lookup file based configuration
   * parameters.
   * 
   * {@link ConfigurationProvider#getFileResourceAccessProvider(String)}
   */
  Class<? extends FileConfigurationResourceFactory> fileConfigurationResourceFactory() default FileConfigurationResourceFactory.class;

  /**
   * Property defines the configuration provider implementation that should be used to lookup configuration parameters
   * from resources.
   * 
   * {@link ConfigurationProvider#getResourceAccessProvider(String)}
   */
  Class<? extends ResourceConfigurationResourceFactory> resourceConfigurationResourceFactory() default ResourceConfigurationResourceFactory.class;

  /**
   * Property defines the configuration provider implementation that should be used to lookup configuration parameters
   * from resource bundles.
   * 
   * {@link ConfigurationProvider#getResourceBundleAccessProvider(String)}
   */
  Class<? extends ResourceBundleConfigurationResourceFactory> resourceBundleConfigurationResourceFactory() default ResourceBundleConfigurationResourceFactory.class;

  /**
   * Property defines the configuration provider that should be used to lookup configuration parameters from system
   * properties.
   * 
   * {@link ConfigurationProvider#getSystemPropertiesResourceAccessProvider()}
   */
  Class<? extends SystemPropertiesConfigurationResourceFactory> systemPropertiesConfigurationResourceFactory() default SystemPropertiesConfigurationResourceFactory.class;
}
