package eu.toop.edm.cpsv.agent;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.toop.edm.jaxb.cpsv.helper.AgentType;

/**
 * Test class for class {@link DataConsumerPojo}
 *
 * @author Philip Helger
 */
public final class DataConsumerPojoTest
{
  private static final Logger LOGGER = LoggerFactory.getLogger (DataConsumerPojoTest.class);

  @Test
  public void testBasic ()
  {
    final DataConsumerPojo x = new DataConsumerPojo ("ID",
                                                     "IDType",
                                                     "Name",
                                                     "FullAddress",
                                                     "StreetName",
                                                     "BuildingNumber",
                                                     "Town",
                                                     "PostalCode",
                                                     "CountryCode");
    final AgentType aAgent = x.getAsAgent ();
    assertNotNull (aAgent);

    final AgentMarshaller m = new AgentMarshaller ();
    m.setFormattedOutput (true);
    assertNotNull (m.getAsDocument (aAgent));
    LOGGER.info (m.getAsString (aAgent));
  }
}
