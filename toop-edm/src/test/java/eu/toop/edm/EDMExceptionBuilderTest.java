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
package eu.toop.edm;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.toop.edm.error.EEDMExceptionType;
import eu.toop.edm.error.EToopErrorOrigin;
import eu.toop.edm.error.EToopErrorSeverity;
import eu.toop.regrep.RegRep4Writer;
import eu.toop.regrep.rs.RegistryExceptionType;

/**
 * Test class for class {@link EDMExceptionBuilder}.
 *
 * @author Philip Helger
 */
public final class EDMExceptionBuilderTest
{
  private static final Logger LOGGER = LoggerFactory.getLogger (EDMExceptionBuilderTest.class);

  @Test
  public void testBasic ()
  {
    for (final EEDMExceptionType e : EEDMExceptionType.values ())
    {
      final EDMExceptionBuilder b = new EDMExceptionBuilder ().exceptionType (e)
                                                              .errorCode ("ec1")
                                                              .errorDetail ("Stacktrace")
                                                              .errorMessage ("What went wrong: nothing")
                                                              .severity (EToopErrorSeverity.FAILURE)
                                                              .timestampNow ()
                                                              .errorOrigin (EToopErrorOrigin.RESPONSE_RECEPTION);
      final RegistryExceptionType aEx = b.build ();
      assertNotNull (aEx);

      final RegRep4Writer <RegistryExceptionType> aWriter = RegRep4Writer.registryException ()
                                                                         .setFormattedOutput (true);
      final String sXML = aWriter.getAsString (aEx);
      assertNotNull (sXML);

      if (false)
        LOGGER.info (sXML);
    }
  }

  @Test
  public void testMinimum ()
  {
    final EDMExceptionBuilder b = new EDMExceptionBuilder ().exceptionType (EEDMExceptionType.OBJECT_NOT_FOUND)
                                                            .severityFailure ()
                                                            .errorMessage ("What went wrong: nothing")
                                                            .timestampNow ();
    final RegistryExceptionType aEx = b.build ();
    assertNotNull (aEx);

    final RegRep4Writer <RegistryExceptionType> aWriter = RegRep4Writer.registryException ().setFormattedOutput (true);
    final String sXML = aWriter.getAsString (aEx);
    assertNotNull (sXML);

    if (true)
      LOGGER.info (sXML);
  }
}
