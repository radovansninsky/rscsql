package sk.rsc.sql.joins;

public class LeftOuterJoin extends AbstractJoin {
  public LeftOuterJoin(String tab, String id1, String id2) {
    super(tab, id1, id2);
  }

  @Override
  String getClause() {
    return "left outer join";
  }
}
