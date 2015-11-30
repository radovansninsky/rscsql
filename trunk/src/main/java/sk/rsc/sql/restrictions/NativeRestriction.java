package sk.rsc.sql.restrictions;

import java.util.List;

/**
 * Native restriction.
 *
 * @author Radovan Sninsky
 * @since 2012-05-21 17:59
 */
public final class NativeRestriction implements Restriction {

  private String text;

  public NativeRestriction(String text) {
    this.text = text;
  }

  @Override
  public boolean hasValues() {
    return false;
  }

  @Override
  public List<Object> getValues() {
    return null;
  }

  @Override
  public String toSql() {
    return text;
  }
}
