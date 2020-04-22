This file contains all the changes necessary in the XSDs to make them work in the TOOP context.

## cpsv-ap_xml_schema_v0.01.helper

Added

```xml
<xsd:element name="Agent" type="AgentType" />
```

## dcat-ap.xsd

### first

replaced

```xml
  <xsd:complexType name="RoleType">
    <xsd:complexContent>
      <xsd:extension base="cbc:CodeType">
        <xsd:sequence/>
      </xsd:extension>
    </xsd:complexContent>
  </xsd:complexType>
```

with

```xml
  <xsd:complexType name="RoleType">
    <xsd:simpleContent>
      <xsd:extension base="cbc:CodeType" />
    </xsd:simpleContent>
  </xsd:complexType>
```
