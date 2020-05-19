package eu.toop.connector.api.me.incoming;

import javax.annotation.Nonnull;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.string.ToStringGenerator;

import eu.toop.edm.EDMRequest;

/**
 * Incoming EDM request. Uses {@link EDMRequest} and
 * {@link MEIncomingTransportMetadata} for the main data,
 *
 * @author Philip Helger
 */
public class IncomingEDMRequest
{
  private final EDMRequest m_aRequest;
  private final MEIncomingTransportMetadata m_aMetadata;

  public IncomingEDMRequest (@Nonnull final EDMRequest aRequest, @Nonnull final MEIncomingTransportMetadata aMetadata)
  {
    ValueEnforcer.notNull (aRequest, "Request");
    ValueEnforcer.notNull (aMetadata, "Metadata");
    m_aRequest = aRequest;
    m_aMetadata = aMetadata;
  }

  @Nonnull
  public EDMRequest getRequest ()
  {
    return m_aRequest;
  }

  @Nonnull
  public MEIncomingTransportMetadata getMetadata ()
  {
    return m_aMetadata;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("Request", m_aRequest).append ("Metadata", m_aMetadata).getToString ();
  }
}
