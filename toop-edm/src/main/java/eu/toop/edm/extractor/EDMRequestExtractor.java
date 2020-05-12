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

import javax.xml.bind.JAXBException;

import com.helger.datetime.util.PDTXMLConverter;

import eu.toop.edm.EDMRequest;
import eu.toop.edm.extractor.unmarshaller.Unmarshallers;
import eu.toop.edm.model.AgentPojo;
import eu.toop.edm.model.BusinessPojo;
import eu.toop.edm.model.ConceptPojo;
import eu.toop.edm.model.DistributionPojo;
import eu.toop.edm.model.EQueryDefinitionType;
import eu.toop.edm.model.PersonPojo;
import eu.toop.edm.slot.SlotAuthorizedRepresentative;
import eu.toop.edm.slot.SlotConceptRequestList;
import eu.toop.edm.slot.SlotConsentToken;
import eu.toop.edm.slot.SlotDataConsumer;
import eu.toop.edm.slot.SlotDataSubjectLegalPerson;
import eu.toop.edm.slot.SlotDataSubjectNaturalPerson;
import eu.toop.edm.slot.SlotDatasetIdentifier;
import eu.toop.edm.slot.SlotDistributionRequestList;
import eu.toop.edm.slot.SlotFullfillingRequirement;
import eu.toop.edm.slot.SlotIssueDateTime;
import eu.toop.edm.slot.SlotProcedure;
import eu.toop.edm.slot.SlotSpecificationIdentifier;
import eu.toop.regrep.query.QueryRequest;
import eu.toop.regrep.rim.AnyValueType;
import eu.toop.regrep.rim.CollectionValueType;
import eu.toop.regrep.rim.DateTimeValueType;
import eu.toop.regrep.rim.InternationalStringValueType;
import eu.toop.regrep.rim.SlotType;
import eu.toop.regrep.rim.StringValueType;

final class EDMRequestExtractor
{

  private EDMRequestExtractor ()
  {

  }

  static EDMRequest extract (final QueryRequest xmlRequest) throws JAXBException
  {
    final EDMRequest.Builder theRequestBuilder = EDMRequest.builder ();
    theRequestBuilder.id (xmlRequest.getId ());

    for (final SlotType slot : xmlRequest.getSlot ())
      applySlots (slot, theRequestBuilder);

    if (xmlRequest.getQuery ().hasSlotEntries ())
      for (final SlotType slot : xmlRequest.getQuery ().getSlot ())
        applySlots (slot, theRequestBuilder);

    return theRequestBuilder.build ();
  }

  private static void applySlots (final SlotType slotType,
                                  final EDMRequest.Builder theRequestBuilder) throws JAXBException
  {
    if (slotType != null && slotType.getName () != null && slotType.getSlotValue () != null)
    {
      switch (slotType.getName ())
      {
        case SlotSpecificationIdentifier.NAME:
          theRequestBuilder.specificationIdentifier (((StringValueType) slotType.getSlotValue ()).getValue ());
          break;
        case SlotIssueDateTime.NAME:
          theRequestBuilder.issueDateTime (PDTXMLConverter.getLocalDateTime (((DateTimeValueType) slotType.getSlotValue ()).getValue ()));
          break;
        case SlotProcedure.NAME:
          theRequestBuilder.procedure (((InternationalStringValueType) slotType.getSlotValue ()).getValue ());
          break;
        case SlotDataConsumer.NAME:
          theRequestBuilder.dataConsumer (AgentPojo.builder (Unmarshallers.getAgentUnmarshaller ()
                                                                          .unmarshal (((AnyValueType) slotType.getSlotValue ()).getAny ()))
                                                   .build ());
          break;
        case SlotConsentToken.NAME:
          theRequestBuilder.consentToken (((StringValueType) slotType.getSlotValue ()).getValue ());
          break;
        case SlotDataSubjectLegalPerson.NAME:
          theRequestBuilder.dataSubject (BusinessPojo.builder (Unmarshallers.getBusinessUnmarshaller ()
                                                                            .unmarshal (((AnyValueType) slotType.getSlotValue ()).getAny ()))
                                                     .build ());
          break;
        case SlotAuthorizedRepresentative.NAME:
          theRequestBuilder.authorizedRepresentative (PersonPojo.builder (Unmarshallers.getPersonUnmarshaller ()
                                                                                       .unmarshal (((AnyValueType) slotType.getSlotValue ()).getAny ()))
                                                                .build ());
          break;
        case SlotDataSubjectNaturalPerson.NAME:
          theRequestBuilder.dataSubject (PersonPojo.builder (Unmarshallers.getPersonUnmarshaller ()
                                                                          .unmarshal (((AnyValueType) slotType.getSlotValue ()).getAny ()))
                                                   .build ());
          break;
        case SlotConceptRequestList.NAME:
          theRequestBuilder.concept (ConceptPojo.builder (Unmarshallers.getConceptUnmarshaller ()
                                                                       .unmarshal (((AnyValueType) ((CollectionValueType) slotType.getSlotValue ()).getElementAtIndex (0)).getAny ()))
                                                .build ());
          theRequestBuilder.queryDefinition (EQueryDefinitionType.CONCEPT);
          break;
        case SlotDistributionRequestList.NAME:
          theRequestBuilder.distribution (DistributionPojo.builder (Unmarshallers.getDistributionUnmarshaller ()
                                                                                 .unmarshal (((AnyValueType) ((CollectionValueType) slotType.getSlotValue ()).getElementAtIndex (0)).getAny ()))
                                                          .build ());
          theRequestBuilder.queryDefinition (EQueryDefinitionType.DOCUMENT);
          break;
        case SlotDatasetIdentifier.NAME:
          theRequestBuilder.datasetIdentifier (((StringValueType) slotType.getSlotValue ()).getValue ());
          break;
        case SlotFullfillingRequirement.NAME:
          theRequestBuilder.fullfillingRequirement (Unmarshallers.getCCCEVRequirementUnmarshaller ()
                                                                 .unmarshal (((AnyValueType) slotType.getSlotValue ()).getAny ()));
          break;
        default:
          throw new IllegalStateException ("Slot is not defined: " + slotType.getName ());
      }
    }
  }
}
