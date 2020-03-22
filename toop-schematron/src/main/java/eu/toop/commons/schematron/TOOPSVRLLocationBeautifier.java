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
package eu.toop.commons.schematron;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.IsSPIImplementation;
import com.helger.schematron.svrl.ISVRLLocationBeautifierSPI;

@IsSPIImplementation
public final class TOOPSVRLLocationBeautifier implements ISVRLLocationBeautifierSPI
{
  private static final String TOOP_140_NS = eu.toop.commons.dataexchange.v140.ObjectFactory._Request_QNAME.getNamespaceURI ();

  @Nullable
  public String getReplacementText (@Nonnull final String sNamespaceURI, @Nonnull final String sLocalName)
  {
    if (sNamespaceURI.equals (TOOP_140_NS))
      return "toop:" + sLocalName;
    return null;
  }
}
