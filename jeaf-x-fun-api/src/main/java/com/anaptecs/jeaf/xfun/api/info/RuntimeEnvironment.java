/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */

package com.anaptecs.jeaf.xfun.api.info;

/**
 * Enumeration describes the possible types of runtime environments within which JEAF is be used.
 */
public enum RuntimeEnvironment {
  /**
   * Enumeration literal stands for Java Standard Edition runtime environments.
   */
  JSE,

  /**
   * Enumeration literal stands for Web Containers of Java Enterprise Edition. Web containers and EJB containers are
   * treated as different runtime environments as they have totally different limitations and rules.
   */
  WEB_CONTAINER,

  /**
   * Enumeration literal stands for EJB Containers of Java Enterprise Edition. Web containers and EJB containers are
   * treated as different runtime environments as they have totally different limitations and rules.
   */
  EJB_CONTAINER,

  /**
   * Enumeration literal for case that runtime environment can not be resolved.
   */
  UNKNOWN;
}
