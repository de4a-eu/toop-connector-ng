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
package eu.toop.edm.extractor.unmarshaller;

import eu.toop.edm.jaxb.cccev.CCCEVConceptType;
import eu.toop.edm.jaxb.cccev.CCCEVRequirementType;
import eu.toop.edm.jaxb.cv.agent.AgentType;
import eu.toop.edm.jaxb.dcatap.DCatAPDatasetType;
import eu.toop.edm.jaxb.dcatap.DCatAPDistributionType;
import eu.toop.edm.jaxb.w3.cv.ac.CoreBusinessType;
import eu.toop.edm.jaxb.w3.cv.ac.CorePersonType;

public class Unmarshallers
{
  private Unmarshallers ()
  {
    // Prevent utility class instantiation
  }

  public static SlotUnmarshaller <AgentType> getAgentUnmarshaller ()
  {
    return new AgentUnmarshaller ();
  }

  public static SlotUnmarshaller <CorePersonType> getPersonUnmarshaller ()
  {
    return new PersonUnmarshaller ();
  }

  public static SlotUnmarshaller <CoreBusinessType> getBusinessUnmarshaller ()
  {
    return new BusinessUnmarshaller ();
  }

  public static SlotUnmarshaller <CCCEVConceptType> getConceptUnmarshaller ()
  {
    return new ConceptUnmarshaller ();
  }

  public static SlotUnmarshaller <DCatAPDistributionType> getDistributionUnmarshaller ()
  {
    return new DistributionUnmarshaller ();
  }

  public static SlotUnmarshaller <DCatAPDatasetType> getDatasetUnmarshaller ()
  {
    return new DatasetUnmarshaller ();
  }

  public static SlotUnmarshaller <CCCEVRequirementType> getCCCEVRequirementUnmarshaller ()
  {
    return new FulFillingRequirementUnmarshaller ();
  }
}
