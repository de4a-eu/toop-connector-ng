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
package eu.toop.edm.xml.cagv;

import javax.annotation.Nullable;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.jaxb.GenericJAXBMarshaller;
import com.helger.jaxb.JAXBContextCache;

import eu.toop.edm.jaxb.cv.agent.AgentType;
import eu.toop.edm.jaxb.cv.agent.ObjectFactory;

public class AgentMarshaller extends GenericJAXBMarshaller <AgentType>
{
  public AgentMarshaller ()
  {
    super (AgentType.class, CCAGV.XSDS, x -> new ObjectFactory ().createAgent (x));
    setNamespaceContext (CAGVNamespaceContext.getInstance ());
  }

  @Override
  protected JAXBContext getJAXBContext (@Nullable final ClassLoader aClassLoader) throws JAXBException
  {
    final Class <?> [] aClasses = new Class <?> [] { eu.toop.edm.jaxb.cv.agent.ObjectFactory.class,
                                                     eu.toop.edm.jaxb.cv.cbc.ObjectFactory.class };
    if (isUseContextCache ())
      return JAXBContextCache.getInstance ().getFromCache (new CommonsArrayList <> (aClasses));
    return JAXBContext.newInstance (aClasses);
  }
}
