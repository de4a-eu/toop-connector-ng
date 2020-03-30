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

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.CollectionHelper;
import com.helger.commons.mime.CMimeType;
import com.helger.commons.mime.IMimeType;
import com.helger.datetime.util.PDTXMLConverter;
import com.helger.peppolid.IDocumentTypeIdentifier;
import com.helger.peppolid.IParticipantIdentifier;
import com.helger.peppolid.IProcessIdentifier;
import com.helger.xhe.v10.XHE10XHEType;
import com.helger.xhe.v10.cac.BusinessScopeCriterionType;
import com.helger.xhe.v10.cac.BusinessScopeType;
import com.helger.xhe.v10.cac.ExternalReferenceType;
import com.helger.xhe.v10.cac.HeaderType;
import com.helger.xhe.v10.cac.PartyIdentificationType;
import com.helger.xhe.v10.cac.PartyType;
import com.helger.xhe.v10.cac.PayloadType;
import com.helger.xhe.v10.cac.PayloadsType;
import com.helger.xhe.v10.cbc.DocumentTypeCodeType;
import com.helger.xhe.v10.cbc.ProfileIDType;

/**
 * Helper class to create XHE data structures more easily.
 *
 * @author Philip Helger
 */
@Immutable
public final class XHEHelper
{
  public static final String ID_TOOP = "TOOP";

  private XHEHelper ()
  {}

  /**
   * Create a new <code>BusinessScopeCriterion</code> element.
   *
   * @param sKey
   *        Type code to use. May not be <code>null</code>.
   * @param sValue
   *        Value to use. May not be <code>null</code>.
   * @return Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static BusinessScopeCriterionType createBusinessScopeCriterion (@Nonnull final String sKey,
                                                                         @Nonnull final String sValue)
  {
    ValueEnforcer.notNull (sKey, "Key");
    ValueEnforcer.notNull (sValue, "Value");

    final BusinessScopeCriterionType ret = new BusinessScopeCriterionType ();
    ret.setBusinessScopeCriterionTypeCode (sKey);
    ret.setBusinessScopeCriterionValue (sValue);
    return ret;
  }

  /**
   * Create an XHE party.
   *
   * @param aParticipantID
   *        Participant ID to use. May not be <code>null</code>.
   * @return New PartyType and never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static PartyType createParty (@Nonnull final IParticipantIdentifier aParticipantID)
  {
    final PartyType ret = new PartyType ();
    final PartyIdentificationType aPartyID = new PartyIdentificationType ();
    aPartyID.setID (aParticipantID.getValue ()).setSchemeID (aParticipantID.getScheme ());
    ret.addPartyIdentification (aPartyID);
    return ret;
  }

  /**
   * Create a payload.
   *
   * @param sID
   *        The unique ID. May neither be <code>null</code> nor empty.
   * @param aDocTypeID
   *        The document type ID to be used. May not be <code>null</code>.
   * @param aProcessID
   *        The process ID to be used. May not be <code>null</code>.
   * @param aMimeType
   *        The MIME type to be used. May not be <code>null</code>.
   * @param sFilename
   *        The filename that is the reference into the ASIC. May neither be
   *        <code>null</code> nor empty.
   * @return The created payload. Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static PayloadType createPayload (@Nonnull @Nonempty final String sID,
                                           @Nonnull final IDocumentTypeIdentifier aDocTypeID,
                                           @Nonnull final IProcessIdentifier aProcessID,
                                           @Nonnull final IMimeType aMimeType,
                                           @Nonnull @Nonempty final String sFilename)
  {
    ValueEnforcer.notEmpty (sID, "ID");
    ValueEnforcer.notNull (aDocTypeID, "DocTypeID");
    ValueEnforcer.notNull (aProcessID, "ProcessID");
    ValueEnforcer.notNull (aMimeType, "MimeType");

    final PayloadType ret = new PayloadType ();
    ret.setID (sID).setSchemeID (ID_TOOP);
    {
      final DocumentTypeCodeType aDTC = new DocumentTypeCodeType ();
      aDTC.setValue (aDocTypeID.getValue ());
      aDTC.setListID (aDocTypeID.getScheme ());
      aDTC.setListAgencyID (ID_TOOP);
      ret.setDocumentTypeCode (aDTC);
    }
    ret.setContentTypeCode (aMimeType.getAsString ());
    {
      final ProfileIDType aPID = new ProfileIDType ();
      aPID.setValue (aProcessID.getValue ());
      aPID.setSchemeID (aProcessID.getScheme ());
      aPID.setSchemeAgencyID (ID_TOOP);
      ret.setProfileID (aPID);
    }
    ret.setInstanceEncryptionIndicator (false);
    {
      final ExternalReferenceType aER = new ExternalReferenceType ();
      aER.setID (sFilename);
      ret.setPayloadExternalReference (aER);
    }
    return ret;
  }

  @Nonnull
  public static PayloadType createXMLPayload (@Nonnull @Nonempty final String sID,
                                              @Nonnull final IDocumentTypeIdentifier aDocTypeID,
                                              @Nonnull final IProcessIdentifier aProcessID)
  {
    return createPayload (sID, aDocTypeID, aProcessID, CMimeType.APPLICATION_XML, sID + ".xml");
  }

  /**
   * @return An empty XHE with only the version ID, a unique ID and the current
   *         creation time Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static XHE10XHEType createEmptyXHE ()
  {
    final XHE10XHEType ret = new XHE10XHEType ();
    ret.setXHEVersionID ("1.0");
    final HeaderType aHeader = new HeaderType ();
    aHeader.setID (UUID.randomUUID ().toString ());
    aHeader.setCreationDateTime (PDTXMLConverter.getXMLCalendarNow ());
    ret.setHeader (aHeader);
    return ret;
  }

  /**
   * Create a new fill XHE.
   *
   * @param aParams
   *        Custom parameter to be used as "Business Scope Criterion". May be
   *        <code>null</code> or empty.
   * @param aFromParty
   *        The from party ID. May be <code>null</code>.
   * @param aToParties
   *        The to-party IDs. May neither be <code>null</code> nor empty.
   * @param aPayloadList
   *        The list of payloads to be added. May be <code>null</code> or empty.
   * @return The XHE object. Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static XHE10XHEType createXHE (@Nullable final Map <String, String> aParams,
                                        @Nullable final IParticipantIdentifier aFromParty,
                                        @Nonnull final List <IParticipantIdentifier> aToParties,
                                        @Nullable final List <PayloadType> aPayloadList)
  {
    ValueEnforcer.notEmptyNoNullValue (aToParties, "ToParties");

    final XHE10XHEType ret = createEmptyXHE ();
    final HeaderType aHeader = ret.getHeader ();

    // Custom parameters
    if (CollectionHelper.isNotEmpty (aParams))
    {
      final BusinessScopeType aBusinessScope = new BusinessScopeType ();
      for (final Map.Entry <String, String> aEntry : aParams.entrySet ())
        aBusinessScope.addBusinessScopeCriterion (createBusinessScopeCriterion (aEntry.getKey (), aEntry.getValue ()));
      aHeader.setBusinessScope (aBusinessScope);
    }

    // Party
    if (aFromParty != null)
      aHeader.setFromParty (createParty (aFromParty));
    for (final IParticipantIdentifier aToParty : aToParties)
      aHeader.addToParty (createParty (aToParty));

    // Payloads
    if (CollectionHelper.isNotEmpty (aPayloadList))
    {
      final PayloadsType aPayloads = new PayloadsType ();
      for (final PayloadType aPayload : aPayloadList)
        aPayloads.addPayload (aPayload);
      ret.setPayloads (aPayloads);
    }
    return ret;
  }
}
