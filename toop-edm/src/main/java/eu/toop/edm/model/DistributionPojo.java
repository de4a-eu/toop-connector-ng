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
package eu.toop.edm.model;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.mime.IMimeType;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.ToStringGenerator;
import com.helger.commons.typeconvert.TypeConverter;

import eu.toop.edm.jaxb.dcatap.DCatAPDistributionType;
import eu.toop.edm.jaxb.dcterms.DCMediaType;

public class DistributionPojo
{
  private final EDistributionFormat m_eFormat;
  private final String m_sMediaType;

  public DistributionPojo (@Nullable final EDistributionFormat eFormat, @Nullable final String sMediaType)
  {
    m_eFormat = eFormat;
    m_sMediaType = sMediaType;
  }

  @Nullable
  public final EDistributionFormat getFormat ()
  {
    return m_eFormat;
  }

  @Nullable
  public final String getMediaType ()
  {
    return m_sMediaType;
  }

  @Nonnull
  public DCatAPDistributionType getAsDistribution ()
  {
    final DCatAPDistributionType ret = new DCatAPDistributionType ();
    // Mandatory element but not needed atm
    ret.setAccessURL ("");
    if (m_eFormat != null)
    {
      final DCMediaType aFormat = new DCMediaType ();
      aFormat.addContent (m_eFormat.getID ());
      ret.setFormat (aFormat);
    }
    if (StringHelper.hasText (m_sMediaType))
    {
      final DCMediaType aMediaType = new DCMediaType ();
      aMediaType.addContent (m_sMediaType);
      ret.setMediaType (aMediaType);
    }
    return ret;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final DistributionPojo rhs = (DistributionPojo) o;
    return EqualsHelper.equals (m_eFormat, rhs.m_eFormat) && EqualsHelper.equals (m_sMediaType, rhs.m_sMediaType);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_eFormat).append (m_sMediaType).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("Format", m_eFormat).append ("MediaType", m_sMediaType).getToString ();
  }

  @Nonnull
  public static Builder builder ()
  {
    return new Builder ();
  }

  @Nonnull
  public static Builder builder (@Nullable final DCatAPDistributionType a)
  {
    final Builder ret = new Builder ();
    if (a != null)
    {
      if (a.getFormat () != null)
        ret.format (EDistributionFormat.getFromIDOrNull (TypeConverter.convert (a.getFormat ().getContentAtIndex (0),
                                                                                String.class)));
      if (a.getMediaType () != null)
        ret.mediaType (TypeConverter.convert (a.getMediaType ().getContentAtIndex (0), String.class));
    }
    return ret;
  }

  public static class Builder
  {
    private EDistributionFormat m_eFormat;
    private String m_sMediaType;

    public Builder ()
    {}

    @Nonnull
    public Builder format (@Nullable final EDistributionFormat e)
    {
      m_eFormat = e;
      return this;
    }

    @Nonnull
    public Builder mediaType (@Nullable final IMimeType a)
    {
      return mediaType (a == null ? null : a.getAsString ());
    }

    @Nonnull
    public Builder mediaType (@Nullable final String s)
    {
      m_sMediaType = s;
      return this;
    }

    @Nonnull
    public DistributionPojo build ()
    {
      return new DistributionPojo (m_eFormat, m_sMediaType);
    }
  }
}
