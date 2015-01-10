package sk.rsc.sql;

import java.util.List;

/**
 * Native restriction.
 *
 * @author Radovan Sninsky
 * @since 1.0, 5/21/12 5:59 PM
 */
public class NativeRestriction implements Restriction {

	private String text;

	public NativeRestriction(String text) {
		this.text = text;
	}

	@Override
	public boolean hasValues() {
		return false;
	}

	@Override
	public List<Object> getValues() {
		return null;
	}

	@Override
	public String toSql() {
		return text;
	}
}
