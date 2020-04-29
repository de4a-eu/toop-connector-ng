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
package eu.toop.edm.xml.cccev;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Singleton;
import com.helger.xml.namespace.MapBasedNamespaceContext;

import eu.toop.edm.xml.cagv.CAGVNamespaceContext;

/**
 * XML Namespace context for CCCEV
 *
 * @author Philip Helger
 */
@Singleton
public class CCCEVNamespaceContext extends MapBasedNamespaceContext
{
  private static final class SingletonHolder
  {
    static final CCCEVNamespaceContext s_aInstance = new CCCEVNamespaceContext ();
  }

  protected CCCEVNamespaceContext ()
  {
    addMappings (CAGVNamespaceContext.getInstance ());
    addMapping ("cccev", "https://semic.org/sa/cv/cccev-2.0.0#");
    addMapping ("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
    addMapping ("spdx", "spdx:xsd::1.0");
    addMapping ("odrl", "http://www.w3.org/ns/odrl/2/");
    addMapping ("r5r", "http://data.europa.eu/r5r/");
    addMapping ("foaf", "http://xmlns.com/foaf/0.1/");
    addMapping ("adms", "http://www.w3.org/ns/adms#");
    addMapping ("dcterms", "http://purl.org/dc/terms/");
  }

  @Nonnull
  public static CCCEVNamespaceContext getInstance ()
  {
    return SingletonHolder.s_aInstance;
  }
}
