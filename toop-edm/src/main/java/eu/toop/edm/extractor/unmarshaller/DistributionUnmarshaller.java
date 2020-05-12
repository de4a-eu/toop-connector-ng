package eu.toop.edm.extractor.unmarshaller;

import java.util.Objects;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.w3c.dom.Node;

import eu.toop.edm.jaxb.dcatap.DCatAPDistributionType;

class DistributionUnmarshaller implements SlotUnmarshaller <DCatAPDistributionType>
{
  @Override
  public DCatAPDistributionType unmarshal (Object object) throws JAXBException
  {
    if (Objects.isNull (object))
      return null;
    JAXBContext context = JAXBContext.newInstance (DCatAPDistributionType.class);
    Unmarshaller um = context.createUnmarshaller ();
    JAXBElement <DCatAPDistributionType> root = um.unmarshal ((Node) object, DCatAPDistributionType.class);
    return root.getValue ();
  }
}
