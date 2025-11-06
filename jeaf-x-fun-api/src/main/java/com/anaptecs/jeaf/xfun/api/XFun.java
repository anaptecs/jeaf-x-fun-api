/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.anaptecs.jeaf.xfun.annotations.XFunConfig;
import com.anaptecs.jeaf.xfun.api.checks.Verifier;
import com.anaptecs.jeaf.xfun.api.checks.VerifierFactory;
import com.anaptecs.jeaf.xfun.api.config.ConfigurationProvider;
import com.anaptecs.jeaf.xfun.api.config.ConfigurationProviderFactory;
import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverterRegistry;
import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverterRegistryFactory;
import com.anaptecs.jeaf.xfun.api.info.ApplicationInfo;
import com.anaptecs.jeaf.xfun.api.info.InfoProvider;
import com.anaptecs.jeaf.xfun.api.info.InfoProviderFactory;
import com.anaptecs.jeaf.xfun.api.info.VersionInfo;
import com.anaptecs.jeaf.xfun.api.locale.LocaleProvider;
import com.anaptecs.jeaf.xfun.api.locale.LocaleProviderFactory;
import com.anaptecs.jeaf.xfun.api.messages.MessageRepository;
import com.anaptecs.jeaf.xfun.api.messages.MessageRepositoryFactory;
import com.anaptecs.jeaf.xfun.api.principal.PrincipalProvider;
import com.anaptecs.jeaf.xfun.api.principal.PrincipalProviderFactory;
import com.anaptecs.jeaf.xfun.api.trace.StartupInfoConfiguration;
import com.anaptecs.jeaf.xfun.api.trace.StartupInfoEventCollector;
import com.anaptecs.jeaf.xfun.api.trace.StartupInfoEventHandler;
import com.anaptecs.jeaf.xfun.api.trace.StartupInfoWriter;
import com.anaptecs.jeaf.xfun.api.trace.Trace;
import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;
import com.anaptecs.jeaf.xfun.api.trace.TraceProvider;
import com.anaptecs.jeaf.xfun.api.trace.TraceProviderFactory;
import com.anaptecs.jeaf.xfun.fallback.checks.FallbackVerifierImpl;
import com.anaptecs.jeaf.xfun.fallback.trace.FallbackTraceProviderFactoryImpl;
import com.anaptecs.jeaf.xfun.fallback.trace.FallbackTraceProviderImpl;

public final class XFun implements StartupInfoWriter, StartupInfoEventHandler {
  /**
   * Name of the base path under which all X-Fun configuration files are located.
   */
  public static final String X_FUN_BASE_PATH = "META-INF/JEAF/XFun";

  /**
   * Name of the class that contains the default configuration annotation.
   */
  public static final String DEFAULT_CONFIGURATION_CLASS = "com.anaptecs.jeaf.xfun.impl.DefaultXFunConfiguration";

  /**
   * Constant for the name of the system property that can be used to overwrite the default resource name
   * {@link XFunConfig#X_FUN_CONFIG_RESOURCE_NAME}
   */
  public static final String X_FUN_CONFIG_RESOURCE_NAME = "xfun.config.resourceName";

  /**
   * Constant for the name of the system property that can be used to overwrite the default resource path
   * {@link XFun#X_FUN_BASE_PATH}
   */
  public static final String X_FUN_CONFIG_RESOURCE_PATH = "xfun.config.resourcePath";

  /**
   * Constant for the system property that can be used to overwrite default exception handling when loading JEAF
   * configuration. By default no exception will be thrown.
   */
  public static final String X_FUN_CONFIG_EXCEPTION_ON_ERROR = "xfun.config.execptionOnError";

  /**
   * Fallback verifier is used at least during startup as at that time configured implementation may not be available
   * already.
   */
  private static final Verifier FALLBACK_VERIFIER = new FallbackVerifierImpl();

  /**
   * Constant for format string that is used when tracing names of used implementations.
   */
  public static final String IMPL_INFO_FORMAT_STRING = "    %1$-40s%2$s";

  /**
   * Current Instance of this class. Existing instances can be replaced by calling
   */
  private static XFun instance;

  /**
   * Attribute marks if tracing is initialized or not. Attribute must only be used in cases where {@link XFun#instance}
   * is null.
   */
  private static boolean tracingInitialized = false;

  /**
   * Lock object to avoid multiple initializations of tracing in multi-threaded cases.
   */
  private static final Object TRACE_INIT_LOCK = new Object();

  /**
   * Timestamp when the XFun instance was created.
   */
  private final long startTimeStamp;

  /**
   * Reference to used X-Fun configuration.
   */
  private final XFunConfiguration configuration;

  /**
   * Reference to configured configuration provider.
   */
  private final ConfigurationProviderFactory configurationProviderFactory;

  /**
   * Reference to configured message repository factory.
   */
  private final MessageRepositoryFactory messageRepositoryFactory;

  /**
   * Reference to configured verifier factory.
   */
  private final VerifierFactory verifierFactory;

  /**
   * Reference to configured locale provider factory.
   */
  private final LocaleProviderFactory localeProviderFactory;

  /**
   * Reference to configured principal provider factory.
   */
  private final PrincipalProviderFactory principalProviderFactory;

  /**
   * Reference to configured info provider factory.
   */
  private final InfoProviderFactory infoProviderFactory;

  /**
   * Reference to configured datatype converter registry factory.
   */
  private final DatatypeConverterRegistryFactory datatypeConverterRegistryFactory;

  /**
   * Reference to configured trace provider factory. In order to avoid problems during startup it will be lazy loaded.
   */
  private TraceProviderFactory traceProviderFactory;

  /**
   * Reference to configuration for startup info.
   */
  private final StartupInfoConfiguration startupInfoConfiguration;

  /**
   * Map contains all {@link StartupInfoWriter} that are configured.
   */
  private final Map<Class<?>, List<StartupInfoWriter>> startupInfoWriters = new HashMap<>();

  /**
   * Method resolved the configuration resource name that contains the JEAF configuration. This name can be configured
   * using the system property {@value #X_FUN_CONFIG_RESOURCE_NAME}. If the system property is not set then the default
   * configuration file (see {@link XFunConfig#X_FUN_CONFIG_RESOURCE_NAME}) will be used.
   * 
   * @return {@link String} Name of the configuration file. The method never returns null.
   */
  private static String getConfigurationResourceName( ) {
    return System.getProperty(X_FUN_CONFIG_RESOURCE_NAME, XFunConfig.X_FUN_CONFIG_RESOURCE_NAME);
  }

  /**
   * Method resolved the configuration path that contains the JEAF configuration. This path can be configured using the
   * system property {@value #X_FUN_CONFIG_RESOURCE_PATH}. If the system property is not set then the default
   * configuration file (see {@link XFun#X_FUN_BASE_PATH}) will be used.
   * 
   * @return {@link String} Path of the configuration file. The method never returns null.
   */
  private static String getConfigurationBasePackagePath( ) {
    return System.getProperty(X_FUN_CONFIG_RESOURCE_PATH, XFun.X_FUN_BASE_PATH);
  }

  /**
   * Method determines if an exception should be thrown in case of configuration problems. This value can be configured
   * through system property {@value #X_FUN_CONFIG_EXCEPTION_ON_ERROR}. By default JEAF tries to continue loading
   * without throwing an exception.
   * 
   * @return boolean Method returns <code>true</code> if an exception should be thrown and <code>false</code> otherwise.
   */
  private static boolean getExceptionOnError( ) {
    return Boolean.getBoolean(X_FUN_CONFIG_EXCEPTION_ON_ERROR);
  }

  /**
   * Initialize single instance of this class.
   */
  static {
    try {
      instance = new XFun();
      StartupInfoEventCollector.startupCompleted(XFun.class);
      StartupInfoEventCollector.registerEventHandler(instance);
    }
    catch (RuntimeException e) {
      Trace lTrace = FallbackTraceProviderImpl.EMERGENCY_TRACE;
      lTrace.fatal(
          "Caught exception during JEAF X-Fun initialization. Most likely this is caused by some missing configuration file or problems with your classpath.");
      lTrace.fatal(e);
      throw e;
    }
  }

  /**
   * Initialize object. Which configuration class will be used can be defined through the system properties
   * {@value #X_FUN_CONFIG_RESOURCE_NAME}, {@value #X_FUN_CONFIG_RESOURCE_PATH} and
   * {@value #X_FUN_CONFIG_EXCEPTION_ON_ERROR}. If one of the parameter or all of them are not defined then their
   * default values will be used.
   * 
   * If nothing special is defined then the X-Fun configuration will be read from the class that is defined in file
   * {@link XFunConfig#XFUN_CONFIG_PATH}
   */
  private XFun( ) {
    // Start initialization
    startTimeStamp = System.currentTimeMillis();
    Trace lTrace = FallbackTraceProviderImpl.EMERGENCY_TRACE;
    lTrace.info("Starting JEAF X-Fun initialization.");

    // Trace classpath
    String lClasspath = System.getProperty("java.class.path");

    // Depending on the operating system classpath entries havew different separators
    String lPathSeparator = System.getProperty("path.separator");
    String[] lClasspathEntries = lClasspath.split(lPathSeparator);
    lTrace.info("Classpath:");
    for (String lNextEntry : lClasspathEntries) {
      lTrace.info("    " + lNextEntry);

    }

    // Trace OS information
    String lOSInfo = "Operating System: " + System.getProperty("os.name") + " (Version: "
        + System.getProperty("os.version") + ", Architecture: " + System.getProperty("os.arch") + ")";
    lTrace.info(lOSInfo);

    // Trace JVM information.
    String lJVMInfo = "Java Virtual Machine: " + System.getProperty("java.runtime.name") + " "
        + System.getProperty("java.version") + " (Vendor: " + System.getProperty("java.vm.vendor") + ")";
    lTrace.info(lJVMInfo);

    // Trace runtime information
    Runtime lRuntime = Runtime.getRuntime();
    int lProcessors = lRuntime.availableProcessors();
    long lFreeMemory = lRuntime.freeMemory() / (1024 * 1024);
    long lMaxMemory = lRuntime.maxMemory() / (1024 * 1024);
    long lTotalMemory = lRuntime.totalMemory() / (1024 * 1024);
    String lRuntimeInfo = "Runtime Environment: " + lProcessors + " Cores, JVM Memory: " + lTotalMemory
        + " MB available, " + lMaxMemory + " MB maximum, " + lFreeMemory + " MB free";

    lTrace.info(lRuntimeInfo);

    // Resolve XFun configuration.
    String lConfigurationResourceName = XFun.getConfigurationResourceName();
    String lConfigurationBasePackagePath = XFun.getConfigurationBasePackagePath();
    boolean lExceptionOnError = XFun.getExceptionOnError();
    configuration = new XFunConfiguration(lConfigurationResourceName, lConfigurationBasePackagePath, lExceptionOnError);

    // Load all configured factory classes except for tracing.
    messageRepositoryFactory = configuration.getMessageRepositoryFactory();
    configurationProviderFactory = configuration.getConfigurationProviderFactory();
    verifierFactory = configuration.getVerifierFactory();
    localeProviderFactory = configuration.getLocaleProviderFactory();
    principalProviderFactory = configuration.getPrincipalProviderFactory();
    infoProviderFactory = configuration.getInfoProviderFactory();
    datatypeConverterRegistryFactory = configuration.getDatatypeConverterRegistryFactory();

    // Load startup info writers
    startupInfoConfiguration = new StartupInfoConfiguration();
    List<StartupInfoWriter> lStartupInfoWriters = startupInfoConfiguration.getStartupInfoWriters();
    lStartupInfoWriters.add(0, this);
    for (StartupInfoWriter lStartupInfoWriter : lStartupInfoWriters) {
      Class<?> lEventSource = lStartupInfoWriter.getStartupCompletedEventSource();
      List<StartupInfoWriter> lWritersList = startupInfoWriters.computeIfAbsent(lEventSource, p -> new ArrayList<>());
      lWritersList.add(lStartupInfoWriter);
    }

    // Trace provider factory will be loaded lazy to avoid trouble during startup.
  }

  /**
   * Method returns only instance of this class.
   * 
   * @return {@link XFun} Instance of this class. The method never returns null.
   */
  private static final XFun getInstance( ) {
    return instance;
  }

  /**
   * Method returns the configured message repository.
   * 
   * @return {@link MessageRepository} Message repository that is used. The method never returns null.
   */
  public static MessageRepository getMessageRepository( ) {
    return XFun.getInstance().messageRepositoryFactory.getMessageRepository();
  }

  /**
   * Method returns the configured verifier.
   * 
   * @return {@link Verifier} Verifier that is used. The method never returns null.
   */
  public static Verifier getVerifier( ) {
    // During initialization there may be cases where an instance of XFun does not exist yet.
    XFun lXFunInstance = XFun.getInstance();
    Verifier lVerifier;
    if (lXFunInstance != null) {
      lVerifier = lXFunInstance.verifierFactory.getVerifier();
      if (lVerifier == null) {
        lVerifier = FALLBACK_VERIFIER;
      }
    }
    // During initialization we will also use our fallback verifier.
    else {
      lVerifier = FALLBACK_VERIFIER;
    }
    return lVerifier;
  }

  /**
   * Method returns the configured configuration provider.
   * 
   * @return {@link ConfigurationProvider} Configuration provider that is used. The method never returns null.
   */
  public static ConfigurationProvider getConfigurationProvider( ) {
    return XFun.getInstance().configurationProviderFactory.getConfigurationProvider();
  }

  /**
   * Method returns the configured locale provider.
   * 
   * @return {@link LocaleProvider} Locale provider that is used. The method never returns null.
   */
  public static LocaleProvider getLocaleProvider( ) {
    return XFun.getInstance().localeProviderFactory.getLocaleProvider();
  }

  /**
   * Method returns the configured principal provider.
   * 
   * @return {@link PrincipalProvider} Principal provider that is used. The method never returns null.
   */
  public static PrincipalProvider getPrincipalProvider( ) {
    return XFun.getInstance().principalProviderFactory.getPrincipalProvider();
  }

  /**
   * Method returns the configured info provider.
   * 
   * @return {@link InfoProvider} Info provider that is used. The method never returns null.
   */
  public static InfoProvider getInfoProvider( ) {
    return XFun.getInstance().infoProviderFactory.getInfoProvider();
  }

  /**
   * Method returns version info of the current application. If no version info is available the a special unknown
   * version is returned.
   * 
   * @return {@link VersionInfo} Version info of the application. The method never returns null.
   */
  public static VersionInfo getVersionInfo( ) {
    return XFun.getInfoProvider().getApplicationInfo().getVersion();
  }

  /**
   * Method returns the configured trace provider.
   * 
   * @return {@link TraceProvider} Trace provider that is used. The method never returns null.
   */
  public static TraceProvider getTraceProvider( ) {
    // In order to avoid problems during initialization we load the trace factory only on request.
    TraceProvider lTraceProvider;
    if (instance == null) {
      // We are still during initialization so we better use our fallback trace provider
      lTraceProvider = new FallbackTraceProviderFactoryImpl().getTraceProvider();
    }
    // Tracing is not yet initialized.
    else if (instance.traceProviderFactory == null) {
      // Avoid race conditions due to multiple trace initializations.
      synchronized (TRACE_INIT_LOCK) {
        // Ensure that tracing is initialized only once. During initialization of tracing it may happen that tracing is
        // already requested from within the same thread.
        if (tracingInitialized == false) {
          // Mark at first that we are initializing
          tracingInitialized = true;

          // Use configured factory to resolve the trace provider that should be used.
          TraceProviderFactory lTraceProviderFactory = instance.configuration.getTraceProviderFactory();

          // Trace provider factory could be loaded.
          if (lTraceProviderFactory != null) {
            instance.traceProviderFactory = lTraceProviderFactory;
          }
          // In case of configuration problems we will not be able to resolve a trace provider factory. In this case we
          // will fallback to the fallback trace provider
          else {
            instance.traceProviderFactory = new FallbackTraceProviderFactoryImpl();
            FallbackTraceProviderImpl.EMERGENCY_TRACE.error(
                "Configuration problems prevent that the configured trace provider can be used. Using fallback tracing instead.");
          }
          // Resolve trace provider that will be used.
          lTraceProvider = instance.traceProviderFactory.getTraceProvider();
        }
        // As we want to avoid problems due to multiple initializations of trace providers. This may happen in cases
        // that during initialization of tracing traces should be written.
        else {
          lTraceProvider = new FallbackTraceProviderFactoryImpl().getTraceProvider();
        }
      }
    }
    // Trace initialization is fully completed
    else {
      lTraceProvider = instance.traceProviderFactory.getTraceProvider();
    }
    return lTraceProvider;
  }

  /**
   * Method returns the configured datatype converter registry.
   * 
   * @return DatatypeConverterRegistry Registry that is used. The method never returns null.
   */
  public static DatatypeConverterRegistry getDatatypeConverterRegistry( ) {
    return XFun.getInstance().datatypeConverterRegistryFactory.getDatatypeConverterRegistry();
  }

  /**
   * Methods returns the trace object that should be used in the current context.
   * 
   * <b>Remark: </b>As the trace object depends on the current context it's not recommended to cache the result of this
   * call.
   * 
   * @return {@link Trace} Trace object for the current context. The method never returns null.
   */
  public static Trace getTrace( ) {
    return XFun.getTraceProvider().getCurrentTrace();
  }

  /**
   * Method returns the X-Fun configuration that is currently used.
   * 
   * @return {@link XFunConfiguration} Configuration that is currently used. The method never returns null.
   */
  public static XFunConfiguration getConfiguration( ) {
    return instance.configuration;
  }

  /**
   * Method resets the current X-Fun instance. This means that JEAF X-Fun will be reinitialized completely. This method
   * is mainly intended to be used for test purposes.
   */
  public static void reload( ) {
    // Create new X-Fun instance add trace startup info.
    XFun.getTrace().warn("Reloading JEAF X-Fun");
    XFun lNewInstance = new XFun();
    lNewInstance.traceStartupInfo(XFun.class);

    // Replace existing instance with new one.
    XFun.getTrace().warn("Existing instance of JEAF X-Fun replaced by new one.");
    instance = lNewInstance;
  }

  @Override
  public void handleStartupInfoEvent( Class<?> pClass ) {
    this.traceStartupInfo(pClass);
  }

  /**
   * Method traces startup info about the current X-Fun configuration.
   */
  private void traceStartupInfo( Class<?> pClass ) {
    boolean lTraceStartupInfo = startupInfoConfiguration.traceStartupInfo();
    if (lTraceStartupInfo == true) {
      Trace lTrace;
      try {
        lTrace = XFun.getTraceProvider().getCurrentTrace();
      }
      catch (Throwable e) {
        lTrace = FallbackTraceProviderImpl.EMERGENCY_TRACE;
        lTrace.writeInitInfo(
            "Unable to load configured trace provider. Using fallback to emergency trace implementation. "
                + e.getMessage(),
            TraceLevel.ERROR);
        lTrace.error(e);
      }
      if (lTrace.isInfoEnabled()) {
        // Try to resolve application info

        // Use configured trace level.
        TraceLevel lTraceLevel = startupInfoConfiguration.getStartupInfoTraceLevel();

        // Call all configured startup info writers.
        List<StartupInfoWriter> lStartupInfoWriters = startupInfoWriters.get(pClass);
        if (lStartupInfoWriters != null) {
          for (StartupInfoWriter lWriter : lStartupInfoWriters) {
            lWriter.traceStartupInfo(lTrace, lTraceLevel);
          }
        }
      }
    }
  }

  @Override
  public Class<?> getStartupCompletedEventSource( ) {
    return XFun.class;
  }

  @Override
  public void traceStartupInfo( Trace pTrace, TraceLevel pTraceLevel ) {
    // Trace application info
    ApplicationInfo lApplicationInfo;
    try {
      lApplicationInfo = XFun.getInfoProvider().getApplicationInfo();
      pTrace.writeInitInfo(lApplicationInfo.toString(), pTraceLevel);
    }
    catch (Throwable e) {
      lApplicationInfo = ApplicationInfo.UNKNOWN_APPLICATION;
      pTrace.writeInitInfo(lApplicationInfo.toString(), pTraceLevel.getLevelWithHigherPriority(TraceLevel.WARN));
    }

    // Trace version info.
    VersionInfo lVersion = lApplicationInfo.getVersion();
    if (lVersion.isUnknownVersion() == true) {
      pTrace.writeInitInfo("Using generic version info as fallback. Please see log above for further details.",
          pTraceLevel.getLevelWithHigherPriority(TraceLevel.WARN));
    }

    // Trace information about used implementations.
    pTrace.writeInitInfo("JEAF X-Fun configuration read from class: " + configuration.getConfigurationClass().getName(),
        pTraceLevel);
    pTrace.writeInitInfo("JEAF X-Fun uses the following implementations:", pTraceLevel);

    // Configuration provider
    String lConfigurationProvider =
        configurationProviderFactory != null ? configurationProviderFactory.getClass().getName() : "null";
    pTrace.writeInitInfo(String.format(IMPL_INFO_FORMAT_STRING,
        ConfigurationProviderFactory.class.getSimpleName() + ":", lConfigurationProvider), pTraceLevel);

    // Message repository
    String lMessageRepositoryFactory =
        messageRepositoryFactory != null ? messageRepositoryFactory.getClass().getName() : "null";
    pTrace.writeInitInfo(String.format(IMPL_INFO_FORMAT_STRING, MessageRepositoryFactory.class.getSimpleName() + ":",
        lMessageRepositoryFactory), pTraceLevel);

    // Verifier
    String lVerifierFactory = verifierFactory != null ? verifierFactory.getClass().getName() : "null";
    pTrace.writeInitInfo(
        String.format(IMPL_INFO_FORMAT_STRING, VerifierFactory.class.getSimpleName() + ":", lVerifierFactory),
        pTraceLevel);

    // Locale provider
    String lLocaleProviderFactory = localeProviderFactory != null ? localeProviderFactory.getClass().getName() : "null";
    pTrace.writeInitInfo(String.format(IMPL_INFO_FORMAT_STRING, LocaleProviderFactory.class.getSimpleName() + ":",
        lLocaleProviderFactory), pTraceLevel);

    // Principal provider
    String lPrincipalProviderFactory =
        principalProviderFactory != null ? principalProviderFactory.getClass().getName() : "null";
    pTrace.writeInitInfo(String.format(IMPL_INFO_FORMAT_STRING, PrincipalProviderFactory.class.getSimpleName() + ":",
        lPrincipalProviderFactory), pTraceLevel);

    // Info provider
    String lInfoProviderFactory = infoProviderFactory != null ? infoProviderFactory.getClass().getName() : "null";
    pTrace.writeInitInfo(
        String.format(IMPL_INFO_FORMAT_STRING, InfoProviderFactory.class.getSimpleName() + ":", lInfoProviderFactory),
        pTraceLevel);

    // Trace provider
    String lTraceProviderFactory = traceProviderFactory != null ? traceProviderFactory.getClass().getName()
        : "not yet loaded due to lazy initialization";
    pTrace.writeInitInfo(
        String.format(IMPL_INFO_FORMAT_STRING, TraceProviderFactory.class.getSimpleName() + ":", lTraceProviderFactory),
        pTraceLevel);

    // Datatype converter
    String lDatatypeConverterRegistryFactory =
        datatypeConverterRegistryFactory != null ? datatypeConverterRegistryFactory.getClass().getName() : "null";
    pTrace.writeInitInfo(String.format(IMPL_INFO_FORMAT_STRING,
        DatatypeConverterRegistryFactory.class.getSimpleName() + ":", lDatatypeConverterRegistryFactory), pTraceLevel);

    // Trace info about end of initialization.
    long lNow = System.currentTimeMillis();
    StringBuilder lBuilder = new StringBuilder();
    lBuilder.append("JEAF X-Fun initialization completed in ");
    lBuilder.append(lNow - startTimeStamp);
    lBuilder.append("ms.");
    pTrace.writeInitInfo(lBuilder.toString(), TraceLevel.INFO);
  }
}
