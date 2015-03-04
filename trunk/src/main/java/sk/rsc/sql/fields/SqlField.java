package sk.rsc.sql.fields;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Trieda XXX.
 *
 * @author Radovan Sninsky
 * @since 1.0, 7/14/12 12:15 AM
 */
public class SqlField extends Field {

	public SqlField(String field, String value) {
		super(field, value);
	}

	@Override
	public Object getValue() {
		return super.getValue();
	}

	@Override
	public void setStmtValue(PreparedStatement stmt, int i) throws SQLException {
	}

	@Override
	public String toValueSql() {
		return super.getValue() != null ? super.getValue().toString() : "";
	}
}
