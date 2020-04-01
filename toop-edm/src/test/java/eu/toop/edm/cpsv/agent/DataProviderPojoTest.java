package eu.toop.edm.cpsv.agent;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.toop.edm.jaxb.cpsv.helper.AgentType;

/**
 * Test class for class {@link DataProviderPojo}
 *
 * @author Philip Helger
 */
public final class DataProviderPojoTest
{
  private static final Logger LOGGER = LoggerFactory.getLogger (DataProviderPojoTest.class);

  @Test
  public void testBasic ()
  {
    final DataProviderPojo x = new DataProviderPojo ("ID", "IDType", "Name");
    final AgentType aAgent = x.getAsAgent ();
    assertNotNull (aAgent);

    final AgentMarshaller m = new AgentMarshaller ();
    m.setFormattedOutput (true);
    assertNotNull (m.getAsDocument (aAgent));
    LOGGER.info (m.getAsString (aAgent));
  }
}
