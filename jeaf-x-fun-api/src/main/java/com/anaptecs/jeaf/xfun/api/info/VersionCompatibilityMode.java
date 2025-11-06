/**
 * Copyright 2004 - 2019
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.info;

/**
 * Enumeration describes the possible variants how to check if two versions are compatible.
 */
public enum VersionCompatibilityMode {
  /**
   * In STRICT mode the version on all levels have to be identical.
   */
  STRICT,

  /**
   * In case of semantic versioning tow versions are compatible if the have the same major version and if the minor
   * version is the same or higher.
   */
  SEMANTIC_VERSIONING
}