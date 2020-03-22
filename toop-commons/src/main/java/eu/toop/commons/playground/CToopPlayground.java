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
package eu.toop.commons.playground;

import javax.annotation.concurrent.Immutable;

import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.io.resource.IReadableResource;
import com.helger.security.keystore.EKeyStoreType;
import com.helger.security.keystore.IKeyStoreType;

/**
 * Playground constants
 *
 * @author Philip Helger
 */
@Immutable
public final class CToopPlayground
{
  /** Trust store file type */
  public static final IKeyStoreType TYPE_PLAYGROUND_TRUST_STORE = EKeyStoreType.JKS;
  /** Trust store classpath */
  public static final IReadableResource PATH_PLAYGROUND_TRUST_STORE = new ClassPathResource ("/truststore/playground-truststore-v4.1.jks");
  /** Trust store password */
  public static final String PASSWORD_PLAYGROUND_TRUST_STORE = "toop4eu";

  private CToopPlayground ()
  {}
}
