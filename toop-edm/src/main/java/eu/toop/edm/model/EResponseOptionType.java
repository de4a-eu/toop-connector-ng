package eu.toop.edm.model;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.id.IHasID;
import com.helger.commons.lang.EnumHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public enum EResponseOptionType implements IHasID<String>
{
    LEAFCLASSWRI("LeafClassWithRepositoryItem"),
    OBJECTREF("ObjectRef");

    private final String m_sID;

    EResponseOptionType (@Nonnull @Nonempty final String sID)
    {
        m_sID = sID;
    }

    @Nonnull
    @Nonempty
    public String getID ()
    {
        return m_sID;
    }

    @Nullable
    public static EResponseOptionType getFromIDOrNull (@Nullable final String sID)
    {
        return EnumHelper.getFromIDOrNull (EResponseOptionType.class, sID);
    }
}
