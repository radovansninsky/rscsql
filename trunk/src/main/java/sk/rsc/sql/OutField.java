package sk.rsc.sql;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Trieda OutField.
 *
 * @author Radovan Sninsky
 * @since 11.10.2013 12:23
 */
public class OutField extends Field {

	private int type;

	public OutField(String field, int type) {
		super(field, null);
    this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public void setStmtValue(PreparedStatement stmt, int i) throws SQLException {
		if (stmt instanceof CallableStatement) {
			((CallableStatement)stmt).registerOutParameter(i, type);
		}
	}
}
