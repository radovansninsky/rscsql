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
 * @since 21.05.2012 18:00
 */
public final class Restrictions {

	public static Restriction eq(String field, Object value) {
		return new SimpleRestriction(field, Op.EQ, value);
	}

	public static Restriction startWith(String field, String value) {
		return new SimpleRestriction(field, Op.LIKE, value+"%");
	}

	public static Restriction endWith(String field, String value) {
		return new SimpleRestriction(field, Op.LIKE, value+"%");
	}

	public static Restriction like(String field, String value) {
		return new SimpleRestriction(field, Op.LIKE, "%"+value+"%");
	}

	public static Restriction ne(String field, Object value) {
		return new SimpleRestriction(field, Op.NE, value);
	}

	public static Restriction between(String field, Date dateFrom, Date dateTo) {
		return new BetweenRestriction(field, dateFrom, dateTo);
	}

	public static Restriction or(Restriction a, Restriction b) {
		return new OrRestriction(a, b);
	}

	public static Restriction sql(String text) {
		return new NativeRestriction(text);
	}

  public static Restriction in(String field, List values) {
    return new InRestriction(field, values);
  }

	public static Restriction in(String field, Object... values) {
		return new InRestriction(field, Arrays.asList(values));
	}
}
