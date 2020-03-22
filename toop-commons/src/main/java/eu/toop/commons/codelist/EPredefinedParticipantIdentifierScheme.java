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
import com.helger.commons.version.Version;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;


/**
 * This file was automatically generated.
 * Do NOT edit!
 */
@CodingStyleguideUnaware
public enum EPredefinedParticipantIdentifierScheme
    implements IPredefined
{

    /**
     * System Information et Repertoire des Entreprise et des Etablissements: SIRENE - <code>0002</code>
     * Scheme agency: Institut National de la Statistique et des Etudes Economiques, (I.N.S.E.E.)<br>
     * Structure of the code: 1) Number of characters: 9 characters (&quot;SIREN&quot;) 14 &quot; 9+5 (&quot;SIRET&quot;), The 9 character number designates an organization, The 14 character number designates a specific establishment of the organization designated by the first 9 characters. 2) Check digits: 9th &amp; 14th character respectively<br>
     * Display requirements: The 9 figure code number (SIREN) is written in groups of 3 characters. Example: 784 301 772, The 14 figure code number is written in 3 groups of 3 characters and a
     * single group of 5. Example: 784 301 772 00025<br>
     * 
     * @since code list v1
     */
    FR_SIRENE("System Information et Repertoire des Entreprise et des Etablissements: SIRENE", "0002", "FR", Version.parse("1"), false, null),

    /**
     * Organisationsnummer - <code>0007</code>
     * Scheme agency: The National Tax Board<br>
     * Structure of the code: 1) 10 digits. 1st digit = Group number, 2nd - 9th digit = Ordinalnumber1st digit, = Group number, 10th digit = Check digit, 2) Last digit.<br>
     * Display requirements: Single group of 10 digits.<br>
     * 
     * @since code list v1
     */
    SE_ORGNR("Organisationsnummer", "0007", "SE", Version.parse("1"), false, null),

    /**
     * SIRET-CODE - <code>0009</code>
     * Scheme agency: DU PONT DE NEMOURS<br>
     * Structure of the code: 1) 14 digits, 2) None<br>
     * Display requirements: In four groups, Groups 1 - 3 = three digits each, Group 4 = five digits<br>
     * 
     * @since code list v1
     */
    FR_SIRET("SIRET-CODE", "0009", "FR", Version.parse("1"), false, null),

    /**
     * LY-tunnus - <code>0037</code>
     * Scheme agency: National Board of Taxes, (Verohallitus)<br>
     * Structure of the code: 1) ICD 4 Digits, Organization code upto 11 characters, Organization name upto 250 characters, 2) None
     * - Example: 00371234567800001
     * - 0037 Country code for Finland (ISO 6523  International Code Designator (ICD) value)
     * - 12345678 Business ID without hyphen 
     * - 00001 Optional specifier for organisation unit (assigned by the organisation itself)<br>
     * Display requirements: None<br>
     * Usage information: OVT identifier conforming to standard ISO6523.   
     * - Constant 0037 (Finnish tax administration organisation code)
     * - Finnish local tax ID, 8 characters with initial zero and no hyphen
     * - Free-format 5 characters, for example profit center.  
     * Example: 003710948874<br>
     * 
     * @since code list v1
     */
    FI_OVT("LY-tunnus", "0037", "FI", Version.parse("1"), false, null),

    /**
     * Data Universal Numbering System (D-U-N-S Number) - <code>0060</code>
     * Scheme agency: Dun and Bradstreet Ltd<br>
     * Structure of the code: 1) 8 digits, 1st-7th digit = number, 8th digit = check number, 2) digit<br>
     * Display requirements: Single group of 8 digits<br>
     * 
     * @since code list v1
     */
    DUNS("Data Universal Numbering System (D-U-N-S Number)", "0060", null, Version.parse("1"), false, null),

    /**
     * Global Location Number - <code>0088</code>
     * Scheme agency: GS1 GLN<br>
     * Structure of the code: 1) Eight identification digits and a check digit. A two digit prefix will be added in the future but it will not be used to calculate the check digit. 2) The Organization name is not part of the D-U-N-S number.<br>
     * Display requirements: IIIIIIIIC where all characters are the digits 0, to 9, I = an identification digit and C = the check digit. When the prefix (P) is added the display requirement will be eleven digits, PPIIIIIIIIC.<br>
     * 
     * @since code list v1
     */
    GLN("Global Location Number", "0088", null, Version.parse("1"), false, null),

    /**
     * DANISH CHAMBER OF COMMERCE Scheme - <code>0096</code>
     * Scheme agency: Danish Chamber of Commerce<br>
     * Structure of the code: 1) 13 digits including check digits, 2) None<br>
     * Display requirements: None<br>
     * 
     * @since code list v1
     */
    DK_P("DANISH CHAMBER OF COMMERCE Scheme", "0096", "DK", Version.parse("1"), false, null),

    /**
     * FTI - Ediforum Italia - <code>0097</code>
     * Scheme agency: FTI - Ediforum Italia<br>
     * Structure of the code: Character repertoire, The EDI identifier consists of digits only. The identifier has a fixed length. No separators are required. Structure: [123] [123456] [123456] [12], 17, &lt; &gt;, A B C D, A: numerical value allocated by the RA to the regional sub-authority, (3 digits), B: numerical value allocated by the sub-authority to the registered organization (mandatory part of the identifier; 6 digits), C: numerical value used by the registered organization (free part; 6 digits), D: numerical check digit calculated by the registered organization; (2 digits), Check digit computation, The check digit is modular 97 computed on ABC as one number.<br>
     * Display requirements: None<br>
     * 
     * @since code list v1
     */
    IT_FTI("FTI - Ediforum Italia", "0097", "IT", Version.parse("1"), false, null),

    /**
     * Vereniging van Kamers van Koophandel en Fabrieken in Nederland (Association of
     * Chambers of Commerce and Industry in the Netherlands), Scheme - <code>0106</code>
     * Scheme agency: Vereniging van Kamers van Koophandel en Fabrieken in Nederland<br>
     * 
     * @since code list v1
     */
    NL_KVK("Vereniging van Kamers van Koophandel en Fabrieken in Nederland (Association of\nChambers of Commerce and Industry in the Netherlands), Scheme", "0106", "NL", Version.parse("1"), false, null),

    /**
     * Directorates of the European Commission - <code>0130</code>
     * Scheme agency: European Commission, Information Directorate, Data Transmission Service<br>
     * Structure of the code: 1) ICD 4 digits, 2) None<br>
     * Display requirements: None<br>
     * 
     * @since code list v2
     */
    EU_NAL("Directorates of the European Commission", "0130", null, Version.parse("2"), false, null),

    /**
     * SIA Object Identifiers - <code>0135</code>
     * Scheme agency: SIA-Società Interbancaria per l'Automazione S.p.A.<br>
     * Structure of the code: Structure of EDI identifier, Character repertoire, The EDI identifier consists of digits only. The identifier has a fixed length. No separators are required. Structure:
     * [1234567] [123] [1] [12345], min 11- max 16, &lt; &gt;, A B C D, A: numerical value (7 digits) assigned by Uffico Provinciale IVA (local branch of Ministry of Finance); B: numerical value a (3 digits) identifying the County; C: numerical check digit (1 digit); D: optional numerical value (up to 5 digits0 used by the registered organization (free part). Check digit computation, The check digit algorithm is the one published in the Gazzetta Ufficiale no 345 of December 29 1976.<br>
     * Display requirements: None<br>
     * 
     * @since code list v1
     */
    IT_SIA("SIA Object Identifiers", "0135", "IT", Version.parse("1"), false, null),

    /**
     * SECETI Object Identifiers - <code>0142</code>
     * Scheme agency: Servizi Centralizzati SECETI S.p.A.<br>
     * Structure of the code: First field: ICD: 4 digits, Second field: sequence of digits<br>
     * Display requirements: None<br>
     * 
     * @since code list v1
     */
    IT_SECETI("SECETI Object Identifiers", "0142", "IT", Version.parse("1"), false, null),

    /**
     * DIGSTORG - <code>0184</code>
     * Scheme agency: DIGSTORG<br>
     * Structure of the code: Defined by Danish Agency for Digitisation<br>
     * 
     * @since code list v1
     */
    DK_DIGST("DIGSTORG", "0184", "DK", Version.parse("1"), false, null),

    /**
     * Organisatie Indentificatie Nummer (OIN) - <code>0190</code>
     * Scheme agency: Logius<br>
     * 
     * @since code list v1
     */
    NL_OINO("Organisatie Indentificatie Nummer (OIN)", "0190", "NL", Version.parse("1"), false, null),

    /**
     * Company code - <code>0191</code>
     * Scheme agency: Centre of Registers and Information Systems of the Ministry of Justice<br>
     * Structure of the code: Always 8-digit number<br>
     * Display requirements: None<br>
     * 
     * @since code list v1
     */
    EE_CC("Company code", "0191", "EE", Version.parse("1"), false, null),

    /**
     * Organisasjonsnummer - <code>0192</code>
     * Scheme agency: The Brønnøysund Register Centre<br>
     * Structure of the code: 9 digits
     * The organization number consists of 9 digits where the last digit is a control digit calculated with standard weights, modulus 11. After this, weights 3, 2, 7, 6, 5, 4, 3 and 2 are calculated from the first digit.<br>
     * Display requirements: None<br>
     * 
     * @since code list v1
     */
    NO_ORG("Organisasjonsnummer", "0192", "NO", Version.parse("1"), false, null),

    /**
     * UBL.BE Party Identifier - <code>0193</code>
     * Scheme agency: UBL.BE<br>
     * Structure of the code: Maximum 50 characters
     *  4 Characters fixed length identifying the type 
     * Maximum 46 characters for the identifier itself<br>
     * Display requirements: None<br>
     * 
     * @since code list v2
     */
    UBLBE("UBL.BE Party Identifier", "0193", "BE", Version.parse("2"), false, null),

    /**
     * Singaport Nationwide E-Invoice Framework - <code>0195</code>
     * Scheme agency: lnfocomm Media Development Authority<br>
     * 
     * @since code list v2
     */
    SG_UEN("Singaport Nationwide E-Invoice Framework", "0195", null, Version.parse("2"), false, null),

    /**
     * Icelandic identifier - <code>0196</code>
     * Scheme agency: Icelandic National Registry<br>
     * 
     * @since code list v2
     */
    IS_KTNR("Icelandic identifier", "0196", "IS", Version.parse("2"), false, null),

    /**
     * Danish Ministry of the Interior and Health - <code>9901</code>
     * Scheme agency: Danish Ministry of the Interior and Health<br>
     * Structure of the code: 1) First field: ICD: 4 digits, Second field: sequence of digits<br>
     * Display requirements: None<br>
     * 
     * @since code list v2
     */
    DK_CPR("Danish Ministry of the Interior and Health", "9901", "DK", Version.parse("2"), false, null),

    /**
     * The Danish Commerce and Companies Agency - <code>9902</code>
     * Scheme agency: The Danish Commerce and Companies Agency<br>
     * Usage information: 7603770123<br>
     * 
     * @since code list v2
     */
    DK_CVR("The Danish Commerce and Companies Agency", "9902", "DK", Version.parse("2"), false, null),

    /**
     * Danish Ministry of Taxation, Central Customs and Tax Administration - <code>9904</code>
     * Scheme agency: Danish Ministry of Taxation, Central Customs and Tax Administration<br>
     * Usage information: DK26769388<br>
     * 
     * @since code list v2
     */
    DK_SE("Danish Ministry of Taxation, Central Customs and Tax Administration", "9904", "DK", Version.parse("2"), false, null),

    /**
     * Danish VANS providers - <code>9905</code>
     * Scheme agency: Danish VANS providers<br>
     * Usage information: DK26769388<br>
     * 
     * @since code list v2
     */
    DK_VANS("Danish VANS providers", "9905", "DK", Version.parse("2"), false, null),

    /**
     * Ufficio responsabile gestione partite IVA - <code>9906</code>
     * Scheme agency: Ufficio responsabile gestione partite IVA<br>
     * Usage information: IT06363391001<br>
     * 
     * @since code list v2
     */
    IT_VAT("Ufficio responsabile gestione partite IVA", "9906", "IT", Version.parse("2"), false, null),

    /**
     * TAX Authority - <code>9907</code>
     * Scheme agency: TAX Authority<br>
     * Usage information: RSSBBR69C48F839A
     * NOTE: The &quot;CF&quot; is a Fiscal Code that can be &quot;personal&quot; or for a &quot;legal entity&quot;.
     * The CF for legal entities is like the Italian VAT code (IT:VAT)<br>
     * 
     * @since code list v2
     */
    IT_CF("TAX Authority", "9907", "IT", Version.parse("2"), false, null),

    /**
     * Organisasjonsnummer - <code>9908</code>
     * Scheme agency: Enhetsregisteret ved Bronnoysundregisterne<br>
     * 
     * @since code list v2
     */
    NO_ORGNR("Organisasjonsnummer", "9908", "NO", Version.parse("2"), false, null),

    /**
     * Hungarian VAT number - <code>9910</code>
     * Usage information: 990399123MVA<br>
     * 
     * @since code list v2
     */
    HU_VAT("Hungarian VAT number", "9910", "HU", Version.parse("2"), false, null),

    /**
     * Business Registers Network - <code>9913</code>
     * Scheme agency: Business Registers Network<br>
     * 
     * @since code list v2
     */
    EU_REID("Business Registers Network", "9913", null, Version.parse("2"), false, null),

    /**
     * Österreichische Umsatzsteuer-Identifikationsnummer - <code>9914</code>
     * Usage information: ATU12345678<br>
     * 
     * @since code list v2
     */
    AT_VAT("\u00d6sterreichische Umsatzsteuer-Identifikationsnummer", "9914", "AT", Version.parse("2"), false, null),

    /**
     * Österreichisches Verwaltungs bzw. Organisationskennzeichen - <code>9915</code>
     * 
     * @since code list v2
     */
    AT_GOV("\u00d6sterreichisches Verwaltungs bzw. Organisationskennzeichen", "9915", "AT", Version.parse("2"), false, null),

    /**
     * IBAN - <code>9918</code>
     * Scheme agency: SOCIETY FOR WORLDWIDE INTERBANK FINANCIAL, TELECOMMUNICATION S.W.I.F.T<br>
     * 
     * @since code list v2
     */
    IBAN("IBAN", "9918", null, Version.parse("2"), false, null),

    /**
     * Kennziffer des Unternehmensregisters - <code>9919</code>
     * Structure of the code: 9 characters in total; letter, number x3, letter, number x3, letter<br>
     * 
     * @since code list v2
     */
    AT_KUR("Kennziffer des Unternehmensregisters", "9919", "AT", Version.parse("2"), false, null),

    /**
     * Agencia Española de Administración Tributaria - <code>9920</code>
     * Scheme agency: Agencia Española de Administración Tributaria<br>
     * 
     * @since code list v2
     */
    ES_VAT("Agencia Espa\u00f1ola de Administraci\u00f3n Tributaria", "9920", "ES", Version.parse("2"), false, null),

    /**
     * Indice delle Pubbliche Amministrazioni - <code>9921</code>
     * Scheme agency: Indice delle Pubbliche Amministrazioni<br>
     * 
     * @since code list v2
     */
    IT_IPA("Indice delle Pubbliche Amministrazioni", "9921", "IT", Version.parse("2"), false, null),

    /**
     * Andorra VAT number - <code>9922</code>
     * 
     * @since code list v2
     */
    AD_VAT("Andorra VAT number", "9922", "AD", Version.parse("2"), false, null),

    /**
     * Albania VAT number - <code>9923</code>
     * 
     * @since code list v2
     */
    AL_VAT("Albania VAT number", "9923", "AL", Version.parse("2"), false, null),

    /**
     * Bosnia and Herzegovina VAT number - <code>9924</code>
     * 
     * @since code list v2
     */
    BA_VAT("Bosnia and Herzegovina VAT number", "9924", "BA", Version.parse("2"), false, null),

    /**
     * Belgium VAT number - <code>9925</code>
     * 
     * @since code list v2
     */
    BE_VAT("Belgium VAT number", "9925", "BE", Version.parse("2"), false, null),

    /**
     * Bulgaria VAT number - <code>9926</code>
     * 
     * @since code list v2
     */
    BG_VAT("Bulgaria VAT number", "9926", "BG", Version.parse("2"), false, null),

    /**
     * Switzerland VAT number - <code>9927</code>
     * 
     * @since code list v2
     */
    CH_VAT("Switzerland VAT number", "9927", "CH", Version.parse("2"), false, null),

    /**
     * Cyprus VAT number - <code>9928</code>
     * 
     * @since code list v2
     */
    CY_VAT("Cyprus VAT number", "9928", "CY", Version.parse("2"), false, null),

    /**
     * Czech Republic VAT number - <code>9929</code>
     * 
     * @since code list v2
     */
    CZ_VAT("Czech Republic VAT number", "9929", "CZ", Version.parse("2"), false, null),

    /**
     * Germany VAT number - <code>9930</code>
     * 
     * @since code list v2
     */
    DE_VAT("Germany VAT number", "9930", "DE", Version.parse("2"), false, null),

    /**
     * Estonia VAT number - <code>9931</code>
     * 
     * @since code list v2
     */
    EE_VAT("Estonia VAT number", "9931", "EE", Version.parse("2"), false, null),

    /**
     * United Kingdom VAT number - <code>9932</code>
     * 
     * @since code list v2
     */
    GB_VAT("United Kingdom VAT number", "9932", "GB", Version.parse("2"), false, null),

    /**
     * Greece VAT number - <code>9933</code>
     * 
     * @since code list v2
     */
    GR_VAT("Greece VAT number", "9933", "GR", Version.parse("2"), false, null),

    /**
     * Croatia VAT number - <code>9934</code>
     * 
     * @since code list v2
     */
    HR_VAT("Croatia VAT number", "9934", "HR", Version.parse("2"), false, null),

    /**
     * Ireland VAT number - <code>9935</code>
     * 
     * @since code list v2
     */
    IE_VAT("Ireland VAT number", "9935", "IE", Version.parse("2"), false, null),

    /**
     * Liechtenstein VAT number - <code>9936</code>
     * 
     * @since code list v2
     */
    LI_VAT("Liechtenstein VAT number", "9936", "LI", Version.parse("2"), false, null),

    /**
     * Lithuania VAT number - <code>9937</code>
     * 
     * @since code list v2
     */
    LT_VAT("Lithuania VAT number", "9937", "LT", Version.parse("2"), false, null),

    /**
     * Luxemburg VAT number - <code>9938</code>
     * 
     * @since code list v2
     */
    LU_VAT("Luxemburg VAT number", "9938", "LU", Version.parse("2"), false, null),

    /**
     * Latvia VAT number - <code>9939</code>
     * 
     * @since code list v2
     */
    LV_VAT("Latvia VAT number", "9939", "LV", Version.parse("2"), false, null),

    /**
     * Monaco VAT number - <code>9940</code>
     * 
     * @since code list v2
     */
    MC_VAT("Monaco VAT number", "9940", "MC", Version.parse("2"), false, null),

    /**
     * Montenegro VAT number - <code>9941</code>
     * 
     * @since code list v2
     */
    ME_VAT("Montenegro VAT number", "9941", "ME", Version.parse("2"), false, null),

    /**
     * Macedonia, the former Yugoslav Republic of VAT number - <code>9942</code>
     * 
     * @since code list v2
     */
    MK_VAT("Macedonia, the former Yugoslav Republic of VAT number", "9942", "MK", Version.parse("2"), false, null),

    /**
     * Malta VAT number - <code>9943</code>
     * 
     * @since code list v2
     */
    MT_VAT("Malta VAT number", "9943", "MT", Version.parse("2"), false, null),

    /**
     * Netherlands VAT number - <code>9944</code>
     * 
     * @since code list v2
     */
    NL_VAT("Netherlands VAT number", "9944", "NL", Version.parse("2"), false, null),

    /**
     * Poland VAT number - <code>9945</code>
     * 
     * @since code list v2
     */
    PL_VAT("Poland VAT number", "9945", "PL", Version.parse("2"), false, null),

    /**
     * Portugal VAT number - <code>9946</code>
     * 
     * @since code list v2
     */
    PT_VAT("Portugal VAT number", "9946", "PT", Version.parse("2"), false, null),

    /**
     * Romania VAT number - <code>9947</code>
     * 
     * @since code list v2
     */
    RO_VAT("Romania VAT number", "9947", "RO", Version.parse("2"), false, null),

    /**
     * Serbia VAT number - <code>9948</code>
     * 
     * @since code list v2
     */
    RS_VAT("Serbia VAT number", "9948", "RS", Version.parse("2"), false, null),

    /**
     * Slovenia VAT number - <code>9949</code>
     * 
     * @since code list v2
     */
    SI_VAT("Slovenia VAT number", "9949", "SI", Version.parse("2"), false, null),

    /**
     * Slovakia VAT number - <code>9950</code>
     * 
     * @since code list v2
     */
    SK_VAT("Slovakia VAT number", "9950", "SK", Version.parse("2"), false, null),

    /**
     * San Marino VAT number - <code>9951</code>
     * 
     * @since code list v2
     */
    SM_VAT("San Marino VAT number", "9951", "SM", Version.parse("2"), false, null),

    /**
     * Turkey VAT number - <code>9952</code>
     * 
     * @since code list v2
     */
    TR_VAT("Turkey VAT number", "9952", "TR", Version.parse("2"), false, null),

    /**
     * Holy See (Vatican City State) VAT number - <code>9953</code>
     * 
     * @since code list v2
     */
    VA_VAT("Holy See (Vatican City State) VAT number", "9953", "VA", Version.parse("2"), false, null),

    /**
     * Swedish VAT number - <code>9955</code>
     * 
     * @since code list v2
     */
    SE_VAT("Swedish VAT number", "9955", "SE", Version.parse("2"), false, null),

    /**
     * Belgian Crossroad Bank of Enterprise number - <code>9956</code>
     * Scheme agency: Belgian Crossroad Bank of Enterprises <br>
     * Structure of the code: Format: 9.999.999.999 - Check: 99 = 97 - (9.999.999.9 modulo 97)<br>
     * 
     * @since code list v2
     */
    BE_CBE("Belgian Crossroad Bank of Enterprise number", "9956", "BE", Version.parse("2"), false, null),

    /**
     * French VAT number - <code>9957</code>
     * 
     * @since code list v2
     */
    FR_VAT("French VAT number", "9957", "FR", Version.parse("2"), false, null),

    /**
     * German Leitweg ID - <code>9958</code>
     * 
     * @since code list v2
     */
    DE_LID("German Leitweg ID", "9958", "DE", Version.parse("2"), false, null);
    private final String m_sName;
    private final String m_sID;
    private final String m_sCountryCode;
    private final Version m_aSince;
    private final boolean m_bDeprecated;
    private final Version m_aDeprecatedSince;

    private EPredefinedParticipantIdentifierScheme(@Nonnull @Nonempty final String sName,
        @Nonnull @Nonempty final String sID,
        @Nullable final String sCountryCode,
        @Nonnull final Version aSince,
        final boolean bDeprecated,
        @Nullable final Version aDeprecatedSince) {
        m_sName = sName;
        m_sID = sID;
        m_sCountryCode = sCountryCode;
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
    public String getID() {
        return m_sID;
    }

    @Nullable
    public String getCountryCode() {
        return m_sCountryCode;
    }

    public boolean isInternational() {
        return (m_sCountryCode == null);
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
}
