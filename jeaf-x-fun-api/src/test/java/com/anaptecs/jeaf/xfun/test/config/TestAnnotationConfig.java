/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TestAnnotationConfig {
  boolean booleanValue();

  boolean[] booleanValues();

  byte byteValue();

  byte[] byteValues();

  short shortValue();

  short[] shortValues();

  int intValue();

  int[] intValues();

  long longValue();

  long[] longValues();

  float floatValue();

  float[] floatValues();

  double doubleValue();

  double[] doubleValues();

  String stringValue();

  String[] stringValues();

  TestEnum testEnum();

  TestEnum[] testEnums();

  Class<?> classValue();

  Class<?>[] classValues();

  Class<? extends TestInterface> classWithDefault() default TestInterface.class;
}
