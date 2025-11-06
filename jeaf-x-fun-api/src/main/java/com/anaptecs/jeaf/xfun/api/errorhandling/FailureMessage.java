/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.errorhandling;

import java.io.Serializable;
import java.util.Locale;

import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.messages.MessageID;
import com.anaptecs.jeaf.xfun.api.messages.MessageRepository;
import com.anaptecs.jeaf.xfun.bootstrap.Assert;
import com.anaptecs.jeaf.xfun.bootstrap.Check;

/**
 * Class is used to indicate that a verification implemented by a verify method failed. Verify methods return instances
 * of this class to describe which verification failed and why.
 */
public final class FailureMessage implements Serializable {
  /**
   * Generated serial version uid for this class.
   */
  private static final long serialVersionUID = 1;

  /**
   * ID of the message that describes the failed verification. The reference is never null, since a not null MessageID
   * object has to passed to the class's constructor.
   */
  private MessageID messageID;

  /**
   * Array of strings that is used to parameterize a message string. The reference may be null.
   */
  private String[] messageParameters;

  /**
   * Throwable object that caused a verification to fail. The reference may be null, since not all verifications fail
   * due to a thrown exception.
   */
  private Throwable cause;

  /**
   * Initialize object with the passed message id and message parameters.
   * 
   * @param pMessageID ID of the message that describes the failed verification. The parameter must not be null.
   */
  public FailureMessage( MessageID pMessageID ) {
    this(pMessageID, null, null);
  }

  /**
   * Initialize object with the passed message id and message parameters.
   * 
   * @param pMessageID ID of the message that describes the failed verification. The parameter must not be null.
   * @param pMessageParameters Message parameters that are used to parameterized a message string that describes the
   * failed verification. The parameter may be null.
   */
  public FailureMessage( MessageID pMessageID, String... pMessageParameters ) {
    this(pMessageID, pMessageParameters, null);
  }

  /**
   * Initialize object with the passed message id and message parameters.
   * 
   * @param pMessageID ID of the message that describes the failed verification. The parameter must not be null.
   * @param pMessageParameters Message parameters that are used to parameterized a message string that describes the
   * failed verification. The parameter may be null.
   * @param pCause Throwable object that caused the verification to fail. The parameter may be null.
   */
  public FailureMessage( MessageID pMessageID, String[] pMessageParameters, Throwable pCause ) {
    // Check message id for null.
    Assert.assertNotNull(pMessageID, "pMessageID");

    messageID = pMessageID;
    if (pMessageParameters != null) {
      messageParameters = pMessageParameters.clone();
    }
    else {
      messageParameters = null;
    }
    cause = pCause;
  }

  /**
   * Method returns the id of the message that describes the failed verification.
   * 
   * @return MessageID ID of the message that describes the failed verification. The method never returns null.
   */
  public MessageID getMessageID( ) {
    return messageID;
  }

  /**
   * Method returns the message parameters that are used to parameterize the message string that describes the failed
   * verification.
   * 
   * @return String[] String array with message parameters that are used to parameterize a message string. The method
   * may return null. If message parameters are set a copy of this array will be returned.
   */
  public String[] getMessageParameters( ) {
    // Return copy of string array with message parameters.
    String[] lCopiedMessageParameters;
    if (messageParameters != null) {
      lCopiedMessageParameters = new String[messageParameters.length];
      System.arraycopy(messageParameters, 0, lCopiedMessageParameters, 0, messageParameters.length);
    }
    // No message parameters set.
    else {
      lCopiedMessageParameters = null;
    }
    return lCopiedMessageParameters;
  }

  /**
   * Method returns a message string describing the failed verification. The method uses the current default locale to
   * localize the created message string.
   * 
   * @return String Description of the failed verification. The method never returns null.
   */
  public String getMessage( ) {
    MessageRepository lMessageRepository = XFun.getMessageRepository();
    return lMessageRepository.getMessage(messageID, messageParameters);
  }

  /**
   * Method returns the Throwable object that caused a verification to fail.
   * 
   * @return Throwable Throwable object that caused the verification to fail. Since not all verifications fail due to a
   * thrown exception the method may return null.
   */
  public Throwable getCause( ) {
    return cause;
  }

  /**
   * Method returns the failure message represented by this object.
   * 
   * @return {@link String} Failure message. The method never return null.
   */
  @Override
  public String toString( ) {
    return XFun.getMessageRepository().getMessage(messageID, messageParameters);
  }

  /**
   * Method return the failure message represented by this object using the passed locale.
   * 
   * @param pLocale Locale that should be used for the failure message. The parameter must not be null.
   * @return {@link String} Created failure message. The message never returns null.
   */
  public String toString( Locale pLocale ) {
    // Check parameter
    Check.checkInvalidParameterNull(pLocale, "pLocale");

    return XFun.getMessageRepository().getMessage(messageID, pLocale, messageParameters);
  }

  /**
   * Method writes this failure message to the log file. The used trace level depends on the message id.
   */
  public void trace( ) {
    XFun.getTrace().write(messageID, cause, messageParameters);
  }
}
