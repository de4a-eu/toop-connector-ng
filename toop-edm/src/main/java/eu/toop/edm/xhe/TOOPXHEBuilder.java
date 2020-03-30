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
package eu.toop.edm.xhe;

import javax.annotation.Nonnull;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.CommonsLinkedHashMap;
import com.helger.commons.collection.impl.ICommonsOrderedMap;
import com.helger.peppolid.IDocumentTypeIdentifier;
import com.helger.peppolid.IParticipantIdentifier;
import com.helger.peppolid.IProcessIdentifier;
import com.helger.xhe.v10.XHE10XHEType;
import com.helger.xhe.v10.cac.PayloadType;

/**
 * TOOP XHE builder.<br>
 * See http://wiki.ds.unipi.gr/display/TOOP/TOOP+EDM+Routing+Envelope for
 * details
 *
 * @author Philip Helger
 */
public class TOOPXHEBuilder
{
  private final ICommonsOrderedMap <String, String> m_aParams = new CommonsLinkedHashMap <> ();
  private IParticipantIdentifier m_aFromParty;
  private IParticipantIdentifier m_aToParty;
  private IDocumentTypeIdentifier m_aDocTypeID;
  private IProcessIdentifier m_aProcessID;
  private EXHEPayloadType m_ePayloadType;

  public TOOPXHEBuilder ()
  {}

  /**
   * Add an optional parameter.
   *
   * @param sKey
   *        Parameter name
   * @param sValue
   *        Parameter value
   * @return this for chaining
   */
  @Nonnull
  public TOOPXHEBuilder addParameter (@Nonnull @Nonempty final String sKey, @Nonnull @Nonempty final String sValue)
  {
    ValueEnforcer.notEmpty (sKey, "Key");
    ValueEnforcer.notEmpty (sValue, "Value");
    m_aParams.put (sKey, sValue);
    return this;
  }

  /**
   * Set the mandatory from party ID
   *
   * @param aPID
   *        From party ID. May not be <code>null</code>.
   * @return this for chaining
   */
  @Nonnull
  public TOOPXHEBuilder setFromParty (@Nonnull final IParticipantIdentifier aPID)
  {
    ValueEnforcer.notNull (aPID, "ParticipantID");
    m_aFromParty = aPID;
    return this;
  }

  /**
   * Set the mandatory to party ID
   *
   * @param aPID
   *        To party ID. May not be <code>null</code>.
   * @return this for chaining
   */
  @Nonnull
  public TOOPXHEBuilder setToParty (@Nonnull final IParticipantIdentifier aPID)
  {
    ValueEnforcer.notNull (aPID, "ParticipantID");
    m_aToParty = aPID;
    return this;
  }

  /**
   * Set the mandatory document type ID to be used.
   *
   * @param aDocTypeID
   *        Document type ID. May not be <code>null</code>.
   * @return this for chaining
   */
  @Nonnull
  public TOOPXHEBuilder setDocumentTypeID (@Nonnull final IDocumentTypeIdentifier aDocTypeID)
  {
    ValueEnforcer.notNull (aDocTypeID, "DocTypeID");
    m_aDocTypeID = aDocTypeID;
    return this;
  }

  /**
   * Set the mandatory process ID to be used.
   *
   * @param aProcessID
   *        Process ID. May not be <code>null</code>.
   * @return this for chaining
   */
  @Nonnull
  public TOOPXHEBuilder setProcessID (@Nonnull final IProcessIdentifier aProcessID)
  {
    ValueEnforcer.notNull (aProcessID, "ProcessID");
    m_aProcessID = aProcessID;
    return this;
  }

  /**
   * Set the mandatory payload to be used.
   *
   * @param ePayloadType
   *        Payload ID. May not be <code>null</code>.
   * @return this for chaining
   */
  @Nonnull
  public TOOPXHEBuilder setPayloadType (@Nonnull final EXHEPayloadType ePayloadType)
  {
    ValueEnforcer.notNull (ePayloadType, "PayloadType");
    m_ePayloadType = ePayloadType;
    return this;
  }

  @Nonnull
  public XHE10XHEType build ()
  {
    if (m_aFromParty == null)
      throw new IllegalStateException ("FromParty is missing");
    if (m_aToParty == null)
      throw new IllegalStateException ("ToParty is missing");
    if (m_aDocTypeID == null)
      throw new IllegalStateException ("DocTypeID is missing");
    if (m_aProcessID == null)
      throw new IllegalStateException ("ProcessID is missing");
    if (m_ePayloadType == null)
      throw new IllegalStateException ("PayloadType is missing");

    final PayloadType aPayload = XHEHelper.createXMLPayload (m_ePayloadType.getID (), m_aDocTypeID, m_aProcessID);
    return XHEHelper.createXHE (m_aParams,
                                m_aFromParty,
                                new CommonsArrayList <> (m_aToParty),
                                new CommonsArrayList <> (aPayload));
  }
}
