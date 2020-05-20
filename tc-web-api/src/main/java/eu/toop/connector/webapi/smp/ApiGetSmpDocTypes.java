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

import com.helger.commons.CGlobal;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.collection.impl.ICommonsSortedMap;
import com.helger.json.IJsonObject;
import com.helger.json.JsonObject;
import com.helger.peppolid.IParticipantIdentifier;
import com.helger.photon.api.IAPIDescriptor;
import com.helger.photon.app.PhotonUnifiedResponse;
import com.helger.web.scope.IRequestWebScopeWithoutResponse;

import eu.toop.connector.api.TCConfig;
import eu.toop.connector.webapi.APIParamException;
import eu.toop.connector.webapi.TCAPIConfig;
import eu.toop.connector.webapi.helper.AbstractTCAPIInvoker;
import eu.toop.connector.webapi.helper.CommonAPIInvoker;

/**
 * Query all document types of a participant
 *
 * @author Philip Helger
 */
public class ApiGetSmpDocTypes extends AbstractTCAPIInvoker
{
  private static final Logger LOGGER = LoggerFactory.getLogger (ApiGetSmpDocTypes.class);

  @Override
  public void invokeAPI (@Nonnull final IAPIDescriptor aAPIDescriptor,
                         @Nonnull @Nonempty final String sPath,
                         @Nonnull final Map <String, String> aPathVariables,
                         @Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                         @Nonnull final PhotonUnifiedResponse aUnifiedResponse)
  {
    final String sParticipantID = aPathVariables.get ("pid");
    final IParticipantIdentifier aParticipantID = TCConfig.getIdentifierFactory ().parseParticipantIdentifier (sParticipantID);
    if (aParticipantID == null)
      throw new APIParamException ("Invalid participant ID '" + sParticipantID + "' provided.");

    LOGGER.info ("[API] Document types of '" + aParticipantID.getURIEncoded () + "' are queried");

    final IJsonObject aJson = new JsonObject ();
    aJson.add (SMPJsonResponse.JSON_PARTICIPANT_ID, aParticipantID.getURIEncoded ());
    CommonAPIInvoker.invoke (aJson, () -> {
      // Query SMP
      final ICommonsSortedMap <String, String> aSGHrefs = TCAPIConfig.getDDServiceGroupHrefProvider ()
                                                                     .getAllServiceGroupHrefs (aParticipantID);

      aJson.add ("success", true);
      aJson.add ("response", SMPJsonResponse.convert (aParticipantID, aSGHrefs, TCConfig.getIdentifierFactory ()));
    });

    aUnifiedResponse.json (aJson);
    aUnifiedResponse.enableCaching (3 * CGlobal.SECONDS_PER_HOUR);
  }
}
