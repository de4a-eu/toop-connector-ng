package eu.toop.edm.cv;

import org.w3.ns.corevocabulary.business.CvbusinessType;
import org.w3.ns.corevocabulary.business.ObjectFactory;

import com.helger.jaxb.GenericJAXBMarshaller;

public class BusinessMarshaller extends GenericJAXBMarshaller <CvbusinessType>
{
  public BusinessMarshaller ()
  {
    super (CvbusinessType.class, CCV.XSDS, x -> new ObjectFactory ().createCvbusiness (x));
    setNamespaceContext (CCVNamespaceContext.getInstance ());
  }
}
