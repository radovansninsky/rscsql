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

  private String field;
  private SqlSelect select;

  public InSelectRestriction(String field, SqlSelect select) {
    this.field = field;
    this.select = select;
  }

  @Override
  public boolean hasValues() {
    return select.listRestrictions().size() > 0;
  }

  @Override
  public List<Object> getValues() {
    List<Object> list = new ArrayList<>();
    list.addAll(select.listRestrictions());
    return list;
  }

  @Override
  public String toSql() {
    return new StringBuilder().append(field).append(" in (").append(select.toSql()).append(")").toString();
  }
}
