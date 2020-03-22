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
package eu.toop.regrep;

import javax.annotation.Nonnull;

import com.helger.jaxb.builder.JAXBReaderBuilder;

import eu.toop.regrep.lcm.RemoveObjectsRequest;
import eu.toop.regrep.lcm.SubmitObjectsRequest;
import eu.toop.regrep.lcm.UpdateObjectsRequest;
import eu.toop.regrep.query.QueryExceptionType;
import eu.toop.regrep.query.QueryRequest;
import eu.toop.regrep.query.QueryResponse;
import eu.toop.regrep.spi.CatalogObjectsRequest;
import eu.toop.regrep.spi.CatalogObjectsResponse;
import eu.toop.regrep.spi.FilterObjectsRequest;
import eu.toop.regrep.spi.FilterObjectsResponse;
import eu.toop.regrep.spi.ValidateObjectsRequest;
import eu.toop.regrep.spi.ValidateObjectsResponse;

/**
 * A class to read RegRep request and response documents in a structured way.
 * Use the static factory methods to create the correct instances.
 *
 * @author Philip Helger
 * @param <JAXBTYPE>
 *        The implementation type.
 */
public class RegRep4Reader <JAXBTYPE> extends JAXBReaderBuilder <JAXBTYPE, RegRep4Reader <JAXBTYPE>>
{
  /**
   * Constructor with an arbitrary document type.
   *
   * @param eDocType
   *        Document type to be used. May not be <code>null</code>.
   * @param aImplClass
   *        Implementation class to use. May not be <code>null</code>.
   */
  public RegRep4Reader (@Nonnull final ERegRep4XMLDocumentType eDocType, @Nonnull final Class <JAXBTYPE> aImplClass)
  {
    super (eDocType, aImplClass);
  }

  /**
   * Create a reader builder for {@link SubmitObjectsRequest}.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static RegRep4Reader <SubmitObjectsRequest> submitObjectsRequest ()
  {
    return new RegRep4Reader <> (ERegRep4XMLDocumentType.SUBMIT_OBJECTS_REQUEST, SubmitObjectsRequest.class);
  }

  /**
   * Create a reader builder for {@link UpdateObjectsRequest}.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static RegRep4Reader <UpdateObjectsRequest> updateObjectsRequest ()
  {
    return new RegRep4Reader <> (ERegRep4XMLDocumentType.UPDATE_OBJECTS_REQUEST, UpdateObjectsRequest.class);
  }

  /**
   * Create a reader builder for {@link RemoveObjectsRequest}.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static RegRep4Reader <RemoveObjectsRequest> removeObjectsRequest ()
  {
    return new RegRep4Reader <> (ERegRep4XMLDocumentType.REMOVE_OBJECTS_REQUEST, RemoveObjectsRequest.class);
  }

  /**
   * Create a reader builder for {@link QueryRequest}.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static RegRep4Reader <QueryRequest> queryRequest ()
  {
    return new RegRep4Reader <> (ERegRep4XMLDocumentType.QUERY_REQUEST, QueryRequest.class);
  }

  /**
   * Create a reader builder for {@link QueryResponse}.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static RegRep4Reader <QueryResponse> queryResponse ()
  {
    return new RegRep4Reader <> (ERegRep4XMLDocumentType.QUERY_RESPONSE, QueryResponse.class);
  }

  /**
   * Create a reader builder for {@link QueryExceptionType}.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static RegRep4Reader <QueryExceptionType> queryException ()
  {
    return new RegRep4Reader <> (ERegRep4XMLDocumentType.QUERY_EXCEPTION, QueryExceptionType.class);
  }

  /**
   * Create a reader builder for {@link ValidateObjectsRequest}.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static RegRep4Reader <ValidateObjectsRequest> validateObjectsRequest ()
  {
    return new RegRep4Reader <> (ERegRep4XMLDocumentType.VALIDATE_OBJECTS_REQUEST, ValidateObjectsRequest.class);
  }

  /**
   * Create a reader builder for {@link ValidateObjectsResponse}.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static RegRep4Reader <ValidateObjectsResponse> validateObjectsResponse ()
  {
    return new RegRep4Reader <> (ERegRep4XMLDocumentType.VALIDATE_OBJECTS_RESPONSE, ValidateObjectsResponse.class);
  }

  /**
   * Create a reader builder for {@link CatalogObjectsRequest}.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static RegRep4Reader <CatalogObjectsRequest> catalogObjectsRequest ()
  {
    return new RegRep4Reader <> (ERegRep4XMLDocumentType.CATALOG_OBJECTS_REQUEST, CatalogObjectsRequest.class);
  }

  /**
   * Create a reader builder for {@link CatalogObjectsResponse}.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static RegRep4Reader <CatalogObjectsResponse> catalogObjectsResponse ()
  {
    return new RegRep4Reader <> (ERegRep4XMLDocumentType.CATALOG_OBJECTS_RESPONSE, CatalogObjectsResponse.class);
  }

  /**
   * Create a reader builder for {@link FilterObjectsRequest}.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static RegRep4Reader <FilterObjectsRequest> filterObjectsRequest ()
  {
    return new RegRep4Reader <> (ERegRep4XMLDocumentType.FILTER_OBJECTS_REQUEST, FilterObjectsRequest.class);
  }

  /**
   * Create a reader builder for {@link FilterObjectsResponse}.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static RegRep4Reader <FilterObjectsResponse> filterObjectsResponse ()
  {
    return new RegRep4Reader <> (ERegRep4XMLDocumentType.FILTER_OBJECTS_RESPONSE, FilterObjectsResponse.class);
  }
}
