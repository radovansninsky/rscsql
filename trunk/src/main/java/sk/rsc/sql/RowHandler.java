package sk.rsc.sql;

import java.sql.SQLException;

/**
 * Handles object represents one row in selected table when selected by {@link SqlSelect}.
 *
 * @author Radovan Sninsky
 * @since 2013-04-09 15:23
 * @see SqlSelect#iterate(RowHandler) and similar for usage
 */
public abstract class RowHandler<T> {

	protected abstract void handle(T obj) throws SQLException;
}
