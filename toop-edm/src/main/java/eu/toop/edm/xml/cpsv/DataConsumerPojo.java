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
package eu.toop.edm.xml.cpsv;

import javax.annotation.Nonnull;

import com.helger.commons.string.StringHelper;

import eu.toop.edm.jaxb.cv.agent.AgentType;
import eu.toop.edm.jaxb.w3.cv.ac.CvidentifierType;
import eu.toop.edm.jaxb.w3.cv.bc.IdentifierTypeType;

public class DataConsumerPojo
{
  private final String m_sID;
  private final String m_sIDType;
  private final String m_sName;
  private final String m_sFullAddress;
  private final String m_sStreetName;
  private final String m_sBuildingNumber;
  private final String m_sTown;
  private final String m_sPostalCode;
  private final String m_sCountryCode;

  public DataConsumerPojo (final String sID,
                           final String sIDType,
                           final String sName,
                           final String sFullAddress,
                           final String sStreetName,
                           final String sBuildingNumber,
                           final String sTown,
                           final String sPostalCode,
                           final String sCountryCode)
  {
    m_sID = sID;
    m_sIDType = sIDType;
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
      final CvidentifierType aAgentID = new CvidentifierType ();
      final IdentifierType aID = new IdentifierType ();
      aID.setValue (m_sID);
      aAgentID.setIdentifier (aID);
      if (StringHelper.hasText (m_sIDType))
      {
        final IdentifierTypeType aIdentifierType = new IdentifierTypeType ();
        aIdentifierType.setValue (m_sIDType);
        aAgentID.setIdentifierType (aIdentifierType);
      }
      ret.setAgentID (aAgentID);
    }
    ret.setAgentName (m_sName);
    if (StringHelper.hasText (m_sFullAddress))
    {
      final AddressType aAddress = new AddressType ();
      aAddress.setAddressFullAddress (m_sFullAddress);
      aAddress.setAddressThoroughfare (m_sStreetName);
      aAddress.setAddressLocatorDesignator (m_sBuildingNumber);
      aAddress.setAddressPostName (m_sTown);
      aAddress.setAddressPostCode (m_sPostalCode);
      aAddress.setAddressAdminUnitL1 (m_sCountryCode);
      ret.setAgentHasAddress (aAddress);
    }
    return ret;
  }
}
