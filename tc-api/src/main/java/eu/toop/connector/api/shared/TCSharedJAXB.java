package eu.toop.connector.api.shared;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.jaxb.GenericJAXBMarshaller;

/**
 * JAXB helper for shared TC NG classes
 *
 * @author Philip Helger
 */
@Immutable
public final class TCSharedJAXB
{
  public static final ClassPathResource XSD_RES = new ClassPathResource ("/schemas/tc-shared.xsd", TCSharedJAXB.class.getClassLoader ());

  private TCSharedJAXB ()
  {}

  @Nonnull
  @ReturnsMutableCopy
  public static ICommonsList <ClassPathResource> getAllXSDResources ()
  {
    final ICommonsList <ClassPathResource> ret = new CommonsArrayList <> ();
    ret.add (XSD_RES);
    return ret;
  }

  @Nonnull
  public static GenericJAXBMarshaller <TCOutgoingMessage> outgoingMessage ()
  {
    return new GenericJAXBMarshaller <> (TCOutgoingMessage.class, getAllXSDResources (), new ObjectFactory ()::createOutgoingMessage);
  }
}
