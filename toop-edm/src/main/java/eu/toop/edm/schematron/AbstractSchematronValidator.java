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
package eu.toop.edm.schematron;

import javax.annotation.Nonnull;

import org.w3c.dom.Document;

import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.io.resource.IReadableResource;
import com.helger.schematron.ISchematronResource;
import com.helger.schematron.svrl.AbstractSVRLMessage;
import com.helger.schematron.svrl.SVRLHelper;
import com.helger.schematron.svrl.jaxb.SchematronOutputType;
import com.helger.schematron.xslt.SchematronResourceXSLT;
import com.helger.xml.serialize.read.DOMReader;

/**
 * TOOP Schematron validator for the 1.2.0 data model. Validate DOM documents or
 * other resources using the predefined TOOP Schematron rules.
 *
 * @author Philip Helger
 */
public abstract class AbstractSchematronValidator
{
  protected AbstractSchematronValidator ()
  {}

  /**
   * @return The Schematron XSLT rule resource. May not be <code>null</code>.
   */
  @Nonnull
  protected abstract IReadableResource getSchematronXSLTResource ();

  /**
   * Create a new {@link ISchematronResource} that is configured correctly so
   * that it can be used to validate TOOP messages. This method is only used
   * internally and is extracted to allow potential modifications in derived
   * classes.
   *
   * @return A new instance every time.
   * @see #validateDocument(Document)
   * @see #validateResource(IReadableResource)
   */
  @Nonnull
  public final ISchematronResource createSchematronResource ()
  {
    final IReadableResource aRes = getSchematronXSLTResource ();
    final SchematronResourceXSLT aSchematron = new SchematronResourceXSLT (aRes);
    if (!aSchematron.isValidSchematron ())
      throw new IllegalStateException ("Failed to compile Schematron/XSLT " + aRes.getPath ());
    return aSchematron;
  }

  @Nonnull
  @ReturnsMutableCopy
  public final ICommonsList <AbstractSVRLMessage> validateResource (@Nonnull final IReadableResource aXML)
  {
    // Parse XML to DOM
    final Document aXMLDoc = DOMReader.readXMLDOM (aXML);
    if (aXMLDoc == null)
      throw new IllegalStateException ("Failed to read the provided XML");

    return validateDocument (aXMLDoc);
  }

  /**
   * Validate the provided DOM representation of a TOOP Request or Response.
   *
   * @param aXMLDoc
   *        The XML DOM node to be validated. May not be <code>null</code>.
   * @return The list of all failed asserts/successful reports
   */
  @Nonnull
  @ReturnsMutableCopy
  public final ICommonsList <AbstractSVRLMessage> validateDocument (@Nonnull final Document aXMLDoc)
  {
    try
    {
      final ISchematronResource aSchematron = createSchematronResource ();
      // No base URI needed since Schematron contains no includes
      final SchematronOutputType aSOT = aSchematron.applySchematronValidationToSVRL (aXMLDoc, null);
      return SVRLHelper.getAllFailedAssertionsAndSuccessfulReports (aSOT);
    }
    catch (final Exception ex)
    {
      throw new IllegalStateException ("Error applying SCH onto XML", ex);
    }
  }
}
