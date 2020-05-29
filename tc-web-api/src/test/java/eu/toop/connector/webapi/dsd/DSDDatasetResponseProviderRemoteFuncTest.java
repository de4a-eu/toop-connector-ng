package eu.toop.connector.webapi.dsd;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.helger.commons.collection.impl.ICommonsSet;

import eu.toop.connector.api.dd.LoggingDDErrorHandler;
import eu.toop.connector.api.dsd.DSDDatasetResponse;
import eu.toop.connector.app.dsd.DSDDatasetResponseProviderRemote;

/**
 * Test {@link DSDDatasetResponseProviderRemote} in tc-web-api as well
 *
 * @author Philip Helger
 */
public class DSDDatasetResponseProviderRemoteFuncTest
{
  @Test
  public void testSimple ()
  {
    final ICommonsSet <DSDDatasetResponse> aResp = new DSDDatasetResponseProviderRemote ().getAllDatasetResponses ("test",
                                                                                                                   "REGISTERED_ORGANIZATION_TYPE",
                                                                                                                   null,
                                                                                                                   LoggingDDErrorHandler.INSTANCE);
    assertNotNull (aResp);
  }
}
