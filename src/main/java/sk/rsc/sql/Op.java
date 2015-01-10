package sk.rsc.sql;

import java.io.Serializable;

/**
 * Op.
 *
 * @author Radovan Sninsky
 * @since 1.0, 5/18/12 10:12 AM
 */
public enum Op implements Serializable {

	EQ("="),
	NE("!="),
	GT(">"),
	GE(">="),
	LT("<"),
	LE("<="),
	LIKE("like"),
	NLIKE("not like"),
	ISNULL("is null"),
	ISNOTNULL("is not null"),
	OR("or");

	private String op;

	Op() {
	}

	Op(String op) {
		this.op = op;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}
}
