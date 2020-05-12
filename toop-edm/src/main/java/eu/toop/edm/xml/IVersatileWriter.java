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
import com.helger.jaxb.IJAXBWriter.IJAXBMarshaller;
import com.helger.xml.microdom.IMicroDocument;
import com.helger.xml.microdom.IMicroElement;
import com.helger.xml.serialize.write.SafeXMLStreamWriter;

public interface IVersatileWriter <T>
{
  /**
   * Write the passed object to a {@link File}.
   *
   * @param aResultFile
   *        The result file to be written to. May not be <code>null</code>.
   * @return {@link ESuccess}
   */
  @Nonnull
  ESuccess write (@Nonnull File aResultFile);

  /**
   * Write the passed object to a {@link Path}.
   *
   * @param aResultPath
   *        The result path to be written to. May not be <code>null</code>.
   * @return {@link ESuccess}
   */
  @Nonnull
  ESuccess write (@Nonnull Path aResultPath);

  /**
   * Write the passed object to an {@link OutputStream}.
   *
   * @param aOS
   *        The output stream to write to. Will always be closed. May not be
   *        <code>null</code>.
   * @return {@link ESuccess}
   */
  @Nonnull
  ESuccess write (@Nonnull @WillClose OutputStream aOS);

  /**
   * Write the passed object to a {@link Writer}.
   *
   * @param aWriter
   *        The writer to write to. Will always be closed. May not be
   *        <code>null</code>.
   * @return {@link ESuccess}
   */
  @Nonnull
  ESuccess write (@Nonnull @WillClose Writer aWriter);

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
  ESuccess write (@Nonnull ByteBuffer aBuffer);

  /**
   * Write the passed object to an {@link IWritableResource}.
   *
   * @param aResource
   *        The result resource to be written to. May not be <code>null</code>.
   * @return {@link ESuccess}
   */
  @Nonnull
  ESuccess write (@Nonnull IWritableResource aResource);

  /**
   * Convert the passed object to XML.
   *
   * @param aMarshallerFunc
   *        The marshalling function. May not be <code>null</code>.
   * @return {@link ESuccess}
   */
  @Nonnull
  ESuccess write (@Nonnull IJAXBMarshaller <T> aMarshallerFunc);

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
  ESuccess write (@Nonnull Result aResult);

  /**
   * Convert the passed object to XML.
   *
   * @param aHandler
   *        XML will be sent to this handler as SAX2 events. May not be
   *        <code>null</code>.
   * @return {@link ESuccess}
   */
  @Nonnull
  ESuccess write (@Nonnull org.xml.sax.ContentHandler aHandler);

  /**
   * Convert the passed object to XML.
   *
   * @param aWriter
   *        XML will be sent to this writer. May not be <code>null</code>.
   * @return {@link ESuccess}
   */
  @Nonnull
  ESuccess write (@Nonnull @WillClose javax.xml.stream.XMLStreamWriter aWriter);

  /**
   * Convert the passed object to a new DOM document (write).
   *
   * @return <code>null</code> if converting the document failed.
   */
  @Nullable
  Document getAsDocument ();

  /**
   * Convert the passed object to a new micro document (write).
   *
   * @return <code>null</code> if converting the document failed.
   */
  @Nullable
  IMicroDocument getAsMicroDocument ();

  /**
   * Convert the passed object to a new micro document and return only the root
   * element (write).
   *
   * @return <code>null</code> if converting the document failed.
   */
  @Nullable
  IMicroElement getAsMicroElement ();

  /**
   * Utility method to directly convert the passed domain object to an XML
   * string (write).
   *
   * @return <code>null</code> if the passed domain object could not be
   *         converted because of validation errors.
   */
  @Nullable
  String getAsString ();

  /**
   * Write the passed object to a {@link ByteBuffer} and return it (write).
   *
   * @return <code>null</code> if the passed domain object could not be
   *         converted because of validation errors.
   */
  @Nullable
  ByteBuffer getAsByteBuffer ();

  /**
   * Write the passed object to a byte array and return the created byte array
   * (write).
   *
   * @return <code>null</code> if the passed domain object could not be
   *         converted because of validation errors.
   */
  @Nullable
  byte [] getAsBytes ();

  /**
   * Write the passed object to a byte array and return the input stream on that
   * array.
   *
   * @return <code>null</code> if the passed domain object could not be
   *         converted because of validation errors.
   */
  @Nullable
  NonBlockingByteArrayInputStream getAsInputStream ();
}
