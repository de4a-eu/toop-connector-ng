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
package eu.toop.commons.asic;

import java.io.Serializable;
import java.util.Arrays;

import javax.annotation.Nonnull;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.MustImplementEqualsAndHashcode;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.ToStringGenerator;

/**
 * This class contains a single ASIC container entry for external handling.
 *
 * @author Philip Helger
 * @since 0.10.2
 */
@MustImplementEqualsAndHashcode
public class AsicReadEntry implements Serializable
{
  private final String m_sEntryName;
  private final byte [] m_aPayload;

  public AsicReadEntry (@Nonnull @Nonempty final String sEntryName, @Nonnull final byte [] aPayload)
  {
    ValueEnforcer.notEmpty (sEntryName, "EntryName");
    ValueEnforcer.notNull (aPayload, "Payload");
    m_sEntryName = sEntryName;
    m_aPayload = aPayload;
  }

  @Nonnull
  @Nonempty
  public String getEntryName ()
  {
    return m_sEntryName;
  }

  @Nonnull
  @ReturnsMutableObject
  public byte [] payload ()
  {
    return m_aPayload;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final AsicReadEntry rhs = (AsicReadEntry) o;
    return m_sEntryName.equals (rhs.m_sEntryName) && Arrays.equals (m_aPayload, rhs.m_aPayload);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sEntryName).append (m_aPayload.length).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("EntryName", m_sEntryName)
                                       .append ("Payload #bytes", m_aPayload.length)
                                       .getToString ();
  }
}
