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
package eu.toop.commons.concept;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.MustImplementEqualsAndHashcode;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.ToStringGenerator;

/**
 * This is a single value that consist of a namespace and a concept name. Such a
 * concept value is used in semantic mapping.
 *
 * @author Philip Helger
 */
@Immutable
@MustImplementEqualsAndHashcode
public final class ConceptValue implements Serializable
{
  private final String m_sNamespace;
  private final String m_sValue;

  public ConceptValue (@Nonnull @Nonempty final String sNamespace, @Nonnull @Nonempty final String sValue)
  {
    ValueEnforcer.notEmpty (sNamespace, "Namespace");
    ValueEnforcer.notEmpty (sValue, "Value");
    m_sNamespace = sNamespace;
    m_sValue = sValue;
  }

  /**
   * @return The namespace as provided in the constructor. Neither
   *         <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  public String getNamespace ()
  {
    return m_sNamespace;
  }

  /**
   * Check if this concept has the provided namespace or not.
   *
   * @param sNamespace
   *        The namespace to compare to. May be <code>null</code>.
   * @return <code>true</code> if the namespaces are identical.
   */
  public boolean hasNamespace (@Nullable final String sNamespace)
  {
    return m_sNamespace.equals (sNamespace);
  }

  /**
   * @return The value as provided in the constructor. Neither <code>null</code>
   *         nor empty.
   */
  @Nonnull
  @Nonempty
  public String getValue ()
  {
    return m_sValue;
  }

  /**
   * Check if this concept value has the provided value or not.
   *
   * @param sValue
   *        The values to compare to. May be <code>null</code>.
   * @return <code>true</code> if the values are identical.
   */
  public boolean hasValue (@Nullable final String sValue)
  {
    return m_sValue.equals (sValue);
  }

  /**
   * @param sSep
   *        The separator to be used. May not be <code>null</code>.
   * @return namespace + separator + value
   */
  @Nonnull
  public String getConcatenatedValue (@Nonnull final String sSep)
  {
    return m_sNamespace + sSep + m_sValue;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final ConceptValue rhs = (ConceptValue) o;
    return m_sNamespace.equals (rhs.m_sNamespace) && m_sValue.equals (rhs.m_sValue);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sNamespace).append (m_sValue).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (null).append ("Namespace", m_sNamespace).append ("Value", m_sValue).getToString ();
  }
}
