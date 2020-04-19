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
package eu.toop.regrep;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;

/**
 * Enumeration with collection types.
 * 
 * @author Philip Helger
 */
public enum ECollectionType
{
  BAG ("urn:oasis:names:tc:ebxml-regrep:CollectionType:Bag"),
  LIST ("urn:oasis:names:tc:ebxml-regrep:CollectionType:List"),
  SET ("urn:oasis:names:tc:ebxml-regrep:CollectionType:Set"),
  SORTED_SET ("urn:oasis:names:tc:ebxml-regrep:CollectionType:Set:SortedSet");

  private final String m_sValue;

  private ECollectionType (@Nonnull @Nonempty final String sValue)
  {
    m_sValue = sValue;
  }

  @Nonnull
  @Nonempty
  public String getValue ()
  {
    return m_sValue;
  }
}
