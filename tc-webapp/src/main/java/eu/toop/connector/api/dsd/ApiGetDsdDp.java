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

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.collection.impl.ICommonsSet;
import com.helger.commons.datetime.PDTFactory;
import com.helger.commons.string.StringHelper;
import com.helger.commons.timing.StopWatch;
import com.helger.json.IJsonObject;
import com.helger.json.JsonArray;
import com.helger.json.JsonObject;
import com.helger.peppolid.IParticipantIdentifier;
import com.helger.photon.api.IAPIDescriptor;
import com.helger.photon.api.IAPIExecutor;
import com.helger.photon.app.PhotonUnifiedResponse;
import com.helger.servlet.response.UnifiedResponse;
import com.helger.web.scope.IRequestWebScopeWithoutResponse;

import eu.toop.connector.api.APIParamException;
import eu.toop.connector.api.smp.LoggingSMPErrorHandler;
import eu.toop.connector.app.dsd.DSDParticipantIDProviderRemote;

public class ApiGetDsdDp implements IAPIExecutor
{
  public void invokeAPI (@Nonnull final IAPIDescriptor aAPIDescriptor,
                         @Nonnull @Nonempty final String sPath,
                         @Nonnull final Map <String, String> aPathVariables,
                         @Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                         @Nonnull final UnifiedResponse aUnifiedResponse) throws Exception
  {
    final String sDatasetType = aPathVariables.get ("dataset");
    if (StringHelper.hasNoText (sDatasetType))
      throw new APIParamException ("Missing DatasetType");

    final ZonedDateTime aQueryDT = PDTFactory.getCurrentZonedDateTimeUTC ();
    final StopWatch aSW = StopWatch.createdStarted ();

    final ICommonsSet <IParticipantIdentifier> aParticipants = new DSDParticipantIDProviderRemote ().getAllParticipantIDs ("[api /dsd/dp]",
                                                                                                                           sDatasetType,
                                                                                                                           null,
                                                                                                                           null,
                                                                                                                           LoggingSMPErrorHandler.INSTANCE);

    final IJsonObject aJson = new JsonObject ();
    final JsonArray aList = new JsonArray ();
    for (final IParticipantIdentifier aPI : aParticipants)
      aList.add (new JsonObject ().add ("scheme", aPI.getScheme ()).add ("value", aPI.getValue ()));
    aJson.add ("participants", aList);
    aJson.add ("queryDateTime", DateTimeFormatter.ISO_ZONED_DATE_TIME.format (aQueryDT));
    aJson.add ("queryDurationMillis", aSW.getMillis ());

    ((PhotonUnifiedResponse) aUnifiedResponse).json (aJson);
  }
}
