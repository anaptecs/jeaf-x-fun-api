/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.config;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

import com.anaptecs.jeaf.xfun.api.config.AnnotationBasedConfiguration;

public class InvalidAnnotationConfiguration extends AnnotationBasedConfiguration<InvalidAnnotation> {

  public InvalidAnnotationConfiguration( boolean pExceptionOnError ) {
    super(null, null, pExceptionOnError);
  }

  @Override
  protected Class<InvalidAnnotation> getAnnotationClass( ) {
    return InvalidAnnotation.class;
  }

  @Override
  protected String getDefaultConfigurationClass( ) {
    return null;
  }

  @Override
  public InvalidAnnotation getEmptyConfiguration( ) {
    return new InvalidAnnotation() {

      @Override
      public Class<? extends Annotation> annotationType( ) {
        // TODO Auto-generated method stub
        return InvalidAnnotation.class;
      }

      @Override
      public String value( ) {
        return null;
      }
    };
  }

  @Override
  public List<String> checkCustomConfiguration( InvalidAnnotation pCustomConfiguration ) {
    return Collections.emptyList();
  }

}
