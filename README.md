# TOOP Connector NG

This is the successor project of the old [toop-interface](https://github.com/TOOP4EU/toop-interface) and [toop-connector](https://github.com/TOOP4EU/toop-connector) projects.

## New design considerations

Compared to the old design, certain architectural decisions changed which lead to a new architecture of the TOOP Connector.
Previously the application was designed to be a separate web application that was deployed between the DC/DP and the AS4 Gateway.
The TOOP Connector NG is a pure library solution that helps you do common stuff on the DC and the DP side.

Major changes compared to the old TOOP Connector:
* The semantic mapping service invocation was removed. The old solution was not satisfying.
* The "multiple DPs" option was removed. This responsibility was moved to the DC.
* The TOOP Directory was replaced by the DSD (Data Service Directory)

What is now contained in the TOOP Connector NG:
* Support for creating ASIC containers
* Support for creating XHE envelopes
* Support for performing dynamic discovery lookups
* Support for querying the DSD
* Support for communicating with the AS4 gateway

Note: the toop-commons project is not affected by these refactorings
