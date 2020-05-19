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
package eu.toop.connector.api.me.outgoing;

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
