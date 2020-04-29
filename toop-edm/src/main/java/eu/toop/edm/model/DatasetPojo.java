package eu.toop.edm.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

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
import eu.toop.edm.jaxb.dcterms.DCPeriodOfTimeType;

public class DatasetPojo
{
  private final ICommonsList <String> m_aDescriptions = new CommonsArrayList <> ();
  private final ICommonsList <String> m_aTitles = new CommonsArrayList <> ();
  private final CCCEVDocumentReferenceType m_aDistribution;
  private final AgentType m_aCreator;
  private final String m_sID;
  private final LocalDateTime m_aIssuedDT;
  private final String m_sLanguage;
  private final LocalDateTime m_aLastModifiedDT;
  private final LocalDate m_aValidFrom;
  private final LocalDate m_aValidTo;

  public DatasetPojo (@Nonnull @Nonempty final ICommonsList <String> aDescriptions,
                      @Nonnull @Nonempty final ICommonsList <String> aTitles,
                      @Nullable final CCCEVDocumentReferenceType aDistribution,
                      @Nullable final AgentType aCreator,
                      @Nullable final String sID,
                      @Nullable final LocalDateTime aIssuedDT,
                      @Nullable final String sLanguage,
                      @Nullable final LocalDateTime aLastModifiedDT,
                      @Nullable final LocalDate aValidFrom,
                      @Nullable final LocalDate aValidTo)
  {
    ValueEnforcer.notEmptyNoNullValue (aTitles, "Titles");
    ValueEnforcer.notEmptyNoNullValue (aDescriptions, "Descriptions");

    m_aDescriptions.addAll (aDescriptions);
    m_aTitles.addAll (aTitles);
    m_aDistribution = aDistribution;
    m_aCreator = aCreator;
    m_sID = sID;
    m_aIssuedDT = aIssuedDT;
    m_sLanguage = sLanguage;
    m_aLastModifiedDT = aLastModifiedDT;
    m_aValidFrom = aValidFrom;
    m_aValidTo = aValidTo;
  }

  @Nonnull
  public DCatAPDatasetType getAsDataset ()
  {
    final DCatAPDatasetType ret = new DCatAPDatasetType ();
    for (final String sDescription : m_aDescriptions)
      ret.addContent (new eu.toop.edm.jaxb.dcterms.ObjectFactory ().createDescription (sDescription));
    for (final String sTitle : m_aTitles)
      ret.addContent (new eu.toop.edm.jaxb.dcterms.ObjectFactory ().createTitle (sTitle));
    if (m_aDistribution != null)
    {
      if (true)
        ret.addContent (new JAXBElement <> (new QName ("http://data.europa.eu/r5r/", "distribution"),
                                            CCCEVDocumentReferenceType.class,
                                            null,
                                            m_aDistribution));
      else
        ret.addContent (new eu.toop.edm.jaxb.dcatap.ObjectFactory ().createDistribution (m_aDistribution));
    }
    if (m_aCreator != null)
      ret.addContent (new eu.toop.edm.jaxb.dcterms.ObjectFactory ().createCreator (m_aCreator));
    if (StringHelper.hasText (m_sID))
      ret.addContent (new eu.toop.edm.jaxb.dcterms.ObjectFactory ().createIdentifier (m_sID));
    if (m_aIssuedDT != null)
      ret.addContent (new eu.toop.edm.jaxb.dcterms.ObjectFactory ().createIssued (PDTXMLConverter.getXMLCalendar (m_aIssuedDT)));
    if (StringHelper.hasText (m_sLanguage))
      ret.addContent (new eu.toop.edm.jaxb.dcterms.ObjectFactory ().createLanguage (m_sLanguage));
    if (m_aLastModifiedDT != null)
      ret.addContent (new eu.toop.edm.jaxb.dcterms.ObjectFactory ().createModified (PDTXMLConverter.getXMLCalendar (m_aLastModifiedDT)));
    if (m_aValidFrom != null || m_aValidTo != null)
    {
      final DCPeriodOfTimeType aPeriodOfType = new DCPeriodOfTimeType ();
      if (m_aValidFrom != null)
        aPeriodOfType.addStartDate (PDTXMLConverter.getXMLCalendarDate (m_aValidFrom));
      if (m_aValidTo != null)
        aPeriodOfType.addEndDate (PDTXMLConverter.getXMLCalendarDate (m_aValidTo));
      ret.addContent (new eu.toop.edm.jaxb.dcterms.ObjectFactory ().createTemporal (aPeriodOfType));
    }
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
    private String m_sID;
    private LocalDateTime m_aIssuedDT;
    private String m_sLanguage;
    private LocalDateTime m_aLastModifiedDT;
    private LocalDate m_aValidFrom;
    private LocalDate m_aValidTo;

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
    public Builder id (@Nullable final String s)
    {
      m_sID = s;
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
    public DatasetPojo build ()
    {
      return new DatasetPojo (m_aDescriptions,
                              m_aTitles,
                              m_aDistribution,
                              m_aCreator,
                              m_sID,
                              m_aIssuedDT,
                              m_sLanguage,
                              m_aLastModifiedDT,
                              m_aValidFrom,
                              m_aValidTo);
    }
  }
}
