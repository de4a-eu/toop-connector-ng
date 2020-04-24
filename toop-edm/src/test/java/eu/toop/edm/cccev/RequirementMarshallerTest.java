package eu.toop.edm.cccev;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import eu.toop.edm.jaxb.cccev.CCCEVRequirementType;

public final class RequirementMarshallerTest
{
  @Test
  public void testBasic ()
  {
    final CCCEVRequirementType r = new CCCEVRequirementType ();

    final RequirementMarshaller m = new RequirementMarshaller ();
    assertNotNull (m.getAsBytes (r));
  }
}
