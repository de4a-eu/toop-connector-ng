package eu.toop.connector.webapi.helper;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.annotation.Nonnull;

import com.helger.commons.datetime.PDTFactory;
import com.helger.commons.timing.StopWatch;
import com.helger.json.IJsonObject;

public class CommonAPIInvoker
{
  private CommonAPIInvoker ()
  {}

  public static void invoke (@Nonnull final IJsonObject aJson, @Nonnull final Runnable r)
  {

    final ZonedDateTime aQueryDT = PDTFactory.getCurrentZonedDateTimeUTC ();
    final StopWatch aSW = StopWatch.createdStarted ();
    r.run ();
    aSW.stop ();

    aJson.add ("invocationDateTime", DateTimeFormatter.ISO_ZONED_DATE_TIME.format (aQueryDT));
    aJson.add ("invocationDurationMillis", aSW.getMillis ());
  }
}
