package eu.toop.connector.api.me.out;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import eu.toop.connector.api.me.MEException;
import eu.toop.edm.error.IToopErrorCode;

/**
 * Exception when sending messages via ME
 *
 * @author Philip Helger
 */
public class MEOutgoingException extends MEException
{
  private IToopErrorCode m_aErrorCode;

  public MEOutgoingException (@Nullable final String sMsg)
  {
    super (sMsg);
    m_aErrorCode = null;
  }

  public MEOutgoingException (@Nullable final String sMsg, @Nullable final Throwable aCause)
  {
    super (sMsg, aCause);
    m_aErrorCode = null;
  }

  public MEOutgoingException (@Nonnull final IToopErrorCode aErrorCode, @Nullable final Throwable aCause)
  {
    this ("TOOP Error " + aErrorCode.getID (), aCause);
    m_aErrorCode = aErrorCode;
  }

  public MEOutgoingException (@Nonnull final IToopErrorCode aErrorCode, @Nullable final String sMsg)
  {
    this ("TOOP Error " + aErrorCode.getID () + " - " + sMsg);
    m_aErrorCode = aErrorCode;
  }

  @Nullable
  public IToopErrorCode getToopErrorCode ()
  {
    return m_aErrorCode;
  }
}
