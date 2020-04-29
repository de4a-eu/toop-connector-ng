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

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.string.StringHelper;

import eu.toop.edm.jaxb.cccev.CCCEVReferenceFrameworkType;
import eu.toop.edm.jaxb.dcatap.DCatAPRelationshipType;

public class QualifiedRelationPojo
{
  private final ICommonsList <String> m_aDescriptions = new CommonsArrayList <> ();
  private final String m_sTitle;
  private final ICommonsList <String> m_aIDs = new CommonsArrayList <> ();

  public QualifiedRelationPojo (@Nonnull @Nonempty final ICommonsList <String> aDescriptions,
                                @Nonnull final String sTitle,
                                @Nullable final ICommonsList <String> aIDs)
  {
    ValueEnforcer.notEmptyNoNullValue (aDescriptions, "Descriptions");
    ValueEnforcer.notNull (sTitle, "Title");

    if (aDescriptions != null)
      m_aDescriptions.addAll (aDescriptions);
    m_sTitle = sTitle;
    if (aIDs != null)
      m_aIDs.addAll (aIDs);
  }

  @Nonnull
  public DCatAPRelationshipType getAsRelationship ()
  {
    final DCatAPRelationshipType ret = new DCatAPRelationshipType ();
    final CCCEVReferenceFrameworkType aRelation = new CCCEVReferenceFrameworkType ();
    for (final String sDescription : m_aDescriptions)
      aRelation.addDescription (sDescription);
    if (StringHelper.hasText (m_sTitle))
      aRelation.addTitle (m_sTitle);
    for (final String sID : m_aIDs)
      aRelation.addIdentifier (sID);
    ret.addRelation (aRelation);
    return ret;
  }

  @Nonnull
  public static Builder builder ()
  {
    return new Builder ();
  }

  public static class Builder
  {
    private final ICommonsList <String> m_aDescriptions = new CommonsArrayList <> ();
    private String m_sTitle;
    private final ICommonsList <String> m_aIDs = new CommonsArrayList <> ();

    public Builder ()
    {}

    @Nonnull
    public Builder addDescription (@Nullable final String s)
    {
      if (StringHelper.hasText (s))
        m_aDescriptions.add (s);
      return this;
    }

    @Nonnull
    public Builder description (@Nullable final String s)
    {
      if (StringHelper.hasText (s))
        m_aDescriptions.set (s);
      else
        m_aDescriptions.clear ();
      return this;
    }

    @Nonnull
    public Builder descriptions (@Nullable final String... a)
    {
      m_aDescriptions.setAll (a);
      return this;
    }

    @Nonnull
    public Builder descriptions (@Nullable final Iterable <String> a)
    {
      m_aDescriptions.setAll (a);
      return this;
    }

    @Nonnull
    public Builder title (@Nullable final String s)
    {
      m_sTitle = s;
      return this;
    }

    @Nonnull
    public Builder addID (@Nullable final String s)
    {
      if (StringHelper.hasText (s))
        m_aIDs.add (s);
      return this;
    }

    @Nonnull
    public Builder id (@Nullable final String s)
    {
      if (StringHelper.hasText (s))
        m_aIDs.set (s);
      else
        m_aIDs.clear ();
      return this;
    }

    @Nonnull
    public Builder ids (@Nullable final String... a)
    {
      m_aIDs.setAll (a);
      return this;
    }

    @Nonnull
    public Builder ids (@Nullable final Iterable <String> a)
    {
      m_aIDs.setAll (a);
      return this;
    }

    @Nonnull
    public QualifiedRelationPojo build ()
    {
      return new QualifiedRelationPojo (m_aDescriptions, m_sTitle, m_aIDs);
    }
  }
}
