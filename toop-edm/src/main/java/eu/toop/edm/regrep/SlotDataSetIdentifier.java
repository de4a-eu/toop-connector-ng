package eu.toop.edm.regrep;

import javax.annotation.Nonnull;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;

import eu.toop.regrep.rim.SlotType;

/**
 * "DataSetIdentifier" slot
 *
 * @author Philip Helger
 */
public class SlotDataSetIdentifier implements ISlotProvider
{
  public static final String NAME = "DataSetIdentifier";

  private final String m_sValue;

  public SlotDataSetIdentifier (@Nonnull final String sValue)
  {
    ValueEnforcer.notNull (sValue, "Value");
    m_sValue = sValue;
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
    return new SlotBuilder ().setName (NAME).setValue (m_sValue).build ();
  }
}
