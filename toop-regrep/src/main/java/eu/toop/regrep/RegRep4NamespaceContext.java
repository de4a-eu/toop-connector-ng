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
import javax.xml.XMLConstants;

import com.helger.commons.annotation.Singleton;
import com.helger.xml.CXML;
import com.helger.xml.namespace.MapBasedNamespaceContext;

/**
 * The namespace context to be used as the namespace prefix mapper.
 *
 * @author Philip Helger
 */
@Singleton
public class RegRep4NamespaceContext extends MapBasedNamespaceContext
{
  private static final class SingletonHolder
  {
    static final RegRep4NamespaceContext s_aInstance = new RegRep4NamespaceContext ();
  }

  protected RegRep4NamespaceContext ()
  {
    addMapping (CXML.XML_NS_PREFIX_XSI, XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI);
    addMapping (CRegRep4.DEFAULT_PREFIX_XLINK, CRegRep4.NAMESPACE_URI_XLINK);
    addMapping (CRegRep4.DEFAULT_PREFIX_WS_ADDRESSING, CRegRep4.NAMESPACE_URI_WS_ADDRESSING);
    addMapping (CRegRep4.DEFAULT_PREFIX_RIM, CRegRep4.NAMESPACE_URI_RIM);
    addMapping (CRegRep4.DEFAULT_PREFIX_RS, CRegRep4.NAMESPACE_URI_RS);
    addMapping (CRegRep4.DEFAULT_PREFIX_LCM, CRegRep4.NAMESPACE_URI_LCM);
    addMapping (CRegRep4.DEFAULT_PREFIX_QUERY, CRegRep4.NAMESPACE_URI_QUERY);
    addMapping (CRegRep4.DEFAULT_PREFIX_SPI, CRegRep4.NAMESPACE_URI_SPI);
  }

  @Nonnull
  public static RegRep4NamespaceContext getInstance ()
  {
    return SingletonHolder.s_aInstance;
  }
}
