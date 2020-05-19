package eu.toop.connector.api;

import java.util.Map;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.collection.impl.ICommonsSet;
import com.helger.json.JsonArray;
import com.helger.json.JsonObject;
import com.helger.peppolid.IParticipantIdentifier;
import com.helger.photon.api.IAPIDescriptor;
import com.helger.photon.api.IAPIExecutor;
import com.helger.photon.app.PhotonUnifiedResponse;
import com.helger.servlet.response.UnifiedResponse;
import com.helger.web.scope.IRequestWebScopeWithoutResponse;

import eu.toop.connector.api.smp.LoggingSMPErrorHandler;
import eu.toop.connector.app.dsd.DSDParticipantIDProviderRemote;

public class ApiGetDsdDpByCountry implements IAPIExecutor
{
  public void invokeAPI (@Nonnull final IAPIDescriptor aAPIDescriptor,
                         @Nonnull @Nonempty final String sPath,
                         @Nonnull final Map <String, String> aPathVariables,
                         @Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                         @Nonnull final UnifiedResponse aUnifiedResponse) throws Exception
  {
    final String sDatasetType = aPathVariables.get ("dataset");
    final String sCountryCode = aPathVariables.get ("country");
    final ICommonsSet <IParticipantIdentifier> aParticipants = new DSDParticipantIDProviderRemote ().getAllParticipantIDs ("[api /dsd/dp/by-country]",
                                                                                                                           sDatasetType,
                                                                                                                           sCountryCode,
                                                                                                                           null,
                                                                                                                           LoggingSMPErrorHandler.INSTANCE);

    final JsonArray ret = new JsonArray ();
    for (final IParticipantIdentifier aPI : aParticipants)
      ret.add (new JsonObject ().add ("scheme", aPI.getScheme ()).add ("value", aPI.getValue ()));
    ((PhotonUnifiedResponse) aUnifiedResponse).json (ret);
  }
}
