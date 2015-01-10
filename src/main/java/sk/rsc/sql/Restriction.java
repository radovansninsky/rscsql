package sk.rsc.sql;

import java.util.List;

/**
 * Rezhranie Restriction.
 *
 * @author Radovan Sninsky
 * @since 1.0, 5/21/12 5:56 PM
 */
public interface Restriction {

	public boolean hasValues();

	public List<Object> getValues();

	public String toSql();
}
