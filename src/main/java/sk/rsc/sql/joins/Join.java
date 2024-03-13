package sk.rsc.sql.joins;

public class Join extends AbstractJoin {
  public Join(String tab, String id1, String id2) {
    super(tab, id1, id2);
  }

  @Override
  String getClause() {
    return "join";
  }
}
