package eu.toop.edm.model;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.id.IHasID;

/**
 * Gender code code list
 * 
 * @author Philip Helger
 */
public enum EGenderCode implements IHasID <String>
{
  M ("M", "Male"),
  F ("F", "Female"),
  UN ("UN",
      "Undifferentiated (the gender of a person could not be uniquely defined as male or female, such as hermaphrodite)");

  private final String m_sID;
  private final String m_sDisplayName;

  EGenderCode (@Nonnull @Nonempty final String sID, @Nonnull @Nonempty final String sDisplayName)
  {
    m_sID = sID;
    m_sDisplayName = sDisplayName;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  @Nonnull
  @Nonempty
  public String getDisplayName ()
  {
    return m_sDisplayName;
  }
}
