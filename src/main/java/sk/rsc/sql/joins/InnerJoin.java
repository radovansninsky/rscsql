package sk.rsc.sql.joins;

public class InnerJoin extends AbstractJoin {
  public InnerJoin(String tab, String id1, String id2) {
    super(tab, id1, id2);
  }

  @Override
  String getClause() {
    return "inner join";
  }
}
