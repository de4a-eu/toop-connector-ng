package eu.toop.edm;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.helger.commons.collection.impl.ICommonsList;
import com.helger.schematron.svrl.AbstractSVRLMessage;

import eu.toop.edm.error.EEDMExceptionType;
import eu.toop.edm.error.EToopErrorOrigin;
import eu.toop.edm.error.EToopErrorSeverity;
import eu.toop.edm.schematron.SchematronEDM2Validator;
import eu.toop.regrep.RegRep4Writer;
import eu.toop.regrep.rs.RegistryExceptionType;

/**
 * Test class for class {@link EDMExceptionBuilder}.
 *
 * @author Philip Helger
 */
public final class EDMExceptionBuilderTest
{
  private static final Logger LOGGER = LoggerFactory.getLogger (EDMExceptionBuilderTest.class);

  @Test
  public void testBasic ()
  {
    for (final EEDMExceptionType e : EEDMExceptionType.values ())
    {
      final EDMExceptionBuilder b = new EDMExceptionBuilder ().exceptionType (e)
                                                              .errorCode ("ec1")
                                                              .errorDetail ("Stacktrace")
                                                              .errorMessage ("What went wrong: nothing")
                                                              .severity (EToopErrorSeverity.FAILURE)
                                                              .publicOrganizationID ("voc", "term")
                                                              .timestampNow ()
                                                              .origin (EToopErrorOrigin.RESPONSE_RECEPTION)
                                                              .errorCategory ("Category");
      final RegistryExceptionType aEx = b.build ();
      assertNotNull (aEx);

      final RegRep4Writer <RegistryExceptionType> aWriter = RegRep4Writer.registryException ()
                                                                         .setFormattedOutput (true);
      final String sXML = aWriter.getAsString (aEx);
      assertNotNull (sXML);

      if (false)
        LOGGER.info (sXML);

      if (false)
      {
        // Schematron validation
        final Document aDoc = aWriter.getAsDocument (aEx);
        assertNotNull (aDoc);
        final ICommonsList <AbstractSVRLMessage> aMsgs = new SchematronEDM2Validator ().validateDocument (aDoc);
        assertTrue (aMsgs.toString (), aMsgs.isEmpty ());
      }
    }
  }

  @Test
  public void testMinimum ()
  {
    final EDMExceptionBuilder b = new EDMExceptionBuilder ().exceptionType (EEDMExceptionType.OBJECT_NOT_FOUND)
                                                            .errorCode ("ec1")
                                                            .errorMessage ("What went wrong: nothing")
                                                            .severity (EToopErrorSeverity.FAILURE)
                                                            .timestampNow ()
                                                            .origin (EToopErrorOrigin.RESPONSE_RECEPTION)
                                                            .errorCategory ("Category");
    final RegistryExceptionType aEx = b.build ();
    assertNotNull (aEx);

    final RegRep4Writer <RegistryExceptionType> aWriter = RegRep4Writer.registryException ().setFormattedOutput (true);
    final String sXML = aWriter.getAsString (aEx);
    assertNotNull (sXML);

    if (true)
      LOGGER.info (sXML);

    if (false)
    {
      // Schematron validation
      final Document aDoc = aWriter.getAsDocument (aEx);
      assertNotNull (aDoc);
      final ICommonsList <AbstractSVRLMessage> aMsgs = new SchematronEDM2Validator ().validateDocument (aDoc);
      assertTrue (aMsgs.toString (), aMsgs.isEmpty ());
    }
  }
}
