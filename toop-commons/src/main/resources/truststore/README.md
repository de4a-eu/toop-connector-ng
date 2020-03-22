PLAYGROUND ONLY trust store.

Version 4.1 as of 2019-12-05

Change from v4 to v4.1:
Updated the SML TLS certificate chain.

Change from v3 to v4: 
Hi all, as mentioned this morning on our call I had to generated new key and certificates for the CA’s in the TOOP pilot PKI due to loss of the old keys’ passwords :enttäuscht:. In attached ZIP you find the new certificates for the three CAs in the PKI (root, authorities (DC/DP) and AP). These certificates must be *added* to the truststore of the connector and gateways as the old certificate should still be accepted so we don’t need to renew all the already issued certificates. Once again sorry for the inconvenience this causes…

Password: `toop4eu`

Contained aliases:
* Root CA: `toop pilots test root ca`
    * AS4 AP gateway CA: `toop pilots test access points ca`
    * DC and DP CA: `toop pilots test competent authorities ca`
* Root CA v2: `toop pilots test root ca v2` (since trust store v4)
    * AS4 AP gateway CA: `toop pilots test access points ca v2` (since trust store v4)
    * DC and DP CA: `toop pilots test competent authorities ca v2` (since trust store v4)
 * CEF Root CA: `connectivity test root ca`
    * SMP CA: `toop pilots test smp ca (connectivity test root ca)`
 * SML TLS Root CA: `globalsign root ca`
    * SML TLS CA: `globalsign organization validation ca - sha256 - g2 (globalsign root ca)`
