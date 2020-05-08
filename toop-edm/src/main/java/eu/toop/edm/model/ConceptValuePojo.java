package eu.toop.edm.model;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.datatype.XMLGregorianCalendar;

import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.math.MathHelper;
import com.helger.commons.string.StringParser;
import com.helger.datetime.util.PDTXMLConverter;

import eu.toop.edm.error.IToopErrorCode;
import eu.toop.edm.jaxb.cccev.CCCEVValueType;
import eu.toop.edm.jaxb.cv.cbc.TextType;
import eu.toop.edm.xml.cccev.CCCEVValueHelper;

public class ConceptValuePojo
{
  private final String m_sIdentifier;
  private final AmountPojo m_aAmount;
  private final String m_sCode;
  private final LocalDate m_aDate;
  private final Boolean m_aIndicator;
  private final MeasurePojo m_aMeasure;
  private final BigDecimal m_aNumeric;
  private final QuantityPojo m_aQuantity;
  private final ICommonsList <String> m_aText = new CommonsArrayList <> ();
  private final LocalTime m_aTime;
  private final String m_sURI;
  private final String m_sErrorCode;

  public ConceptValuePojo (@Nullable final String sIdentifier,
                           @Nullable final AmountPojo aAmount,
                           @Nullable final String sCode,
                           @Nullable final LocalDate aDate,
                           @Nullable final Boolean aIndicator,
                           @Nullable final MeasurePojo aMeasure,
                           @Nullable final BigDecimal aNumeric,
                           @Nullable final QuantityPojo aQuantity,
                           @Nullable final List <String> aText,
                           @Nullable final LocalTime aTime,
                           @Nullable final String sURI,
                           @Nullable final String sErrorCode)
  {
    m_sIdentifier = sIdentifier;
    m_aAmount = aAmount;
    m_sCode = sCode;
    m_aDate = aDate;
    m_aIndicator = aIndicator;
    m_aMeasure = aMeasure;
    m_aNumeric = aNumeric;
    m_aQuantity = aQuantity;
    if (aText != null)
      m_aText.addAll (aText);
    m_aTime = aTime;
    m_sURI = sURI;
    m_sErrorCode = sErrorCode;
  }

  @Nullable
  public String getIdentifier ()
  {
    return m_sIdentifier;
  }

  @Nullable
  public AmountPojo getAmount ()
  {
    return m_aAmount;
  }

  @Nullable
  public String getCode ()
  {
    return m_sCode;
  }

  @Nullable
  public LocalDate getDate ()
  {
    return m_aDate;
  }

  @Nullable
  public Boolean getBoolean ()
  {
    return m_aIndicator;
  }

  @Nullable
  public MeasurePojo getMeasure ()
  {
    return m_aMeasure;
  }

  @Nullable
  public BigDecimal getNumeric ()
  {
    return m_aNumeric;
  }

  @Nullable
  public QuantityPojo getQuantity ()
  {
    return m_aQuantity;
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> text ()
  {
    return m_aText;
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllTexts ()
  {
    return m_aText.getClone ();
  }

  @Nullable
  public LocalTime getTime ()
  {
    return m_aTime;
  }

  @Nullable
  public String getURI ()
  {
    return m_sURI;
  }

  @Nullable
  public String getErrorCode ()
  {
    return m_sErrorCode;
  }

  @Nullable
  public CCCEVValueType getAsCCCEVValueType ()
  {
    if (m_sIdentifier != null)
      return CCCEVValueHelper.createID (m_sIdentifier);
    if (m_aAmount != null)
      return CCCEVValueHelper.create (m_aAmount.getAsAmount ());
    if (m_sCode != null)
      return CCCEVValueHelper.createCode (m_sCode);
    if (m_aDate != null)
      return CCCEVValueHelper.create (m_aDate);
    if (m_aIndicator != null)
      return CCCEVValueHelper.create (m_aIndicator);
    if (m_aMeasure != null)
      return CCCEVValueHelper.create (m_aMeasure.getAsMeasure ());
    if (m_aNumeric != null)
      return CCCEVValueHelper.create (m_aNumeric);
    if (m_aQuantity != null)
      return CCCEVValueHelper.create (m_aQuantity.getAsQuantity ());
    if (m_aText.isNotEmpty ())
      return CCCEVValueHelper.createText (m_aText);
    if (m_aTime != null)
      return CCCEVValueHelper.create (m_aTime);
    if (m_sURI != null)
      return CCCEVValueHelper.createURI (m_sURI);
    if (m_sErrorCode != null)
      return CCCEVValueHelper.createError (m_sErrorCode);
    return null;
  }

  @Nonnull
  public static Builder builder ()
  {
    return new Builder ();
  }

  @Nonnull
  public static Builder builder (@Nullable final CCCEVValueType a)
  {
    final Builder ret = new Builder ();
    if (a != null)
    {
      ret.identifier (a.getIdentifierValueValue ())
         .amount (AmountPojo.builder (a.getAmountValue ()))
         .code (a.getCodeValueValue ())
         .date (a.getDateValueValue ())
         .indicator (a.getIndicatorValue () == null ? null
                                                    : StringParser.parseBoolObj (a.getIndicatorValue ().getValue ()))
         .measure (MeasurePojo.builder (a.getMeasureValue ()))
         .numeric (a.getNumericValueValue ())
         .quantity (QuantityPojo.builder (a.getQuantityValue ()))
         .text (new CommonsArrayList <> (a.getTextValue (), TextType::getValue))
         .time (a.getTimeValueValue ())
         .uri (a.getUriValueValue ())
         .errorCode (a.getErrorValue ());
    }
    return ret;
  }

  public static class Builder
  {
    private String m_sIdentifier;
    private AmountPojo m_aAmount;
    private String m_sCode;
    private LocalDate m_aDate;
    private Boolean m_aIndicator;
    private MeasurePojo m_aMeasure;
    private BigDecimal m_aNumeric;
    private QuantityPojo m_aQuantity;
    private final ICommonsList <String> m_aText = new CommonsArrayList <> ();
    private LocalTime m_aTime;
    private String m_sURI;
    private String m_sErrorCode;

    public Builder ()
    {}

    @Nonnull
    public Builder identifier (@Nullable final String s)
    {
      m_sIdentifier = s;
      return this;
    }

    @Nonnull
    public Builder amount (@Nullable final BigDecimal aValue, @Nullable final String sCurrencyID)
    {
      return amount (new AmountPojo (aValue, sCurrencyID));
    }

    @Nonnull
    public Builder amount (@Nullable final AmountPojo.Builder a)
    {
      return amount (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder amount (@Nullable final AmountPojo a)
    {
      m_aAmount = a;
      return this;
    }

    @Nonnull
    public Builder code (@Nullable final String s)
    {
      m_sCode = s;
      return this;
    }

    @Nonnull
    public Builder date (@Nullable final XMLGregorianCalendar a)
    {
      return date (PDTXMLConverter.getLocalDate (a));
    }

    @Nonnull
    public Builder date (@Nullable final LocalDate a)
    {
      m_aDate = a;
      return this;
    }

    @Nonnull
    public Builder indicator (final boolean b)
    {
      return indicator (Boolean.valueOf (b));
    }

    @Nonnull
    public Builder indicator (@Nullable final Boolean a)
    {
      m_aIndicator = a;
      return this;
    }

    @Nonnull
    public Builder measure (@Nullable final BigDecimal aValue, @Nullable final String sUnitCode)
    {
      return measure (new MeasurePojo (aValue, sUnitCode));
    }

    @Nonnull
    public Builder measure (@Nullable final MeasurePojo.Builder a)
    {
      return measure (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder measure (@Nullable final MeasurePojo a)
    {
      m_aMeasure = a;
      return this;
    }

    @Nonnull
    public Builder numeric (final long n)
    {
      return numeric (MathHelper.toBigDecimal (n));
    }

    @Nonnull
    public Builder numeric (final double d)
    {
      return numeric (MathHelper.toBigDecimal (d));
    }

    @Nonnull
    public Builder numeric (@Nullable final BigDecimal a)
    {
      m_aNumeric = a;
      return this;
    }

    @Nonnull
    public Builder quantity (@Nullable final BigDecimal aValue, @Nullable final String sUnitCode)
    {
      return quantity (new QuantityPojo (aValue, sUnitCode));
    }

    @Nonnull
    public Builder quantity (@Nullable final QuantityPojo.Builder a)
    {
      return quantity (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder quantity (@Nullable final QuantityPojo a)
    {
      m_aQuantity = a;
      return this;
    }

    @Nonnull
    public Builder text (@Nullable final String... a)
    {
      m_aText.setAll (a);
      return this;
    }

    @Nonnull
    public Builder text (@Nullable final Collection <String> a)
    {
      m_aText.setAll (a);
      return this;
    }

    @Nonnull
    public Builder time (@Nullable final XMLGregorianCalendar a)
    {
      return time (PDTXMLConverter.getLocalTime (a));
    }

    @Nonnull
    public Builder time (@Nullable final LocalTime a)
    {
      m_aTime = a;
      return this;
    }

    @Nonnull
    public Builder uri (@Nullable final URI a)
    {
      return uri (a == null ? null : a.toString ());
    }

    @Nonnull
    public Builder uri (@Nullable final URL a)
    {
      return uri (a == null ? null : a.toExternalForm ());
    }

    @Nonnull
    public Builder uri (@Nullable final String s)
    {
      m_sURI = s;
      return this;
    }

    @Nonnull
    public Builder errorCode (@Nullable final IToopErrorCode a)
    {
      return errorCode (a == null ? null : a.getID ());
    }

    @Nonnull
    public Builder errorCode (@Nullable final String s)
    {
      m_sErrorCode = s;
      return this;
    }

    public void checkConsistency ()
    {
      int nCount = 0;
      if (m_sIdentifier != null)
        nCount++;
      if (m_aAmount != null)
        nCount++;
      if (m_sCode != null)
        nCount++;
      if (m_aDate != null)
        nCount++;
      if (m_aIndicator != null)
        nCount++;
      if (m_aMeasure != null)
        nCount++;
      if (m_aNumeric != null)
        nCount++;
      if (m_aQuantity != null)
        nCount++;
      if (m_aText.isNotEmpty ())
        nCount++;
      if (m_aTime != null)
        nCount++;
      if (m_sURI != null)
        nCount++;
      if (m_sErrorCode != null)
        nCount++;
      if (nCount == 0)
        throw new IllegalStateException ("No value was provided to the Concept Value");
      if (nCount > 1)
        throw new IllegalStateException ("The Concept Value must have exactly one value");
    }

    @Nonnull
    public ConceptValuePojo build ()
    {
      checkConsistency ();

      return new ConceptValuePojo (m_sIdentifier,
                                   m_aAmount,
                                   m_sCode,
                                   m_aDate,
                                   m_aIndicator,
                                   m_aMeasure,
                                   m_aNumeric,
                                   m_aQuantity,
                                   m_aText,
                                   m_aTime,
                                   m_sURI,
                                   m_sErrorCode);
    }
  }
}
