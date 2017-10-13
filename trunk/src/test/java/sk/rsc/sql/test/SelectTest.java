package sk.rsc.sql.test;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import sk.rsc.sql.Mapper;
import sk.rsc.sql.Restrictions;
import sk.rsc.sql.Row;
import sk.rsc.sql.Sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

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
      stmt.executeUpdate("insert into test1 (desc, num1) values ('desc093456733', 10)");
      stmt.executeUpdate("insert into test1 (desc, num1) values ('desc093457382', 20)");
      stmt.executeUpdate("insert into test1 (desc, num1) values ('desc093457638', 30)");
      stmt.executeUpdate("insert into test1 (desc, num2) values ('desc082748302', 216.46)");
      stmt.executeUpdate("insert into test1 (desc, num2) values ('desc093444593', 226.46)");
      stmt.executeUpdate("insert into test1 (desc, num2) values ('desc093446373', 236.46)");
      stmt.executeUpdate("insert into test1 (desc, num2) values ('desc093456777', 246.46)");
    } finally {
      if (stmt != null) {
        stmt.close();
      }
    }
  }

  @AfterClass
  public void tearDown() {
    if (conn != null) {
      try { conn.close(); } catch (Exception ignored) { }
    }
  }

  @Test(enabled = false)
  public void testNoFrom() throws SQLException {
    assertEquals(new Sql(conn, true).select("1").firstRow().get("1"), "1");
  }

  @Test
  public void testStartWith() throws SQLException {
    List<String> list = new Sql<String>(conn, true)
      .select("desc").from("test1").where(Restrictions.startWith("desc", "desc09345"))
      .list(new Mapper<String>() {
        @Override
        protected String toObject() throws SQLException {
          return get("desc");
        }
      });

    assertNotNull(list);
    assertEquals(list.size(), 4);
  }

  @Test(enabled = false)
  public void testInVararray() throws SQLException {
    List list = new Sql(conn, true).select("*").from("test1")
      .where(Restrictions.in("num2", 1, 2))
      .list();

    assertEquals(list.size(), 3);
  }

  @Test(enabled = false)
  public void testInList() throws SQLException {
    List list = new Sql(conn, true).select("*").from("test1")
      .where(Restrictions.in("num2", Arrays.asList(3, 1)))
      .list();

    assertEquals(list.size(), 5);
  }

  @Test(enabled = false)
  public void testBefore() throws SQLException {
    Calendar c = Calendar.getInstance();
    c.set(2015, Calendar.APRIL, 8, 15, 12, 13);

    List<Row> list = new Sql<Row>(conn, true).select("*").from("test1")
      .where(Restrictions.before("now", c.getTime()))
      .list();

    assertEquals(list.size(), 1);
    assertEquals(list.get(0).get("desc"), "1st date");
  }

  @Test(enabled = false)
  public void testAfter() throws SQLException {
    Calendar c = Calendar.getInstance();
    c.set(2015, Calendar.APRIL, 8, 15, 12, 13);

    List<Row> list = new Sql<Row>(conn, true).select("*").from("test1")
      .where(Restrictions.after("now", c.getTime()))
      .list();

    assertEquals(list.size(), 1);
    assertEquals(list.get(0).get("desc"), "2nd date");
  }

  @Test(enabled = false)
  public void testMapperAll() throws SQLException {
    List<Test1Bean> list = new Sql<Test1Bean>(conn, true).select("*").from("test1").list(TEST1BEAN_MAPPER);
    assertNotNull(list);
  }

  @Test(enabled = false)
  public void testMapperSelected() throws SQLException {
    List<Test1Bean> list = new Sql<Test1Bean>(conn, true).select("desc, num2").from("test1").list(TEST1BEAN_MAPPER);
    assertNotNull(list);
  }

  @Test(enabled = true)
  public void testSelectedFields() throws SQLException {
    List<Test1Bean> list = new Sql<Test1Bean>(conn, true).select(new ArrayList<String>(0)).from("test1").list(TEST1BEAN_MAPPER);
    assertNotNull(list);
  }

  class Test1Bean {
    String desc;
    Integer num1;
    Date now;
    Double num2;
  }

  Mapper<Test1Bean> TEST1BEAN_MAPPER = new Mapper<Test1Bean>() {
    @Override
    protected Test1Bean toObject() throws SQLException {
      Test1Bean t = new Test1Bean();
      t.desc = get("desc");
      t.num1 = getInt("num1");
      t.now = getTimestamp("now");
      t.num2 = getDouble("num2");
      return t;
    }
  };
}
