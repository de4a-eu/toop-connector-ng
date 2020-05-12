/**
 * Copyright (C) 2018-2020 toop.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.toop.edm.extractor;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.junit.Test;

import com.helger.commons.io.resource.ClassPathResource;

public final class EDMExtractorsTest {

    @Test
    public void testEDMConceptRequestExtract() throws JAXBException, XMLStreamException {
        assertNotNull(EDMExtractors.extractEDMRequest(ClassPathResource.getInputStream("Concept Request_LP.xml")));
    }

    @Test
    public void testEDMDocumentRequestExtract() throws JAXBException, XMLStreamException {
        assertNotNull(EDMExtractors.extractEDMRequest(ClassPathResource.getInputStream("Document Request_NP.xml")));
    }

    @Test
    public void testEDMConceptResponseExtract() throws JAXBException, XMLStreamException {
        assertNotNull(EDMExtractors.extractEDMResponse(ClassPathResource.getInputStream("Concept Response.xml")));
    }

    @Test
    public void testEDMDocumentResponseExtract() throws JAXBException, XMLStreamException {
        assertNotNull(EDMExtractors.extractEDMResponse(ClassPathResource.getInputStream("Document Response.xml")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequestExtractionWithResponseExtractor() throws JAXBException, XMLStreamException {
        EDMExtractors.extractEDMResponse(ClassPathResource.getInputStream("Concept Request_LP.xml"));
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResponseExtractionWithRequestExtractor() throws JAXBException, XMLStreamException {
        EDMExtractors.extractEDMRequest(ClassPathResource.getInputStream("Concept Response.xml"));
        fail();
    }

    @Test(expected = XMLStreamException.class)
    public void testBogusExtractionWithRequestExtractor() throws JAXBException, XMLStreamException {
        EDMExtractors.extractEDMRequest(ClassPathResource.getInputStream("Bogus.xml"));
        fail();
    }

    @Test(expected = XMLStreamException.class)
    public void testBogusExtractionWithResponseExtractor() throws JAXBException, XMLStreamException {
        EDMExtractors.extractEDMResponse(ClassPathResource.getInputStream("Bogus.xml"));
        fail();
    }

}