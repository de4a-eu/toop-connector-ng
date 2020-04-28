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
package eu.toop.edm.xml.cpsv;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Singleton;
import com.helger.xml.namespace.MapBasedNamespaceContext;

import eu.toop.edm.xml.cv.CCVNamespaceContext;

/**
 * XML Namespace context for CPSV
 *
 * @author Philip Helger
 */
@Singleton
public class CPSVNamespaceContext extends MapBasedNamespaceContext
{
  private static final class SingletonHolder
  {
    static final CPSVNamespaceContext s_aInstance = new CPSVNamespaceContext ();
  }

  protected CPSVNamespaceContext ()
  {
    // Based on CV
    addMappings (CCVNamespaceContext.getInstance ());
    addMapping ("cpsvh", "http://data.europa.eu/m8g/CPSVAP2.0.Helper");
    addMapping ("cpsv", "http://data.europa.eu/m8g/CPSVAP2.0");
  }

  @Nonnull
  public static CPSVNamespaceContext getInstance ()
  {
    return SingletonHolder.s_aInstance;
  }
}
