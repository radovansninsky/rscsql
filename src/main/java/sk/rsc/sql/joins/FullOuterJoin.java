package sk.rsc.sql.joins;

public class FullOuterJoin extends AbstractJoin {
  public FullOuterJoin(String tab, String id1, String id2) {
    super(tab, id1, id2);
  }

  @Override
  String getClause() {
    return "full outer join";
  }
}
