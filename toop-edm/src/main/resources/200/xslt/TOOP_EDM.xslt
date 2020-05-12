<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<xsl:stylesheet xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:cagv="https://semic.org/sa/cv/cagv/agent-2.0.0#" xmlns:cbc="https://semic.org/sa/cv/common/cbc-2.0.0#" xmlns:cbd="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:cccev="https://semic.org/sa/cv/cccev-2.0.0#" xmlns:cpov="http://www.w3.org/ns/corevocabulary/po" xmlns:cva="http://www.w3.org/ns/corevocabulary/AggregateComponents" xmlns:cvb="http://www.w3.org/ns/corevocabulary/BasicComponents" xmlns:dcat="http://data.europa.eu/r5r/" xmlns:dct="http://purl.org/dc/terms/" xmlns:iso="http://purl.oclc.org/dsdl/schematron" xmlns:locn="http://www.w3.org/ns/locn#" xmlns:query="urn:oasis:names:tc:ebxml-regrep:xsd:query:4.0" xmlns:rim="urn:oasis:names:tc:ebxml-regrep:xsd:rim:4.0" xmlns:rs="urn:oasis:names:tc:ebxml-regrep:xsd:rs:4.0" xmlns:saxon="http://saxon.sf.net/" xmlns:schold="http://www.ascc.net/xml/schematron" xmlns:xhtml="http://www.w3.org/1999/xhtml" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="urn:oasis:names:tc:ebxml-regrep:xsd:query:4.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
<!--Implementers: please note that overriding process-prolog or process-root is 
    the preferred method for meta-stylesheets to use where possible. -->

<xsl:param name="archiveDirParameter" />
  <xsl:param name="archiveNameParameter" />
  <xsl:param name="fileNameParameter" />
  <xsl:param name="fileDirParameter" />
  <xsl:variable name="document-uri">
    <xsl:value-of select="document-uri(/)" />
  </xsl:variable>

<!--PHASES-->


<!--PROLOG-->
<xsl:output indent="yes" method="xml" omit-xml-declaration="no" standalone="yes" />

<!--XSD TYPES FOR XSLT2-->


<!--KEYS AND FUNCTIONS-->


<!--DEFAULT RULES-->


<!--MODE: SCHEMATRON-SELECT-FULL-PATH-->
<!--This mode can be used to generate an ugly though full XPath for locators-->
<xsl:template match="*" mode="schematron-select-full-path">
    <xsl:apply-templates mode="schematron-get-full-path" select="." />
  </xsl:template>

<!--MODE: SCHEMATRON-FULL-PATH-->
<!--This mode can be used to generate an ugly though full XPath for locators-->
<xsl:template match="*" mode="schematron-get-full-path">
    <xsl:apply-templates mode="schematron-get-full-path" select="parent::*" />
    <xsl:text>/</xsl:text>
    <xsl:choose>
      <xsl:when test="namespace-uri()=''">
        <xsl:value-of select="name()" />
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>*:</xsl:text>
        <xsl:value-of select="local-name()" />
        <xsl:text>[namespace-uri()='</xsl:text>
        <xsl:value-of select="namespace-uri()" />
        <xsl:text>']</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="preceding" select="count(preceding-sibling::*[local-name()=local-name(current())                                   and namespace-uri() = namespace-uri(current())])" />
    <xsl:text>[</xsl:text>
    <xsl:value-of select="1+ $preceding" />
    <xsl:text>]</xsl:text>
  </xsl:template>
  <xsl:template match="@*" mode="schematron-get-full-path">
    <xsl:apply-templates mode="schematron-get-full-path" select="parent::*" />
    <xsl:text>/</xsl:text>
    <xsl:choose>
      <xsl:when test="namespace-uri()=''">@<xsl:value-of select="name()" />
</xsl:when>
      <xsl:otherwise>
        <xsl:text>@*[local-name()='</xsl:text>
        <xsl:value-of select="local-name()" />
        <xsl:text>' and namespace-uri()='</xsl:text>
        <xsl:value-of select="namespace-uri()" />
        <xsl:text>']</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

<!--MODE: SCHEMATRON-FULL-PATH-2-->
<!--This mode can be used to generate prefixed XPath for humans-->
<xsl:template match="node() | @*" mode="schematron-get-full-path-2">
    <xsl:for-each select="ancestor-or-self::*">
      <xsl:text>/</xsl:text>
      <xsl:value-of select="name(.)" />
      <xsl:if test="preceding-sibling::*[name(.)=name(current())]">
        <xsl:text>[</xsl:text>
        <xsl:value-of select="count(preceding-sibling::*[name(.)=name(current())])+1" />
        <xsl:text>]</xsl:text>
      </xsl:if>
    </xsl:for-each>
    <xsl:if test="not(self::*)">
      <xsl:text />/@<xsl:value-of select="name(.)" />
    </xsl:if>
  </xsl:template>
<!--MODE: SCHEMATRON-FULL-PATH-3-->
<!--This mode can be used to generate prefixed XPath for humans 
	(Top-level element has index)-->

<xsl:template match="node() | @*" mode="schematron-get-full-path-3">
    <xsl:for-each select="ancestor-or-self::*">
      <xsl:text>/</xsl:text>
      <xsl:value-of select="name(.)" />
      <xsl:if test="parent::*">
        <xsl:text>[</xsl:text>
        <xsl:value-of select="count(preceding-sibling::*[name(.)=name(current())])+1" />
        <xsl:text>]</xsl:text>
      </xsl:if>
    </xsl:for-each>
    <xsl:if test="not(self::*)">
      <xsl:text />/@<xsl:value-of select="name(.)" />
    </xsl:if>
  </xsl:template>

<!--MODE: GENERATE-ID-FROM-PATH -->
<xsl:template match="/" mode="generate-id-from-path" />
  <xsl:template match="text()" mode="generate-id-from-path">
    <xsl:apply-templates mode="generate-id-from-path" select="parent::*" />
    <xsl:value-of select="concat('.text-', 1+count(preceding-sibling::text()), '-')" />
  </xsl:template>
  <xsl:template match="comment()" mode="generate-id-from-path">
    <xsl:apply-templates mode="generate-id-from-path" select="parent::*" />
    <xsl:value-of select="concat('.comment-', 1+count(preceding-sibling::comment()), '-')" />
  </xsl:template>
  <xsl:template match="processing-instruction()" mode="generate-id-from-path">
    <xsl:apply-templates mode="generate-id-from-path" select="parent::*" />
    <xsl:value-of select="concat('.processing-instruction-', 1+count(preceding-sibling::processing-instruction()), '-')" />
  </xsl:template>
  <xsl:template match="@*" mode="generate-id-from-path">
    <xsl:apply-templates mode="generate-id-from-path" select="parent::*" />
    <xsl:value-of select="concat('.@', name())" />
  </xsl:template>
  <xsl:template match="*" mode="generate-id-from-path" priority="-0.5">
    <xsl:apply-templates mode="generate-id-from-path" select="parent::*" />
    <xsl:text>.</xsl:text>
    <xsl:value-of select="concat('.',name(),'-',1+count(preceding-sibling::*[name()=name(current())]),'-')" />
  </xsl:template>

<!--MODE: GENERATE-ID-2 -->
<xsl:template match="/" mode="generate-id-2">U</xsl:template>
  <xsl:template match="*" mode="generate-id-2" priority="2">
    <xsl:text>U</xsl:text>
    <xsl:number count="*" level="multiple" />
  </xsl:template>
  <xsl:template match="node()" mode="generate-id-2">
    <xsl:text>U.</xsl:text>
    <xsl:number count="*" level="multiple" />
    <xsl:text>n</xsl:text>
    <xsl:number count="node()" />
  </xsl:template>
  <xsl:template match="@*" mode="generate-id-2">
    <xsl:text>U.</xsl:text>
    <xsl:number count="*" level="multiple" />
    <xsl:text>_</xsl:text>
    <xsl:value-of select="string-length(local-name(.))" />
    <xsl:text>_</xsl:text>
    <xsl:value-of select="translate(name(),':','.')" />
  </xsl:template>
<!--Strip characters-->  <xsl:template match="text()" priority="-1" />

<!--SCHEMA SETUP-->
<xsl:template match="/">
    <svrl:schematron-output schemaVersion="" title="TOOP EDM Rules (specs Version 2.0.1)">
      <xsl:comment>
        <xsl:value-of select="$archiveDirParameter" />   
		 <xsl:value-of select="$archiveNameParameter" />  
		 <xsl:value-of select="$fileNameParameter" />  
		 <xsl:value-of select="$fileDirParameter" />
      </xsl:comment>
      <svrl:ns-prefix-in-attribute-values prefix="query" uri="urn:oasis:names:tc:ebxml-regrep:xsd:query:4.0" />
      <svrl:ns-prefix-in-attribute-values prefix="rim" uri="urn:oasis:names:tc:ebxml-regrep:xsd:rim:4.0" />
      <svrl:ns-prefix-in-attribute-values prefix="cva" uri="http://www.w3.org/ns/corevocabulary/AggregateComponents" />
      <svrl:ns-prefix-in-attribute-values prefix="cvb" uri="http://www.w3.org/ns/corevocabulary/BasicComponents" />
      <svrl:ns-prefix-in-attribute-values prefix="cbd" uri="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" />
      <svrl:ns-prefix-in-attribute-values prefix="rs" uri="urn:oasis:names:tc:ebxml-regrep:xsd:rs:4.0" />
      <svrl:ns-prefix-in-attribute-values prefix="cpov" uri="http://www.w3.org/ns/corevocabulary/po" />
      <svrl:ns-prefix-in-attribute-values prefix="cagv" uri="https://semic.org/sa/cv/cagv/agent-2.0.0#" />
      <svrl:ns-prefix-in-attribute-values prefix="cbc" uri="https://semic.org/sa/cv/common/cbc-2.0.0#" />
      <svrl:ns-prefix-in-attribute-values prefix="locn" uri="http://www.w3.org/ns/locn#" />
      <svrl:ns-prefix-in-attribute-values prefix="cccev" uri="https://semic.org/sa/cv/cccev-2.0.0#" />
      <svrl:ns-prefix-in-attribute-values prefix="dcat" uri="http://data.europa.eu/r5r/" />
      <svrl:ns-prefix-in-attribute-values prefix="dct" uri="http://purl.org/dc/terms/" />
      <svrl:ns-prefix-in-attribute-values prefix="xsi" uri="urn:oasis:names:tc:ebxml-regrep:xsd:query:4.0" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M15" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M16" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M17" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M18" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M19" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M20" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M21" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M22" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M23" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M24" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M25" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M26" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M27" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M28" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M29" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M30" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M31" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M32" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M33" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M34" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M35" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M36" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M37" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M38" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M39" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M40" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M41" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M42" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M43" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M44" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M45" select="/" />
    </svrl:schematron-output>
  </xsl:template>

<!--SCHEMATRON PATTERNS-->
<svrl:text>TOOP EDM Rules (specs Version 2.0.1)</svrl:text>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="/" mode="M15" priority="1000">
    <svrl:fired-rule context="/" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( (exists(query:QueryRequest)) or (exists(query:QueryResponse)) )" />
      <xsl:otherwise>
        <svrl:failed-assert test="( (exists(query:QueryRequest)) or (exists(query:QueryResponse)) )">
          <xsl:attribute name="id">mandatory_request_or_response</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The message must contain either a QueryRequest or a QueryResponse. Please check if the namespace is correct.
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M15" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M15" priority="-1" />
  <xsl:template match="@*|node()" mode="M15" priority="-2">
    <xsl:apply-templates mode="M15" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="query:QueryRequest" mode="M16" priority="1000">
    <svrl:fired-rule context="query:QueryRequest" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="exists(@id)" />
      <xsl:otherwise>
        <svrl:failed-assert test="exists(@id)">
          <xsl:attribute name="id">mandatory_request_id</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The QueryRequest must contain an id attribute.
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countIssueDateTime" select="count(rim:Slot[@name = 'IssueDateTime'])" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countIssueDateTime=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countIssueDateTime=1)">
          <xsl:attribute name="id">req_card_IssueDateTime</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The QueryRequest must contain exactly ONE IssueDateTime slot (found: <xsl:text />
            <xsl:value-of select="$countIssueDateTime" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countProcedure" select="count(rim:Slot[@name = 'Procedure'])" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countProcedure=0) or ($countProcedure=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countProcedure=0) or ($countProcedure=1)">
          <xsl:attribute name="id">req_card_Procedure</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The QueryRequest must contain ZERO or ONE Procedure slots (found: <xsl:text />
            <xsl:value-of select="$countProcedure" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countDataConsumer" select="count(rim:Slot[@name = 'DataConsumer'])" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countDataConsumer=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countDataConsumer=1)">
          <xsl:attribute name="id">req_card_DataConsumer</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The QueryRequest must contain exactly ONE DataConsumer slot (found: <xsl:text />
            <xsl:value-of select="$countDataConsumer" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countConsentToken" select="count(rim:Slot[@name = 'ConsentToken'])" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countConsentToken=0) or ($countConsentToken=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countConsentToken=0) or ($countConsentToken=1)">
          <xsl:attribute name="id">req_card_ConsentToken</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The QueryRequest must contain ZERO or ONE ConsentToken slots (found: <xsl:text />
            <xsl:value-of select="$countConsentToken" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countDatasetIdentifier" select="count(rim:Slot[@name = 'DatasetIdentifier'])" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countDatasetIdentifier=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countDatasetIdentifier=1)">
          <xsl:attribute name="id">req_card_DatasetIdentifier</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The QueryRequest must contain exactly ONE DatasetIdentifier slot (found: <xsl:text />
            <xsl:value-of select="$countDatasetIdentifier" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countQuery" select="count(query:Query)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countQuery=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countQuery=1)">
          <xsl:attribute name="id">req_card_Query</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The QueryRequest must contain exactly ONE Query slot (found: <xsl:text />
            <xsl:value-of select="$countQuery" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M16" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M16" priority="-1" />
  <xsl:template match="@*|node()" mode="M16" priority="-2">
    <xsl:apply-templates mode="M16" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="query:QueryResponse" mode="M17" priority="1000">
    <svrl:fired-rule context="query:QueryResponse" />
    <xsl:variable name="IAMERROR" select="exists(rs:Exception)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="exists(@requestId)" />
      <xsl:otherwise>
        <svrl:failed-assert test="exists(@requestId)">
          <xsl:attribute name="id">mandatory_response_requestId</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The QueryResponse must contain a requestId attribute.
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countIssueDateTime" select="count(rim:Slot[@name = 'IssueDateTime'])" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( ($countIssueDateTime=1) or ($IAMERROR=true()) )" />
      <xsl:otherwise>
        <svrl:failed-assert test="( ($countIssueDateTime=1) or ($IAMERROR=true()) )">
          <xsl:attribute name="id">res_card_IssueDateTime</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The QueryResponse must contain exactly ONE IssueDateTime slot (found: <xsl:text />
            <xsl:value-of select="$countIssueDateTime" />
            <xsl:text />).  
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countDataProvider" select="count(rim:Slot[@name = 'DataProvider'])" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countDataProvider=1) or ($IAMERROR=true())" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countDataProvider=1) or ($IAMERROR=true())">
          <xsl:attribute name="id">res_card_DataProvider</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The QueryResponse must contain exactly ONE DataProvider slot (found: <xsl:text />
            <xsl:value-of select="$countDataProvider" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countRegistryObjectList" select="count(rim:RegistryObjectList)" />
    <xsl:variable name="countObjectRefList" select="count(rim:ObjectRefList)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countRegistryObjectList+$countObjectRefList=1) or ($IAMERROR=true())" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countRegistryObjectList+$countObjectRefList=1) or ($IAMERROR=true())">
          <xsl:attribute name="id">res_card_RegistryObjectList</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The QueryResponse must contain either exactly ONE RegistryObjectList or exactly ONE ObjectRefList.
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M17" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M17" priority="-1" />
  <xsl:template match="@*|node()" mode="M17" priority="-2">
    <xsl:apply-templates mode="M17" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="query:QueryResponse" mode="M18" priority="1000">
    <svrl:fired-rule context="query:QueryResponse" />
    <xsl:variable name="IAMERROR" select="exists(rs:Exception)" />
    <xsl:variable name="countExceptionErrorProvider" select="count(rim:Slot[@name = 'ErrorProvider'])" />

		<!--REPORT -->
<xsl:if test="( ($countExceptionErrorProvider > 1) and ($IAMERROR=true()) )">
      <svrl:successful-report test="( ($countExceptionErrorProvider > 1) and ($IAMERROR=true()) )">
        <xsl:attribute name="id">exc_card_ErrorProvider</xsl:attribute>
        <xsl:attribute name="flag">ERROR</xsl:attribute>
        <xsl:attribute name="location">
          <xsl:apply-templates mode="schematron-select-full-path" select="." />
        </xsl:attribute>
        <svrl:text>
                A QueryResponse including Exceptions must contain ZERO or ONE ErrorProvider slots (found: <xsl:text />
          <xsl:value-of select="$countExceptionErrorProvider" />
          <xsl:text />).  
            </svrl:text>
      </svrl:successful-report>
    </xsl:if>
    <xsl:apply-templates mode="M18" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M18" priority="-1" />
  <xsl:template match="@*|node()" mode="M18" priority="-2">
    <xsl:apply-templates mode="M18" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="query:QueryResponse/rim:Slot[@name = 'ErrorProvider']" mode="M19" priority="1000">
    <svrl:fired-rule context="query:QueryResponse/rim:Slot[@name = 'ErrorProvider']" />
    <xsl:variable name="countAgent" select="count(rim:SlotValue/cagv:Agent)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( ($countAgent = 1) )" />
      <xsl:otherwise>
        <svrl:failed-assert test="( ($countAgent = 1) )">
          <xsl:attribute name="id">exc_dc_card_Agent</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The ErrorProvider slot must contain exactly ONE Agent (found: <xsl:text />
            <xsl:value-of select="$countAgent" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M19" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M19" priority="-1" />
  <xsl:template match="@*|node()" mode="M19" priority="-2">
    <xsl:apply-templates mode="M19" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="query:QueryResponse/rs:Exception" mode="M20" priority="1000">
    <svrl:fired-rule context="query:QueryResponse/rs:Exception" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="exists(@severity)" />
      <xsl:otherwise>
        <svrl:failed-assert test="exists(@severity)">
          <xsl:attribute name="id">mandatory_exception_severity</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The Exception must contain a severity attribute.
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="exists(@message)" />
      <xsl:otherwise>
        <svrl:failed-assert test="exists(@message)">
          <xsl:attribute name="id">mandatory_exception_message</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The Exception must contain a message attribute.
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countExceptionTimeStamp" select="count(rim:Slot[@name = 'Timestamp'])" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( ($countExceptionTimeStamp=1) )" />
      <xsl:otherwise>
        <svrl:failed-assert test="( ($countExceptionTimeStamp=1) )">
          <xsl:attribute name="id">exc_card_TimeStamp</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                Each Exception must contain exactly ONE TimeStamp slot (found: <xsl:text />
            <xsl:value-of select="$countExceptionTimeStamp" />
            <xsl:text />).  
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countExceptionOrigin" select="count(rim:Slot[@name = 'ErrorOrigin'])" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( ($countExceptionOrigin=1) )" />
      <xsl:otherwise>
        <svrl:failed-assert test="( ($countExceptionOrigin=1) )">
          <xsl:attribute name="id">exc_card_Origin</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                Each Exception must contain ZERO or ONE ErrorOrigin slots (found: <xsl:text />
            <xsl:value-of select="$countExceptionOrigin" />
            <xsl:text />).  
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M20" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M20" priority="-1" />
  <xsl:template match="@*|node()" mode="M20" priority="-2">
    <xsl:apply-templates mode="M20" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="query:QueryRequest | query:QueryResponse" mode="M21" priority="1000">
    <svrl:fired-rule context="query:QueryRequest | query:QueryResponse" />
    <xsl:variable name="countSpecificationIdentifier" select="count(rim:Slot[@name = 'SpecificationIdentifier'])" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countSpecificationIdentifier=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countSpecificationIdentifier=1)">
          <xsl:attribute name="id">req_card_SpecificationIdentifier</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
            The message must contain exactly ONE SpecificationIdentifier slot (found: <xsl:text />
            <xsl:value-of select="$countSpecificationIdentifier" />
            <xsl:text />).
        </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M21" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M21" priority="-1" />
  <xsl:template match="@*|node()" mode="M21" priority="-2">
    <xsl:apply-templates mode="M21" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="query:QueryRequest/rim:Slot[@name = 'DataConsumer']" mode="M22" priority="1000">
    <svrl:fired-rule context="query:QueryRequest/rim:Slot[@name = 'DataConsumer']" />
    <xsl:variable name="countAgent" select="count(./rim:SlotValue/cagv:Agent)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countAgent=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countAgent=1)">
          <xsl:attribute name="id">req_dc_card_Agent</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The DataConsumer slot must contain exactly ONE Agent (found: <xsl:text />
            <xsl:value-of select="$countAgent" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M22" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M22" priority="-1" />
  <xsl:template match="@*|node()" mode="M22" priority="-2">
    <xsl:apply-templates mode="M22" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="query:QueryRequest/rim:Slot[@name = 'DataConsumer']/rim:SlotValue/cagv:Agent" mode="M23" priority="1000">
    <svrl:fired-rule context="query:QueryRequest/rim:Slot[@name = 'DataConsumer']/rim:SlotValue/cagv:Agent" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="exists(cbc:id)" />
      <xsl:otherwise>
        <svrl:failed-assert test="exists(cbc:id)">
          <xsl:attribute name="id">req_agent_mandatory_id</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The Agent must have an Id.
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="exists(cbc:name)" />
      <xsl:otherwise>
        <svrl:failed-assert test="exists(cbc:name)">
          <xsl:attribute name="id">req_agent_mandatory_name</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The Agent must have a name.
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countLocation" select="count(cagv:location)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countLocation=0) or ($countLocation=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countLocation=0) or ($countLocation=1)">
          <xsl:attribute name="id">req_agent_card_location</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The Agent must have ZERO or ONE Location elements (found: <xsl:text />
            <xsl:value-of select="$countLocation" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countAgentAddressFullAddress" select="count(cagv:location/locn:address/locn:fullAddress)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countAgentAddressFullAddress &lt; 4)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countAgentAddressFullAddress &lt; 4)">
          <xsl:attribute name="id">req_card_Agent_Address_FullAddress</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The Agent Address must contain UP TO THREE AddressFullAddress elements (found: <xsl:text />
            <xsl:value-of select="$countAgentAddressFullAddress" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countAgentAddressThoroughfare" select="count(cagv:location/locn:address/locn:thoroughfare)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countAgentAddressThoroughfare=0) or ($countAgentAddressThoroughfare=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countAgentAddressThoroughfare=0) or ($countAgentAddressThoroughfare=1)">
          <xsl:attribute name="id">req_card_Agent_Address_AddressThoroughfare</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The Agent Address must contain ZERO or ONE AddressThoroughfare elements (found: <xsl:text />
            <xsl:value-of select="$countAgentAddressThoroughfare" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countAgentAddressLocatorDesignator" select="count(cagv:location/locn:address/locn:thoroughfare)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countAgentAddressLocatorDesignator=0) or ($countAgentAddressLocatorDesignator=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countAgentAddressLocatorDesignator=0) or ($countAgentAddressLocatorDesignator=1)">
          <xsl:attribute name="id">req_card_Agent_Address_AddressLocatorDesignator</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The Agent Address must contain ZERO or ONE AddressLocatorDesignator elements (found: <xsl:text />
            <xsl:value-of select="$countAgentAddressLocatorDesignator" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countAgentAddressPostName" select="count(cagv:location/locn:address/locn:thoroughfare)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countAgentAddressPostName=0) or ($countAgentAddressPostName=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countAgentAddressPostName=0) or ($countAgentAddressPostName=1)">
          <xsl:attribute name="id">req_card_Agent_Address_AddressPostName</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The Agent Address must contain ZERO or ONE AddressPostName elements (found: <xsl:text />
            <xsl:value-of select="$countAgentAddressPostName" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countAgentAddressAdminUnitLocationOne" select="count(cagv:location/locn:address/locn:thoroughfare)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countAgentAddressAdminUnitLocationOne=0) or ($countAgentAddressAdminUnitLocationOne=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countAgentAddressAdminUnitLocationOne=0) or ($countAgentAddressAdminUnitLocationOne=1)">
          <xsl:attribute name="id">req_card_Agent_Address_AddressAdminUnitLocationOne</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The Agent Address must contain ZERO or ONE AddressAdminUnitLocationOne elements (found: <xsl:text />
            <xsl:value-of select="$countAgentAddressAdminUnitLocationOne" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countAgentAddressPostCode" select="count(cagv:location/locn:address/locn:thoroughfare)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countAgentAddressPostCode=0) or ($countAgentAddressPostCode=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countAgentAddressPostCode=0) or ($countAgentAddressPostCode=1)">
          <xsl:attribute name="id">req_card_Agent_Address_AddressPostCode</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The Agent Address must contain ZERO or ONE AddressPostCode elements (found: <xsl:text />
            <xsl:value-of select="$countAgentAddressPostCode" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M23" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M23" priority="-1" />
  <xsl:template match="@*|node()" mode="M23" priority="-2">
    <xsl:apply-templates mode="M23" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="query:QueryRequest/query:Query" mode="M24" priority="1000">
    <svrl:fired-rule context="query:QueryRequest/query:Query" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="exists(@queryDefinition)" />
      <xsl:otherwise>
        <svrl:failed-assert test="exists(@queryDefinition)">
          <xsl:attribute name="id">mandatory_querydefinition</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The Query must contain a queryDefinition attribute.
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( (exists(rim:Slot[@name = 'ConceptRequestList'])) or (exists(rim:Slot[@name = 'DistributionRequestList'])) or (exists(rim:Slot[@name = 'id'])) )" />
      <xsl:otherwise>
        <svrl:failed-assert test="( (exists(rim:Slot[@name = 'ConceptRequestList'])) or (exists(rim:Slot[@name = 'DistributionRequestList'])) or (exists(rim:Slot[@name = 'id'])) )">
          <xsl:attribute name="id">mandatory_query_concept_or_distribution_request_list</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The Query must contain either a ConceptRequestList, a DistributionRequestList, or an id (for two-step queries).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--REPORT -->
<xsl:if test="( (exists(rim:Slot[@name = 'ConceptRequestList'])) and (exists(rim:Slot[@name = 'DistributionRequestList'])) )">
      <svrl:successful-report test="( (exists(rim:Slot[@name = 'ConceptRequestList'])) and (exists(rim:Slot[@name = 'DistributionRequestList'])) )">
        <xsl:attribute name="id">alternative_query_concept_or_distribution_request_list</xsl:attribute>
        <xsl:attribute name="flag">ERROR</xsl:attribute>
        <xsl:attribute name="location">
          <xsl:apply-templates mode="schematron-select-full-path" select="." />
        </xsl:attribute>
        <svrl:text>
                The Query cannot contain both a ConceptRequestList and a DistributionRequestList.
            </svrl:text>
      </svrl:successful-report>
    </xsl:if>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( (exists(rim:Slot[@name = 'LegalPerson'])) or (exists(rim:Slot[@name = 'NaturalPerson'])) )" />
      <xsl:otherwise>
        <svrl:failed-assert test="( (exists(rim:Slot[@name = 'LegalPerson'])) or (exists(rim:Slot[@name = 'NaturalPerson'])) )">
          <xsl:attribute name="id">mandatory_query_legal_or_natural_person</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The Query must contain either a LegalPerson or a NaturalPerson.
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--REPORT -->
<xsl:if test="( (exists(rim:Slot[@name = 'LegalPerson'])) and (exists(rim:Slot[@name = 'NaturalPerson'])) )">
      <svrl:successful-report test="( (exists(rim:Slot[@name = 'LegalPerson'])) and (exists(rim:Slot[@name = 'NaturalPerson'])) )">
        <xsl:attribute name="id">alternative_query_legal_or_natural_person</xsl:attribute>
        <xsl:attribute name="flag">ERROR</xsl:attribute>
        <xsl:attribute name="location">
          <xsl:apply-templates mode="schematron-select-full-path" select="." />
        </xsl:attribute>
        <svrl:text>
                The Query cannot contain both a LegalPerson and a NaturalPerson.
            </svrl:text>
      </svrl:successful-report>
    </xsl:if>
    <xsl:variable name="countConceptRequestList" select="count(rim:Slot[@name = 'ConceptRequestList'])" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countConceptRequestList=0) or ($countConceptRequestList=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countConceptRequestList=0) or ($countConceptRequestList=1)">
          <xsl:attribute name="id">req_card_Query_ConceptRequestList</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The Query must contain ZERO or ONE ConceptRequestList elements (found: <xsl:text />
            <xsl:value-of select="$countConceptRequestList" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countDistributionRequestList" select="count(rim:Slot[@name = 'DistributionRequestList'])" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countDistributionRequestList=0) or ($countDistributionRequestList=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countDistributionRequestList=0) or ($countDistributionRequestList=1)">
          <xsl:attribute name="id">req_card_Query_DistributionRequestList</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The Query must contain ZERO or ONE DistributionRequestList elements (found: <xsl:text />
            <xsl:value-of select="$countDistributionRequestList" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countLegalPerson" select="count(rim:Slot[@name = 'LegalPerson'])" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countLegalPerson=0) or ($countLegalPerson=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countLegalPerson=0) or ($countLegalPerson=1)">
          <xsl:attribute name="id">req_card_Query_LegalPerson</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The Query must contain ZERO or ONE LegalPerson elements (found: <xsl:text />
            <xsl:value-of select="$countLegalPerson" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countNaturalPerson" select="count(rim:Slot[@name = 'NaturalPerson'])" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countNaturalPerson=0) or ($countNaturalPerson=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countNaturalPerson=0) or ($countNaturalPerson=1)">
          <xsl:attribute name="id">req_card_Query_NaturalPerson</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The Query must contain ZERO or ONE NaturalPerson elements (found: <xsl:text />
            <xsl:value-of select="$countNaturalPerson" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countAuthorizedRepresentative" select="count(rim:Slot[@name = 'AuthorizedRepresentative'])" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countAuthorizedRepresentative=0) or ($countAuthorizedRepresentative=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countAuthorizedRepresentative=0) or ($countAuthorizedRepresentative=1)">
          <xsl:attribute name="id">req_card_Query_AuthorizedRepresentative</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The Query must contain ZERO or ONE AuthorizedRepresentative elements (found: <xsl:text />
            <xsl:value-of select="$countAuthorizedRepresentative" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M24" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M24" priority="-1" />
  <xsl:template match="@*|node()" mode="M24" priority="-2">
    <xsl:apply-templates mode="M24" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="query:QueryRequest/query:Query/rim:Slot[@name = 'ConceptRequestList']" mode="M25" priority="1000">
    <svrl:fired-rule context="query:QueryRequest/query:Query/rim:Slot[@name = 'ConceptRequestList']" />
    <xsl:variable name="countElement" select="count(rim:SlotValue/rim:Element)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countElement > 0)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countElement > 0)">
          <xsl:attribute name="id">req_crlist_element</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The ConceptRequestList slot must contain at least ONE Element (found: <xsl:text />
            <xsl:value-of select="$countElement" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countElementConcept" select="count(rim:SlotValue/rim:Element/cccev:concept)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countElementConcept = 1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countElementConcept = 1)">
          <xsl:attribute name="id">req_crlist_element_concept</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                Each ConceptRequestList/Element must contain exactly ONE concept (found: <xsl:text />
            <xsl:value-of select="$countElementConcept" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M25" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M25" priority="-1" />
  <xsl:template match="@*|node()" mode="M25" priority="-2">
    <xsl:apply-templates mode="M25" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="query:QueryRequest/query:Query/rim:Slot[@name = 'DistributionRequestList']" mode="M26" priority="1000">
    <svrl:fired-rule context="query:QueryRequest/query:Query/rim:Slot[@name = 'DistributionRequestList']" />
    <xsl:variable name="countElement" select="count(rim:SlotValue/rim:Element)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countElement > 0)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countElement > 0)">
          <xsl:attribute name="id">req_distlist_element</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The DistributionRequestList slot must contain at least ONE Element (found: <xsl:text />
            <xsl:value-of select="$countElement" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countElementDistribution" select="count(rim:SlotValue/rim:Element/dcat:distribution)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countElementDistribution = 1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countElementDistribution = 1)">
          <xsl:attribute name="id">req_crlist_element_distribution</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                Each DistributionRequestList/Element must contain exactly ONE distribution (found: <xsl:text />
            <xsl:value-of select="$countElementDistribution" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M26" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M26" priority="-1" />
  <xsl:template match="@*|node()" mode="M26" priority="-2">
    <xsl:apply-templates mode="M26" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="query:QueryRequest/query:Query/rim:Slot[@name = 'LegalPerson']" mode="M27" priority="1000">
    <svrl:fired-rule context="query:QueryRequest/query:Query/rim:Slot[@name = 'LegalPerson']" />
    <xsl:variable name="countCoreBusiness" select="count(rim:SlotValue/cva:CoreBusiness)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countCoreBusiness=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countCoreBusiness=1)">
          <xsl:attribute name="id">req_legalperson_corebusiness</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The LegalPerson must contain exactly ONE CoreBusiness element (found: <xsl:text />
            <xsl:value-of select="$countCoreBusiness" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M27" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M27" priority="-1" />
  <xsl:template match="@*|node()" mode="M27" priority="-2">
    <xsl:apply-templates mode="M27" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="query:QueryRequest/query:Query/rim:Slot[@name = 'NaturalPerson'] | query:QueryRequest/query:Query/rim:Slot[@name = 'AuthorizedRepresentative']" mode="M28" priority="1000">
    <svrl:fired-rule context="query:QueryRequest/query:Query/rim:Slot[@name = 'NaturalPerson'] | query:QueryRequest/query:Query/rim:Slot[@name = 'AuthorizedRepresentative']" />
    <xsl:variable name="countCorePerson" select="count(rim:SlotValue/cva:CorePerson)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countCorePerson=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countCorePerson=1)">
          <xsl:attribute name="id">req_person_CorePerson</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The NaturalPerson or AuthorizedRepresentative must contain exactly ONE CorePerson element (found: <xsl:text />
            <xsl:value-of select="$countCorePerson" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M28" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M28" priority="-1" />
  <xsl:template match="@*|node()" mode="M28" priority="-2">
    <xsl:apply-templates mode="M28" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="query:QueryRequest/query:Query/rim:Slot[@name = 'NaturalPerson']/rim:SlotValue/cva:CorePerson                       | query:QueryRequest/query:Query/rim:Slot[@name = 'AuthorizedRepresentative']/rim:SlotValue/cva:CorePerson" mode="M29" priority="1000">
    <svrl:fired-rule context="query:QueryRequest/query:Query/rim:Slot[@name = 'NaturalPerson']/rim:SlotValue/cva:CorePerson                       | query:QueryRequest/query:Query/rim:Slot[@name = 'AuthorizedRepresentative']/rim:SlotValue/cva:CorePerson" />
    <xsl:variable name="countPersonId" select="count(cvb:PersonID)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countPersonId > 0)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countPersonId > 0)">
          <xsl:attribute name="id">req_card_CorePerson_PersonId</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The CorePerson must have at least ONE PersonId (found: <xsl:text />
            <xsl:value-of select="$countPersonId" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countPersonFamilyName" select="count(cvb:PersonFamilyName)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countPersonFamilyName=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countPersonFamilyName=1)">
          <xsl:attribute name="id">req_card_CorePerson_PersonFamilyName</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The CorePerson must have ONE PersonFamilyName (found: <xsl:text />
            <xsl:value-of select="$countPersonFamilyName" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countPersonGivenName" select="count(cvb:PersonGivenName)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countPersonGivenName=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countPersonGivenName=1)">
          <xsl:attribute name="id">req_card_CorePerson_PersonGivenName</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The CorePerson must have ONE PersonGivenName (found: <xsl:text />
            <xsl:value-of select="$countPersonGivenName" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countPersonGenderCode" select="count(cvb:PersonGenderCode)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countPersonGenderCode=0) or ($countPersonGenderCode=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countPersonGenderCode=0) or ($countPersonGenderCode=1)">
          <xsl:attribute name="id">req_card_Person_PersonGenderCode</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The CorePerson must have ZERO or ONE PersonGenderCode elements (found: <xsl:text />
            <xsl:value-of select="$countPersonGenderCode" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countPersonBirthName" select="count(cvb:PersonBirthName)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countPersonBirthName=0) or ($countPersonBirthName=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countPersonBirthName=0) or ($countPersonBirthName=1)">
          <xsl:attribute name="id">req_card_Person_PersonBirthName</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The CorePerson must have ZERO or ONE PersonBirthName elements (found: <xsl:text />
            <xsl:value-of select="$countPersonBirthName" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countPersonBirthDate" select="count(cvb:PersonBirthDate)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countPersonBirthDate=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countPersonBirthDate=1)">
          <xsl:attribute name="id">req_card_CorePerson_PersonBirthDate</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The CorePerson must have ONE PersonBirthDate (found: <xsl:text />
            <xsl:value-of select="$countPersonBirthDate" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countPersonPersonPlaceOfBirthCoreLocation" select="count(cvb:AddressPostName)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countPersonPersonPlaceOfBirthCoreLocation=0) or ($countPersonPersonPlaceOfBirthCoreLocation=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countPersonPersonPlaceOfBirthCoreLocation=0) or ($countPersonPersonPlaceOfBirthCoreLocation=1)">
          <xsl:attribute name="id">req_card_CorePerson_PlaceOfBirthCoreLocation</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The CorePerson must have ZERO or ONE  PlaceOfBirthCoreLocation/AddressPostName elements (found: <xsl:text />
            <xsl:value-of select="$countPersonPersonPlaceOfBirthCoreLocation" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countPersonCoreAddress" select="count(cva:PersonCoreAddress)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countPersonCoreAddress=0) or ($countPersonCoreAddress=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countPersonCoreAddress=0) or ($countPersonCoreAddress=1)">
          <xsl:attribute name="id">req_card_coreBusiness_PersonCoreAddress</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The CoreBusiness must contain ZERO or ONE PersonCoreAddress elements (found: <xsl:text />
            <xsl:value-of select="$countPersonCoreAddress" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M29" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M29" priority="-1" />
  <xsl:template match="@*|node()" mode="M29" priority="-2">
    <xsl:apply-templates mode="M29" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="query:QueryRequest/query:Query/rim:Slot[@name = 'ConceptRequestList']/cccev:Concept                       |query:QueryResponse/rim:RegistryObjectList/rim:RegistryObject/rim:Slot/rim:SlotValue/rim:Element/cccev:Concept" mode="M30" priority="1000">
    <svrl:fired-rule context="query:QueryRequest/query:Query/rim:Slot[@name = 'ConceptRequestList']/cccev:Concept                       |query:QueryResponse/rim:RegistryObjectList/rim:RegistryObject/rim:Slot/rim:SlotValue/rim:Element/cccev:Concept" />
    <xsl:variable name="countConceptId" select="count(cbc:id)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countConceptId=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countConceptId=1)">
          <xsl:attribute name="id">req_card_concept_id</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                Each root concept must have ONE id (found: <xsl:text />
            <xsl:value-of select="$countConceptId" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countConceptQName" select="count(cbc:QName)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countConceptQName=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countConceptQName=1)">
          <xsl:attribute name="id">req_card_concept_qname</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                Each root concept must have ONE QName (found: <xsl:text />
            <xsl:value-of select="$countConceptQName" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countconcepts" select="count(cccev:concept)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countconcepts > 0)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countconcepts > 0)">
          <xsl:attribute name="id">req_card_Concepts_concepts</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The root concept must contain at least ONE concepts element (found: <xsl:text />
            <xsl:value-of select="$countconcepts" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M30" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M30" priority="-1" />
  <xsl:template match="@*|node()" mode="M30" priority="-2">
    <xsl:apply-templates mode="M30" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="cccev:concept" mode="M31" priority="1000">
    <svrl:fired-rule context="cccev:concept" />
    <xsl:variable name="countConceptId" select="count(cbc:id)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countConceptId=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countConceptId=1)">
          <xsl:attribute name="id">req_card_nested_concept_id</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                Each concept must have ONE ConceptId (found: <xsl:text />
            <xsl:value-of select="$countConceptId" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countConceptQName" select="count(cbc:QName)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countConceptQName=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countConceptQName=1)">
          <xsl:attribute name="id">req_card_nested_concept_qname</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                Each concept must have ONE QName (found: <xsl:text />
            <xsl:value-of select="$countConceptQName" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M31" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M31" priority="-1" />
  <xsl:template match="@*|node()" mode="M31" priority="-2">
    <xsl:apply-templates mode="M31" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="query:QueryRequest/query:Query/rim:Slot[@name = 'DistributionRequestList']/rim:SlotValue/rim:Element/dcat:distribution" mode="M32" priority="1000">
    <svrl:fired-rule context="query:QueryRequest/query:Query/rim:Slot[@name = 'DistributionRequestList']/rim:SlotValue/rim:Element/dcat:distribution" />
    <xsl:variable name="countDistAccessURL" select="count(dcat:accessURL)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countDistAccessURL=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countDistAccessURL=1)">
          <xsl:attribute name="id">req_card_dist_AccessURL</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                Each Distribution must have ONE accessURL (found: <xsl:text />
            <xsl:value-of select="$countDistAccessURL" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countDistMediaType" select="count(dcat:mediaType)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countDistMediaType=0) or ($countDistMediaType=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countDistMediaType=0) or ($countDistMediaType=1)">
          <xsl:attribute name="id">req_card_dist_MediaType</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                Each Distribution must have ZERO or ONE mediaType elements (found: <xsl:text />
            <xsl:value-of select="$countDistMediaType" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countDistFormat" select="count(dct:format)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countDistFormat=0) or ($countDistFormat=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countDistFormat=0) or ($countDistFormat=1)">
          <xsl:attribute name="id">req_card_dist_Format</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                Each Distribution must have ZERO or ONE format elements (found: <xsl:text />
            <xsl:value-of select="$countDistFormat" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M32" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M32" priority="-1" />
  <xsl:template match="@*|node()" mode="M32" priority="-2">
    <xsl:apply-templates mode="M32" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="query:QueryRequest/query:Query/rim:Slot[@name = 'LegalPerson']/rim:SlotValue/cva:CoreBusiness" mode="M33" priority="1000">
    <svrl:fired-rule context="query:QueryRequest/query:Query/rim:Slot[@name = 'LegalPerson']/rim:SlotValue/cva:CoreBusiness" />
    <xsl:variable name="countLegalEntityLegalID" select="count(cvb:LegalEntityLegalID)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countLegalEntityLegalID=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countLegalEntityLegalID=1)">
          <xsl:attribute name="id">req_card_CoreBusiness_LegalEntityLegalID</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The CoreBusiness must have ONE LegalEntityLegalID (found: <xsl:text />
            <xsl:value-of select="$countLegalEntityLegalID" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countLegalEntityLegalName" select="count(cvb:LegalEntityLegalName)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countLegalEntityLegalName=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countLegalEntityLegalName=1)">
          <xsl:attribute name="id">req_card_CoreBusiness_LegalEntityLegalName</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The CorePerson must have ONE LegalEntityLegalName (found: <xsl:text />
            <xsl:value-of select="$countLegalEntityLegalName" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countLegalEntityCoreAddress" select="count(cva:LegalEntityCoreAddress)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countLegalEntityCoreAddress=0) or ($countLegalEntityCoreAddress=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countLegalEntityCoreAddress=0) or ($countLegalEntityCoreAddress=1)">
          <xsl:attribute name="id">req_card_coreBusiness_LegalEntityCoreAddress</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The CoreBusiness must contain ZERO or ONE LegalEntityCoreAddress elements (found: <xsl:text />
            <xsl:value-of select="$countLegalEntityCoreAddress" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M33" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M33" priority="-1" />
  <xsl:template match="@*|node()" mode="M33" priority="-2">
    <xsl:apply-templates mode="M33" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="cva:PersonCoreAddress | cva:LegalEntityCoreAddress" mode="M34" priority="1000">
    <svrl:fired-rule context="cva:PersonCoreAddress | cva:LegalEntityCoreAddress" />
    <xsl:variable name="countAddressFullAddress" select="count(cvb:AddressFullAddress)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countAddressFullAddress &lt; 4)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countAddressFullAddress &lt; 4)">
          <xsl:attribute name="id">req_card_Address_FullAddress</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The Address must contain UP TO THREE AddressFullAddress elements (found: <xsl:text />
            <xsl:value-of select="$countAddressFullAddress" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countAddressThoroughfare" select="count(cvb:AddressThoroughfare)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countAddressThoroughfare=0) or ($countAddressThoroughfare=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countAddressThoroughfare=0) or ($countAddressThoroughfare=1)">
          <xsl:attribute name="id">req_card_Address_AddressThoroughfare</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The Address must contain ZERO or ONE AddressThoroughfare elements (found: <xsl:text />
            <xsl:value-of select="$countAddressThoroughfare" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countAddressLocatorDesignator" select="count(cvb:AddressLocatorDesignator)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countAddressLocatorDesignator=0) or ($countAddressLocatorDesignator=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countAddressLocatorDesignator=0) or ($countAddressLocatorDesignator=1)">
          <xsl:attribute name="id">req_card_Address_AddressLocatorDesignator</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The Address must contain ZERO or ONE AddressLocatorDesignator elements (found: <xsl:text />
            <xsl:value-of select="$countAddressLocatorDesignator" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countAddressPostName" select="count(cvb:AddressPostName)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countAddressPostName=0) or ($countAddressPostName=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countAddressPostName=0) or ($countAddressPostName=1)">
          <xsl:attribute name="id">req_card_Address_AddressPostName</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The Address must contain ZERO or ONE AddressPostName elements (found: <xsl:text />
            <xsl:value-of select="$countAddressPostName" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countAddressAdminUnitLocationOne" select="count(cvb:AddressAdminUnitLocationOne)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countAddressAdminUnitLocationOne=0) or ($countAddressAdminUnitLocationOne=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countAddressAdminUnitLocationOne=0) or ($countAddressAdminUnitLocationOne=1)">
          <xsl:attribute name="id">req_card_Address_AddressAdminUnitLocationOne</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The Address must contain ZERO or ONE AddressAdminUnitLocationOne elements (found: <xsl:text />
            <xsl:value-of select="$countAddressAdminUnitLocationOne" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countAddressPostCode" select="count(cvb:AddressPostCode)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countAddressPostCode=0) or ($countAddressPostCode=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countAddressPostCode=0) or ($countAddressPostCode=1)">
          <xsl:attribute name="id">req_card_Address_AddressPostCode</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The Address must contain ZERO or ONE AddressPostCode elements (found: <xsl:text />
            <xsl:value-of select="$countAddressPostCode" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M34" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M34" priority="-1" />
  <xsl:template match="@*|node()" mode="M34" priority="-2">
    <xsl:apply-templates mode="M34" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="query:QueryResponse/rim:Slot[@name = 'DataProvider']" mode="M35" priority="1000">
    <svrl:fired-rule context="query:QueryResponse/rim:Slot[@name = 'DataProvider']" />
    <xsl:variable name="countAgent" select="count(./rim:SlotValue/cagv:Agent)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countAgent=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countAgent=1)">
          <xsl:attribute name="id">res_dp_card_Agent</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The DataProvider slot must contain exactly ONE Agent (found: <xsl:text />
            <xsl:value-of select="$countAgent" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M35" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M35" priority="-1" />
  <xsl:template match="@*|node()" mode="M35" priority="-2">
    <xsl:apply-templates mode="M35" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="query:QueryResponse/rim:Slot[@name = 'DataProvider']/rim:SlotValue/cagv:Agent | query:QueryResponse/rim:Slot[@name = 'ErrorProvider']/rim:SlotValue/cagv:Agent" mode="M36" priority="1000">
    <svrl:fired-rule context="query:QueryResponse/rim:Slot[@name = 'DataProvider']/rim:SlotValue/cagv:Agent | query:QueryResponse/rim:Slot[@name = 'ErrorProvider']/rim:SlotValue/cagv:Agent" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="exists(cbc:id)" />
      <xsl:otherwise>
        <svrl:failed-assert test="exists(cbc:id)">
          <xsl:attribute name="id">res_agent_mandatory_id</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The Agent must have an Id.
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="exists(cbc:name)" />
      <xsl:otherwise>
        <svrl:failed-assert test="exists(cbc:name)">
          <xsl:attribute name="id">res_agent_mandatory_name</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The Agent must have a name.
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M36" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M36" priority="-1" />
  <xsl:template match="@*|node()" mode="M36" priority="-2">
    <xsl:apply-templates mode="M36" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="query:QueryResponse/rim:RegistryObjectList/rim:RegistryObject" mode="M37" priority="1000">
    <svrl:fired-rule context="query:QueryResponse/rim:RegistryObjectList/rim:RegistryObject" />
    <xsl:variable name="countConceptValues" select="count(rim:Slot[@name = 'ConceptValues'])" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countConceptValues=0) or ($countConceptValues=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countConceptValues=0) or ($countConceptValues=1)">
          <xsl:attribute name="id">res_card_ConceptValues</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The RegistryObjectList must contain ZERO or ONE ConceptValues elements (found: <xsl:text />
            <xsl:value-of select="$countConceptValues" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countDocumentMetadata" select="count(rim:Slot[@name = 'DocumentMetadata'])" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countDocumentMetadata=0) or ($countDocumentMetadata=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countDocumentMetadata=0) or ($countDocumentMetadata=1)">
          <xsl:attribute name="id">res_card_DocumentMetadata</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The RegistryObjectList must contain ZERO or ONE DocumentMetadata elements (found: <xsl:text />
            <xsl:value-of select="$countDocumentMetadata" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countRepositoryItemRef" select="count(rim:RepositoryItemRef)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countRepositoryItemRef=0) or ($countRepositoryItemRef=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countRepositoryItemRef=0) or ($countRepositoryItemRef=1)">
          <xsl:attribute name="id">res_card_RepositoryItemRef</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The RegistryObjectList must contain ZERO or ONE RepositoryItemRef elements (found: <xsl:text />
            <xsl:value-of select="$countRepositoryItemRef" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M37" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M37" priority="-1" />
  <xsl:template match="@*|node()" mode="M37" priority="-2">
    <xsl:apply-templates mode="M37" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="query:QueryResponse/rim:RegistryObjectList/rim:RegistryObject/rim:Slot[@name = 'ConceptValues']" mode="M38" priority="1000">
    <svrl:fired-rule context="query:QueryResponse/rim:RegistryObjectList/rim:RegistryObject/rim:Slot[@name = 'ConceptValues']" />
    <xsl:variable name="countElement" select="count(rim:SlotValue/rim:Element)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countElement > 0)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countElement > 0)">
          <xsl:attribute name="id">res_crvalues_element</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The ConceptValues slot must contain at least ONE Element (found: <xsl:text />
            <xsl:value-of select="$countElement" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countElementConcept" select="count(rim:SlotValue/rim:Element/cccev:concept)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countElementConcept = 1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countElementConcept = 1)">
          <xsl:attribute name="id">res_crvalues_element_concept</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                Each ConceptValues/Element must contain exactly ONE concept (found: <xsl:text />
            <xsl:value-of select="$countElementConcept" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M38" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M38" priority="-1" />
  <xsl:template match="@*|node()" mode="M38" priority="-2">
    <xsl:apply-templates mode="M38" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="query:QueryResponse/rim:RegistryObjectList/rim:RegistryObject/rim:Slot[@name = 'ConceptValues']/rim:SlotValue/rim:Element/cccev:concept//cccev:concept" mode="M39" priority="1000">
    <svrl:fired-rule context="query:QueryResponse/rim:RegistryObjectList/rim:RegistryObject/rim:Slot[@name = 'ConceptValues']/rim:SlotValue/rim:Element/cccev:concept//cccev:concept" />
    <xsl:variable name="countConceptValues" select="count(cccev:value)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countConceptValues = 1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countConceptValues = 1)">
          <xsl:attribute name="id">cardinality_concept_value</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                Each concept (except the main concept) must contain exactly ONE value (found: <xsl:text />
            <xsl:value-of select="$countConceptValues" />
            <xsl:text /> for id:<xsl:text />
            <xsl:value-of select="cbc:id" />
            <xsl:text /> and QName:<xsl:text />
            <xsl:value-of select="cbc:QName" />
            <xsl:text />. ).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M39" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M39" priority="-1" />
  <xsl:template match="@*|node()" mode="M39" priority="-2">
    <xsl:apply-templates mode="M39" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="query:QueryResponse/rim:RegistryObjectList/rim:RegistryObject/rim:Slot/rim:SlotValue/dcat:Dataset" mode="M40" priority="1000">
    <svrl:fired-rule context="query:QueryResponse/rim:RegistryObjectList/rim:RegistryObject/rim:Slot/rim:SlotValue/dcat:Dataset" />
    <xsl:variable name="countTemporal" select="count(dct:temporal)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countTemporal=0) or ($countTemporal=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countTemporal=0) or ($countTemporal=1)">
          <xsl:attribute name="id">res_card_dataset_Temporal</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The Dataset must contain ZERO or ONE Temporal elements (found: <xsl:text />
            <xsl:value-of select="$countTemporal" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countcreator" select="count(dct:creator)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countcreator=0) or ($countcreator=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countcreator=0) or ($countcreator=1)">
          <xsl:attribute name="id">res_card_dataset_creator</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The Dataset must contain ZERO or ONE creator elements (found: <xsl:text />
            <xsl:value-of select="$countcreator" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countdistribution" select="count(dcat:distribution)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countdistribution=0) or ($countdistribution=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countdistribution=0) or ($countdistribution=1)">
          <xsl:attribute name="id">res_card_dataset_distribution</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The Dataset must contain ZERO or ONE distribution elements (found: <xsl:text />
            <xsl:value-of select="$countdistribution" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="counttemporalstartdate" select="count(dct:temporal/dct:startDate)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($counttemporalstartdate=0) or ($counttemporalstartdate=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($counttemporalstartdate=0) or ($counttemporalstartdate=1)">
          <xsl:attribute name="id">res_card_dataset_temporal_startdate</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The Dataset must contain ZERO or ONE temporal/startDate elements (found: <xsl:text />
            <xsl:value-of select="$counttemporalstartdate" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="counttemporalendDate" select="count(dct:temporal/dct:endDate)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($counttemporalendDate=0) or ($counttemporalendDate=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($counttemporalendDate=0) or ($counttemporalendDate=1)">
          <xsl:attribute name="id">res_card_dataset_temporal_endDate</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The Dataset must contain ZERO or ONE temporal/endDate elements (found: <xsl:text />
            <xsl:value-of select="$counttemporalendDate" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M40" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M40" priority="-1" />
  <xsl:template match="@*|node()" mode="M40" priority="-2">
    <xsl:apply-templates mode="M40" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="query:QueryResponse/rim:RegistryObjectList/rim:RegistryObject/rim:Slot/rim:SlotValue/dcat:Dataset/dcat:distribution" mode="M41" priority="1000">
    <svrl:fired-rule context="query:QueryResponse/rim:RegistryObjectList/rim:RegistryObject/rim:Slot/rim:SlotValue/dcat:Dataset/dcat:distribution" />
    <xsl:variable name="countaccessURL" select="count(dcat:accessURL)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countaccessURL > 0)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countaccessURL > 0)">
          <xsl:attribute name="id">res_card_distribution_accessURL</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The distribution Element must contain at least ONE accessURL element (found: <xsl:text />
            <xsl:value-of select="$countaccessURL" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countdocumentURI" select="count(cccev:documentURI)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countdocumentURI = 1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countdocumentURI = 1)">
          <xsl:attribute name="id">res_card_distribution_documentURI</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The distribution Element must contain exactly ONE documentURI element (found: <xsl:text />
            <xsl:value-of select="$countdocumentURI" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countdocumentType" select="count(cccev:documentType)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countdocumentType = 0) or ($countdocumentType = 1) " />
      <xsl:otherwise>
        <svrl:failed-assert test="($countdocumentType = 0) or ($countdocumentType = 1)">
          <xsl:attribute name="id">res_card_distribution_documentType</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The distribution Element must contain ZERO or ONE documentType elements (found: <xsl:text />
            <xsl:value-of select="$countdocumentType" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countlocaleCode" select="count(cccev:localeCode)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countlocaleCode = 0) or ($countlocaleCode = 1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countlocaleCode = 0) or ($countlocaleCode = 1)">
          <xsl:attribute name="id">res_card_distribution_localeCode</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The distribution Element must contain ZERO or ONE localeCode elements (found: <xsl:text />
            <xsl:value-of select="$countlocaleCode" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M41" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M41" priority="-1" />
  <xsl:template match="@*|node()" mode="M41" priority="-2">
    <xsl:apply-templates mode="M41" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="query:QueryResponse/rim:RegistryObjectList/rim:RegistryObject/rim:Slot/rim:SlotValue/dcat:Dataset/dcat:qualifiedRelation/dct:relation" mode="M42" priority="1000">
    <svrl:fired-rule context="query:QueryResponse/rim:RegistryObjectList/rim:RegistryObject/rim:Slot/rim:SlotValue/dcat:Dataset/dcat:qualifiedRelation/dct:relation" />
    <xsl:variable name="counttitle" select="count(dct:title)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($counttitle > 0)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($counttitle > 0)">
          <xsl:attribute name="id">res_card_distribution_title</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The relation Element must contain at least ONE title element (found: <xsl:text />
            <xsl:value-of select="$counttitle" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countdescription" select="count(dct:description)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countdescription > 0)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countdescription > 0)">
          <xsl:attribute name="id">res_card_distribution_description</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The relation Element must contain at least ONE description element (found: <xsl:text />
            <xsl:value-of select="$countdescription" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M42" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M42" priority="-1" />
  <xsl:template match="@*|node()" mode="M42" priority="-2">
    <xsl:apply-templates mode="M42" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="cccev:value" mode="M43" priority="1000">
    <svrl:fired-rule context="cccev:value" />
    <xsl:variable name="countamountValue" select="count(cccev:amountValue)" />
    <xsl:variable name="countcodeValue" select="count(cccev:codeValue)" />
    <xsl:variable name="countdateValue" select="count(cccev:dateValue)" />
    <xsl:variable name="countidentifierValue" select="count(cccev:identifierValue)" />
    <xsl:variable name="countindicatorValue" select="count(cccev:indicatorValue)" />
    <xsl:variable name="countmeasureValue" select="count(cccev:measureValue)" />
    <xsl:variable name="countnumericValue" select="count(cccev:numericValue)" />
    <xsl:variable name="countquantityValue" select="count(cccev:quantityValue)" />
    <xsl:variable name="counttextValue" select="count(cccev:textValue)" />
    <xsl:variable name="counttimeValue" select="count(cccev:timeValue)" />
    <xsl:variable name="counturiValue" select="count(cccev:uriValue)" />
    <xsl:variable name="countperiodValue" select="count(cccev:periodValue)" />
    <xsl:variable name="counterror" select="count(cccev:error)" />
    <xsl:variable name="sum" select="                      $countamountValue                 +$countcodeValue                 +$countdateValue                 +$countidentifierValue                 +$countindicatorValue                 +$countmeasureValue                 +$countnumericValue                 +$countquantityValue                 +$counttimeValue                 +$counturiValue                 +$countperiodValue                 +$counterror" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( ($sum=1 and $counttextValue  = 0) or ($counttextValue >0 and $sum=0) )" />
      <xsl:otherwise>
        <svrl:failed-assert test="( ($sum=1 and $counttextValue = 0) or ($counttextValue >0 and $sum=0) )">
          <xsl:attribute name="id">res_one_valid_value</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                Invalid value in concept (id:<xsl:text />
            <xsl:value-of select="../cbc:id" />
            <xsl:text /> and QName:<xsl:text />
            <xsl:value-of select="../cbc:QName" />
            <xsl:text />). 
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M43" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M43" priority="-1" />
  <xsl:template match="@*|node()" mode="M43" priority="-2">
    <xsl:apply-templates mode="M43" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="query:QueryResponse/rim:RegistryObjectList/rim:RegistryObject/rim:Slot/rim:SlotValue/dcat:Dataset/dct:creator" mode="M44" priority="1000">
    <svrl:fired-rule context="query:QueryResponse/rim:RegistryObjectList/rim:RegistryObject/rim:Slot/rim:SlotValue/dcat:Dataset/dct:creator" />
    <xsl:variable name="countid" select="count(cbc:id)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countid=0) or ($countid=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countid=0) or ($countid=1)">
          <xsl:attribute name="id">res_card_creator_id</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The Creator must contain ZERO or ONE id elements (found: <xsl:text />
            <xsl:value-of select="$countid" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countname" select="count(cbc:name)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countname=0) or ($countname=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countname=0) or ($countname=1)">
          <xsl:attribute name="id">res_card_creator_name</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The Creator must contain ZERO or ONE id elements (found: <xsl:text />
            <xsl:value-of select="$countname" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="countpostName" select="count(cagv:location/locn:address/locn:postName)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($countpostName=0) or ($countpostName=1)" />
      <xsl:otherwise>
        <svrl:failed-assert test="($countpostName=0) or ($countpostName=1)">
          <xsl:attribute name="id">res_card_creator_postname</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The Creator must contain ZERO or ONE location/address/postName elements (found: <xsl:text />
            <xsl:value-of select="$countpostName" />
            <xsl:text />).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M44" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M44" priority="-1" />
  <xsl:template match="@*|node()" mode="M44" priority="-2">
    <xsl:apply-templates mode="M44" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="             rim:Slot[@name = 'SpecificationIdentifier']/rim:SlotValue             | rim:Slot[@name = 'ConsentToken']/rim:SlotValue              | rim:Slot[@name = 'DatasetIdentifier']/rim:SlotValue             | rim:ObjectRefList/rim:ObjectRef/rim:Slot[@name = 'shortDescription']/rim:SlotValue             " mode="M45" priority="1005">
    <svrl:fired-rule context="             rim:Slot[@name = 'SpecificationIdentifier']/rim:SlotValue             | rim:Slot[@name = 'ConsentToken']/rim:SlotValue              | rim:Slot[@name = 'DatasetIdentifier']/rim:SlotValue             | rim:ObjectRefList/rim:ObjectRef/rim:Slot[@name = 'shortDescription']/rim:SlotValue             " />
    <xsl:variable name="datatype" select="@*[ends-with(name(.), ':type') and . != '']" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="matches($datatype,':StringValueType$')" />
      <xsl:otherwise>
        <svrl:failed-assert test="matches($datatype,':StringValueType$')">
          <xsl:attribute name="id">expecting_StringValueType</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                Expecting StringValueType for slot: <xsl:text />
            <xsl:value-of select="../@name" />
            <xsl:text /> (found:  <xsl:text />
            <xsl:value-of select="$datatype" />
            <xsl:text />)
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M45" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="             rim:Slot[@name = 'Procedure']/rim:SlotValue             | rim:Slot[@name = 'ErrorText']/rim:SlotValue/rim:Element              " mode="M45" priority="1004">
    <svrl:fired-rule context="             rim:Slot[@name = 'Procedure']/rim:SlotValue             | rim:Slot[@name = 'ErrorText']/rim:SlotValue/rim:Element              " />
    <xsl:variable name="datatype" select="@*[ends-with(name(.), ':type') and . != '']" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="matches($datatype,':InternationalStringValueType$')" />
      <xsl:otherwise>
        <svrl:failed-assert test="matches($datatype,':InternationalStringValueType$')">
          <xsl:attribute name="id">expecting_InternationalStringValueType</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                Expecting InternationalStringValueType for slot: <xsl:text />
            <xsl:value-of select="../@name" />
            <xsl:text />
            <xsl:text />
            <xsl:value-of select="../../@name" />
            <xsl:text /> (found:  <xsl:text />
            <xsl:value-of select="$datatype" />
            <xsl:text />)
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M45" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="             rim:Slot[@name = 'IssueDateTime']/rim:SlotValue             | rim:Slot[@name = 'Timestamp']/rim:SlotValue             " mode="M45" priority="1003">
    <svrl:fired-rule context="             rim:Slot[@name = 'IssueDateTime']/rim:SlotValue             | rim:Slot[@name = 'Timestamp']/rim:SlotValue             " />
    <xsl:variable name="datatype" select="@*[ends-with(name(.), ':type') and . != '']" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="matches($datatype,':DateTimeValueType$')" />
      <xsl:otherwise>
        <svrl:failed-assert test="matches($datatype,':DateTimeValueType$')">
          <xsl:attribute name="id">expecting_DateTimeValueType</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                Expecting DateTimeValueType for slot: <xsl:text />
            <xsl:value-of select="../@name" />
            <xsl:text /> (found:  <xsl:text />
            <xsl:value-of select="$datatype" />
            <xsl:text />)
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M45" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="             rim:Slot[@name = 'DataConsumer']/rim:SlotValue             | rim:Slot[@name = 'LegalPerson']/rim:SlotValue             | rim:Slot[@name = 'NaturalPerson']/rim:SlotValue             | rim:Slot[@name = 'AuthorizedRepresentative']/rim:SlotValue             | rim:Slot[@name = 'ConceptRequestList']/rim:SlotValue/rim:Element              | rim:Slot[@name = 'DistributionRequestList']/rim:SlotValue/rim:Element              | rim:Slot[@name = 'DataProvider']/rim:SlotValue             | rim:Slot[@name = 'ConceptValues']/rim:SlotValue/rim:Element              " mode="M45" priority="1002">
    <svrl:fired-rule context="             rim:Slot[@name = 'DataConsumer']/rim:SlotValue             | rim:Slot[@name = 'LegalPerson']/rim:SlotValue             | rim:Slot[@name = 'NaturalPerson']/rim:SlotValue             | rim:Slot[@name = 'AuthorizedRepresentative']/rim:SlotValue             | rim:Slot[@name = 'ConceptRequestList']/rim:SlotValue/rim:Element              | rim:Slot[@name = 'DistributionRequestList']/rim:SlotValue/rim:Element              | rim:Slot[@name = 'DataProvider']/rim:SlotValue             | rim:Slot[@name = 'ConceptValues']/rim:SlotValue/rim:Element              " />
    <xsl:variable name="datatype" select="@*[ends-with(name(.), ':type') and . != '']" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="matches($datatype,':AnyValueType$')" />
      <xsl:otherwise>
        <svrl:failed-assert test="matches($datatype,':AnyValueType$')">
          <xsl:attribute name="id">expecting_AnyValueType</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                Expecting AnyValueType for slot: <xsl:text />
            <xsl:value-of select="../@name" />
            <xsl:text />
            <xsl:text />
            <xsl:value-of select="../../@name" />
            <xsl:text /> (found:  <xsl:text />
            <xsl:value-of select="$datatype" />
            <xsl:text />)
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M45" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="             rim:Slot[@name = 'ConceptRequestList']/rim:SlotValue             | rim:Slot[@name = 'DistributionRequestList']/rim:SlotValue             | rim:Slot[@name = 'ErrorText']/rim:SlotValue             | rim:Slot[@name = 'FullfillingRequirement']/rim:SlotValue             " mode="M45" priority="1001">
    <svrl:fired-rule context="             rim:Slot[@name = 'ConceptRequestList']/rim:SlotValue             | rim:Slot[@name = 'DistributionRequestList']/rim:SlotValue             | rim:Slot[@name = 'ErrorText']/rim:SlotValue             | rim:Slot[@name = 'FullfillingRequirement']/rim:SlotValue             " />
    <xsl:variable name="datatype" select="@*[ends-with(name(.), ':type') and . != '']" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="matches($datatype,':CollectionValueType$')" />
      <xsl:otherwise>
        <svrl:failed-assert test="matches($datatype,':CollectionValueType$')">
          <xsl:attribute name="id">expecting_CollectionValueType</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                Expecting CollectionValueType for slot: <xsl:text />
            <xsl:value-of select="../@name" />
            <xsl:text />
            <xsl:text />
            <xsl:value-of select="../../@name" />
            <xsl:text /> (found:  <xsl:text />
            <xsl:value-of select="$datatype" />
            <xsl:text />)
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M45" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="             rim:Slot[@name = 'ConceptRequestList']/rim:SlotValue/rim:Element              | rim:Slot[@name = 'DistributionRequestList']/rim:SlotValue/rim:Element              | rim:Slot[@name = 'ErrorText']/rim:SlotValue             " mode="M45" priority="1000">
    <svrl:fired-rule context="             rim:Slot[@name = 'ConceptRequestList']/rim:SlotValue/rim:Element              | rim:Slot[@name = 'DistributionRequestList']/rim:SlotValue/rim:Element              | rim:Slot[@name = 'ErrorText']/rim:SlotValue             " />
    <xsl:variable name="datatype" select="@*[ends-with(name(.), ':type') and . != '']" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="matches($datatype,':VocabularyTermValueType$')" />
      <xsl:otherwise>
        <svrl:failed-assert test="matches($datatype,':VocabularyTermValueType$')">
          <xsl:attribute name="id">expecting_VocabularyTermValueType</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                Expecting VocabularyTermValueType for slot: <xsl:text />
            <xsl:value-of select="../@name" />
            <xsl:text />
            <xsl:text />
            <xsl:value-of select="../../@name" />
            <xsl:text /> (found:  <xsl:text />
            <xsl:value-of select="$datatype" />
            <xsl:text />)
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M45" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M45" priority="-1" />
  <xsl:template match="@*|node()" mode="M45" priority="-2">
    <xsl:apply-templates mode="M45" select="*|comment()|processing-instruction()" />
  </xsl:template>
</xsl:stylesheet>
