package eu.toop.edm.cpsv.agent;

import com.helger.jaxb.GenericJAXBMarshaller;

import eu.toop.edm.cpsv.CCPSV;
import eu.toop.edm.cpsv.CPSVNamespaceContext;
import eu.toop.edm.jaxb.cpsv.helper.AgentType;
import eu.toop.edm.jaxb.cpsv.helper.ObjectFactory;

public class AgentMarshaller extends GenericJAXBMarshaller <AgentType>
{
  public AgentMarshaller ()
  {
    super (AgentType.class, CCPSV.XSDS, x -> new ObjectFactory ().createAgent (x));
    setNamespaceContext (CPSVNamespaceContext.getInstance ());
  }
}
