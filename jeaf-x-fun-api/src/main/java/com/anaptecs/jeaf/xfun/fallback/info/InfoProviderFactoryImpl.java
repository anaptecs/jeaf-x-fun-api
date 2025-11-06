/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.fallback.info;

import com.anaptecs.jeaf.xfun.api.info.InfoProvider;
import com.anaptecs.jeaf.xfun.api.info.InfoProviderFactory;

public class InfoProviderFactoryImpl implements InfoProviderFactory {
  private final InfoProvider infoProvider = new InfoProviderImpl();

  @Override
  public InfoProvider getInfoProvider( ) {
    return infoProvider;
  }
}
