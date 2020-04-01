package eu.toop.edm.cv.business;

import org.w3.ns.corevocabulary.business.CvbusinessType;
import org.w3.ns.corevocabulary.business.ObjectFactory;

import com.helger.jaxb.GenericJAXBMarshaller;

import eu.toop.edm.cv.CCV;
import eu.toop.edm.cv.CCVNamespaceContext;

public class BusinessMarshaller extends GenericJAXBMarshaller <CvbusinessType>
{
  public BusinessMarshaller ()
  {
    super (CvbusinessType.class, CCV.XSDS, x -> new ObjectFactory ().createCvbusiness (x));
    setNamespaceContext (CCVNamespaceContext.getInstance ());
  }
}
