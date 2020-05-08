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

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.ToStringGenerator;

import eu.toop.edm.jaxb.cccev.CCCEVDocumentReferenceType;
import eu.toop.edm.jaxb.cv.cbc.DescriptionType;
import eu.toop.edm.jaxb.cv.cbc.IDType;

/**
 * Represents a "Reference to a Document"
 *
 * @author Philip Helger
 */
public class DocumentReferencePojo
{
  private final String m_sDocumentURI;
  private final ICommonsList <String> m_aDocumentDescriptions = new CommonsArrayList <> ();
  private final String m_sDocumentType;
  private final String m_sLocaleCode;

  public DocumentReferencePojo (@Nonnull final String sDocumentURI,
                                @Nullable final List <String> aDocumentDescriptions,
                                @Nullable final String sDocumentType,
                                @Nullable final String sLocaleCode)
  {
    ValueEnforcer.notNull (sDocumentURI, "DocumentURI");

    m_sDocumentURI = sDocumentURI;
    m_aDocumentDescriptions.addAll (aDocumentDescriptions);
    m_sDocumentType = sDocumentType;
    m_sLocaleCode = sLocaleCode;
  }

  @Nullable
  public final String getDocumentURI ()
  {
    return m_sDocumentURI;
  }

  @Nonnull
  @ReturnsMutableObject
  public final List <String> documentDescriptions ()
  {
    return m_aDocumentDescriptions;
  }

  @Nonnull
  @ReturnsMutableCopy
  public final List <String> getAllDocumentDescriptions ()
  {
    return m_aDocumentDescriptions.getClone ();
  }

  @Nullable
  public final String getDocumentType ()
  {
    return m_sDocumentType;
  }

  @Nullable
  public final String getLocaleCode ()
  {
    return m_sLocaleCode;
  }

  @Nonnull
  public CCCEVDocumentReferenceType getAsDocumentReference ()
  {
    final CCCEVDocumentReferenceType ret = new CCCEVDocumentReferenceType ();
    // Mandatory element but not needed atm
    ret.setAccessURL ("");
    if (StringHelper.hasText (m_sDocumentURI))
      ret.addDocumentURI (new IDType (m_sDocumentURI));
    for (final String s : m_aDocumentDescriptions)
      ret.addDocumentDescription (new DescriptionType (s));
    if (StringHelper.hasText (m_sDocumentType))
      ret.setDocumentType (m_sDocumentType);
    if (StringHelper.hasText (m_sLocaleCode))
      ret.setLocaleCode (m_sLocaleCode);
    return ret;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final DocumentReferencePojo rhs = (DocumentReferencePojo) o;
    return EqualsHelper.equals (m_sDocumentURI, rhs.m_sDocumentURI) &&
           EqualsHelper.equals (m_aDocumentDescriptions, rhs.m_aDocumentDescriptions) &&
           EqualsHelper.equals (m_sDocumentType, rhs.m_sDocumentType) &&
           EqualsHelper.equals (m_sLocaleCode, rhs.m_sLocaleCode);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sDocumentURI)
                                       .append (m_aDocumentDescriptions)
                                       .append (m_sDocumentType)
                                       .append (m_sLocaleCode)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("DocumentURI", m_sDocumentURI)
                                       .append ("DocumentDescriptions", m_aDocumentDescriptions)
                                       .append ("DocumentType", m_sDocumentType)
                                       .append ("LocaleCode", m_sLocaleCode)
                                       .getToString ();
  }

  @Nonnull
  public static Builder builder ()
  {
    return new Builder ();
  }

  @Nonnull
  public static Builder builder (@Nullable final CCCEVDocumentReferenceType a)
  {
    final Builder ret = new Builder ();
    if (a != null)
    {
      if (a.hasDocumentURIEntries ())
        ret.documentURI (a.getDocumentURIAtIndex (0).getValue ());
      ret.documentDescriptions (new CommonsArrayList <> (a.getDocumentDescription (), DescriptionType::getValue))
         .documentType (a.getDocumentTypeValue ())
         .localeCode (a.getLocaleCodeValue ());
    }
    return ret;
  }

  public static class Builder
  {
    private String m_sDocumentURI;
    private final ICommonsList <String> m_aDocumentDescriptions = new CommonsArrayList <> ();
    private String m_sDocumentType;
    private String m_sLocaleCode;

    public Builder ()
    {}

    @Nonnull
    public Builder documentURI (@Nullable final String s)
    {
      m_sDocumentURI = s;
      return this;
    }

    @Nonnull
    public Builder addDocumentDescription (@Nullable final String s)
    {
      if (StringHelper.hasText (s))
        m_aDocumentDescriptions.add (s);
      return this;
    }

    @Nonnull
    public Builder documentDescription (@Nullable final String s)
    {
      if (StringHelper.hasText (s))
        m_aDocumentDescriptions.set (s);
      else
        m_aDocumentDescriptions.clear ();
      return this;
    }

    @Nonnull
    public Builder documentDescriptions (@Nullable final String... a)
    {
      m_aDocumentDescriptions.setAll (a);
      return this;
    }

    @Nonnull
    public Builder documentDescriptions (@Nullable final Iterable <String> a)
    {
      m_aDocumentDescriptions.setAll (a);
      return this;
    }

    @Nonnull
    public Builder documentType (@Nullable final String s)
    {
      m_sDocumentType = s;
      return this;
    }

    @Nonnull
    public Builder localeCode (@Nullable final String s)
    {
      m_sLocaleCode = s;
      return this;
    }

    @Nonnull
    public DocumentReferencePojo build ()
    {
      return new DocumentReferencePojo (m_sDocumentURI, m_aDocumentDescriptions, m_sDocumentType, m_sLocaleCode);
    }
  }
}
