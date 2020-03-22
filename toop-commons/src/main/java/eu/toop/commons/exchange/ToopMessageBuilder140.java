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
package eu.toop.commons.exchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.asic.AsicReaderFactory;
import com.helger.asic.AsicWriterFactory;
import com.helger.asic.ESignatureMethod;
import com.helger.asic.IAsicReader;
import com.helger.asic.IAsicWriter;
import com.helger.asic.SignatureHelper;
import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.io.stream.NonBlockingByteArrayInputStream;
import com.helger.commons.io.stream.NonBlockingByteArrayOutputStream;
import com.helger.commons.mime.CMimeType;
import com.helger.commons.string.StringHelper;
import com.helger.commons.text.IMultilingualText;
import com.helger.datetime.util.PDTXMLConverter;

import eu.toop.commons.codelist.EPredefinedDocumentTypeIdentifier;
import eu.toop.commons.codelist.EPredefinedProcessIdentifier;
import eu.toop.commons.concept.ConceptValue;
import eu.toop.commons.concept.EConceptType;
import eu.toop.commons.dataexchange.v140.TDEAddressType;
import eu.toop.commons.dataexchange.v140.TDEAddressWithLOAType;
import eu.toop.commons.dataexchange.v140.TDEConceptRequestType;
import eu.toop.commons.dataexchange.v140.TDEDataConsumerType;
import eu.toop.commons.dataexchange.v140.TDEDataElementRequestType;
import eu.toop.commons.dataexchange.v140.TDEDataElementResponseValueType;
import eu.toop.commons.dataexchange.v140.TDEDataProviderType;
import eu.toop.commons.dataexchange.v140.TDEDataRequestAuthorizationType;
import eu.toop.commons.dataexchange.v140.TDEDataRequestSubjectType;
import eu.toop.commons.dataexchange.v140.TDEDocumentRequestType;
import eu.toop.commons.dataexchange.v140.TDEErrorType;
import eu.toop.commons.dataexchange.v140.TDELegalPersonType;
import eu.toop.commons.dataexchange.v140.TDENaturalPersonType;
import eu.toop.commons.dataexchange.v140.TDERoutingInformationType;
import eu.toop.commons.dataexchange.v140.TDETOOPRequestType;
import eu.toop.commons.dataexchange.v140.TDETOOPResponseType;
import eu.toop.commons.error.EToopErrorCategory;
import eu.toop.commons.error.EToopErrorCode;
import eu.toop.commons.error.EToopErrorOrigin;
import eu.toop.commons.error.EToopErrorSeverity;
import eu.toop.commons.error.IToopErrorCode;
import eu.toop.commons.error.ToopErrorException;
import eu.toop.commons.jaxb.ToopReader;
import eu.toop.commons.jaxb.ToopWriter;
import eu.toop.commons.jaxb.ToopXSDHelper140;
import eu.toop.commons.usecase.EToopEntityType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.BinaryObjectType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.IdentifierType;

/**
 * A helper class to build TOOP data model 1.4.0 stuff.
 *
 * @author Philip Helger
 * @since 0.10.0
 */
@Immutable
public final class ToopMessageBuilder140
{
  public static final ClassPathResource TOOP_XSD = new ClassPathResource ("/xsd/toop/TOOP_DataExchange-1.4.0.xsd",
                                                                          ToopMessageBuilder140.class.getClassLoader ());
  private static final String ENTRY_NAME_TOOP_DATA_REQUEST = "TOOPRequest";
  private static final String ENTRY_NAME_TOOP_DATA_RESPONSE = "TOOPResponse";

  private static final Logger LOGGER = LoggerFactory.getLogger (ToopMessageBuilder140.class);

  private ToopMessageBuilder140 ()
  {}

  public static void createRequestMessageAsic (@Nonnull final TDETOOPRequestType aRequest,
                                               @Nonnull final OutputStream aOS,
                                               @Nonnull final SignatureHelper aSigHelper) throws ToopErrorException
  {
    createRequestMessageAsic (aRequest, aOS, aSigHelper, null);
  }

  public static void createRequestMessageAsic (@Nonnull final TDETOOPRequestType aRequest,
                                               @Nonnull final OutputStream aOS,
                                               @Nonnull final SignatureHelper aSigHelper,
                                               @Nullable final Iterable <? extends AsicWriteEntry> aAttachments) throws ToopErrorException
  {
    ValueEnforcer.notNull (aRequest, "Request");
    ValueEnforcer.notNull (aOS, "ArchiveOutput");
    ValueEnforcer.notNull (aSigHelper, "SignatureHelper");

    final AsicWriterFactory aAsicWriterFactory = AsicWriterFactory.newFactory (ESignatureMethod.CAdES);
    try
    {
      final IAsicWriter aAsicWriter = aAsicWriterFactory.newContainer (aOS);
      final byte [] aXML = ToopWriter.request140 ().getAsBytes (aRequest);
      if (aXML == null)
        throw new ToopErrorException ("Error marshalling the TOOP Request", EToopErrorCode.TC_001);

      aAsicWriter.add (new NonBlockingByteArrayInputStream (aXML),
                       ENTRY_NAME_TOOP_DATA_REQUEST,
                       CMimeType.APPLICATION_XML);

      // Add optional attachments
      if (aAttachments != null)
        for (final AsicWriteEntry aEntry : aAttachments)
          aAsicWriter.add (new NonBlockingByteArrayInputStream (aEntry.payload ()),
                           aEntry.getEntryName (),
                           aEntry.getMimeType ());

      aAsicWriter.sign (aSigHelper);
      LOGGER.info ("Successfully created request ASiC");
    }
    catch (final IOException ex)
    {
      throw new ToopErrorException ("Error creating signed ASIC container", ex, EToopErrorCode.TC_001);
    }
  }

  @Deprecated
  public static void createResponseMessageAsic (@Nonnull final TDETOOPResponseType aResponse,
                                                @Nonnull final OutputStream aOS,
                                                @Nonnull final SignatureHelper aSigHelper) throws ToopErrorException
  {
    createResponseMessageAsic (aResponse, aOS, aSigHelper, null);
  }

  public static void createResponseMessageAsic (@Nonnull final TDETOOPResponseType aResponse,
                                                @Nonnull final OutputStream aOS,
                                                @Nonnull final SignatureHelper aSigHelper,
                                                @Nullable final Iterable <? extends AsicWriteEntry> aAttachments) throws ToopErrorException
  {
    ValueEnforcer.notNull (aResponse, "Response");
    ValueEnforcer.notNull (aOS, "ArchiveOutput");
    ValueEnforcer.notNull (aSigHelper, "SignatureHelper");

    final AsicWriterFactory aAsicWriterFactory = AsicWriterFactory.newFactory (ESignatureMethod.CAdES);
    try
    {
      final IAsicWriter aAsicWriter = aAsicWriterFactory.newContainer (aOS);
      final byte [] aXML = ToopWriter.response140 ().getAsBytes (aResponse);
      if (aXML == null)
        throw new ToopErrorException ("Error marshalling the TOOP Response", EToopErrorCode.TC_001);

      aAsicWriter.add (new NonBlockingByteArrayInputStream (aXML),
                       ENTRY_NAME_TOOP_DATA_RESPONSE,
                       CMimeType.APPLICATION_XML);

      // Add optional attachments
      if (aAttachments != null)
        for (final AsicWriteEntry aEntry : aAttachments)
          aAsicWriter.add (new NonBlockingByteArrayInputStream (aEntry.payload ()),
                           aEntry.getEntryName (),
                           aEntry.getMimeType ());

      aAsicWriter.sign (aSigHelper);
      LOGGER.info ("Successfully created response ASiC");
    }
    catch (final IOException ex)
    {
      throw new ToopErrorException ("Error creating signed ASIC container", ex, EToopErrorCode.TC_001);
    }
  }

  /**
   * Parse the given InputStream as an ASiC container and return the contained
   * {@link TDETOOPRequestType} or {@link TDETOOPResponseType}.
   *
   * @param aIS
   *        Input stream to read from. May not be <code>null</code>.
   * @return New {@link TDETOOPRequestType} or {@link TDETOOPResponseType}.
   *         every time the method is called or <code>null</code> if none is
   *         contained in the ASIC.
   * @throws IOException
   *         In case of IO error
   * @deprecated Use {@link #parseRequestOrResponse(InputStream, Consumer)}
   *             instead
   */
  @Nullable
  @Deprecated
  @ReturnsMutableObject
  public static Serializable parseRequestOrResponse (@Nonnull @WillClose final InputStream aIS) throws IOException
  {
    return parseRequestOrResponse (aIS, null);
  }

  /**
   * Parse the given InputStream as an ASiC container and return the contained
   * {@link TDETOOPRequestType} or {@link TDETOOPResponseType}.
   *
   * @param aIS
   *        Input stream to read from. May not be <code>null</code>.
   * @param aAttachmentProcessor
   *        An optional consumer for handling attachments in the ASiC container.
   *        Usually attachments are only present in responses (as document
   *        values). 0-n attachments may be present, so the consumer can be
   *        invoked more than once. May be <code>null</code>.
   * @return New {@link TDETOOPRequestType} or {@link TDETOOPResponseType}.
   *         every time the method is called or <code>null</code> if none is
   *         contained in the ASIC.
   * @throws IOException
   *         In case of IO error
   */
  @Nullable
  @ReturnsMutableObject
  public static Serializable parseRequestOrResponse (@Nonnull @WillClose final InputStream aIS,
                                                     @Nullable final Consumer <AsicReadEntry> aAttachmentProcessor) throws IOException
  {
    ValueEnforcer.notNull (aIS, "archiveInput");

    boolean bNeedResponse = true;
    Serializable aResult = null;
    try (final IAsicReader aAsicReader = AsicReaderFactory.newFactory ().open (aIS))
    {
      String sEntryName;
      while ((sEntryName = aAsicReader.getNextFile ()) != null)
      {
        if (bNeedResponse && sEntryName.equals (ENTRY_NAME_TOOP_DATA_REQUEST))
        {
          try (final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ())
          {
            aAsicReader.writeFile (aBAOS);
            aResult = ToopReader.request140 ().read (aBAOS.getAsInputStream ());
            bNeedResponse = false;
          }
        }
        else
          if (bNeedResponse && sEntryName.equals (ENTRY_NAME_TOOP_DATA_RESPONSE))
          {
            try (final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ())
            {
              aAsicReader.writeFile (aBAOS);
              aResult = ToopReader.response140 ().read (aBAOS.getAsInputStream ());
              bNeedResponse = false;
            }
          }
          else
            if (aAttachmentProcessor != null)
            {
              // Handle all other elements, not specifically known
              // Since the order is unknown, handle attachments before and after
              // the main response
              try (final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ())
              {
                aAsicReader.writeFile (aBAOS);
                final AsicReadEntry aEntry = new AsicReadEntry (sEntryName, aBAOS.toByteArray ());
                aAttachmentProcessor.accept (aEntry);
              }
            }
      }
    }

    return aResult;
  }

  /**
   * Parse the given InputStream as an ASiC container and return the contained
   * {@link TDETOOPRequestType}.
   *
   * @param aIS
   *        Input stream to read from. May not be <code>null</code>.
   * @return New {@link TDETOOPRequestType} every time the method is called or
   *         <code>null</code> if none is contained in the ASIC.
   * @throws IOException
   *         In case of IO error
   * @deprecated Use {@link #parseRequestMessage(InputStream, Consumer)} instead
   */
  @Nullable
  @Deprecated
  @ReturnsMutableObject
  public static TDETOOPRequestType parseRequestMessage (@Nonnull @WillClose final InputStream aIS) throws IOException
  {
    return parseRequestMessage (aIS, null);
  }

  /**
   * Parse the given InputStream as an ASiC container and return the contained
   * {@link TDETOOPRequestType}.
   *
   * @param aIS
   *        Input stream to read from. May not be <code>null</code>.
   * @param aAttachmentProcessor
   *        An optional consumer for handling attachments in the ASiC container.
   *        0-n attachments may be present, so the consumer can be invoked more
   *        than once. May be <code>null</code>.
   * @return New {@link TDETOOPRequestType} every time the method is called or
   *         <code>null</code> if none is contained in the ASIC.
   * @throws IOException
   *         In case of IO error
   */
  @Nullable
  @ReturnsMutableObject
  public static TDETOOPRequestType parseRequestMessage (@Nonnull @WillClose final InputStream aIS,
                                                        @Nullable final Consumer <AsicReadEntry> aAttachmentProcessor) throws IOException
  {
    ValueEnforcer.notNull (aIS, "archiveInput");

    // No attachment processor needed for requests
    final Serializable aObj = parseRequestOrResponse (aIS, aAttachmentProcessor);
    if (aObj instanceof TDETOOPRequestType)
      return (TDETOOPRequestType) aObj;

    return null;
  }

  /**
   * Parse the given InputStream as an ASiC container and return the contained
   * {@link TDETOOPResponseType}.
   *
   * @param aIS
   *        Input stream to read from. May not be <code>null</code>.
   * @return New {@link TDETOOPResponseType} every time the method is called or
   *         <code>null</code> if none is contained in the ASIC.
   * @throws IOException
   *         In case of IO error
   * @deprecated Use {@link #parseResponseMessage(InputStream, Consumer)}
   *             instead
   */
  @Nullable
  @Deprecated
  @ReturnsMutableObject
  public static TDETOOPResponseType parseResponseMessage (@Nonnull @WillClose final InputStream aIS) throws IOException
  {
    return parseResponseMessage (aIS, null);
  }

  /**
   * Parse the given InputStream as an ASiC container and return the contained
   * {@link TDETOOPResponseType}.
   *
   * @param aIS
   *        Input stream to read from. May not be <code>null</code>.
   * @param aAttachmentProcessor
   *        An optional consumer for handling attachments in the ASiC container.
   *        0-n attachments may be present, so the consumer can be invoked more
   *        than once. May be <code>null</code>.
   * @return New {@link TDETOOPResponseType} every time the method is called or
   *         <code>null</code> if none is contained in the ASIC.
   * @throws IOException
   *         In case of IO error
   */
  @Nullable
  @ReturnsMutableObject
  public static TDETOOPResponseType parseResponseMessage (@Nonnull @WillClose final InputStream aIS,
                                                          @Nullable final Consumer <AsicReadEntry> aAttachmentProcessor) throws IOException
  {
    ValueEnforcer.notNull (aIS, "archiveInput");

    final Serializable aObj = parseRequestOrResponse (aIS, aAttachmentProcessor);
    if (aObj instanceof TDETOOPResponseType)
      return (TDETOOPResponseType) aObj;

    return null;
  }

  @Deprecated
  @Nonnull
  public static TDEAddressWithLOAType createMockAddressType (@Nonnull @Nonempty final String sCountryCode)
  {
    final TDEAddressWithLOAType aAddress = new TDEAddressWithLOAType ();
    aAddress.addAddressLine (ToopXSDHelper140.createTextWithLOA ("Hintere Zollamtstraße 4"));
    aAddress.addAddressLine (ToopXSDHelper140.createTextWithLOA ("1030 Wien"));
    aAddress.setStreetName (ToopXSDHelper140.createTextWithLOA ("Hintere Zollamtstraße"));
    aAddress.setStreetNumber (ToopXSDHelper140.createTextWithLOA ("4"));
    aAddress.setCity (ToopXSDHelper140.createTextWithLOA ("Wien"));
    aAddress.setPostCode (ToopXSDHelper140.createTextWithLOA ("1030"));
    // Destination country to use
    aAddress.setCountryCode (ToopXSDHelper140.createCodeWithLOA (sCountryCode));
    return aAddress;
  }

  @Deprecated
  @Nonnull
  public static TDEDataRequestSubjectType createMockDataRequestSubject (@Nonnull @Nonempty final String sSrcCountryCode,
                                                                        @Nonnull @Nonempty final String sDstCountryCode,
                                                                        final boolean bLegalPerson,
                                                                        @Nonnull final String sUniqueIdentifier)
  {
    final TDEDataRequestSubjectType aRet = new TDEDataRequestSubjectType ();
    if (bLegalPerson)
    {
      // Codelist value (legacy)
      aRet.setDataRequestSubjectTypeCode (ToopXSDHelper140.createCode (EToopEntityType.LEGAL_ENTITY.getID ()));

      final TDELegalPersonType aLE = new TDELegalPersonType ();
      aLE.setLegalPersonUniqueIdentifier (ToopXSDHelper140.createIdentifierWithLOA (sSrcCountryCode +
                                                                                    "/" +
                                                                                    sDstCountryCode +
                                                                                    "/" +
                                                                                    sUniqueIdentifier));
      aLE.setLegalName (ToopXSDHelper140.createTextWithLOA ("ACME Inc."));
      aLE.setLegalPersonLegalAddress (createMockAddressType (sDstCountryCode));
      aRet.setLegalPerson (aLE);
    }
    else
    {
      // Codelist value
      aRet.setDataRequestSubjectTypeCode (ToopXSDHelper140.createCode (EToopEntityType.NATURAL_PERSON.getID ()));

      final TDENaturalPersonType aNP = new TDENaturalPersonType ();
      aNP.setPersonIdentifier (ToopXSDHelper140.createIdentifierWithLOA (sSrcCountryCode +
                                                                         "/" +
                                                                         sDstCountryCode +
                                                                         "/" +
                                                                         sUniqueIdentifier));
      aNP.setFamilyName (ToopXSDHelper140.createTextWithLOA ("Helger"));
      aNP.setFirstName (ToopXSDHelper140.createTextWithLOA ("Philip"));
      aNP.setBirthDate (ToopXSDHelper140.createDateWithLOANow ());
      aNP.setNaturalPersonLegalAddress (createMockAddressType (sDstCountryCode));
      aRet.setNaturalPerson (aNP);
    }
    return aRet;
  }

  @Deprecated
  private static void _fillRequest (@Nonnull final TDETOOPRequestType aRet,
                                    @Nonnull final TDEDataRequestSubjectType aRequestSubject,
                                    @Nonnull final String sDCCountryCode,
                                    @Nonnull final String sDPCountryCode,
                                    @Nonnull final IdentifierType aSenderParticipantID,
                                    @Nonnull final EPredefinedDocumentTypeIdentifier eDocumentTypeID,
                                    @Nonnull final EPredefinedProcessIdentifier eProcessID,
                                    @Nullable final Iterable <? extends ConceptValue> aValues)
  {
    aRet.setDocumentUniversalUniqueIdentifier (ToopXSDHelper140.createIdentifierUUID ());
    aRet.setDocumentIssueDate (PDTXMLConverter.getXMLCalendarDateNow ());
    aRet.setDocumentIssueTime (PDTXMLConverter.getXMLCalendarTimeNow ());
    aRet.setCopyIndicator (ToopXSDHelper140.createIndicator (false));
    aRet.setSpecificationIdentifier (ToopXSDHelper140.createIdentifier (EPredefinedDocumentTypeIdentifier.DOC_TYPE_SCHEME,
                                                                        eDocumentTypeID.getID ()
                                                                                       .substring (0,
                                                                                                   eDocumentTypeID.getID ()
                                                                                                                  .indexOf ("##"))));
    aRet.setDataConsumerDocumentIdentifier (ToopXSDHelper140.createIdentifier ("whatsoever", null, "DC-ID-17"));

    if (false)
      aRet.setDataRequestIdentifier (ToopXSDHelper140.createIdentifier (UUID.randomUUID ().toString ()));

    {
      final TDERoutingInformationType aRoutingInfo = new TDERoutingInformationType ();
      // Document type ID
      aRoutingInfo.setDocumentTypeIdentifier (ToopXSDHelper140.createIdentifier (eDocumentTypeID.getScheme (),
                                                                                 eDocumentTypeID.getID ()));
      // Process ID
      aRoutingInfo.setProcessIdentifier (ToopXSDHelper140.createIdentifier (eProcessID.getScheme (),
                                                                            eProcessID.getID ()));
      {
        // Sender participant ID
        final IdentifierType aID = aSenderParticipantID.clone ();
        aID.setSchemeAgencyID ("0002");
        aRoutingInfo.setDataConsumerElectronicAddressIdentifier (aID);
      }
      // Sender country code
      aRoutingInfo.setDataConsumerCountryCode (ToopXSDHelper140.createCode (sDCCountryCode));
      // Destination country code
      aRoutingInfo.setDataProviderCountryCode (ToopXSDHelper140.createCode (sDPCountryCode));
      aRet.setRoutingInformation (aRoutingInfo);
    }

    {
      final TDEDataConsumerType aDC = new TDEDataConsumerType ();
      aDC.setDCUniqueIdentifier (ToopXSDHelper140.createIdentifier ("whatsoever", "9914", "ATU12345678"));
      aDC.setDCName (ToopXSDHelper140.createText ("Helger Enterprises"));
      final TDEAddressType aAddress = new TDEAddressType ();
      aAddress.setCountryCode (ToopXSDHelper140.createCodeWithLOA (sDCCountryCode));
      aDC.setDCLegalAddress (aAddress);
      aRet.setDataConsumer (aDC);
    }
    {
      aRet.setDataRequestSubject (aRequestSubject);
    }
    {
      final TDEDataRequestAuthorizationType aAuth = new TDEDataRequestAuthorizationType ();
      final BinaryObjectType aBO = new BinaryObjectType ();
      aBO.setValue ("11101010101010001110101".getBytes (StandardCharsets.ISO_8859_1));
      aBO.setMimeCode ("application/octet-stream");
      aAuth.setDataRequestConsentToken (aBO);
      aRet.setDataRequestAuthorization (aAuth);
    }

    if (aValues != null)
      for (final ConceptValue aCV : aValues)
      {
        final TDEDataElementRequestType aReq = new TDEDataElementRequestType ();
        aReq.setDataElementRequestIdentifier (ToopXSDHelper140.createIdentifier ("bla"));
        {
          final TDEConceptRequestType aSrcConcept = new TDEConceptRequestType ();
          aSrcConcept.setConceptTypeCode (ToopXSDHelper140.createCode (EConceptType.DC.getID ()));
          aSrcConcept.setSemanticMappingExecutionIndicator (ToopXSDHelper140.createIndicator (false));
          aSrcConcept.setConceptNamespace (ToopXSDHelper140.createIdentifier (aCV.getNamespace ()));
          aSrcConcept.setConceptName (ToopXSDHelper140.createText (aCV.getValue ()));
          aSrcConcept.addConceptDefinition (ToopXSDHelper140.createText ("Definition of " + aCV.getValue ()));
          aReq.setConceptRequest (aSrcConcept);
        }

        aRet.addDataElementRequest (aReq);
      }
  }

  @Deprecated
  @Nonnull
  public static TDETOOPRequestType createMockRequest (@Nonnull final TDEDataRequestSubjectType aRequestSubject,
                                                      @Nonnull final String sDCCountryCode,
                                                      @Nonnull final String sDPCountryCode,
                                                      @Nonnull final IdentifierType aSenderParticipantID,
                                                      @Nonnull final EPredefinedDocumentTypeIdentifier eDocumentTypeID,
                                                      @Nonnull final EPredefinedProcessIdentifier eProcessID,
                                                      @Nullable final Iterable <? extends ConceptValue> aValues)
  {
    ValueEnforcer.notNull (aRequestSubject, "RequestSubject");
    ValueEnforcer.notNull (aSenderParticipantID, "SenderParticipantID");
    ValueEnforcer.notNull (eDocumentTypeID, "DocumentTypeID");
    ValueEnforcer.notNull (eProcessID, "ProcessID");

    final TDETOOPRequestType aRet = new TDETOOPRequestType ();
    _fillRequest (aRet,
                  aRequestSubject,
                  sDCCountryCode,
                  sDPCountryCode,
                  aSenderParticipantID,
                  eDocumentTypeID,
                  eProcessID,
                  aValues);
    return aRet;
  }

  @Deprecated
  @Nonnull
  public static TDETOOPResponseType createMockResponse (@Nonnull final IdentifierType aSenderParticipantID,
                                                        @Nonnull final TDEDataRequestSubjectType aRequestSubject,
                                                        @Nonnull final String sDCCountryCode,
                                                        @Nonnull final String sDPCountryCode,
                                                        @Nonnull final EPredefinedDocumentTypeIdentifier eDocumentTypeID,
                                                        @Nonnull final EPredefinedProcessIdentifier eProcessID,
                                                        @Nullable final Iterable <? extends ConceptValue> aValues)
  {
    ValueEnforcer.notNull (aSenderParticipantID, "SenderParticipantID");
    ValueEnforcer.notNull (aRequestSubject, "RequestSubject");
    ValueEnforcer.notNull (eDocumentTypeID, "DocumentTypeID");
    ValueEnforcer.notNull (eProcessID, "ProcessID");

    final TDETOOPResponseType aRet = new TDETOOPResponseType ();
    // Values are added below
    _fillRequest (aRet,
                  aRequestSubject,
                  sDCCountryCode,
                  sDPCountryCode,
                  aSenderParticipantID,
                  eDocumentTypeID,
                  eProcessID,
                  null);

    aRet.setDataRequestIdentifier (ToopXSDHelper140.createIdentifier ("schas", "uuid", UUID.randomUUID ().toString ()));

    aRet.getRoutingInformation ()
        .setDataProviderElectronicAddressIdentifier (ToopXSDHelper140.createIdentifier ("0002",
                                                                                        "iso6523-actorid-upis",
                                                                                        "9915:test"));

    {
      final TDEDataRequestAuthorizationType aAuth = new TDEDataRequestAuthorizationType ();
      final BinaryObjectType aBO = new BinaryObjectType ();
      aBO.setValue ("11101010101010001110101".getBytes (StandardCharsets.ISO_8859_1));
      aBO.setMimeCode (CMimeType.APPLICATION_OCTET_STREAM.getAsString ());
      aAuth.setDataRequestConsentToken (aBO);
      aRet.setDataRequestAuthorization (aAuth);
    }

    if (aValues != null)
    {
      // Add data response values
      for (final ConceptValue aCV : aValues)
      {
        final TDEDataElementRequestType aReq = new TDEDataElementRequestType ();
        aReq.setDataElementRequestIdentifier (ToopXSDHelper140.createIdentifier ("bla"));
        {
          final TDEConceptRequestType aSrcConcept = new TDEConceptRequestType ();
          aSrcConcept.setConceptTypeCode (ToopXSDHelper140.createCode (EConceptType.DC.getID ()));
          aSrcConcept.setSemanticMappingExecutionIndicator (ToopXSDHelper140.createIndicator (false));
          aSrcConcept.setConceptNamespace (ToopXSDHelper140.createIdentifier (aCV.getNamespace ()));
          aSrcConcept.setConceptName (ToopXSDHelper140.createText (aCV.getValue ()));
          aSrcConcept.addConceptDefinition (ToopXSDHelper140.createText ("Definition of " + aCV.getValue ()));
          {
            final TDEConceptRequestType aToopConcept = new TDEConceptRequestType ();
            aToopConcept.setConceptTypeCode (ToopXSDHelper140.createCode (EConceptType.TC.getID ()));
            aToopConcept.setSemanticMappingExecutionIndicator (ToopXSDHelper140.createIndicator (false));
            aToopConcept.setConceptNamespace (ToopXSDHelper140.createIdentifier (aCV.getNamespace () + "-toop"));
            aToopConcept.setConceptName (ToopXSDHelper140.createText ("toop." + aCV.getValue ()));
            {
              final TDEConceptRequestType aDPConcept = new TDEConceptRequestType ();
              aDPConcept.setConceptTypeCode (ToopXSDHelper140.createCode (EConceptType.DP.getID ()));
              aDPConcept.setSemanticMappingExecutionIndicator (ToopXSDHelper140.createIndicator (false));
              aDPConcept.setConceptNamespace (ToopXSDHelper140.createIdentifier (aCV.getNamespace () + "-dp"));
              aDPConcept.setConceptName (ToopXSDHelper140.createText ("dp." + aCV.getValue ()));

              // Response value must be present
              final TDEDataElementResponseValueType aResponseValue = new TDEDataElementResponseValueType ();
              aResponseValue.setErrorIndicator (ToopXSDHelper140.createIndicator (false));
              aResponseValue.setResponseCode (ToopXSDHelper140.createCode ("anyCode"));
              aDPConcept.addDataElementResponseValue (aResponseValue);

              aToopConcept.addConceptRequest (aDPConcept);
            }
            aSrcConcept.addConceptRequest (aToopConcept);
          }
          aReq.setConceptRequest (aSrcConcept);
        }
        aRet.addDataElementRequest (aReq);
      }
    }
    else
    {
      // Add document response
      final TDEDocumentRequestType aDR = new TDEDocumentRequestType ();
      aDR.setDocumentRequestIdentifier (ToopXSDHelper140.createIdentifier ("CertificatePDF-1234"));
      aDR.setDocumentRequestTypeCode (ToopXSDHelper140.createCode ("pdf"));
      aDR.addPreferredDocumentMimeTypeCode (ToopXSDHelper140.createMimeTypeCode (CMimeType.APPLICATION_PDF));
      aRet.addDocumentRequest (aDR);
    }

    {
      final TDEDataProviderType aDP = new TDEDataProviderType ();
      aDP.setDPIdentifier (ToopXSDHelper140.createIdentifier ("schas", null, "atbla"));
      aDP.setDPName (ToopXSDHelper140.createText ("Register1"));
      final TDEAddressType aAddress = new TDEAddressType ();
      aAddress.setCountryCode (ToopXSDHelper140.createCodeWithLOA (sDPCountryCode));
      aDP.setDPLegalAddress (aAddress);
      aRet.addDataProvider (aDP);
    }

    return aRet;
  }

  /**
   * Create a new response with all cloned values from the request. Additionally
   * the "DataRequestIdentifier" and the "DocumentUniversalUniqueIdentifier" is
   * set accordingly.
   *
   * @param aRequest
   *        Source request. May not be <code>null</code>.
   * @return Destination response. Never <code>null</code>.
   */
  @Nonnull
  public static TDETOOPResponseType createResponse (@Nonnull final TDETOOPRequestType aRequest)
  {
    final TDETOOPResponseType aResponse = new TDETOOPResponseType ();
    aRequest.cloneTo (aResponse);
    // Set response specific stuff
    aResponse.setDataRequestIdentifier (aRequest.getDocumentUniversalUniqueIdentifier ().clone ());
    // Create a new UUID
    aResponse.setDocumentUniversalUniqueIdentifier (ToopXSDHelper140.createIdentifierUUID ());
    return aResponse;
  }

  @Nullable
  private static IdentifierType _getClone (@Nullable final IdentifierType x)
  {
    return x == null ? null : x.clone ();
  }

  /**
   * Create a single error object.
   *
   * @param sDPIdentifier
   *        Optional DP identifier. May be <code>null</code>.
   * @param eOrigin
   *        Error origin. May not be <code>null</code>.
   * @param eCategory
   *        Error category. May not be <code>null</code>.
   * @param aErrorCode
   *        Error code. May not be <code>null</code>.
   * @param eSeverity
   *        Error severity. May not be <code>null</code>.
   * @param aMLT
   *        Multilingual text to use. May not be <code>null</code>.
   * @param sTechDetails
   *        Optional technical details. May be <code>null</code>.
   * @return Never <code>null</code>.
   * @since 0.9.2
   */
  @Nonnull
  public static TDEErrorType createError (@Nullable final String sDPIdentifier,
                                          @Nonnull final EToopErrorOrigin eOrigin,
                                          @Nonnull final EToopErrorCategory eCategory,
                                          @Nonnull final IToopErrorCode aErrorCode,
                                          @Nonnull final EToopErrorSeverity eSeverity,
                                          @Nonnull final IMultilingualText aMLT,
                                          @Nullable final String sTechDetails)
  {
    ValueEnforcer.notNull (eOrigin, "Origin");
    ValueEnforcer.notNull (eCategory, "Category");
    ValueEnforcer.notNull (aErrorCode, "ErrorCode");
    ValueEnforcer.notNull (eSeverity, "Severity");
    ValueEnforcer.notNull (aMLT, "MLT");

    final TDEErrorType ret = new TDEErrorType ();
    if (StringHelper.hasText (sDPIdentifier))
      ret.setDataProviderIdentifier (ToopXSDHelper140.createIdentifier (sDPIdentifier));
    ret.setOrigin (ToopXSDHelper140.createCode (eOrigin.getID ()));
    ret.setCategory (ToopXSDHelper140.createCode (eCategory.getID ()));
    ret.setErrorCode (ToopXSDHelper140.createCode (aErrorCode.getID ()));
    ret.setSeverity (ToopXSDHelper140.createCode (eSeverity.getID ()));
    for (final Map.Entry <Locale, String> aEntry : aMLT.texts ().entrySet ())
      ret.addErrorText (ToopXSDHelper140.createText (aEntry.getKey (), aEntry.getValue ()));
    if (StringHelper.hasText (sTechDetails))
      ret.setTechnicalDetails (ToopXSDHelper140.createText (sTechDetails));
    ret.setTimestamp (PDTXMLConverter.getXMLCalendarNow ());
    return ret;
  }
}
