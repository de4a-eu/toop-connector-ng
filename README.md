# TOOP Connector NG

The TOOP Connector NG is a set of shared utility functions that you CAN include in your DC (Data Consumer) and/or DP (Data Provider) to perform common tasks that are required for a safe and interoperable data exchange. The TOOP Connector NG can be used both as a Java library AND via an REST API.  
The TOOP Connector NG is a Java only solution. Other environments like .NET etc. are currently not supported.

It uses the shared components from:
* https://github.com/TOOP4EU/toop-commons-ng
* https://github.com/TOOP4EU/data-services-directory

Note: this is the successor project of the old [toop-interface](https://github.com/TOOP4EU/toop-interface) and [toop-connector](https://github.com/TOOP4EU/toop-connector) projects.

# Modules

This project consists of the following sub modules:
* **tc-api** - the shared data structures, building on top of the "toop-edm" project (see toop-commons-ng)
* **tc-mem-external** - connection between TOOP Connector and an external AS4 gateway (mem = message exchange module)
* **tc-mem-phase4** - a built-in AS4 gateway for the TOOP Connector avoiding the use of an external AS4 gateway
* **tc-main** - contains the business logic functionality as an embeddable JAR
* **tc-web-api** - contains the REST API that invokes the functionality from "tc-main"
* **tc-webapp** - a web application (WAR) that includes the "tc-web-api" project
* **tc-jetty** - a self-contained application that combines the "tc-webapp" project with a Jetty instance - this serves as the basis for the Docker image but can also be run standalone

# Maven coordinates

Use one of the following artefacts for inclusion, replacing `x.y.z` with the effective version number:

To integrate the TOOP Connector NG directly into your DC/DP without the HTTP bridge:

```xml
      <dependency>
        <groupId>eu.toop</groupId>
        <artifactId>tc-main</artifactId>
        <version>x.y.z</version>
      </dependency>
```

To use the full WAR file (unlikely in Maven):

```xml
      <dependency>
        <groupId>eu.toop</groupId>
        <artifactId>tc-webapp</artifactId>
        <version>x.y.z</version>
        <type>war</type>
      </dependency>
```

or download directly from Maven Central: https://repo1.maven.org/maven2/eu/toop/tc-webapp/

# Configuration

The configuration file is called `application.properties` and is search by default in the classpath.
Custom configuration files can be located like this:
1. if the system property `config.resource` or the environment variable `CONFIG_RESOURCE` is present, and it points to an existing classpath resource, the first one matching is used - priority 200 or determined by the system property `config.resource.priority` or the environment variable `CONFIG_RESOURCE_PRIORITY`. Note: the file type is determined by the extension and defaults to "properties".
1. if the system property `config.file` or the environment variable `CONFIG_FILE` is present, and it points to an existing file, it is used - priority 200 or determined by the system property `config.file.priority` or the environment variable `CONFIG_FILE_PRIORITY`. Note: the file type is determined by the extension and defaults to "properties".
1. if the system property `config.url` or the environment variable `CONFIG_URL` is present, and it points to an existing URL, it is used - priority 200 or determined by the system property `config.url.priority` or the environment variable `CONFIG_URL_PRIORITY`. Note: the file type is determined by the extension and defaults to "properties".

The following configuration properties are **supported** - some of them have default values:

* **`global.debug`** (boolean) - enable development debug functionality
* **`global.production`** (boolean) - enable production mode (performance optimizations, less checks)
* **`global.instancename`** (string) - this is only used as the log prefix if the tracker is used
* **`toop.tracker.enabled`** (boolean) - enable or disable the remote tracker
* **`toop.tracker.url`** (string) - the URL where the tracker is collecting data elements
* **`toop.tracker.topic`** (string) - the TOOP tracker topic (left pane)
* **`toop.dsd.service.baseurl`** (string) - the URL of the DSD
* **`toop.r2d2.usedns`** (boolean) - use the SML system to dynamically discover partner systems?
* **`toop.r2d2.sml.name`** (string) - internal name of the SML
* **`toop.r2d2.sml.dnszone`** (string) - the DNS zone of the SML
* **`toop.r2d2.sml.serviceurl`** (string) - the management service URL of the SML
* **`toop.r2d2.sml.clientcert`** (boolean) - is a client certificate need when talking to this SML?
* **`toop.r2d2.smp.url`** (string) - the absolute URL of the SMP to use, if `toop.r2d2.usedns` is set to `false`
* **`toop.mem.implementation`** (string) - the ID of the AS4 implementation to use. Can be either `external` or `phase4` - depending on this, different configuration properties must be configured (see below)
* **`http.proxy.enabled`** (boolean) - is an HTTP proxy needed?
* **`http.proxy.address`** (string) - the URL of the proxy server (including the scheme)
* **`http.proxy.port`** (int) - the port to access the HTTP proxy server
* **`http.proxy.non-proxy`** (string) - a list of hosts that should not be proxied. Use the pipe character as the separator for multiple entries.
* **`http.tls.trustall`** (boolean) - use this to disable the hostname and trusted certificate check for SSL/TLS connections
* **`http.connection-timeout`** (int) - the HTTP connection timeout in milliseconds
* **`http.read-timeout`** (int) - the HTTP read/socket timeout in milliseconds

Note: this TOOP Connector uses a different configuration engine than the old version.

Note: see https://github.com/TOOP4EU/toop-connector-ng/blob/master/tc-webapp/src/main/resources/application.properties for the default configuration file

### Properties for MEM implementation `external`

* **`toop.mem.as4.endpoint`** (string) - the AS4 endpoint
* **`toop.mem.as4.gw.partyid`** (string) - the AS4 gateway party ID
* **`toop.mem.as4.tc.partyid`** (string) - the AS4 TOOP Connector party ID
* **`toop.mem.as4.notificationWaitTimeout`** (long) - the timeout for a notification in milliseconds
* **`toop.mem.incoming.url`** (string) - the URL inside your DC/DP where incoming messages (of type `TCIncomingMessage`) should be send to
* **`toop.mem.outgoing.dump.enabled`** (boolean) (since v2.0.0-rc3) - enable or disable the dumping of outgoing messages. By default this is disabled. 
* **`toop.mem.outgoing.dump.path`** (string) (since v2.0.0-rc3) - the file system directory in which the dumps of the outgoing messages are stored. This should be an absolute path. The filenames in the directory start with `toop-mem-external-outgoing-`.
* **`toop.mem.incoming.dump.enabled`** (boolean) (since v2.0.0-rc3) - enable or disable the dumping of incoming messages. By default this is disabled.
* **`toop.mem.incoming.dump.path`** (string) (since v2.0.0-rc3) - the file system directory in which the dumps of the incoming messages are stored. This should be an absolute path. The filenames in the directory start with `toop-mem-external-incoming-`.

Note: the receiving endpoint for MEM implementation `external` is `/from-as4` - this must be part of your SMP endpoint URL.

### Properties for MEM implementation `phase4`

* **`phase4.datapath`** (string) - the absolute path to a local directory to store data
* **`phase4.debug.http`** (boolean) - enable or disable HTTP debugging for AS4 transmissions. The default value is `false`.
* **`phase4.debug.incoming`** (boolean) - enable or disable debug logging for incoming AS4 transmissions. The default value is `false`.
* **`phase4.dump.incoming.path`** (string) (since v2.0.0-rc3) - an optional absolute directory path where the incoming AS4 messages should be dumped to. Disabled by default.
* **`phase4.dump.outgoing.path`** (string) (since v2.0.0-rc3) - an optional absolute directory path where the outgoing AS4 messages should be dumped to. Disabled by default.
* **`phase4.send.fromparty.id`** (string) (since v2.0.0-rc3) - the from party ID to be used for outgoing messages. Previous versions need to use **`toop.mem.as4.tc.partyid`** - starting from RC3 this property is still used as a fallback)
* **`phase4.send.response.folder`** (string) - an optional folder, where sent responses should be stored. If this property is not provided, they are not stored
* **`phase4.keystore.type`** (string) - the type of the keystore (either "JKS" or "PKCS12" - case insensitive) - defaults to JKS.
* **`phase4.keystore.path`** (string) - the path to the keystore (can be classpath relative or an absolute file)
* **`phase4.keystore.password`** (string) - the password to access the keystore 
* **`phase4.keystore.key-alias`** (string) - the alias of the key in the keystore (may be case sensitive)
* **`phase4.keystore.key-password`** (string) - the password to access the key in the keystore
* **`phase4.truststore.type`** (string) - the type of the truststore (either "JKS" or "PKCS12" - case insensitive) - defaults to JKS.
* **`phase4.truststore.path`** (string) - the path to the truststore (can be classpath relative or an absolute file)
* **`phase4.truststore.password`** (string) - the password to access the truststore 

Note: the receiving endpoint for MEM implementation `phase4` is `/phase4` - this must be part of your SMP endpoint URL.

# Running

The `tc-webapp` module is a web application that can be deployed in arbitrary application servers supporting the Servlet specification 3.1 and onwards.

**Note** there were classloader issues running TOOP Connector NG in Tomcat (prior to - we suggest to use Jetty until we have figured out how to resolve it. 

## tc-jetty

Standalone version of TOOP Connector NG

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

to add a custom configuration file add the property "config.file" like this:

```
java -Dconfig.file=/path/to/your/file.properties -jar tc-jetty-x.y.z-full.jar start
```

Binary versions of the standalone TOOP Connector NG are available at https://repo1.maven.org/maven2/eu/toop/tc-jetty/

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

# News and Noteworthy

Next version:
* Updated to phase4 0.10.1 which finally fixes the XLink JAXB error, since it no longer contains its own version of xlink.xsd
* Added new configuration options to dump incoming and outgoing AS4 messages more easily 

2020-06-19: release of `2.0.0-rc2`
* Fixed a regression in `/user/submit/...` API that accidentally delivered HTTP 400 in case of success

2020-06-18: release of `2.0.0-rc1`
* Integrated the standalone version `tc-jetty` into this repository
* Fixed the XSD validation for error responses in the context of Schematron validation
* Made some message exchange components implement equals/hashCode
* This version should again work in Tomcat
* Changed class name `IDDErrorHandler` to `ITCErrorHandler`
* Changed the API calls to return HTTP 400 in case of error
* The API `/dsd/dp/{datasetType}` (without country code) was disabled

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
