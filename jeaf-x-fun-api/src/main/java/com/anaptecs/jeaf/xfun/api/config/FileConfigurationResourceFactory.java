/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.config;

import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFSystemException;

/**
 * Interface defines the way how to get configuration values that are stored in files.
 */
public interface FileConfigurationResourceFactory {
  /**
   * Method returns an object to access configuration values that are located inside a plain file.
   * 
   * @param pFileName Name of the file from which the configuration values should be read. The parameter must not be
   * null.
   * @return {@link Configuration} Configuration object to access the configuration values of the file with the passed
   * name. The method never returns null.
   * @throws JEAFSystemException In case that the file could not be opened an {@link JEAFSystemException} will be
   * thrown.
   */
  ConfigurationResource getFileConfigurationResource( String pFileName ) throws JEAFSystemException;
}
