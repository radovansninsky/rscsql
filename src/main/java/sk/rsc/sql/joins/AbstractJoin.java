package sk.rsc.sql.joins;

public abstract class AbstractJoin {
  String tab;
  String id1;
  String id2;

  AbstractJoin(String tab, String id1, String id2) {
    this.tab = tab;
    this.id1 = id1;
    this.id2 = id2;
  }

  abstract String getClause();

  public String toSql() {
    return getClause() + " " + tab + " on " + id1 + " = " + id2;
  }
}
