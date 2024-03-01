package sk.rsc.sql.test.vendors;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import sk.rsc.sql.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

/**
 * PostgresSelectTest.
 *
 * @author Radovan Sninsky
 * @since 2017-10-11 14:03
 */
public class PostgresSelectTest {

  private Connection conn;

  @BeforeClass(enabled = true, groups = {"psql"})
  public void setUpPsql() throws SQLException {
    conn = DriverManager.getConnection("jdbc:postgresql://172.18.0.20:5432/samdb", "sam", "sam");
  }

  @AfterClass(enabled = true, groups = {"psql"})
  public void tearDownPsql() {
    if (conn != null) { try { conn.close(); } catch (Exception ignored) { } }
  }


  @Test(enabled = true, groups = {"psql"})
  public void testJoinedFieldWithStar() throws SQLException {
    SqlSelect<Item> select = new Sql<Item>(conn)
      .select("*", "at.id, at.first_name, at.surname, at.birth, at.death")
      .from("item")
      .leftJoin("author at", "author_id", "at.id");

    List<Item> list = select.list(ITEM_LIST, 0, 2);
    assertNotNull(list);
    assertEquals(list.size(), 2);
    assertNotNull(list.get(0));
    assertNotNull(list.get(0).author);
    assertEquals(list.get(0).authorId, list.get(0).author.id);
  }

  @Test(enabled = true, groups = {"psql"})
  public void testJoinedFieldWithTablePrefixByMapper() throws SQLException {
    SqlSelect<Item> select = new Sql<Item>(conn)
      .select("item.id", "inc_id", "item.name", "author_id", "at.id, at.first_name, at.surname, at.birth, at.death")
      .from("item")
      .leftJoin("author at", "author_id", "at.id");

    List<Item> list = select.list(ITEM_LIST, 0, 4);
    assertNotNull(list);
    assertEquals(list.size(), 4);
    assertNotNull(list.get(0));
    assertNotNull(list.get(0).author);
    assertEquals(list.get(0).authorId, list.get(0).author.id);
  }

  @Test(enabled = true, groups = {"psql"})
  public void testJoinedFieldWithTablePrefixByRow() throws SQLException {
    SqlSelect<Item> select = new Sql<Item>(conn)
      .select("item.id", "inc_id", "item.name", "author_id", "at.id, at.first_name, at.surname, at.birth, at.death")
      .from("item")
      .leftJoin("author at", "author_id", "at.id");

    final List<Item> list = new ArrayList<>();
    select.iterate(4, new RowHandler<Row>() {
      @Override
      protected void handle(Row r) {
        Author a = new Author();
        a.id = r.getLong("at.id");
        a.firstname = r.get("at.first_name");
        a.lastname = r.get("at.surname");
        a.birth = r.get("at.birth");
        a.death = r.get("at.death");

        Item i = new Item();
        i.id = r.getLong("id");
        i.incId = r.getLong("inc_id");
        i.name = r.get("name");
        i.authorId = r.getLong("author_id");
        i.author = a;

        list.add(i);
      }
    });
    assertNotNull(list);
    assertEquals(list.size(), 4);
    assertNotNull(list.get(0));
    assertNotNull(list.get(0).author);
    assertEquals(list.get(0).authorId, list.get(0).author.id);
  }

  final class Author {
    Long id;
    String firstname;
    String lastname;
    String birth;
    String death;
  }

  final class Item {
    Long id;
    Long incId;
    String name;
    Long authorId;
    Author author;
  }

  Mapper<Item> ITEM_LIST = new Mapper<Item>() {
    @Override
    protected Item toObject() throws SQLException {
      Author a = new Author();
      a.id = getLong("at.id");
      a.firstname = get("at.first_name");
      a.lastname = get("at.surname");
      a.birth = get("at.birth");
      a.death = get("at.death");

      Item i = new Item();
      i.id = getLong("id");
      i.incId = getLong("inc_id");
      i.name = get("name");
      i.authorId = getLong("author_id");
      i.author = a;
      return i;
    }
  };

  @Test(groups = {"psql"})
  void testSettingBooleanValue() throws SQLException {
    new Sql(conn).update("customer")
      .set("swp", false
      )
      .where(Restrictions.eq("id", 277))
      .execute();
  }
}
