/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.health;

/**
 * Enumeration describes all possible health states.
 * 
 * @author JEAF Development Team
 */
public enum HealthStatus {
  /**
   * This state means that everything is fine.
   */
  OK,

  /**
   * This state means that the checked component / service etc. is currently disabled. this does not mean that there is
   * a problem . It just means that currently it is not possible to resolve a status.
   */
  DISABLED,

  /**
   * This state means that a service is still up and running but that there exist smaller problems like may be timeouts
   * that occur once in a while.
   */
  WARNING,

  /**
   * This state means that the service is no longer able to provide its services.
   */
  ERROR,

  /**
   * This state means that the health state is unknown.
   */
  UNKNOWN;

  /**
   * Method checks if the health status means that a component / service is able to serve requests. This is exactly the
   * case if state is either {@value HealthStatus#OK} or {@value HealthStatus#WARNING}
   * 
   * @return boolean Method returns true if this health status means that a component / service is able to serer
   * requests.
   */
  public boolean isWorking( ) {
    boolean lIsWorking;
    if (this == OK || this == WARNING) {
      lIsWorking = true;
    }
    else {
      lIsWorking = false;
    }
    return lIsWorking;
  }

  /**
   * Method checks is a component / service is not able to server requests.
   * 
   * @return boolean method returns true if the component service is out-of-service.
   */
  public boolean isOutOfService( ) {
    return !this.isWorking();
  }
}
