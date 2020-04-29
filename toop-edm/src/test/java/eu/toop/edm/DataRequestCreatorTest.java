package eu.toop.edm;

import static org.junit.Assert.assertNotNull;

import java.time.Month;
import java.util.Locale;

import org.junit.Test;

import com.helger.commons.datetime.PDTFactory;

import eu.toop.edm.model.AddressPojo;
import eu.toop.edm.model.AgentPojo;
import eu.toop.edm.model.PersonPojo;
import eu.toop.regrep.RegRep4Writer;
import eu.toop.regrep.query.QueryRequest;

/**
 * Test class for class {@link DataRequestCreator}
 *
 * @author Philip Helger
 */
public final class DataRequestCreatorTest
{
  @Test
  public void testBasicRequestCreator ()
  {
    final QueryRequest aRequest = DataRequestCreator.builderCeoncept ()
                                                    .setIssueDateTimeNow ()
                                                    .setProcedure (Locale.UK, "I am the producer")
                                                    .setFullfillingRequirement (null)
                                                    .setConsentToken ("I consent")
                                                    .setDatasetIdentifier ("dsID")
                                                    .setDataConsumer (AgentPojo.builder ()
                                                                               .id ("DE730757727")
                                                                               .idSchemeID ("VAT")
                                                                               .name ("Company Name")
                                                                               .address (AddressPojo.builder ()
                                                                                                    .fullAddress ("Prince Street 15")
                                                                                                    .streetName ("Prince Street")
                                                                                                    .buildingNumber ("15")
                                                                                                    .town ("Liverpool")
                                                                                                    .postalCode ("15115")
                                                                                                    .countryCode ("GB"))
                                                                               .build ())
                                                    .setDataSubject (PersonPojo.builder ()
                                                                               .familyName ("DataSubjectFamily")
                                                                               .birthDate (PDTFactory.createLocalDate (2000,
                                                                                                                       Month.FEBRUARY,
                                                                                                                       28))
                                                                               .build ())
                                                    .setAuthorizedRepresentative (PersonPojo.builder ()
                                                                                            .givenName ("Authhorizer")
                                                                                            .build ())
                                                    .build ();
    assertNotNull (aRequest);
    final String sXML = RegRep4Writer.queryRequest ().setFormattedOutput (true).getAsString (aRequest);
    System.out.println (sXML);
  }
}
