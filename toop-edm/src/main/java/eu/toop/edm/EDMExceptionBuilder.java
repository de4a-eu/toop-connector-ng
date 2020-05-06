package eu.toop.edm;

import java.time.LocalDateTime;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.datetime.PDTFactory;
import com.helger.commons.string.StringHelper;
import com.helger.commons.text.IMultilingualText;

import eu.toop.edm.error.EEDMExceptionType;
import eu.toop.edm.error.EToopErrorOrigin;
import eu.toop.edm.error.EToopErrorSeverity;
import eu.toop.edm.slot.SlotErrorCategory;
import eu.toop.edm.slot.SlotErrorText;
import eu.toop.edm.slot.SlotErrorOrigin;
import eu.toop.edm.slot.SlotPublicOrganizationIdentifier;
import eu.toop.edm.slot.SlotTimestamp;
import eu.toop.regrep.helper.VocabularyTerm;
import eu.toop.regrep.rs.RegistryExceptionType;

/**
 * Build TOOP EDM exceptions to be used in TOOP error responses.
 *
 * @author Philip Helger
 */
public class EDMExceptionBuilder
{
  private EEDMExceptionType m_eExceptionType;
  private String m_sErrorCode;
  private String m_sErrorDetail;
  private String m_sErrorMessage;
  private EToopErrorSeverity m_eSeverity;
  private VocabularyTerm m_aPublicOrganizationID;
  private LocalDateTime m_aTimestamp;
  private String m_sErrorOrigin;
  private String m_sErrorCategory;
  // TODO not used in beta1
  private final ICommonsList <IMultilingualText> m_aErrorTexts = new CommonsArrayList <> ();

  public EDMExceptionBuilder ()
  {}

  @Nonnull
  public EDMExceptionBuilder exceptionType (@Nullable final EEDMExceptionType x)
  {
    m_eExceptionType = x;
    return this;
  }

  @Nonnull
  public EDMExceptionBuilder errorCode (@Nullable final String x)
  {
    m_sErrorCode = x;
    return this;
  }

  @Nonnull
  public EDMExceptionBuilder errorDetail (@Nullable final String x)
  {
    m_sErrorDetail = x;
    return this;
  }

  @Nonnull
  public EDMExceptionBuilder errorMessage (@Nullable final String x)
  {
    m_sErrorMessage = x;
    return this;
  }

  @Nonnull
  public EDMExceptionBuilder severity (@Nullable final EToopErrorSeverity x)
  {
    m_eSeverity = x;
    return this;
  }

  @Nonnull
  public EDMExceptionBuilder publicOrganizationID (@Nullable final String sVocabulary, @Nullable final String sTerm)
  {
    return publicOrganizationID (StringHelper.hasText (sVocabulary) &&
                                 StringHelper.hasText (sTerm) ? new VocabularyTerm (sVocabulary, sTerm) : null);
  }

  @Nonnull
  public EDMExceptionBuilder publicOrganizationID (@Nullable final VocabularyTerm x)
  {
    m_aPublicOrganizationID = x;
    return this;
  }

  @Nonnull
  public EDMExceptionBuilder timestampNow ()
  {
    return timestamp (PDTFactory.getCurrentLocalDateTime ());
  }

  @Nonnull
  public EDMExceptionBuilder timestamp (@Nullable final LocalDateTime x)
  {
    m_aTimestamp = x;
    return this;
  }

  @Nonnull
  public EDMExceptionBuilder errorOrigin (@Nullable final EToopErrorOrigin x)
  {
    return errorOrigin (x == null ? null : x.getID ());
  }

  @Nonnull
  public EDMExceptionBuilder errorOrigin (@Nullable final String x)
  {
    m_sErrorOrigin = x;
    return this;
  }

  @Nonnull
  public EDMExceptionBuilder errorCategory (@Nullable final String x)
  {
    m_sErrorCategory = x;
    return this;
  }

  public void checkConsistency ()
  {
    if (m_eExceptionType == null)
      throw new IllegalStateException ("Exception Type must be provided");
    if (StringHelper.hasNoText (m_sErrorCode))
      throw new IllegalStateException ("Error Code must be provided");
    if (StringHelper.hasNoText (m_sErrorMessage))
      throw new IllegalStateException ("Error Message must be provided");
    if (m_eSeverity == null)
      throw new IllegalStateException ("Error Severity must be provided");
    if (m_aTimestamp == null)
      throw new IllegalStateException ("Timestamp must be provided");
    if (StringHelper.hasNoText (m_sErrorOrigin))
      throw new IllegalStateException ("Error Origin must be provided");
    if (StringHelper.hasNoText (m_sErrorCategory))
      throw new IllegalStateException ("Error Category must be provided");
  }

  @Nonnull
  public RegistryExceptionType build ()
  {
    checkConsistency ();

    final RegistryExceptionType ret = m_eExceptionType.invoke ();
    ret.setCode (m_sErrorCode);
    ret.setDetail (m_sErrorDetail);
    ret.setMessage (m_sErrorMessage);
    if (m_eSeverity != null)
      ret.setSeverity (m_eSeverity.getID ());

    if (m_aPublicOrganizationID != null)
      ret.addSlot (new SlotPublicOrganizationIdentifier (m_aPublicOrganizationID).createSlot ());
    if (m_aTimestamp != null)
      ret.addSlot (new SlotTimestamp (m_aTimestamp).createSlot ());
    if (StringHelper.hasText (m_sErrorOrigin))
      ret.addSlot (new SlotErrorOrigin (m_sErrorOrigin).createSlot ());
    if (StringHelper.hasText (m_sErrorCategory))
      ret.addSlot (new SlotErrorCategory (m_sErrorCategory).createSlot ());
    if (m_aErrorTexts.isNotEmpty ())
      ret.addSlot (new SlotErrorText (m_aErrorTexts).createSlot ());

    return ret;
  }
}
