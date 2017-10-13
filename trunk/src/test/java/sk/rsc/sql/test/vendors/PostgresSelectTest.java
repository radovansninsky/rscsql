package sk.rsc.sql.test.vendors;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import sk.rsc.sql.Mapper;
import sk.rsc.sql.Sql;
import sk.rsc.sql.SqlSelect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
  public void tearDownOracle() {
    if (conn != null) { try { conn.close(); } catch (Exception ignored) { } }
  }


  @Test(enabled = true, groups = {"psql"})
  public void testJoinedField() throws SQLException {
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
}
