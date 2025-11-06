/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.checks;

import com.anaptecs.jeaf.xfun.annotations.XFunConfig;

/**
 * Interface defines the factory to create a new verifier implementation. Which verifier factory should be used can be
 * configured using annotation {@link XFunConfig}
 */
public interface VerifierFactory {
  /**
   * Method creates a new verifier instance.
   * 
   * @return {@link Verifier} New verifier instance. The method must not return null.
   */
  Verifier getVerifier( );
}
