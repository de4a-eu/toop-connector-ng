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
package eu.toop.connector.webapi.smp;

import java.util.Map;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.annotation.Nonempty;
import com.helger.json.IJsonObject;
import com.helger.json.JsonObject;
import com.helger.peppolid.IDocumentTypeIdentifier;
import com.helger.peppolid.IParticipantIdentifier;
import com.helger.photon.api.IAPIDescriptor;
import com.helger.web.scope.IRequestWebScopeWithoutResponse;
import com.helger.xsds.bdxr.smp1.ServiceMetadataType;

import eu.toop.connector.api.TCConfig;
import eu.toop.connector.app.api.TCAPIConfig;
import eu.toop.connector.webapi.APIParamException;
import eu.toop.connector.webapi.helper.AbstractTCAPIInvoker;
import eu.toop.connector.webapi.helper.CommonAPIInvoker;

/**
 * Query all matching endpoints from an SMP
 *
 * @author Philip Helger
 */
public class ApiGetSmpEndpoints extends AbstractTCAPIInvoker
{
  private static final Logger LOGGER = LoggerFactory.getLogger (ApiGetSmpEndpoints.class);

  @Override
  public IJsonObject invokeAPI (@Nonnull final IAPIDescriptor aAPIDescriptor,
                                @Nonnull @Nonempty final String sPath,
                                @Nonnull final Map <String, String> aPathVariables,
                                @Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
  {
    final String sParticipantID = aPathVariables.get ("pid");
    final IParticipantIdentifier aParticipantID = TCConfig.getIdentifierFactory ().parseParticipantIdentifier (sParticipantID);
    if (aParticipantID == null)
      throw new APIParamException ("Invalid participant ID '" + sParticipantID + "' provided.");

    final String sDocTypeID = aPathVariables.get ("doctypeid");
    final IDocumentTypeIdentifier aDocTypeID = TCConfig.getIdentifierFactory ().parseDocumentTypeIdentifier (sDocTypeID);
    if (aDocTypeID == null)
      throw new APIParamException ("Invalid document type ID '" + sDocTypeID + "' provided.");

    LOGGER.info ("[API] Participant information of '" +
                 aParticipantID.getURIEncoded () +
                 "' is queried for document type '" +
                 aDocTypeID.getURIEncoded () +
                 "'");

    final IJsonObject aJson = new JsonObject ();
    aJson.add (SMPJsonResponse.JSON_PARTICIPANT_ID, aParticipantID.getURIEncoded ());
    aJson.add (SMPJsonResponse.JSON_DOCUMENT_TYPE_ID, aDocTypeID.getURIEncoded ());
    CommonAPIInvoker.invoke (aJson, () -> {
      // Main query
      final ServiceMetadataType aSM = TCAPIConfig.getDDServiceMetadataProvider ().getServiceMetadata (aParticipantID, aDocTypeID);
      if (aSM != null)
      {
        aJson.add (JSON_SUCCESS, true);
        aJson.addJson ("response", SMPJsonResponse.convert (aParticipantID, aDocTypeID, aSM));
      }
      else
        aJson.add (JSON_SUCCESS, false);
    });

    return aJson;
  }
}
