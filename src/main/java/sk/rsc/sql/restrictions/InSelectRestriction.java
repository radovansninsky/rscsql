package sk.rsc.sql.restrictions;

import sk.rsc.sql.SqlSelect;

import java.util.ArrayList;
import java.util.List;

/**
 * InSelectRestriction.
 *
 * @author Radovan Sninsky
 * @since 2017-11-25 20:46
 */
public class InSelectRestriction implements Restriction {

  private final String field;
  private final SqlSelect select;

  public InSelectRestriction(String field, SqlSelect select) {
    this.field = field;
    this.select = select;
  }

  @Override
  public boolean hasValues() {
    return !select.listRestrictions().isEmpty();
  }

  @Override
  public List<Object> getValues() {
    return new ArrayList<>(select.listRestrictions());
  }

  @Override
  public String toSql() {
    return field + " in (" + select.toSql() + ")";
  }
}
