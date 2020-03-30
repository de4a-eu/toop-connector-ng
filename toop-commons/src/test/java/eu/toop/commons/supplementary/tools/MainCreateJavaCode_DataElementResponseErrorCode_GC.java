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
package eu.toop.commons.supplementary.tools;

import java.io.File;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.regex.RegExHelper;
import com.helger.genericode.Genericode10Helper;
import com.helger.genericode.builder.GenericodeReader;
import com.helger.genericode.v10.CodeListDocument;
import com.helger.genericode.v10.Row;

/**
 * Extract error code enum content from Genericode file
 *
 * @author Philip Helger
 */
public final class MainCreateJavaCode_DataElementResponseErrorCode_GC
{
  private static final Logger LOGGER = LoggerFactory.getLogger (MainCreateJavaCode_DataElementResponseErrorCode_GC.class);

  public static void main (final String [] args)
  {
    final CodeListDocument aCLD = GenericodeReader.gc10CodeList ()
                                                  .read (new File ("src/test/resources/codelists/gc/DataElementResponseErrorCode-CodeList.gc"));
    final StringBuilder aSB = new StringBuilder ();
    for (final Row aRow : aCLD.getSimpleCodeList ().getRow ())
    {
      final String sID = Genericode10Helper.getRowValue (aRow, "code");
      final String sName = Genericode10Helper.getRowValue (aRow, "name-en");
      aSB.append ("/** ").append (sName).append (" */\n");
      aSB.append (RegExHelper.getAsIdentifier (sID.toUpperCase (Locale.US)))
         .append (" (\"")
         .append (sID)
         .append ("\", \"")
         .append (sName)
         .append ("\"),\n");
    }
    LOGGER.info (aSB.toString ());
  }
}
