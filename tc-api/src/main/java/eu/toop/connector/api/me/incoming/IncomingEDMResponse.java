package eu.toop.connector.api.me.incoming;

import java.util.List;

import javax.annotation.Nonnull;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.commons.collection.impl.CommonsLinkedHashMap;
import com.helger.commons.collection.impl.ICommonsOrderedMap;
import com.helger.commons.string.ToStringGenerator;

import eu.toop.connector.api.me.model.MEPayload;
import eu.toop.edm.EDMResponse;

/**
 * Incoming EDM response. Uses {@link EDMResponse}, optional attachments and
 * {@link MEIncomingTransportMetadata} for the main data,
 *
 * @author Philip Helger
 */
public class IncomingEDMResponse
{
  private final EDMResponse m_aResponse;
  private final ICommonsOrderedMap <String, MEPayload> m_aAttachments = new CommonsLinkedHashMap <> ();
  private final MEIncomingTransportMetadata m_aMetadata;

  public IncomingEDMResponse (@Nonnull final EDMResponse aResponse,
                              @Nonnull final List <MEPayload> aAttachments,
                              @Nonnull final MEIncomingTransportMetadata aMetadata)
  {
    ValueEnforcer.notNull (aResponse, "Response");
    ValueEnforcer.notNullNoNullValue (aAttachments, "Attachments");
    ValueEnforcer.notNull (aMetadata, "Metadata");

    m_aResponse = aResponse;
    for (final MEPayload aItem : aAttachments)
      m_aAttachments.put (aItem.getContentID (), aItem);
    m_aMetadata = aMetadata;
  }

  @Nonnull
  public EDMResponse getResponse ()
  {
    return m_aResponse;
  }

  @Nonnull
  @ReturnsMutableObject
  public ICommonsOrderedMap <String, MEPayload> attachments ()
  {
    return m_aAttachments;
  }

  @Nonnull
  @ReturnsMutableCopy
  public ICommonsOrderedMap <String, MEPayload> getAllAttachments ()
  {
    return m_aAttachments.getClone ();
  }

  @Nonnull
  public MEIncomingTransportMetadata getMetadata ()
  {
    return m_aMetadata;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("Response", m_aResponse)
                                       .append ("Attachments", m_aAttachments)
                                       .append ("Metadata", m_aMetadata)
                                       .getToString ();
  }
}
