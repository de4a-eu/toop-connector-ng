package eu.toop.connector.api.me.incoming;

import javax.annotation.Nonnull;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.string.ToStringGenerator;

import eu.toop.edm.EDMErrorResponse;

/**
 * Incoming EDM error response. Uses {@link EDMErrorResponse} and
 * {@link MEIncomingTransportMetadata} for the main data,
 *
 * @author Philip Helger
 */
public class IncomingEDMErrorResponse
{
  private final EDMErrorResponse m_aErrorResponse;
  private final MEIncomingTransportMetadata m_aMetadata;

  public IncomingEDMErrorResponse (@Nonnull final EDMErrorResponse aErrorResponse, @Nonnull final MEIncomingTransportMetadata aMetadata)
  {
    ValueEnforcer.notNull (aErrorResponse, "ErrorResponse");
    ValueEnforcer.notNull (aMetadata, "Metadata");
    m_aErrorResponse = aErrorResponse;
    m_aMetadata = aMetadata;
  }

  @Nonnull
  public EDMErrorResponse getErrorResponse ()
  {
    return m_aErrorResponse;
  }

  @Nonnull
  public MEIncomingTransportMetadata getMetadata ()
  {
    return m_aMetadata;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("ErrorResponse", m_aErrorResponse).append ("Metadata", m_aMetadata).getToString ();
  }
}
