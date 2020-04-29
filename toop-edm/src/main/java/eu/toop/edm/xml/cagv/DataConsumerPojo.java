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
package eu.toop.edm.xml.cagv;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.string.StringHelper;

import eu.toop.edm.jaxb.cv.agent.AgentType;
import eu.toop.edm.jaxb.cv.agent.LocationType;
import eu.toop.edm.jaxb.cv.cac.AddressType;
import eu.toop.edm.jaxb.cv.cbc.IDType;
import eu.toop.edm.jaxb.cv.cbc.NameType;

public class DataConsumerPojo
{
  private final String m_sID;
  private final String m_sIDSchemeID;
  private final String m_sName;
  private final String m_sFullAddress;
  private final String m_sStreetName;
  private final String m_sBuildingNumber;
  private final String m_sTown;
  private final String m_sPostalCode;
  private final String m_sCountryCode;

  public DataConsumerPojo (@Nullable final String sID,
                           @Nullable final String sIDSchemeID,
                           @Nullable final String sName,
                           @Nullable final String sFullAddress,
                           @Nullable final String sStreetName,
                           @Nullable final String sBuildingNumber,
                           @Nullable final String sTown,
                           @Nullable final String sPostalCode,
                           @Nullable final String sCountryCode)
  {
    m_sID = sID;
    m_sIDSchemeID = sIDSchemeID;
    m_sName = sName;
    m_sFullAddress = sFullAddress;
    m_sStreetName = sStreetName;
    m_sBuildingNumber = sBuildingNumber;
    m_sTown = sTown;
    m_sPostalCode = sPostalCode;
    m_sCountryCode = sCountryCode;
  }

  @Nonnull
  public AgentType getAsAgent ()
  {
    final AgentType ret = new AgentType ();
    if (StringHelper.hasText (m_sID))
    {
      final IDType aID = new IDType ();
      aID.setValue (m_sID);
      aID.setSchemeID (m_sIDSchemeID);
      ret.addId (aID);
    }
    if (StringHelper.hasText (m_sName))
    {
      final NameType aName = new NameType ();
      aName.setValue (m_sName);
      ret.addName (aName);
    }
    if (StringHelper.hasText (m_sFullAddress) ||
        StringHelper.hasText (m_sStreetName) ||
        StringHelper.hasText (m_sBuildingNumber) ||
        StringHelper.hasText (m_sTown) ||
        StringHelper.hasText (m_sPostalCode) ||
        StringHelper.hasText (m_sCountryCode))
    {
      final LocationType aLocation = new LocationType ();
      final AddressType aAddress = new AddressType ();
      aAddress.setFullAddress (m_sFullAddress);
      aAddress.setThoroughfare (m_sStreetName);
      aAddress.setLocatorDesignator (m_sBuildingNumber);
      aAddress.setPostName (m_sTown);
      aAddress.setPostCode (m_sPostalCode);
      aAddress.setAdminUnitLevel1 (m_sCountryCode);
      aLocation.setAddress (aAddress);
      ret.addLocation (aLocation);
    }
    return ret;
  }

  @Nonnull
  public static DataConsumerPojo createMinimum ()
  {
    return new DataConsumerPojo (null, null, null, null, null, null, null, null, null);
  }
}
