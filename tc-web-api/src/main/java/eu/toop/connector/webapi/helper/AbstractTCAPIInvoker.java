package eu.toop.connector.webapi.helper;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.timing.StopWatch;
import com.helger.photon.api.IAPIDescriptor;
import com.helger.photon.api.IAPIExecutor;
import com.helger.photon.app.PhotonUnifiedResponse;
import com.helger.servlet.response.UnifiedResponse;
import com.helger.web.scope.IRequestWebScopeWithoutResponse;

public abstract class AbstractTCAPIInvoker implements IAPIExecutor
{
  private static final Logger LOGGER = LoggerFactory.getLogger (AbstractTCAPIInvoker.class);

  public abstract void invokeAPI (@Nonnull final IAPIDescriptor aAPIDescriptor,
                                  @Nonnull @Nonempty final String sPath,
                                  @Nonnull final Map <String, String> aPathVariables,
                                  @Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                  @Nonnull final PhotonUnifiedResponse aUnifiedResponse) throws IOException;

  public final void invokeAPI (@Nonnull final IAPIDescriptor aAPIDescriptor,
                               @Nonnull @Nonempty final String sPath,
                               @Nonnull final Map <String, String> aPathVariables,
                               @Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                               @Nonnull final UnifiedResponse aUnifiedResponse) throws Exception
  {
    final StopWatch aSW = StopWatch.createdStarted ();

    invokeAPI (aAPIDescriptor, sPath, aPathVariables, aRequestScope, (PhotonUnifiedResponse) aUnifiedResponse);

    aSW.stop ();
    LOGGER.info ("[API] Succesfully finished '" +
                 aAPIDescriptor.getPathDescriptor ().getAsURLString () +
                 "' after " +
                 aSW.getMillis () +
                 " milliseconds");

  }
}
