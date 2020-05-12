# TOOP Connector NG

This is the successor project of the old [toop-commons](https://github.com/TOOP4EU/toop-commons), [toop-interface](https://github.com/TOOP4EU/toop-interface) and [toop-connector](https://github.com/TOOP4EU/toop-connector) projects.

The TOOP Connector NG is a set of shared utility functions that you include in your DC (Data Consumer) and/or DP (Data Provider) to perform common tasks that are required for a safe and interoperable data exchange.
The TOOP Connector NG is a Java only solution. Other environments like .NET etc. are currently not supported.

## Status

2020-05-12: release of `v2.0.0-beta2`
* Changed the main EDM classes for request, response and error response to `EDMRequest`, `EDMResponse` and `EDMErrorResponse`
* Added `getReader()` and `getWriter()` methods to easily read and write these objects from and to different structures

2020-05-06: release of `v2.0.0-beta1`
* Libraries for creating the new data model
* Consisting of `toop-edm`, `toop-regrep`, `toop-kafka-client` and `toop-commons`
* Allows to create Requests, Responses and Errors according to the new EDM (Electronic Data Model)

## Maven coordinates

```xml
      <dependency>
        <groupId>eu.toop</groupId>
        <artifactId>toop-edm</artifactId>
        <version>2.0.0-beta2</version>
      </dependency>
```

The rest comes via transitive dependencies.

## Design considerations

Compared to the old design, certain architectural decisions changed which lead to a new architecture of the TOOP Connector.
Previously the application was designed to be a separate web application that was deployed between the DC/DP and the AS4 Gateway.
The TOOP Connector NG is a both a library and a standalone solution that helps you do common stuff on the DC and the DP side.

Major changes compared to the old TOOP Connector:
* The semantic mapping service invocation was removed. The old solution was not satisfying.
* The "multiple DPs" option was removed. This responsibility was moved to the DC.
* The TOOP Directory was replaced by the DSD (Data Service Directory)
* No more usage of ASIC
* The content previously contained in "toop-commons" is now included in this project. The "toop-interface" project is no longer needed."

What is now contained in the TOOP Connector NG:
* Support for performing dynamic discovery lookups (as before)
* Support for Schematron validation of the EDM Requests and Responses (as before)
* Support for querying the DSD (new - was previously the TOOP Directory)
* Support for communicating with the AS4 gateway (as before)

## Building

Requires at least

* Java 1.8 or later
* Apache Maven for building

Do an initial `mvn clean install` on the command line.

Afterwards don't forget to add the following paths to your build path (in your IDE):

* toop-regrep/target/generated-sources/xjc
* toop-edm/target/generated-sources/xjc


Note: the `toop-codelist-tools` is for internal usage only.
