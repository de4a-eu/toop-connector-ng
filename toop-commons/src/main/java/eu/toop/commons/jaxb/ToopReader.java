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
package eu.toop.commons.jaxb;

import javax.annotation.Nonnull;

import com.helger.jaxb.builder.JAXBReaderBuilder;

/**
 * A class to read TOOP request and response documents in a structured way. Use
 * the static factory methods to create the correct instances.
 *
 * @author Philip Helger
 * @param <JAXBTYPE>
 *        The implementation type.
 */
public class ToopReader <JAXBTYPE> extends JAXBReaderBuilder <JAXBTYPE, ToopReader <JAXBTYPE>>
{
  /**
   * Constructor with an arbitrary document type.
   *
   * @param aDocType
   *        Document type to be used. May not be <code>null</code>.
   * @param aImplClass
   *        Implementation class to use. May not be <code>null</code>.
   */
  public ToopReader (@Nonnull final EToopXMLDocumentType aDocType, @Nonnull final Class <JAXBTYPE> aImplClass)
  {
    super (aDocType, aImplClass);
  }

  /**
   * Create a reader builder for
   * {@link eu.toop.commons.dataexchange.v140.TDETOOPRequestType}.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static ToopReader <eu.toop.commons.dataexchange.v140.TDETOOPRequestType> request140 ()
  {
    return new ToopReader <> (EToopXMLDocumentType.REQUEST_140,
                              eu.toop.commons.dataexchange.v140.TDETOOPRequestType.class);
  }

  /**
   * Create a reader builder for
   * {@link eu.toop.commons.dataexchange.v140.TDETOOPResponseType}.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static ToopReader <eu.toop.commons.dataexchange.v140.TDETOOPResponseType> response140 ()
  {
    return new ToopReader <> (EToopXMLDocumentType.RESPONSE_140,
                              eu.toop.commons.dataexchange.v140.TDETOOPResponseType.class);
  }
}
