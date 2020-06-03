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
package eu.toop.connector.app.validation;

import java.util.Locale;

import javax.annotation.Nonnull;

import org.w3c.dom.Document;

import com.helger.bdve.EValidationType;
import com.helger.bdve.artefact.ValidationArtefact;
import com.helger.bdve.execute.ValidationExecutionManager;
import com.helger.bdve.executorset.IValidationExecutorSet;
import com.helger.bdve.executorset.VESID;
import com.helger.bdve.executorset.ValidationExecutorSetRegistry;
import com.helger.bdve.result.ValidationResult;
import com.helger.bdve.result.ValidationResultList;
import com.helger.bdve.source.ValidationSource;
import com.helger.commons.error.list.ErrorList;
import com.helger.commons.io.resource.inmemory.ReadableResourceByteArray;
import com.helger.xml.EXMLParserFeature;
import com.helger.xml.sax.WrappedCollectingSAXErrorHandler;
import com.helger.xml.serialize.read.DOMReader;
import com.helger.xml.serialize.read.DOMReaderSettings;

import eu.toop.connector.api.validation.IVSValidator;

/**
 * The default implementation of {@link IVSValidator}
 *
 * @author Philip Helger
 */
public class TCValidator implements IVSValidator
{
  private static final ValidationExecutorSetRegistry VER = new ValidationExecutorSetRegistry ();
  static
  {
    // Init all TOOP rules
    TCValidationRules.initToopEDM (VER);
  }

  @Nonnull
  public static ValidationExecutorSetRegistry internalGetRegistry ()
  {
    return VER;
  }

  @Nonnull
  public static IValidationExecutorSet getVES (@Nonnull final VESID aVESID)
  {
    final IValidationExecutorSet aVES = VER.getOfID (aVESID);
    if (aVES == null)
      throw new IllegalStateException ("Unexpected VESID '" + aVESID.getAsSingleID () + "'");
    return aVES;
  }

  public TCValidator ()
  {}

  @Nonnull
  public ValidationResultList validate (@Nonnull final VESID aVESID, @Nonnull final byte [] aPayload, @Nonnull final Locale aDisplayLocale)
  {
    final ValidationExecutionManager aValidator = getVES (aVESID).createExecutionManager ();
    final ErrorList aXMLErrors = new ErrorList ();
    final ValidationResultList aValidationResultList = new ValidationResultList ();

    final ReadableResourceByteArray aXMLRes = new ReadableResourceByteArray (aPayload);
    final Document aDoc = DOMReader.readXMLDOM (aXMLRes,
                                                new DOMReaderSettings ().setErrorHandler (new WrappedCollectingSAXErrorHandler (aXMLErrors))
                                                                        .setLocale (aDisplayLocale)
                                                                        .setFeatureValues (EXMLParserFeature.AVOID_XML_ATTACKS));
    if (aDoc != null)
    {
      // What to validate?
      final ValidationSource aValidationSource = new ValidationSource ("uploaded content", () -> DOMReader.readXMLDOM (aPayload), false);
      // Start validation
      aValidator.executeValidation (aValidationSource, aValidationResultList, aDisplayLocale);
    }

    // Add all XML parsing stuff - always first item
    // Also add if no error is present to have it shown in the list
    aValidationResultList.add (0, new ValidationResult (new ValidationArtefact (EValidationType.XML, aXMLRes), aXMLErrors));
    return aValidationResultList;
  }
}
