package eu.toop.edm.model;

import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.time.Month;
import java.util.UUID;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.junit.Test;

import com.helger.commons.datetime.PDTFactory;
import com.helger.commons.io.resource.ClassPathResource;

import eu.toop.edm.EQueryDefinitionType;
import eu.toop.edm.extractor.EDMExtractors;
import eu.toop.edm.pilot.gbm.EToopConcept;
import eu.toop.regrep.ERegRepResponseStatus;

public final class EDMResponseTest {

    @Test
    public void createConceptResponse() {
        EDMResponse res = new EDMResponse.Builder()
                .queryDefinition(EQueryDefinitionType.CONCEPT)
                .requestID(UUID.randomUUID())
                .issueDateTimeNow()
                .concept(ConceptPojo.builder()
                        .id("ConceptID-1")
                        .name(EToopConcept.REGISTERED_ORGANIZATION)
                        .addChild(ConceptPojo.builder()
                                .name(EToopConcept.COMPANY_NAME)
                                .valueText("Helger Enterprises"))
                        .addChild(ConceptPojo.builder()
                                .name(EToopConcept.FAX_NUMBER)
                                .valueText("342342424"))
                        .addChild(ConceptPojo.builder()
                                .name(EToopConcept.FOUNDATION_DATE)
                                .valueDate(PDTFactory.createLocalDate(1960, Month.AUGUST, 12)))
                        .build())
                .dataProvider(AgentPojo.builder()
                        .address(AddressPojo.builder()
                                .town("MyTown")
                                .streetName("MyStreet")
                                .buildingNumber("22")
                                .countryCode("GR")
                                .fullAddress("MyStreet 22, 11134, MyTown, GR")
                                .postalCode("11134").build())
                        .name("DP NAME")
                        .id("1234")
                        .idSchemeID("VAT")
                        .build())
                .responseStatus(ERegRepResponseStatus.SUCCESS)
                .specificationIdentifier("Niar")
                .build();
    }

    @Test
    public void createDocumentResponse() {
        EDMResponse res = new EDMResponse.Builder()
                .queryDefinition(EQueryDefinitionType.DOCUMENT)
                .requestID(UUID.randomUUID())
                .issueDateTimeNow()
                .dataset((DatasetPojo.builder()
                        .description("bla desc")
                        .title("bla title")
                        .distribution(DocumentReferencePojo.builder()
                                .documentURI("URI")
                                .documentDescription("DocumentDescription")
                                .documentType("docType")
                                .localeCode("GR"))
                        .creator(AgentPojo.builder()
                                .name("Agent name")
                                .address(AddressPojo.builder()
                                        .town("Kewlkidshome")))
                        .ids("RE238918378", "DOC-555")
                        .issuedNow()
                        .language("en")
                        .lastModifiedNow()
                        .validFrom(PDTFactory.getCurrentLocalDate()
                                .minusMonths(1))
                        .validTo(PDTFactory.getCurrentLocalDate()
                                .plusYears(1))
                        .qualifiedRelation(QualifiedRelationPojo.builder()
                                .description("LegalResourceDesc")
                                .title("Name")
                                .id("RE238918378")))
                        .build())
                .dataProvider(AgentPojo.builder()
                        .address(AddressPojo.builder()
                                .town("MyTown")
                                .streetName("MyStreet")
                                .buildingNumber("22")
                                .countryCode("GR")
                                .fullAddress("MyStreet 22, 11134, MyTown, GR")
                                .postalCode("11134").build())
                        .name("DP NAME")
                        .id("1234")
                        .idSchemeID("VAT")
                        .build())
                .responseStatus(ERegRepResponseStatus.SUCCESS)
                .specificationIdentifier("Niar")
                .build();
    }

    // This attempts to create an EDMResponse with a dataset element but with ConceptQuery set as the QueryDefinition
    // which is not permitted and fails
    @Test(expected = IllegalStateException.class)
    public void createDocumentResponseWithConceptType() {
        EDMResponse res = new EDMResponse.Builder()
                .queryDefinition(EQueryDefinitionType.CONCEPT)
                .requestID(UUID.randomUUID())
                .issueDateTimeNow()
                .dataset((DatasetPojo.builder()
                        .description("bla desc")
                        .title("bla title")
                        .distribution(DocumentReferencePojo.builder()
                                .documentURI("URI")
                                .documentDescription("DocumentDescription")
                                .documentType("docType")
                                .localeCode("GR"))
                        .creator(AgentPojo.builder()
                                .name("Agent name")
                                .address(AddressPojo.builder()
                                        .town("Kewlkidshome")))
                        .ids("RE238918378", "DOC-555")
                        .issuedNow()
                        .language("en")
                        .lastModifiedNow()
                        .validFrom(PDTFactory.getCurrentLocalDate()
                                .minusMonths(1))
                        .validTo(PDTFactory.getCurrentLocalDate()
                                .plusYears(1))
                        .qualifiedRelation(QualifiedRelationPojo.builder()
                                .description("LegalResourceDesc")
                                .title("Name")
                                .id("RE238918378")))
                        .build())
                .dataProvider(AgentPojo.builder()
                        .address(AddressPojo.builder()
                                .town("MyTown")
                                .streetName("MyStreet")
                                .buildingNumber("22")
                                .countryCode("GR")
                                .fullAddress("MyStreet 22, 11134, MyTown, GR")
                                .postalCode("11134").build())
                        .name("DP NAME")
                        .id("1234")
                        .idSchemeID("VAT")
                        .build())
                .responseStatus(ERegRepResponseStatus.SUCCESS)
                .specificationIdentifier("Niar")
                .build();
    }

    @Test
    public void testInputStreamEDMConceptResponseExport() throws JAXBException, XMLStreamException {
        assertNotNull(EDMExtractors
                .extractEDMResponse(ClassPathResource.getInputStream("Concept Response.xml"))
                .getAsXMLString());
    }


    @Test
    public void testEDMConceptResponseExport() throws JAXBException, XMLStreamException, FileNotFoundException {
        assertNotNull(EDMExtractors
                .extractEDMResponse(ClassPathResource.getAsFile("Concept Response.xml"))
                .getAsXMLString());
    }

    @Test
    public void testEDMDocumentResponseExport() throws JAXBException, XMLStreamException, FileNotFoundException {
        assertNotNull(EDMExtractors
                .extractEDMResponse(ClassPathResource.getAsFile("Document Response.xml"))
                .getAsXMLString());
    }

}