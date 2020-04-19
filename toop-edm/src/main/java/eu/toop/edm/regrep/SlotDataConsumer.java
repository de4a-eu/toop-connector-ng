package eu.toop.edm.regrep;

import javax.annotation.Nonnull;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;

import eu.toop.edm.cpsv.AgentMarshaller;
import eu.toop.edm.jaxb.cpsv.helper.AgentType;
import eu.toop.regrep.rim.SlotType;

/**
 * "DataConsumer" slot
 *
 * @author Philip Helger
 */
public class SlotDataConsumer implements ISlotProvider
{
  public static final String NAME = "DataConsumer";

  private final AgentType m_aAgent;

  public SlotDataConsumer (@Nonnull final AgentType aAgent)
  {
    ValueEnforcer.notNull (aAgent, "Agent");
    m_aAgent = aAgent;
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
    return new SlotBuilder ().setName (NAME).setValue (new AgentMarshaller ().getAsDocument (m_aAgent)).build ();
  }
}
