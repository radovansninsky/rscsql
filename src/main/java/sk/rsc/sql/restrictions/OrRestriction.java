package sk.rsc.sql.restrictions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Or Restriction.
 *
 * @author Radovan Sninsky
 * @since 2012-07-30 11:29
 */
public final class OrRestriction implements Restriction {

  private final Restriction[] items;

  public OrRestriction(Restriction... items) {
    this.items = items;
  }

  @Override
  public boolean hasValues() {
    return true;
  }

  @Override
  public List<Object> getValues() {
    return Arrays.stream(items).flatMap(r -> r.getValues().stream()).collect(Collectors.toList());
  }

  @Override
  public String toSql() {
    return
      "(" +
      String.join(" " + Op.OR.getOp() + " ", Arrays.stream(items).map(Restriction::toSql).toArray(String[]::new)) +
      ")";
  }
}
