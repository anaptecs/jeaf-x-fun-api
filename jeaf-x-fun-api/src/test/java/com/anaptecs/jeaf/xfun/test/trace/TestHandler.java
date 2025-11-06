/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.trace;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class TestHandler extends Handler {
  private List<LogRecord> logRecords = new ArrayList<>();

  @Override
  public void publish( LogRecord pRecord ) {
    logRecords.add(pRecord);
  }

  public List<LogRecord> getAllLogRecords( ) {
    return logRecords;
  }

  public LogRecord getLastLogRecord( ) {
    int lLastIndex = logRecords.size() - 1;
    LogRecord lLastLogRecord;
    if (lLastIndex >= 0) {
      lLastLogRecord = logRecords.get(lLastIndex);
    }
    else {
      lLastLogRecord = null;
    }
    return lLastLogRecord;
  }

  public void clear( ) {
    logRecords.clear();
  }

  @Override
  public void flush( ) {
    // Nothing to do
  }

  @Override
  public void close( ) throws SecurityException {
    // Nothing to do
  }
}
