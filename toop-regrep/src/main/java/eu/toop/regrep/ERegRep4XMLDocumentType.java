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
import javax.xml.namespace.QName;
import javax.xml.validation.Schema;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.string.StringHelper;
import com.helger.jaxb.builder.IJAXBDocumentType;
import com.helger.jaxb.builder.JAXBDocumentType;

/**
 * Enumeration with all available RegRep4 XML document types.
 *
 * @author Philip Helger
 */
public enum ERegRep4XMLDocumentType implements IJAXBDocumentType
{
  REGISTRY_EXCEPTION (eu.toop.regrep.rs.RegistryExceptionType.class, CRegRep4.getAllXSDsQuery ()),

  SUBMIT_OBJECTS_REQUEST (eu.toop.regrep.lcm.SubmitObjectsRequest.class, CRegRep4.getAllXSDsLCM ()),
  UPDATE_OBJECTS_REQUEST (eu.toop.regrep.lcm.UpdateObjectsRequest.class, CRegRep4.getAllXSDsLCM ()),
  REMOVE_OBJECTS_REQUEST (eu.toop.regrep.lcm.RemoveObjectsRequest.class, CRegRep4.getAllXSDsLCM ()),

  QUERY_REQUEST (eu.toop.regrep.query.QueryRequest.class, CRegRep4.getAllXSDsQuery ()),
  QUERY_RESPONSE (eu.toop.regrep.query.QueryResponse.class, CRegRep4.getAllXSDsQuery ()),

  VALIDATE_OBJECTS_REQUEST (eu.toop.regrep.spi.ValidateObjectsRequest.class, CRegRep4.getAllXSDsSPI ()),
  VALIDATE_OBJECTS_RESPONSE (eu.toop.regrep.spi.ValidateObjectsResponse.class, CRegRep4.getAllXSDsSPI ()),
  CATALOG_OBJECTS_REQUEST (eu.toop.regrep.spi.CatalogObjectsRequest.class, CRegRep4.getAllXSDsSPI ()),
  CATALOG_OBJECTS_RESPONSE (eu.toop.regrep.spi.CatalogObjectsResponse.class, CRegRep4.getAllXSDsSPI ()),
  FILTER_OBJECTS_REQUEST (eu.toop.regrep.spi.FilterObjectsRequest.class, CRegRep4.getAllXSDsSPI ()),
  FILTER_OBJECTS_RESPONSE (eu.toop.regrep.spi.FilterObjectsResponse.class, CRegRep4.getAllXSDsSPI ());

  private final JAXBDocumentType m_aDocType;

  private ERegRep4XMLDocumentType (@Nonnull final Class <?> aClass,
                                   @Nonnull final ICommonsList <ClassPathResource> aXSDs)
  {
    m_aDocType = new JAXBDocumentType (aClass, aXSDs, x -> StringHelper.trimEnd (x, "Type"));
  }

  private ERegRep4XMLDocumentType (@Nonnull final Class <?> aClass,
                                   @Nonnull final ICommonsList <ClassPathResource> aXSDs,
                                   @Nonnull final QName aQName)
  {
    m_aDocType = new JAXBDocumentType (aClass, aXSDs, aQName.getNamespaceURI (), aQName.getLocalPart ());
  }

  @Nonnull
  public Class <?> getImplementationClass ()
  {
    return m_aDocType.getImplementationClass ();
  }

  @Nonnull
  @Nonempty
  @ReturnsMutableCopy
  public ICommonsList <ClassPathResource> getAllXSDResources ()
  {
    return m_aDocType.getAllXSDResources ();
  }

  @Nonnull
  public String getNamespaceURI ()
  {
    return m_aDocType.getNamespaceURI ();
  }

  @Nonnull
  @Nonempty
  public String getLocalName ()
  {
    return m_aDocType.getLocalName ();
  }

  @Nonnull
  public Schema getSchema ()
  {
    return m_aDocType.getSchema ();
  }
}
