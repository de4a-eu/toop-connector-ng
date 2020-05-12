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

import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.transform.Source;

import org.w3c.dom.Node;

import com.helger.commons.ValueEnforcer;
import com.helger.jaxb.IJAXBReader;

/**
 * Default implementation of {@link IJAXBVersatileReader}
 *
 * @author Philip Helger
 * @param <JAXBTYPE>
 *        internal JAXB type
 * @param <T>
 *        Outside data type to be read
 */
public class JAXBVersatileReader <JAXBTYPE, T> implements IJAXBVersatileReader <T>
{
  private final IJAXBReader <JAXBTYPE> m_aReader;
  private final Function <JAXBTYPE, T> m_aMapper;

  public JAXBVersatileReader (@Nonnull final IJAXBReader <JAXBTYPE> aReader,
                              @Nonnull final Function <JAXBTYPE, T> aMapper)
  {
    ValueEnforcer.notNull (aReader, "Reader");
    ValueEnforcer.notNull (aMapper, "Mapper");
    m_aReader = aReader;
    m_aMapper = aMapper;
  }

  @Nullable
  public T read (@Nonnull final Source aSource)
  {
    final JAXBTYPE aObj = m_aReader.read (aSource);
    if (aObj == null)
      return null;

    try
    {
      return m_aMapper.apply (aObj);
    }
    catch (final RuntimeException ex)
    {
      return null;
    }
  }

  @Nullable
  public T read (@Nonnull final Node aNode)
  {
    final JAXBTYPE aObj = m_aReader.read (aNode);
    return aObj == null ? null : m_aMapper.apply (aObj);
  }
}
