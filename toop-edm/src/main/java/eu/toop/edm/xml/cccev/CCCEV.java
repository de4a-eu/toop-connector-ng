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

import java.util.List;

import javax.annotation.Nonnull;

import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.io.resource.ClassPathResource;

import eu.toop.edm.xml.cv.CCV;
import eu.toop.edm.xml.dcatap.CDCatAP;

public final class CCCEV
{
  @Nonnull
  private static final ClassLoader _getCL ()
  {
    return CCCEV.class.getClassLoader ();
  }

  public static final List <ClassPathResource> XSDS;
  static
  {
    final ICommonsList <ClassPathResource> aList = new CommonsArrayList <> ();
    aList.addAll (CCV.XSDS);
    aList.addAll (CDCatAP.XSDS);
    aList.addAll (new ClassPathResource ("schemas/CV-CommonAggregateComponents.xsd", _getCL ()),
                  new ClassPathResource ("schemas/CV-Agent.xsd", _getCL ()),
                  new ClassPathResource ("schemas/cccev-2.0.0.xsd", _getCL ()));
    XSDS = aList.getAsUnmodifiable ();
  }

  private CCCEV ()
  {}
}
