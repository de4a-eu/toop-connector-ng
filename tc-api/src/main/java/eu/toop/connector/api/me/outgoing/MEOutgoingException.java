/**
 * This work is protected under copyrights held by the members of the
 * TOOP Project Consortium as indicated at
 * http://wiki.ds.unipi.gr/display/TOOP/Contributors
 * (c) 2018-2021. All rights reserved.
 *
 * This work is dual licensed under Apache License, Version 2.0
 * and the EUPL 1.2.
 *
 *  = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
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
 *
 *  = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved
 * by the European Commission - subsequent versions of the EUPL
 * (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 *         https://joinup.ec.europa.eu/software/page/eupl
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
  private final IToopErrorCode m_aErrorCode;

  protected MEOutgoingException (@Nullable final String sMsg, @Nullable final Throwable aCause, @Nullable final IToopErrorCode aErrorCode)
  {
    super (sMsg, aCause);
    m_aErrorCode = aErrorCode;
  }

  public MEOutgoingException (@Nullable final String sMsg)
  {
    this (sMsg, null, null);
  }

  public MEOutgoingException (@Nullable final String sMsg, @Nullable final Throwable aCause)
  {
    this (sMsg, aCause, null);
  }

  public MEOutgoingException (@Nonnull final IToopErrorCode aErrorCode, @Nullable final Throwable aCause)
  {
    this ("TOOP Error " + aErrorCode.getID (), aCause, aErrorCode);
  }

  public MEOutgoingException (@Nonnull final IToopErrorCode aErrorCode, @Nullable final String sMsg)
  {
    this ("TOOP Error " + aErrorCode.getID () + " - " + sMsg, null, aErrorCode);
  }

  @Nullable
  public final IToopErrorCode getToopErrorCode ()
  {
    return m_aErrorCode;
  }
}
