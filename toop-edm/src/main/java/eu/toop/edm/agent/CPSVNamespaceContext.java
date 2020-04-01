package eu.toop.edm.agent;

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
public class CPSVNamespaceContext extends MapBasedNamespaceContext
{
  private static final class SingletonHolder
  {
    static final CPSVNamespaceContext s_aInstance = new CPSVNamespaceContext ();
  }

  protected CPSVNamespaceContext ()
  {
    addMappings (UBL20NamespaceContext.getInstance ());
    addMapping ("cpsvh", "http://data.europa.eu/m8g/CPSVAP2.0.Helper");
    addMapping ("cpsv", "http://data.europa.eu/m8g/CPSVAP2.0");
    addMapping ("cva", "http://www.w3.org/ns/corevocabulary/AggregateComponents");
    addMapping ("cvb", "http://www.w3.org/ns/corevocabulary/BasicComponents");
  }

  @Nonnull
  public static CPSVNamespaceContext getInstance ()
  {
    return SingletonHolder.s_aInstance;
  }
}
