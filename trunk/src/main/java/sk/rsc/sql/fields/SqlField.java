package sk.rsc.sql.fields;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * SqlField.
 *
 * @author Radovan Sninsky
 * @since 2012-07-14 12:15
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
