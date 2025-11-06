/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.health;

/**
 * This enumeration describes the different levels of health checks that can be performed. Intention of the different
 * levels is that most times the execution of detailed checks that usually are quite expensive in time and resources are
 * not required. However from time to time some more detailed checks should be performed.
 * 
 * JEAF defines the following check levels:
 * <ol>
 * <li>Internal Checks - Checks of the internal state of a service or service provider. At this level only internal
 * checks are run.</li>
 * <li>Infrastructure Tests - Checks of the infrastructure that is required by the service or service provider.</li>
 * <li>External Checks - Checks of external systems that are required by the service or service provider.</li> </ol
 * 
 * @author JEAF Development Team
 */
public enum CheckLevel {
  /**
   * Running checks on this check level means that only the internal state of a component should be checked. Checks
   * concerning infrastructure or external systems should not be executed on this level.
   */
  INTERNAL_CHECKS_ONLY,

  /**
   * Running checks on this level means that the internal state of a component and its required specific infrastructure
   * should be checked. Checks concerning external systems should not be performed on this level.
   */
  INFRASTRUCTURE_CHECKS,

  /**
   * Running checks on this level means that the internal state of a component, its required infrastructure and may be
   * required external systems should be checked.
   */
  EXTERNAL_CHECKS;

  /**
   * Method returns the default check level.
   * 
   * @return {@link CheckLevel} Default check level. Method never returns null.
   */
  public static CheckLevel getDefaultLevel( ) {
    return INTERNAL_CHECKS_ONLY;
  }

}
