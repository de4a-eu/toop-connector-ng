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
package eu.toop.connector.api.dsd;

import com.helger.commons.collection.impl.CommonsHashSet;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.collection.impl.ICommonsSet;
import com.helger.commons.string.StringHelper;
import eu.toop.connector.api.TCConfig;
import eu.toop.edm.jaxb.cv.agent.PublicOrganizationType;
import eu.toop.edm.jaxb.cv.cbc.IDType;
import eu.toop.edm.jaxb.dcatap.DCatAPDatasetType;

import java.util.List;

/**
 * Contains helper functions for DSD service.
 *
 * @author yerlibilgin
 */
public class DSDDatasetHelper {
  /**
   * Creates a set of {@link DSDDatasetResponse} objects from the given <code>datasetTypesList</code>
   *
   * @param datasetTypesList the list of {@link DCatAPDatasetType} objects
   * @return the set of {@link DSDDatasetResponse} objects
   */
  public static ICommonsSet<DSDDatasetResponse> buildDSDResponseSet(List<DCatAPDatasetType> datasetTypesList) {
    final ICommonsSet<DSDDatasetResponse> ret = new CommonsHashSet<>();
    datasetTypesList.forEach(d -> {
      d.getDistribution().forEach(dist -> {
        final DSDDatasetResponse resp = new DSDDatasetResponse();
        // Access Service Conforms To
        if (dist.getAccessService().hasConformsToEntries())
          resp.setAccessServiceConforms(dist.getAccessService().getConformsToAtIndex(0).getValue());

        // DP Identifier
        final IDType aDPID = ((PublicOrganizationType) d.getPublisherAtIndex(0)).getIdAtIndex(0);
        resp.setDPIdentifier(TCConfig.getIdentifierFactory().createParticipantIdentifier(aDPID.getSchemeName(), aDPID.getValue()));

        // Access Service Identifier, used as Document Type ID
        final ICommonsList<String> aDTParts = StringHelper.getExploded("::", dist.getAccessService().getIdentifier(), 2);
        if (aDTParts.size() == 2)
          resp.setDocumentTypeIdentifier(TCConfig.getIdentifierFactory()
              .createDocumentTypeIdentifier(aDTParts.get(0), aDTParts.get(1)));

        resp.setDatasetIdentifier(d.getIdentifierAtIndex(0));
        if (dist.hasConformsToEntries())
          resp.setDistributionConforms(dist.getConformsToAtIndex(0).getValue());

        resp.setDistributionFormat(dist.getFormat().getContentAtIndex(0).toString());
        ret.add(resp);
      });
    });

    return ret;
  }
}
