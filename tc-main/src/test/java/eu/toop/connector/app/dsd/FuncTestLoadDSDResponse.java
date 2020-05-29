package eu.toop.connector.app.dsd;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.helger.commons.io.resource.FileSystemResource;
import com.helger.xml.XMLHelper;
import com.helger.xml.serialize.read.DOMReader;

import eu.toop.edm.jaxb.dcatap.DCatAPDatasetType;
import eu.toop.edm.xml.dcatap.DatasetMarshaller;

public class FuncTestLoadDSDResponse
{
  @Test
  public void testRead ()
  {
    final Document aDoc = DOMReader.readXMLDOM (new FileSystemResource ("src/test/resources/dsd/dsd-response1.xml"));
    final Element aROL = XMLHelper.getFirstChildElementOfName (aDoc.getDocumentElement (), "RegistryObjectList");
    final Element aRO = XMLHelper.getFirstChildElementOfName (aROL, "RegistryObject");
    final Element aSlot = XMLHelper.getFirstChildElementOfName (aRO, "Slot");
    final Element aSlotValue = XMLHelper.getFirstChildElementOfName (aSlot, "SlotValue");
    assertNotNull (aSlotValue);
    final Element aDataset = XMLHelper.getFirstChildElement (aSlotValue);
    final DCatAPDatasetType aDS = new DatasetMarshaller ().read (aDataset);
    assertNotNull (aDS);
  }
}
