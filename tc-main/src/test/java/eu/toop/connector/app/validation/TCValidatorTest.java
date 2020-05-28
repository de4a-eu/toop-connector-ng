package eu.toop.connector.app.validation;

import static org.junit.Assert.assertTrue;

import java.util.Locale;

import javax.annotation.Nonnull;

import org.junit.Test;

import com.helger.bdve.executorset.VESID;
import com.helger.bdve.result.ValidationResultList;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.io.stream.StreamHelper;

/**
 * Test class for class {@link TCValidator}
 *
 * @author Philip Helger
 */
public final class TCValidatorTest
{
  @Nonnull
  private static ValidationResultList _validate (@Nonnull final VESID aVESID, @Nonnull final String sFilename)
  {
    return new TCValidator ().validate (aVESID, StreamHelper.getAllBytes (new ClassPathResource ("edm/" + sFilename)), Locale.US);
  }

  private static void _assumeSuccess (@Nonnull final ValidationResultList aRL)
  {
    // No errors and no warnings
    assertTrue ("Hmpf: " + aRL.toString (), aRL.containsNoFailure ());
  }

  private static void _assumeError (@Nonnull final ValidationResultList aRL)
  {
    // At least one error
    assertTrue ("Hmpf: " + aRL.toString (), aRL.containsAtLeastOneError ());
  }

  @Test
  public void testRequest ()
  {
    final VESID aVESID = TCValidationRules.VID_TOOP_EDM_REQUEST_200;
    _assumeSuccess (_validate (aVESID, "Concept Request_LP.xml"));
    _assumeSuccess (_validate (aVESID, "Concept Request_NP.xml"));
    _assumeError (_validate (aVESID, "Concept Response.xml"));
    _assumeSuccess (_validate (aVESID, "Document Request_LP.xml"));
    _assumeSuccess (_validate (aVESID, "Document Request_NP.xml"));
    _assumeError (_validate (aVESID, "Document Response.xml"));
    _assumeError (_validate (aVESID, "Error Response 1.xml"));
    _assumeError (_validate (aVESID, "Bogus.xml"));
  }

  @Test
  public void testResponse ()
  {
    final VESID aVESID = TCValidationRules.VID_TOOP_EDM_RESPONSE_200;
    _assumeError (_validate (aVESID, "Concept Request_LP.xml"));
    _assumeError (_validate (aVESID, "Concept Request_NP.xml"));
    _assumeSuccess (_validate (aVESID, "Concept Response.xml"));
    _assumeError (_validate (aVESID, "Document Request_LP.xml"));
    _assumeError (_validate (aVESID, "Document Request_NP.xml"));
    _assumeSuccess (_validate (aVESID, "Document Response.xml"));
    _assumeError (_validate (aVESID, "Error Response 1.xml"));
    _assumeError (_validate (aVESID, "Bogus.xml"));
  }

  @Test
  public void testErrorResponse ()
  {
    final VESID aVESID = TCValidationRules.VID_TOOP_EDM_ERROR_RESPONSE_200;
    _assumeError (_validate (aVESID, "Concept Request_LP.xml"));
    _assumeError (_validate (aVESID, "Concept Request_NP.xml"));
    _assumeError (_validate (aVESID, "Concept Response.xml"));
    _assumeError (_validate (aVESID, "Document Request_LP.xml"));
    _assumeError (_validate (aVESID, "Document Request_NP.xml"));
    _assumeError (_validate (aVESID, "Document Response.xml"));
    _assumeSuccess (_validate (aVESID, "Error Response 1.xml"));
    _assumeError (_validate (aVESID, "Bogus.xml"));
  }
}
