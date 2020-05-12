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
package eu.toop.edm.xml;

import javax.annotation.Nonnull;

import com.helger.jaxb.IJAXBWriter;

/**
 * The default implementation of {@link IJAXBVersatileWriter} using a constant
 * value and an instance of {@link IJAXBWriter}.
 *
 * @author Philip Helger
 * @param <T>
 *        Type to be written.
 */
public final class JAXBVersatileWriter <T> implements IJAXBVersatileWriter <T>
{
  private final T m_aObject;
  private final IJAXBWriter <T> m_aWriter;

  public JAXBVersatileWriter (@Nonnull final T aObject, @Nonnull final IJAXBWriter <T> aWriter)
  {
    m_aObject = aObject;
    m_aWriter = aWriter;
  }

  @Nonnull
  public T getObjectToWrite ()
  {
    return m_aObject;
  }

  @Nonnull
  public IJAXBWriter <T> getWriter ()
  {
    return m_aWriter;
  }
}
