package eu.toop.edm.cv;

import org.w3.ns.corevocabulary.person.CvpersonType;
import org.w3.ns.corevocabulary.person.ObjectFactory;

import com.helger.jaxb.GenericJAXBMarshaller;

public class PersonMarshaller extends GenericJAXBMarshaller <CvpersonType>
{
  public PersonMarshaller ()
  {
    super (CvpersonType.class, CCV.XSDS, x -> new ObjectFactory ().createCvperson (x));
    setNamespaceContext (CCVNamespaceContext.getInstance ());
  }
}
