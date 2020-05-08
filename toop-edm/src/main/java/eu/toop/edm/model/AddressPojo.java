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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.string.StringHelper;

import eu.toop.edm.jaxb.cv.cac.AddressType;
import eu.toop.edm.jaxb.w3.cv.ac.CoreAddressType;
import eu.toop.edm.jaxb.w3.cv.bc.AddressAdminUnitLocationOneType;
import eu.toop.edm.jaxb.w3.cv.bc.AddressFullAddressType;
import eu.toop.edm.jaxb.w3.cv.bc.AddressLocatorDesignatorType;
import eu.toop.edm.jaxb.w3.cv.bc.AddressPostCodeType;
import eu.toop.edm.jaxb.w3.cv.bc.AddressPostNameType;
import eu.toop.edm.jaxb.w3.cv.bc.AddressThoroughfareType;

/**
 * Represents the needed extract of an Address.
 *
 * @author Philip Helger
 */
public class AddressPojo
{
  private final String m_sFullAddress;
  private final String m_sStreetName;
  private final String m_sBuildingNumber;
  private final String m_sTown;
  private final String m_sPostalCode;
  private final String m_sCountryCode;

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
    m_sPostalCode = sPostalCode;
    m_sCountryCode = sCountryCode;
  }

  @Nullable
  public String getFullAddress ()
  {
    return m_sFullAddress;
  }

  @Nullable
  public String getStreetName ()
  {
    return m_sStreetName;
  }

  @Nullable
  public String getBuildingNumber ()
  {
    return m_sBuildingNumber;
  }

  @Nullable
  public String getTown ()
  {
    return m_sTown;
  }

  @Nullable
  public String getPostalCode ()
  {
    return m_sPostalCode;
  }

  @Nullable
  public String getCountryCode ()
  {
    return m_sCountryCode;
  }

  @Nullable
  public CoreAddressType getAsCoreAddress ()
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

  @Nullable
  public AddressType getAsAgentAddress ()
  {
    boolean bAny = false;
    final AddressType aAddress = new AddressType ();
    if (StringHelper.hasText (m_sFullAddress))
    {
      aAddress.setFullAddress (m_sFullAddress);
      bAny = true;
    }
    if (StringHelper.hasText (m_sStreetName))
    {
      aAddress.setThoroughfare (m_sStreetName);
      bAny = true;
    }
    if (StringHelper.hasText (m_sBuildingNumber))
    {
      aAddress.setLocatorDesignator (m_sBuildingNumber);
      bAny = true;
    }
    if (StringHelper.hasText (m_sTown))
    {
      aAddress.setPostName (m_sTown);
      bAny = true;
    }
    if (StringHelper.hasText (m_sPostalCode))
    {
      aAddress.setPostCode (m_sPostalCode);
      bAny = true;
    }
    if (StringHelper.hasText (m_sCountryCode))
    {
      aAddress.setAdminUnitLevel1 (m_sCountryCode);
      bAny = true;
    }
    return bAny ? aAddress : null;
  }

  @Nonnull
  public static Builder builder ()
  {
    return new Builder ();
  }

  @Nonnull
  public static Builder builder (@Nullable final CoreAddressType a)
  {
    final Builder ret = new Builder ();
    if (a != null)
    {
      if (a.hasAddressFullAddressEntries ())
        ret.fullAddress (a.getAddressFullAddressAtIndex (0).getValue ());
      if (a.hasAddressThoroughfareEntries ())
        ret.streetName (a.getAddressThoroughfareAtIndex (0).getValue ());
      if (a.hasAddressLocatorDesignatorEntries ())
        ret.buildingNumber (a.getAddressLocatorDesignatorAtIndex (0).getValue ());
      if (a.hasAddressPostNameEntries ())
        ret.town (a.getAddressPostNameAtIndex (0).getValue ());
      if (a.hasAddressPostCodeEntries ())
        ret.postalCode (a.getAddressPostCodeAtIndex (0).getValue ());
      if (a.hasAddressAdminUnitLocationOneEntries ())
        ret.countryCode (a.getAddressAdminUnitLocationOneAtIndex (0).getValue ());
    }
    return ret;
  }

  @Nonnull
  public static Builder builder (@Nullable final AddressType a)
  {
    final Builder ret = new Builder ();
    if (a != null)
    {
      ret.fullAddress (a.getFullAddress ())
         .streetName (a.getThoroughfare ())
         .buildingNumber (a.getLocatorDesignator ())
         .town (a.getPostName ())
         .postalCode (a.getPostCode ())
         .countryCode (a.getAdminUnitLevel1 ());
    }
    return ret;
  }

  public static class Builder
  {
    private String m_sFullAddress;
    private String m_sStreetName;
    private String m_sBuildingNumber;
    private String m_sTown;
    private String m_sPostalCode;
    private String m_sCountryCode;

    public Builder ()
    {}

    @Nonnull
    public Builder fullAddress (@Nullable final String s)
    {
      m_sFullAddress = s;
      return this;
    }

    @Nonnull
    public Builder streetName (@Nullable final String s)
    {
      m_sStreetName = s;
      return this;
    }

    @Nonnull
    public Builder buildingNumber (@Nullable final String s)
    {
      m_sBuildingNumber = s;
      return this;
    }

    @Nonnull
    public Builder town (@Nullable final String s)
    {
      m_sTown = s;
      return this;
    }

    @Nonnull
    public Builder postalCode (@Nullable final String s)
    {
      m_sPostalCode = s;
      return this;
    }

    @Nonnull
    public Builder countryCode (@Nullable final String s)
    {
      m_sCountryCode = s;
      return this;
    }

    @Nonnull
    public AddressPojo build ()
    {
      return new AddressPojo (m_sFullAddress, m_sStreetName, m_sBuildingNumber, m_sTown, m_sPostalCode, m_sCountryCode);
    }
  }
}
