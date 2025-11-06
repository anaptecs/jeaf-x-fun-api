/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.messages;

import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.errorhandling.SystemException;
import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;
import com.anaptecs.jeaf.xfun.bootstrap.Assert;

/**
 * Class implements a message id that can be used to identify message objects that are stored in the message repository.
 * 
 * @see com.anaptecs.jeaf.fwk.core.MessageRepositoryImpl
 */
public class MessageID extends LocalizedObject {
  /**
   * Generated serial version uid for this class.
   * 
   * Since this version of the class (JEAF Release 1.2 and higher) is incompatible to its previous version from release
   * 1.1.x a serialization between them is not possible.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Trace level of the represented message.
   */
  private final TraceLevel traceLevel;

  /**
   * Initialize created MessageID object. The class ensures that every MessageID object has a unique message code and so
   * for each message code only one object can be created.
   * 
   * @param pMessageCode Message code of the MessageID object that should be created. The parameter must be zero or
   * greater.
   * @param pTraceLevel Level of the Message ID object that should be created. The TraceLevel can't be null
   * @throws SystemException if the passed message code was already used to create an MessageID object.
   */
  public MessageID( int pMessageCode, TraceLevel pTraceLevel ) throws SystemException {
    super(pMessageCode);

    // Check if pMessageCode is a valid parameter.
    Assert.assertNotNull(pTraceLevel, "pTraceLevel");

    // Set the trace level.
    traceLevel = pTraceLevel;
  }

  /**
   * Method returns the trace level of the represented message.
   * 
   * @return {@link TraceLevel} Trace level that is used for messages of this type by default. The method never returns
   * null.
   */
  public TraceLevel getTraceLevel( ) {
    return traceLevel;
  }

  /**
   * Method checks if tracing is enabled this message.
   * 
   * @return boolean Method returns true if tracing is enabled for the represented message and false in all other cases.
   */
  public boolean isEnabled( ) {
    return XFun.getTrace().isLevelEnabled(traceLevel);
  }
}
