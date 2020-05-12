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

import javax.xml.bind.JAXBException;

import org.junit.Test;

import com.helger.commons.io.resource.ClassPathResource;

import eu.toop.edm.xml.cccev.CCCEV;
import eu.toop.regrep.RegRep4Reader;

public final class EDMResponseExtractorTest {

    @Test
    public void testExtractConceptResponse() throws JAXBException {
        assertNotNull(EDMResponseExtractor.extract(RegRep4Reader.queryResponse(CCCEV.XSDS)
                .read(ClassPathResource.getInputStream("Concept Response.xml"))));
    }

    @Test
    public void testExtractDocumentResponse() throws JAXBException {
        assertNotNull(EDMResponseExtractor.extract(RegRep4Reader.queryResponse(CCCEV.XSDS)
                .read(ClassPathResource.getInputStream("Document Response.xml"))));
    }
}