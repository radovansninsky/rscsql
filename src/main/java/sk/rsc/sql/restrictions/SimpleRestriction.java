package sk.rsc.sql.restrictions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Simple restriction for field based on single operation represented by {@link Op} and value.
 *
 * @author Radovan Sninsky
 * @since 2012-05-16 23:11
 * @see Op for operations definition
 */
public final class SimpleRestriction implements Restriction {

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
    return Collections.singletonList(value);
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
    return field + ' ' + op.getOp() + " ?";
  }
}
