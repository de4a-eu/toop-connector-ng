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
package eu.toop.edm.xml.cv;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Singleton;
import com.helger.ubl21.UBL21NamespaceContext;
import com.helger.xml.namespace.MapBasedNamespaceContext;

/**
 * XML Namespace context for CV
 *
 * @author Philip Helger
 */
@Singleton
public class CCVNamespaceContext extends MapBasedNamespaceContext
{
  private static final class SingletonHolder
  {
    static final CCVNamespaceContext s_aInstance = new CCVNamespaceContext ();
  }

  protected CCVNamespaceContext ()
  {
    // Include UBL 2.1
    addMappings (UBL21NamespaceContext.getInstance ());
    addMapping ("cva", "http://www.w3.org/ns/corevocabulary/AggregateComponents");
    addMapping ("cvb", "http://www.w3.org/ns/corevocabulary/BasicComponents");
  }

  @Nonnull
  public static CCVNamespaceContext getInstance ()
  {
    return SingletonHolder.s_aInstance;
  }
}
