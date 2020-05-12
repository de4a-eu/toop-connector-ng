package eu.toop.edm.xml;

import javax.annotation.Nonnull;

import com.helger.jaxb.IJAXBWriter;

public final class JAXBVersatileWriter <T> implements IJAXBVersatileWriter <T>
{
  private final T m_aObject;
  private final IJAXBWriter <T> m_aWriter;

  public JAXBVersatileWriter (@Nonnull final T aObject, @Nonnull final IJAXBWriter <T> aWriter)
  {
    m_aObject = aObject;
    m_aWriter = aWriter;
  }

  @Nonnull
  public T getObjectToWrite ()
  {
    return m_aObject;
  }

  @Nonnull
  public IJAXBWriter <T> getWriter ()
  {
    return m_aWriter;
  }
}
