package eu.toop.connector.api.me.incoming;

import org.junit.Test;

import com.helger.commons.mock.CommonsTestHelper;
import com.helger.peppolid.factory.IIdentifierFactory;

import eu.toop.connector.api.TCConfig;

/**
 * Test class for class {@link MEIncomingTransportMetadata}.
 *
 * @author Philip Helger
 */
public final class MEIncomingTransportMetadataTest
{
  @Test
  public void testEqualsHashcode ()
  {
    final IIdentifierFactory aIF = TCConfig.getIdentifierFactory ();

    MEIncomingTransportMetadata m = new MEIncomingTransportMetadata (null, null, null, null);
    CommonsTestHelper.testDefaultImplementationWithEqualContentObject (m, new MEIncomingTransportMetadata (null, null, null, null));

    m = new MEIncomingTransportMetadata (aIF.createParticipantIdentifierWithDefaultScheme ("bla"), null, null, null);
    CommonsTestHelper.testDefaultImplementationWithEqualContentObject (m,
                                                                       new MEIncomingTransportMetadata (aIF.createParticipantIdentifierWithDefaultScheme ("bla"),
                                                                                                        null,
                                                                                                        null,
                                                                                                        null));

    m = new MEIncomingTransportMetadata (null, aIF.createParticipantIdentifierWithDefaultScheme ("bla"), null, null);
    CommonsTestHelper.testDefaultImplementationWithEqualContentObject (m,
                                                                       new MEIncomingTransportMetadata (null,
                                                                                                        aIF.createParticipantIdentifierWithDefaultScheme ("bla"),
                                                                                                        null,
                                                                                                        null));

    m = new MEIncomingTransportMetadata (null, null, aIF.createDocumentTypeIdentifierWithDefaultScheme ("foo"), null);
    CommonsTestHelper.testDefaultImplementationWithEqualContentObject (m,
                                                                       new MEIncomingTransportMetadata (null,
                                                                                                        null,
                                                                                                        aIF.createDocumentTypeIdentifierWithDefaultScheme ("foo"),
                                                                                                        null));

    m = new MEIncomingTransportMetadata (null, null, null, aIF.createProcessIdentifierWithDefaultScheme ("proc"));
    CommonsTestHelper.testDefaultImplementationWithEqualContentObject (m,
                                                                       new MEIncomingTransportMetadata (null,
                                                                                                        null,
                                                                                                        null,
                                                                                                        aIF.createProcessIdentifierWithDefaultScheme ("proc")));

    m = new MEIncomingTransportMetadata (aIF.createParticipantIdentifierWithDefaultScheme ("bla"),
                                         aIF.createParticipantIdentifierWithDefaultScheme ("bla2"),
                                         aIF.createDocumentTypeIdentifierWithDefaultScheme ("foo"),
                                         aIF.createProcessIdentifierWithDefaultScheme ("proc"));
    CommonsTestHelper.testDefaultImplementationWithEqualContentObject (m,
                                                                       new MEIncomingTransportMetadata (aIF.createParticipantIdentifierWithDefaultScheme ("bla"),
                                                                                                        aIF.createParticipantIdentifierWithDefaultScheme ("bla2"),
                                                                                                        aIF.createDocumentTypeIdentifierWithDefaultScheme ("foo"),
                                                                                                        aIF.createProcessIdentifierWithDefaultScheme ("proc")));
  }
}
