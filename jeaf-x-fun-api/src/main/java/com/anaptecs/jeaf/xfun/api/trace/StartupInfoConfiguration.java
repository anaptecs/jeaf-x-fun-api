/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.trace;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.anaptecs.jeaf.xfun.annotations.StartupInfoConfig;
import com.anaptecs.jeaf.xfun.annotations.StartupInfoWriterImpl;
import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.config.AnnotationBasedConfiguration;
import com.anaptecs.jeaf.xfun.api.config.ConfigurationReader;

@StartupInfoConfig
public final class StartupInfoConfiguration extends AnnotationBasedConfiguration<StartupInfoConfig> {
  /**
   * Initialize object.
   */
  public StartupInfoConfiguration( ) {
    // Nothing to do. Super class does all the work.
    this(StartupInfoConfig.STARTUP_INFO_CONFIG_RESOURCE_NAME, XFun.X_FUN_BASE_PATH, false);
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
  public StartupInfoConfiguration( String pCustomConfigurationResourceName, String pCustomConfigurationBasePackagePath,
      boolean pExceptionOnError ) {

    // Class super class constructor.
    super(pCustomConfigurationResourceName, pCustomConfigurationBasePackagePath, pExceptionOnError);
  }

  @Override
  protected Class<StartupInfoConfig> getAnnotationClass( ) {
    return StartupInfoConfig.class;
  }

  @Override
  protected String getDefaultConfigurationClass( ) {
    return StartupInfoConfiguration.class.getName();
  }

  @Override
  public StartupInfoConfig getEmptyConfiguration( ) {
    return new StartupInfoConfig() {

      @Override
      public Class<? extends Annotation> annotationType( ) {
        return StartupInfoConfig.class;
      }

      @Override
      public boolean traceStartupInfo( ) {
        return StartupInfoConfig.DEFAULT_TRACE_STARTUP_INFO;
      }

      @Override
      public TraceLevel startupInfoTraceLevel( ) {
        return StartupInfoConfig.DEFAULT_STARTUP_INFO_TRACE_LEVEL;
      }

      @Override
      public String startupInfoWritersResourcePath( ) {
        return StartupInfoWriterImpl.STARTUP_INFO_WRITERS_PATH;
      }
    };
  }

  @Override
  public List<String> checkCustomConfiguration( StartupInfoConfig pCustomConfiguration ) {
    // Nothing to do.
    return Collections.emptyList();
  }

  /**
   * Method returns if startup info should be traced or not.
   * 
   * @return boolean Method returns true if startup info should be traced and false otherwise.
   */
  public boolean traceStartupInfo( ) {
    return theConfig.traceStartupInfo();
  }

  /**
   * Method returns the trace level that should be used for the startup info.
   * 
   * @return {@link TraceLevel} that should be used for startup info. The method never returns null.
   */
  public TraceLevel getStartupInfoTraceLevel( ) {
    return theConfig.startupInfoTraceLevel();
  }

  /**
   * Method returns the path of the resource file where all the startup info writers will be listed.
   * 
   * @return {@link String} Path of the resource file where all the startup info writers will be listed. The method
   * never returns null.
   */
  public String startupInfoWritersResourcePath( ) {
    return theConfig.startupInfoWritersResourcePath();
  }

  /**
   * Method returns all startup info writers that were configured and could be created.
   * 
   * @return {@link List} List with all configured startup info writer. The method never returns null.
   */
  public List<StartupInfoWriter> getStartupInfoWriters( ) {
    // Resolve all configured classes.
    ConfigurationReader lReader = new ConfigurationReader();
    List<Class<? extends StartupInfoWriter>> lClasses =
        lReader.readClassesFromConfigFile(this.startupInfoWritersResourcePath(), StartupInfoWriter.class);

    // Create new info writer objects for all configured classes.
    List<StartupInfoWriter> lWriters = new ArrayList<>(lClasses.size());
    for (Class<? extends StartupInfoWriter> lNextClass : lClasses) {
      StartupInfoWriter lNewWriter = this.newInstance(lNextClass, exceptionOnError);
      if (lNewWriter != null) {
        lWriters.add(lNewWriter);
      }
    }
    return lWriters;
  }
}
