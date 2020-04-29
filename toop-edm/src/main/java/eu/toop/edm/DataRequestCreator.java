package eu.toop.edm;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.CommonsLinkedHashMap;
import com.helger.commons.collection.impl.CommonsLinkedHashSet;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.collection.impl.ICommonsOrderedMap;
import com.helger.commons.collection.impl.ICommonsOrderedSet;
import com.helger.commons.datetime.PDTFactory;

import eu.toop.edm.jaxb.cccev.CCCEVRequirementType;
import eu.toop.edm.jaxb.cv.agent.AgentType;
import eu.toop.edm.jaxb.w3.cv.ac.CoreBusinessType;
import eu.toop.edm.jaxb.w3.cv.ac.CorePersonType;
import eu.toop.edm.slot.ISlotProvider;
import eu.toop.edm.slot.SlotAuthorizedRepresentative;
import eu.toop.edm.slot.SlotConsentToken;
import eu.toop.edm.slot.SlotDataConsumer;
import eu.toop.edm.slot.SlotDataSubjectLegalPerson;
import eu.toop.edm.slot.SlotDataSubjectNaturalPerson;
import eu.toop.edm.slot.SlotDatasetIdentifier;
import eu.toop.edm.slot.SlotFullfillingRequirement;
import eu.toop.edm.slot.SlotIssueDateTime;
import eu.toop.edm.slot.SlotProcedure;
import eu.toop.edm.xml.cagv.DataConsumerPojo;
import eu.toop.edm.xml.cv.BusinessPojo;
import eu.toop.edm.xml.cv.PersonPojo;
import eu.toop.regrep.RegRepHelper;
import eu.toop.regrep.query.QueryRequest;
import eu.toop.regrep.rim.InternationalStringType;
import eu.toop.regrep.rim.LocalizedStringType;
import eu.toop.regrep.rim.QueryType;

/**
 * A simple builder to create valid TOOP Request for both "concept queries" and
 * for "document queries".
 *
 * @author Philip Helger
 */
public class DataRequestCreator
{
  private static final ICommonsOrderedSet <String> TOP_LEVEL_SLOTS = new CommonsLinkedHashSet <> (SlotIssueDateTime.NAME,
                                                                                                  SlotProcedure.NAME,
                                                                                                  SlotFullfillingRequirement.NAME,
                                                                                                  SlotConsentToken.NAME,
                                                                                                  SlotDatasetIdentifier.NAME,
                                                                                                  SlotDataConsumer.NAME);

  private final EQueryDefinitionType m_eQueryDefinition;
  private final ICommonsOrderedMap <String, ISlotProvider> m_aProviders = new CommonsLinkedHashMap <> ();

  /**
   * Constructor
   *
   * @param eQueryDefinition
   *        Query definition type to be used. May not be <code>null</code>.
   * @param aProviders
   *        All slot providers to be added. May not be <code>null</code>.
   */
  private DataRequestCreator (@Nonnull final EQueryDefinitionType eQueryDefinition,
                              @Nonnull final ICommonsList <ISlotProvider> aProviders)
  {
    ValueEnforcer.notNull (eQueryDefinition, "QueryDefinition");
    ValueEnforcer.notEmptyNoNullValue (aProviders, "Providers");

    m_eQueryDefinition = eQueryDefinition;
    for (final ISlotProvider aItem : aProviders)
    {
      final String sName = aItem.getName ();
      if (m_aProviders.containsKey (sName))
        throw new IllegalArgumentException ("A slot provider for name '" + sName + "' is already present");
      m_aProviders.put (sName, aItem);
    }
  }

  @Nonnull
  QueryRequest createQueryRequest ()
  {
    final QueryRequest ret = RegRepHelper.createEmptyQueryRequest ();

    // All slots outside of query
    for (final String sTopLevel : TOP_LEVEL_SLOTS)
    {
      final ISlotProvider aSP = m_aProviders.get (sTopLevel);
      if (aSP != null)
        ret.addSlot (aSP.createSlot ());
    }

    final QueryType aQuery = new QueryType ();
    aQuery.setQueryDefinition (m_eQueryDefinition.getAttrValue ());

    // All slots inside of query
    for (final Map.Entry <String, ISlotProvider> aEntry : m_aProviders.entrySet ())
      if (!TOP_LEVEL_SLOTS.contains (aEntry.getKey ()))
        aQuery.addSlot (aEntry.getValue ().createSlot ());

    ret.setQuery (aQuery);

    return ret;
  }

  @Nonnull
  public static Builder builderCeoncept ()
  {
    return new Builder ().setQueryDefinition (EQueryDefinitionType.CONCEPT);
  }

  @Nonnull
  public static Builder builderDocument ()
  {
    return new Builder ().setQueryDefinition (EQueryDefinitionType.DOCUMENT);
  }

  public static class Builder
  {
    private EQueryDefinitionType m_eQueryDefinition;
    private LocalDateTime m_aIssueDateTime;
    private InternationalStringType m_aProcedure;
    private CCCEVRequirementType m_aFullfillingRequirement;
    private AgentType m_aDataConsumer;
    private String m_sConsentToken;
    private String m_sDatasetIdentifier;
    private CoreBusinessType m_aDataSubjectLegalPerson;
    private CorePersonType m_aDataSubjectNaturalPerson;
    private CorePersonType m_aAuthorizedRepresentative;

    public Builder ()
    {}

    @Nonnull
    public Builder setQueryDefinition (@Nullable final EQueryDefinitionType eQueryDefinition)
    {
      m_eQueryDefinition = eQueryDefinition;
      return this;
    }

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
    public Builder setProcedure (@Nullable final Map <Locale, String> aMap)
    {
      return setProcedure (aMap == null ? null : RegRepHelper.createInternationalStringType (aMap));
    }

    @Nonnull
    public Builder setProcedure (@Nullable final LocalizedStringType... aArray)
    {
      return setProcedure (aArray == null ? null : RegRepHelper.createInternationalStringType (aArray));
    }

    @Nonnull
    public Builder setProcedure (@Nullable final InternationalStringType aProcedure)
    {
      m_aProcedure = aProcedure;
      return this;
    }

    @Nonnull
    public Builder setFullfillingRequirement (@Nullable final CCCEVRequirementType aFullfillingRequirement)
    {
      m_aFullfillingRequirement = aFullfillingRequirement;
      return this;
    }

    @Nonnull
    public Builder setConsentToken (@Nullable final String sConsentToken)
    {
      m_sConsentToken = sConsentToken;
      return this;
    }

    @Nonnull
    public Builder setDatasetIdentifier (@Nullable final String sDataSetIdentifier)
    {
      m_sDatasetIdentifier = sDataSetIdentifier;
      return this;
    }

    @Nonnull
    public Builder setDataConsumer (@Nullable final DataConsumerPojo aDC)
    {
      return setDataConsumer (aDC == null ? null : aDC.getAsAgent ());
    }

    @Nonnull
    public Builder setDataConsumer (@Nullable final AgentType aAgent)
    {
      m_aDataConsumer = aAgent;
      return this;
    }

    @Nonnull
    public Builder setDataSubject (@Nullable final BusinessPojo aBusiness)
    {
      return setDataSubject (aBusiness == null ? null : aBusiness.getAsBusiness ());
    }

    @Nonnull
    public Builder setDataSubject (@Nullable final CoreBusinessType aBusiness)
    {
      m_aDataSubjectLegalPerson = aBusiness;
      m_aDataSubjectNaturalPerson = null;
      return this;
    }

    @Nonnull
    public Builder setDataSubject (@Nullable final PersonPojo aPerson)
    {
      return setDataSubject (aPerson == null ? null : aPerson.getAsPerson ());
    }

    @Nonnull
    public Builder setDataSubject (@Nullable final CorePersonType aPerson)
    {
      m_aDataSubjectLegalPerson = null;
      m_aDataSubjectNaturalPerson = aPerson;
      return this;
    }

    @Nonnull
    public Builder setAuthorizedRepresentative (@Nullable final PersonPojo aPerson)
    {
      return setAuthorizedRepresentative (aPerson == null ? null : aPerson.getAsPerson ());
    }

    @Nonnull
    public Builder setAuthorizedRepresentative (@Nullable final CorePersonType aPerson)
    {
      m_aAuthorizedRepresentative = aPerson;
      return this;
    }

    public void checkConsistency ()
    {
      if (m_eQueryDefinition == null)
        throw new IllegalStateException ("Query Definition must be present");
      if (m_aIssueDateTime == null)
        throw new IllegalStateException ("Issue Date Time must be present");
      if (m_aDataConsumer == null)
        throw new IllegalStateException ("Cata Consumer must be present");
      if (m_aDataSubjectLegalPerson == null && m_aDataSubjectNaturalPerson == null)
        throw new IllegalStateException ("Data Subject must be present");
      if (m_aDataSubjectLegalPerson != null && m_aDataSubjectNaturalPerson != null)
        throw new IllegalStateException ("Data Subject MUST be either legal person OR natural person");
    }

    @Nonnull
    public QueryRequest build ()
    {
      checkConsistency ();

      final ICommonsList <ISlotProvider> x = new CommonsArrayList <> ();

      // Top-level slots
      if (m_aIssueDateTime != null)
        x.add (new SlotIssueDateTime (m_aIssueDateTime));
      if (m_aProcedure != null)
        x.add (new SlotProcedure (m_aProcedure));
      if (m_aFullfillingRequirement != null)
        x.add (new SlotFullfillingRequirement (m_aFullfillingRequirement));
      if (m_sConsentToken != null)
        x.add (new SlotConsentToken (m_sConsentToken));
      if (m_sDatasetIdentifier != null)
        x.add (new SlotDatasetIdentifier (m_sDatasetIdentifier));
      if (m_aDataConsumer != null)
        x.add (new SlotDataConsumer (m_aDataConsumer));

      // Commons Query slots
      if (m_aDataSubjectLegalPerson != null)
        x.add (new SlotDataSubjectLegalPerson (m_aDataSubjectLegalPerson));
      if (m_aDataSubjectNaturalPerson != null)
        x.add (new SlotDataSubjectNaturalPerson (m_aDataSubjectNaturalPerson));
      if (m_aAuthorizedRepresentative != null)
        x.add (new SlotAuthorizedRepresentative (m_aAuthorizedRepresentative));

      return new DataRequestCreator (m_eQueryDefinition, x).createQueryRequest ();
    }
  }
}
