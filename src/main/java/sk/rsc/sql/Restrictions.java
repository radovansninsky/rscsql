package sk.rsc.sql;

import sk.rsc.sql.restrictions.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Restriction factory, simplify writing where clauses.
 * It is recomended to import class staticaly.
 *
 * @author Radovan Sninsky
 * @since 2012-05-21 18:00
 */
@SuppressWarnings("unused")
public final class Restrictions {

  /**
   * Is Equals clause.
   * @param field  field
   * @param value  value
   * @return initialized Restriction with equals operation
   */
  public static Restriction eq(String field, Object value) {
    return new SimpleRestriction(field, Op.EQ, value);
  }

  /**
   * Is not equals clause.
   * @param field  field
   * @param value  value
   * @return initialized Restriction with not equals operation
   */
  public static Restriction ne(String field, Object value) {
    return new SimpleRestriction(field, Op.NE, value);
  }

  /**
   * Is greater clause.
   * @param field  field
   * @param value  value
   * @return initialized Restriction with greater then operation
   */
  public static Restriction gt(String field, Object value) {
    return new SimpleRestriction(field, Op.GT, value);
  }

  /**
   * Is gerater or equals clause.
   * @param field  field
   * @param value  value
   * @return initialized Restriction with greater or equals operation
   */
  public static Restriction ge(String field, Object value) {
    return new SimpleRestriction(field, Op.GE, value);
  }

  /**
   * Is lower clause.
   * @param field  field
   * @param value  value
   * @return initialized Restriction with lower then operation
   */
  public static Restriction lt(String field, Object value) {
    return new SimpleRestriction(field, Op.LT, value);
  }

  /**
   * Is lower or equals clause.
   * @param field  field
   * @param value  value
   * @return initialized Restriction with lower or equals operation
   */
  public static Restriction le(String field, Object value) {
    return new SimpleRestriction(field, Op.LE, value);
  }

  /**
   * Like with arbitrary string at the end clause.
   * @param field  field
   * @param value  value
   * @return initialized Restriction with like operation
   */
  public static Restriction startWith(String field, String value) {
    return new SimpleRestriction(field, Op.LIKE, value + "%");
  }

  /**
   * Like with arbitrary string at the start clause.
   * @param field  field
   * @param value  value
   * @return initialized Restriction with like operation
   */
  public static Restriction endWith(String field, String value) {
    return new SimpleRestriction(field, Op.LIKE, value + "%");
  }

  /**
   * Like with arbitrary string at the start and end clause.
   * @param field  field
   * @param value  value
   * @return initialized Restriction with like operation
   */
  public static Restriction like(String field, String value) {
    return new SimpleRestriction(field, Op.LIKE, "%" + value + "%");
  }

  /**
   * Is between clause.
   * @param field     field
   * @param dateFrom  from date
   * @param dateTo    to date
   * @return initialized BetweenRestriction
   */
  public static Restriction between(String field, Date dateFrom, Date dateTo) {
    return new BetweenRestriction(field, dateFrom, dateTo);
  }

  /**
   * Logical or clause.
   * @param a  1st restriction
   * @param b  2nd restriction
   * @return initialized OrRestriction
   */
  public static Restriction or(Restriction a, Restriction b) {
    return new OrRestriction(a, b);
  }

  /**
   * Arbitrary sql clause with no parameter. Its purpose is to have posibility to write sql clause
   * not covered by build in clauses.
   * @param text  sql restriction
   * @return initialized NativeRestriction
   */
  public static Restriction sql(String text) {
    return new NativeRestriction(text);
  }

  /**
   * Is in array of values clause.
   * @param field   field
   * @param values  list of any values
   * @return initialized InRestriction
   */
  public static Restriction in(String field, List values) {
    return new InValuesRestriction(field, values);
  }

  /**
   * Is in array of values clause.
   * @param field   field
   * @param values  varargs of any values
   * @return initialized InRestriction
   */
  public static Restriction in(String field, Object... values) {
    return new InValuesRestriction(field, Arrays.asList(values));
  }

  /**
   * Is in select clause.
   * @param field   field
   * @param select  sql select
   * @return initialized InRestriction
   */
  public static Restriction in(String field, SqlSelect select) {
    return new InSelectRestriction(field, select);
  }

  /**
   * Is before clause.
   * @param field   field
   * @param date    date value
   * @return initialized Restriction with lower then operation
   */
  public static Restriction before(String field, Date date) {
    return new SimpleRestriction(field, Op.LT, date);
  }

  /**
   * Is before or equals clause.
   * @param field   field
   * @param date    date value
   * @return initialized Restriction with lower or equals operation
   */
  public static Restriction beforeEq(String field, Date date) {
    return new SimpleRestriction(field, Op.LE, date);
  }

  /**
   * Is after clause.
   * @param field   field
   * @param date    date value
   * @return initialized Restriction with greater then operation
   */
  public static Restriction after(String field, Date date) {
    return new SimpleRestriction(field, Op.GT, date);
  }

  /**
   * Is after or equals clause.
   * @param field   field
   * @param date    date value
   * @return initialized Restriction with greater or equals then operation
   */
  public static Restriction afterEq(String field, Date date) {
    return new SimpleRestriction(field, Op.GE, date);
  }
}
