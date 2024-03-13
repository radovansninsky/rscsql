package sk.rsc.sql.joins;

public class RightOuterJoin extends AbstractJoin {
  public RightOuterJoin(String tab, String id1, String id2) {
    super(tab, id1, id2);
  }

  @Override
  String getClause() {
    return "right outer join";
  }
}
