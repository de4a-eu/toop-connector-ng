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

import com.helger.commons.annotation.CodingStyleguideUnaware;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.string.StringHelper;
import com.helger.commons.version.Version;
import com.helger.peppolid.IDocumentTypeIdentifier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;


/**
 * This file was automatically generated.
 * Do NOT edit!
 */
@CodingStyleguideUnaware
public enum EPredefinedDocumentTypeIdentifier
    implements IDocumentTypeIdentifier, IPredefined
{

    /**
     * <b>This item is deprecated since version 2 and should not be used to issue new identifiers!</b><br>Registered Organization Request - <code>urn:eu:toop:ns:dataexchange-1p10::Request##urn:eu.toop.request.registeredorganization::1.10</code><br>
     * 
     * @since code list v1
     */
    @Deprecated
    URN_EU_TOOP_NS_DATAEXCHANGE_1P10_REQUEST_URN_EU_TOOP_REQUEST_REGISTEREDORGANIZATION_1_10("Registered Organization Request", "urn:eu:toop:ns:dataexchange-1p10::Request##urn:eu.toop.request.registeredorganization::1.10", Version.parse("1"), true, Version.parse("2")),

    /**
     * <b>This item is deprecated since version 2 and should not be used to issue new identifiers!</b><br>Registered Organization Response - <code>urn:eu:toop:ns:dataexchange-1p10::Response##urn:eu.toop.response.registeredorganization::1.10</code><br>
     * 
     * @since code list v1
     */
    @Deprecated
    URN_EU_TOOP_NS_DATAEXCHANGE_1P10_RESPONSE_URN_EU_TOOP_RESPONSE_REGISTEREDORGANIZATION_1_10("Registered Organization Response", "urn:eu:toop:ns:dataexchange-1p10::Response##urn:eu.toop.response.registeredorganization::1.10", Version.parse("1"), true, Version.parse("2")),

    /**
     * Registered Organization List Request - <code>urn:eu:toop:ns:dataexchange-1p40::Request##urn:eu.toop.request.registeredorganization-list::1.40</code><br>
     * 
     * @since code list v2
     */
    URN_EU_TOOP_NS_DATAEXCHANGE_1P40_REQUEST_URN_EU_TOOP_REQUEST_REGISTEREDORGANIZATION_LIST_1_40("Registered Organization List Request", "urn:eu:toop:ns:dataexchange-1p40::Request##urn:eu.toop.request.registeredorganization-list::1.40", Version.parse("2"), false, null),

    /**
     * Registered Organization List Response - <code>urn:eu:toop:ns:dataexchange-1p40::Response##urn:eu.toop.response.registeredorganization-list::1.40</code><br>
     * 
     * @since code list v2
     */
    URN_EU_TOOP_NS_DATAEXCHANGE_1P40_RESPONSE_URN_EU_TOOP_RESPONSE_REGISTEREDORGANIZATION_LIST_1_40("Registered Organization List Response", "urn:eu:toop:ns:dataexchange-1p40::Response##urn:eu.toop.response.registeredorganization-list::1.40", Version.parse("2"), false, null),

    /**
     * Registered Organization Request - <code>urn:eu:toop:ns:dataexchange-1p40::Request##urn:eu.toop.request.registeredorganization::1.40</code><br>
     * 
     * @since code list v2
     */
    URN_EU_TOOP_NS_DATAEXCHANGE_1P40_REQUEST_URN_EU_TOOP_REQUEST_REGISTEREDORGANIZATION_1_40("Registered Organization Request", "urn:eu:toop:ns:dataexchange-1p40::Request##urn:eu.toop.request.registeredorganization::1.40", Version.parse("2"), false, null),

    /**
     * Registered Organization Response - <code>urn:eu:toop:ns:dataexchange-1p40::Response##urn:eu.toop.response.registeredorganization::1.40</code><br>
     * 
     * @since code list v2
     */
    URN_EU_TOOP_NS_DATAEXCHANGE_1P40_RESPONSE_URN_EU_TOOP_RESPONSE_REGISTEREDORGANIZATION_1_40("Registered Organization Response", "urn:eu:toop:ns:dataexchange-1p40::Response##urn:eu.toop.response.registeredorganization::1.40", Version.parse("2"), false, null),

    /**
     * Ship Certificate List Request - <code>urn:eu:toop:ns:dataexchange-1p40::Request##urn:eu.toop.request.shipcertificate-list::1.40</code><br>
     * 
     * @since code list v2
     */
    URN_EU_TOOP_NS_DATAEXCHANGE_1P40_REQUEST_URN_EU_TOOP_REQUEST_SHIPCERTIFICATE_LIST_1_40("Ship Certificate List Request", "urn:eu:toop:ns:dataexchange-1p40::Request##urn:eu.toop.request.shipcertificate-list::1.40", Version.parse("2"), false, null),

    /**
     * Ship Certificate List Response - <code>urn:eu:toop:ns:dataexchange-1p40::Response##urn:eu.toop.response.shipcertificate-list::1.40</code><br>
     * 
     * @since code list v2
     */
    URN_EU_TOOP_NS_DATAEXCHANGE_1P40_RESPONSE_URN_EU_TOOP_RESPONSE_SHIPCERTIFICATE_LIST_1_40("Ship Certificate List Response", "urn:eu:toop:ns:dataexchange-1p40::Response##urn:eu.toop.response.shipcertificate-list::1.40", Version.parse("2"), false, null),

    /**
     * Ship Certificate Request - <code>urn:eu:toop:ns:dataexchange-1p40::Request##urn:eu.toop.request.shipcertificate::1.40</code><br>
     * 
     * @since code list v2
     */
    URN_EU_TOOP_NS_DATAEXCHANGE_1P40_REQUEST_URN_EU_TOOP_REQUEST_SHIPCERTIFICATE_1_40("Ship Certificate Request", "urn:eu:toop:ns:dataexchange-1p40::Request##urn:eu.toop.request.shipcertificate::1.40", Version.parse("2"), false, null),

    /**
     * Ship Certificate Response - <code>urn:eu:toop:ns:dataexchange-1p40::Response##urn:eu.toop.response.shipcertificate::1.40</code><br>
     * 
     * @since code list v2
     */
    URN_EU_TOOP_NS_DATAEXCHANGE_1P40_RESPONSE_URN_EU_TOOP_RESPONSE_SHIPCERTIFICATE_1_40("Ship Certificate Response", "urn:eu:toop:ns:dataexchange-1p40::Response##urn:eu.toop.response.shipcertificate::1.40", Version.parse("2"), false, null),

    /**
     * Crew Certificate List Request - <code>urn:eu:toop:ns:dataexchange-1p40::Request##urn:eu.toop.request.crewcertificate-list::1.40</code><br>
     * 
     * @since code list v2
     */
    URN_EU_TOOP_NS_DATAEXCHANGE_1P40_REQUEST_URN_EU_TOOP_REQUEST_CREWCERTIFICATE_LIST_1_40("Crew Certificate List Request", "urn:eu:toop:ns:dataexchange-1p40::Request##urn:eu.toop.request.crewcertificate-list::1.40", Version.parse("2"), false, null),

    /**
     * Crew Certificate List Response - <code>urn:eu:toop:ns:dataexchange-1p40::Response##urn:eu.toop.response.crewcertificate-list::1.40</code><br>
     * 
     * @since code list v2
     */
    URN_EU_TOOP_NS_DATAEXCHANGE_1P40_RESPONSE_URN_EU_TOOP_RESPONSE_CREWCERTIFICATE_LIST_1_40("Crew Certificate List Response", "urn:eu:toop:ns:dataexchange-1p40::Response##urn:eu.toop.response.crewcertificate-list::1.40", Version.parse("2"), false, null),

    /**
     * Crew Certificate Request - <code>urn:eu:toop:ns:dataexchange-1p40::Request##urn:eu.toop.request.crewcertificate::1.40</code><br>
     * 
     * @since code list v2
     */
    URN_EU_TOOP_NS_DATAEXCHANGE_1P40_REQUEST_URN_EU_TOOP_REQUEST_CREWCERTIFICATE_1_40("Crew Certificate Request", "urn:eu:toop:ns:dataexchange-1p40::Request##urn:eu.toop.request.crewcertificate::1.40", Version.parse("2"), false, null),

    /**
     * Crew Certificate Response - <code>urn:eu:toop:ns:dataexchange-1p40::Response##urn:eu.toop.response.crewcertificate::1.40</code><br>
     * 
     * @since code list v2
     */
    URN_EU_TOOP_NS_DATAEXCHANGE_1P40_RESPONSE_URN_EU_TOOP_RESPONSE_CREWCERTIFICATE_1_40("Crew Certificate Response", "urn:eu:toop:ns:dataexchange-1p40::Response##urn:eu.toop.response.crewcertificate::1.40", Version.parse("2"), false, null),

    /**
     * Evidence List Request - <code>urn:eu:toop:ns:dataexchange-1p40::Request##urn:eu.toop.request.evidence-list::1.40</code><br>
     * 
     * @since code list v2
     */
    URN_EU_TOOP_NS_DATAEXCHANGE_1P40_REQUEST_URN_EU_TOOP_REQUEST_EVIDENCE_LIST_1_40("Evidence List Request", "urn:eu:toop:ns:dataexchange-1p40::Request##urn:eu.toop.request.evidence-list::1.40", Version.parse("2"), false, null),

    /**
     * Evidence List Response - <code>urn:eu:toop:ns:dataexchange-1p40::Response##urn:eu.toop.response.evidence-list::1.40</code><br>
     * 
     * @since code list v2
     */
    URN_EU_TOOP_NS_DATAEXCHANGE_1P40_RESPONSE_URN_EU_TOOP_RESPONSE_EVIDENCE_LIST_1_40("Evidence List Response", "urn:eu:toop:ns:dataexchange-1p40::Response##urn:eu.toop.response.evidence-list::1.40", Version.parse("2"), false, null),

    /**
     * Evidence Request - <code>urn:eu:toop:ns:dataexchange-1p40::Request##urn:eu.toop.request.evidence::1.40</code><br>
     * 
     * @since code list v2
     */
    URN_EU_TOOP_NS_DATAEXCHANGE_1P40_REQUEST_URN_EU_TOOP_REQUEST_EVIDENCE_1_40("Evidence Request", "urn:eu:toop:ns:dataexchange-1p40::Request##urn:eu.toop.request.evidence::1.40", Version.parse("2"), false, null),

    /**
     * Evidence Response - <code>urn:eu:toop:ns:dataexchange-1p40::Response##urn:eu.toop.response.evidence::1.40</code><br>
     * 
     * @since code list v2
     */
    URN_EU_TOOP_NS_DATAEXCHANGE_1P40_RESPONSE_URN_EU_TOOP_RESPONSE_EVIDENCE_1_40("Evidence Response", "urn:eu:toop:ns:dataexchange-1p40::Response##urn:eu.toop.response.evidence::1.40", Version.parse("2"), false, null);
    public static final EPredefinedDocumentTypeIdentifier REQUEST_REGISTEREDORGANIZATION_LIST = EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_REQUEST_URN_EU_TOOP_REQUEST_REGISTEREDORGANIZATION_LIST_1_40;
    public static final EPredefinedDocumentTypeIdentifier RESPONSE_REGISTEREDORGANIZATION_LIST = EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_RESPONSE_URN_EU_TOOP_RESPONSE_REGISTEREDORGANIZATION_LIST_1_40;
    public static final EPredefinedDocumentTypeIdentifier REQUEST_REGISTEREDORGANIZATION = EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_REQUEST_URN_EU_TOOP_REQUEST_REGISTEREDORGANIZATION_1_40;
    public static final EPredefinedDocumentTypeIdentifier RESPONSE_REGISTEREDORGANIZATION = EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_RESPONSE_URN_EU_TOOP_RESPONSE_REGISTEREDORGANIZATION_1_40;
    public static final EPredefinedDocumentTypeIdentifier REQUEST_SHIPCERTIFICATE_LIST = EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_REQUEST_URN_EU_TOOP_REQUEST_SHIPCERTIFICATE_LIST_1_40;
    public static final EPredefinedDocumentTypeIdentifier RESPONSE_SHIPCERTIFICATE_LIST = EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_RESPONSE_URN_EU_TOOP_RESPONSE_SHIPCERTIFICATE_LIST_1_40;
    public static final EPredefinedDocumentTypeIdentifier REQUEST_SHIPCERTIFICATE = EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_REQUEST_URN_EU_TOOP_REQUEST_SHIPCERTIFICATE_1_40;
    public static final EPredefinedDocumentTypeIdentifier RESPONSE_SHIPCERTIFICATE = EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_RESPONSE_URN_EU_TOOP_RESPONSE_SHIPCERTIFICATE_1_40;
    public static final EPredefinedDocumentTypeIdentifier REQUEST_CREWCERTIFICATE_LIST = EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_REQUEST_URN_EU_TOOP_REQUEST_CREWCERTIFICATE_LIST_1_40;
    public static final EPredefinedDocumentTypeIdentifier RESPONSE_CREWCERTIFICATE_LIST = EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_RESPONSE_URN_EU_TOOP_RESPONSE_CREWCERTIFICATE_LIST_1_40;
    public static final EPredefinedDocumentTypeIdentifier REQUEST_CREWCERTIFICATE = EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_REQUEST_URN_EU_TOOP_REQUEST_CREWCERTIFICATE_1_40;
    public static final EPredefinedDocumentTypeIdentifier RESPONSE_CREWCERTIFICATE = EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_RESPONSE_URN_EU_TOOP_RESPONSE_CREWCERTIFICATE_1_40;
    public static final EPredefinedDocumentTypeIdentifier REQUEST_EVIDENCE_LIST = EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_REQUEST_URN_EU_TOOP_REQUEST_EVIDENCE_LIST_1_40;
    public static final EPredefinedDocumentTypeIdentifier RESPONSE_EVIDENCE_LIST = EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_RESPONSE_URN_EU_TOOP_RESPONSE_EVIDENCE_LIST_1_40;
    public static final EPredefinedDocumentTypeIdentifier REQUEST_EVIDENCE = EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_REQUEST_URN_EU_TOOP_REQUEST_EVIDENCE_1_40;
    public static final EPredefinedDocumentTypeIdentifier RESPONSE_EVIDENCE = EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_RESPONSE_URN_EU_TOOP_RESPONSE_EVIDENCE_1_40;
    public static final String DOC_TYPE_SCHEME = "toop-doctypeid-qns";
    private final String m_sName;
    private final String m_sID;
    private final Version m_aSince;
    private final boolean m_bDeprecated;
    private final Version m_aDeprecatedSince;

    private EPredefinedDocumentTypeIdentifier(@Nonnull @Nonempty final String sName,
        @Nonnull @Nonempty final String sID,
        @Nonnull final Version aSince,
        final boolean bDeprecated,
        @Nullable final Version aDeprecatedSince) {
        m_sName = sName;
        m_sID = sID;
        m_aSince = aSince;
        m_bDeprecated = bDeprecated;
        m_aDeprecatedSince = aDeprecatedSince;
    }

    @Nonnull
    @Nonempty
    public String getName() {
        return m_sName;
    }

    @Nonnull
    @Nonempty
    public String getScheme() {
        return DOC_TYPE_SCHEME;
    }

    @Nonnull
    @Nonempty
    public String getID() {
        return m_sID;
    }

    @Nonnull
    @Nonempty
    public String getValue() {
        return m_sID;
    }

    @Nonnull
    public Version getSince() {
        return m_aSince;
    }

    public boolean isDeprecated() {
        return m_bDeprecated;
    }

    @Nullable
    public Version getDeprecatedSince() {
        return m_aDeprecatedSince;
    }

    @Nullable
    public static EPredefinedDocumentTypeIdentifier getFromDocumentTypeIdentifierOrNull(@Nullable final String sID) {
        if (StringHelper.hasText(sID)) {
            for (EPredefinedDocumentTypeIdentifier e: EPredefinedDocumentTypeIdentifier.values()) {
                if (e.getID().equals(sID)) {
                    return e;
                }
            }
        }
        return null;
    }

    @Nullable
    public static EPredefinedDocumentTypeIdentifier getFromDocumentTypeIdentifierOrNull(@Nullable final String sScheme, @Nullable final String sID) {
        if (StringHelper.hasText(sScheme)&&StringHelper.hasText(sID)) {
            for (EPredefinedDocumentTypeIdentifier e: EPredefinedDocumentTypeIdentifier.values()) {
                if (e.getScheme().equals(sScheme)&&e.getID().equals(sID)) {
                    return e;
                }
            }
        }
        return null;
    }
}
