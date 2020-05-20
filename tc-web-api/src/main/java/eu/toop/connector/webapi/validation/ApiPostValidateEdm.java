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
package eu.toop.connector.webapi.validation;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.bdve.executorset.VESID;
import com.helger.bdve.json.BDVEJsonHelper;
import com.helger.bdve.result.ValidationResultList;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.io.stream.StreamHelper;
import com.helger.commons.timing.StopWatch;
import com.helger.json.IJsonObject;
import com.helger.json.JsonObject;
import com.helger.photon.api.IAPIDescriptor;
import com.helger.photon.app.PhotonUnifiedResponse;
import com.helger.web.scope.IRequestWebScopeWithoutResponse;

import eu.toop.connector.app.validation.TCValidator;
import eu.toop.connector.webapi.ETCEdmType;
import eu.toop.connector.webapi.TCAPIConfig;
import eu.toop.connector.webapi.helper.AbstractTCAPIInvoker;
import eu.toop.connector.webapi.helper.CommonAPIInvoker;

/**
 * Perform validation via API
 *
 * @author Philip Helger
 */
public class ApiPostValidateEdm extends AbstractTCAPIInvoker
{
  private static final Logger LOGGER = LoggerFactory.getLogger (ApiPostValidateEdm.class);

  private final ETCEdmType m_eType;

  public ApiPostValidateEdm (@Nonnull final ETCEdmType eType)
  {
    m_eType = eType;
  }

  @Override
  public void invokeAPI (@Nonnull final IAPIDescriptor aAPIDescriptor,
                         @Nonnull @Nonempty final String sPath,
                         @Nonnull final Map <String, String> aPathVariables,
                         @Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                         @Nonnull final PhotonUnifiedResponse aUnifiedResponse) throws IOException
  {
    final Locale aDisplayLocale = Locale.UK;

    final byte [] aPayload = StreamHelper.getAllBytes (aRequestScope.getRequest ().getInputStream ());
    final VESID aVESID = m_eType.getVESID ();

    LOGGER.info ("API validating " + aPayload.length + " bytes using '" + aVESID.getAsSingleID () + "'");

    final IJsonObject aJson = new JsonObject ();
    CommonAPIInvoker.invoke (aJson, () -> {
      // Main validation
      final StopWatch aSW = StopWatch.createdStarted ();
      final ValidationResultList aValidationResultList = TCAPIConfig.getVSValidator ().validate (aVESID, aPayload, aDisplayLocale);
      aSW.stop ();

      // Build response
      aJson.add ("success", true);
      BDVEJsonHelper.applyValidationResultList (aJson,
                                                TCValidator.getVES (aVESID),
                                                aValidationResultList,
                                                aDisplayLocale,
                                                aSW.getMillis (),
                                                null,
                                                null);
    });

    aUnifiedResponse.json (aJson);
  }
}
