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
package eu.toop.edm.cv;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.string.StringHelper;

import eu.toop.edm.jaxb.w3.cv.ac.CvaddressType;
import eu.toop.edm.jaxb.w3.cv.ac.CvidentifierType;
import eu.toop.edm.jaxb.w3.cv.bc.AdminunitFirstlineType;
import eu.toop.edm.jaxb.w3.cv.bc.FullCvaddressType;
import eu.toop.edm.jaxb.w3.cv.bc.IdentifierType;
import eu.toop.edm.jaxb.w3.cv.bc.IdentifierTypeType;
import eu.toop.edm.jaxb.w3.cv.bc.LegalNameType;
import eu.toop.edm.jaxb.w3.cv.bc.LocatorDesignatorType;
import eu.toop.edm.jaxb.w3.cv.bc.PostCodeType;
import eu.toop.edm.jaxb.w3.cv.bc.PostNameType;
import eu.toop.edm.jaxb.w3.cv.bc.ThoroughfareType;
import eu.toop.edm.jaxb.w3.cv.business.CvbusinessType;

public class BusinessPojo
{
  private final String m_sLegalName;
  private final String m_sLegalID;
  private final String m_sLegalIDType;
  private final String m_sID;
  private final String m_sIDType;
  private final String m_sFullAddress;
  private final String m_sStreetName;
  private final String m_sBuildingNumber;
  private final String m_sTown;
  private final String m_sPostalCode;
  private final String m_sCountryCode;

  public BusinessPojo (final String sLegalName,
                       final String sLegalID,
                       final String sLegalIDType,
                       final String sID,
                       final String sIDType,
                       final String sFullAddress,
                       final String sStreetName,
                       final String sBuildingNumber,
                       final String sTown,
                       final String sPostalCode,
                       final String sCountryCode)
  {
    m_sLegalName = sLegalName;
    m_sLegalID = sLegalID;
    m_sLegalIDType = sLegalIDType;
    m_sID = sID;
    m_sIDType = sIDType;
    m_sFullAddress = sFullAddress;
    m_sStreetName = sStreetName;
    m_sBuildingNumber = sBuildingNumber;
    m_sTown = sTown;
    m_sPostalCode = sPostalCode;
    m_sCountryCode = sCountryCode;
  }

  @Nullable
  public static CvaddressType createAddress (final String sFullAddress,
                                             final String sStreetName,
                                             final String sBuildingNumber,
                                             final String sTown,
                                             final String sPostalCode,
                                             final String sCountryCode)
  {
    boolean bAny = false;
    final CvaddressType aAddress = new CvaddressType ();
    if (StringHelper.hasText (sFullAddress))
    {
      final FullCvaddressType aFullAddress = new FullCvaddressType ();
      aFullAddress.setValue (sFullAddress);
      aAddress.addFullCvaddress (aFullAddress);
      bAny = true;
    }
    if (StringHelper.hasText (sStreetName))
    {
      final ThoroughfareType aThoroughfare = new ThoroughfareType ();
      aThoroughfare.setValue (sStreetName);
      aAddress.addThoroughfare (aThoroughfare);
      bAny = true;
    }
    if (StringHelper.hasText (sBuildingNumber))
    {
      final LocatorDesignatorType aLD = new LocatorDesignatorType ();
      aLD.setValue (sBuildingNumber);
      aAddress.addLocatorDesignator (aLD);
      bAny = true;
    }
    if (StringHelper.hasText (sTown))
    {
      final PostNameType aPostName = new PostNameType ();
      aPostName.setValue (sTown);
      aAddress.addPostName (aPostName);
      bAny = true;
    }
    if (StringHelper.hasText (sPostalCode))
    {
      final PostCodeType aPostCode = new PostCodeType ();
      aPostCode.setValue (sPostalCode);
      aAddress.addPostCode (aPostCode);
      bAny = true;
    }
    if (StringHelper.hasText (sCountryCode))
    {
      final AdminunitFirstlineType aAdmin1 = new AdminunitFirstlineType ();
      aAdmin1.setValue (sCountryCode);
      aAddress.addAdminunitFirstline (aAdmin1);
      bAny = true;
    }
    return bAny ? aAddress : null;
  }

  @Nonnull
  public CvbusinessType getAsBusiness ()
  {
    final CvbusinessType ret = new CvbusinessType ();
    if (StringHelper.hasText (m_sLegalName))
    {
      final LegalNameType aLegalName = new LegalNameType ();
      aLegalName.setValue (m_sLegalName);
      ret.addLegalName (aLegalName);
    }

    // Legal ID is mandatory
    {
      final CvidentifierType aLegalID = new CvidentifierType ();
      {
        final IdentifierType aID = new IdentifierType ();
        aID.setValue (m_sLegalID);
        aLegalID.setIdentifier (aID);
      }
      if (StringHelper.hasText (m_sLegalIDType))
      {
        final IdentifierTypeType aIDType = new IdentifierTypeType ();
        aIDType.setValue (m_sLegalIDType);
        aLegalID.setIdentifierType (aIDType);
      }
      ret.setLegalIdentifierCvidentifier (aLegalID);
    }

    if (StringHelper.hasText (m_sID))
    {
      final CvidentifierType aLegalID = new CvidentifierType ();
      {
        final IdentifierType aID = new IdentifierType ();
        aID.setValue (m_sID);
        aLegalID.setIdentifier (aID);
      }
      if (StringHelper.hasText (m_sIDType))
      {
        final IdentifierTypeType aIDType = new IdentifierTypeType ();
        aIDType.setValue (m_sIDType);
        aLegalID.setIdentifierType (aIDType);
      }
      ret.addIdentifierCvidentifier (aLegalID);
    }

    ret.setCvaddress (createAddress (m_sFullAddress,
                                     m_sStreetName,
                                     m_sBuildingNumber,
                                     m_sTown,
                                     m_sPostalCode,
                                     m_sCountryCode));
    return ret;
  }

  @Nonnull
  public static BusinessPojo createMinimum ()
  {
    return new BusinessPojo (null, null, null, null, null, null, null, null, null, null, null);
  }
}
