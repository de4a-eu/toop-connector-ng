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
package eu.toop.commons.codelist;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;

/**
 * Base interface for predefined identifiers
 *
 * @author Philip Helger
 */
public interface IPredefinedIdentifier extends IPredefined
{
  /**
   * @return The identifier scheme to be used. May neither be <code>null</code>
   *         nor empty.
   */
  @Nonnull
  @Nonempty
  String getScheme ();

  /**
   * @return The URI encoded identifier consisting of <code>scheme::id</code>
   */
  @Nonnull
  @Nonempty
  default String getURIEncoded ()
  {
    return getScheme () + "::" + getID ();
  }
}
