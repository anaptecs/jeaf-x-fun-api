/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.trace;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Locale.Category;
import java.util.Map;

import com.anaptecs.jeaf.xfun.annotations.TraceConfig;
import com.anaptecs.jeaf.xfun.annotations.TraceObjectFormatter;
import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.XFunRuntimeException;
import com.anaptecs.jeaf.xfun.api.config.AnnotationBasedConfiguration;
import com.anaptecs.jeaf.xfun.api.config.ConfigurationReader;
import com.anaptecs.jeaf.xfun.bootstrap.Check;

/**
 * Class is responsible to resolve the current configuration for tracing. As all JEAf configurations also trace
 * configuration is based on annotations ({@link TraceConfig} and {@link TraceObjectFormatter}).
 * 
 * All implementations of this interface must be able to be executed in a multi-threaded environment and must provide an
 * empty public default constructor.
 */
public final class TraceConfiguration extends AnnotationBasedConfiguration<TraceConfig> implements StartupInfoWriter {
  /**
   * Only instance of this class.
   */
  private static final TraceConfiguration INSTANCE = new TraceConfiguration();

  /**
   * Map contains all trace object formatters that were configured.
   */
  private final Map<Class<?>, ObjectFormatter<?>> objectFormatters = new HashMap<>();

  /**
   * Initialize object. During initialization all configuration parameters are read from the defined annotations.
   */
  private TraceConfiguration( ) {
    // Load all configuration parameters from annotations.
    this(TraceConfig.TRACE_CONFIG_RESOURCE_NAME, XFun.X_FUN_BASE_PATH, false);
  }

  /**
   * Initialize object. During initialization all configuration parameters are read from the defined annotations.
   * 
   * @param pCustomConfigurationResourceName Name of the file which contains the class name of the custom configuration
   * class. The parameter must not be null.
   * @param pBasePackagePath Path under which the file should be found in the classpath. The parameter may be null.
   * @param pExceptionOnError If parameter is set to true then an exception will be thrown in case of configuration
   * errors.
   */
  @SuppressWarnings("rawtypes")
  public TraceConfiguration( String pCustomConfigurationResourceName, String pCustomConfigurationBasePackagePath,
      boolean pExceptionOnError ) {

    // Class super class constructor.
    super(pCustomConfigurationResourceName, pCustomConfigurationBasePackagePath, pExceptionOnError);

    // Load custom trace object formatters.
    ConfigurationReader lReader = new ConfigurationReader();
    List<Class<? extends ObjectFormatter>> lObjectFormatters =
        lReader.readClassesFromConfigFile(this.objectFormattersResourcePath(), ObjectFormatter.class);

    // Create new instance of all object formatters.
    for (Class<? extends ObjectFormatter> lNextClass : lObjectFormatters) {
      try {
        TraceObjectFormatter lAnnotation = lNextClass.getAnnotation(TraceObjectFormatter.class);
        for (Class<?> lSupportedClass : lAnnotation.supportedClasses()) {
          objectFormatters.put(lSupportedClass, lNextClass.newInstance());
        }
      }
      catch (ReflectiveOperationException e) {
        throw new XFunRuntimeException("Unable to create new ObjectFormatter instance of class " + lNextClass.getName(),
            e);
      }
    }
  }

  /**
   * Method returns only instance of this class.
   * 
   * @return {@link TraceConfiguration} Only instance of this class. The method never returns null.
   */
  public static TraceConfiguration getInstance( ) {
    return INSTANCE;
  }

  @Override
  protected Class<TraceConfig> getAnnotationClass( ) {
    return TraceConfig.class;
  }

  @Override
  protected String getDefaultConfigurationClass( ) {
    return XFun.DEFAULT_CONFIGURATION_CLASS;
  }

  @Override
  public TraceConfig getEmptyConfiguration( ) {
    return new TraceConfig() {

      @Override
      public Class<? extends Annotation> annotationType( ) {
        return TraceConfig.class;
      }

      @Override
      public String defaultLoggerName( ) {
        return TraceConfig.DEFAULT_LOGGER_NAME;
      }

      @Override
      public boolean traceWithSystemLocale( ) {
        return TraceConfig.TRACE_WITH_SYSTEM_LOCALE;
      }

      @Override
      public String traceMessageFormat( ) {
        return TraceConfig.DEFAULT_FORMAT;
      }

      @Override
      public boolean indentTrace( ) {
        return TraceConfig.INDENT_TRACE;
      }

      @Override
      public int indentSize( ) {
        return TraceConfig.DEFAULT_INDENT_SIZE;
      }

      @Override
      public boolean exposeLoggersViaJMX( ) {
        return true;
      }

      @Override
      public String customTraceLocale( ) {
        return TraceConfig.CUSTOM_TRACE_LOCALE;
      }

      @Override
      public boolean showCurrentUserInTraces( ) {
        return TraceConfig.SHOW_CURRENT_USER_IN_TRACES;
      }

      @Override
      public boolean useApplicationIDAsPrefix( ) {
        return TraceConfig.USE_APPLICATION_ID_AS_PREFIX;
      }

      @Override
      public String objectFormattersResourcePath( ) {
        return XFun.X_FUN_BASE_PATH + "/" + TraceConfig.TRACE_CONFIG_RESOURCE_NAME;
      }
    };
  }

  /**
   * Method checks the passed custom configuration. During check we check all params of the configuration as we assume
   * that there is not default configuration.
   * 
   * @param pCustomConfiguration
   * @return String Describing the found problems in the passed custom configuration or null if everything is fine.
   */
  @Override
  public List<String> checkCustomConfiguration( TraceConfig pCustomConfiguration ) {
    // As the configuration just defines classes of interface implementations we just have to ensure that
    // the defined classes are real classes and not just interfaces.
    List<String> lConfigErrors = new ArrayList<>(0);

    // Check indent size
    if (pCustomConfiguration.indentSize() < 0) {
      lConfigErrors.add("Trace indentation size must be zero or greater.");
    }

    // Check custom trace locale
    if (pCustomConfiguration.traceWithSystemLocale() == false) {
      String lLocaleString = pCustomConfiguration.customTraceLocale();
      Locale lLocale = Locale.forLanguageTag(lLocaleString);
      if (lLocale.getLanguage().isEmpty()) {
        lConfigErrors.add("Configuration contains invalid locale '" + lLocaleString + "' for tracing.");
      }
    }

    // Return result of configuration check.
    return lConfigErrors;
  }

  /**
   * @see TraceConfig#defaultLoggerName()
   */
  public String getDefaultLoggerName( ) {
    return theConfig.defaultLoggerName();
  }

  /**
   * @see TraceConfig#traceMessageFormat()
   */
  public String getTraceMessageFormat( ) {
    return theConfig.traceMessageFormat();
  }

  /**
   * @see TraceConfig#indentTrace()
   */
  public boolean isTraceIndentationEnabled( ) {
    return theConfig.indentTrace();
  }

  /**
   * @see TraceConfig#indentSize()
   */
  public int getIndentSize( ) {
    return theConfig.indentSize();
  }

  /**
   * @see TraceConfig#exposeLoggersViaJMX()
   */
  public boolean exposeLoggersViaJMX( ) {
    return theConfig.exposeLoggersViaJMX();
  }

  /**
   * @see TraceConfig#traceWithSystemLocale()
   */
  public boolean isTraceWithSystemLocaleEnabled( ) {
    return theConfig.traceWithSystemLocale();
  }

  /**
   * @see TraceConfig#customTraceLocale()
   */
  public Locale getCustomTraceLocale( ) {
    String lLocaleString = theConfig.customTraceLocale();

    // Convert string into real locale object.
    Locale lLocale = Locale.forLanguageTag(lLocaleString);

    // Ensure that locale really exists. If not we will switch to default locale
    if (lLocale.getLanguage().isEmpty() == true) {
      lLocale = Locale.getDefault(Category.DISPLAY);
    }
    return lLocale;
  }

  /**
   * @see TraceConfig#showCurrentUserInTraces()
   */
  public boolean showCurrentUserInTraces( ) {
    return theConfig.showCurrentUserInTraces();
  }

  /**
   * @see TraceConfig#useApplicationIDAsPrefix()
   */
  public boolean useApplicationIDAsPrefix( ) {
    return theConfig.useApplicationIDAsPrefix();
  }

  /**
   * @see TraceConfig#objectFormattersResourcePath()
   */
  public String objectFormattersResourcePath( ) {
    return theConfig.objectFormattersResourcePath();
  }

  /**
   * Method returns the object formatter for the passed class. If no formatter is defined for the class itself then we
   * recursively check for its super classes.
   * 
   * @param pType Class for which an object formatter should be returned. The parameter must not be null.
   * @return {@link ObjectFormatter} for the passed class or null if none could be found.
   */
  @SuppressWarnings("rawtypes")
  public ObjectFormatter getObjectFormatter( Class<?> pType ) {
    // Check parameter
    Check.checkInvalidParameterNull(pType, "pType");

    ObjectFormatter<?> lFormatter = objectFormatters.get(pType);
    // No formatter found for passed class. May be we found one for its base class.
    if (lFormatter == null) {
      // Resolve super class
      Class<?> lSuperclass = pType.getSuperclass();
      if (lSuperclass != null) {
        lFormatter = this.getObjectFormatter(lSuperclass);
      }
      // We are at the very top of the class hierarchy
      else {
        lFormatter = null;
      }
    }
    return lFormatter;
  }

  @Override
  public Class<?> getStartupCompletedEventSource( ) {
    return XFun.class;
  }

  @Override
  public void traceStartupInfo( Trace pTrace, TraceLevel pTraceLevel ) {
    pTrace.writeInitInfo("JEAF X-Fun uses the following trace configuration:", pTraceLevel);
    pTrace.info("Default Logger name:      " + this.getDefaultLoggerName());
    pTrace.info("Indentation enabled:      " + this.isTraceIndentationEnabled());
    pTrace.info("Trace message format:     " + this.getTraceMessageFormat());
    pTrace.info("Trace indentation size:   " + this.getIndentSize());
    pTrace.info("Trace with system locale: " + this.isTraceWithSystemLocaleEnabled());
    pTrace.info("Show current user:        " + this.showCurrentUserInTraces());
    pTrace.info("Custom trace locale:      " + this.getCustomTraceLocale());
  }
}
