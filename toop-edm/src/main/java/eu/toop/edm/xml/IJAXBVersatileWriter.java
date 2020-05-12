package eu.toop.edm.xml;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.file.Path;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;
import javax.xml.transform.Result;

import org.w3c.dom.Document;

import com.helger.commons.io.resource.IWritableResource;
import com.helger.commons.io.stream.NonBlockingByteArrayInputStream;
import com.helger.commons.state.ESuccess;
import com.helger.jaxb.IJAXBWriter;
import com.helger.jaxb.IJAXBWriter.IJAXBMarshaller;
import com.helger.xml.microdom.IMicroDocument;
import com.helger.xml.microdom.IMicroElement;
import com.helger.xml.serialize.write.SafeXMLStreamWriter;

public interface IJAXBVersatileWriter <T> extends IVersatileWriter <T>
{
  @Nonnull
  T getObjectToWrite ();

  @Nonnull
  IJAXBWriter <T> getWriter ();

  /**
   * Write the passed object to a {@link File}.
   *
   * @param aResultFile
   *        The result file to be written to. May not be <code>null</code>.
   * @return {@link ESuccess}
   */
  @Nonnull
  default ESuccess write (@Nonnull final File aResultFile)
  {
    return getWriter ().write (getObjectToWrite (), aResultFile);
  }

  /**
   * Write the passed object to a {@link Path}.
   *
   * @param aResultPath
   *        The result path to be written to. May not be <code>null</code>.
   * @return {@link ESuccess}
   */
  @Nonnull
  default ESuccess write (@Nonnull final Path aResultPath)
  {
    return getWriter ().write (getObjectToWrite (), aResultPath);
  }

  /**
   * Write the passed object to an {@link OutputStream}.
   *
   * @param aOS
   *        The output stream to write to. Will always be closed. May not be
   *        <code>null</code>.
   * @return {@link ESuccess}
   */
  @Nonnull
  default ESuccess write (@Nonnull @WillClose final OutputStream aOS)
  {
    return getWriter ().write (getObjectToWrite (), aOS);
  }

  /**
   * Write the passed object to a {@link Writer}.
   *
   * @param aWriter
   *        The writer to write to. Will always be closed. May not be
   *        <code>null</code>.
   * @return {@link ESuccess}
   */
  @Nonnull
  default ESuccess write (@Nonnull @WillClose final Writer aWriter)
  {
    return getWriter ().write (getObjectToWrite (), aWriter);
  }

  /**
   * Write the passed object to a {@link ByteBuffer}.
   *
   * @param aBuffer
   *        The byte buffer to write to. If the buffer is too small, it is
   *        automatically extended. May not be <code>null</code>.
   * @return {@link ESuccess}
   * @throws BufferOverflowException
   *         If the ByteBuffer is too small
   */
  @Nonnull
  default ESuccess write (@Nonnull final ByteBuffer aBuffer)
  {
    return getWriter ().write (getObjectToWrite (), aBuffer);
  }

  /**
   * Write the passed object to an {@link IWritableResource}.
   *
   * @param aResource
   *        The result resource to be written to. May not be <code>null</code>.
   * @return {@link ESuccess}
   */
  @Nonnull
  default ESuccess write (@Nonnull final IWritableResource aResource)
  {
    return getWriter ().write (getObjectToWrite (), aResource);
  }

  /**
   * Convert the passed object to XML.
   *
   * @param aMarshallerFunc
   *        The marshalling function. May not be <code>null</code>.
   * @return {@link ESuccess}
   */
  @Nonnull
  default ESuccess write (@Nonnull final IJAXBMarshaller <T> aMarshallerFunc)
  {
    return getWriter ().write (getObjectToWrite (), aMarshallerFunc);
  }

  /**
   * Convert the passed object to XML. This method is potentially dangerous,
   * when using StreamResult because it may create invalid XML. Only when using
   * the {@link SafeXMLStreamWriter} it is ensured that only valid XML is
   * created!
   *
   * @param aResult
   *        The result object holder. May not be <code>null</code>. Usually
   *        SAXResult, DOMResult and StreamResult are supported.
   * @return {@link ESuccess}
   */
  @Nonnull
  default ESuccess write (@Nonnull final Result aResult)
  {
    return getWriter ().write (getObjectToWrite (), aResult);
  }

  /**
   * Convert the passed object to XML.
   *
   * @param aHandler
   *        XML will be sent to this handler as SAX2 events. May not be
   *        <code>null</code>.
   * @return {@link ESuccess}
   */
  @Nonnull
  default ESuccess write (@Nonnull final org.xml.sax.ContentHandler aHandler)
  {
    return getWriter ().write (getObjectToWrite (), aHandler);
  }

  /**
   * Convert the passed object to XML.
   *
   * @param aWriter
   *        XML will be sent to this writer. May not be <code>null</code>.
   * @return {@link ESuccess}
   */
  @Nonnull
  default ESuccess write (@Nonnull @WillClose final javax.xml.stream.XMLStreamWriter aWriter)
  {
    return getWriter ().write (getObjectToWrite (), aWriter);
  }

  /**
   * Convert the passed object to a new DOM document (write).
   *
   * @return <code>null</code> if converting the document failed.
   */
  @Nullable
  default Document getAsDocument ()
  {
    return getWriter ().getAsDocument (getObjectToWrite ());
  }

  /**
   * Convert the passed object to a new micro document (write).
   *
   * @return <code>null</code> if converting the document failed.
   */
  @Nullable
  default IMicroDocument getAsMicroDocument ()
  {
    return getWriter ().getAsMicroDocument (getObjectToWrite ());
  }

  /**
   * Convert the passed object to a new micro document and return only the root
   * element (write).
   *
   * @return <code>null</code> if converting the document failed.
   */
  @Nullable
  default IMicroElement getAsMicroElement ()
  {
    return getWriter ().getAsMicroElement (getObjectToWrite ());
  }

  /**
   * Utility method to directly convert the passed domain object to an XML
   * string (write).
   *
   * @return <code>null</code> if the passed domain object could not be
   *         converted because of validation errors.
   */
  @Nullable
  default String getAsString ()
  {
    return getWriter ().getAsString (getObjectToWrite ());
  }

  /**
   * Write the passed object to a {@link ByteBuffer} and return it (write).
   *
   * @return <code>null</code> if the passed domain object could not be
   *         converted because of validation errors.
   */
  @Nullable
  default ByteBuffer getAsByteBuffer ()
  {
    return getWriter ().getAsByteBuffer (getObjectToWrite ());
  }

  /**
   * Write the passed object to a byte array and return the created byte array
   * (write).
   *
   * @return <code>null</code> if the passed domain object could not be
   *         converted because of validation errors.
   */
  @Nullable
  default byte [] getAsBytes ()
  {
    return getWriter ().getAsBytes (getObjectToWrite ());
  }

  /**
   * Write the passed object to a byte array and return the input stream on that
   * array.
   *
   * @return <code>null</code> if the passed domain object could not be
   *         converted because of validation errors.
   */
  @Nullable
  default NonBlockingByteArrayInputStream getAsInputStream ()
  {
    return getWriter ().getAsInputStream (getObjectToWrite ());
  }
}
