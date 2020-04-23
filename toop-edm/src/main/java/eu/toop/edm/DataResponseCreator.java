package eu.toop.edm;

import java.time.LocalDateTime;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.CommonsLinkedHashMap;
import com.helger.commons.collection.impl.CommonsLinkedHashSet;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.collection.impl.ICommonsOrderedMap;
import com.helger.commons.collection.impl.ICommonsOrderedSet;
import com.helger.commons.datetime.PDTFactory;

import eu.toop.edm.jaxb.cpsv.helper.AgentType;
import eu.toop.edm.jaxb.w3.cv.business.CvbusinessType;
import eu.toop.edm.jaxb.w3.cv.person.CvpersonType;
import eu.toop.edm.regrep.ISlotProvider;
import eu.toop.edm.regrep.SlotConsentToken;
import eu.toop.edm.regrep.SlotDataConsumer;
import eu.toop.edm.regrep.SlotDataProvider;
import eu.toop.edm.regrep.SlotDataSetIdentifier;
import eu.toop.edm.regrep.SlotDataSubjectLegalPerson;
import eu.toop.edm.regrep.SlotDataSubjectNaturalPerson;
import eu.toop.edm.regrep.SlotIssueDateTime;
import eu.toop.regrep.RegRepHelper;
import eu.toop.regrep.query.QueryResponse;

public class DataResponseCreator
{
  private static final ICommonsOrderedSet <String> HEADER_SLOTS = new CommonsLinkedHashSet <> (SlotIssueDateTime.NAME,
                                                                                               SlotDataProvider.NAME);
  private final ICommonsOrderedMap <String, ISlotProvider> m_aProviders = new CommonsLinkedHashMap <> ();

  private DataResponseCreator (@Nonnull final ICommonsList <ISlotProvider> aProviders)
  {
    for (final ISlotProvider aItem : aProviders)
    {
      final String sName = aItem.getName ();
      if (m_aProviders.containsKey (sName))
        throw new IllegalArgumentException ("A slot provider for name '" + sName + "' is already present");
      m_aProviders.put (sName, aItem);
    }
  }

  @Nonnull
  QueryResponse createQueryResponse ()
  {
    final QueryResponse ret = RegRepHelper.createEmptyQueryResponse ();
    // All slots outside of query
    for (final String sHeader : HEADER_SLOTS)
    {
      final ISlotProvider aSP = m_aProviders.get (sHeader);
      if (aSP != null)
        ret.addSlot (aSP.createSlot ());
    }
    // All slots inside of query
    // TODO
    // for (final Map.Entry <String, ISlotProvider> aEntry :
    // m_aProviders.entrySet ())
    // if (!HEADER_SLOTS.contains (aEntry.getKey ()))
    // ret.getRegistryObjectList ().getQuery ().addSlot (aEntry.getValue
    // ().createSlot ());

    return ret;
  }

  public static Builder builder ()
  {
    return new Builder ();
  }

  public static class Builder
  {
    private LocalDateTime m_aIssueDateTime;
    private AgentType m_aDCAgent;
    private AgentType m_aDPAgent;
    private String m_sConsentToken;
    private String m_sDataSetIdentifier;
    private CvbusinessType m_aDSLegalPerson;
    private CvpersonType m_aDSNaturalPerson;

    public Builder ()
    {}

    @Nonnull
    public Builder setIssueDateTime (@Nullable final LocalDateTime aIssueDateTime)
    {
      m_aIssueDateTime = aIssueDateTime;
      return this;
    }

    @Nonnull
    public Builder setIssueDateTimeNow ()
    {

      return setIssueDateTime (PDTFactory.getCurrentLocalDateTime ());
    }

    @Nonnull
    public Builder setDataConsumer (@Nullable final AgentType aAgent)
    {
      m_aDCAgent = aAgent;
      return this;
    }

    @Nonnull
    public Builder setDataProvider (@Nullable final AgentType aAgent)
    {
      m_aDPAgent = aAgent;
      return this;
    }

    @Nonnull
    public Builder setConsentToken (@Nullable final String sConsentToken)
    {
      m_sConsentToken = sConsentToken;
      return this;
    }

    @Nonnull
    public Builder setDataSetIdentifier (@Nullable final String sDataSetIdentifier)
    {
      m_sDataSetIdentifier = sDataSetIdentifier;
      return this;
    }

    @Nonnull
    public Builder setDataSubject (@Nullable final CvbusinessType aBusiness)
    {
      m_aDSLegalPerson = aBusiness;
      m_aDSNaturalPerson = null;
      return this;
    }

    @Nonnull
    public Builder setDataSubject (@Nullable final CvpersonType aPerson)
    {
      m_aDSLegalPerson = null;
      m_aDSNaturalPerson = aPerson;
      return this;
    }

    @Nonnull
    public QueryResponse build ()
    {
      final ICommonsList <ISlotProvider> x = new CommonsArrayList <> ();
      if (m_aIssueDateTime != null)
        x.add (new SlotIssueDateTime (m_aIssueDateTime));
      if (m_aDCAgent != null)
        x.add (new SlotDataConsumer (m_aDCAgent));
      if (m_aDPAgent != null)
        x.add (new SlotDataProvider (m_aDPAgent));
      if (m_sConsentToken != null)
        x.add (new SlotConsentToken (m_sConsentToken));
      if (m_sDataSetIdentifier != null)
        x.add (new SlotDataSetIdentifier (m_sDataSetIdentifier));
      if (m_aDSLegalPerson != null)
        x.add (new SlotDataSubjectLegalPerson (m_aDSLegalPerson));
      else
        if (m_aDSNaturalPerson != null)
          x.add (new SlotDataSubjectNaturalPerson (m_aDSNaturalPerson));
      return new DataResponseCreator (x).createQueryResponse ();
    }
  }
}
