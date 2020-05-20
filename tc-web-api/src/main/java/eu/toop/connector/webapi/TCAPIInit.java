package eu.toop.connector.webapi;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.helger.photon.api.APIDescriptor;
import com.helger.photon.api.APIPath;
import com.helger.photon.api.IAPIRegistry;

import eu.toop.connector.app.validation.EValidationEdmType;
import eu.toop.connector.webapi.as4.ApiPostSend;
import eu.toop.connector.webapi.dsd.ApiGetDsdDp;
import eu.toop.connector.webapi.dsd.ApiGetDsdDpByCountry;
import eu.toop.connector.webapi.smp.ApiGetSmpDocTypes;
import eu.toop.connector.webapi.smp.ApiGetSmpEndpoints;
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
    aAPIRegistry.registerAPI (new APIDescriptor (APIPath.get ("/dsd/dp/{dataset}"), ApiGetDsdDp.class));
    aAPIRegistry.registerAPI (new APIDescriptor (APIPath.get ("/dsd/dp/{dataset}/by-country/{country}"), ApiGetDsdDpByCountry.class));

    // SMP stuff
    aAPIRegistry.registerAPI (new APIDescriptor (APIPath.get ("/smp/doctypes/{pid}"), ApiGetSmpDocTypes.class));
    aAPIRegistry.registerAPI (new APIDescriptor (APIPath.get ("/smp/endpoints/{pid}/{doctypeid}"), ApiGetSmpEndpoints.class));

    // Validation stuff
    aAPIRegistry.registerAPI (new APIDescriptor (APIPath.post ("/validate/request"), new ApiPostValidateEdm (EValidationEdmType.REQUEST)));
    aAPIRegistry.registerAPI (new APIDescriptor (APIPath.post ("/validate/response"),
                                                 new ApiPostValidateEdm (EValidationEdmType.RESPONSE)));
    aAPIRegistry.registerAPI (new APIDescriptor (APIPath.post ("/validate/error"),
                                                 new ApiPostValidateEdm (EValidationEdmType.ERROR_RESPONSE)));

    // AS4 stuff
    aAPIRegistry.registerAPI (new APIDescriptor (APIPath.post ("/send/{pid}"), ApiPostSend.class));
  }

}
