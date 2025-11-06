/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.config;

@TestAnnotationConfig(
    booleanValue = false,
    booleanValues = { false, true, false },
    byteValue = -10,
    byteValues = { 10, 100, 127 },
    shortValue = -100,
    shortValues = { -10, -20, -30, -40 },
    intValue = -12111,
    intValues = { -10, 10, -200, 200 },
    longValue = 888899997,
    longValues = { 11110, 22220, 33330, 44440 },
    floatValue = 0.99889f,
    floatValues = { 30.1f, 30.14f },
    doubleValue = 47.12,
    doubleValues = { -1.0, -2.1, -3.2 },
    stringValue = "Goodbye",
    stringValues = { "Goodbye", "Moon" },
    testEnum = TestEnum.MAYBE,
    testEnums = { TestEnum.NO },
    classValue = String.class,
    classValues = { Boolean.class, Byte.class, Short.class })
public interface CustomConfiguration {

}
