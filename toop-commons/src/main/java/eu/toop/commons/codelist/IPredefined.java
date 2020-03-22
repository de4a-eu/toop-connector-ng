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
import javax.annotation.Nullable;

import com.helger.commons.id.IHasID;
import com.helger.commons.name.IHasName;
import com.helger.commons.version.Version;

/**
 * Base interface for a single predefined code list item.
 *
 * @author Philip Helger
 */
public interface IPredefined extends IHasID <String>, IHasName
{
  /**
   * @return The code list version when the item was added. Never
   *         <code>null</code>.
   */
  @Nonnull
  Version getSince ();

  /**
   * @return <code>true</code> if this code list entry is deprecated in the code
   *         list.
   * @see #getDeprecatedSince()
   */
  boolean isDeprecated ();

  /**
   * @return The code list version when the item was deprecated. May be
   *         <code>null</code>. Must not be <code>null</code> if
   *         {@link #isDeprecated()} returns <code>true</code>.
   * @see #isDeprecated()
   */
  @Nullable
  Version getDeprecatedSince ();
}
