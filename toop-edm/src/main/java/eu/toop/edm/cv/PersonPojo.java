package eu.toop.edm.cv;

import java.time.LocalDate;

import javax.annotation.Nonnull;

import org.w3.ns.corevocabulary.aggregatecomponents.CvidentifierType;
import org.w3.ns.corevocabulary.basiccomponents.BirthDateType;
import org.w3.ns.corevocabulary.basiccomponents.BirthNameType;
import org.w3.ns.corevocabulary.basiccomponents.GenderCodeType;
import org.w3.ns.corevocabulary.basiccomponents.GivenNameType;
import org.w3.ns.corevocabulary.basiccomponents.IdentifierType;
import org.w3.ns.corevocabulary.person.CvpersonType;

import com.helger.commons.string.StringHelper;
import com.helger.datetime.util.PDTXMLConverter;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.FamilyNameType;

public class PersonPojo
{
  private final String m_sFamilyName;
  private final String m_sGivenName;
  private final String m_sGenderCode;
  private final String m_sBirthName;
  private final LocalDate m_aBirthDate;
  private final String m_sID;

  public PersonPojo (final String sFamilyName,
                     final String sGivenName,
                     final String sGenderCode,
                     final String sBirthName,
                     final LocalDate aBirthDate,
                     final String sID)
  {
    m_sFamilyName = sFamilyName;
    m_sGivenName = sGivenName;
    m_sGenderCode = sGenderCode;
    m_sBirthName = sBirthName;
    m_aBirthDate = aBirthDate;
    m_sID = sID;
  }

  @Nonnull
  public CvpersonType getAsPerson ()
  {
    final CvpersonType ret = new CvpersonType ();

    if (StringHelper.hasText (m_sFamilyName))
    {
      final FamilyNameType aFamilyName = new FamilyNameType ();
      aFamilyName.setValue (m_sFamilyName);
      ret.addFamilyName (aFamilyName);
    }
    if (StringHelper.hasText (m_sGivenName))
    {
      final GivenNameType aGivenName = new GivenNameType ();
      aGivenName.setValue (m_sGivenName);
      ret.addGivenName (aGivenName);
    }
    if (StringHelper.hasText (m_sGenderCode))
    {
      final GenderCodeType aGC = new GenderCodeType ();
      aGC.setValue (m_sGenderCode);
      ret.addGenderCode (aGC);
    }
    if (StringHelper.hasText (m_sBirthName))
    {
      final BirthNameType aBirthName = new BirthNameType ();
      aBirthName.setValue (m_sBirthName);
      ret.addBirthName (aBirthName);
    }
    if (m_aBirthDate != null)
    {
      final BirthDateType aBirthDate = new BirthDateType ();
      aBirthDate.setValue (PDTXMLConverter.getXMLCalendarDate (m_aBirthDate));
      ret.addBirthDate (aBirthDate);
    }
    if (StringHelper.hasText (m_sID))
    {
      final CvidentifierType aID = new CvidentifierType ();
      final IdentifierType aIdentifier = new IdentifierType ();
      aIdentifier.setValue (m_sID);
      aID.setIdentifier (aIdentifier);
      ret.addCvidentifier (aID);
    }

    return ret;
  }

  @Nonnull
  public static PersonPojo createMinimum ()
  {
    return new PersonPojo (null, null, null, null, null, null);
  }
}
