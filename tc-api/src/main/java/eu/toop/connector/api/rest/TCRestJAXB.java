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
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.jaxb.GenericJAXBMarshaller;
import com.helger.peppolid.IIdentifier;

/**
 * JAXB helper for TC NG REST classes
 *
 * @author Philip Helger
 */
@Immutable
public final class TCRestJAXB
{
  public static final ClassPathResource XSD_RES = new ClassPathResource ("/schemas/tc-rest.xsd", TCRestJAXB.class.getClassLoader ());
  public static final String NS_URI = "urn:eu.toop/toop-connector-ng/2020/05/";

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
    final GenericJAXBMarshaller <TCOutgoingMessage> ret = new GenericJAXBMarshaller <> (TCOutgoingMessage.class,
                                                                                        getAllXSDResources (),
                                                                                        new ObjectFactory ()::createOutgoingMessage);
    ret.setFormattedOutput (true);
    ret.setNamespaceContext (TCRestNamespaceContext.getInstance ());
    return ret;
  }

  @Nonnull
  public static GenericJAXBMarshaller <TCIncomingMessage> incomingMessage ()
  {
    final GenericJAXBMarshaller <TCIncomingMessage> ret = new GenericJAXBMarshaller <> (TCIncomingMessage.class,
                                                                                        getAllXSDResources (),
                                                                                        new ObjectFactory ()::createIncomingMessage);
    ret.setFormattedOutput (true);
    ret.setNamespaceContext (TCRestNamespaceContext.getInstance ());
    return ret;
  }

  @Nonnull
  public static TCIdentifierType createTCID (@Nonnull final IIdentifier aID)
  {
    ValueEnforcer.notNull(aID, "aID");
    return createTCID (aID.getScheme (), aID.getValue ());
  }

  @Nonnull
  public static TCIdentifierType createTCID (@Nullable final String sScheme, @Nonnull final String sValue)
  {
    final TCIdentifierType ret = new TCIdentifierType ();
    ret.setScheme (sScheme);
    ret.setValue (sValue);
    return ret;
  }
}
