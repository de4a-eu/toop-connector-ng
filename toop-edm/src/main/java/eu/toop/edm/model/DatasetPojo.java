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
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.datatype.XMLGregorianCalendar;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.datetime.PDTFactory;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.ToStringGenerator;
import com.helger.datetime.util.PDTXMLConverter;

import eu.toop.edm.jaxb.cccev.CCCEVDocumentReferenceType;
import eu.toop.edm.jaxb.cv.agent.AgentType;
import eu.toop.edm.jaxb.dcatap.DCatAPDatasetType;
import eu.toop.edm.jaxb.dcatap.DCatAPDistributionType;
import eu.toop.edm.jaxb.dcatap.DCatAPRelationshipType;
import eu.toop.edm.jaxb.dcterms.DCPeriodOfTimeType;

/**
 * Contains a response "Dataset" for a "document response"
 *
 * @author Philip Helger
 */
public class DatasetPojo
{
  private final ICommonsList <String> m_aDescriptions = new CommonsArrayList <> ();
  private final ICommonsList <String> m_aTitles = new CommonsArrayList <> ();
  private final DocumentReferencePojo m_aDistribution;
  private final AgentPojo m_aCreator;
  private final ICommonsList <String> m_aIDs = new CommonsArrayList <> ();
  private final LocalDateTime m_aIssuedDT;
  private final String m_sLanguage;
  private final LocalDateTime m_aLastModifiedDT;
  private final LocalDate m_aValidFrom;
  private final LocalDate m_aValidTo;
  private final ICommonsList <QualifiedRelationPojo> m_aQualifiedRelations = new CommonsArrayList <> ();

  public DatasetPojo (@Nonnull @Nonempty final ICommonsList <String> aDescriptions,
                      @Nonnull @Nonempty final ICommonsList <String> aTitles,
                      @Nullable final DocumentReferencePojo aDistribution,
                      @Nullable final AgentPojo aCreator,
                      @Nullable final ICommonsList <String> aIDs,
                      @Nullable final LocalDateTime aIssuedDT,
                      @Nullable final String sLanguage,
                      @Nullable final LocalDateTime aLastModifiedDT,
                      @Nullable final LocalDate aValidFrom,
                      @Nullable final LocalDate aValidTo,
                      @Nullable final ICommonsList <QualifiedRelationPojo> aQualifiedRelations)
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

  @Nullable
  public final DocumentReferencePojo getDistribution ()
  {
    return m_aDistribution;
  }

  @Nullable
  public final AgentPojo getCreator ()
  {
    return m_aCreator;
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
    return m_aIDs;
  }

  @Nullable
  public final LocalDateTime getIssuedDT ()
  {
    return m_aIssuedDT;
  }

  @Nullable
  public final String getLanguage ()
  {
    return m_sLanguage;
  }

  @Nullable
  public final LocalDateTime getLastModifiedDT ()
  {
    return m_aLastModifiedDT;
  }

  @Nullable
  public final LocalDate getValidFrom ()
  {
    return m_aValidFrom;
  }

  @Nullable
  public final LocalDate getValidTo ()
  {
    return m_aValidTo;
  }

  @Nonnull
  @ReturnsMutableObject
  public final List <QualifiedRelationPojo> qualifiedRelations ()
  {
    return m_aQualifiedRelations;
  }

  @Nonnull
  @ReturnsMutableCopy
  public final List <QualifiedRelationPojo> getAllQualifiedRelations ()
  {
    return m_aQualifiedRelations.getClone ();
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
      ret.addDistribution (m_aDistribution.getAsDocumentReference ());
    if (m_aCreator != null)
      ret.setCreator (m_aCreator.getAsAgent ());
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
    for (final QualifiedRelationPojo aItem : m_aQualifiedRelations)
      ret.addQualifiedRelation (aItem.getAsRelationship ());
    return ret;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final DatasetPojo rhs = (DatasetPojo) o;
    return EqualsHelper.equals (m_aDescriptions, rhs.m_aDescriptions) &&
           EqualsHelper.equals (m_aTitles, rhs.m_aTitles) &&
           EqualsHelper.equals (m_aDistribution, rhs.m_aDistribution) &&
           EqualsHelper.equals (m_aCreator, rhs.m_aCreator) &&
           EqualsHelper.equals (m_aIDs, rhs.m_aIDs) &&
           EqualsHelper.equals (m_aIssuedDT, rhs.m_aIssuedDT) &&
           EqualsHelper.equals (m_sLanguage, rhs.m_sLanguage) &&
           EqualsHelper.equals (m_aLastModifiedDT, rhs.m_aLastModifiedDT) &&
           EqualsHelper.equals (m_aValidFrom, rhs.m_aValidFrom) &&
           EqualsHelper.equals (m_aValidTo, rhs.m_aValidTo) &&
           EqualsHelper.equals (m_aQualifiedRelations, rhs.m_aQualifiedRelations);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aDescriptions)
                                       .append (m_aTitles)
                                       .append (m_aDistribution)
                                       .append (m_aCreator)
                                       .append (m_aIDs)
                                       .append (m_aIssuedDT)
                                       .append (m_sLanguage)
                                       .append (m_aLastModifiedDT)
                                       .append (m_aValidFrom)
                                       .append (m_aValidTo)
                                       .append (m_aQualifiedRelations)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("Descriptions", m_aDescriptions)
                                       .append ("Titles", m_aTitles)
                                       .append ("Distribution", m_aDistribution)
                                       .append ("Creator", m_aCreator)
                                       .append ("IDs", m_aIDs)
                                       .append ("IssuedDT", m_aIssuedDT)
                                       .append ("Language", m_sLanguage)
                                       .append ("LastModifiedDT", m_aLastModifiedDT)
                                       .append ("ValidFrom", m_aValidFrom)
                                       .append ("ValidTo", m_aValidTo)
                                       .append ("QualifiedRelations", m_aQualifiedRelations)
                                       .getToString ();
  }

  @Nonnull
  public static Builder builder ()
  {
    return new Builder ();
  }

  @Nonnull
  public static Builder builder (@Nullable final DCatAPDatasetType a)
  {
    final Builder ret = new Builder ();
    if (a != null)
    {
      ret.descriptions (a.getDescription ()).titles (a.getTitle ());
      if (a.hasDistributionEntries ())
      {
        final DCatAPDistributionType aDist = a.getDistributionAtIndex (0);
        if (aDist instanceof CCCEVDocumentReferenceType)
          ret.distribution (DocumentReferencePojo.builder ((CCCEVDocumentReferenceType) aDist));
      }
      if (a.getCreator () instanceof AgentType)
        ret.creator (AgentPojo.builder ((AgentType) a.getCreator ()));
      ret.ids (a.getIdentifier ()).issued (a.getIssued ());
      if (a.hasLanguageEntries ())
        ret.language (a.getLanguageAtIndex (0));
      ret.lastModified (a.getModified ());
      if (a.hasTemporalEntries ())
      {
        final DCPeriodOfTimeType aTemp = a.getTemporalAtIndex (0);
        if (aTemp.hasStartDateEntries ())
          ret.validFrom (aTemp.getStartDateAtIndex (0));
        if (aTemp.hasEndDateEntries ())
          ret.validTo (aTemp.getEndDateAtIndex (0));
      }
      for (final DCatAPRelationshipType aItem : a.getQualifiedRelation ())
        ret.addQualifiedRelation (QualifiedRelationPojo.builder (aItem));
    }
    return ret;
  }

  public static class Builder
  {
    private final ICommonsList <String> m_aDescriptions = new CommonsArrayList <> ();
    private final ICommonsList <String> m_aTitles = new CommonsArrayList <> ();
    private DocumentReferencePojo m_aDistribution;
    private AgentPojo m_aCreator;
    private final ICommonsList <String> m_aIDs = new CommonsArrayList <> ();
    private LocalDateTime m_aIssuedDT;
    private String m_sLanguage;
    private LocalDateTime m_aLastModifiedDT;
    private LocalDate m_aValidFrom;
    private LocalDate m_aValidTo;
    private final ICommonsList <QualifiedRelationPojo> m_aQualifiedRelations = new CommonsArrayList <> ();

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
      m_aDistribution = a;
      return this;
    }

    @Nonnull
    public Builder distribution (@Nullable final CCCEVDocumentReferenceType a)
    {
      return distribution (a == null ? null : DocumentReferencePojo.builder (a));
    }

    @Nonnull
    public Builder creator (@Nullable final AgentPojo.Builder a)
    {
      return creator (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder creator (@Nullable final AgentPojo a)
    {
      m_aCreator = a;
      return this;
    }

    @Nonnull
    public Builder creator (@Nullable final AgentType a)
    {
      return creator (a == null ? null : AgentPojo.builder (a));
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
    public Builder issued (@Nullable final XMLGregorianCalendar a)
    {
      return issued (PDTXMLConverter.getLocalDateTime (a));
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
    public Builder lastModified (@Nullable final XMLGregorianCalendar a)
    {
      return lastModified (PDTXMLConverter.getLocalDateTime (a));
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
    public Builder validFrom (@Nullable final XMLGregorianCalendar a)
    {
      return validFrom (PDTXMLConverter.getLocalDate (a));
    }

    @Nonnull
    public Builder validFrom (@Nullable final LocalDate a)
    {
      m_aValidFrom = a;
      return this;
    }

    @Nonnull
    public Builder validTo (@Nullable final XMLGregorianCalendar a)
    {
      return validTo (PDTXMLConverter.getLocalDate (a));
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
      if (a != null)
        m_aQualifiedRelations.add (a);
      return this;
    }

    @Nonnull
    public Builder addQualifiedRelation (@Nullable final DCatAPRelationshipType a)
    {
      return addQualifiedRelation (a == null ? null : QualifiedRelationPojo.builder (a));
    }

    @Nonnull
    public Builder qualifiedRelation (@Nullable final QualifiedRelationPojo.Builder a)
    {
      return qualifiedRelation (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder qualifiedRelation (@Nullable final QualifiedRelationPojo a)
    {
      if (a != null)
        m_aQualifiedRelations.set (a);
      else
        m_aQualifiedRelations.clear ();
      return this;
    }

    @Nonnull
    public Builder qualifiedRelation (@Nullable final DCatAPRelationshipType a)
    {
      return qualifiedRelation (a == null ? null : QualifiedRelationPojo.builder (a));
    }

    @Nonnull
    public Builder qualifiedRelations (@Nullable final QualifiedRelationPojo... a)
    {
      m_aQualifiedRelations.setAll (a);
      return this;
    }

    @Nonnull
    public Builder qualifiedRelations (@Nullable final Iterable <? extends QualifiedRelationPojo> a)
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
