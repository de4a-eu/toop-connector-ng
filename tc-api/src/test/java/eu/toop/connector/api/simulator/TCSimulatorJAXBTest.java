package eu.toop.connector.api.simulator;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for class {@link TCSimulatorJAXB}.
 *
 * @author Philip Helger
 */
public final class TCSimulatorJAXBTest
{
  @Test
  public void testBasic ()
  {
    assertTrue (TCSimulatorJAXB.XSD_RES.exists ());
  }
}
