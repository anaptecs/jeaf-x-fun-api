/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.trace;

import com.anaptecs.jeaf.xfun.annotations.TraceConfig;

@TraceConfig(
    indentSize = 2,
    indentTrace = false,
    showCurrentUserInTraces = true,
    traceMessageFormat = "abc",
    customTraceLocale = "de",
    traceWithSystemLocale = false,
    objectFormattersResourcePath = "META-INF/TEST/TRACE/INVALID_OBJECT_FORMATTERS")
public interface InvalidFormatterTraceConfig {
}
