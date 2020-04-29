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

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.datetime.PDTFactory;
import com.helger.commons.string.StringHelper;
import com.helger.datetime.util.PDTXMLConverter;

import eu.toop.edm.jaxb.cccev.CCCEVDocumentReferenceType;
import eu.toop.edm.jaxb.cv.agent.AgentType;
import eu.toop.edm.jaxb.dcatap.DCatAPDatasetType;
import eu.toop.edm.jaxb.dcatap.DCatAPRelationshipType;
import eu.toop.edm.jaxb.dcterms.DCPeriodOfTimeType;

public class DatasetPojo
{
  private final ICommonsList <String> m_aDescriptions = new CommonsArrayList <> ();
  private final ICommonsList <String> m_aTitles = new CommonsArrayList <> ();
  private final CCCEVDocumentReferenceType m_aDistribution;
  private final AgentType m_aCreator;
  private final ICommonsList <String> m_aIDs = new CommonsArrayList <> ();
  private final LocalDateTime m_aIssuedDT;
  private final String m_sLanguage;
  private final LocalDateTime m_aLastModifiedDT;
  private final LocalDate m_aValidFrom;
  private final LocalDate m_aValidTo;
  private final ICommonsList <DCatAPRelationshipType> m_aQualifiedRelations = new CommonsArrayList <> ();

  public DatasetPojo (@Nonnull @Nonempty final ICommonsList <String> aDescriptions,
                      @Nonnull @Nonempty final ICommonsList <String> aTitles,
                      @Nullable final CCCEVDocumentReferenceType aDistribution,
                      @Nullable final AgentType aCreator,
                      @Nullable final ICommonsList <String> aIDs,
                      @Nullable final LocalDateTime aIssuedDT,
                      @Nullable final String sLanguage,
                      @Nullable final LocalDateTime aLastModifiedDT,
                      @Nullable final LocalDate aValidFrom,
                      @Nullable final LocalDate aValidTo,
                      @Nullable final ICommonsList <DCatAPRelationshipType> aQualifiedRelations)
  {
    ValueEnforcer.notEmptyNoNullValue (aDescriptions, "Descriptions");
    ValueEnforcer.notEmptyNoNullValue (aTitles, "Titles");
    ValueEnforcer.noNullValue (aIDs, "IDs");
    ValueEnforcer.noNullValue (aQualifiedRelations, "QualifiedRelations");

    m_aDescriptions.addAll (aDescriptions);
    m_aTitles.addAll (aTitles);
    m_aDistribution = aDistribution;
    m_aCreator = aCreator;
    if (aIDs != null)
      m_aIDs.addAll (aIDs);
    m_aIssuedDT = aIssuedDT;
    m_sLanguage = sLanguage;
    m_aLastModifiedDT = aLastModifiedDT;
    m_aValidFrom = aValidFrom;
    m_aValidTo = aValidTo;
    if (aQualifiedRelations != null)
      m_aQualifiedRelations.addAll (aQualifiedRelations);
  }

  @Nonnull
  public DCatAPDatasetType getAsDataset ()
  {
    final DCatAPDatasetType ret = new DCatAPDatasetType ();
    for (final String sDescription : m_aDescriptions)
      ret.addDescription (sDescription);
    for (final String sTitle : m_aTitles)
      ret.addTitle (sTitle);
    if (m_aDistribution != null)
      ret.addDistribution (m_aDistribution);
    if (m_aCreator != null)
      ret.setCreator (m_aCreator);
    for (final String sID : m_aIDs)
      ret.addIdentifier (sID);
    if (m_aIssuedDT != null)
      ret.setIssued (PDTXMLConverter.getXMLCalendar (m_aIssuedDT));
    if (StringHelper.hasText (m_sLanguage))
      ret.addLanguage (m_sLanguage);
    if (m_aLastModifiedDT != null)
      ret.setModified (PDTXMLConverter.getXMLCalendar (m_aLastModifiedDT));
    if (m_aValidFrom != null || m_aValidTo != null)
    {
      final DCPeriodOfTimeType aPeriodOfType = new DCPeriodOfTimeType ();
      if (m_aValidFrom != null)
        aPeriodOfType.addStartDate (PDTXMLConverter.getXMLCalendarDate (m_aValidFrom));
      if (m_aValidTo != null)
        aPeriodOfType.addEndDate (PDTXMLConverter.getXMLCalendarDate (m_aValidTo));
      ret.addTemporal (aPeriodOfType);
    }
    for (final DCatAPRelationshipType aItem : m_aQualifiedRelations)
      ret.addQualifiedRelation (aItem);
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
    private final ICommonsList <String> m_aTitles = new CommonsArrayList <> ();
    private CCCEVDocumentReferenceType m_aDistribution;
    private AgentType m_aCreator;
    private final ICommonsList <String> m_aIDs = new CommonsArrayList <> ();
    private LocalDateTime m_aIssuedDT;
    private String m_sLanguage;
    private LocalDateTime m_aLastModifiedDT;
    private LocalDate m_aValidFrom;
    private LocalDate m_aValidTo;
    private final ICommonsList <DCatAPRelationshipType> m_aQualifiedRelations = new CommonsArrayList <> ();

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
    public Builder distribution (@Nullable final DocumentReferencePojo.Builder a)
    {
      return distribution (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder distribution (@Nullable final DocumentReferencePojo a)
    {
      return distribution (a == null ? null : a.getAsDocumentReference ());
    }

    @Nonnull
    public Builder distribution (@Nullable final CCCEVDocumentReferenceType a)
    {
      m_aDistribution = a;
      return this;
    }

    @Nonnull
    public Builder creator (@Nullable final AgentPojo.Builder a)
    {
      return creator (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder creator (@Nullable final AgentPojo a)
    {
      return creator (a == null ? null : a.getAsAgent ());
    }

    @Nonnull
    public Builder creator (@Nullable final AgentType a)
    {
      m_aCreator = a;
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
    public Builder issuedNow ()
    {
      return issued (PDTFactory.getCurrentLocalDateTime ());
    }

    @Nonnull
    public Builder issued (@Nullable final LocalDateTime a)
    {
      m_aIssuedDT = a;
      return this;
    }

    @Nonnull
    public Builder language (@Nullable final String s)
    {
      m_sLanguage = s;
      return this;
    }

    @Nonnull
    public Builder lastModifiedNow ()
    {
      return lastModified (PDTFactory.getCurrentLocalDateTime ());
    }

    @Nonnull
    public Builder lastModified (@Nullable final LocalDateTime a)
    {
      m_aLastModifiedDT = a;
      return this;
    }

    @Nonnull
    public Builder validFrom (@Nullable final LocalDate a)
    {
      m_aValidFrom = a;
      return this;
    }

    @Nonnull
    public Builder validTo (@Nullable final LocalDate a)
    {
      m_aValidTo = a;
      return this;
    }

    @Nonnull
    public Builder addQualifiedRelation (@Nullable final QualifiedRelationPojo.Builder a)
    {
      return addQualifiedRelation (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder addQualifiedRelation (@Nullable final QualifiedRelationPojo a)
    {
      return addQualifiedRelation (a == null ? null : a.getAsRelationship ());
    }

    @Nonnull
    public Builder addQualifiedRelation (@Nullable final DCatAPRelationshipType a)
    {
      if (a != null)
        m_aQualifiedRelations.add (a);
      return this;
    }

    @Nonnull
    public Builder qualifiedRelation (@Nullable final QualifiedRelationPojo.Builder a)
    {
      return qualifiedRelation (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder qualifiedRelation (@Nullable final QualifiedRelationPojo a)
    {
      return qualifiedRelation (a == null ? null : a.getAsRelationship ());
    }

    @Nonnull
    public Builder qualifiedRelation (@Nullable final DCatAPRelationshipType a)
    {
      if (a != null)
        m_aQualifiedRelations.set (a);
      else
        m_aQualifiedRelations.clear ();
      return this;
    }

    @Nonnull
    public Builder qualifiedRelations (@Nullable final DCatAPRelationshipType... a)
    {
      m_aQualifiedRelations.setAll (a);
      return this;
    }

    @Nonnull
    public Builder qualifiedRelations (@Nullable final Iterable <? extends DCatAPRelationshipType> a)
    {
      m_aQualifiedRelations.setAll (a);
      return this;
    }

    @Nonnull
    public DatasetPojo build ()
    {
      return new DatasetPojo (m_aDescriptions,
                              m_aTitles,
                              m_aDistribution,
                              m_aCreator,
                              m_aIDs,
                              m_aIssuedDT,
                              m_sLanguage,
                              m_aLastModifiedDT,
                              m_aValidFrom,
                              m_aValidTo,
                              m_aQualifiedRelations);
    }
  }
}
