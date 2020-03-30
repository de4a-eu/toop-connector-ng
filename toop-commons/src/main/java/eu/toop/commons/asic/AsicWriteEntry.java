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
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.mime.CMimeType;
import com.helger.commons.mime.IMimeType;
import com.helger.commons.string.ToStringGenerator;

/**
 * This class contains a single ASIC container entry for creating ASICs.
 *
 * @author Philip Helger
 * @since 0.10.2
 */
public class AsicWriteEntry implements Serializable
{
  public static final IMimeType FALLBACK_MIME_TYPE = CMimeType.APPLICATION_OCTET_STREAM;

  private final String m_sEntryName;
  private final byte [] m_aPayload;
  private final IMimeType m_aMimeType;

  public AsicWriteEntry (@Nonnull @Nonempty final String sEntryName,
                         @Nonnull final byte [] aPayload,
                         @Nonnull final IMimeType aMimeType)
  {
    ValueEnforcer.notEmpty (sEntryName, "EntryName");
    ValueEnforcer.notNull (aPayload, "Payload");
    ValueEnforcer.notNull (aMimeType, "MimeType");
    m_sEntryName = sEntryName;
    m_aPayload = aPayload;
    m_aMimeType = aMimeType;
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

  @Nonnull
  public IMimeType getMimeType ()
  {
    return m_aMimeType;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final AsicWriteEntry rhs = (AsicWriteEntry) o;
    return m_sEntryName.equals (rhs.m_sEntryName) &&
           Arrays.equals (m_aPayload, rhs.m_aPayload) &&
           m_aMimeType.equals (rhs.m_aMimeType);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sEntryName).append (m_aPayload).append (m_aMimeType).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("EntryName", m_sEntryName)
                                       .append ("Payload #bytes", m_aPayload.length)
                                       .append ("MimeType", m_aMimeType)
                                       .getToString ();
  }

  /**
   * Create an {@link AsicWriteEntry} based on an {@link AsicReadEntry} using
   * the default MIME type "application/octet-stream".
   *
   * @param aEntry
   *        The {@link AsicReadEntry} to use. May not be <code>null</code>.
   * @return The created {@link AsicWriteEntry} and never <code>null</code>.
   */
  @Nonnull
  public static AsicWriteEntry create (@Nonnull final AsicReadEntry aEntry)
  {
    return create (aEntry, FALLBACK_MIME_TYPE);
  }

  /**
   * Create an {@link AsicWriteEntry} based on an {@link AsicReadEntry} using
   * the provided MIME type.
   *
   * @param aEntry
   *        The {@link AsicReadEntry} to use. May not be <code>null</code>.
   * @param aMimeType
   *        The MIME Type to be used. May not be <code>null</code>.
   * @return The created {@link AsicWriteEntry} and never <code>null</code>.
   */
  @Nonnull
  public static AsicWriteEntry create (@Nonnull final AsicReadEntry aEntry, @Nonnull final IMimeType aMimeType)
  {
    return new AsicWriteEntry (aEntry.getEntryName (), aEntry.payload (), aMimeType);
  }
}
