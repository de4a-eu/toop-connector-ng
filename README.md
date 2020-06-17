# TOOP Connector NG

This is the successor project of the old [toop-interface](https://github.com/TOOP4EU/toop-interface) and [toop-connector](https://github.com/TOOP4EU/toop-connector) projects.

The TOOP Connector NG is a set of shared utility functions that you CAN include in your DC (Data Consumer) and/or DP (Data Provider) to perform common tasks that are required for a safe and interoperable data exchange. The TOOP Connector NG can be used both as a Java library AND via an REST API.  
The TOOP Connector NG is a Java only solution. Other environments like .NET etc. are currently not supported.

It uses the shared components from:
* https://github.com/TOOP4EU/toop-commons-ng
* https://github.com/TOOP4EU/data-services-directory

# News and Noteworthy

Status of `2.0.0-rc1`
* Integrated the standalone version `tc-jetty` into this repository
* Fixed the XSD validation for error responses in the context of Schematron validation
* Made some message exchange components implement equals/hashCode

2020-06-01: release of `2.0.0-beta5`
* Improved the validation API to create no more false positives
* Improved response of `/user/submit/...` if the SMP lookup failed
* The metadata of incoming messages are preserved and forwarded to DC/DP 
* Note the changed Schematron rules in "toop-commons-ng" 2.0.0-beta5

2020-05-26: release of `2.0.0-beta4`
* Initial version of TOOP Connector that can exchange files
* For dependency management reasons, the TOOP shared libraries are now available in https://github.com/TOOP4EU/toop-commons-ng

2020-05-21: release of `2.0.0-beta3`
* First proof of concept release that cannot yet send documents

# Maven coordinates

```xml
      <dependency>
        <groupId>eu.toop</groupId>
        <artifactId>tc-webapp</artifactId>
        <version>2.0.0-beta5</version>
        <type>war</type>
      </dependency>
```

or download directly from Maven Central: https://repo1.maven.org/maven2/eu/toop/tc-webapp/

# Running

The `tc-webapp` module is a web application that can be deployed in arbitrary application servers supporting the Servlet specification 3.1 and onwards.

**Note** there are classloader issues running TOOP Connector NG in Tomcat - we suggest to use Jetty until we have figured out how to resolve it. 

## tc-jetty

Standalone version of TOOP Connector NG (integrated since 2.0.0-rc1)

Usage:

```
Usage: tc-jetty [-hV] [-p Port] [-s Stop Port] command
Standalone TOOP Connector NG
      command                What to do. Options are start, stop
  -h, --help                 Show this help message and exit.
  -p, --port Port            Port to run the TOOP Connector on (default: 8080)
  -s, --stopPort Stop Port   Internal port to watch for the shutdown command
                               (default: 8079)
  -V, --version              Print version information and exit.
```

Start via `start` and shut it down with `stop`.

How to run it: invoke the following command, replacing `x.y.z` with the effective version number

```
java -jar tc-jetty-x.y.z-full.jar start
```

to add a custom configuration file add the propert "config.file" like this:

```
java -Dconfig.file=/path/to/your/file.properties -jar tc-jetty-x.y.z-full.jar start
```

Binary versions of the TOOP Connector are available at https://repo1.maven.org/maven2/eu/toop/tc-jetty/

# Design considerations

Compared to the old design, certain architectural decisions changed which lead to a new architecture of the TOOP Connector.
Previously the application was designed to be a separate web application that was deployed between the DC/DP and the AS4 Gateway.
The TOOP Connector NG is both a library and a standalone solution that helps you do common stuff on the DC and the DP side.

Major changes compared to the old TOOP Connector:
* The semantic mapping service invocation was removed. The old solution was not satisfying.
* The "multiple DPs" option was removed. This responsibility was moved to the DC.
* The TOOP Directory was replaced by the DSD (Data Service Directory)
* No more usage of ASIC
* The content previously contained in "toop-commons" is now included in this project. The "toop-interface" project is no longer needed."
* The TOOP Connector is now stateless

What is now contained in the TOOP Connector NG:
* Support for performing dynamic discovery lookups (as before)
* Support for Schematron validation of the EDM Requests and Responses (as before)
* Support for querying the DSD (new - was previously the TOOP Directory)
* Support for communicating with the AS4 gateway (as before)

# Building

Requires at least

* Java 1.8 or later
* Apache Maven for building

Do a simple `mvn clean install` on the command line.
