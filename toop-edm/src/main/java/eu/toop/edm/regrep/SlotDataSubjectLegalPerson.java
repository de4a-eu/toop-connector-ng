package eu.toop.edm.regrep;

import javax.annotation.Nonnull;

import org.w3.ns.corevocabulary.business.CvbusinessType;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;

import eu.toop.edm.cv.BusinessMarshaller;
import eu.toop.regrep.rim.SlotType;

/**
 * DataSubject "LegalPerson" slot
 *
 * @author Philip Helger
 */
public class SlotDataSubjectLegalPerson implements ISlotProvider
{
  public static final String NAME = "LegalPerson";

  private final CvbusinessType m_aLegalPerson;

  public SlotDataSubjectLegalPerson (@Nonnull final CvbusinessType aLegalPerson)
  {
    ValueEnforcer.notNull (aLegalPerson, "LegalPerson");
    m_aLegalPerson = aLegalPerson;
  }

  @Nonnull
  @Nonempty
  public String getName ()
  {
    return NAME;
  }

  @Nonnull
  public SlotType createSlot ()
  {
    return new SlotBuilder ().setName (NAME)
                             .setValue (new BusinessMarshaller ().getAsDocument (m_aLegalPerson))
                             .build ();
  }
}
