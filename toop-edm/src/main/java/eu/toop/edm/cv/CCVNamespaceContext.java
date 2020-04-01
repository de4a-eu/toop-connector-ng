package eu.toop.edm.cv;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Singleton;
import com.helger.ubl20.UBL20NamespaceContext;
import com.helger.xml.namespace.MapBasedNamespaceContext;

/**
 * XML Namespace context for CPSV
 *
 * @author Philip Helger
 */
@Singleton
public class CCVNamespaceContext extends MapBasedNamespaceContext
{
  private static final class SingletonHolder
  {
    static final CCVNamespaceContext s_aInstance = new CCVNamespaceContext ();
  }

  protected CCVNamespaceContext ()
  {
    // Include UBL 2.0
    addMappings (UBL20NamespaceContext.getInstance ());
    addMapping ("cva", "http://www.w3.org/ns/corevocabulary/AggregateComponents");
    addMapping ("cvb", "http://www.w3.org/ns/corevocabulary/BasicComponents");
    addMapping ("clv", "http://www.w3.org/ns/corevocabulary/location");
    addMapping ("cbv", "http://www.w3.org/ns/corevocabulary/business");
    addMapping ("cpv", "http://www.w3.org/ns/corevocabulary/person");
  }

  @Nonnull
  public static CCVNamespaceContext getInstance ()
  {
    return SingletonHolder.s_aInstance;
  }
}
