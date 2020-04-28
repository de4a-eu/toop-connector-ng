/**
 * Copyright (C) 2018-2020 toop.eu
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.toop.edm.xml.dcatap;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Singleton;
import com.helger.xml.namespace.MapBasedNamespaceContext;

/**
 * XML Namespace context for DCAT
 *
 * @author yerlibilgin
 */
@Singleton
public class DCATNamespaceContext extends MapBasedNamespaceContext {
  private static final class SingletonHolder {
    static final DCATNamespaceContext s_aInstance = new DCATNamespaceContext();
  }

  /**
   * eu.toop.edm.jaxb.dcatap.ObjectFactory.class,
   *         eu.toop.edm.jaxb.foaf.ObjectFactory.class,
   *         eu.toop.edm.jaxb.cv.cbc.ObjectFactory.class,
   *         eu.toop.edm.jaxb.w3.adms.ObjectFactory.class,
   *         eu.toop.edm.jaxb.cv.agent.ObjectFactory.class,
   *         eu.toop.edm.jaxb.w3.locn.ObjectFactory.class
   */
  protected DCATNamespaceContext() {
    addMapping("dcat", "http://data.europa.eu/r5r/");
    addMapping("dct", "http://purl.org/dc/terms/");
    addMapping("cagv", "https://semic.org/sa/cv/cagv/agent-2.0.0#");
    addMapping("cbc", "https://semic.org/sa/cv/common/cbc-2.0.0#");
    addMapping("cac", "https://semic.org/sa/cv/common/cac-2.0.0#");
    addMapping("locn", "http://www.w3.org/ns/locn#");
    addMapping("skos", "http://www.w3.org/2004/02/skos/core#");
  }

  @Nonnull
  public static DCATNamespaceContext getInstance() {
    return SingletonHolder.s_aInstance;
  }
}
