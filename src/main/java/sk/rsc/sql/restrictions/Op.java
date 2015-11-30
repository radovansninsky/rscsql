package sk.rsc.sql.restrictions;

import java.io.Serializable;

/**
 * Represents simple sql operation like greater, equals, etc..
 *
 * @author Radovan Sninsky
 * @since 2012-05-18 10:12
 * @see SimpleRestriction for usage
 * @see sk.rsc.sql.Restrictions factory for field restrictions
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
