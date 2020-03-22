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
package eu.toop.commons.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.regex.RegExHelper;

public class CliCommandTest
{
  @Test
  public void parseEmptyCommandList ()
  {
    try
    {
      CliCommand.parse (new ArrayList <> (), true);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {
      // Expected
    }
  }

  @Test
  public void parseOneCommand ()
  {
    final CliCommand singleCommand = CliCommand.parse (Arrays.asList ("singleCommand"), true);

    assertEquals ("singleCommand", singleCommand.getMainCommand ());
    assertNull (singleCommand.getOptions ());
  }

  @Test
  public void parseComplexCommand () throws Exception
  {
    final String s = "sampleCommand optionwithoutdash1 optionwithoutdash2 -f file1 -q -t option1 option2 -c option3";
    final String [] tokens = RegExHelper.getSplitToArray (s, "\\s");
    final CliCommand singleCommand = CliCommand.parse (new CommonsArrayList <> (tokens), true);

    assertEquals ("sampleCommand", singleCommand.getMainCommand ());
    assertNotNull (singleCommand.getOptions ());

    assertArrayEquals (new String [] { "optionwithoutdash1", "optionwithoutdash2" },
                       singleCommand.getEmptyParameters ().toArray ());
    assertArrayEquals (new String [] { "file1" }, singleCommand.getArguments ("f").toArray ());
    assertArrayEquals (new String [0], singleCommand.getArguments ("q").toArray ());
    assertArrayEquals (new String [] { "option1", "option2" }, singleCommand.getArguments ("t").toArray ());
    assertArrayEquals (new String [] { "option3" }, singleCommand.getArguments ("c").toArray ());
  }
}
