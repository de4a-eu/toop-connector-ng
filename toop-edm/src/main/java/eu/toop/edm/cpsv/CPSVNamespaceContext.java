package eu.toop.edm.cpsv;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Singleton;
import com.helger.xml.namespace.MapBasedNamespaceContext;

import eu.toop.edm.cv.CCVNamespaceContext;

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
    // Based on CV
    addMappings (CCVNamespaceContext.getInstance ());
    addMapping ("cpsvh", "http://data.europa.eu/m8g/CPSVAP2.0.Helper");
    addMapping ("cpsv", "http://data.europa.eu/m8g/CPSVAP2.0");
  }

  @Nonnull
  public static CPSVNamespaceContext getInstance ()
  {
    return SingletonHolder.s_aInstance;
  }
}
