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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.CommonsLinkedHashMap;
import com.helger.commons.collection.impl.CommonsLinkedHashSet;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.collection.impl.ICommonsOrderedMap;
import com.helger.commons.collection.impl.ICommonsOrderedSet;
import com.helger.commons.string.StringHelper;

import eu.toop.edm.slot.ISlotProvider;
import eu.toop.edm.slot.SlotSpecificationIdentifier;
import eu.toop.regrep.ERegRepResponseStatus;
import eu.toop.regrep.RegRepHelper;
import eu.toop.regrep.query.QueryResponse;
import eu.toop.regrep.rs.RegistryExceptionType;

/**
 * A simple builder to create valid TOOP Error responses for both "concept
 * queries" and for "document queries". See {@link #builder()}.
 *
 * @author Philip Helger
 */
public class EDMErrorCreator
{
  private static final ICommonsOrderedSet <String> TOP_LEVEL_SLOTS = new CommonsLinkedHashSet <> (SlotSpecificationIdentifier.NAME);

  private final ERegRepResponseStatus m_eResponseStatus;
  private final String m_sRequestID;
  private final ICommonsOrderedMap <String, ISlotProvider> m_aProviders = new CommonsLinkedHashMap <> ();

  private EDMErrorCreator (@Nonnull final ERegRepResponseStatus eResponseStatus,
                           @Nonnull @Nonempty final String sRequestID,
                           @Nonnull final ICommonsList <ISlotProvider> aProviders)
  {
    ValueEnforcer.notNull (eResponseStatus, "ResponseStatus");
    ValueEnforcer.notEmpty (sRequestID, "RequestID");
    ValueEnforcer.noNullValue (aProviders, "Providers");

    m_eResponseStatus = eResponseStatus;
    m_sRequestID = sRequestID;

    for (final ISlotProvider aItem : aProviders)
    {
      final String sName = aItem.getName ();
      if (m_aProviders.containsKey (sName))
        throw new IllegalArgumentException ("A slot provider for name '" + sName + "' is already present");
      m_aProviders.put (sName, aItem);
    }
  }

  @Nonnull
  QueryResponse createQueryResponse (@Nonnull @Nonempty final ICommonsList <RegistryExceptionType> aExceptions)
  {
    final QueryResponse ret = RegRepHelper.createEmptyQueryResponse (m_eResponseStatus);
    ret.setRequestId (m_sRequestID);

    // All top-level slots outside of object list
    for (final String sHeader : TOP_LEVEL_SLOTS)
    {
      final ISlotProvider aSP = m_aProviders.get (sHeader);
      if (aSP != null)
        ret.addSlot (aSP.createSlot ());
    }

    ret.getException ().addAll (aExceptions);

    return ret;
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
    public QueryResponse build ()
    {
      checkConsistency ();

      final ICommonsList <ISlotProvider> aSlots = new CommonsArrayList <> ();
      if (m_sSpecificationIdentifier != null)
        aSlots.add (new SlotSpecificationIdentifier (m_sSpecificationIdentifier));

      // Exceptions
      return new EDMErrorCreator (m_eResponseStatus, m_sRequestID, aSlots).createQueryResponse (m_aExceptions);
    }
  }
}
