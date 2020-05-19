package eu.toop.connector.api.me.model;

import java.util.List;

import javax.annotation.Nonnull;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.commons.collection.impl.CommonsLinkedHashMap;
import com.helger.commons.collection.impl.ICommonsOrderedMap;
import com.helger.commons.string.ToStringGenerator;

import eu.toop.edm.EDMResponse;

/**
 * Represents an EDM response in combination with it's attachments. It consists
 * of an {@link EDMResponse} and an ordered map of all attachments, where the ID
 * is the "Content-ID" of the attachment.
 *
 * @author Philip Helger
 */
public class EDMResponseWithAttachments
{
  private final EDMResponse m_aResponse;
  private final ICommonsOrderedMap <String, MEPayload> m_aAttachments = new CommonsLinkedHashMap <> ();

  /**
   * Constructor
   *
   * @param aResponse
   *        Response to use. May not be <code>null</code>.
   * @param aAttachments
   *        The attachments to add. May not be <code>null</code> and may not
   *        contain <code>null</code> entries but maybe empty.
   */
  public EDMResponseWithAttachments (@Nonnull final EDMResponse aResponse, @Nonnull final List <MEPayload> aAttachments)
  {
    ValueEnforcer.notNull (aResponse, "Response");
    ValueEnforcer.notNullNoNullValue (aAttachments, "Attachments");

    m_aResponse = aResponse;
    for (final MEPayload aItem : aAttachments)
      m_aAttachments.put (aItem.getContentID (), aItem);
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

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("Response", m_aResponse).append ("Attachments", m_aAttachments).getToString ();
  }
}
