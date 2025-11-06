/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.trace;

import com.anaptecs.jeaf.xfun.annotations.TraceConfig;

@TraceConfig(
    indentSize = -12,
    indentTrace = false,
    showCurrentUserInTraces = true,
    traceMessageFormat = "abc",
    customTraceLocale = "invalid_locale",
    traceWithSystemLocale = false)
public interface InvalidTraceConfig {
}
