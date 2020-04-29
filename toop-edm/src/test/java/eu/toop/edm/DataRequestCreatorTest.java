package eu.toop.edm;

import static org.junit.Assert.assertNotNull;

import java.util.Locale;

import org.junit.Test;

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
                                                    .setDataConsumer (AgentPojo.createMinimum ())
                                                    .setDataSubject (PersonPojo.createMinimum ())
                                                    .setAuthorizedRepresentative (PersonPojo.createMinimum ())
                                                    .build ();
    assertNotNull (aRequest);
    final String sXML = RegRep4Writer.queryRequest ().setFormattedOutput (true).getAsString (aRequest);
    System.out.println (sXML);
  }
}
