/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */

package com.anaptecs.jeaf.xfun.api.datatypeconverter;

import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFSystemException;

/**
 * This class manages all available datatype converters. In order to avoid that there is more than one registry of
 * datatype converters, this class is realized as singleton.
 * 
 * @author JEAF Development Team
 * @version 1.0
 */
public interface DatatypeConverterRegistry {
  /**
   * Method returns the configured datatype converter registry.
   * 
   * @return DatatypeConverterRegistry Registry that is used. The method never returns null.
   */
  static DatatypeConverterRegistry getDatatypeConverterRegistry( ) {
    return XFun.getDatatypeConverterRegistry();
  }

  /**
   * Method returns the appropriate converter for the passed combination of in- and output type.
   * 
   * @param <I> Input type. The parameter must not be null.
   * @param <O> Output type. The parameter must not be null.
   * @param pInputType InputType The parameter must not be null.
   * @param pOutputType OutputType. The parameter must not be null
   * @return {@link DatatypeConverter} Datatype converter for the passed combination of in- and output type. The method
   * never returns null.
   * @throws JEAFSystemException In the case that there is no converter available for a specific combination an
   * exception will be thrown.
   */
  <I, O> DatatypeConverter<I, O> getConverter( Class<I> pInputType, Class<O> pOutputType );
}