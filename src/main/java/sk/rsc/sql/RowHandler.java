package sk.rsc.sql;

import java.sql.SQLException;

/**
 * Trieda RowHandler.
 *
 * @author Radovan Sninsky
 * @since 9.4.2013 15:23
 */
public abstract class RowHandler<T> {

	protected abstract void handle(T obj) throws SQLException;
}
