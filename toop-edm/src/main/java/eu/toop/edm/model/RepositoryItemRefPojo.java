package eu.toop.edm.model;

import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.ToStringGenerator;
import eu.toop.regrep.rim.SimpleLinkType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class RepositoryItemRefPojo
{
    private final String m_sTitle;
    private final String m_sLink;

    public RepositoryItemRefPojo (@Nullable final String sTitle, @Nullable final String sLink)
    {
        m_sLink = sLink;
        m_sTitle = sTitle;
    }

    @Nullable
    public final String getTitle ()
    {
        return m_sTitle;
    }

    @Nullable
    public final String getLink ()
    {
        return m_sLink;
    }

    @Nonnull
    public SimpleLinkType getAsSimpleLink (){
        final SimpleLinkType ret = new SimpleLinkType ();
        ret.setTitle (m_sTitle);
        ret.setHref(m_sLink);

        return ret;
    }

    @Override
    public boolean equals (final Object o)
    {
        if (o == this)
            return true;
        if (o == null || !getClass ().equals (o.getClass ()))
            return false;
        final RepositoryItemRefPojo rhs = (RepositoryItemRefPojo) o;
        return EqualsHelper.equals (m_sLink, rhs.m_sLink) && EqualsHelper.equals (m_sTitle, rhs.m_sTitle);
    }

    @Override
    public int hashCode ()
    {
        return new HashCodeGenerator(this).append (m_sTitle).append (m_sLink).getHashCode ();
    }

    @Override
    public String toString ()
    {
        return new ToStringGenerator(this).append ("Title", m_sTitle).append ("Link", m_sLink).getToString ();
    }

    @Nonnull
    public static RepositoryItemRefPojo.Builder builder ()
    {
        return new RepositoryItemRefPojo.Builder();
    }

    @Nonnull
    public static RepositoryItemRefPojo.Builder builder (@Nullable final SimpleLinkType a)
    {
        final RepositoryItemRefPojo.Builder ret = new RepositoryItemRefPojo.Builder();
        if (a != null) {
            return ret.link(a.getHref()).title(a.getTitle());
        }
        return ret;
    }

    public static class Builder
    {
        private String m_sTitle;
        private String m_sLink;

        public Builder ()
        {}

        public RepositoryItemRefPojo.Builder title (@Nonnull final String sTitle)
        {
            m_sTitle = sTitle;
            return this;
        }

        @Nonnull
        public RepositoryItemRefPojo.Builder link (@Nonnull final String sLink)
        {
            m_sLink = sLink;
            return this;
        }

        @Nonnull
        public RepositoryItemRefPojo build ()
        {
            return new RepositoryItemRefPojo (m_sTitle, m_sLink);
        }
    }
}