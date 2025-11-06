/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.config;

import java.util.List;
import java.util.MissingResourceException;

/**
 * Interface defines all the methods that are offered to retrieve configuration values from a resource.
 */
public interface Configuration {
  /**
   * Constant for delimiter for list properties.
   */
  String LIST_SEPERATOR = ";";

  /**
   * Method returns the value of the configuration entry with the passed name.
   * 
   * @param pConfigurationKey Name of the configuration entry whose value should be returned. The parameter must not be
   * null.
   * @param pType Type as which the configuration value should be returned. The parameter must not be null.
   * @return T Value of the configuration entry. The method returns null if the configuration entry is not defined or
   * not set.
   */
  <T> T getConfigurationValue( String pConfigurationKey, Class<T> pType );

  /**
   * Method returns the value of the configuration entry with the passed name.
   * 
   * @param pConfigurationKey Name of the configuration entry whose value should be returned. The parameter must not be
   * null.
   * @param pRequired Indicates whether the configuration entry is required or not. If pRequired is <code>true</code>
   * then the configuration entry must be exist.
   * @param pType Type as which the configuration value should be returned. The parameter must not be null.
   * @return T Value of the configuration entry with the passed name. The method never returns null. If the
   * configuration entry is not defined or not set but required then a {@link MissingResourceException} will be thrown.
   * @throws MissingResourceException if configuration does not exist or is not set but is required.
   */
  <T> T getConfigurationValue( String pConfigurationKey, boolean pRequired, Class<T> pType )
    throws MissingResourceException;

  /**
   * Method returns the value of the configuration entry with the passed name.
   * 
   * @param pConfigurationKey Name of the configuration entry whose value should be returned. The parameter must not be
   * null.
   * @param pDefaultValue Default value that will be returned if the configuration entry is not defined, not set or can
   * not be converted. The parameter may be null.
   * @return T Value of the configuration entry with the passed name. The method returns the passed default value if the
   * configuration entry is not defined or not set.
   */
  <T> T getConfigurationValue( String pConfigurationKey, T pDefaultValue, Class<T> pType );

  /**
   * Method returns the value of the configuration entry with the passed name as list of values. The list entries need
   * to be separated by {@link #LIST_SEPERATOR}
   * 
   * @param pConfigurationKey Name of the configuration entry whose value should be returned. The parameter must not be
   * null.
   * @param pType Type as which the configuration value should be returned. The parameter must not be null.
   * @return {@link List} List with all configuration values. The method never returns null. If the entry is not defined
   * or not set then an empty list will be returned.
   */
  <T> List<T> getConfigurationValueList( String pConfigurationKey, Class<T> pType );

  /**
   * Method returns the value of the configuration entry with the passed name as list of values. The list entries need
   * to be separated by {@link #LIST_SEPERATOR}
   * 
   * @param pConfigurationKey Name of the configuration entry whose value should be returned. The parameter must not be
   * null.
   * @param pType Type as which the configuration value should be returned. The parameter must not be null.
   * @return {@link List} List with all configuration values. If the configuration entry is not defined or not set but
   * required then a {@link MissingResourceException} will be thrown.
   * @throws MissingResourceException if configuration does not exist or is not set but is required.
   */
  <T> List<T> getConfigurationValueList( String pConfigurationKey, boolean pRequired, Class<T> pType )
    throws MissingResourceException;

  /**
   * Method returns the value of the configuration entry with the passed name as list of values. The list entries need
   * to be separated by {@link #LIST_SEPERATOR}
   * 
   * @param pConfigurationKey Name of the configuration entry whose value should be returned. The parameter must not be
   * null.
   * @param pDefaultValue Default value that should be used in case that the configuration entry is not defined or not
   * set. The parameter may be null.
   * @param pType Type as which the configuration value should be returned. The parameter must not be null.
   * @return {@link List} List with all configuration values. If the configuration entry is not defined or not set then
   * the passed default value will be returned wrapped in a list. The method never returns null. If pDefaultValue is
   * null then an empty list will be returned.
   */
  <T> List<T> getConfigurationValueList( String pConfigurationKey, T pDefaultValue, Class<T> pType );

  /**
   * Method returns the value of the configuration entry with the passed name as list of values. The list entries need
   * to be separated by {@link #LIST_SEPERATOR}
   * 
   * @param pConfigurationKey Name of the configuration entry whose value should be returned. The parameter must not be
   * null.
   * @param pDefaultValue Default value that should be used in case that the configuration entry is not defined or not
   * set. The parameter may be null.
   * @param pType Type as which the configuration value should be returned. The parameter must not be null.
   * @return {@link List} List with all configuration values. If the configuration entry is not defined or not set then
   * the passed default value will be returned.
   */
  <T> List<T> getConfigurationValueList( String pConfigurationKey, List<T> pDefaultValue, Class<T> pType );

}