package sk.rsc.sql;

import java.util.Date;

/**
 * Restriction factory.
 *
 * @author Radovan Sninsky
 * @since 1.0, 5/21/12 6:00 PM
 */
public final class Restrictions {

	public static Restriction eq(String field, Object value) {
		return new SimpleRestriction(field, Op.EQ, value);
	}

	public static Restriction startWith(String field, String value) {
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
}
