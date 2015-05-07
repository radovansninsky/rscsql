package sk.rsc.sql.test;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import sk.rsc.sql.Restrictions;
import sk.rsc.sql.Row;
import sk.rsc.sql.Sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static org.testng.Assert.*;

/**
 * SelectTest.
 *
 * @author Radovan Sninsky
 * @since 2015-01-19 08:24
 */
public class SelectTest {

  private static final String CREATE_TABLE =
    "create table test1 (desc varchar(512), num1 integer, now timestamp, num2 numeric(12,2))";

  private Connection conn;

  @BeforeClass
  public void setUp() throws SQLException {
    Sql.setSoutLogger();

    conn = DriverManager.getConnection("jdbc:h2:mem:selectdb", "sa", "");
    Statement stmt = conn.createStatement();
    try {
      stmt.executeUpdate(CREATE_TABLE);
      stmt.executeUpdate("insert into test1 (num1, num2) values (1, 1)");
      stmt.executeUpdate("insert into test1 (num1, num2) values (2, 1)");
      stmt.executeUpdate("insert into test1 (num1, num2) values (3, 2)");
      stmt.executeUpdate("insert into test1 (num1, num2) values (4, 3)");
      stmt.executeUpdate("insert into test1 (num1, num2) values (5, 3)");
      stmt.executeUpdate("insert into test1 (num1, num2) values (6, 3)");

      stmt.executeUpdate("insert into test1 (desc, now) values ('1st date', '2015-04-08 11:22:33.888')");
      stmt.executeUpdate("insert into test1 (desc, now) values ('2nd date', '2015-04-08 22:33:44.888')");
    } finally {
      if (stmt != null) {
        stmt.close();
      }
    }
  }

  @AfterClass
  public void tearDown() {
    if (conn != null) { try { conn.close(); } catch (Throwable t) { } }
  }

  @Test
  public void testNoFrom() throws SQLException {
    assertEquals(new Sql(conn, true).select("1").firstRow().get("1"), "1");
  }

  @Test
  public void testInVararray() throws SQLException {
    List list = new Sql(conn, true).select("*").from("test1")
      .where(Restrictions.in("num2", 1, 2))
      .list();

    assertEquals(list.size(), 3);
  }

  @Test
  public void testInList() throws SQLException {
    List list = new Sql(conn, true).select("*").from("test1")
      .where(Restrictions.in("num2", Arrays.asList(3, 1)))
      .list();

    assertEquals(list.size(), 5);
  }

  @Test
  public void testBefore() throws SQLException {
    Calendar c = Calendar.getInstance();
    c.set(2015, Calendar.APRIL, 8, 15, 12, 13);

    List<Row> list = new Sql<Row>(conn, true).select("*").from("test1")
      .where(Restrictions.before("now", c.getTime()))
      .list();

    assertEquals(list.size(), 1);
    assertEquals(list.get(0).get("desc"), "1st date");
  }

  @Test
  public void testAfter() throws SQLException {
    Calendar c = Calendar.getInstance();
    c.set(2015, Calendar.APRIL, 8, 15, 12, 13);

    List<Row> list = new Sql<Row>(conn, true).select("*").from("test1")
      .where(Restrictions.after("now", c.getTime()))
      .list();

    assertEquals(list.size(), 1);
    assertEquals(list.get(0).get("desc"), "2nd date");
  }
}
