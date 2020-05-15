package eu.toop.regrep;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;

import eu.toop.regrep.query.QueryRequest;
import eu.toop.regrep.query.QueryResponse;
import eu.toop.regrep.query.ResponseOptionType;
import eu.toop.regrep.rim.QueryType;
import eu.toop.regrep.rim.SlotType;

/**
 * Helper class to simplify the creation of RegRep data constructs.
 *
 * @author Philip Helger
 */
@Immutable
public final class RegRepHelper
{
  private RegRepHelper ()
  {}

  @Nonnull
  public static QueryRequest createEmptyQueryRequest ()
  {
    final QueryRequest ret = new QueryRequest ();
    ret.setId (UUID.randomUUID ().toString ());
    ret.setResponseOption (new ResponseOptionType ());
    return ret;
  }

  @Nonnull
  public static QueryRequest createQueryRequest (@Nonnull @Nonempty final String sQueryDefinition,
                                                 @Nonnull @Nonempty final SlotType... aSlots)
  {
    ValueEnforcer.notEmpty (sQueryDefinition, "sQueryDefinition");
    ValueEnforcer.notEmptyNoNullValue (aSlots, "Slots");
    final QueryRequest ret = createEmptyQueryRequest ();

    final QueryType aQuery = new QueryType ();
    aQuery.setQueryDefinition (sQueryDefinition);
    for (final SlotType aSlot : aSlots)
      aQuery.addSlot (aSlot);
    ret.setQuery (aQuery);
    return ret;
  }

  @Nonnull
  public static QueryResponse createEmptyQueryResponse (@Nonnull final ERegRepResponseStatus eStatus)
  {
    ValueEnforcer.notNull (eStatus, "Status");

    final QueryResponse ret = new QueryResponse ();
    ret.setStatus (eStatus.getID ());
    return ret;
  }

  @Nonnull
  public static QueryResponse createQueryResponse (@Nonnull final ERegRepResponseStatus eStatus,
                                                   @Nullable final String sRequestID,
                                                   @Nonnull @Nonempty final SlotType... aSlots)
  {
    final QueryResponse ret = createEmptyQueryResponse (eStatus);
    ret.setRequestId (sRequestID);
    for (final SlotType aSlot : aSlots)
      ret.addSlot (aSlot);
    return ret;
  }
}
