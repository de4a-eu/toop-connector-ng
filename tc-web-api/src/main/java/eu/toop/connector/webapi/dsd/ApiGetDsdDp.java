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
package eu.toop.connector.webapi.dsd;

import java.util.Map;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.collection.impl.ICommonsSet;
import com.helger.commons.string.StringHelper;
import com.helger.json.IJsonObject;
import com.helger.json.JsonArray;
import com.helger.json.JsonObject;
import com.helger.photon.api.IAPIDescriptor;
import com.helger.photon.app.PhotonUnifiedResponse;
import com.helger.web.scope.IRequestWebScopeWithoutResponse;

import eu.toop.connector.api.dsd.DSDDatasetResponse;
import eu.toop.connector.api.error.ITCErrorHandler;
import eu.toop.connector.webapi.APIParamException;
import eu.toop.connector.webapi.TCAPIConfig;
import eu.toop.connector.webapi.helper.AbstractTCAPIInvoker;
import eu.toop.connector.webapi.helper.CommonAPIInvoker;

/**
 * Search DSD participants by dataset only. <br>
 * Was disabled for RC1
 *
 * @author Philip Helger
 */
public class ApiGetDsdDp extends AbstractTCAPIInvoker
{
  @Override
  public void invokeAPI (@Nonnull final IAPIDescriptor aAPIDescriptor,
                         @Nonnull @Nonempty final String sPath,
                         @Nonnull final Map <String, String> aPathVariables,
                         @Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                         @Nonnull final PhotonUnifiedResponse aUnifiedResponse)
  {
    final String sDatasetType = aPathVariables.get ("dataset");
    if (StringHelper.hasNoText (sDatasetType))
      throw new APIParamException ("Missing DatasetType");

    final IJsonObject aJson = new JsonObject ();
    CommonAPIInvoker.invoke (aJson, () -> {
      final ICommonsList <String> aErrorMsgs = new CommonsArrayList <> ();
      final ITCErrorHandler aErrorHdl = (eErrorLevel, sMsg, t, eCode) -> {
        if (eErrorLevel.isError ())
          aErrorMsgs.add (sMsg);
      };
      // Query DSD
      final ICommonsSet <DSDDatasetResponse> aResponses = TCAPIConfig.getDSDDatasetResponseProvider ()
                                                                     .getAllDatasetResponses ("[api /dsd/dp]",
                                                                                              sDatasetType,
                                                                                              null,
                                                                                              aErrorHdl);

      if (aErrorMsgs.isEmpty ())
      {
        aJson.add ("success", true);

        final JsonArray aList = new JsonArray ();
        for (final DSDDatasetResponse aResponse : aResponses)
          aList.add (aResponse.getAsJson ());
        aJson.addJson ("dataset-responses", aList);
      }
      else
      {
        aJson.add ("success", false);
        aJson.addJson ("errors", new JsonArray ().addAll (aErrorMsgs));
      }
    });

    aUnifiedResponse.json (aJson);
  }
}
