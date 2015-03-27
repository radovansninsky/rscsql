package sk.rsc.sql.restrictions;

import java.util.Arrays;
import java.util.List;

/**
 * SimpleRestriction.
 *
 * @author Radovan Sninsky
 * @since 1.0, 5/16/12 11:11 PM
 */
public class SimpleRestriction implements Restriction {

	private String field;
	private Op op;
	private Object value;

	public SimpleRestriction() {
	}

	public SimpleRestriction(String field, Object value) {
		this(field, Op.EQ, value);
	}

	public SimpleRestriction(String field, Op op, Object value) {
		this.field = field;
		this.op = op;
		this.value = value;
	}

	@Override
	public boolean hasValues() {
		return true;
	}

	@Override
	public List<Object> getValues() {
		return Arrays.asList(value);
	}

	public void setField(String field) {
		this.field = field;
	}

	public void setOp(Op op) {
		this.op = op;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public String toSql() {
		return new StringBuilder().append(field).append(' ').append(op.getOp()).append(" ?").toString();
	}
}
