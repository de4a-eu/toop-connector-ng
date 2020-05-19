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
package eu.toop.connector.api.smp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.error.level.EErrorLevel;
import com.helger.commons.log.LogHelper;

import eu.toop.edm.error.IToopErrorCode;

public class LoggingSMPErrorHandler implements ISMPErrorHandler
{
  public static final LoggingSMPErrorHandler INSTANCE = new LoggingSMPErrorHandler ();
  private static final Logger LOGGER = LoggerFactory.getLogger (LoggingSMPErrorHandler.class);

  public void onMessage (final EErrorLevel eErrorLevel, final String sMsg, final Throwable t, final IToopErrorCode eCode)
  {
    LogHelper.log (LOGGER, eErrorLevel, sMsg, t);
  }
}
