/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.anaptecs.jeaf.xfun.api.XFunRuntimeException;
import com.anaptecs.jeaf.xfun.bootstrap.Assert;
import com.anaptecs.jeaf.xfun.bootstrap.Check;
import com.anaptecs.jeaf.xfun.fallback.trace.FallbackTraceProviderImpl;

/**
 * Class implements a so called configuration reader. This classes resolves the name of a single class or of a list of
 * classes from a configuration file. Configuration file loaded using the current class loader. This means that the
 * provided base package must be aware of this as well as the configuration file must be loadable from the classpath.
 * 
 * The class is not intended to be used to read configuration values from a file e.g. a property file.
 * 
 * Configuration classes are expected to consist of one class name per line.
 * 
 * @author JEAF Development Team
 */
public class ConfigurationReader {
  private static final String UTF_8 = StandardCharsets.UTF_8.name();

  /**
   * Method reads all classes from the configuration file with the passed name. This method is intended to be used in
   * cases where a config file should not consists of more than one class name.
   * 
   * @param pConfigurationFileName Name of configuration file. The parameter must not be null. The passed file name will
   * be extended with <code>pBasePackage</code>.
   * @param pBasePackagePath Path under which the file should be found in the classpath. The parameter may be null.
   * @return {@link List} List with all classes that were read from the configuration file. The method never returns
   * null. If the file could not read read from the classpath then the method returns an empty list. The returned list
   * is immutable.
   */
  public List<Class<?>> readClassesFromConfigFile( String pConfigurationFileName, String pBasePackagePath ) {
    return this.readClassesFromConfigFile(pConfigurationFileName, pBasePackagePath, Object.class);
  }

  /**
   * Method reads all classes from the configuration file with the passed name. This method is intended to be used in
   * cases where a config file should not consists of more than one class name.
   * 
   * @param pConfigurationFileName Name of configuration file. The parameter must not be null. The passed file name will
   * be extended with <code>pBasePackage</code>.
   * @param pBasePackagePath Path under which the file should be found in the classpath. The parameter may be null.
   * @param pType Class object representing the expected type of the returned classes.
   * @return {@link List} List with all classes that were read from the configuration file. The method never returns
   * null. If the file could not read read from the classpath then the method returns an empty list. The returned list
   * is immutable. If the configuration file contains a class that is not of the expected type then it will be filtered
   * out.
   */
  public <T> List<Class<? extends T>> readClassesFromConfigFile( String pConfigurationFileName, String pBasePackagePath,
      Class<T> pType ) {

    // Check parameter
    Check.checkInvalidParameterNull(pConfigurationFileName, "pConfigurationFileName");
    Check.checkInvalidParameterNull(pType, "pType");

    // Build resource name from parameters.
    StringBuilder lBuilder = new StringBuilder();
    if (pBasePackagePath != null) {
      lBuilder.append(pBasePackagePath);
      lBuilder.append('/');
    }
    lBuilder.append(pConfigurationFileName);
    String lResourceName = lBuilder.toString();
    return this.readClassesFromConfigFile(lResourceName, pType);
  }

  /**
   * Method reads all classes from the configuration file with the passed name. This method is intended to be used in
   * cases where a config file should not consists of more than one class name.
   * 
   * @param pConfigurationFilePath Path of configuration file. The parameter must not be null.
   * @param pType Class object representing the expected type of the returned classes.
   * @return {@link List} List with all classes that were read from the configuration file. The method never returns
   * null. If the file could not read read from the classpath then the method returns an empty list. The returned list
   * is immutable. If the configuration file contains a class that is not of the expected type then it will be filtered
   * out.
   */
  public List<Class<?>> readClassesFromConfigFile( String pConfigurationFilePath ) {
    return this.readClassesFromConfigFile(pConfigurationFilePath, Object.class);
  }

  /**
   * Method reads all classes from the configuration file with the passed name. This method is intended to be used in
   * cases where a config file should not consists of more than one class name.
   * 
   * @param pConfigurationFilePath Path of configuration file. The parameter must not be null.
   * @param pType Class object representing the expected type of the returned classes.
   * @return {@link List} List with all classes that were read from the configuration file. The method never returns
   * null. If the file could not read read from the classpath then the method returns an empty list. The returned list
   * is immutable. If the configuration file contains a class that is not of the expected type then it will be filtered
   * out.
   */
  public <T> List<Class<? extends T>> readClassesFromConfigFile( String pConfigurationFilePath, Class<T> pType ) {
    // Check parameter
    Check.checkInvalidParameterNull(pConfigurationFilePath, "pConfigurationFilePath");
    Check.checkInvalidParameterNull(pType, "pType");

    // Resolve URL of configuration file
    URL lResourceURL = this.resolveResource(pConfigurationFilePath);

    // Configuration file could be located.
    Collection<String> lConfigurationFileEntries;
    if (lResourceURL != null) {
      try (InputStreamReader lInputStream = new InputStreamReader(lResourceURL.openStream(), UTF_8);
          BufferedReader lReader = new BufferedReader(lInputStream);) {

        // Read configuration entries.
        lConfigurationFileEntries = lReader.lines().collect(Collectors.toList());
      }
      catch (IOException e) {
        throw new XFunRuntimeException(
            "Unable to read configuration from resource " + pConfigurationFilePath + ". " + e.getMessage(), e);
      }
    }
    // Configuration file could not be found.
    else {
      FallbackTraceProviderImpl.EMERGENCY_TRACE
          .warn("Configuration file " + pConfigurationFilePath + " could not be found in the application's classpath.");
      lConfigurationFileEntries = Collections.emptyList();
    }

    // Get classes objects for all configured class names
    return this.toClasses(lConfigurationFileEntries, pType, pConfigurationFilePath);
  }

  /**
   * Method reads exactly 1 classes from the configuration file with the passed name. This method is intended to be used
   * in cases where a config file is expected to consist of exactly one class name.
   * 
   * @param pConfigurationFileName Name of configuration file. The parameter must not be null. The passed file name will
   * be extended with <code>pBasePackage</code>.
   * @param pBasePackagePath Path under which the file should be found in the classpath. The parameter may be null.
   * @return Class that was read from the configuration file. The method returns null if the config file does not exists
   * or is empty.
   */
  public Class<?> readClassFromConfigFile( String pConfigurationFileName, String pBasePackagePath ) {
    return this.readClassFromConfigFile(pConfigurationFileName, pBasePackagePath, Object.class);
  }

  /**
   * Method reads exactly 1 classes from the configuration file with the passed name. This method is intended to be used
   * in cases where a config file is expected to consist of exactly one class name.
   * 
   * @param pConfigurationFileName Name of configuration file. The parameter must not be null. The passed file name will
   * be extended with <code>pBasePackage</code>.
   * @param pBasePackagePath Path under which the file should be found in the classpath. The parameter may be null.
   * @param pType Class object representing the expected type of the returned classes.
   * @return Class that was read from the configuration file. The method returns null if the config file does not
   * exists, is empty or the configured class is not of the expected type.
   */
  public <T> Class<? extends T> readClassFromConfigFile( String pConfigurationFileName, String pBasePackagePath,
      Class<T> pType ) {

    // Check parameter
    Check.checkInvalidParameterNull(pConfigurationFileName, "pConfigurationFileName");
    Check.checkInvalidParameterNull(pType, "pType");

    // Build resource name from parameters.
    StringBuilder lBuilder = new StringBuilder();
    if (pBasePackagePath != null) {
      lBuilder.append(pBasePackagePath);
      lBuilder.append('/');
    }
    lBuilder.append(pConfigurationFileName);
    String lResourceName = lBuilder.toString();
    return this.readClassFromConfigFile(lResourceName, pType);
  }

  /**
   * Method reads exactly 1 classes from the configuration file with the passed name. This method is intended to be used
   * in cases where a config file is expected to consist of exactly one class name.
   * 
   * @param pConfigurationFilePath Path of configuration file. The parameter must not be null.
   * @param pType Class object representing the expected type of the returned classes.
   * @return Class that was read from the configuration file. The method returns null if the config file does not
   * exists, is empty or the configured class is not of the expected type.
   */
  public Class<?> readClassFromConfigFile( String pConfigurationFilePath ) {
    return this.readClassFromConfigFile(pConfigurationFilePath, Object.class);
  }

  /**
   * Method reads exactly 1 classes from the configuration file with the passed name. This method is intended to be used
   * in cases where a config file is expected to consist of exactly one class name.
   * 
   * @param pConfigurationFilePath Path of configuration file. The parameter must not be null.
   * @param pType Class object representing the expected type of the returned classes.
   * @return Class that was read from the configuration file. The method returns null if the config file does not
   * exists, is empty or the configured class is not of the expected type.
   */
  public <T> Class<? extends T> readClassFromConfigFile( String pConfigurationFilePath, Class<T> pType ) {
    // Check parameter
    Check.checkInvalidParameterNull(pConfigurationFilePath, "pConfigurationFilePath");
    Check.checkInvalidParameterNull(pType, "pType");

    List<Class<? extends T>> lClasses = this.readClassesFromConfigFile(pConfigurationFilePath, pType);
    int lSize = lClasses.size();
    Class<? extends T> lClass;

    // No class is configured.
    if (lSize == 0) {
      lClass = null;
    }
    // Class is configured.
    else if (lSize == 1) {
      lClass = lClasses.get(0);
    }
    // Wrong configuration. We expect not more than one class name.
    else {
      throw new XFunRuntimeException("Configuration file " + pConfigurationFilePath
          + " contains more than one class name. Please correct the file.");
    }
    return lClass;
  }

  /**
   * Method checks if the configuration file with the passed path is available through the application's classpath.
   * 
   * @param pConfigurationFilePath Path of configuration file. The parameter must not be null.
   * @return
   */
  public boolean isConfigurationAvailable( String pConfigurationFilePath ) {
    // Check parameter
    Check.checkInvalidParameterNull(pConfigurationFilePath, "pConfigurationFilePath");

    return this.resolveResource(pConfigurationFilePath) != null;
  }

  /**
   * Method resolved the names of the classes that are defined in the passed configuration files. From all mentioned
   * classes the passed annotation will be read and returned.
   * 
   * @param pConfigurationFileName Name of configuration file. The parameter must not be null. The passed file name will
   * be extended with <code>pBasePackage</code>.
   * @param pBasePackagePath Path under which the file should be found in the classpath. The parameter may be null.
   * @param pAnnotation Annotation type that contains the configurations that should be returned. The parameter must not
   * be null.
   * @return {@link List} List with all annotations of the requested type. The method never returns null.
   */
  public <T extends Annotation> List<T> readAnnotations( String pConfigurationFileName, String pBasePackagePath,
      Class<T> pAnnotation ) {

    // Check parameter
    Check.checkInvalidParameterNull(pConfigurationFileName, "pConfigurationFileName");
    Check.checkInvalidParameterNull(pAnnotation, "pAnnotation");

    // Build resource name from parameters.
    StringBuilder lBuilder = new StringBuilder();
    if (pBasePackagePath != null) {
      lBuilder.append(pBasePackagePath);
      lBuilder.append('/');
    }
    lBuilder.append(pConfigurationFileName);
    String lResourceName = lBuilder.toString();
    return this.readAnnotations(lResourceName, pAnnotation);
  }

  /**
   * Method resolved the names of the classes that are defined in the passed configuration files. From all mentioned
   * classes the passed annotation will be read and returned.
   * 
   * @param pConfigurationFilePath Configuration file from which the names of annotated classes should be read. The
   * parameter must not be null.
   * @param pAnnotation Annotation type that contains the configurations that should be returned. The parameter must not
   * be null.
   * @return {@link List} List with all annotations of the requested type. The method never returns null.
   */
  public <T extends Annotation> List<T> readAnnotations( String pConfigurationFilePath, Class<T> pAnnotation ) {
    // Check parameters
    Check.checkInvalidParameterNull(pConfigurationFilePath, "pConfigurationFilePath");
    Check.checkInvalidParameterNull(pAnnotation, "pAnnotation");

    // Read all classes from configuration file and get their requested annotations.
    List<Class<?>> lClasses = this.readClassesFromConfigFile(pConfigurationFilePath);
    return this.getAnnotations(lClasses, pAnnotation);
  }

  /**
   * Method returns all the annotations of the passed type from the passed classes. The annotations will only be
   * detected if they are assigned to the type.
   * 
   * @param <T>
   * @param pClasses Classes from which the annotations should be read. The parameter must not be null.
   * @param pAnnotation Annotation type that should be returned. The parameter must not be null. all other annotations
   * will be ignored.
   * @return {@link List} List with all annotations of the passed type. The method never returns null.
   */
  private <T extends Annotation> List<T> getAnnotations( List<Class<?>> pClasses, Class<T> pAnnotation ) {
    List<T> lAnnotations = new ArrayList<>(pClasses.size());
    for (Class<?> lNextClass : pClasses) {
      T lAnnotation = lNextClass.getAnnotation(pAnnotation);
      if (lAnnotation != null) {
        lAnnotations.add(lAnnotation);
      }
    }
    return lAnnotations;
  }

  /**
   * Method returns a map which contains a mapping between the class and its annotation of the passed type. The classes
   * are read from the configuration file with the passed name.
   * 
   * @param pConfigurationFileName Name of configuration file. The parameter must not be null. The passed file name will
   * be extended with <code>pBasePackage</code>.
   * @param pBasePackagePath Path under which the file should be found in the classpath. The parameter may be null.
   * @param pAnnotation Annotation type that should be returned. The parameter must not be null. all other annotations
   * will be ignored.
   * @return {@link Map} Map which contains the mapping between the class and its annotation of the passed type. If a
   * class does not have the requested annotation then it will not be part of the returned map even though its mentioned
   * on the defined configuration file.
   */
  public <T extends Annotation> Map<Class<?>, T> readAnnotationsMap( String pConfigurationFileName,
      String pBasePackagePath, Class<T> pAnnotation ) {

    // Check parameter
    Check.checkInvalidParameterNull(pConfigurationFileName, "pConfigurationFileName");
    Check.checkInvalidParameterNull(pAnnotation, "pAnnotation");

    // Build resource name from parameters.
    StringBuilder lBuilder = new StringBuilder();
    if (pBasePackagePath != null) {
      lBuilder.append(pBasePackagePath);
      lBuilder.append('/');
    }
    lBuilder.append(pConfigurationFileName);
    String lResourceName = lBuilder.toString();
    return this.readAnnotationsMap(lResourceName, pAnnotation);
  }

  /**
   * Method returns a map which contains a mapping between the class and its annotation of the passed type. The classes
   * are read from the configuration file with the passed name.
   * 
   * @param pConfigurationFilePath Configuration file from which the names of annotated classes should be read. The
   * parameter must not be null.
   * @param pAnnotation Annotation type that should be returned. The parameter must not be null. all other annotations
   * will be ignored.
   * @return {@link Map} Map which contains the mapping between the class and its annotation of the passed type. If a
   * class does not have the requested annotation then it will not be part of the returned map even though its mentioned
   * on the defined configuration file.
   */
  public <T extends Annotation> Map<Class<?>, T> readAnnotationsMap( String pConfigurationFilePath,
      Class<T> pAnnotation ) {

    // Check parameters
    Check.checkInvalidParameterNull(pConfigurationFilePath, "pConfigurationFilePath");
    Check.checkInvalidParameterNull(pAnnotation, "pAnnotation");

    // Read all classes from configuration file and get their requested annotations.
    List<Class<?>> lClasses = this.readClassesFromConfigFile(pConfigurationFilePath);
    return this.getAnnotationsMap(lClasses, pAnnotation);
  }

  /**
   * 
   * @param <T>
   * @param pClasses
   * @param pAnnotation
   * @return
   */
  private <T extends Annotation> Map<Class<?>, T> getAnnotationsMap( List<Class<?>> pClasses, Class<T> pAnnotation ) {
    Map<Class<?>, T> lAnnotationsMap = new HashMap<>();
    for (Class<?> lNextClass : pClasses) {
      T lAnnotation = lNextClass.getAnnotation(pAnnotation);
      if (lAnnotation != null) {
        lAnnotationsMap.put(lNextClass, lAnnotation);
      }
    }
    return lAnnotationsMap;
  }

  /**
   * Method converts a list of class names into a list of class objects.
   * 
   * @param pClassNames List with all class names. The parameter must not be null.
   * @return {@link List} with all class objects to the passed class names. The method never returns null. The returned
   * {@link List} is immutable.
   */
  private <T> List<Class<? extends T>> toClasses( Collection<String> pClassNames, Class<T> pType,
      String pResourceName ) {
    List<Class<T>> lClasses = new ArrayList<>(pClassNames.size());
    for (String lNextClassName : pClassNames) {
      try {
        @SuppressWarnings("unchecked")
        Class<T> lNextClass = (Class<T>) Class.forName(lNextClassName);

        // As Java generics not really check at runtime that the types match we have to do this manually now.
        if (pType.isAssignableFrom(lNextClass)) {
          lClasses.add(lNextClass);
        }
        else {
          String lMessage = "Configuration error for " + pResourceName + ". Class " + lNextClassName
              + " is not an subclass / implementation of " + pType.getName();
          FallbackTraceProviderImpl.EMERGENCY_TRACE.error(lMessage);
        }
      }
      catch (ExceptionInInitializerError e) {
        Throwable lCause = e.getCause();
        String lCauseMessage;
        if (lCause != null) {
          lCauseMessage = "Root-Cause: " + lCause.getMessage();
        }
        else {
          lCauseMessage = "";
        }
        String lMessage = "Configuration error for " + pResourceName + ". Class " + lNextClassName
            + " could not be loaded. " + lCauseMessage;
        FallbackTraceProviderImpl.EMERGENCY_TRACE.error(lMessage, e);

      }
      catch (Throwable e) {
        String lMessage = "Configuration error for " + pResourceName + ". Class " + lNextClassName
            + " could not be loaded. " + e.getMessage();
        FallbackTraceProviderImpl.EMERGENCY_TRACE.error(lMessage, e);
      }
    }
    return Collections.unmodifiableList(lClasses);
  }

  /**
   * Method resolves the URL of the passed configuration file using the application's classpath.
   * 
   * @param pConfigurationFilePath Path of configuration file. The parameter must not be null.
   * @return {@link URL} URL of the configuration file or null if it could not be accessed through the applications
   * classpath
   */
  private URL resolveResource( String pConfigurationFilePath ) {
    // Check parameter
    Assert.assertNotNull(pConfigurationFilePath, "pConfigurationFilePath");

    // Try to locate resource
    URL lResourceURL = this.getClass().getClassLoader().getResource(pConfigurationFilePath);

    // Try to locate resource as system resource if we could not find it directly.
    if (lResourceURL == null) {
      lResourceURL = ClassLoader.getSystemResource(pConfigurationFilePath);
    }
    // Return URL or null
    return lResourceURL;
  }

}
