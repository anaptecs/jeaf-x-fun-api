/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */

package com.anaptecs.jeaf.xfun.api.health;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.anaptecs.jeaf.xfun.api.checks.Check;
import com.anaptecs.jeaf.xfun.api.errorhandling.FailureMessage;

/**
 * This class describes the result of a health check.
 * 
 * @author JEAF Development Team
 */
public final class HealthCheckResult implements Serializable {
  /**
   * Serial version uid of this class.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Constant for the case the all checks completed successfully with neither errors nor warnings. This object can be
   * used to avoid useless memory consumption when implementing checks.
   */
  public static final HealthCheckResult CHECK_OK =
      new HealthCheckResult(HealthStatus.OK, (FailureMessage) null, (FailureMessage) null);

  /**
   * Overall health status as result of a check.
   */
  private final HealthStatus status;

  /**
   * List contains all warnings that were pending or occurred when the check was executed. The reference is never null
   * and the list is unmodifiable.
   */
  private final List<FailureMessage> warnings;

  /**
   * List contains all errors that were pending or occurred when the check was executed. The reference is never null and
   * the list is unmodifiable.
   */
  private final List<FailureMessage> errors;

  /**
   * Initialize object. Therefore the status of the service and a may be existing warning and error have to be passed.
   * 
   * @param pStatus Overall status as result of a health check. The parameter must not be null.
   */
  public HealthCheckResult( HealthStatus pStatus ) {
    this(pStatus, (FailureMessage) null, (FailureMessage) null);
  }

  /**
   * Initialize object. Therefore the status of the service and a may be existing warning and error have to be passed.
   * 
   * @param pStatus Overall status as result of a health check. The parameter must not be null.
   * @param pWarning May be existing warning of the checked service or service provider. The parameter may be null.
   * @param pError May be existing error of the checked service or service provider. The parameter may be null.
   */
  public HealthCheckResult( HealthStatus pStatus, FailureMessage pWarning, FailureMessage pError ) {
    // Check parameters.
    Check.checkInvalidParameterNull(pStatus, "pStatus");

    // Set status
    status = pStatus;

    // Set warnings
    if (pWarning != null) {
      List<FailureMessage> lWarnings = new ArrayList<>(1);
      lWarnings.add(pWarning);
      warnings = Collections.unmodifiableList(lWarnings);
    }
    else {
      warnings = Collections.emptyList();
    }

    // Set errors
    if (pError != null) {
      List<FailureMessage> lErrors = new ArrayList<>(1);
      lErrors.add(pError);
      errors = Collections.unmodifiableList(lErrors);
    }
    else {
      errors = Collections.emptyList();
    }
  }

  /**
   * Initialize object. Therefore the status of the service and may be existing warnings and errors have to be passed.
   * 
   * @param pStatus Status of the service. The parameter must not be null.
   * @param pWarnings May be existing warnings of the checked service or service provider. The parameter may be null.
   * @param pErrors May be existing errors of the checked service or service provider. The parameter may be null.
   */
  public HealthCheckResult( HealthStatus pStatus, List<FailureMessage> pWarnings, List<FailureMessage> pErrors ) {
    // Check parameters.
    Check.checkInvalidParameterNull(pStatus, "pStatus");

    // Set status
    status = pStatus;

    // Set warnings
    if (pWarnings != null) {
      warnings = Collections.unmodifiableList(pWarnings);
    }
    else {
      warnings = Collections.emptyList();
    }

    // Set errors
    if (pErrors != null) {
      errors = Collections.unmodifiableList(pErrors);
    }
    else {
      errors = Collections.emptyList();
    }
  }

  /**
   * Method returns the health status that represents the result of the check.
   * 
   * @return {@link HealthStatus} Overall health status as result of a check. The method never returns null.
   */
  public HealthStatus getHealthStatus( ) {
    return status;
  }

  /**
   * Method returns all warnings that were pending or occurred when the check was executed.
   * 
   * @return {@link List} List with all warnings. The method never returns null and the returned list is unmodifiable.
   */
  public List<FailureMessage> getWarnings( ) {
    return warnings;
  }

  /**
   * Method returns all errors that were pending or occurred when the check was executed.
   * 
   * @return {@link List} List with all errors. The method never returns null and the returned list is unmodifiable.
   */
  public List<FailureMessage> getErrors( ) {
    return errors;
  }
}
