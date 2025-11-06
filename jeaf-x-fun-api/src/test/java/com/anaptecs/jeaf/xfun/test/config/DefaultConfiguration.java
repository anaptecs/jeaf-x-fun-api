/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.config;

@TestAnnotationConfig(
    booleanValue = true,
    booleanValues = { true, false, true },
    byteValue = 127,
    byteValues = { 0, 1, 2 },
    shortValue = -1,
    shortValues = { -1, -2, -3, -4 },
    intValue = -12,
    intValues = { -1, 1, -2, 2 },
    longValue = 88889999,
    longValues = { 1111, 2222, 3333, 4444 },
    floatValue = 0.889f,
    floatValues = { 3.1f, 3.14f },
    doubleValue = 47.11,
    doubleValues = { 1.0, 2.1, 3.2 },
    stringValue = "Hello",
    stringValues = { "Hello", "World" },
    testEnum = TestEnum.YES,
    testEnums = { TestEnum.YES, TestEnum.NO, TestEnum.MAYBE },
    classValue = Object.class,
    classValues = {},
    classWithDefault = DefaultTestInterfaceImpl.class)
public interface DefaultConfiguration {
}
