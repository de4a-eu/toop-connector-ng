package eu.toop.edm.slot;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import eu.toop.regrep.SlotBuilder;
import eu.toop.regrep.rim.SlotType;

import javax.annotation.Nonnull;

public class SlotId implements ISlotProvider
{
    public static final String NAME = "id";

    private final String m_sValue;

    public SlotId (@Nonnull final String sValue)
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
        return new SlotBuilder().setName (NAME).setValue (m_sValue).build ();
    }
}
