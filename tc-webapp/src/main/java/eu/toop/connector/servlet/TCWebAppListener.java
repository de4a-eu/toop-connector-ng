package eu.toop.connector.servlet;

import javax.servlet.ServletContext;

import com.helger.photon.api.APIDescriptor;
import com.helger.photon.api.APIPath;
import com.helger.photon.api.IAPIRegistry;
import com.helger.photon.core.servlet.WebAppListener;

import eu.toop.connector.api.ApiGetDsdDp;
import eu.toop.connector.api.ApiGetDsdDpByCountry;
import eu.toop.connector.app.TCInit;

public class TCWebAppListener extends WebAppListener
{
  @Override
  protected void afterContextInitialized (final ServletContext aSC)
  {
    TCInit.initGlobally (aSC);
  }

  @Override
  protected void initAPI (final IAPIRegistry aAPIRegistry)
  {
    aAPIRegistry.registerAPI (new APIDescriptor (APIPath.get ("/dsd/dp/{dataset}"), ApiGetDsdDp.class));
    aAPIRegistry.registerAPI (new APIDescriptor (APIPath.get ("/dsd/dp/{dataset}/by-country/{country}"), ApiGetDsdDpByCountry.class));
  }

  @Override
  protected void beforeContextDestroyed (final ServletContext aSC)
  {
    TCInit.shutdownGlobally (aSC);
  }
}
