package eu.toop.connector.api.smp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.error.level.EErrorLevel;
import com.helger.commons.log.LogHelper;

import eu.toop.edm.error.IToopErrorCode;

public class LoggingSMPErrorHandler implements ISMPErrorHandler
{
  public static final LoggingSMPErrorHandler INSTANCE = new LoggingSMPErrorHandler ();
  private static final Logger LOGGER = LoggerFactory.getLogger (LoggingSMPErrorHandler.class);

  public void onMessage (final EErrorLevel eErrorLevel, final String sMsg, final Throwable t, final IToopErrorCode eCode)
  {
    LogHelper.log (LOGGER, eErrorLevel, sMsg, t);
  }
}
