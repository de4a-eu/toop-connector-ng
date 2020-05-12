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
package eu.toop.edm.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.helger.commons.datetime.PDTFactory;
import com.helger.commons.mock.CommonsTestHelper;

import eu.toop.edm.jaxb.cv.cac.PeriodType;

/**
 * Test class for class {@link PeriodPojo}
 *
 * @author Philip Helger
 */
public final class PeriodPojoTest
{
  @Test
  public void testBasic ()
  {
    final PeriodPojo x = PeriodPojo.builder ()
                                   .startDateTime (PDTFactory.getCurrentLocalDateTime ().minusMonths (1))
                                   .endDateTime (PDTFactory.getCurrentLocalDateTime ().plusMonths (7))
                                   .build ();

    final PeriodType a = x.getAsPeriod ();
    assertNotNull (a);

    final PeriodPojo y = PeriodPojo.builder (a).build ();
    CommonsTestHelper.testEqualsImplementationWithEqualContentObject (x, y);
  }

  @Test
  public void testDateOnly ()
  {
    final PeriodPojo x = PeriodPojo.builder ()
                                   .startDate (PDTFactory.getCurrentLocalDate ().minusMonths (1))
                                   .endDate (PDTFactory.getCurrentLocalDate ().plusMonths (7))
                                   .build ();

    final PeriodType a = x.getAsPeriod ();
    assertNotNull (a);

    final PeriodPojo y = PeriodPojo.builder (a).build ();
    CommonsTestHelper.testEqualsImplementationWithEqualContentObject (x, y);
  }

  @Test
  public void testMinimum ()
  {
    final PeriodPojo x = PeriodPojo.builder ().build ();
    assertNotNull (x);

    final PeriodType a = x.getAsPeriod ();
    assertNotNull (a);

    final PeriodPojo y = PeriodPojo.builder (a).build ();
    CommonsTestHelper.testEqualsImplementationWithEqualContentObject (x, y);
  }
}
