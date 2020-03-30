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
import com.helger.peppolid.IProcessIdentifier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;


/**
 * This file was automatically generated.
 * Do NOT edit!
 */
@CodingStyleguideUnaware
public enum EPredefinedProcessIdentifier
    implements IProcessIdentifier, IPredefined
{

    /**
     * TOOP Request Response for Data - <code>urn:eu.toop.process.datarequestresponse</code><br>
     * 
     * @since code list v1
     */
    URN_EU_TOOP_PROCESS_DATAREQUESTRESPONSE("TOOP Request Response for Data", "urn:eu.toop.process.datarequestresponse", Version.parse("1"), false, null),

    /**
     * TOOP Request Response for Documents - <code>urn:eu.toop.process.documentrequestresponse</code><br>
     * 
     * @since code list v1
     */
    URN_EU_TOOP_PROCESS_DOCUMENTREQUESTRESPONSE("TOOP Request Response for Documents", "urn:eu.toop.process.documentrequestresponse", Version.parse("1"), false, null),

    /**
     * TOOP Two Phased Request Response for Documents - <code>urn:eu.toop.process.twophasedrequestresponse</code><br>
     * 
     * @since code list v2
     */
    URN_EU_TOOP_PROCESS_TWOPHASEDREQUESTRESPONSE("TOOP Two Phased Request Response for Documents", "urn:eu.toop.process.twophasedrequestresponse", Version.parse("2"), false, null);
    public static final EPredefinedProcessIdentifier DATAREQUESTRESPONSE = EPredefinedProcessIdentifier.URN_EU_TOOP_PROCESS_DATAREQUESTRESPONSE;
    public static final EPredefinedProcessIdentifier DOCUMENTREQUESTRESPONSE = EPredefinedProcessIdentifier.URN_EU_TOOP_PROCESS_DOCUMENTREQUESTRESPONSE;
    public static final EPredefinedProcessIdentifier TWOPHASEDREQUESTRESPONSE = EPredefinedProcessIdentifier.URN_EU_TOOP_PROCESS_TWOPHASEDREQUESTRESPONSE;
    public static final String PROCESS_SCHEME = "toop-procid-agreement";
    private final String m_sName;
    private final String m_sID;
    private final Version m_aSince;
    private final boolean m_bDeprecated;
    private final Version m_aDeprecatedSince;

    private EPredefinedProcessIdentifier(@Nonnull @Nonempty final String sName,
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
        return PROCESS_SCHEME;
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
    public static EPredefinedProcessIdentifier getFromProcessIdentifierOrNull(@Nullable final String sID) {
        if (StringHelper.hasText(sID)) {
            for (EPredefinedProcessIdentifier e: EPredefinedProcessIdentifier.values()) {
                if (e.getID().equals(sID)) {
                    return e;
                }
            }
        }
        return null;
    }

    @Nullable
    public static EPredefinedProcessIdentifier getFromProcessIdentifierOrNull(@Nullable final String sScheme, @Nullable final String sID) {
        if (StringHelper.hasText(sScheme)&&StringHelper.hasText(sID)) {
            for (EPredefinedProcessIdentifier e: EPredefinedProcessIdentifier.values()) {
                if (e.getScheme().equals(sScheme)&&e.getID().equals(sID)) {
                    return e;
                }
            }
        }
        return null;
    }
}
