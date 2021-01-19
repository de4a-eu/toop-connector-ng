/**
 * This work is protected under copyrights held by the members of the
 * TOOP Project Consortium as indicated at
 * http://wiki.ds.unipi.gr/display/TOOP/Contributors
 * (c) 2018-2021. All rights reserved.
 *
 * This work is dual licensed under Apache License, Version 2.0
 * and the EUPL 1.2.
 *
 *  = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
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
 *
 *  = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved
 * by the European Commission - subsequent versions of the EUPL
 * (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 *         https://joinup.ec.europa.eu/software/page/eupl
 */
package eu.toop.connector.api.simulator;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.jaxb.GenericJAXBMarshaller;
import com.helger.xsds.bdxr.smp1.CBDXRSMP1;

/**
 * JAXB helper for Simulator classes
 *
 * @author Philip Helger
 */
@Immutable
public final class TCSimulatorJAXB
{
  public static final ClassPathResource XSD_RES = new ClassPathResource ("/schemas/tc-simulator.xsd",
                                                                         TCSimulatorJAXB.class.getClassLoader ());
  public static final String NS_URI = "urn:eu.toop/toop-simulator-ng/2020/05/discovery";

  private TCSimulatorJAXB ()
  {}

  @Nonnull
  @ReturnsMutableCopy
  public static ICommonsList <ClassPathResource> getAllXSDResources ()
  {
    final ICommonsList <ClassPathResource> ret = CBDXRSMP1.getAllXSDResources ();
    ret.add (XSD_RES);
    return ret;
  }

  @Nonnull
  public static GenericJAXBMarshaller <CountryAwareServiceMetadataListType> countryAwareServiceMetadata ()
  {
    final GenericJAXBMarshaller<CountryAwareServiceMetadataListType> ret = new GenericJAXBMarshaller <> (CountryAwareServiceMetadataListType.class,
                                                                                                      getAllXSDResources (),
                                                                                                      new ObjectFactory ()::createCountryAwareServiceMetadataList);
    ret.setFormattedOutput (true);
    ret.setNamespaceContext (TCSimulatorNamespaceContext.getInstance ());
    return ret;
  }
}
