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

import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.jaxb.builder.IJAXBDocumentType;
import com.helger.jaxb.builder.JAXBDocumentType;
import com.helger.jaxb.builder.JAXBWriterBuilder;

import eu.toop.regrep.lcm.RemoveObjectsRequest;
import eu.toop.regrep.lcm.SubmitObjectsRequest;
import eu.toop.regrep.lcm.UpdateObjectsRequest;
import eu.toop.regrep.query.QueryRequest;
import eu.toop.regrep.query.QueryResponse;
import eu.toop.regrep.rs.RegistryExceptionType;
import eu.toop.regrep.spi.CatalogObjectsRequest;
import eu.toop.regrep.spi.CatalogObjectsResponse;
import eu.toop.regrep.spi.FilterObjectsRequest;
import eu.toop.regrep.spi.FilterObjectsResponse;
import eu.toop.regrep.spi.ValidateObjectsRequest;
import eu.toop.regrep.spi.ValidateObjectsResponse;

/**
 * A class to write RegRep request and response documents in a structured way.
 * Use the static factory methods to create the correct instances.
 *
 * @author Philip Helger
 * @param <JAXBTYPE>
 *        The implementation type.
 */
public class RegRep4Writer <JAXBTYPE> extends JAXBWriterBuilder <JAXBTYPE, RegRep4Writer <JAXBTYPE>>
{
  /**
   * Constructor with an arbitrary document type.
   *
   * @param eDocType
   *        Document type to be used. May not be <code>null</code>.
   */
  public RegRep4Writer (@Nonnull final ERegRep4XMLDocumentType eDocType)
  {
    super (eDocType);
    setNamespaceContext (RegRep4NamespaceContext.getInstance ());
  }

  private RegRep4Writer (@Nonnull final IJAXBDocumentType eDocType)
  {
    super (eDocType);
    setNamespaceContext (RegRep4NamespaceContext.getInstance ());
  }

  /**
   * Create a reader builder for {@link SubmitObjectsRequest}.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static RegRep4Writer <SubmitObjectsRequest> submitObjectsRequest ()
  {
    return new RegRep4Writer <> (ERegRep4XMLDocumentType.SUBMIT_OBJECTS_REQUEST);
  }

  /**
   * Create a reader builder for {@link UpdateObjectsRequest}.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static RegRep4Writer <UpdateObjectsRequest> updateObjectsRequest ()
  {
    return new RegRep4Writer <> (ERegRep4XMLDocumentType.UPDATE_OBJECTS_REQUEST);
  }

  /**
   * Create a reader builder for {@link RemoveObjectsRequest}.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static RegRep4Writer <RemoveObjectsRequest> removeObjectsRequest ()
  {
    return new RegRep4Writer <> (ERegRep4XMLDocumentType.REMOVE_OBJECTS_REQUEST);
  }

  /**
   * Create a reader builder for {@link QueryRequest}.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static RegRep4Writer <QueryRequest> queryRequest ()
  {
    return new RegRep4Writer <> (ERegRep4XMLDocumentType.QUERY_REQUEST);
  }

  /**
   * Create a reader builder for {@link QueryRequest}.
   *
   * @param aAdditionalXSDs
   *        Additional XSDs
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static RegRep4Writer <QueryRequest> queryRequest (@Nonnull final ClassPathResource... aAdditionalXSDs)
  {
    final ICommonsList <ClassPathResource> aXSDs = CRegRep4.getAllXSDsQuery ().getClone ();
    aXSDs.addAll (aAdditionalXSDs);
    return new RegRep4Writer <> (new JAXBDocumentType (eu.toop.regrep.query.QueryRequest.class, aXSDs, null));
  }

  /**
   * Create a reader builder for {@link QueryResponse}.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static RegRep4Writer <QueryResponse> queryResponse ()
  {
    return new RegRep4Writer <> (ERegRep4XMLDocumentType.QUERY_RESPONSE);
  }

  /**
   * Create a reader builder for {@link QueryResponse}.
   *
   * @param aAdditionalXSDs
   *        Additional XSDs
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static RegRep4Writer <QueryResponse> queryResponse (@Nonnull final ClassPathResource... aAdditionalXSDs)
  {
    final ICommonsList <ClassPathResource> aXSDs = CRegRep4.getAllXSDsQuery ().getClone ();
    aXSDs.addAll (aAdditionalXSDs);
    return new RegRep4Writer <> (new JAXBDocumentType (eu.toop.regrep.query.QueryResponse.class, aXSDs, null));
  }

  /**
   * Create a reader builder for {@link RegistryExceptionType}.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static RegRep4Writer <RegistryExceptionType> registryException ()
  {
    return new RegRep4Writer <> (ERegRep4XMLDocumentType.REGISTRY_EXCEPTION);
  }

  /**
   * Create a reader builder for {@link ValidateObjectsRequest}.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static RegRep4Writer <ValidateObjectsRequest> validateObjectsRequest ()
  {
    return new RegRep4Writer <> (ERegRep4XMLDocumentType.VALIDATE_OBJECTS_REQUEST);
  }

  /**
   * Create a reader builder for {@link ValidateObjectsResponse}.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static RegRep4Writer <ValidateObjectsResponse> validateObjectsResponse ()
  {
    return new RegRep4Writer <> (ERegRep4XMLDocumentType.VALIDATE_OBJECTS_RESPONSE);
  }

  /**
   * Create a reader builder for {@link CatalogObjectsRequest}.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static RegRep4Writer <CatalogObjectsRequest> catalogObjectsRequest ()
  {
    return new RegRep4Writer <> (ERegRep4XMLDocumentType.CATALOG_OBJECTS_REQUEST);
  }

  /**
   * Create a reader builder for {@link CatalogObjectsResponse}.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static RegRep4Writer <CatalogObjectsResponse> catalogObjectsResponse ()
  {
    return new RegRep4Writer <> (ERegRep4XMLDocumentType.CATALOG_OBJECTS_RESPONSE);
  }

  /**
   * Create a reader builder for {@link FilterObjectsRequest}.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static RegRep4Writer <FilterObjectsRequest> filterObjectsRequest ()
  {
    return new RegRep4Writer <> (ERegRep4XMLDocumentType.FILTER_OBJECTS_REQUEST);
  }

  /**
   * Create a reader builder for {@link FilterObjectsResponse}.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static RegRep4Writer <FilterObjectsResponse> filterObjectsResponse ()
  {
    return new RegRep4Writer <> (ERegRep4XMLDocumentType.FILTER_OBJECTS_RESPONSE);
  }
}
