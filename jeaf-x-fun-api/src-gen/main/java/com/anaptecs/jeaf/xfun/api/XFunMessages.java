package com.anaptecs.jeaf.xfun.api;

import com.anaptecs.jeaf.xfun.annotations.MessageResource;
import com.anaptecs.jeaf.xfun.api.errorhandling.ErrorCode;
import com.anaptecs.jeaf.xfun.api.messages.MessageID;
import com.anaptecs.jeaf.xfun.api.messages.MessageRepository;

/**
 * Class contains all generated message constants for JEAF X-Functions.
 *
 * @author JEAF Development Team
 * @version 1.0
 */
@MessageResource(path = "XFunMessages.xml")
public final class XFunMessages {
  /**
   * Constant for XML file that contains all messages that are defined within this class.
   */
  private static final String MESSAGE_RESOURCE = "XFunMessages.xml";

  /**
   * Info-Message is used to trace the version details of the application.
   */
  public static final MessageID APPLICATION_VERSION_INFO;

  /**
   * This error code is used by the verifier to indicate that a specific class is not within the application's
   * classpath.
   */
  public static final ErrorCode CLASS_NOT_LOADABLE;

  /**
   * This error code is used by the verifier to indicate that a passed class had the invalid value null.
   */
  public static final ErrorCode NULL_IS_NOT_A_VALID_CLASS_NAME;

  /**
   * This error code is used by the verifier to indicate that the passed values do not define a valid set.
   */
  public static final ErrorCode SET_IS_INVALID;

  /**
   * This error code is used by the verifier to indicate that a passed value is not part of a defined set.
   */
  public static final ErrorCode VALUE_IS_NOT_PART_OF_SET;

  /**
   * This error code is used by the verifier to indicate that a passed object is not assignable to a class or interface.
   */
  public static final ErrorCode OBJECT_IS_NOT_ASSIGNABLE;

  /**
   * This error code is used by the verifier to indicate that a passed string is null.
   */
  public static final ErrorCode NULL_IS_NOT_A_REAL_STRING;

  /**
   * This error code is used by the verifier to indicate that a passed string is empty.
   */
  public static final ErrorCode STRING_IS_EMPTY;

  /**
   * This message code is to indicate that a verification result does neither contain errors nor warnings.
   */
  public static final MessageID VERIFICATION_SUCCESSFUL;

  /**
   * This error code is used to indicate that a verification failed.
   */
  public static final ErrorCode VERIFICATION_FAILED;

  /**
   * This error code is used to indicate that a verification failed.
   */
  public static final ErrorCode CONSTRAINT_CHECK_FAILED_DUE_TO_EXCEPTION;

  /**
   * This error code is used by the verifier to indicate that a passed value is part of a defined set.
   */
  public static final ErrorCode VALUE_IS_PART_OF_SET;

  /**
   * This error code is used by the verifier to indicate that a passed set is not a subset of the other passed set.
   */
  public static final ErrorCode INVALID_SUBSET;

  /**
   * This error code is used by the verifier to indicate that the passed sets do not have a required intersection.
   */
  public static final ErrorCode SETS_HAVE_NO_INTERSECTION;

  /**
   * This error code is used by the verifier to indicate that the passed sets do not have an empty intersection.
   */
  public static final ErrorCode SETS_HAVE_INTERSECTION;

  /**
   * Exceptions with this error code are thrown if a verification for a null value fails.
   */
  public static final ErrorCode IS_NULL;

  /**
   * Exceptions with this error code are thrown if a verification for a pattern fails.
   */
  public static final ErrorCode PATTERN_NOT_MATCHED;

  /**
   * Exceptions with this error code are thrown if a verification for the maximum string length fails.
   */
  public static final ErrorCode STRING_TOO_LONG;

  /**
   * Exceptions with this error code are thrown if the method 'Trace.getCurrentTrace()' is called outside of a service
   * call.
   */
  public static final ErrorCode NO_CURRENT_TRACE_OBJECT;

  /**
   * Exceptions with this error code are thrown if a verification for a minimum collection size fails.
   */
  public static final ErrorCode COLLECTION_DOES_NOT_HAVE_MIN_SIZE;

  /**
   * Exceptions with this error code are thrown if a verification for a maximum collection size fails.
   */
  public static final ErrorCode COLLECTION_EXCEEDS_MAX_SIZE;

  /**
   * Exceptions with this error code are thrown if a verification for a valid period fails.
   */
  public static final ErrorCode INVALID_PERIOD;

  /**
   * Exceptions with this error code are thrown if one or more constraint vialoations fail.
   */
  public static final ErrorCode CONSTRAINT_VIOLATION;

  /**
   * Exceptions with this error code are thrown if an internal error occurs. An internal error are such things as not
   * expected subclass etc.
   */
  public static final ErrorCode INTERNAL_ERROR;

  /**
   * Exceptions with this error code are thrown if an condition is not fullfilled
   */
  public static final ErrorCode NOT_TRUE;

  /**
   * Exceptions with this error code are thrown if an condition is not fullfilled
   */
  public static final ErrorCode NOT_FALSE;

  /**
   * Exceptions with this error code are thrown if a verification for an email address fails.
   */
  public static final ErrorCode INVALID_EMAIL_ADDRESS;

  /**
   * Exceptions with this error code are thrown if an unexpected enumeration literal is met.
   */
  public static final ErrorCode UNEXPECTED_ENUM_LITERAL;

  /**
   * Exceptions with this error code are thrown if the ConverterRegistry contains no datatype converter for a passed
   * combination of in- and output type.
   */
  public static final ErrorCode NO_DATATYPE_CONVERTER_DEFINED;

  /**
   * Exceptions with this error code are thrown if an existing datatype converter illegally should be overwritten.
   */
  public static final ErrorCode DATATYPE_CONVERTER_ALREADY_EXISTS;

  /**
   * Error code is used in case that a datatype conversion is not possible.
   */
  public static final ErrorCode DATATYPE_CONVERSION_NOT_POSSIBLE;

  /**
   * Trace message for reading properties.
   */
  public static final MessageID LOADING_JEAF_PROPERTIES;

  /**
   * Trace message for location of properties.
   */
  public static final MessageID PROPERTIES_LOCATION;

  /**
   * Trace message for every loaded property.
   */
  public static final MessageID TRACE_PROPERTY_VALUE;

  /**
   * Exceptions with this error code if a resource access providers tries to read an url from a resource but its value
   * is malformed.
   */
  public static final ErrorCode MALFORMED_URL_PROPERTY;

  /**
   * Messages with this message id are traced if an exception occurs when trying to load a locale provider.
   */
  public static final ErrorCode UNABLE_TO_LOAD_LOCALE_PROVIDER;

  /**
   * Exceptions with this error code are thrown if the maximum value of a base 36 encoded value is exceeded.
   */
  public static final ErrorCode MAX_BASE36_VALUE_EXCEEDED;

  /**
   * Exceptions with this error code are thrown if an invalid value is used for a digit of a base 36 encoded number.
   */
  public static final ErrorCode INVALID_BASE_36_DIGIT;

  /**
   * Messages with this message id are traced if an exception occurs when to open a resource bundle.
   */
  public static final ErrorCode RESOURCE_NOT_FOUND;

  /**
   * Messages with this message id are traced if an exception occurs when to open a file as resource.
   */
  public static final ErrorCode FILE_NOT_FOUND;

  /**
   * Message contains content for first line about an object.
   */
  public static final MessageID OBJECT_INFO;

  /**
   * Message contains header line for attributes of an object.
   */
  public static final MessageID OBJECT_ATTRIBUTES_SECTION;

  /**
   * Message contains name and value of one attribute of an object.
   */
  public static final MessageID OBJECT_ATTRIBUTE;

  /**
   * Error code is used in case that Java releases are compared and at least one of them is an unknown Java release.
   */
  public static final ErrorCode UNABLE_TO_COMPARE_UNKNOWN_JAVA_RELEASES;
  /**
   * Static initializer contains initialization for all generated constants.
   */
  static {
    MessageRepository lRepository = XFun.getMessageRepository();
    lRepository.loadResource(MESSAGE_RESOURCE);
    // Handle all info messages.
    APPLICATION_VERSION_INFO = lRepository.getMessageID(20);
    VERIFICATION_SUCCESSFUL = lRepository.getMessageID(28);
    LOADING_JEAF_PROPERTIES = lRepository.getMessageID(51);
    PROPERTIES_LOCATION = lRepository.getMessageID(52);
    TRACE_PROPERTY_VALUE = lRepository.getMessageID(53);
    OBJECT_INFO = lRepository.getMessageID(60);
    OBJECT_ATTRIBUTES_SECTION = lRepository.getMessageID(61);
    OBJECT_ATTRIBUTE = lRepository.getMessageID(62);
    // Handle all messages for errors.
    CLASS_NOT_LOADABLE = lRepository.getErrorCode(21);
    NULL_IS_NOT_A_VALID_CLASS_NAME = lRepository.getErrorCode(22);
    SET_IS_INVALID = lRepository.getErrorCode(23);
    VALUE_IS_NOT_PART_OF_SET = lRepository.getErrorCode(24);
    OBJECT_IS_NOT_ASSIGNABLE = lRepository.getErrorCode(25);
    NULL_IS_NOT_A_REAL_STRING = lRepository.getErrorCode(26);
    STRING_IS_EMPTY = lRepository.getErrorCode(27);
    VERIFICATION_FAILED = lRepository.getErrorCode(29);
    CONSTRAINT_CHECK_FAILED_DUE_TO_EXCEPTION = lRepository.getErrorCode(30);
    VALUE_IS_PART_OF_SET = lRepository.getErrorCode(31);
    INVALID_SUBSET = lRepository.getErrorCode(32);
    SETS_HAVE_NO_INTERSECTION = lRepository.getErrorCode(33);
    SETS_HAVE_INTERSECTION = lRepository.getErrorCode(34);
    IS_NULL = lRepository.getErrorCode(35);
    PATTERN_NOT_MATCHED = lRepository.getErrorCode(36);
    STRING_TOO_LONG = lRepository.getErrorCode(37);
    NO_CURRENT_TRACE_OBJECT = lRepository.getErrorCode(38);
    COLLECTION_DOES_NOT_HAVE_MIN_SIZE = lRepository.getErrorCode(39);
    COLLECTION_EXCEEDS_MAX_SIZE = lRepository.getErrorCode(40);
    INVALID_PERIOD = lRepository.getErrorCode(41);
    CONSTRAINT_VIOLATION = lRepository.getErrorCode(42);
    INTERNAL_ERROR = lRepository.getErrorCode(43);
    NOT_TRUE = lRepository.getErrorCode(44);
    NOT_FALSE = lRepository.getErrorCode(45);
    INVALID_EMAIL_ADDRESS = lRepository.getErrorCode(46);
    UNEXPECTED_ENUM_LITERAL = lRepository.getErrorCode(47);
    NO_DATATYPE_CONVERTER_DEFINED = lRepository.getErrorCode(48);
    DATATYPE_CONVERTER_ALREADY_EXISTS = lRepository.getErrorCode(49);
    DATATYPE_CONVERSION_NOT_POSSIBLE = lRepository.getErrorCode(50);
    MALFORMED_URL_PROPERTY = lRepository.getErrorCode(54);
    UNABLE_TO_LOAD_LOCALE_PROVIDER = lRepository.getErrorCode(55);
    MAX_BASE36_VALUE_EXCEEDED = lRepository.getErrorCode(56);
    INVALID_BASE_36_DIGIT = lRepository.getErrorCode(57);
    RESOURCE_NOT_FOUND = lRepository.getErrorCode(58);
    FILE_NOT_FOUND = lRepository.getErrorCode(59);
    UNABLE_TO_COMPARE_UNKNOWN_JAVA_RELEASES = lRepository.getErrorCode(63);
    // Handle all localized strings.
  }

  /**
   * Constructor is private to ensure that no instances of this class will be created.
   */
  private XFunMessages( ) {
    // Nothing to do.
  }
}