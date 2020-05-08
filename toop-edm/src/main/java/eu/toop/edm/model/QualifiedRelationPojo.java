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
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.ToStringGenerator;

import eu.toop.edm.jaxb.cccev.CCCEVReferenceFrameworkType;
import eu.toop.edm.jaxb.dcatap.DCatAPDatasetType;
import eu.toop.edm.jaxb.dcatap.DCatAPRelationshipType;

/**
 * Represents a qualified relation.
 *
 * @author Philip Helger
 */
public class QualifiedRelationPojo
{
  private final ICommonsList <String> m_aDescriptions = new CommonsArrayList <> ();
  private final ICommonsList <String> m_aTitles = new CommonsArrayList <> ();
  private final ICommonsList <String> m_aIDs = new CommonsArrayList <> ();

  public QualifiedRelationPojo (@Nonnull @Nonempty final ICommonsList <String> aDescriptions,
                                @Nonnull @Nonempty final ICommonsList <String> aTitles,
                                @Nullable final ICommonsList <String> aIDs)
  {
    ValueEnforcer.notEmptyNoNullValue (aDescriptions, "Descriptions");
    ValueEnforcer.notEmptyNoNullValue (aTitles, "Titles");
    ValueEnforcer.noNullValue (aIDs, "IDs");

    m_aDescriptions.addAll (aDescriptions);
    m_aTitles.addAll (aTitles);
    if (aIDs != null)
      m_aIDs.addAll (aIDs);
  }

  @Nonnull
  @ReturnsMutableObject
  public final List <String> descriptions ()
  {
    return m_aDescriptions;
  }

  @Nonnull
  @ReturnsMutableCopy
  public final List <String> getAllDescriptions ()
  {
    return m_aDescriptions.getClone ();
  }

  @Nonnull
  @ReturnsMutableObject
  public final List <String> titles ()
  {
    return m_aTitles;
  }

  @Nonnull
  @ReturnsMutableCopy
  public final List <String> getAllTitles ()
  {
    return m_aTitles.getClone ();
  }

  @Nonnull
  @ReturnsMutableObject
  public final List <String> ids ()
  {
    return m_aIDs;
  }

  @Nonnull
  @ReturnsMutableCopy
  public final List <String> getAllIDs ()
  {
    return m_aIDs.getClone ();
  }

  @Nonnull
  public DCatAPRelationshipType getAsRelationship ()
  {
    final DCatAPRelationshipType ret = new DCatAPRelationshipType ();
    final CCCEVReferenceFrameworkType aRelation = new CCCEVReferenceFrameworkType ();
    for (final String sDescription : m_aDescriptions)
      aRelation.addDescription (sDescription);
    for (final String sTitle : m_aTitles)
      aRelation.addTitle (sTitle);
    for (final String sID : m_aIDs)
      aRelation.addIdentifier (sID);
    ret.addRelation (aRelation);
    return ret;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final QualifiedRelationPojo rhs = (QualifiedRelationPojo) o;
    return EqualsHelper.equals (m_aDescriptions, rhs.m_aDescriptions) &&
           EqualsHelper.equals (m_aTitles, rhs.m_aTitles) &&
           EqualsHelper.equals (m_aIDs, rhs.m_aIDs);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aDescriptions).append (m_aTitles).append (m_aIDs).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("Descriptions", m_aDescriptions)
                                       .append ("Titles", m_aTitles)
                                       .append ("IDs", m_aIDs)
                                       .getToString ();
  }

  @Nonnull
  public static Builder builder ()
  {
    return new Builder ();
  }

  @Nonnull
  public static Builder builder (@Nullable final DCatAPRelationshipType a)
  {
    final Builder ret = new Builder ();
    if (a != null && a.hasRelationEntries ())
    {
      final Object r = a.getRelationAtIndex (0);
      if (r instanceof DCatAPDatasetType)
      {
        final DCatAPDatasetType rf = (DCatAPDatasetType) r;
        ret.descriptions (rf.getDescription ()).titles (rf.getTitle ()).ids (rf.getIdentifier ());
      }
    }
    return ret;
  }

  public static class Builder
  {
    private final ICommonsList <String> m_aDescriptions = new CommonsArrayList <> ();
    private final ICommonsList <String> m_aTitles = new CommonsArrayList <> ();
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
    public Builder addTitle (@Nullable final String s)
    {
      if (StringHelper.hasText (s))
        m_aTitles.add (s);
      return this;
    }

    @Nonnull
    public Builder title (@Nullable final String s)
    {
      if (StringHelper.hasText (s))
        m_aTitles.set (s);
      else
        m_aTitles.clear ();
      return this;
    }

    @Nonnull
    public Builder titles (@Nullable final String... a)
    {
      m_aTitles.setAll (a);
      return this;
    }

    @Nonnull
    public Builder titles (@Nullable final Iterable <String> a)
    {
      m_aTitles.setAll (a);
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
      return new QualifiedRelationPojo (m_aDescriptions, m_aTitles, m_aIDs);
    }
  }
}
