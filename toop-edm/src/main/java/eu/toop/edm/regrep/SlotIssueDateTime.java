package eu.toop.edm.regrep;

import java.time.LocalDateTime;

import javax.annotation.Nonnull;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;

import eu.toop.regrep.rim.SlotType;

/**
 * "IssueDateTime" slot
 *
 * @author Philip Helger
 */
public class SlotIssueDateTime implements ISlotProvider
{
  public static final String NAME = "IssueDateTime";

  private final LocalDateTime m_aLDT;

  public SlotIssueDateTime (@Nonnull final LocalDateTime aLDT)
  {
    ValueEnforcer.notNull (aLDT, "LDT");
    m_aLDT = aLDT;
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
    return new SlotBuilder ().setName (NAME).setValue (m_aLDT).build ();
  }
}
