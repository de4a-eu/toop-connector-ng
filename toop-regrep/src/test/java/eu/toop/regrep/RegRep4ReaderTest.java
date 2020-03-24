package eu.toop.regrep;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import eu.toop.regrep.query.QueryRequest;
import eu.toop.regrep.query.QueryResponse;
import eu.toop.regrep.rs.AuthorizationExceptionType;
import eu.toop.regrep.rs.RegistryExceptionType;

/**
 * Test class for class {@link RegRep4Reader}.
 *
 * @author Philip Helger
 */
public final class RegRep4ReaderTest
{
  @Test
  public void testQueryRequest ()
  {
    QueryRequest qr = RegRep4Reader.queryRequest ().read (new File ("src/test/resources/examples/Data Request.xml"));
    assertNotNull (qr);

    assertNotNull (RegRep4Writer.queryRequest ().getAsBytes (qr));

    qr = RegRep4Reader.queryRequest ().read (new File ("src/test/resources/examples/Document Request.xml"));
    assertNotNull (qr);

    assertNotNull (RegRep4Writer.queryRequest ().getAsBytes (qr));
  }

  @Test
  public void testQueryResponse ()
  {
    final QueryResponse qr = RegRep4Reader.queryResponse ()
                                          .read (new File ("src/test/resources/examples/Data Response.xml"));
    assertNotNull (qr);

    assertNotNull (RegRep4Writer.queryResponse ().getAsBytes (qr));
  }

  @Test
  public void testRegistryException ()
  {
    final RegistryExceptionType re = RegRep4Reader.registryException ()
                                                  .read (new File ("src/test/resources/examples/Exception.xml"));
    assertNotNull (re);
    assertTrue (re instanceof AuthorizationExceptionType);

    assertEquals ("DD-004", re.getCode ());

    assertNotNull (RegRep4Writer.registryException ().getAsBytes (re));
  }
}
