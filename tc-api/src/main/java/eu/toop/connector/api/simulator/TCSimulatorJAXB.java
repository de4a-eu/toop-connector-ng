package eu.toop.connector.api.simulator;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.jaxb.GenericJAXBMarshaller;
import com.helger.xsds.bdxr.smp1.CBDXRSMP1;

/**
 * JAXB helper for Simulator classes
 * 
 * @author Philip Helger
 */
@Immutable
public final class TCSimulatorJAXB
{
  public static final ClassPathResource XSD_RES = new ClassPathResource ("/schemas/tc-simulator.xsd",
                                                                         TCSimulatorJAXB.class.getClassLoader ());

  private TCSimulatorJAXB ()
  {}

  @Nonnull
  @ReturnsMutableCopy
  public static ICommonsList <ClassPathResource> getAllXSDResources ()
  {
    final ICommonsList <ClassPathResource> ret = CBDXRSMP1.getAllXSDResources ();
    ret.add (XSD_RES);
    return ret;
  }

  @Nonnull
  public static GenericJAXBMarshaller <CountryAwareServiceMetadataType> countryAwareServiceMetadata ()
  {
    return new GenericJAXBMarshaller <> (CountryAwareServiceMetadataType.class,
                                         getAllXSDResources (),
                                         new ObjectFactory ()::createCountryAwareServiceMetadata);
  }
}
