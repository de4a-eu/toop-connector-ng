# TOOP Connector NG

This is the successor project of the old [toop-interface](https://github.com/TOOP4EU/toop-interface) and [toop-connector](https://github.com/TOOP4EU/toop-connector) projects.

The TOOP Connector NG is a set of shared utility functions that you CAN include in your DC (Data Consumer) and/or DP (Data Provider) to perform common tasks that are required for a safe and interoperable data exchange. The TOOP Connector NG can be used both as a Java library AND via an REST API.  
The TOOP Connector NG is a Java only solution. Other environments like .NET etc. are currently not supported.

It uses the shared components from:
* https://github.com/TOOP4EU/toop-commons-ng

## Status

Work in progress
* For dependency management reasons, the TOOP shared libraries are now available in https://github.com/TOOP4EU/toop-commons-ng

## Maven coordinates

Only the TOOP Commons NG have some yet.

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
