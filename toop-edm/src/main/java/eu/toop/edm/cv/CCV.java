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
package eu.toop.edm.cv;

import java.util.List;

import javax.annotation.Nonnull;

import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.ubl20.CUBL20;

public final class CCV
{
  @Nonnull
  private static final ClassLoader _getCL ()
  {
    return CCV.class.getClassLoader ();
  }

  public static final List <ClassPathResource> XSDS = new CommonsArrayList <> (CUBL20.XSD_UNQUALIFIED_DATA_TYPES,
                                                                               CUBL20.XSD_QUALIFIED_DATA_TYPES,
                                                                               CUBL20.XSD_COMMON_BASIC_COMPONENTS,
                                                                               CUBL20.XSD_COMMON_EXTENSION_COMPONENTS,
                                                                               new ClassPathResource ("schemas/CoreVocabularyBasicComponents-v1.00.xsd",
                                                                                                      _getCL ()),
                                                                               new ClassPathResource ("schemas/CoreVocabularyAggregateComponents-v1.00.xsd",
                                                                                                      _getCL ()),
                                                                               new ClassPathResource ("schemas/CoreLocation-v1.00.xsd",
                                                                                                      _getCL ()),
                                                                               new ClassPathResource ("schemas/CorePerson-v1.00.xsd",
                                                                                                      _getCL ()),
                                                                               new ClassPathResource ("schemas/CoreBusiness-v1.00.xsd",
                                                                                                      _getCL ())).getAsUnmodifiable ();

  private CCV ()
  {}
}
