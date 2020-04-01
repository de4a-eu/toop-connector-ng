package eu.toop.edm.regrep;

import javax.annotation.Nonnull;

import eu.toop.regrep.rim.SlotType;

@FunctionalInterface
public interface ISlotProvider <T>
{
  @Nonnull
  SlotType createSlot (@Nonnull String sName, T aValue);
}
