package eu.toop.connector.api.shared;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for class {@link TCSharedJAXB}.
 *
 * @author Philip Helger
 */
public final class TCSharedJAXBTest
{
  @Test
  public void testBasic ()
  {
    assertTrue (TCSharedJAXB.XSD_RES.exists ());
  }
}
