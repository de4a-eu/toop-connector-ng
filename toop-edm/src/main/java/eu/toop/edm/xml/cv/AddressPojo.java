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
package eu.toop.edm.xml.cv;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.string.StringHelper;

import eu.toop.edm.jaxb.w3.cv.ac.CoreAddressType;
import eu.toop.edm.jaxb.w3.cv.bc.AddressAdminUnitLocationOneType;
import eu.toop.edm.jaxb.w3.cv.bc.AddressFullAddressType;
import eu.toop.edm.jaxb.w3.cv.bc.AddressLocatorDesignatorType;
import eu.toop.edm.jaxb.w3.cv.bc.AddressPostCodeType;
import eu.toop.edm.jaxb.w3.cv.bc.AddressPostNameType;
import eu.toop.edm.jaxb.w3.cv.bc.AddressThoroughfareType;

public class AddressPojo
{
  private final String m_sFullAddress;
  private final String m_sStreetName;
  private final String m_sBuildingNumber;
  private final String m_sTown;
  private final String m_sCountryCode;
  private final String m_sPostalCode;

  public AddressPojo (@Nullable final String sFullAddress,
                      @Nullable final String sStreetName,
                      @Nullable final String sBuildingNumber,
                      @Nullable final String sTown,
                      @Nullable final String sPostalCode,
                      @Nullable final String sCountryCode)
  {
    m_sFullAddress = sFullAddress;
    m_sStreetName = sStreetName;
    m_sBuildingNumber = sBuildingNumber;
    m_sTown = sTown;
    m_sCountryCode = sCountryCode;
    m_sPostalCode = sPostalCode;
  }

  @Nonnull
  public CoreAddressType getAsAddress ()
  {
    boolean bAny = false;
    final CoreAddressType aAddress = new CoreAddressType ();
    if (StringHelper.hasText (m_sFullAddress))
    {
      final AddressFullAddressType aFullAddress = new AddressFullAddressType ();
      aFullAddress.setValue (m_sFullAddress);
      aAddress.addAddressFullAddress (aFullAddress);
      bAny = true;
    }
    if (StringHelper.hasText (m_sStreetName))
    {
      final AddressThoroughfareType aThoroughfare = new AddressThoroughfareType ();
      aThoroughfare.setValue (m_sStreetName);
      aAddress.addAddressThoroughfare (aThoroughfare);
      bAny = true;
    }
    if (StringHelper.hasText (m_sBuildingNumber))
    {
      final AddressLocatorDesignatorType aLD = new AddressLocatorDesignatorType ();
      aLD.setValue (m_sBuildingNumber);
      aAddress.addAddressLocatorDesignator (aLD);
      bAny = true;
    }
    if (StringHelper.hasText (m_sTown))
    {
      final AddressPostNameType aPostName = new AddressPostNameType ();
      aPostName.setValue (m_sTown);
      aAddress.addAddressPostName (aPostName);
      bAny = true;
    }
    if (StringHelper.hasText (m_sPostalCode))
    {
      final AddressPostCodeType aPostCode = new AddressPostCodeType ();
      aPostCode.setValue (m_sPostalCode);
      aAddress.addAddressPostCode (aPostCode);
      bAny = true;
    }
    if (StringHelper.hasText (m_sCountryCode))
    {
      final AddressAdminUnitLocationOneType aAdmin1 = new AddressAdminUnitLocationOneType ();
      aAdmin1.setValue (m_sCountryCode);
      aAddress.addAddressAdminUnitLocationOne (aAdmin1);
      bAny = true;
    }
    return bAny ? aAddress : null;
  }

  @Nonnull
  public static AddressPojo createMinimum ()
  {
    return new AddressPojo (null, null, null, null, null, null);
  }
}
