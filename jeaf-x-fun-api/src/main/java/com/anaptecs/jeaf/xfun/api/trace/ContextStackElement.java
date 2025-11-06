/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.trace;

import com.anaptecs.jeaf.xfun.api.common.ComponentID;
import com.anaptecs.jeaf.xfun.bootstrap.Assert;

/**
 * In order to know the current context JEAF's trace mechanism supports a so called context stack. Based on this context
 * stack we are able to know the current context and can provide the current trace object.
 * 
 * As there may be multiple contexts e.g. when service call other services. The context is organized as stack.
 */
public class ContextStackElement {
  /**
   * Name of the context.
   */
  private final String contextName;

  /**
   * ID of the component to which this context belongs to.
   */
  private final ComponentID componentID;

  /**
   * Initialize object.
   * 
   * @param pContextName Name of the context. The parameter must not be null.
   * @param pComponentID ID of the component to which the context belongs to. The parameter must not be null.
   */
  public ContextStackElement( String pContextName, ComponentID pComponentID ) {
    // Check parameter
    Assert.assertNotNull(pContextName, "pContextName");
    Assert.assertNotNull(pComponentID, "pComponentID");

    contextName = pContextName;
    componentID = pComponentID;
  }

  /**
   * Method returns the name of the context.
   * 
   * @return {@link String} Name of the context. The method never returns null.
   */
  public String getContextName( ) {
    return contextName;
  }

  /**
   * Method returns the ID of the component to which this context belongs to.
   * 
   * @return {@link ComponentID} ID of the component of the context. The method never returns null.
   */
  public ComponentID getComponentID( ) {
    return componentID;
  }

}
