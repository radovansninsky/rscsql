package sk.rsc.sql.restrictions;

import java.util.List;

/**
 * Restriction interface for sql command objects to work with.
 *
 * @author Radovan Sninsky
 * @since 2012-05-21 17:56
 */
public interface Restriction {

	/**
	 * Indicates if restriction has values.
	 * @return true if restriction has values, false if not
	 */
	boolean hasValues();

	/**
	 * Provides list of values of restriction for setting into jdbc {@link java.sql.PreparedStatement}
	 * @return values of restriction as list
	 */
	List<Object> getValues();

	/**
	 * Builds restriction as string representation for jdbc {@link java.sql.PreparedStatement}.
	 * @return string representation of restriction
	 */
	String toSql();
}
