package eu.toop.edm.extractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import eu.toop.edm.EDMRequest;
import eu.toop.edm.EDMResponse;
import eu.toop.edm.xml.cccev.CCCEV;
import eu.toop.regrep.RegRep4Reader;
import eu.toop.regrep.query.QueryRequest;
import eu.toop.regrep.query.QueryResponse;

@Immutable
public final class EDMExtractors
{
  private EDMExtractors ()
  {}

  // For request

  @Nonnull
  public static RegRep4Reader <QueryRequest> queryRequestReader ()
  {
    return RegRep4Reader.queryRequest (CCCEV.XSDS);
  }

  @Deprecated
  public static EDMRequest extractEDMRequest (final File f) throws JAXBException,
                                                            FileNotFoundException,
                                                            XMLStreamException
  {
    return extractEDMRequest (new FileInputStream (f));
  }

  @Deprecated
  public static EDMRequest extractEDMRequest (final Path p) throws JAXBException,
                                                            FileNotFoundException,
                                                            XMLStreamException
  {
    return extractEDMRequest (p.toFile ());
  }

  @Deprecated
  public static EDMRequest extractEDMRequest (final QueryRequest qr) throws JAXBException
  {
    return EDMRequestExtractor.extract (qr);
  }

  @Deprecated
  @Nonnull
  public static EDMRequest extractEDMRequest (final InputStream is) throws JAXBException, XMLStreamException
  {
    final XMLInputFactory factory = XMLInputFactory.newInstance ();
    final XMLEventReader eventReader = factory.createXMLEventReader (is);

    // Move cursor to the start of the document
    while (!eventReader.peek ().isStartElement ())
    {
      eventReader.nextEvent ();
    }

    // Peek at first element to check if it is a QueryRequest
    if ((eventReader.peek ().asStartElement ().getName ().getLocalPart ().equals ("QueryRequest")))
      return extractEDMRequest (RegRep4Reader.queryRequest (CCCEV.XSDS).read (eventReader));

    throw new IllegalArgumentException ("This document does not contain a QueryRequest.");
  }

  // For response

  @Nonnull
  public static RegRep4Reader <QueryResponse> queryResponseReader ()
  {
    return RegRep4Reader.queryResponse (CCCEV.XSDS);
  }

  @Deprecated
  public static EDMResponse extractEDMResponse (final File f) throws JAXBException,
                                                              FileNotFoundException,
                                                              XMLStreamException
  {
    return extractEDMResponse (new FileInputStream (f));
  }

  @Deprecated
  public static EDMResponse extractEDMResponse (final Path p) throws JAXBException,
                                                              FileNotFoundException,
                                                              XMLStreamException
  {
    return extractEDMResponse (p.toFile ());
  }

  @Deprecated
  public static EDMResponse extractEDMResponse (final InputStream is) throws JAXBException, XMLStreamException
  {
    final XMLInputFactory factory = XMLInputFactory.newInstance ();
    final XMLEventReader eventReader = factory.createXMLEventReader (is);

    // Move cursor to the start of the document
    while (!eventReader.peek ().isStartElement ())
    {
      eventReader.nextEvent ();
    }

    // Peek at first element to check if it is a QueryResponse
    if ((eventReader.peek ().asStartElement ().getName ().getLocalPart ().equals ("QueryResponse")))
      return extractEDMResponse (RegRep4Reader.queryResponse (CCCEV.XSDS).read (eventReader));

    throw new IllegalArgumentException ("This document does not contain a QueryResponse.");
  }

  @Deprecated
  public static EDMResponse extractEDMResponse (final QueryResponse queryResponse) throws JAXBException
  {
    return EDMResponseExtractor.extract (queryResponse);
  }
}
