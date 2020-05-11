package eu.toop.edm.model;

import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.ToStringGenerator;
import com.helger.datetime.util.PDTXMLConverter;
import eu.toop.edm.jaxb.cv.cac.PeriodType;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;

public class PeriodPojo {
    private final LocalDateTime m_aStartDate;
    private final LocalDateTime m_aEndDate;

    public PeriodPojo (@Nullable final LocalDateTime aStartDate, @Nullable final LocalDateTime aEndDate)
    {
        m_aStartDate = aStartDate;
        m_aEndDate = aEndDate;
    }

    @Nullable
    public final LocalDateTime getStartDate ()
    {
        return m_aEndDate;
    }

    @Nullable
    public final LocalDateTime getEndDate ()
    {
        return m_aEndDate;
    }

    @Nonnull
    public PeriodType getAsPeriod () {
        final PeriodType ret = new PeriodType();
        ret.setStartDate (PDTXMLConverter.getXMLCalendar(m_aStartDate));
        ret.setEndDate (PDTXMLConverter.getXMLCalendar(m_aEndDate));
        return ret;
    }

    @Override
    public boolean equals (final Object o)
    {
        if (o == this)
            return true;
        if (o == null || !getClass ().equals (o.getClass ()))
            return false;
        final PeriodPojo rhs = (PeriodPojo) o;
        return EqualsHelper.equals (m_aEndDate, rhs.m_aEndDate) && EqualsHelper.equals (m_aStartDate, rhs.m_aStartDate);
    }

    @Override
    public int hashCode ()
    {
        return new HashCodeGenerator(this).append (m_aStartDate).append (m_aEndDate).getHashCode ();
    }

    @Override
    public String toString ()
    {
        return new ToStringGenerator(this).append ("Start Date", m_aStartDate).append ("End Date", m_aEndDate).getToString ();
    }

    @Nonnull
    public static Builder builder ()
    {
        return new Builder();
    }

    @Nonnull
    public static Builder builder (@Nullable final PeriodType a)
    {

        final Builder ret = new Builder();
        if (a != null)
            ret.startDate (PDTXMLConverter.getLocalDateTime(a.getStartDateValue ()))
               .endDate (PDTXMLConverter.getLocalDateTime(a.getEndDateValue()));
        return ret;
    }

    public static class Builder
    {
        private LocalDateTime m_aStartDate;
        private LocalDateTime m_aEndDate;

        public Builder ()
        {}

        @Nonnull
        public Builder startDate (@Nullable final LocalDateTime a)
        {
            m_aStartDate = a;
            return this;
        }

        @Nonnull
        public Builder endDate (@Nullable final LocalDateTime s)
        {
            m_aEndDate = s;
            return this;
        }

        @Nonnull
        public PeriodPojo build ()
        {
            return new PeriodPojo (m_aStartDate, m_aEndDate);
        }
    }
}
