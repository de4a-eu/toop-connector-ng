# cpsv-ap_xml_schema_v0.01.helper

Added:
```xml
<xsd:element name="Agent" type="AgentType" />
```

# dcat-ap.xsd

### first

```xml
  <xsd:import namespace="http://purl.org/dc/terms/" schemaLocation="skos.xsd"/>
```

with

```xml
  <xsd:import namespace="http://www.w3.org/2004/02/skos/core#" schemaLocation="skos.xsd"/>
```

### second

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
