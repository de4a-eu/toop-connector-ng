package eu.toop.edm.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.datatype.XMLGregorianCalendar;

import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.ToStringGenerator;
import com.helger.datetime.util.PDTXMLConverter;

import eu.toop.edm.jaxb.cv.cac.PeriodType;

/**
 * Represents a single "Period".
 *
 * @author Konstantinos Douloudis
 * @author Philip Helger
 */
public class PeriodPojo
{
  private final LocalDate m_aStartDate;
  private final LocalTime m_aStartTime;
  private final LocalDate m_aEndDate;
  private final LocalTime m_aEndTime;

  public PeriodPojo (@Nullable final LocalDate aStartDate,
                     @Nullable final LocalTime aStartTime,
                     @Nullable final LocalDate aEndDate,
                     @Nullable final LocalTime aEndTime)
  {
    m_aStartDate = aStartDate;
    m_aStartTime = aStartTime;
    m_aEndDate = aEndDate;
    m_aEndTime = aEndTime;
  }

  @Nullable
  public final LocalDate getStartDate ()
  {
    return m_aEndDate;
  }

  @Nullable
  public final LocalTime getStartTime ()
  {
    return m_aEndTime;
  }

  @Nullable
  public final LocalDate getEndDate ()
  {
    return m_aEndDate;
  }

  @Nullable
  public final LocalTime getEndTime ()
  {
    return m_aEndTime;
  }

  @Nonnull
  public PeriodType getAsPeriod ()
  {
    final PeriodType ret = new PeriodType ();
    ret.setStartDate (PDTXMLConverter.getXMLCalendarDate (m_aStartDate));
    ret.setStartTime (PDTXMLConverter.getXMLCalendarTime (m_aStartTime));
    ret.setEndDate (PDTXMLConverter.getXMLCalendarDate (m_aEndDate));
    ret.setEndTime (PDTXMLConverter.getXMLCalendarTime (m_aEndTime));
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
    return EqualsHelper.equals (m_aStartDate, rhs.m_aStartDate) &&
           EqualsHelper.equals (m_aStartTime, rhs.m_aStartTime) &&
           EqualsHelper.equals (m_aEndDate, rhs.m_aEndDate) &&
           EqualsHelper.equals (m_aEndTime, rhs.m_aEndTime);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aStartDate)
                                       .append (m_aStartTime)
                                       .append (m_aEndDate)
                                       .append (m_aEndTime)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("StartDate", m_aStartDate)
                                       .append ("StartTime", m_aStartDate)
                                       .append ("EndDate", m_aEndDate)
                                       .append ("EndTime", m_aEndTime)
                                       .getToString ();
  }

  @Nonnull
  public static Builder builder ()
  {
    return new Builder ();
  }

  @Nonnull
  public static Builder builder (@Nullable final PeriodType a)
  {
    final Builder ret = new Builder ();
    if (a != null)
    {
      ret.startDate (a.getStartDateValue ())
         .startTime (a.getStartTimeValue ())
         .endDate (a.getEndDateValue ())
         .endTime (a.getEndTimeValue ());
    }
    return ret;
  }

  public static class Builder
  {
    private LocalDate m_aStartDate;
    private LocalTime m_aStartTime;
    private LocalDate m_aEndDate;
    private LocalTime m_aEndTime;

    public Builder ()
    {}

    @Nonnull
    public Builder startDateTime (@Nullable final XMLGregorianCalendar a)
    {
      return startDateTime (PDTXMLConverter.getLocalDateTime (a));
    }

    @Nonnull
    public Builder startDateTime (@Nullable final LocalDateTime a)
    {
      return startDate (a == null ? null : a.toLocalDate ()).startTime (a == null ? null : a.toLocalTime ());
    }

    @Nonnull
    public Builder startDate (@Nullable final XMLGregorianCalendar a)
    {
      return startDate (PDTXMLConverter.getLocalDate (a));
    }

    @Nonnull
    public Builder startDate (@Nullable final LocalDate a)
    {
      m_aStartDate = a;
      return this;
    }

    @Nonnull
    public Builder startTime (@Nullable final XMLGregorianCalendar a)
    {
      return startTime (PDTXMLConverter.getLocalTime (a));
    }

    @Nonnull
    public Builder startTime (@Nullable final LocalTime a)
    {
      m_aStartTime = a == null ? null : a.truncatedTo (ChronoUnit.MILLIS);
      return this;
    }

    @Nonnull
    public Builder endDateTime (@Nullable final XMLGregorianCalendar a)
    {
      return endDateTime (PDTXMLConverter.getLocalDateTime (a));
    }

    @Nonnull
    public Builder endDateTime (@Nullable final LocalDateTime a)
    {
      return endDate (a == null ? null : a.toLocalDate ()).endTime (a == null ? null : a.toLocalTime ());
    }

    @Nonnull
    public Builder endDate (@Nullable final XMLGregorianCalendar a)
    {
      return endDate (PDTXMLConverter.getLocalDate (a));
    }

    @Nonnull
    public Builder endDate (@Nullable final LocalDate a)
    {
      m_aEndDate = a;
      return this;
    }

    @Nonnull
    public Builder endTime (@Nullable final XMLGregorianCalendar a)
    {
      return endTime (PDTXMLConverter.getLocalTime (a));
    }

    @Nonnull
    public Builder endTime (@Nullable final LocalTime a)
    {
      m_aEndTime = a == null ? null : a.truncatedTo (ChronoUnit.MILLIS);
      return this;
    }

    @Nonnull
    public PeriodPojo build ()
    {
      return new PeriodPojo (m_aStartDate, m_aStartTime, m_aEndDate, m_aEndTime);
    }
  }
}
