package eu.toop.edm.cv;

import javax.annotation.Nonnull;

import org.w3.ns.corevocabulary.aggregatecomponents.CvaddressType;
import org.w3.ns.corevocabulary.basiccomponents.AdminunitFirstlineType;
import org.w3.ns.corevocabulary.basiccomponents.FullCvaddressType;
import org.w3.ns.corevocabulary.basiccomponents.LocatorDesignatorType;
import org.w3.ns.corevocabulary.basiccomponents.PostCodeType;
import org.w3.ns.corevocabulary.basiccomponents.PostNameType;
import org.w3.ns.corevocabulary.basiccomponents.ThoroughfareType;
import org.w3.ns.corevocabulary.location.CvlocationType;

import com.helger.commons.string.StringHelper;

public class LocationPojo
{
  private final String m_sFullAddress;
  private final String m_sStreetName;
  private final String m_sBuildingNumber;
  private final String m_sTown;
  private final String m_sPostalCode;
  private final String m_sCountryCode;

  public LocationPojo (final String sFullAddress,
                       final String sStreetName,
                       final String sBuildingNumber,
                       final String sTown,
                       final String sPostalCode,
                       final String sCountryCode)
  {
    m_sFullAddress = sFullAddress;
    m_sStreetName = sStreetName;
    m_sBuildingNumber = sBuildingNumber;
    m_sTown = sTown;
    m_sPostalCode = sPostalCode;
    m_sCountryCode = sCountryCode;
  }

  @Nonnull
  public CvlocationType getAsLocation ()
  {
    final CvlocationType ret = new CvlocationType ();

    final CvaddressType aAddress = new CvaddressType ();
    if (StringHelper.hasText (m_sFullAddress))
    {
      final FullCvaddressType aFullAddress = new FullCvaddressType ();
      aFullAddress.setValue (m_sFullAddress);
      aAddress.addFullCvaddress (aFullAddress);
    }
    if (StringHelper.hasText (m_sStreetName))
    {
      final ThoroughfareType aThoroughfare = new ThoroughfareType ();
      aThoroughfare.setValue (m_sStreetName);
      aAddress.addThoroughfare (aThoroughfare);
    }
    if (StringHelper.hasText (m_sBuildingNumber))
    {
      final LocatorDesignatorType aLD = new LocatorDesignatorType ();
      aLD.setValue (m_sBuildingNumber);
      aAddress.addLocatorDesignator (aLD);
    }
    if (StringHelper.hasText (m_sTown))
    {
      final PostNameType aPostName = new PostNameType ();
      aPostName.setValue (m_sTown);
      aAddress.addPostName (aPostName);
    }
    if (StringHelper.hasText (m_sPostalCode))
    {
      final PostCodeType aPostCode = new PostCodeType ();
      aPostCode.setValue (m_sPostalCode);
      aAddress.addPostCode (aPostCode);
    }
    if (StringHelper.hasText (m_sCountryCode))
    {
      final AdminunitFirstlineType aAdmin1 = new AdminunitFirstlineType ();
      aAdmin1.setValue (m_sCountryCode);
      aAddress.addAdminunitFirstline (aAdmin1);
    }
    ret.addCvaddress (aAddress);

    return ret;
  }
}
