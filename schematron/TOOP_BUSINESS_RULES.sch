<?xml version="1.0" encoding="UTF-8"?>
<!--

    Copyright (C) 2018-2020 toop.eu

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

            http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

-->

<schema xmlns="http://purl.oclc.org/dsdl/schematron" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    queryBinding="xslt2" 
    >
    <ns prefix="query"  uri="urn:oasis:names:tc:ebxml-regrep:xsd:query:4.0"/>
    <ns prefix="rim"    uri="urn:oasis:names:tc:ebxml-regrep:xsd:rim:4.0"/>
    <ns prefix="cva"    uri="http://www.w3.org/ns/corevocabulary/AggregateComponents"/>
    <ns prefix="cvb"    uri="http://www.w3.org/ns/corevocabulary/BasicComponents"/>
    <ns prefix="cbd"    uri="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2"/>
    <ns prefix="rs"     uri="urn:oasis:names:tc:ebxml-regrep:xsd:rs:4.0"/>
    <ns prefix="cpov"   uri="http://www.w3.org/ns/corevocabulary/po"/>
    <ns prefix="cagv"   uri="https://semic.org/sa/cv/cagv/agent-2.0.0#"/>
    <ns prefix="cbc"    uri="https://semic.org/sa/cv/common/cbc-2.0.0#"/> 
    <ns prefix="locn"   uri="http://www.w3.org/ns/locn#"/>
    <ns prefix="cccev"  uri="https://semic.org/sa/cv/cccev-2.0.0#"/>
    <ns prefix="dcat"   uri="http://data.europa.eu/r5r/"/>
    <ns prefix="dct"    uri="http://purl.org/dc/terms/"/>
    <ns prefix="xsi"    uri="urn:oasis:names:tc:ebxml-regrep:xsd:query:4.0"/>    
    
    <title>TOOP EDM Business Rules (specs Version 2.0.1)</title>
    
    
    <!--Check the format of the UUID's-->
    <pattern>
        <rule context="query:QueryRequest/@id | query:QueryResponse/@requestId">
            <assert test="matches(normalize-space((.)),'^[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}$','i')" 
                flag='ERROR' id="br_wrong_uuid_format">
                Rule: The UUID MUST be created following the UUID Version 4 specification. 
                Please check <value-of select="name(.)"/>, found: <value-of select="normalize-space((.))"/> .
            </assert>
        </rule>
    </pattern>
    
    
    <!--Check the Specification Identifier-->
    <pattern>
        <rule context="query:QueryRequest | query:QueryResponse">
            <assert test="matches(rim:Slot[@name = 'SpecificationIdentifier']/rim:SlotValue/rim:Value/text(),'toop-edm:v2.0')" 
                flag='ERROR' id="br_mandatory_res_specs_id">
                Rule: The message MUST have the specification identifier "toop-edm:v2.0".
            </assert>
        </rule>
    </pattern>
    
       
    <!--Check if an identifier is valid according to the eIDAS specifications-->
    <pattern>
        <rule context="query:QueryRequest/query:Query/rim:Slot[@name = 'LegalPerson']/rim:SlotValue/cva:CoreBusiness/cvb:LegalEntityID">
            <assert test="matches(normalize-space(text()),'^[a-z]{2}/[a-z]{2}/(.*?)','i')"  
                flag='ERROR' id="wrong_id_format">
                Rule: The uniqueness identifier consists of:
                1. The first part is the Nationality Code of the identifier. This is one of the ISO 3166-1 alpha-2 codes, followed by a slash ("/"))
                2. The second part is the Nationality Code of the destination country or international organization. This is one of the ISO 3166-1 alpha-2 codes, followed by a slash ("/")
                3. The third part a combination of readable characters. This uniquely identifies the identity asserted in the country of origin but does not necessarily reveal any discernible correspondence with the subject's actual identifier (for example, username, fiscal number etc).
                Please check <value-of select="name(.)"/>.
            </assert>
        </rule>
    </pattern>
    
    
    <!--Check for unique ID's in concepts-->
    <pattern>
        <rule context="query:QueryRequest/query:Query/rim:Slot[@name = 'ConceptRequestList']/rim:SlotValue/rim:Element/cccev:concept">
            <assert test="count(//cbc:id) = count(distinct-values(//cbc:id))"
                flag='ERROR' id="br_request_concept_id_not_unique">
                In a QueryRequest,  two or more concepts can not share the same ID.
            </assert>
        </rule>
    </pattern>
    
    
    <!--Check for unique QNames in same level concepts-->
    <pattern>
        <rule context="query:QueryRequest/query:Query/rim:Slot[@name = 'ConceptRequestList']/rim:SlotValue/rim:Element/cccev:concept">
            <assert test="count(cccev:concept/cbc:QName) = count(distinct-values(cccev:concept/cbc:QName))"
                flag='ERROR' id="br_request_concept_qname_not_unique">
                In a QueryRequest,  two or more concepts at the same level (with a common parent) can not share the same Qname. 
            </assert>
        </rule>
    </pattern>
    
    
    <!--***********************-->
    <!--*RULES USING CODELISTS*-->
    <!--***********************-->  
    
    <!--Check codelist for gender-->
    <pattern> 
        <let name="gendertypecodes" value="document('..\codelists\gc\Gender-CodeList.gc')//Value[@ColumnRef='code']" />
        <rule context="query:QueryRequest/query:Query/rim:Slot[@name = 'NaturalPerson']/rim:SlotValue/cva:CorePerson/cvb:PersonGenderCode 
            | query:QueryRequest/query:Query/rim:Slot[@name = 'AuthorizedRepresentative']/rim:SlotValue/cva:CorePerson/cvb:PersonGenderCode">
            <assert test="$gendertypecodes/SimpleValue[normalize-space(.) = normalize-space(current()/.)]"
                flag='ERROR' id="br_check_gender_code">
                A gender code must always be specified using the correct code list. 
            </assert> 
        </rule> 
    </pattern> 
    
    
    <!--Check codelist for country-->
    <pattern> 
        <let name="countrycodes" value="document('..\codelists\std-gc\CountryIdentificationCode-2.1.gc')//Value[@ColumnRef='code']" />
        <rule context="cva:PersonCoreAddress/cvb:AddressAdminUnitLocationOne 
            | cva:LegalEntityCoreAddress/cvb:AddressAdminUnitLocationOne 
            | query:QueryRequest/rim:Slot[@name = 'DataConsumer']/rim:SlotValue/cagv:Agent/cagv:location/locn:address/locn:adminUnitLevel1"            
            flag='ERROR' id='gc_check_country_countrycode'> 
            <assert test="$countrycodes/SimpleValue[normalize-space(.) = normalize-space(current()/.)]">The country code must always be specified using the correct code list. Please check <value-of select="name(.)"/>.</assert> 
        </rule> 
        <rule context="query:QueryRequest/query:Query/rim:Slot[@name = 'LegalPerson']/rim:SlotValue/cva:CoreBusiness/cvb:LegalEntityID" 
            flag='ERROR' id='gc_check_id_countrycode'>
            <assert test="$countrycodes/SimpleValue[normalize-space(.) = (tokenize(normalize-space(current()/.),'/')[1])]">The country code in the first part of the identifier must always be specified using the correct code list (found:<value-of select="(tokenize(normalize-space(current()/.),'/')[1])"/>).</assert> 
            <assert test="$countrycodes/SimpleValue[normalize-space(.) = (tokenize(normalize-space(current()/.),'/')[2])]">The country code in the second part of the identifier must always be specified using the correct code list (found:<value-of select="(tokenize(normalize-space(current()/.),'/')[2])"/>).</assert> 
        </rule>
    </pattern> 
    
    
</schema>