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
package eu.toop.connector.api.me.model;

import java.io.Serializable;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;

/**
 * List of {@link MEPayload} objects.
 *
 * @author myildiz at 12.02.2018.
 */
public class MEMessage implements Serializable
{
  private final String m_sSenderID;
  private final String m_sReceiverID;
  private final String m_sDocTypeID;
  private final String m_sProcessID;
  private final ICommonsList <MEPayload> m_aPayloads = new CommonsArrayList <> ();

  /**
   * Instantiates a new Me message.
   *
   * @param sProcessID
   * @param sDocTypeID
   * @param sReceiverID
   * @param sSenderID
   * @param aPayloads
   *        the a payloads
   */
  protected MEMessage (@Nullable final String sSenderID,
                       @Nullable final String sReceiverID,
                       @Nullable final String sDocTypeID,
                       @Nullable final String sProcessID,
                       @Nonnull @Nonempty final ICommonsList <MEPayload> aPayloads)
  {
    ValueEnforcer.notEmptyNoNullValue (aPayloads, "Payloads");
    m_sSenderID = sSenderID;
    m_sReceiverID = sReceiverID;
    m_sDocTypeID = sDocTypeID;
    m_sProcessID = sProcessID;
    m_aPayloads.addAll (aPayloads);
  }

  /**
   * @return the sender participant id. Maybe <code>null</code>.
   */
  @Nullable
  public String getSenderID ()
  {
    return m_sSenderID;
  }

  /**
   * @return the receiver participant id. Maybe <code>null</code>.
   */
  @Nullable
  public String getReceiverID ()
  {
    return m_sReceiverID;
  }

  /**
   * @return the document type id. Maybe <code>null</code>.
   */
  @Nullable
  public String getDoctypeID ()
  {
    return m_sDocTypeID;
  }

  /**
   * @return the process id. Maybe <code>null</code>.
   */
  @Nullable
  public String getProcessID ()
  {
    return m_sProcessID;
  }

  /**
   * @return A non-<code>null</code>, non-empty list of payloads. The result
   *         object is mutable and can change the content of this object.
   */
  @Nonnull
  @Nonempty
  @ReturnsMutableObject
  public ICommonsList <MEPayload> payloads ()
  {
    return m_aPayloads;
  }

  /**
   * @return A non-<code>null</code>, non-empty list of payloads. The result
   *         object is a cloned list.
   */
  @Nonnull
  @Nonempty
  @ReturnsMutableCopy
  public ICommonsList <MEPayload> getAllPayloads ()
  {
    return m_aPayloads.getClone ();
  }

  /**
   * Builder builder.
   *
   * @return the builder
   */
  @Nonnull
  public static Builder builder ()
  {
    return new Builder ();
  }

  /**
   * Builder class for {@link MEMessage}.
   *
   * @author Philip Helger
   */
  public static class Builder
  {
    private String m_sSenderID;
    private String m_sReceiverID;
    private String m_sDocTypeID;
    private String m_sProcessID;
    private final ICommonsList <MEPayload> m_aPayloads = new CommonsArrayList <> ();

    /**
     * Instantiates a new Builder.
     */
    protected Builder ()
    {}

    /**
     * Sets sender id.
     *
     * @param senderId
     *        the sender id
     * @return the builder
     */
    @Nonnull
    public Builder senderID (@Nullable final String s)
    {
      m_sSenderID = s;
      return this;
    }

    /**
     * Sets receiver id.
     *
     * @param s
     *        the receiver id
     * @return the builder
     */
    @Nonnull
    public Builder receiverID (@Nullable final String s)
    {
      m_sReceiverID = s;
      return this;
    }

    /**
     * Sets document type id.
     *
     * @param s
     *        the document type id
     * @return the builder
     */
    @Nonnull
    public Builder docTypeID (@Nullable final String s)
    {
      m_sDocTypeID = s;
      return this;
    }

    /**
     * Sets process id.
     *
     * @param s
     *        the process id
     * @return the builder
     */
    @Nonnull
    public Builder processID (@Nullable final String s)
    {
      m_sProcessID = s;
      return this;
    }

    /**
     * Add payload builder.
     *
     * @param a
     *        the a
     * @return the builder
     */
    @Nonnull
    public Builder addPayload (@Nullable final Consumer <? super MEPayload.Builder> a)
    {
      if (a != null)
      {
        final MEPayload.Builder aBuilder = MEPayload.builder ();
        a.accept (aBuilder);
        addPayload (aBuilder);
      }
      return this;
    }

    /**
     * Add payload builder.
     *
     * @param a
     *        the a
     * @return the builder
     */
    @Nonnull
    public Builder addPayload (@Nullable final MEPayload.Builder a)
    {
      return addPayload (a == null ? null : a.build ());
    }

    /**
     * Add payload builder.
     *
     * @param a
     *        the a
     * @return the builder
     */
    @Nonnull
    public Builder addPayload (@Nullable final MEPayload a)
    {
      if (a != null)
        m_aPayloads.add (a);
      return this;
    }

    /**
     * Payload builder.
     *
     * @param a
     *        the a
     * @return the builder
     */
    @Nonnull
    public Builder payload (@Nullable final Consumer <? super MEPayload.Builder> a)
    {
      if (a != null)
      {
        final MEPayload.Builder aBuilder = MEPayload.builder ();
        a.accept (aBuilder);
        payload (aBuilder);
      }
      return this;
    }

    /**
     * Payload builder.
     *
     * @param a
     *        the a
     * @return the builder
     */
    @Nonnull
    public Builder payload (@Nullable final MEPayload.Builder a)
    {
      return payload (a == null ? null : a.build ());
    }

    /**
     * Payload builder.
     *
     * @param a
     *        the a
     * @return the builder
     */
    @Nonnull
    public Builder payload (@Nullable final MEPayload a)
    {
      if (a != null)
        m_aPayloads.set (a);
      else
        m_aPayloads.clear ();
      return this;
    }

    /**
     * Payloads builder.
     *
     * @param a
     *        the a
     * @return the builder
     */
    @Nonnull
    public Builder payloads (@Nullable final MEPayload... a)
    {
      m_aPayloads.setAll (a);
      return this;
    }

    /**
     * Payloads builder.
     *
     * @param a
     *        the a
     * @return the builder
     */
    @Nonnull
    public Builder payloads (@Nullable final Iterable <MEPayload> a)
    {
      m_aPayloads.setAll (a);
      return this;
    }

    /**
     * Check consistency.
     */
    public void checkConsistency ()
    {
      if (m_aPayloads.isEmpty ())
        throw new IllegalStateException ("At least one payload MUST be present");
    }

    /**
     * Build me message.
     *
     * @return the me message
     */
    @Nonnull
    public MEMessage build ()
    {
      checkConsistency ();
      return new MEMessage (m_sSenderID, m_sReceiverID, m_sDocTypeID, m_sProcessID, m_aPayloads);
    }
  }
}
