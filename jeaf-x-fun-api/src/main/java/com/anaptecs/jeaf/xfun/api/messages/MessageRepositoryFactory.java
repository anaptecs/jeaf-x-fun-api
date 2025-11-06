/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.messages;

import com.anaptecs.jeaf.xfun.annotations.XFunConfig;

/**
 * Interface defines the factory to create a new message repository implementation. Which factory should be used can be
 * configured using annotation {@link XFunConfig}
 */
public interface MessageRepositoryFactory {
  /**
   * Method creates a new instance of the message repository.
   * 
   * @return {@link MessageRepository} New instance of the message repository. The method must not return null.
   */
  MessageRepository getMessageRepository( );
}
