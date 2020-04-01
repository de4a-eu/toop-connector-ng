package eu.toop.edm.cpsv;

import java.util.List;

import javax.annotation.Nonnull;

import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.io.resource.ClassPathResource;

import eu.toop.edm.cv.CCV;

public final class CCPSV
{
  @Nonnull
  private static final ClassLoader _getCL ()
  {
    return CCPSV.class.getClassLoader ();
  }

  public static final List <ClassPathResource> XSDS;
  static
  {
    final ICommonsList <ClassPathResource> aList = new CommonsArrayList <> ();
    aList.addAll (CCV.XSDS);
    aList.addAll (new ClassPathResource ("schemas/cpsv-ap_xml_schema_v0.01.xsd", _getCL ()),
                  new ClassPathResource ("schemas/cpsv-ap_xml_schema_v0.01.helper.xsd", _getCL ()));
    XSDS = aList.getAsUnmodifiable ();
  }

  private CCPSV ()
  {}
}
