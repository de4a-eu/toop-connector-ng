package eu.toop.edm.regrep;

import javax.annotation.Nonnull;

import org.w3.ns.corevocabulary.person.CvpersonType;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;

import eu.toop.edm.cv.PersonMarshaller;
import eu.toop.regrep.rim.SlotType;

/**
 * DataSubject "AuthorizedRepresentative" slot
 *
 * @author Philip Helger
 */
public class SlotAuthorizedRepresentative implements ISlotProvider
{
  public static final String NAME = "AuthorizedRepresentative";

  private final CvpersonType m_aNaturalPerson;

  public SlotAuthorizedRepresentative (@Nonnull final CvpersonType aNaturalPerson)
  {
    ValueEnforcer.notNull (aNaturalPerson, "NaturalPerson");
    m_aNaturalPerson = aNaturalPerson;
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
                             .setValue (new PersonMarshaller ().getAsDocument (m_aNaturalPerson))
                             .build ();
  }
}
