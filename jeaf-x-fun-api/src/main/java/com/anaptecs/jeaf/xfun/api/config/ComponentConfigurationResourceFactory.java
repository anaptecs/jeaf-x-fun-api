/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.config;

import com.anaptecs.jeaf.xfun.api.common.ComponentID;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFSystemException;

/**
 * Interface defines the way how to get the configuration of components.
 */
public interface ComponentConfigurationResourceFactory {
  /**
   * Method returns the configuration for the component with the passed ID.
   * 
   * @param pComponentID ID of the component whose configuration should be returned. The parameter must not be null.
   * @return {@link Configuration} Configuration of the component. The method never returns null.
   * @throws JEAFSystemException In case that the configuration of the component could not be loaded.
   */
  ConfigurationResource getComponentConfigurationResource( ComponentID pComponentID ) throws JEAFSystemException;
}
