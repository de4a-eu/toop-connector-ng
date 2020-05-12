package eu.toop.edm.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.time.Month;
import java.util.Locale;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.junit.Test;

import com.helger.commons.datetime.PDTFactory;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.mime.CMimeType;

import eu.toop.edm.EQueryDefinitionType;
import eu.toop.edm.extractor.EDMExtractors;
import eu.toop.edm.pilot.gbm.EToopConcept;

public final class EDMRequestTest {

    @Test
    public void createEDMDocumentRequestNP() {
        EDMRequest request = new EDMRequest.Builder()
                .randomID()
                .queryDefinition(EQueryDefinitionType.DOCUMENT)
                .issueDateTimeNow()
                .procedure(Locale.US, "GBM Procedure")
                .dataConsumer(AgentPojo.builder()
                        .address(AddressPojo.builder()
                                .town("MyTown")
                                .streetName("MyStreet")
                                .buildingNumber("22")
                                .countryCode("GR")
                                .fullAddress("MyStreet 22, 11134, MyTown, GR")
                                .postalCode("11134").build())
                        .name("DC NAME")
                        .id("1234")
                        .idSchemeID("VAT")
                        .build())
                .authorizedRepresentative(PersonPojo.builder()
                        .address(
                                AddressPojo.builder()
                                        .town("MyTown")
                                        .streetName("MyStreet")
                                        .buildingNumber("22")
                                        .countryCode("GR")
                                        .fullAddress("MyStreet 22, 11134, MyTown, GR")
                                        .postalCode("11134").build())
                        .birthDate(PDTFactory.createLocalDate(1994, Month.FEBRUARY, 1))
                        .birthTown("ATown")
                        .birthName("John Doe")
                        .familyName("Doe")
                        .genderCode(EGenderCode.M)
                        .givenName("John")
                        .id("LALALA")
                        .idSchemeID("LALALA")
                        .build())
                .dataSubject(PersonPojo.builder()
                        .address(
                                AddressPojo.builder()
                                        .town("MyTown")
                                        .streetName("MyStreet")
                                        .buildingNumber("22")
                                        .countryCode("GR")
                                        .fullAddress("MyStreet 22, 11134, MyTown, GR")
                                        .postalCode("11134").build())
                        .birthDate(PDTFactory.createLocalDate(1994, Month.FEBRUARY, 1))
                        .birthTown("ATown")
                        .birthName("John Doe")
                        .familyName("Doe")
                        .genderCode(EGenderCode.M)
                        .givenName("John")
                        .id("LALALA")
                        .idSchemeID("LALALA")
                        .build())
                .datasetIdentifier("IdentifierForDatasets")
                .specificationIdentifier("SpecID")
                .consentToken("AAABBB")
                .distribution(DistributionPojo.builder()
                        .format(EDistributionFormat.STRUCTURED)
                        .mediaType(CMimeType.APPLICATION_PDF).build())
                .build();
    }

    @Test
    public void createEDMConceptRequestLP() {
        EDMRequest request = new EDMRequest.Builder()
                .randomID()
                .queryDefinition(EQueryDefinitionType.CONCEPT)
                .issueDateTimeNow()
                .procedure(Locale.US, "GBM Procedure")
                .dataConsumer(AgentPojo.builder()
                        .address(AddressPojo.builder()
                                .town("MyTown")
                                .streetName("MyStreet")
                                .buildingNumber("22")
                                .countryCode("GR")
                                .fullAddress("MyStreet 22, 11134, MyTown, GR")
                                .postalCode("11134").build())
                        .name("DC NAME")
                        .id("1234")
                        .idSchemeID("VAT")
                        .build())
                .authorizedRepresentative(PersonPojo.builder()
                        .address(
                                AddressPojo.builder()
                                        .town("MyTown")
                                        .streetName("MyStreet")
                                        .buildingNumber("22")
                                        .countryCode("GR")
                                        .fullAddress("MyStreet 22, 11134, MyTown, GR")
                                        .postalCode("11134").build())
                        .birthDate(PDTFactory.createLocalDate(1994, Month.FEBRUARY, 1))
                        .birthTown("ATown")
                        .birthName("John Doe")
                        .familyName("Doe")
                        .genderCode(EGenderCode.M)
                        .givenName("John")
                        .id("LALALA")
                        .idSchemeID("LALALA")
                        .build())
                .dataSubject(BusinessPojo.builder()
                        .address(
                                AddressPojo.builder()
                                        .town("MyTown")
                                        .streetName("MyStreet")
                                        .buildingNumber("22")
                                        .countryCode("GR")
                                        .fullAddress("MyStreet 22, 11134, MyTown, GR")
                                        .postalCode("11134").build())
                        .legalID("Niar")
                        .legalIDSchemeID("Tsiou")
                        .legalName("NiarTsiou")
                        .id("anID")
                        .build())
                .datasetIdentifier("IdentifierForDatasets")
                .specificationIdentifier("SpecID")
                .consentToken("AAABBB")
                .concept(ConceptPojo.builder()
                        .name(EToopConcept.COMPANY_TYPE)
                        .addChild(ConceptPojo.builder()
                                .name(EToopConcept.COMPANY_NAME))
                        .addChild(ConceptPojo.builder()
                                .name(EToopConcept.COMPANY_CODE))
                        .addChild(ConceptPojo.builder()
                                .name(EToopConcept.COMPANY_TYPE))
                        .build())
                .build();
    }

    // This attempts to create an EDMRequest with both concept and distribution which is not permitted and fails
    @Test(expected = IllegalStateException.class)
    public void createInvalidEDMRequest() {
        EDMRequest request = new EDMRequest.Builder()
                .queryDefinition(EQueryDefinitionType.CONCEPT)
                .issueDateTimeNow()
                .dataConsumer(AgentPojo.builder()
                        .address(AddressPojo.builder()
                                .town("MyTown")
                                .streetName("MyStreet")
                                .buildingNumber("22")
                                .countryCode("GR")
                                .fullAddress("MyStreet 22, 11134, MyTown, GR")
                                .postalCode("11134").build())
                        .name("DC NAME")
                        .id("1234")
                        .idSchemeID("VAT")
                        .build())
                .authorizedRepresentative(PersonPojo.builder()
                        .address(
                                AddressPojo.builder()
                                        .town("MyTown")
                                        .streetName("MyStreet")
                                        .buildingNumber("22")
                                        .countryCode("GR")
                                        .fullAddress("MyStreet 22, 11134, MyTown, GR")
                                        .postalCode("11134").build())
                        .birthDate(PDTFactory.createLocalDate(1994, Month.FEBRUARY, 1))
                        .birthTown("ATown")
                        .birthName("John Doe")
                        .familyName("Doe")
                        .genderCode(EGenderCode.M)
                        .givenName("John")
                        .id("LALALA")
                        .idSchemeID("LALALA")
                        .build())
                .dataSubject(PersonPojo.builder()
                        .address(
                                AddressPojo.builder()
                                        .town("MyTown")
                                        .streetName("MyStreet")
                                        .buildingNumber("22")
                                        .countryCode("GR")
                                        .fullAddress("MyStreet 22, 11134, MyTown, GR")
                                        .postalCode("11134").build())
                        .birthDate(PDTFactory.createLocalDate(1994, Month.FEBRUARY, 1))
                        .birthTown("ATown")
                        .birthName("John Doe")
                        .familyName("Doe")
                        .genderCode(EGenderCode.M)
                        .givenName("John")
                        .id("LALALA")
                        .idSchemeID("LALALA")
                        .build())
                .datasetIdentifier("IdentifierForDatasets")
                .specificationIdentifier("SpecID")
                .consentToken("AAABBB")
                .distribution(DistributionPojo.builder()
                        .format(EDistributionFormat.STRUCTURED)
                        .mediaType(CMimeType.APPLICATION_PDF).build())
                .concept(ConceptPojo.builder()
                        .name(EToopConcept.COMPANY_TYPE)
                        .addChild(ConceptPojo.builder()
                                .name(EToopConcept.COMPANY_NAME))
                        .addChild(ConceptPojo.builder()
                                .name(EToopConcept.COMPANY_CODE))
                        .addChild(ConceptPojo.builder()
                                .name(EToopConcept.COMPANY_TYPE))
                        .build())
                .build();
    }


    @Test
    public void testInputStreamExport() throws JAXBException, XMLStreamException {
        assertNotNull(EDMExtractors
                .extractEDMRequest(ClassPathResource.getInputStream("Concept Request_LP.xml"))
                .getAsXMLString());
    }

    @Test
    public void testEDMConceptRequestExport() throws JAXBException, XMLStreamException, FileNotFoundException {
        assertNotNull(EDMExtractors
                .extractEDMRequest(ClassPathResource.getAsFile("Concept Request_LP.xml"))
                .getAsXMLString());
    }

    @Test
    public void testEDMDocumentRequestExport() throws JAXBException, XMLStreamException, FileNotFoundException {
        assertNotNull(EDMExtractors
                .extractEDMRequest(ClassPathResource.getAsFile("Document Request_NP.xml"))
                .getAsXMLString());
    }

    @Test
    public void checkConsistencyConceptRequest() throws JAXBException, XMLStreamException, FileNotFoundException {
        String XMLRequest = EDMExtractors
                .extractEDMRequest(ClassPathResource.getAsFile("Concept Request_NP.xml"))
                .getAsXMLString();

        assertNotNull(XMLRequest);
        assertEquals(XMLRequest, EDMExtractors.extractEDMRequest(XMLRequest).getAsXMLString());
    }

    @Test
    public void checkConsistencyDocumentRequest() throws JAXBException, XMLStreamException, FileNotFoundException {
        String XMLRequest = EDMExtractors
                .extractEDMRequest(ClassPathResource.getAsFile("Document Request_LP.xml"))
                .getAsXMLString();

        assertNotNull(XMLRequest);
        assertEquals(XMLRequest, EDMExtractors.extractEDMRequest(XMLRequest).getAsXMLString());
    }
}