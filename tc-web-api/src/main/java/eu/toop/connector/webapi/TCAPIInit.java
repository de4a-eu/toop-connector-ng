/**
 * Copyright (C) 2018-2021 toop.eu
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
package eu.toop.connector.webapi;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.helger.photon.api.APIDescriptor;
import com.helger.photon.api.APIPath;
import com.helger.photon.api.IAPIRegistry;

import eu.toop.connector.webapi.as4.ApiPostSend;
import eu.toop.connector.webapi.dsd.ApiGetDsdDpByCountry;
import eu.toop.connector.webapi.dsd.ApiGetDsdDpByDPType;
import eu.toop.connector.webapi.smp.ApiGetSmpDocTypes;
import eu.toop.connector.webapi.smp.ApiGetSmpEndpoints;
import eu.toop.connector.webapi.user.ApiPostUserSubmitEdm;
import eu.toop.connector.webapi.validation.ApiPostValidateEdm;

/**
 * Register all APIs
 *
 * @author Philip Helger
 */
@Immutable
public final class TCAPIInit
{
  private TCAPIInit ()
  {}

  public static void initAPI (@Nonnull final IAPIRegistry aAPIRegistry)
  {
    // DSD stuff
    aAPIRegistry.registerAPI (new APIDescriptor (APIPath.get ("/dsd/dp/{datasetType}/by-country/{country}"), ApiGetDsdDpByCountry.class));
    aAPIRegistry.registerAPI (new APIDescriptor (APIPath.get ("/dsd/dp/{datasetType}/by-dp-type/{dpType}"), ApiGetDsdDpByDPType.class));

    // SMP stuff
    aAPIRegistry.registerAPI (new APIDescriptor (APIPath.get ("/smp/doctypes/{pid}"), ApiGetSmpDocTypes.class));
    aAPIRegistry.registerAPI (new APIDescriptor (APIPath.get ("/smp/endpoints/{pid}/{doctypeid}"), ApiGetSmpEndpoints.class));

    // Validation stuff
    aAPIRegistry.registerAPI (new APIDescriptor (APIPath.post ("/validate/request"), new ApiPostValidateEdm (ETCEdmType.REQUEST)));
    aAPIRegistry.registerAPI (new APIDescriptor (APIPath.post ("/validate/response"), new ApiPostValidateEdm (ETCEdmType.RESPONSE)));
    aAPIRegistry.registerAPI (new APIDescriptor (APIPath.post ("/validate/error"), new ApiPostValidateEdm (ETCEdmType.ERROR_RESPONSE)));

    // AS4 stuff
    aAPIRegistry.registerAPI (new APIDescriptor (APIPath.post ("/send"), ApiPostSend.class));

    // User stuff
    aAPIRegistry.registerAPI (new APIDescriptor (APIPath.post ("/user/submit/request"), new ApiPostUserSubmitEdm (ETCEdmType.REQUEST)));
    aAPIRegistry.registerAPI (new APIDescriptor (APIPath.post ("/user/submit/response"), new ApiPostUserSubmitEdm (ETCEdmType.RESPONSE)));
    aAPIRegistry.registerAPI (new APIDescriptor (APIPath.post ("/user/submit/error"),
                                                 new ApiPostUserSubmitEdm (ETCEdmType.ERROR_RESPONSE)));
  }
}
