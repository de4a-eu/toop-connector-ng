/**
 * Copyright (C) 2018-2020 toop.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.toop.commons.supplementary.tools;

import java.io.File;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.annotation.Nonnull;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.bc.PBCProvider;
import com.helger.commons.CGlobal;
import com.helger.commons.io.file.FileHelper;
import com.helger.commons.io.stream.NonBlockingByteArrayInputStream;

/**
 * <b>DO NOT EXECUTE THIS</b><br>
 * Handle with care - needed once only :)<br>
 * This class creates the TOOP playground CA. It is a certificate hierarchy with
 * a self-signed root certificate (CN=playground-root) and a set of derived
 * child certificates.
 *
 * @author Philip Helger
 */
public final class MainCreateCert
{
  private static final Logger LOGGER = LoggerFactory.getLogger (MainCreateCert.class);
  private static final Provider PROVIDER = PBCProvider.getProvider ();
  private static final int RSA_KEY_LEN = 2048;
  private static final String CN_ROOT = "toop-playground-root";
  private static final String SIGNING_ALGO = "SHA256WithRSAEncryption";

  @Nonnull
  private static Date _now ()
  {
    return new Date (System.currentTimeMillis ());
  }

  @Nonnull
  private static Date _plusDays (final long nDays)
  {
    return new Date (System.currentTimeMillis () + nDays * 24 * CGlobal.MILLISECONDS_PER_HOUR);
  }

  @Nonnull
  private static X500Name _x500 (final String fqdn)
  {
    return new X500Name ("CN=" + fqdn + ", O=toop4eu, C=EU");
  }

  @Nonnull
  static KeyPair genRSAKeyPair () throws NoSuchAlgorithmException
  {
    // Get RSA key factory:
    final KeyPairGenerator kpg = KeyPairGenerator.getInstance ("RSA", PROVIDER);
    // Generate RSA public/private key pair:
    kpg.initialize (RSA_KEY_LEN);
    return kpg.genKeyPair ();
  }

  @Nonnull
  static X509Certificate generateCertificate (final String fqdn,
                                              final KeyPair keypair,
                                              final Date notAfter) throws Exception
  {
    final PrivateKey key = keypair.getPrivate ();
    // Prepare the information required for generating an X.509 certificate.
    final X500Name owner = _x500 (fqdn);
    final X509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder (owner,
                                                                              new BigInteger (64,
                                                                                              SecureRandom.getInstanceStrong ()),
                                                                              _now (),
                                                                              notAfter,
                                                                              owner,
                                                                              keypair.getPublic ());

    final ContentSigner signer = new JcaContentSignerBuilder (SIGNING_ALGO).build (key);
    final X509CertificateHolder certHolder = builder.build (signer);
    final X509Certificate cert = new JcaX509CertificateConverter ().setProvider (PROVIDER).getCertificate (certHolder);
    cert.verify (keypair.getPublic ());

    return cert;
  }

  @Nonnull
  static PKCS10CertificationRequest createCSR (final X509Certificate cert, final KeyPair keyPair) throws Exception
  {
    final Principal principal = cert.getSubjectDN ();
    // generate certification request
    final X500Name x500Name = new X500Name (principal.toString ());
    final PKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder (x500Name,
                                                                                                   keyPair.getPublic ());
    final JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder (SIGNING_ALGO);
    final ContentSigner signer = csBuilder.build (keyPair.getPrivate ());
    return p10Builder.build (signer);
  }

  @Nonnull
  static X509Certificate signCSR (final PKCS10CertificationRequest inputCSR,
                                  final PrivateKey caPrivate,
                                  final KeyPair pair,
                                  final Date notAfter) throws Exception
  {

    final AlgorithmIdentifier sigAlgId = new DefaultSignatureAlgorithmIdentifierFinder ().find (SIGNING_ALGO);
    final AlgorithmIdentifier digAlgId = new DefaultDigestAlgorithmIdentifierFinder ().find (sigAlgId);

    final AsymmetricKeyParameter foo = PrivateKeyFactory.createKey (caPrivate.getEncoded ());
    final SubjectPublicKeyInfo keyInfo = SubjectPublicKeyInfo.getInstance (pair.getPublic ().getEncoded ());

    final X509v3CertificateBuilder myCertificateGenerator = new X509v3CertificateBuilder (_x500 (CN_ROOT),
                                                                                          new BigInteger (64,
                                                                                                          SecureRandom.getInstanceStrong ()),
                                                                                          _now (),
                                                                                          notAfter,
                                                                                          inputCSR.getSubject (),
                                                                                          keyInfo);

    final ContentSigner sigGen = new BcRSAContentSignerBuilder (sigAlgId, digAlgId).build (foo);

    final X509CertificateHolder holder = myCertificateGenerator.build (sigGen);

    final org.bouncycastle.asn1.x509.Certificate eeX509CertificateStructure = holder.toASN1Structure ();

    // Read Certificate
    try (final InputStream is1 = new NonBlockingByteArrayInputStream (eeX509CertificateStructure.getEncoded ()))
    {
      final CertificateFactory cf = CertificateFactory.getInstance ("X.509", PROVIDER);
      final X509Certificate theCert = (X509Certificate) cf.generateCertificate (is1);
      return theCert;
    }
  }

  public static void main (final String [] args) throws Exception
  {
    Security.addProvider (PROVIDER);
    final String sPassword = "toop4eu";

    // Start the key and trust store
    final KeyStore aKS = KeyStore.getInstance ("JKS");
    aKS.load (null, null);
    final KeyStore aTS = KeyStore.getInstance ("JKS");
    aTS.load (null, null);

    // Root stuff
    final KeyPair aRootKey = genRSAKeyPair ();
    final X509Certificate aRootCert = generateCertificate (CN_ROOT, aRootKey, _plusDays (9999));

    aKS.setKeyEntry ("root-key", aRootKey.getPrivate (), sPassword.toCharArray (), new Certificate [] { aRootCert });
    aTS.setCertificateEntry ("root-cert", aRootCert);

    // all child certificates
    for (final String sContext : new String [] { "smp",
                                                 "directory",
                                                 "package-tracker",
                                                 "sms",
                                                 "elonia-dc",
                                                 "freedonia-dc",
                                                 "elonia-dp",
                                                 "freedonia-dp",
                                                 "elonia-mp",
                                                 "freedonia-mp",
                                                 "elonia-as4",
                                                 "freedonia-as4" })
    {
      final Date aNotAfter = _plusDays (1000);
      final KeyPair aSMPKey = genRSAKeyPair ();
      X509Certificate aSMPCert = generateCertificate (sContext, aSMPKey, aNotAfter);
      final PKCS10CertificationRequest aSMPCSR = createCSR (aSMPCert, aSMPKey);
      aSMPCert = signCSR (aSMPCSR, aRootKey.getPrivate (), aSMPKey, aNotAfter);

      // Add to keystore
      aKS.setKeyEntry (sContext + "-key",
                       aSMPKey.getPrivate (),
                       sPassword.toCharArray (),
                       new Certificate [] { aSMPCert, aRootCert });
      // Add to truststore
      aTS.setCertificateEntry (sContext + "-cert", aSMPCert);
    }

    // Write to file
    aKS.store (FileHelper.getOutputStream (new File ("playground-keystore.jks")), sPassword.toCharArray ());
    aTS.store (FileHelper.getOutputStream (new File ("playground-truststore.jks")), sPassword.toCharArray ());

    LOGGER.info ("Done creating keys and writing key store");
  }
}
