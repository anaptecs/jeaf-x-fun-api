/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.api.trace;

import java.util.ArrayList;
import java.util.List;

public class StartupInfoEventCollector {
  private static final StartupInfoEventCollector INSTANCE = new StartupInfoEventCollector();

  private List<Class<?>> delayedStartupCompletedEvents = new ArrayList<>();

  private List<StartupInfoEventHandler> eventHandlers = new ArrayList<>();

  /**
   * Method notifies that the startup of a class is completed. This is used as trigger for all startup info writers that
   * are registered for that specific class. If no event handler are yet registered (e.g. during early pahses of
   * bootstrapping) then the events will be stored until the first event handler becomes available.
   * 
   * @param pClass Class whose startup is completed. The parameter must not be null.
   */
  public static void startupCompleted( Class<?> pClass ) {
    INSTANCE.internalStartupCompleted(pClass);
  }

  public static void registerEventHandler( StartupInfoEventHandler pEventHandler ) {
    INSTANCE.internalRegisterEventHandler(pEventHandler);
  }

  /**
   * Method notifies the the startup of a class is completed. This is used as trigger for all startup info writers that
   * are registered for that specific class.
   * 
   * @param pClass Class whose startup is completed. The parameter must not be null.
   */
  synchronized void internalStartupCompleted( Class<?> pClass ) {
    // No event handlers are registered yet, so we have to delay notification about the event
    if (eventHandlers.isEmpty() == true) {
      delayedStartupCompletedEvents.add(pClass);
    }
    else {
      this.notifyEventHandlers(pClass);
    }
  }

  synchronized void internalRegisterEventHandler( StartupInfoEventHandler pEventHandler ) {
    // Register new event handler
    if (pEventHandler != null) {
      eventHandlers.add(pEventHandler);

      // Process may be delayed events
      if (delayedStartupCompletedEvents.isEmpty() == false) {
        for (Class<?> lNextEvent : delayedStartupCompletedEvents) {
          this.notifyEventHandlers(lNextEvent);
        }
        delayedStartupCompletedEvents.clear();
      }
    }
  }

  private void notifyEventHandlers( Class<?> pClass ) {
    // Notify all event handlers
    for (StartupInfoEventHandler lNextHandler : eventHandlers) {
      lNextHandler.handleStartupInfoEvent(pClass);
    }
  }
}
