package sk.rsc.sql.restrictions;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents sql in operation, if field is in list of values.
 *
 * @author Radovan Sninsky
 * @since 2015-03-27 13:37
 */
public final class InValuesRestriction implements Restriction {

  private String field;
  private List values;

  public InValuesRestriction(String field, List values) {
    this.field = field;
    this.values = values;
  }

  public void setField(String field) {
    this.field = field;
  }

  @Override
  public boolean hasValues() {
    return true;
  }

  @Override
  public List<Object> getValues() {
    return new ArrayList<Object>(values);
  }

  @Override
  public String toSql() {
    StringBuilder sb = new StringBuilder().append(field).append(" in (");
    for (int i=0; i<values.size(); i++) {
      sb.append("?").append(i==values.size()-1 ? ")" : ", ");
    }
    return sb.toString();
  }
}
