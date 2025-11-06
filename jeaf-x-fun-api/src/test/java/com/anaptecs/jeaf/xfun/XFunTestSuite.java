/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun;

import com.anaptecs.jeaf.xfun.test.trace.FallbackTraceTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("X-Fun API Test Suite")
@SelectClasses({ MulitThreadedInitTest.class, FallbackTraceTest.class })
@SelectPackages(value = "com.anaptecs.jeaf.xfun")
public class XFunTestSuite {
}
