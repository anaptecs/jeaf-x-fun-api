/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.config;

import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFSystemException;

/**
 * Interface defines the way how to get configuration entries that are stored in resource bundles / property files and
 * are accessed via the applications classpath.
 */
public interface ResourceBundleConfigurationResourceFactory {
  /**
   * Method returns an object to access configuration values that are located inside a resource bundle / property file.
   * 
   * @param pResourceBundelName Name of the resource bundle from which the configuration values should be read. The
   * parameter must not be null.
   * @return {@link Configuration} Object to access configuration values from resource bundle with the passed name. The
   * method never returns null.
   * @throws JEAFSystemException In case that the resource bundle could not be found an {@link JEAFSystemException} will
   * be thrown.
   */
  ConfigurationResource getResourceBundleConfigurationResource( String pResourceBundelName ) throws JEAFSystemException;
}
