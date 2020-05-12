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
package eu.toop.edm;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.w3c.dom.Node;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.CommonsLinkedHashMap;
import com.helger.commons.collection.impl.CommonsLinkedHashSet;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.collection.impl.ICommonsOrderedMap;
import com.helger.commons.collection.impl.ICommonsOrderedSet;
import com.helger.commons.string.StringHelper;

import eu.toop.edm.error.EDMExceptionBuilder;
import eu.toop.edm.jaxb.cv.agent.AgentType;
import eu.toop.edm.model.AgentPojo;
import eu.toop.edm.slot.ISlotProvider;
import eu.toop.edm.slot.SlotErrorProvider;
import eu.toop.edm.slot.SlotSpecificationIdentifier;
import eu.toop.edm.xml.IJAXBVersatileReader;
import eu.toop.edm.xml.IVersatileWriter;
import eu.toop.edm.xml.JAXBVersatileReader;
import eu.toop.edm.xml.JAXBVersatileWriter;
import eu.toop.edm.xml.cagv.AgentMarshaller;
import eu.toop.regrep.ERegRepResponseStatus;
import eu.toop.regrep.RegRep4Reader;
import eu.toop.regrep.RegRep4Writer;
import eu.toop.regrep.RegRepHelper;
import eu.toop.regrep.query.QueryResponse;
import eu.toop.regrep.rim.AnyValueType;
import eu.toop.regrep.rim.SlotType;
import eu.toop.regrep.rim.StringValueType;
import eu.toop.regrep.rim.ValueType;
import eu.toop.regrep.rs.RegistryExceptionType;

/**
 * A simple builder to create valid TOOP Error responses for both "concept
 * queries" and for "document queries". See {@link #builder()}.
 *
 * @author Philip Helger
 */
public class EDMErrorResponse
{
  private static final ICommonsOrderedSet <String> TOP_LEVEL_SLOTS = new CommonsLinkedHashSet <> (SlotSpecificationIdentifier.NAME,
                                                                                                  SlotErrorProvider.NAME);

  private final ERegRepResponseStatus m_eResponseStatus;
  private final String m_sRequestID;
  private final String m_sSpecificationIdentifier;
  private final AgentType m_aErrorProvider;
  private final ICommonsList <RegistryExceptionType> m_aExceptions = new CommonsArrayList <> ();

  public EDMErrorResponse (@Nonnull final ERegRepResponseStatus eResponseStatus,
                           @Nonnull @Nonempty final String sRequestID,
                           @Nonnull @Nonempty final String sSpecificationIdentifier,
                           @Nullable final AgentType aErrorProvider,
                           @Nonnull @Nonempty final ICommonsList <RegistryExceptionType> aExceptions)
  {
    ValueEnforcer.notNull (eResponseStatus, "ResponseStatus");
    ValueEnforcer.notEmpty (sRequestID, "RequestID");
    ValueEnforcer.notEmpty (sSpecificationIdentifier, "SpecificationIdentifier");
    ValueEnforcer.notEmptyNoNullValue (aExceptions, "Exceptions");

    m_eResponseStatus = eResponseStatus;
    m_sRequestID = sRequestID;
    m_sSpecificationIdentifier = sSpecificationIdentifier;
    m_aErrorProvider = aErrorProvider;
    m_aExceptions.addAll (aExceptions);
  }

  @Nonnull
  public final ERegRepResponseStatus getResponseStatus ()
  {
    return m_eResponseStatus;
  }

  @Nonnull
  @Nonempty
  public final String getRequestID ()
  {
    return m_sRequestID;
  }

  @Nonnull
  @Nonempty
  public final String getSpecificationIdentifier ()
  {
    return m_sSpecificationIdentifier;
  }

  @Nullable
  public final AgentType getErrorProvider ()
  {
    return m_aErrorProvider;
  }

  @Nonnull
  @Nonempty
  public final List <RegistryExceptionType> exceptions ()
  {
    return m_aExceptions;
  }

  @Nonnull
  @Nonempty
  @ReturnsMutableCopy
  public final List <RegistryExceptionType> getAllExceptions ()
  {
    return m_aExceptions.getClone ();
  }

  @Nonnull
  private QueryResponse _createQueryResponse (@Nonnull final ICommonsList <ISlotProvider> aProviders)
  {
    final ICommonsOrderedMap <String, ISlotProvider> aProviderMap = new CommonsLinkedHashMap <> ();
    for (final ISlotProvider aItem : aProviders)
    {
      final String sName = aItem.getName ();
      if (aProviderMap.containsKey (sName))
        throw new IllegalArgumentException ("A slot provider for name '" + sName + "' is already present");
      aProviderMap.put (sName, aItem);
    }

    final QueryResponse ret = RegRepHelper.createEmptyQueryResponse (m_eResponseStatus);
    ret.setRequestId (m_sRequestID);

    // All top-level slots outside of object list
    for (final String sHeader : TOP_LEVEL_SLOTS)
    {
      final ISlotProvider aSP = aProviderMap.get (sHeader);
      if (aSP != null)
        ret.addSlot (aSP.createSlot ());
    }

    ret.getException ().addAll (m_aExceptions);

    return ret;
  }

  @Nonnull
  public QueryResponse getAsErrorResponse ()
  {
    final ICommonsList <ISlotProvider> aSlots = new CommonsArrayList <> ();
    if (m_sSpecificationIdentifier != null)
      aSlots.add (new SlotSpecificationIdentifier (m_sSpecificationIdentifier));
    if (m_aErrorProvider != null)
      aSlots.add (new SlotErrorProvider (m_aErrorProvider));

    // Exceptions
    return _createQueryResponse (aSlots);
  }

  @Nonnull
  public IVersatileWriter <QueryResponse> getWriter ()
  {
    return new JAXBVersatileWriter <> (getAsErrorResponse (), RegRep4Writer.queryResponse ().setFormattedOutput (true));
  }

  @Nonnull
  public static IJAXBVersatileReader <EDMErrorResponse> getReader ()
  {
    return new JAXBVersatileReader <> (RegRep4Reader.queryResponse (), EDMErrorResponse::create);
  }

  @Nonnull
  public static Builder builder ()
  {
    // Use the default specification identifier
    // Failure by default
    return new Builder ().specificationIdentifier (CToopEDM.SPECIFICATION_IDENTIFIER_TOOP_EDM_V20)
                         .responseStatus (ERegRepResponseStatus.FAILURE);
  }

  public static class Builder
  {
    private ERegRepResponseStatus m_eResponseStatus;
    private String m_sRequestID;
    private String m_sSpecificationIdentifier;
    private AgentType m_aErrorProvider;
    private final ICommonsList <RegistryExceptionType> m_aExceptions = new CommonsArrayList <> ();

    public Builder ()
    {}

    @Nonnull
    public Builder responseStatus (@Nullable final ERegRepResponseStatus e)
    {
      m_eResponseStatus = e;
      return this;
    }

    @Nonnull
    public Builder requestID (@Nullable final String s)
    {
      m_sRequestID = s;
      return this;
    }

    @Nonnull
    public Builder specificationIdentifier (@Nullable final String s)
    {
      m_sSpecificationIdentifier = s;
      return this;
    }

    @Nonnull
    public Builder errorProvider (@Nullable final AgentPojo.Builder a)
    {
      return errorProvider (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder errorProvider (@Nullable final AgentPojo a)
    {
      return errorProvider (a == null ? null : a.getAsAgent ());
    }

    @Nonnull
    public Builder errorProvider (@Nullable final AgentType a)
    {
      m_aErrorProvider = a;
      return this;
    }

    @Nonnull
    public Builder addException (@Nullable final EDMExceptionBuilder a)
    {
      return addException (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder addException (@Nullable final RegistryExceptionType a)
    {
      if (a != null)
        m_aExceptions.add (a);
      return this;
    }

    @Nonnull
    public Builder exception (@Nullable final EDMExceptionBuilder a)
    {
      return exception (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder exception (@Nullable final RegistryExceptionType a)
    {
      if (a != null)
        m_aExceptions.set (a);
      else
        m_aExceptions.clear ();
      return this;
    }

    @Nonnull
    public Builder exceptions (@Nullable final RegistryExceptionType... a)
    {
      m_aExceptions.setAll (a);
      return this;
    }

    @Nonnull
    public Builder exceptions (@Nullable final Iterable <? extends RegistryExceptionType> a)
    {
      m_aExceptions.setAll (a);
      return this;
    }

    public void checkConsistency ()
    {
      if (m_eResponseStatus == null)
        throw new IllegalStateException ("Response Status must be present");
      if (StringHelper.hasNoText (m_sRequestID))
        throw new IllegalStateException ("Request ID must be present");
      if (StringHelper.hasNoText (m_sSpecificationIdentifier))
        throw new IllegalStateException ("SpecificationIdentifier must be present");
      if (m_aExceptions.isEmpty ())
        throw new IllegalStateException ("At least one Exception must be present");
    }

    @Nonnull
    public EDMErrorResponse build ()
    {
      checkConsistency ();

      return new EDMErrorResponse (m_eResponseStatus,
                                   m_sRequestID,
                                   m_sSpecificationIdentifier,
                                   m_aErrorProvider,
                                   m_aExceptions);
    }
  }

  private static void _applySlots (@Nonnull final SlotType aSlot, @Nonnull final EDMErrorResponse.Builder aBuilder)
  {
    final String sName = aSlot.getName ();
    final ValueType aSlotValue = aSlot.getSlotValue ();
    switch (sName)
    {
      case SlotSpecificationIdentifier.NAME:
        if (aSlotValue instanceof StringValueType)
        {
          final String sValue = ((StringValueType) aSlotValue).getValue ();
          aBuilder.specificationIdentifier (sValue);
        }
        break;
      case SlotErrorProvider.NAME:
        if (aSlotValue instanceof AnyValueType)
        {
          final Node aAny = (Node) ((AnyValueType) aSlotValue).getAny ();
          aBuilder.errorProvider (AgentPojo.builder (new AgentMarshaller ().read (aAny)));
        }
        break;
      default:
        throw new IllegalStateException ("Slot is not defined: " + sName);
    }
  }

  @Nonnull
  public static EDMErrorResponse create (@Nonnull final QueryResponse aQueryResponse)
  {
    final EDMErrorResponse.Builder aBuilder = EDMErrorResponse.builder ()
                                                              .responseStatus (ERegRepResponseStatus.getFromIDOrNull (aQueryResponse.getStatus ()))
                                                              .requestID (aQueryResponse.getRequestId ());

    for (final SlotType s : aQueryResponse.getSlot ())
      _applySlots (s, aBuilder);

    aBuilder.exceptions (aQueryResponse.getException ());
    return aBuilder.build ();
  }
}
