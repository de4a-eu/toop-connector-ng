package eu.toop.edm.cv;

import java.util.List;

import javax.annotation.Nonnull;

import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.ubl20.CUBL20;

public final class CCV
{
  @Nonnull
  private static final ClassLoader _getCL ()
  {
    return CCV.class.getClassLoader ();
  }

  public static final List <ClassPathResource> XSDS = new CommonsArrayList <> (CUBL20.XSD_UNQUALIFIED_DATA_TYPES,
                                                                               CUBL20.XSD_QUALIFIED_DATA_TYPES,
                                                                               CUBL20.XSD_COMMON_BASIC_COMPONENTS,
                                                                               CUBL20.XSD_COMMON_EXTENSION_COMPONENTS,
                                                                               new ClassPathResource ("schemas/CoreVocabularyBasicComponents-v1.00.xsd",
                                                                                                      _getCL ()),
                                                                               new ClassPathResource ("schemas/CoreVocabularyAggregateComponents-v1.00.xsd",
                                                                                                      _getCL ()),
                                                                               new ClassPathResource ("schemas/CoreLocation-v1.00.xsd",
                                                                                                      _getCL ()),
                                                                               new ClassPathResource ("schemas/CorePerson-v1.00.xsd",
                                                                                                      _getCL ()),
                                                                               new ClassPathResource ("schemas/CoreBusiness-v1.00.xsd",
                                                                                                      _getCL ())).getAsUnmodifiable ();

  private CCV ()
  {}
}
