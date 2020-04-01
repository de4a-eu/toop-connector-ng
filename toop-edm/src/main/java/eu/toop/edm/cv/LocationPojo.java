package eu.toop.edm.cv;

import javax.annotation.Nonnull;

import org.w3.ns.corevocabulary.location.CvlocationType;

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

    ret.addCvaddress (BusinessPojo.createAddress (m_sFullAddress,
                                                  m_sStreetName,
                                                  m_sBuildingNumber,
                                                  m_sTown,
                                                  m_sPostalCode,
                                                  m_sCountryCode));

    return ret;
  }

  @Nonnull
  public static LocationPojo createMinimum ()
  {
    return new LocationPojo (null, null, null, null, null, null);
  }
}
