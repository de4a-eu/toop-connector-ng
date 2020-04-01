package eu.toop.edm.cv;

import static org.junit.Assert.assertNotNull;

import javax.annotation.Nonnull;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3.ns.corevocabulary.business.CvbusinessType;

/**
 * Test class for class {@link BusinessPojo}.
 *
 * @author Philip Helger
 */
public final class BusinessPojoTest
{
  private static final Logger LOGGER = LoggerFactory.getLogger (BusinessPojoTest.class);

  private static void _validate (@Nonnull final BusinessPojo x)
  {
    final CvbusinessType aBusiness = x.getAsBusiness ();
    assertNotNull (aBusiness);

    final BusinessMarshaller m = new BusinessMarshaller ();
    m.setFormattedOutput (true);
    assertNotNull (m.getAsDocument (aBusiness));
    LOGGER.info (m.getAsString (aBusiness));
  }

  @Test
  public void testBasic ()
  {
    final BusinessPojo x = new BusinessPojo ("LegalName",
                                             "LegalID",
                                             "LegalIDType",
                                             "ID",
                                             "IDType",
                                             "FullAddress",
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
    final BusinessPojo x = BusinessPojo.createMinimum ();
    _validate (x);
  }
}
