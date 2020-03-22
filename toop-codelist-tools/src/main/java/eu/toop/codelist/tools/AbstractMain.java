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
package eu.toop.codelist.tools;

import javax.annotation.Nonnull;

import com.helger.commons.version.Version;

/**
 * Abstract base class with shared ideas
 *
 * @author Philip Helger
 */
abstract class AbstractMain
{
  public static final Version CODELIST_VERSION = new Version (2, 0, 0);
  public static final String DO_NOT_EDIT = "This file was automatically generated.\nDo NOT edit!";
  public static final String CODELIST_XLSX_DIR = "../../toop/Code Lists/";
  private static final String CL_XML_DIRECTORY = CODELIST_XLSX_DIR;

  protected AbstractMain ()
  {}

  @Nonnull
  protected static final String getDocTypFilePrefix ()
  {
    return CL_XML_DIRECTORY + "ToopDocumentTypeIdentifiers-v" + CODELIST_VERSION.getAsString (false);
  }

  @Nonnull
  protected static final String getParticipantIdentifierSchemesFilePrefix ()
  {
    return CL_XML_DIRECTORY + "ToopParticipantIdentifierSchemes-v" + CODELIST_VERSION.getAsString (false);
  }

  @Nonnull
  protected static final String getProcessFilePrefix ()
  {
    return CL_XML_DIRECTORY + "ToopProcessIdentifiers-v" + CODELIST_VERSION.getAsString (false);
  }

  @Nonnull
  protected static final String getTransportProfilesPrefix ()
  {
    return CL_XML_DIRECTORY + "ToopTransportProfiles-v" + CODELIST_VERSION.getAsString (false);
  }
}
