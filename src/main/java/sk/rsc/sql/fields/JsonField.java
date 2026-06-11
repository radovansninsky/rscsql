package sk.rsc.sql.fields;

import sk.rsc.sql.Sql;

public class JsonField extends Field {

  public JsonField(String field, String value) {
    super(field, value);
  }

  @Override
  public String toValueSql() {
    if (Sql.isPostgres()) {
      return "?::jsonb";
    } else {
      return super.toValueSql();
    }
  }
}
