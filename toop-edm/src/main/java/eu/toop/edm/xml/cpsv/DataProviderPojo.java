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
package eu.toop.edm.xml.cpsv;

import javax.annotation.Nonnull;

import com.helger.commons.string.StringHelper;

import eu.toop.edm.jaxb.cpsv.helper.AgentType;
import eu.toop.edm.jaxb.w3.cv.ac.CvidentifierType;
import eu.toop.edm.jaxb.w3.cv.bc.IdentifierType;
import eu.toop.edm.jaxb.w3.cv.bc.IdentifierTypeType;

public class DataProviderPojo
{
  private final String m_sID;
  private final String m_sIDType;
  private final String m_sName;

  public DataProviderPojo (final String sID, final String sIDType, final String sName)
  {
    m_sID = sID;
    m_sIDType = sIDType;
    m_sName = sName;
  }

  @Nonnull
  public AgentType getAsAgent ()
  {
    final AgentType ret = new AgentType ();
    if (StringHelper.hasText (m_sID))
    {
      final CvidentifierType aAgentID = new CvidentifierType ();
      final IdentifierType aID = new IdentifierType ();
      aID.setValue (m_sID);
      aAgentID.setIdentifier (aID);
      if (StringHelper.hasText (m_sIDType))
      {
        final IdentifierTypeType aIdentifierType = new IdentifierTypeType ();
        aIdentifierType.setValue (m_sIDType);
        aAgentID.setIdentifierType (aIdentifierType);
      }
      ret.setAgentID (aAgentID);
    }
    ret.setAgentName (m_sName);
    return ret;
  }
}
