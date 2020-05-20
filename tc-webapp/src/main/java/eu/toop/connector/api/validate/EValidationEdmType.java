package eu.toop.connector.api.validate;

import javax.annotation.Nonnull;

import com.helger.bdve.executorset.VESID;
import com.helger.commons.annotation.Nonempty;

public enum EValidationEdmType
{
  REQUEST ("req", ToopEdm2Validation.VID_TOOP_EDM_REQUEST_200),
  RESPONSE ("resp", ToopEdm2Validation.VID_TOOP_EDM_RESPONSE_200),
  ERROR_RESPONSE ("errresp", ToopEdm2Validation.VID_TOOP_EDM_ERROR_RESPONSE_200);

  private final String m_sID;
  private final VESID m_aVESID;

  EValidationEdmType (@Nonnull @Nonempty final String sID, @Nonnull final VESID aVESID)
  {
    m_sID = sID;
    m_aVESID = aVESID;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  @Nonnull
  public VESID getVESID ()
  {
    return m_aVESID;
  }
}
