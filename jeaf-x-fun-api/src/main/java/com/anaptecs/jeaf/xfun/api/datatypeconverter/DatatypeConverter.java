/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */

package com.anaptecs.jeaf.xfun.api.datatypeconverter;

/**
 * Interface defines all methods of a datatype converter as used by JEAF. Converters can be used in several places all
 * over the code. JEAF already provides as set of default converters for the most common conversions. In addition
 * individual converters can be provided by implementing this interface. All realizations of this interface have to
 * provide an empty default constructor and must not have any state.
 * 
 * JEAFs default convert implementations are all located in the package
 * <code>com.anaptecs.jeaf.core.util.converters</code>.
 * 
 * @param <I> is the InputType which will be converted into the Output Type.
 * @param <O> is the Type which is the result of the conversion of the InputType.
 * @author JEAF Development Team
 * @version 1.0
 */
public interface DatatypeConverter<I, O> {
  /**
   * Method returns the input type that is supported by the datatype converter implementation.
   * 
   * @return {@link Class} Class object describing the supported input type. The method must not return null.
   */
  Class<I> getInputType( );

  /**
   * Method returns the output type that is supported by the datatype converter implementation.
   * 
   * @return {@link Class} Class object describing the supported output type. The method must not return null.
   */
  Class<O> getOutputType( );

  /**
   * Method converts the passed input type object to the defined output type.
   * 
   * @param pInput Input type object that should be converted. The parameter may be null.
   * @return OutputType Output type object to which the input type was converted. The method may return null.
   */
  O convert( I pInput );
}
