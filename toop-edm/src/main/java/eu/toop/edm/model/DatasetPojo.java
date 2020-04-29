package eu.toop.edm.model;

import java.time.LocalDateTime;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.string.StringHelper;

import eu.toop.edm.jaxb.dcatap.DCatAPDatasetType;

public class DatasetPojo
{
  private final ICommonsList <String> m_aDescriptions = new CommonsArrayList <> ();
  private final ICommonsList <String> m_aTitles = new CommonsArrayList <> ();
  private final String m_sID;
  private final LocalDateTime m_aIssuedDT;
  private final LocalDateTime m_aLastModifiedDT;
  private final String m_sLanguage;
  private final ICommonsList <String> m_aErrorCodes;

  public DatasetPojo (@Nonnull @Nonempty final ICommonsList <String> aDescriptions,
                      @Nonnull @Nonempty final ICommonsList <String> aTitles,
                      @Nullable final String sID,
                      @Nullable final LocalDateTime aIssuedDT,
                      @Nullable final LocalDateTime aLastModifiedDT,
                      @Nullable final String sLanguage,
                      @Nullable final ICommonsList <String> aErrorCodes)
  {
    ValueEnforcer.notEmptyNoNullValue (aTitles, "Titles");
    ValueEnforcer.notEmptyNoNullValue (aDescriptions, "Descriptions");

    m_sID = sID;
    m_aTitles.addAll (aTitles);
    m_aDescriptions.addAll (aDescriptions);
    m_aIssuedDT = aIssuedDT;
    m_aLastModifiedDT = aLastModifiedDT;
    m_sLanguage = sLanguage;
    m_aErrorCodes = aErrorCodes;
  }

  @Nonnull
  public DCatAPDatasetType getAsDataset ()
  {
    final DCatAPDatasetType ret = new DCatAPDatasetType ();
    for (final String sDescription : m_aDescriptions)
      ret.addContent (new eu.toop.edm.jaxb.dcterms.ObjectFactory ().createDescription (sDescription));
    for (final String sTitle : m_aTitles)
      ret.addContent (new eu.toop.edm.jaxb.dcterms.ObjectFactory ().createTitle (sTitle));
    if (StringHelper.hasText (m_sID))
      ret.addContent (new eu.toop.edm.jaxb.dcterms.ObjectFactory ().createIdentifier (m_sID));
    return ret;
  }

  @Nonnull
  public static Builder builder ()
  {
    return new Builder ();
  }

  public static class Builder
  {
    private final ICommonsList <String> m_aDescriptions = new CommonsArrayList <> ();
    private final ICommonsList <String> m_aTitles = new CommonsArrayList <> ();
    private String m_sID;
    private LocalDateTime m_aIssuedDT;
    private LocalDateTime m_aLastModifiedDT;
    private String m_sLanguage;
    private ICommonsList <String> m_aErrorCodes;

    public Builder ()
    {}

    @Nonnull
    public Builder addDescription (@Nullable final String s)
    {
      if (StringHelper.hasText (s))
        m_aDescriptions.add (s);
      return this;
    }

    @Nonnull
    public Builder description (@Nullable final String s)
    {
      if (StringHelper.hasText (s))
        m_aDescriptions.set (s);
      else
        m_aDescriptions.clear ();
      return this;
    }

    @Nonnull
    public Builder addTitle (@Nullable final String s)
    {
      if (StringHelper.hasText (s))
        m_aTitles.add (s);
      return this;
    }

    @Nonnull
    public Builder title (@Nullable final String s)
    {
      if (StringHelper.hasText (s))
        m_aTitles.set (s);
      else
        m_aTitles.clear ();
      return this;
    }

    @Nonnull
    public Builder id (@Nullable final String s)
    {
      m_sID = s;
      return this;
    }

    @Nonnull
    public Builder issued (@Nullable final LocalDateTime a)
    {
      m_aIssuedDT = a;
      return this;
    }

    @Nonnull
    public Builder lastModified (@Nullable final LocalDateTime a)
    {
      m_aLastModifiedDT = a;
      return this;
    }

    @Nonnull
    public Builder language (@Nullable final String s)
    {
      m_sLanguage = s;
      return this;
    }

    @Nonnull
    public Builder addErrorCode (@Nullable final String s)
    {
      if (StringHelper.hasText (s))
        m_aErrorCodes.add (s);
      return this;
    }

    @Nonnull
    public Builder errorCode (@Nullable final String s)
    {
      if (StringHelper.hasText (s))
        m_aErrorCodes.set (s);
      else
        m_aErrorCodes.clear ();
      return this;
    }

    @Nonnull
    public DatasetPojo build ()
    {
      return new DatasetPojo (m_aDescriptions,
                              m_aTitles,
                              m_sID,
                              m_aIssuedDT,
                              m_aLastModifiedDT,
                              m_sLanguage,
                              m_aErrorCodes);
    }
  }
}
