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

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.helger.bdve.EValidationType;
import com.helger.bdve.artefact.ValidationArtefact;
import com.helger.bdve.execute.IValidationExecutor;
import com.helger.bdve.execute.ValidationExecutorSchematron;
import com.helger.bdve.execute.ValidationExecutorXSD;
import com.helger.bdve.executorset.VESID;
import com.helger.bdve.executorset.ValidationExecutorSet;
import com.helger.bdve.executorset.ValidationExecutorSetRegistry;
import com.helger.commons.ValueEnforcer;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.io.resource.IReadableResource;
import com.helger.jaxb.builder.IJAXBDocumentType;
import com.helger.ubl21.UBL21NamespaceContext;

import eu.toop.edm.schematron.SchematronBusinessRules2Validator;
import eu.toop.edm.schematron.SchematronEDM2Validator;
import eu.toop.edm.xml.cagv.CCAGV;
import eu.toop.edm.xml.cccev.CCCEV;
import eu.toop.regrep.CRegRep4;
import eu.toop.regrep.RegRep4Reader;

/**
 * Generic TOOP EDM v2 validation configuration
 *
 * @author Philip Helger
 */
@Immutable
public final class TCValidationRules
{
  public static final String GROUP_ID = "eu.toop";

  public static final VESID VID_TOOP_EDM_REQUEST_200 = new VESID (GROUP_ID, "edm-request", "2.0.0");
  public static final VESID VID_TOOP_EDM_RESPONSE_200 = new VESID (GROUP_ID, "edm-response", "2.0.0");
  public static final VESID VID_TOOP_EDM_ERROR_RESPONSE_200 = new VESID (GROUP_ID, "edm-error-response", "2.0.0");

  private TCValidationRules ()
  {}

  @Nonnull
  private static ClassLoader _getCL ()
  {
    return TCValidationRules.class.getClassLoader ();
  }

  @Nonnull
  private static IValidationExecutor _createXSLT (@Nonnull final IReadableResource aRes)
  {
    return new ValidationExecutorSchematron (new ValidationArtefact (EValidationType.SCHEMATRON_XSLT, _getCL (), aRes),
                                             null,
                                             UBL21NamespaceContext.getInstance ());
  }

  private static final ClassPathResource TOOP_BUSINESS_RULES_XSLT = SchematronBusinessRules2Validator.TOOP_BUSINESS_RULES_XSLT;
  private static final ClassPathResource TOOP_EDM2_XSLT = SchematronEDM2Validator.TOOP_EDM2_XSLT;

  /**
   * Register all standard TOOP EDM v2 validation execution sets to the provided
   * registry.
   *
   * @param aRegistry
   *        The registry to add the artefacts. May not be <code>null</code>.
   */
  public static void initToopEDM (@Nonnull final ValidationExecutorSetRegistry aRegistry)
  {
    ValueEnforcer.notNull (aRegistry, "Registry");

    final boolean bNotDeprecated = false;

    // Request
    {
      final IJAXBDocumentType aDT = RegRep4Reader.queryRequest (CCAGV.XSDS).getJAXBDocumentType ();
      aRegistry.registerValidationExecutorSet (ValidationExecutorSet.create (VID_TOOP_EDM_REQUEST_200,
                                                                             "TOOP EDM Request 2.0.0",
                                                                             bNotDeprecated,
                                                                             new ValidationExecutorXSD (new ValidationArtefact (EValidationType.XSD,
                                                                                                                                _getCL (),
                                                                                                                                CRegRep4.getXSDResourceQuery ()),
                                                                                                        () -> aDT.getSchema ()),
                                                                             _createXSLT (TOOP_EDM2_XSLT),
                                                                             _createXSLT (TOOP_BUSINESS_RULES_XSLT)));
    }

    // Response
    {
      final IJAXBDocumentType aDT = RegRep4Reader.queryResponse (CCCEV.XSDS).getJAXBDocumentType ();
      aRegistry.registerValidationExecutorSet (ValidationExecutorSet.create (VID_TOOP_EDM_RESPONSE_200,
                                                                             "TOOP EDM Response 2.0.0",
                                                                             bNotDeprecated,
                                                                             new ValidationExecutorXSD (new ValidationArtefact (EValidationType.XSD,
                                                                                                                                _getCL (),
                                                                                                                                CRegRep4.getXSDResourceQuery ()),
                                                                                                        () -> aDT.getSchema ()),
                                                                             _createXSLT (TOOP_EDM2_XSLT),
                                                                             _createXSLT (TOOP_BUSINESS_RULES_XSLT)));
    }

    // Error Response
    {
      final IJAXBDocumentType aDT = RegRep4Reader.queryResponse ().getJAXBDocumentType ();
      aRegistry.registerValidationExecutorSet (ValidationExecutorSet.create (VID_TOOP_EDM_ERROR_RESPONSE_200,
                                                                             "TOOP EDM Error Response 2.0.0",
                                                                             bNotDeprecated,
                                                                             new ValidationExecutorXSD (new ValidationArtefact (EValidationType.XSD,
                                                                                                                                _getCL (),
                                                                                                                                CRegRep4.getXSDResourceQuery ()),
                                                                                                        () -> aDT.getSchema ()),
                                                                             _createXSLT (TOOP_EDM2_XSLT),
                                                                             _createXSLT (TOOP_BUSINESS_RULES_XSLT)));
    }
  }
}
