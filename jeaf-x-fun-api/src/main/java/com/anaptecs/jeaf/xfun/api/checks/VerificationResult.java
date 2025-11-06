/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.checks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.XFunMessages;
import com.anaptecs.jeaf.xfun.api.errorhandling.ExceptionInfoProvider;
import com.anaptecs.jeaf.xfun.api.errorhandling.FailureMessage;
import com.anaptecs.jeaf.xfun.api.messages.MessageID;
import com.anaptecs.jeaf.xfun.api.messages.MessageRepository;
import com.anaptecs.jeaf.xfun.bootstrap.Check;

/**
 * Class provides information about the result of constraint checks that are performed by verifiable objects. This class
 * is intended to be used by classes that implement the interface Verifiable and have to return a result of the
 * performed constraint checks. The class differs between two types of failed constraint checks. If the first category
 * of checks fails a warning is reported if the second category fails an error is reported. Its the callers task how to
 * react on either errors or warnings or both. A verifiable object only indicates which constraints are not fulfilled.
 * 
 * @see com.anaptecs.jeaf.xfun.api.checks.Verifiable
 */
public final class VerificationResult implements Serializable {

  /**
   * System's line separator.
   */
  private static final String LINE_SEPERATOR = System.getProperty("line.separator");

  /**
   * Generated serial version uid for this class.
   */
  private static final long serialVersionUID = 1L;

  /**
   * List contains all verification failures that were added as warnings to this object. In order to reduce memory
   * consumption as much as possible the reference is lazy initialized. Due to this fact the attribute must not be
   * access directly. Whenever access to the collection of warnings is required the method <code>getWarnings()</code>
   * has to be used.
   */
  private List<FailureMessage> warnings;

  /**
   * List contains all verification failures that were added as errors to this objects. In order to reduce memory
   * consumption as much as possible the reference is lazy initialized. Due to this fact the attribute must not be
   * access directly. Whenever access to the collection of warnings is required the method <code>getErrors()</code> has
   * to be used.
   */
  private List<FailureMessage> errors;

  /**
   * Initialize object. No further actions are performed.
   */
  public VerificationResult( ) {
    warnings = null;
    errors = null;
  }

  /**
   * Initialize object. No further actions are performed.
   * 
   * @param pError Error message that should be added to the created verification result. The parameter may be null.
   */
  public VerificationResult( FailureMessage pError ) {
    if (pError != null) {
      this.addError(pError);
    }
  }

  /**
   * Initialize object. No further actions are performed.
   * 
   * @param pError Error message that should be added to the created verification result. The parameter may be null.
   * @param pWarning Warning message that should be added to the created verification result. The parameter may be null.
   */
  public VerificationResult( FailureMessage pError, FailureMessage pWarning ) {
    if (pError != null) {
      this.addError(pError);
    }

    if (pWarning != null) {
      this.addWarning(pWarning);
    }
  }

  /**
   * Method returns the list of warnings that were added to this object. The returned list is not modifiable by the
   * caller.
   * 
   * @return {@link List} List contains all verification failures that were added as warning. The method never returns
   * null. The returned list must / can not be modified by the caller.
   */
  public List<FailureMessage> getWarnings( ) {
    return Collections.unmodifiableList(this.getWarningsCollection());
  }

  /**
   * Method checks whether VerificationFailure objects were added to this object as warning.
   * 
   * @return boolean The method returns true if at least one VerificationFailure object was added as warning. In all
   * other cases the method returns false.
   */
  public boolean containsWarnings( ) {
    boolean lContainsWarnings;
    // Collection with warnings was already created.
    if (warnings != null) {
      lContainsWarnings = !warnings.isEmpty();
    }
    // No collection with warnings was created yet.
    else {
      lContainsWarnings = false;
    }
    // Return result of check.
    return lContainsWarnings;
  }

  /**
   * Method returns the collection of warnings that were already added to this object. If no messages were added yet a
   * new and empty collection will be returned.
   * 
   * @return Collection with all warnings that were already added. The method never returns null. The returned
   * collection may be modified by the caller.
   */
  private List<FailureMessage> getWarningsCollection( ) {
    // Check if collection of warnings was already created.
    if (warnings == null) {
      warnings = new ArrayList<>();
    }
    return warnings;
  }

  /**
   * Method adds the passed verification failure to the list of warnings. Using this method a verifiable object can
   * report a warning that occurred during a constraint check.
   * 
   * @param pVerificationFailure Description of the constraint check that failed. The parameter may be null. In this
   * case no actions are performed.
   */
  public void addWarning( FailureMessage pVerificationFailure ) {
    // Check pVerificationFailure for null.
    if (pVerificationFailure != null) {
      // Add verification failure to the collection of warnings.
      Collection<FailureMessage> lWarnings = this.getWarningsCollection();
      lWarnings.add(pVerificationFailure);
    }
  }

  /**
   * Method adds the passed exception info as warning to this VerificationResult. Method is only there for more API
   * convenience as it simply converts the exception info into a {@link FailureMessage} and all JEAF exceptions
   * implement interface {@link ExceptionInfoProvider}.
   * 
   * @param pExceptionInfo Information about exception that should be added as warning. The parameter may be null. In
   * this case nothing will be done.
   */
  public void addWarning( ExceptionInfoProvider pExceptionInfo ) {
    if (pExceptionInfo != null) {
      FailureMessage lFailureMessage = new FailureMessage(pExceptionInfo.getErrorCode(),
          pExceptionInfo.getMessageParameters(), pExceptionInfo.getCause());
      this.addWarning(lFailureMessage);
    }
  }

  /**
   * Method creates a new {@link FailureMessage} from the passed parameters and adds them as warning to this
   * VerificationResult. Method is only there for more API convenience.
   * 
   * @param pMessageID MessageID of the failure message that will be created. The parameter must not be null.
   * @param pParams String to parameterize the failure message. The parameter may be null.
   */
  public void addWarning( MessageID pMessageID, String... pParams ) {
    this.addWarning(pMessageID, null, pParams);
  }

  /**
   * Method creates a new {@link FailureMessage} from the passed parameters and adds them as warning to this
   * VerificationResult. Method is only there for more API convenience.
   * 
   * @param pMessageID MessageID of the failure message that will be created. The parameter must not be null.
   * @param pThrowable Exception that is the cause for this failure message. The parameter may be null.
   * @param pParams String to parameterize the failure message. The parameter may be null.
   */
  public void addWarning( MessageID pMessageID, Throwable pThrowable, String... pParams ) {
    FailureMessage lFailureMessage = new FailureMessage(pMessageID, pParams, pThrowable);
    this.addWarning(lFailureMessage);
  }

  /**
   * Method returns the list of errors that were added to this object. The returned list is not modifiable by the
   * caller.
   * 
   * @return {@link List} List contains all verification failures that were added as errors. The method never returns
   * null. The returned list must / can not be modified by the caller.
   */
  public List<FailureMessage> getErrors( ) {
    return Collections.unmodifiableList(this.getErrorsCollection());
  }

  /**
   * Method checks whether VerificationFailure objects were added to this object as error.
   * 
   * @return boolean The method returns true if at least one FailureMessage object was added as error. In all other
   * cases the method returns false.
   */
  public boolean containsErrors( ) {
    boolean lContainsErrors;
    // Collection with errors was already created.
    if (errors != null) {
      lContainsErrors = !errors.isEmpty();
    }
    // No collection with errors was created yet.
    else {
      lContainsErrors = false;
    }
    // Return result of check.
    return lContainsErrors;
  }

  /**
   * Method returns the collection of errors that were already added to this object. If no messages were added yet a new
   * and empty collection will be returned.
   * 
   * @return Collection with all errors that were already added. The method never returns null. The returned collection
   * may be modified by the caller.
   */
  private List<FailureMessage> getErrorsCollection( ) {
    // Check if collection of errors was already created.
    if (errors == null) {
      errors = new ArrayList<>();
    }
    return errors;
  }

  /**
   * Method checks whether VerificationFailure objects were added to this object either as error or as warning.
   * 
   * @return boolean The method returns true if at least one FailureMessage object was added to this object no matter if
   * as error or warning.
   */
  public boolean containsFailures( ) {
    return this.containsErrors() || this.containsWarnings();
  }

  /**
   * Method adds the passed verification failure to the list of errors. Using this method a verifiable object can report
   * an error that occurred during a constraint check.
   * 
   * @param pVerificationFailure Description of the constraint check that failed. The parameter may be null. In this
   * case no actions are performed.
   */
  public void addError( FailureMessage pVerificationFailure ) {
    // Check pVerificationFailure for null.
    if (pVerificationFailure != null) {
      // Add verification failure to the collection of errors.
      Collection<FailureMessage> lErrors = this.getErrorsCollection();
      lErrors.add(pVerificationFailure);
    }
  }

  /**
   * Method adds the passed exception info as error to this VerificationResult. Method is only there for more API
   * convenience as it simply converts the exception info into a {@link FailureMessage} and all JEAF exceptions
   * implement interface {@link ExceptionInfoProvider}.
   * 
   * @param pExceptionInfo Information about exception that should be added as error. The parameter may be null. In this
   * case nothing will be done.
   */
  public void addError( ExceptionInfoProvider pExceptionInfo ) {
    if (pExceptionInfo != null) {
      FailureMessage lFailureMessage = new FailureMessage(pExceptionInfo.getErrorCode(),
          pExceptionInfo.getMessageParameters(), pExceptionInfo.getCause());
      this.addError(lFailureMessage);
    }
  }

  /**
   * Method creates a new {@link FailureMessage} from the passed parameters and adds them as error to this
   * VerificationResult. Method is only there for more API convenience.
   * 
   * @param pMessageID MessageID of the failure message that will be created. The parameter must not be null.
   * @param pParams String to parameterize the failure message. The parameter may be null.
   */
  public void addError( MessageID pMessageID, String... pParams ) {
    this.addError(pMessageID, null, pParams);
  }

  /**
   * Method creates a new {@link FailureMessage} from the passed parameters and adds them as error to this
   * VerificationResult. Method is only there for more API convenience.
   * 
   * @param pMessageID MessageID of the failure message that will be created. The parameter must not be null.
   * @param pThrowable Exception that is the cause for this failure message. The parameter may be null.
   * @param pParams String to parameterize the failure message. The parameter may be null.
   */
  public void addError( MessageID pMessageID, Throwable pThrowable, String... pParams ) {
    FailureMessage lFailureMessage = new FailureMessage(pMessageID, pParams, pThrowable);
    this.addError(lFailureMessage);
  }

  /**
   * Method adds all warnings and errors of the passed VerificationResult object to this object.
   * 
   * @param pVerificationResult Result object containing all warnings and errors that should be added to this object.
   * The parameter may be null. In this case no actions will be performed.
   */
  public void addVerificationResult( VerificationResult pVerificationResult ) {
    // Check parameter for null.
    if (pVerificationResult != null) {
      // Copy warnings from passed verify result.
      if (pVerificationResult.containsWarnings() == true) {
        Collection<FailureMessage> lWarnings = this.getWarningsCollection();
        lWarnings.addAll(pVerificationResult.getWarnings());
      }

      // Copy warnings from passed verify result.
      if (pVerificationResult.containsErrors() == true) {
        Collection<FailureMessage> lErrors = this.getErrorsCollection();
        lErrors.addAll(pVerificationResult.getErrors());
      }
    }
  }

  /**
   * Method returns a message string describing the verification result. In the case of a failed verification the
   * returned string describes all the failed verifications. The method uses the current default locale to localize the
   * created message string.
   * 
   * @return String Description of the verification result. The method never returns null.
   */
  public String getMessage( ) {
    // Verification result contains at least one error or warning.
    MessageRepository lMessageRepository = XFun.getMessageRepository();
    String lMessage;
    if (this.containsFailures() == true) {
      Collection<FailureMessage> lErrors = this.getErrors();
      Collection<FailureMessage> lWarnings = this.getWarnings();

      // Create description of errors and warning.
      String lErrorsDescription = this.createDescription(lErrors);
      String lWarningsDescription = this.createDescription(lWarnings);
      String lErrorCount = Integer.toString(lErrors.size());
      String lWarningCount = Integer.toString(lWarnings.size());
      String[] lParams = new String[] { lErrorCount, lWarningCount, lErrorsDescription, lWarningsDescription };
      lMessage = lMessageRepository.getMessage(XFunMessages.VERIFICATION_FAILED, lParams);
    }
    // Verification was successful.
    else {
      lMessage = lMessageRepository.getMessage(XFunMessages.VERIFICATION_SUCCESSFUL);
    }
    // Return message.
    return lMessage;
  }

  /**
   * Method creates a string describing all VerificationFailure objects of the passed collection.
   * 
   * @param pVerificationFailures Collection with all VerificationFailure objects which are used to create a
   * description. The parameter must not be null and all objects within the collection must be an instance of class
   * VerificationFailure.
   * @return String Description of the passed verification failures. The method never returns null.
   */
  private String createDescription( Collection<FailureMessage> pVerificationFailures ) {
    // Check parameters for null.
    Check.checkInvalidParameterNull(pVerificationFailures, "pVerificationFailures");

    final int lMultiplicator = 64;
    StringBuilder lBuilder = new StringBuilder(pVerificationFailures.size() * lMultiplicator);
    Iterator<FailureMessage> lIterator = pVerificationFailures.iterator();
    while (lIterator.hasNext()) {
      // Get next verification failure.
      FailureMessage lNextVerificationFailure = lIterator.next();
      lBuilder.append(LINE_SEPERATOR);
      lBuilder.append("- ");
      lBuilder.append(lNextVerificationFailure.getMessage());
    }
    // Return description.
    return lBuilder.toString();
  }
}
