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
package eu.toop.commons.usecase;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.id.IHasID;

import eu.toop.commons.concept.ConceptValue;

/**
 * Abstract interface for a predefined TOOP concept.
 *
 * @author Philip Helger
 * @since 0.10.8
 */
public interface IToopConcept extends IHasID <String>
{
  @Nonnull
  @Nonempty
  String getConceptNamespaceURI ();

  @Nonnull
  @ReturnsMutableCopy
  default ConceptValue getAsConceptValue ()
  {
    return new ConceptValue (getConceptNamespaceURI (), getID ());
  }
}
