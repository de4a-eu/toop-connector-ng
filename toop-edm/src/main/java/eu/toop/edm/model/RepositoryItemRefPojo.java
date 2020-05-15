/**
 * Copyright (C) 2018-2020 toop.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.toop.edm.model;

import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.ToStringGenerator;
import eu.toop.regrep.rim.SimpleLinkType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RepositoryItemRefPojo
{
    private final String m_sTitle;
    private final String m_sLink;

    public RepositoryItemRefPojo (@Nullable final String sTitle, @Nullable final String sLink)
    {
        m_sTitle = sTitle;
        m_sLink = sLink;
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
        if(m_sTitle != null)
            ret.setTitle (m_sTitle);
        if(m_sLink != null)
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
        return EqualsHelper.equals (m_sTitle, rhs.m_sTitle) && EqualsHelper.equals (m_sLink, rhs.m_sLink);
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
            if(a.getTitle() != null)
                ret.title(a.getTitle());
            if(a.getHref() != null)
                ret.link(a.getHref());
            return ret;
        }
        return ret;
    }

    public static class Builder
    {
        private String m_sTitle;
        private String m_sLink;

        public Builder ()
        {}

        @Nonnull
        public RepositoryItemRefPojo.Builder title (@Nullable final String sTitle)
        {
            m_sTitle = sTitle;
            return this;
        }

        @Nonnull
        public RepositoryItemRefPojo.Builder link (@Nullable final String sLink)
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