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
package eu.toop.connector.api.rest;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.jaxb.GenericJAXBMarshaller;

/**
 * JAXB helper for shared TC NG classes
 *
 * @author Philip Helger
 */
@Immutable
public final class TCRestJAXB
{
  public static final ClassPathResource XSD_RES = new ClassPathResource ("/schemas/tc-shared.xsd", TCRestJAXB.class.getClassLoader ());

  private TCRestJAXB ()
  {}

  @Nonnull
  @ReturnsMutableCopy
  public static ICommonsList <ClassPathResource> getAllXSDResources ()
  {
    final ICommonsList <ClassPathResource> ret = new CommonsArrayList <> ();
    ret.add (XSD_RES);
    return ret;
  }

  @Nonnull
  public static GenericJAXBMarshaller <TCOutgoingMessage> outgoingMessage ()
  {
    return new GenericJAXBMarshaller <> (TCOutgoingMessage.class, getAllXSDResources (), new ObjectFactory ()::createOutgoingMessage);
  }
}
