package sk.rsc.sql;

/**
 * Order.
 *
 * @author Radovan Sninsky
 * @since 1.0, 5/16/12 11:12 PM
 */
public final class Order {

  private String field;
  private boolean asc;

  public Order(String field, boolean asc) {
    this.field = field;
    this.asc = asc;
  }

  public static Order asc(String field) {
    return new Order(field, true);
  }

  public static Order desc(String field) {
    return new Order(field, false);
  }

  public String toSql() {
    final StringBuilder sb = new StringBuilder();
    sb.append(field).append(' ').append(asc ? "asc" : "desc");
    return sb.toString();
  }
}
