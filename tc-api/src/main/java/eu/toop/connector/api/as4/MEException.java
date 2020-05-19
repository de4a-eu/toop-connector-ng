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
package eu.toop.connector.api.as4;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import eu.toop.edm.error.EToopErrorCode;

/**
 * A separate runtime exception to make it easier for the users to distinguish
 * between the 'source path' to the underlying problem.
 *
 * @author yerlibilgin
 */
public class MEException extends RuntimeException
{
  private final EToopErrorCode m_eToopErrorCode;

  public MEException (final String sMsg)
  {
    this (sMsg, (Throwable) null);
  }

  public MEException (final Throwable aCause)
  {
    this ((String) null, aCause);
  }

  public MEException (final String sMsg, final Throwable aCause)
  {
    super (sMsg, aCause);
    m_eToopErrorCode = null;
  }

  public MEException (@Nonnull final EToopErrorCode toopErrorCode)
  {
    super (toopErrorCode.name ());
    m_eToopErrorCode = toopErrorCode;
  }

  public MEException (@Nonnull final EToopErrorCode toopErrorCode, final Throwable aCause)
  {
    super (toopErrorCode.name (), aCause);
    m_eToopErrorCode = toopErrorCode;
  }

  public MEException (@Nonnull final EToopErrorCode toopErrorCode, final String sMsg)
  {
    super (toopErrorCode.name () + " - " + sMsg);
    m_eToopErrorCode = toopErrorCode;
  }

  @Nullable
  public final EToopErrorCode getToopErrorCode ()
  {
    return m_eToopErrorCode;
  }
}
