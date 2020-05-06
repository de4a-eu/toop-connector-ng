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
package eu.toop.commons.codelist;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.commons.collection.impl.CommonsEnumMap;
import com.helger.commons.collection.impl.ICommonsMap;

/**
 * This class manages the relationship between TOOP request and response
 * Document types. It is used to create the appropriate "response" document type
 * from an arbitrary source document type.
 *
 * @author Philip Helger
 */
public final class ReverseDocumentTypeMapping
{
  private static final ICommonsMap <EPredefinedDocumentTypeIdentifier, EPredefinedDocumentTypeIdentifier> MAP = new CommonsEnumMap <> (EPredefinedDocumentTypeIdentifier.class);

  private static void _add (@Nonnull final EPredefinedDocumentTypeIdentifier aRequest,
                            @Nonnull final EPredefinedDocumentTypeIdentifier aResponse)
  {
    MAP.put (aRequest, aResponse);
    MAP.put (aResponse, aRequest);
  }

  static
  {
    // Fill the map
    _add (EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_REQUEST_URN_EU_TOOP_REQUEST_REGISTEREDORGANIZATION_LIST_1_40,
          EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_RESPONSE_URN_EU_TOOP_RESPONSE_REGISTEREDORGANIZATION_LIST_1_40);
    _add (EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_REQUEST_URN_EU_TOOP_REQUEST_REGISTEREDORGANIZATION_1_40,
          EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_RESPONSE_URN_EU_TOOP_RESPONSE_REGISTEREDORGANIZATION_1_40);
    _add (EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_REQUEST_URN_EU_TOOP_REQUEST_SHIPCERTIFICATE_LIST_1_40,
          EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_RESPONSE_URN_EU_TOOP_RESPONSE_SHIPCERTIFICATE_LIST_1_40);
    _add (EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_REQUEST_URN_EU_TOOP_REQUEST_SHIPCERTIFICATE_1_40,
          EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_RESPONSE_URN_EU_TOOP_RESPONSE_SHIPCERTIFICATE_1_40);
    _add (EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_REQUEST_URN_EU_TOOP_REQUEST_CREWCERTIFICATE_LIST_1_40,
          EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_RESPONSE_URN_EU_TOOP_RESPONSE_CREWCERTIFICATE_LIST_1_40);
    _add (EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_REQUEST_URN_EU_TOOP_REQUEST_CREWCERTIFICATE_1_40,
          EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_RESPONSE_URN_EU_TOOP_RESPONSE_CREWCERTIFICATE_1_40);
    _add (EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_REQUEST_URN_EU_TOOP_REQUEST_EVIDENCE_LIST_1_40,
          EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_RESPONSE_URN_EU_TOOP_RESPONSE_EVIDENCE_LIST_1_40);
    _add (EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_REQUEST_URN_EU_TOOP_REQUEST_EVIDENCE_1_40,
          EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_RESPONSE_URN_EU_TOOP_RESPONSE_EVIDENCE_1_40);
  }

  private ReverseDocumentTypeMapping ()
  {}

  @Nullable
  public static EPredefinedDocumentTypeIdentifier getReverseDocumentTypeOrNull (@Nullable final EPredefinedDocumentTypeIdentifier eDocType)
  {
    return MAP.get (eDocType);
  }

  @Nonnull
  @Nonempty
  public static EPredefinedDocumentTypeIdentifier getReverseDocumentType (@Nullable final EPredefinedDocumentTypeIdentifier eDocType)
  {
    final EPredefinedDocumentTypeIdentifier ret = getReverseDocumentTypeOrNull (eDocType);
    if (ret == null)
      throw new IllegalArgumentException ("Unsupported document type " + eDocType);
    return ret;
  }

  /**
   * @return A copy of the map used for reverse mapping. Never
   *         <code>null</code>.
   * @since 0.10.2
   */
  @Nonnull
  @ReturnsMutableObject
  public static ICommonsMap <EPredefinedDocumentTypeIdentifier, EPredefinedDocumentTypeIdentifier> getAllMappings ()
  {
    return MAP.getClone ();
  }
}
