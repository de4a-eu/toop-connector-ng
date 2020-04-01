package eu.toop.edm.cv;

import static org.junit.Assert.assertNotNull;

import javax.annotation.Nonnull;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3.ns.corevocabulary.location.CvlocationType;

/**
 * Test class for class {@link LocationPojo}.
 *
 * @author Philip Helger
 */
public final class LocationPojoTest
{
  private static final Logger LOGGER = LoggerFactory.getLogger (LocationPojoTest.class);

  private static void _validate (@Nonnull final LocationPojo x)
  {
    final CvlocationType aLocation = x.getAsLocation ();
    assertNotNull (aLocation);

    final LocationMarshaller m = new LocationMarshaller ();
    m.setFormattedOutput (true);
    assertNotNull (m.getAsDocument (aLocation));
    LOGGER.info (m.getAsString (aLocation));
  }

  @Test
  public void testBasic ()
  {
    final LocationPojo x = new LocationPojo ("FullAddress",
                                             "StreetName",
                                             "BuildingNumber",
                                             "Town",
                                             "PostalCode",
                                             "CountryCode");
    _validate (x);
  }

  @Test
  public void testMinimum ()
  {
    final LocationPojo x = LocationPojo.createMinimum ();
    _validate (x);
  }
}
