/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.config;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.XFunRuntimeException;
import com.anaptecs.jeaf.xfun.api.trace.Trace;
import com.anaptecs.jeaf.xfun.bootstrap.Assert;
import com.anaptecs.jeaf.xfun.fallback.trace.FallbackTraceProviderImpl;

/**
 * Class implements a generic configuration provider that can be used to easily implement an configuration provider
 * based on annotations.
 */
public abstract class AnnotationBasedConfiguration<T extends Annotation> {
  /**
   * Attribute controls behavior in case of exceptions. <code>false</code> means that when ever possible throwing an
   * exception is suppressed.
   */
  protected final boolean exceptionOnError;

  /**
   * Reference to default configuration. When ever the custom configuration does not contain any values then the default
   * configuration will be used.
   */
  protected final T defaultConfig;

  /**
   * Attribute defines if the default configuration is available.
   */
  protected final boolean isDefaultConfigAvailable;

  /**
   * Reference to custom configuration.
   */
  protected final T customConfig;

  /**
   * Reference to configuration that holds the values. This can either be the custom or the default configuration. The
   * reference is never null.
   */
  protected final T theConfig;

  /**
   * Attribute defines if a custom configuration is available.
   */
  protected final boolean isCustomConfigAvailable;

  /**
   * Name of the class that provided the configuration that is used. This is either the default configuration class or
   * some custom configuration class.
   */
  private Class<?> configurationClass;

  /**
   * List with all configuration errors that were detected when the configuration was loaded.
   */
  private final List<String> configErrors;

  /**
   * Initialize object. During initialization configuration will be loaded.
   * 
   * @param pCustomConfigurationResourceName Name of the file which contains the class name of the custom configuration
   * class. The parameter must not be null.
   * @param pCustomConfigurationBasePackagePath Path under which the file should be found in the classpath. The
   * parameter may be null.
   * @param pExceptionOnError If parameter is set to true then an exception will be thrown in case of configuration
   * errors.
   */
  protected AnnotationBasedConfiguration( String pCustomConfigurationResourceName,
      String pCustomConfigurationBasePackagePath, boolean pExceptionOnError ) {
    // It is required that the defined annotation has retention type runtime. Otherwise the configuration parameters
    // that are defined through the annotation are not visible at runtime.
    Class<T> lAnnotationClass = this.getAnnotationClass();
    Retention lRetentionAnnotation = lAnnotationClass.getAnnotation(Retention.class);
    RetentionPolicy lRetentionPolicy;
    if (lRetentionAnnotation != null) {
      lRetentionPolicy = lRetentionAnnotation.value();
    }
    else {
      // As retention policy is not defined we fall back to default.
      lRetentionPolicy = RetentionPolicy.CLASS;
    }

    // Annotation that is used for configuration has the required retention policy, so we can just proceed.
    if (lRetentionPolicy == RetentionPolicy.RUNTIME) {
      // Configure exception handling
      exceptionOnError = pExceptionOnError;

      // Load default configuration
      defaultConfig = this.loadDefaultConfiguration();
      isDefaultConfigAvailable = this.isDefaultConfigurationAvailable();

      // Load custom configuration
      customConfig =
          this.loadCustomConfiguration(pCustomConfigurationResourceName, pCustomConfigurationBasePackagePath);
      isCustomConfigAvailable = this.isCustomConfigurationAvailable();

      // Try to detect configuration errors
      configErrors =
          this.detectConfigurationErrors(pCustomConfigurationBasePackagePath + "/" + pCustomConfigurationResourceName);

      // Make custom config to the one that should be used.
      if (isCustomConfigAvailable == true) {
        theConfig = customConfig;
      }
      // Make default config to the one that should be used.
      else {
        theConfig = defaultConfig;
      }
    }
    // The annotation type that is used for configuration does not have retention policy RUNTIME
    else {
      String lMessage = "The annotation " + lAnnotationClass.getName()
          + " does not have retention RUNTIME. Please correction the annotation by adding '@Retention(RetentionPolicy.RUNTIME)'.";
      FallbackTraceProviderImpl.EMERGENCY_TRACE.error(lMessage);
      throw new XFunRuntimeException(lMessage);
    }
  }

  /**
   * Method tries to detect configuration errors.
   * 
   * @param pCustomConfigurationResourceName Name of the resource from which the configuration was read. The parameter
   * may be null.
   * @param pCustomConfigurationBasePackagePath Path under which the file should be found in the classpath. The
   * parameter may be null.
   * @return {@link List} List with a description of all configuration errors that were detected. The method never
   * returns null.
   */
  private List<String> detectConfigurationErrors( String pCustomConfigurationResourceName ) {
    // Start check of configuration
    List<String> lConfigErrors;
    if (isDefaultConfigAvailable == false && isCustomConfigAvailable == false) {
      String lMessage = "Configuration error. Default configuration class '" + this.getDefaultConfigurationClass()
          + "' is not in classpath AND no custom configuration could be loaded from resource file '"
          + pCustomConfigurationResourceName + "'. Please check your custom configuration file and your classpath.";
      throw new XFunRuntimeException(lMessage);
    }
    // Check custom configuration only in case that there is no default configuration. It's an explicit feature the a
    // custom configuration is allowed to define only some values.
    else if (isCustomConfigAvailable == true && isDefaultConfigAvailable == false) {
      // We need to check that custom configuration contains only valid values.
      lConfigErrors = this.checkCustomConfiguration();
    }
    // We only have our default configuration, so no special checks are required as we believe in the overall
    // correctness of our default configuration ;-)
    else {
      lConfigErrors = Collections.emptyList();
    }
    return lConfigErrors;
  }

  /**
   * Method checks if a custom configuration is correct.
   * 
   * @return {@link List} List with a description of all configuration errors that were detected. The method never
   * returns null.
   */
  private List<String> checkCustomConfiguration( ) {
    List<String> lConfigErrors;
    lConfigErrors = this.checkCustomConfiguration(customConfig);
    this.traceConfigurationProblems(lConfigErrors);

    // Abort loading process if it was requested.
    if (exceptionOnError == true) {
      if (lConfigErrors != null && lConfigErrors.isEmpty() == false) {
        String lMessage = "Found " + lConfigErrors.size()
            + " error(s) during analysis of configuration. Please see error log for further details.";
        throw new XFunRuntimeException(lMessage);
      }
      else {
        lConfigErrors = Collections.emptyList();
      }
    }
    // Even if configuration errors were found we should continue
    else {
      // There are errors in the configuration
      if (lConfigErrors != null && lConfigErrors.isEmpty() == false) {
        lConfigErrors = Collections.unmodifiableList(lConfigErrors);
      }
      // No configuration errors detected.
      else {
        lConfigErrors = Collections.emptyList();
      }
    }
    return lConfigErrors;
  }

  /**
   * Method traces the passed configuration problems.
   * 
   * @param pConfigfurationProblems List with all configuration problems that were found. The list may be null or empty.
   * In this case it's assumed that there are no configuration problems.
   */
  private void traceConfigurationProblems( List<String> pConfigfurationProblems ) {
    if (pConfigfurationProblems != null && pConfigfurationProblems.isEmpty() == false) {
      Trace lTrace = XFun.getTrace();
      lTrace.error("Found " + pConfigfurationProblems.size()
          + " configuration problems during analysis of configuration provided through class "
          + configurationClass.getName() + ":");

      for (String lMessage : pConfigfurationProblems) {
        lTrace.error(lMessage);
      }
    }
  }

  /**
   * Method returns the class object of the annotation that holds the configuration data. This method is only needed due
   * to limitations of Java generics ;-(
   * 
   * @return {@link Class} Class object of the annotation. The method must not return null.
   */
  protected abstract Class<T> getAnnotationClass( );

  /**
   * Method returns the name of the class that contains the default configuration. In order to avoid direct compile time
   * dependencies to this class only its name should be returned.
   * 
   * @return Name of the class that hold the annotation with the default values. The method may return null in case that
   * there is no meaningful default.
   */
  protected abstract String getDefaultConfigurationClass( );

  /**
   * Method should return an empty custom configuration. Once again we have to fight with limitation in Java generics.
   * In order to simplify the further processing this method has to be implemented.
   * 
   * @return T Empty custom configuration. The method must not return null.
   */
  protected abstract T getEmptyConfiguration( );

  /**
   * Method needs to be implemented by subclasses in order to check the custom configuration.
   * 
   * @param pCustomConfiguration Custom configuration that should be checked. The parameter is never null.
   * @return {@link List} List of Strings describing the found problems during the check. For each found problem one
   * entry should be provided inside the list. If no problems were found the the method is supposed to return null or an
   * empty list.
   */
  protected abstract List<String> checkCustomConfiguration( T pCustomConfiguration );

  /**
   * Method loads default configuration and returns it.
   * 
   * @return T Loaded default configuration. The method method never returns null.
   */
  private T loadDefaultConfiguration( ) {
    // Load default configuration values. In order to avoid ugly dependencies in client code it's getting a little ugly
    // here instead. We have to know the class name of the default configuration values and load them via reflection.
    Class<?> lDefaultConfigurationClass = this.loadDefaultConfigurationClass();

    // If we found the default configuration class then we take the configuration values from the annotation.
    T lDefaultConfiguration;
    if (lDefaultConfigurationClass != null) {
      lDefaultConfiguration = lDefaultConfigurationClass.getAnnotation(this.getAnnotationClass());
      configurationClass = lDefaultConfigurationClass;
    }
    // No default configuration is available. This may be fine or not depending of a may be existing custom
    // configuration.
    else {
      lDefaultConfiguration = this.getEmptyConfiguration();
      configurationClass = null;
    }
    return lDefaultConfiguration;
  }

  /**
   * Method returns the class from which the configuration was loaded.
   * 
   * @return Class from which the configuration was actually read. The method never returns null.
   */
  public final Class<?> getConfigurationClass( ) {
    return configurationClass;
  }

  /**
   * Method returns a list with all configuration errors that were detected when the configuration was loaded.
   * 
   * @return List with all detected configuration errors. The method never returns null.
   */
  public final List<String> getConfigurationErrors( ) {
    return configErrors;
  }

  /**
   * Method checks if the default configuration is available.
   * 
   * @return boolean Method returns <code>true</code> if the default configuration is available and <code>false</code>
   * otherwise.
   */
  public final boolean isDefaultConfigurationAvailable( ) {
    boolean lDefaultConfigAvailable;
    if (defaultConfig == null) {
      lDefaultConfigAvailable = false;
    }
    else {
      lDefaultConfigAvailable = !this.getEmptyConfiguration().getClass().equals(defaultConfig.getClass());
    }
    return lDefaultConfigAvailable;
  }

  /**
   * Method loads the class that contains the default configuration.
   * 
   * @return {@link Class} Class that contains the default configuration. The method may return null.
   */
  private Class<?> loadDefaultConfigurationClass( ) {
    Class<?> lDefaultConfigurationClass;
    try {
      // Try to load default configuration class.
      String lClassName = this.getDefaultConfigurationClass();
      if (lClassName != null) {
        lDefaultConfigurationClass = Class.forName(lClassName);
      }
      // There may be case when there is no meaningful default.
      else {
        lDefaultConfigurationClass = null;
      }
    }
    // Configured default configuration could not be found in classpath. This is a regular situation.
    catch (ClassNotFoundException e) {
      lDefaultConfigurationClass = null;
    }
    return lDefaultConfigurationClass;
  }

  /**
   * Method returns the configured custom configuration.
   * 
   * @param pCustomConfigurationResourceName Name of the file which contains the class name of the custom configuration
   * class. The parameter must not be null.
   * @param pBasePackagePath Path under which the file should be found in the classpath. The parameter may be null.
   * @return T Custom configuration. The method never returns null. In case of no custom configuration an empty
   * configuration will be used {@link #getEmptyConfiguration()}.
   */
  private T loadCustomConfiguration( String pCustomConfigurationResourceName,
      String pCustomConfigurationBasePackagePath ) {

    // Determine class that contains the config annotation from resources.
    T lAnnotation;
    if (pCustomConfigurationResourceName != null) {
      ConfigurationReader lReader = new ConfigurationReader();
      Class<?> lCustomConfigClass =
          lReader.readClassFromConfigFile(pCustomConfigurationResourceName, pCustomConfigurationBasePackagePath);

      // Custom configuration is present
      if (lCustomConfigClass != null) {
        // Get annotation
        lAnnotation = lCustomConfigClass.getAnnotation(this.getAnnotationClass());
        if (lAnnotation == null) {
          XFun.getTrace().error("Configured class " + lCustomConfigClass.getName() + " does not have annotation "
              + this.getAnnotationClass().getName());
          lAnnotation = this.getEmptyConfiguration();
        }
        else {
          configurationClass = lCustomConfigClass;
        }
      }
      // No custom configuration is available, so we use an empty default annotation.
      else {
        lAnnotation = this.getEmptyConfiguration();
      }
    }
    // Name of custom configuration is null.
    else {
      lAnnotation = this.getEmptyConfiguration();
    }

    // Return annotation.
    Assert.assertNotNull(lAnnotation, "lAnnotation (Custom Configuration Annotation)");
    return lAnnotation;
  }

  /**
   * Method checks if the default configuration is available.
   * 
   * @return boolean Method returns <code>true</code> if the default configuration is available and <code>false</code>
   * otherwise.
   */
  public final boolean isCustomConfigurationAvailable( ) {
    // Compare custom and default configuration
    boolean lCustomEqualsDefaultConfig;
    if (defaultConfig != null) {
      lCustomEqualsDefaultConfig = defaultConfig.equals(customConfig);
    }
    else {
      lCustomEqualsDefaultConfig = false;
    }

    // Compare custom and empty configuration
    Class<? extends Annotation> lEmptyConfigClass = this.getEmptyConfiguration().getClass();
    boolean lCustomEqualsEmptyConfig = lEmptyConfigClass.equals(customConfig.getClass());

    // A custom configuration is available if the custom configuration is not the same as the default one and if the
    // custom is not the empty configuration.
    boolean lCustomConfigurationAvailable;
    if (lCustomEqualsDefaultConfig == false && lCustomEqualsEmptyConfig == false) {
      lCustomConfigurationAvailable = true;
    }
    else {
      lCustomConfigurationAvailable = false;
    }
    return lCustomConfigurationAvailable;
  }

  /**
   * Method checks the passed custom configuration. During check we check all params of the configuration as we assume
   * that there is no default configuration.
   * 
   * @param pImplClass Class object of which a new instance should be created. The parameter must not be null.
   * @param pInterface Interface that is supposed to be implemented by the passed class. The parameter is only needed to
   * generate some meaningful exception message.
   * @param pConfigErrors List of strings describing the found problems in the passed custom configuration. If this
   * methods identifies new problems then it will add an entry to the passed list. The parameter must not be null.
   */
  protected final <X> void tryNewInstance( Class<? extends X> pImplClass, Class<X> pInterface,
      List<String> pConfigErrors ) {

    // Check parameter
    Assert.assertNotNull(pConfigErrors, "pConfigErrors");

    // Try to create new instance of this class. If this is possible then everything is fine. That the right interface
    // is implemented is already ensured by the compiler.
    if (pImplClass != null && pImplClass.isInterface() == false) {
      try {
        pImplClass.newInstance();
      }
      // We have a configuration issue. We can not create a new instance of the passed class.
      catch (ReflectiveOperationException e) {
        String lConfigError = "Unable to create new instance of implementation for interface " + pInterface.getName()
            + ". Faulty implemetation class: " + pImplClass.getName();

        // In order to not loose exception in critical cases we have to do it in this ugly way.
        System.err.println(lConfigError);
        e.printStackTrace(System.err);
        pConfigErrors.add(lConfigError);
      }
    }
    else {
      // Implementation class is null.
      if (pImplClass == null) {
        String lConfigError = "Unable to create new instance of configured class. Configured class is 'null'.";
        pConfigErrors.add(lConfigError);
      }
      // Provided implementation class actually is just an interface.
      else {
        String lConfigError = "Unable to create new instance of configured class " + pImplClass.getName()
            + ". Configured class is an interface.";
        pConfigErrors.add(lConfigError);
      }
    }
  }

  /**
   * Method is intended to be used when creating new objects of configured classes. In case that a custom class is
   * provided it will be used if not the the default class will be used.
   * 
   * @param pCustomClass Custom class of which a new instance should be created. The parameter may be null. At least one
   * of the parameters pCustomClass and pDefaultClass must not be null.
   * @param pDefaultClass Default class of which an instance should be created. The parameter may be null. At least one
   * of the parameters pCustomClass and pDefaultClass must not be null.
   * @param pType Type parameter for Java generics.
   * @param pExceptionOnError If parameter is set to true then an exception will be thrown in case of an error else null
   * will be returned.
   * @return X Created instance of one of the passed classes. The method may return null in case that neither a custom
   * class nor a default class is configured or in case that no instance of the configured class could be created.
   */
  protected final <X> X newInstance( Class<? extends X> pCustomClass, Class<? extends X> pDefaultClass,
      boolean pExceptionOnError ) {
    // Resolve configured class in a 2-way approach. First we look at the custom configuration afterwards we do the
    // fallback to our default configuration.
    Class<? extends X> lConfiguredClass = pCustomClass;

    // Do fallback to default configuration
    if (lConfiguredClass == null || lConfiguredClass.isInterface()) {
      lConfiguredClass = pDefaultClass;
    }

    // Check if configured class is set.
    return this.newInstance(lConfiguredClass, pExceptionOnError);
  }

  /**
   * Method tries to create a new instance of the passed class using its default constructor.
   * 
   * @param pClass Class of which a new instance should be created. The parameter must not be null.
   * @param pExceptionOnError If parameter is set to true then an exception will be thrown in case of an error else null
   * will be returned.
   * @return X New instance of the passed class or null if the instance could not be created.
   */
  protected final <X> X newInstance( Class<? extends X> pClass, boolean pExceptionOnError ) {
    X lNewInstance;
    if (pClass != null) {
      lNewInstance = this.newInstanceOrNull(pClass, pExceptionOnError);
    }
    // No class configured, so we can not create an instance and will return null.
    else {
      if (pExceptionOnError == true) {
        throw new XFunRuntimeException("Unable to create new instance of class 'null'.");
      }
      else {
        lNewInstance = null;
      }
    }
    return lNewInstance;
  }

  /**
   * Method tries to create a new instance of the passed class.
   * 
   * @param <X>
   * @param pClass Class from which a new instance should be created. The parameter must not be null.
   * @param pExceptionOnError Parameter defines if in case of an error either null should be returned or an exception
   * should be thrown.
   * @return New instance that was created. Depending on parameter pExceptionOnError the method might return null.
   */
  private <X> X newInstanceOrNull( Class<? extends X> pClass, boolean pExceptionOnError ) {
    // Create instance of class.
    X lNewInstance;
    try {
      lNewInstance = pClass.newInstance();
    }
    catch (ReflectiveOperationException e) {
      String lMessage = "Unable to create new instance of class " + pClass.getName() + ".";
      FallbackTraceProviderImpl.EMERGENCY_TRACE.error(lMessage, e);
      if (pExceptionOnError == true) {
        throw new XFunRuntimeException(lMessage, e);
      }
      else {
        lNewInstance = null;
      }
    }
    catch (RuntimeException | Error e) {
      String lMessage = "Unable to create new instance of class " + pClass.getName() + ".";
      FallbackTraceProviderImpl.EMERGENCY_TRACE.error(lMessage, e);
      if (pExceptionOnError == true) {
        throw e;
      }
      else {
        lNewInstance = null;
      }
    }
    return lNewInstance;
  }

  /**
   * 
   * @param <X>
   * @param pClasses
   * @param pExceptionOnError If parameter is set to true then an exception will be thrown in case of an error else null
   * will be returned.
   * @return
   */
  protected final <X> List<X> newInstances( Collection<Class<? extends X>> pClasses, boolean pExceptionOnError ) {
    // Try to create new instance of each class.
    List<X> lNewInstances;
    if (pClasses != null) {
      lNewInstances = new ArrayList<>(pClasses.size());
      for (Class<? extends X> lNextClass : pClasses) {
        X lNewInstance = this.newInstance(lNextClass, pExceptionOnError);
        if (lNewInstance != null) {
          lNewInstances.add(lNewInstance);
        }
      }
    }
    else {
      lNewInstances = Collections.emptyList();
    }
    return lNewInstances;
  }

  /**
   * Method converts that passed array of objects to a list of objects.
   * 
   * @param pBooleans Array of objects. The parameter may be null.
   * @return {@link List} List of objects. The method returns null if null is passed. If the array is empty then an
   * empty list will be returned.
   */
  protected final <X> List<X> asList( X[] pObjects ) {
    List<X> lList;
    if (pObjects != null) {
      lList = Arrays.asList(pObjects);
    }
    else {
      lList = null;
    }
    return lList;
  }

  /**
   * Method converts that passed array of primitive booleans to a list of Boolean objects.
   * 
   * @param pBooleans Array of primitive booleans. The parameter may be null.
   * @return {@link List} List of Boolean objects. The method returns null if null is passed. If the array is empty then
   * an empty list will be returned.
   */
  protected final List<Boolean> asList( boolean[] pBooleans ) {
    List<Boolean> lList;
    if (pBooleans != null) {
      lList = new ArrayList<>(pBooleans.length);
      for (int i = 0; i < pBooleans.length; i++) {
        lList.add(pBooleans[i]);
      }
    }
    else {
      lList = null;
    }
    return lList;
  }

  /**
   * Method converts that passed array of primitive bytes to a list of Byte objects.
   * 
   * @param pBytes Array of primitive byte. The parameter may be null.
   * @return {@link List} List of Byte objects. The method returns null if null is passed. If the array is empty then an
   * empty list will be returned.
   */
  protected final List<Byte> asList( byte[] pBytes ) {
    List<Byte> lList;
    if (pBytes != null) {
      lList = new ArrayList<>(pBytes.length);
      for (int i = 0; i < pBytes.length; i++) {
        lList.add(pBytes[i]);
      }
    }
    else {
      lList = null;
    }
    return lList;
  }

  /**
   * Method converts that passed array of primitive shorts to a list of Short objects.
   * 
   * @param pShorts Array of primitive shorts. The parameter may be null.
   * @return {@link List} List of Short objects. The method returns null if null is passed. If the array is empty then
   * an empty list will be returned.
   */
  protected final List<Short> asList( short[] pShorts ) {
    List<Short> lList;
    if (pShorts != null) {
      lList = new ArrayList<>(pShorts.length);
      for (int i = 0; i < pShorts.length; i++) {
        lList.add(pShorts[i]);
      }
    }
    else {
      lList = null;
    }
    return lList;
  }

  /**
   * Method converts that passed array of primitive ints to a list of Integer objects.
   * 
   * @param pInts Array of primitive ints. The parameter may be null.
   * @return {@link List} List of Integer objects. The method returns null if null is passed. If the array is empty then
   * an empty list will be returned.
   */
  protected final List<Integer> asList( int[] pInts ) {
    List<Integer> lList;
    if (pInts != null) {
      lList = new ArrayList<>(pInts.length);
      for (int i = 0; i < pInts.length; i++) {
        lList.add(pInts[i]);
      }
    }
    else {
      lList = null;
    }
    return lList;
  }

  /**
   * Method converts that passed array of primitive longs to a list of Long objects.
   * 
   * @param pLongs Array of primitive longs. The parameter may be null.
   * @return {@link List} List of Long objects. The method returns null if null is passed. If the array is empty then an
   * empty list will be returned.
   */
  protected final List<Long> asList( long[] pLongs ) {
    List<Long> lList;
    if (pLongs != null) {
      lList = new ArrayList<>(pLongs.length);
      for (int i = 0; i < pLongs.length; i++) {
        lList.add(pLongs[i]);
      }
    }
    else {
      lList = null;
    }
    return lList;
  }

  /**
   * Method converts that passed array of primitive floats to a list of Float objects.
   * 
   * @param pFloats Array of primitive floats. The parameter may be null.
   * @return {@link List} List of Float objects. The method returns null if null is passed. If the array is empty then
   * an empty list will be returned.
   */
  protected final List<Float> asList( float[] pFloats ) {
    List<Float> lList;
    if (pFloats != null) {
      lList = new ArrayList<>(pFloats.length);
      for (int i = 0; i < pFloats.length; i++) {
        lList.add(pFloats[i]);
      }
    }
    else {
      lList = null;
    }
    return lList;
  }

  /**
   * Method converts that passed array of primitive doubles to a list of Double objects.
   * 
   * @param pDoubles Array of primitive doubles. The parameter may be null.
   * @return {@link List} List of Double objects. The method returns null if null is passed. If the array is empty then
   * an empty list will be returned.
   */
  protected final List<Double> asList( double[] pDoubles ) {
    List<Double> lList;
    if (pDoubles != null) {
      lList = new ArrayList<>(pDoubles.length);
      for (int i = 0; i < pDoubles.length; i++) {
        lList.add(pDoubles[i]);
      }
    }
    else {
      lList = null;
    }
    return lList;
  }
}
