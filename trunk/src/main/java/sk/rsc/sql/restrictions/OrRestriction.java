package sk.rsc.sql.restrictions;

import java.util.ArrayList;
import java.util.List;

/**
 * Or Restriction.
 *
 * @author Radovan Sninsky
 * @since 30.07.2012 11:29
 */
public class OrRestriction implements Restriction {

	private Restriction a, b;

	public OrRestriction(Restriction a, Restriction b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public boolean hasValues() {
		return true;
	}

	@Override
	public List<Object> getValues() {
		List<Object> list = new ArrayList<Object>();
		list.addAll(a.getValues());
		list.addAll(b.getValues());
		return list;
	}

	@Override
	public String toSql() {
		return new StringBuilder()
			.append("(").append(a.toSql()).append(' ')
			.append(Op.OR.getOp()).append(" ")
			.append(b.toSql()).append(")")
			.toString();
	}
}
