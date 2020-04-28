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
package eu.toop.edm.xml.cagv;

import javax.annotation.Nonnull;

import com.helger.commons.string.StringHelper;

import eu.toop.edm.jaxb.cv.agent.AgentType;
import eu.toop.edm.jaxb.cv.cbc.IDType;
import eu.toop.edm.jaxb.cv.cbc.NameType;

public class DataProviderPojo
{
  private final String m_sID;
  private final String m_sIDSchemeID;
  private final String m_sName;

  public DataProviderPojo (final String sID, final String sIDSchemeID, final String sName)
  {
    m_sID = sID;
    m_sIDSchemeID = sIDSchemeID;
    m_sName = sName;
  }

  @Nonnull
  public AgentType getAsAgent ()
  {
    final AgentType ret = new AgentType ();
    if (StringHelper.hasText (m_sID))
    {
      final IDType aID = new IDType ();
      aID.setValue (m_sID);
      aID.setSchemeID (m_sIDSchemeID);
      ret.addId (aID);
    }
    if (StringHelper.hasText (m_sName))
    {
      final NameType aName = new NameType ();
      aName.setValue (m_sName);
      ret.addName (aName);
    }
    return ret;
  }
}
