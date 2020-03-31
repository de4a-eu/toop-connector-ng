package eu.toop.edm.dc;

import java.util.List;

import javax.annotation.Nonnull;

import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.jaxb.GenericJAXBMarshaller;
import com.helger.ubl20.CUBL20;

import eu.toop.edm.jaxb.cpsv.helper.AgentType;

public class AgentMarshaller extends GenericJAXBMarshaller <AgentType>
{
  @Nonnull
  private static final ClassLoader _getCL ()
  {
    return AgentMarshaller.class.getClassLoader ();
  }

  private static final List <ClassPathResource> XSDs = new CommonsArrayList <> (CUBL20.XSD_UNQUALIFIED_DATA_TYPES,
                                                                                CUBL20.XSD_QUALIFIED_DATA_TYPES,
                                                                                CUBL20.XSD_COMMON_BASIC_COMPONENTS,
                                                                                CUBL20.XSD_COMMON_EXTENSION_COMPONENTS,
                                                                                new ClassPathResource ("schemas/cpsv-ap_xml_schema_v0.01.xsd",
                                                                                                       _getCL ()),
                                                                                new ClassPathResource ("schemas/CorePOVocabularyBasicComponents-v1.00.xsd",
                                                                                                       _getCL ()),
                                                                                new ClassPathResource ("schemas/CoreLocation-v1.00.xsd",
                                                                                                       _getCL ()),
                                                                                new ClassPathResource ("schemas/CorePerson-v1.00.xsd",
                                                                                                       _getCL ()),
                                                                                new ClassPathResource ("schemas/CoreBusiness-v1.00.xsd",
                                                                                                       _getCL ()),
                                                                                new ClassPathResource ("schemas/CoreVocabularyAggregateComponents-v1.00.xsd",
                                                                                                       _getCL ()),
                                                                                new ClassPathResource ("schemas/cpsv-ap_xml_schema_v0.01.helper.xsd",
                                                                                                       _getCL ()));

  public AgentMarshaller ()
  {
    super (AgentType.class, XSDs, x -> new eu.toop.edm.jaxb.cpsv.helper.ObjectFactory ().createAgent (x));
    setNamespaceContext (CPSVNamespaceContext.getInstance ());
  }
}
