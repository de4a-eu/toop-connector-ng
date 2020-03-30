package eu.toop.edm.regrep;

import static org.junit.Assert.assertNotNull;

import java.math.BigInteger;
import java.util.Date;
import java.util.Locale;

import javax.annotation.Nonnull;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.helger.commons.collection.impl.CommonsLinkedHashMap;
import com.helger.commons.collection.impl.ICommonsMap;
import com.helger.commons.datetime.PDTFactory;
import com.helger.commons.mutable.MutableInt;
import com.helger.datetime.util.PDTXMLConverter;
import com.helger.xml.serialize.read.DOMReader;

import eu.toop.regrep.RegRep4Writer;
import eu.toop.regrep.query.QueryRequest;
import eu.toop.regrep.rim.ValueType;

/**
 * Test class for class {@link SlotBuilder}
 *
 * @author Philip Helger
 */
public final class SlotBuilderTest
{
  private static final Logger LOGGER = LoggerFactory.getLogger (SlotBuilderTest.class);
  private static final MutableInt COUNTER = new MutableInt (0);

  @Nonnull
  private static SlotBuilder _sb ()
  {
    return new SlotBuilder ().setName ("slot" + COUNTER.inc ());
  }

  @Test
  public void testBasic ()
  {
    final Document aDoc = DOMReader.readXMLDOM ("<root attr='a' xmlns='urn:anything-weird/bla-foo'><child><child2>value</child2></child></root>");
    final ICommonsMap <ValueType, ValueType> aMap = new CommonsLinkedHashMap <> ();
    aMap.put (RegRepHelper.createSlotValue ("Key1"),
              RegRepHelper.createSlotValue (PDTXMLConverter.getXMLCalendarNow ()));
    aMap.put (RegRepHelper.createSlotValue (BigInteger.valueOf (1234)), RegRepHelper.createSlotValue (42f));

    final QueryRequest aQR;
    aQR = RegRepHelper.createQueryRequest ("mock-data-request",
                                           _sb ().setValue (aDoc.getDocumentElement ()).build (),
                                           _sb ().setValue (true).build (),
                                           _sb ().setValue (RegRepHelper.createSlotValue ("ListItem1"),
                                                            RegRepHelper.createSlotValue ("ListItem2"))
                                                 .build (),
                                           _sb ().setValue (PDTXMLConverter.getXMLCalendarNow ()).build (),
                                           _sb ().setValue (PDTFactory.getCurrentLocalDateTime ()).build (),
                                           _sb ().setValue (PDTFactory.getCurrentLocalDate ()).build (),
                                           _sb ().setValue (PDTFactory.getCurrentZonedDateTimeUTC ()).build (),
                                           _sb ().setValue (new Date ()).build (),
                                           _sb ().setValue (3.223344f).build (),
                                           _sb ().setValue (BigInteger.TEN).build (),
                                           _sb ().setValue (11).build (),
                                           _sb ().setValue (12L).build (),
                                           _sb ().setValue (RegRepHelper.createInternationalStringType (RegRepHelper.createLocalizedString (Locale.ENGLISH,
                                                                                                                                            "Qualification Procedure in Public Procurement"),
                                                                                                        RegRepHelper.createLocalizedString (Locale.GERMAN,
                                                                                                                                            "Qualifizierungsverfahren im öffentlichen Beschaffungswesen")))
                                                 .build (),
                                           _sb ().setValue (RegRepHelper.createMap (aMap)).build (),
                                           _sb ().setValue (RegRepHelper.createSlot ("nestedSlot",
                                                                                     RegRepHelper.createSlotValue ("simpleString")))
                                                 .build (),
                                           _sb ().setValue ("text only").build (),
                                           _sb ().setValue (RegRepHelper.createVocabularyTerm ("myVoc", "myTerm"))
                                                 .build (),
                                           _sb ().setVocabularyTermValue ("myVoc2", "myTerm2").build ());
    assertNotNull (RegRep4Writer.queryRequest ().getAsDocument (aQR));

    LOGGER.info (RegRep4Writer.queryRequest ().setFormattedOutput (true).getAsString (aQR));
  }
}
