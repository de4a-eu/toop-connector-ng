package eu.toop.edm.error;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import eu.toop.regrep.query.QueryExceptionType;
import eu.toop.regrep.rs.AuthenticationExceptionType;
import eu.toop.regrep.rs.AuthorizationExceptionType;
import eu.toop.regrep.rs.InvalidRequestExceptionType;
import eu.toop.regrep.rs.ObjectExistsExceptionType;
import eu.toop.regrep.rs.ObjectNotFoundExceptionType;
import eu.toop.regrep.rs.QuotaExceededExceptionType;
import eu.toop.regrep.rs.ReferencesExistExceptionType;
import eu.toop.regrep.rs.RegistryExceptionType;
import eu.toop.regrep.rs.TimeoutExceptionType;
import eu.toop.regrep.rs.UnresolvedReferenceExceptionType;
import eu.toop.regrep.rs.UnsupportedCapabilityExceptionType;

/**
 * Contains the different possible exception types.
 *
 * @author Philip Helger
 */
public enum EEDMExceptionType
{
  /**
   * Generated when a client sends a request with authentication credentials and
   * the authentication fails for any reason.
   */
  AUTHENTICATION (AuthenticationExceptionType::new),
  /**
   * Generated when a client sends a request to the server for which it is not
   * authorized.
   */
  AUTHORIZATION (AuthorizationExceptionType::new),
  /**
   * Generated when a client sends a request that is syntactically or
   * semantically invalid.
   */
  INVALID_REQUEST (InvalidRequestExceptionType::new),
  /**
   * Generated when a SubmitObjectsRequest attempts to create an object with the
   * same id as an existing object and the mode is “CreateOnly”.
   */
  OBJECT_EXISTS (ObjectExistsExceptionType::new),
  /**
   * Generated when a QueryRequest expects an object but it is not found in
   * server.
   */
  OBJECT_NOT_FOUND (ObjectNotFoundExceptionType::new),
  /**
   * Generated when a a request exceeds a server specific quota for the client.
   */
  QUOTA_EXCEEDED (QuotaExceededExceptionType::new),
  /**
   * Generated when a RemoveObjectRequest attempts to remove a RegistryObject
   * while references to it still exist.
   */
  REFERENCES_EXIST (ReferencesExistExceptionType::new),
  /**
   * Generated when a the processing of a request exceeds a server specific
   * timeout period.
   */
  TIMEOUT (TimeoutExceptionType::new),
  /**
   * Generated when a request references an object that cannot be resolved
   * within the request or to an existing object in the server.
   */
  UNRESOLVED_REFERENCE (UnresolvedReferenceExceptionType::new),
  /**
   * Generated when when a request attempts to use an optional feature or
   * capability that the server does not support.
   */
  UNSUPPORTED_CAPABILITY (UnsupportedCapabilityExceptionType::new),
  /**
   * Generated when the query syntax or semantics was invalid. Client must fix
   * the query syntax or semantic error and re-submit the query
   */
  QUERY (QueryExceptionType::new);

  private final Supplier <? extends RegistryExceptionType> m_aInvoker;

  <T extends RegistryExceptionType> EEDMExceptionType (@Nonnull final Supplier <T> aInvoker)
  {
    m_aInvoker = aInvoker;
  }

  @Nonnull
  public RegistryExceptionType invoke ()
  {
    return m_aInvoker.get ();
  }
}
