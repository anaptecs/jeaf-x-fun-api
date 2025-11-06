/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.config;

import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.common.ComponentID;

/**
 * Class collects all the different types of resource access providers that are required for the different source of
 * configuration parameters.
 */
public interface ConfigurationProvider {
  /**
   * Method returns the configured configuration provider.
   * 
   * @return {@link ConfigurationProvider} Configuration provider that is used. The method never returns null.
   */
  static ConfigurationProvider getConfigurationProvider( ) {
    return XFun.getConfigurationProvider();
  }

  /**
   * Method returns the configuration for the component with the passed ID.
   * 
   * @param pComponentID ID of the component. The parameter must not be null.
   * @return {@link Configuration} Configuration of the component with the passed ID. The method never returns null.
   */
  Configuration getComponentConfiguration( ComponentID pComponentID );

  /**
   * Method returns a configuration representing the operating system environment variables.
   * 
   * @return {@link Configuration} Configuration object providing the values of OS environment variables. The method
   * never returns null.
   */
  Configuration getEnvironmentConfiguration( );

  /**
   * Method returns a configuration object that returns configuration values that are read from a configuration file
   * using Java properties file convention.
   * 
   * @param pFileName Name of the file from which the configuration values should be read. The parameter must not be
   * null and must point to an existing file.
   * @return {@link Configuration} Object representing the configuration values of the passed file. The method never
   * returns null.
   */
  Configuration getFileConfiguration( String pFileName );

  /**
   * Method returns a configuration object that returns configuration values from a resource with the passed name. In
   * opposite to {@link #getFileConfiguration(String)} this method reads the configuration values via the applications
   * classpath.
   * 
   * 
   * @param pResourceName Name of the resource that should be read.
   * @return {@link Configuration} Object representing the configuration values of the resource with the passed name.The
   * method never returns null.
   */
  Configuration getResourceConfiguration( String pResourceName );

  /**
   * Method returns a configuration object that returns configuration values from a resource bundle with the passed
   * name. In opposite to {@link #getFileConfiguration(String)} this method reads the configuration values via the
   * applications classpath.
   * 
   * 
   * @param pResourceBundleName Name of the resource bundle that should be read.
   * @return {@link Configuration} Object representing the configuration values of the resource with the passed name.The
   * method never returns null.
   */
  Configuration getResourceBundleConfiguration( String pResourceBundleName );

  /**
   * Method returns a configuration object that provides access to all system properties of the application.
   * 
   * @return {@link Configuration} Object to access system properties.The method never returns null.
   */
  Configuration getSystemPropertiesConfiguration( );

  /**
   * Method replaces may be existing place holders for system properties inside the passed string. Place holders are
   * defined by a leading '${' and an ending '}'.
   * 
   * @param pValue String inside which system properties should be replaced. The parameter may be null.
   * @return String with replaced system properties. If no system properties are defined inside the string then a same
   * string will be returned. The method may return null.
   */
  String replaceSystemProperties( String pValue );

}
