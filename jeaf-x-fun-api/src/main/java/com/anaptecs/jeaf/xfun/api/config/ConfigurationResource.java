/**
 * Copyright 2004 - 2020 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.config;

import java.util.List;
import java.util.MissingResourceException;

/**
 * Interface defines a generic API to read configuration values from a resource.
 * 
 * @author JEAF Development Team
 */
public interface ConfigurationResource {
  /**
   * Method returns the name of the resource from which configuration values are read.
   * 
   * @return String Name of the resource. The method must not return null.
   */
  String getResourceName( );

  /**
   * Method returns the location of the resource from which configuration values are read.
   * 
   * @return String Location of the resource. The method never returns null.
   */
  String getResourceLocation( );

  /**
   * Method checks if the configuration resource has a configuration value with the passed name.
   * 
   * @param pConfigurationKey Key which should be checked. The parameter must not be null.
   * @return boolean Method returns true if the passed key is defined and otherwise false.
   */
  boolean hasConfigurationValue( String pConfigurationKey );

  /**
   * Method returns the names of all configuration keys that are available through the configuration resource.
   * 
   * @return List List all all available keys. The method must not return null.
   */
  List<String> getAllConfigurationKeys( );

  /**
   * Method returns the value of the configuration key with the passed name. The way how the configuration key's value
   * is resolved depends on the concrete implementation of this interface.
   * 
   * @param pConfigurationKey Name of the configuration key whose value should be returned. The parameter must not be
   * null.
   * @return String Value of the configuration with the passed name. The method has to return null if the configuration
   * key is not set. The returned string will be trimmed by the caller.
   * @throws MissingResourceException The method has to throw a MissingResourceException if the configuration key with
   * the passed name does not exist within the configuration resource.
   */
  String getConfigurationValue( String pConfigurationKey ) throws MissingResourceException;
}
