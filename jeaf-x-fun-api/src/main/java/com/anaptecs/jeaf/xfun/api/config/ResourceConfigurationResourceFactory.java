/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.config;

import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFSystemException;

/**
 * Interface defines the way how to get configuration values that are stored in resources and are accessed via the
 * applications classpath.
 */
public interface ResourceConfigurationResourceFactory {
  /**
   * Method returns an object to acccess configuration values that are located inside any resource file that is
   * accessible via the applications classpath. In opposite to
   * {@link ResourceBundleConfigurationResourceFactory#getResourceBundleConfiguration(String)} here we also need the
   * extension of the resource file if it has one.
   * 
   * @param pResourceName Name of the resource from which the configuration values should be read. The parameter must
   * not be null.
   * @return {@link Configuration} Object to access configuration values of resource with the passed name. The method
   * never returns null.
   * @throws JEAFSystemException In case that the resource could not be found an {@link JEAFSystemException} will be
   * thrown.
   */
  ConfigurationResource getResourceConfigurationResource( String pResourceName );
}
