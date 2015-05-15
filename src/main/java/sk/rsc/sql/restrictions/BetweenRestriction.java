package sk.rsc.sql.restrictions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents sql between operation, if field is between two dates.
 *
 * @author Radovan Sninsky
 * @since 13.06.2012 22:21
 */
public final class BetweenRestriction implements Restriction {

  private String field;
  private Date dateFrom;
  private Date dateTo;

  public BetweenRestriction(String field, Date dateFrom, Date dateTo) {
    this.field = field;
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
  }

  public BetweenRestriction(String field, long dateFrom, long dateTo) {
    this.field = field;
    this.dateFrom = new Date();
    this.dateFrom.setTime(dateFrom);
    this.dateTo = new Date();
    this.dateTo.setTime(dateTo);
  }

  @Override
  public boolean hasValues() {
    return true;
  }

  @Override
  public List<Object> getValues() {
    List<Object> list = new ArrayList<Object>();
    list.add(dateFrom);
    list.add(dateTo);
    return list;
  }

  @Override
  public String toSql() {
    return new StringBuilder().append(field).append(" between ? and ?").toString();
  }
}
