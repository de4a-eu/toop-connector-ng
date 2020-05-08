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

import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.ToStringGenerator;

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

  /**
   * @return The full address line. May be <code>null</code>.
   */
  @Nullable
  public final String getFullAddress ()
  {
    return m_sFullAddress;
  }

  /**
   * @return The street name of the address. May be <code>null</code>.
   */
  @Nullable
  public final String getStreetName ()
  {
    return m_sStreetName;
  }

  /**
   * @return The building number of the address. May be <code>null</code>.
   */
  @Nullable
  public final String getBuildingNumber ()
  {
    return m_sBuildingNumber;
  }

  /**
   * @return The town or city name of the address. May be <code>null</code>.
   */
  @Nullable
  public final String getTown ()
  {
    return m_sTown;
  }

  /**
   * @return The postal code of the address. May be <code>null</code>.
   */
  @Nullable
  public final String getPostalCode ()
  {
    return m_sPostalCode;
  }

  /**
   * @return The country code of the address. May be <code>null</code>. Note:
   *         that is not checked against any rules.
   */
  @Nullable
  public final String getCountryCode ()
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
  public eu.toop.edm.jaxb.cv.cac.AddressType getAsAgentAddress ()
  {
    boolean bAny = false;
    final eu.toop.edm.jaxb.cv.cac.AddressType aAddress = new eu.toop.edm.jaxb.cv.cac.AddressType ();
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

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final AddressPojo rhs = (AddressPojo) o;
    return EqualsHelper.equals (m_sFullAddress, rhs.m_sFullAddress) &&
           EqualsHelper.equals (m_sStreetName, rhs.m_sStreetName) &&
           EqualsHelper.equals (m_sBuildingNumber, rhs.m_sBuildingNumber) &&
           EqualsHelper.equals (m_sTown, rhs.m_sTown) &&
           EqualsHelper.equals (m_sPostalCode, rhs.m_sPostalCode) &&
           EqualsHelper.equals (m_sCountryCode, rhs.m_sCountryCode);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sFullAddress)
                                       .append (m_sStreetName)
                                       .append (m_sBuildingNumber)
                                       .append (m_sTown)
                                       .append (m_sPostalCode)
                                       .append (m_sCountryCode)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("FullAddress", m_sFullAddress)
                                       .append ("StreetName", m_sStreetName)
                                       .append ("BuildingNumber", m_sBuildingNumber)
                                       .append ("Town", m_sTown)
                                       .append ("PostalCode", m_sPostalCode)
                                       .append ("CountryCode", m_sCountryCode)
                                       .getToString ();
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
  public static Builder builder (@Nullable final eu.toop.edm.jaxb.w3.locn.AddressType a)
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
