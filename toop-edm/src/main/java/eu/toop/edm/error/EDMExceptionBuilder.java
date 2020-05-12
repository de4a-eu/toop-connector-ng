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
package eu.toop.edm.error;

import java.time.LocalDateTime;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.datetime.PDTFactory;
import com.helger.commons.string.StringHelper;

import eu.toop.edm.slot.SlotErrorOrigin;
import eu.toop.edm.slot.SlotTimestamp;
import eu.toop.regrep.rs.RegistryExceptionType;

/**
 * Build TOOP EDM exceptions to be used in TOOP error responses.
 *
 * @author Philip Helger
 */
public class EDMExceptionBuilder
{
  private EEDMExceptionType m_eExceptionType;
  private EToopErrorSeverity m_eSeverity;
  private String m_sErrorMessage;
  private String m_sErrorDetail;
  private String m_sErrorCode;
  private LocalDateTime m_aTimestamp;
  private String m_sErrorOrigin;

  public EDMExceptionBuilder ()
  {}

  @Nonnull
  public EDMExceptionBuilder exceptionType (@Nullable final EEDMExceptionType x)
  {
    m_eExceptionType = x;
    return this;
  }

  @Nonnull
  public EDMExceptionBuilder severityFailure ()
  {
    return severity (EToopErrorSeverity.FAILURE);
  }

  @Nonnull
  public EDMExceptionBuilder severity (@Nullable final EToopErrorSeverity x)
  {
    m_eSeverity = x;
    return this;
  }

  @Nonnull
  public EDMExceptionBuilder errorMessage (@Nullable final String x)
  {
    m_sErrorMessage = x;
    return this;
  }

  @Nonnull
  public EDMExceptionBuilder errorDetail (@Nullable final String x)
  {
    m_sErrorDetail = x;
    return this;
  }

  @Nonnull
  public EDMExceptionBuilder errorCode (@Nullable final IToopErrorCode x)
  {
    return errorCode (x == null ? null : x.getID ());
  }

  @Nonnull
  public EDMExceptionBuilder errorCode (@Nullable final String x)
  {
    m_sErrorCode = x;
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

  public void checkConsistency ()
  {
    if (m_eExceptionType == null)
      throw new IllegalStateException ("Exception Type must be provided");
    if (m_eSeverity == null)
      throw new IllegalStateException ("Error Severity must be provided");
    if (StringHelper.hasNoText (m_sErrorMessage))
      throw new IllegalStateException ("Error Message must be provided");
    if (m_aTimestamp == null)
      throw new IllegalStateException ("Timestamp must be provided");
  }

  @Nonnull
  public RegistryExceptionType build ()
  {
    checkConsistency ();

    final RegistryExceptionType ret = m_eExceptionType.invoke ();
    if (m_eSeverity != null)
      ret.setSeverity (m_eSeverity.getID ());
    ret.setMessage (m_sErrorMessage);
    ret.setDetail (m_sErrorDetail);
    ret.setCode (m_sErrorCode);

    if (m_aTimestamp != null)
      ret.addSlot (new SlotTimestamp (m_aTimestamp).createSlot ());
    if (StringHelper.hasText (m_sErrorOrigin))
      ret.addSlot (new SlotErrorOrigin (m_sErrorOrigin).createSlot ());

    return ret;
  }
}
