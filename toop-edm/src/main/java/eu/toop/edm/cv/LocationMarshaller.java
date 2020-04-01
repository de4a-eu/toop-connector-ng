package eu.toop.edm.cv;

import org.w3.ns.corevocabulary.location.CvlocationType;
import org.w3.ns.corevocabulary.location.ObjectFactory;

import com.helger.jaxb.GenericJAXBMarshaller;

public class LocationMarshaller extends GenericJAXBMarshaller <CvlocationType>
{
  public LocationMarshaller ()
  {
    super (CvlocationType.class, CCV.XSDS, x -> new ObjectFactory ().createCvlocation (x));
    setNamespaceContext (CCVNamespaceContext.getInstance ());
  }
}
