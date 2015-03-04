package sk.rsc.sql.test;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import sk.rsc.sql.Sql;
import sk.rsc.sql.SqlCallable;
import sk.rsc.sql.fields.InOutField;
import sk.rsc.sql.fields.OutField;

import java.sql.*;

/**
 * CallTest.
 *
 * @author Radovan Sninsky
 * @since 2015-03-02 22:54
 */
public class CallTest {

  private static final String CREATE_PROC1 =
    "CREATE ALIAS PROC_1 AS $$ void proc1() { System.out.println(\"procedure no params\"); } $$;";

  private static final String CREATE_PROC2 =
    "CREATE ALIAS PROC_2 AS $$ void proc2(String p1, int p2) { System.out.println(\"procedure w p1=\"+p1+\", p2=\"+p2); } $$;";

  private static final String CREATE_PROC3 =
    "CREATE ALIAS PROC_3 AS $$ String proc3(String p1) {\n" +
    "  System.out.println(\"procedure w p1=\"+p1);\n" +
    "  return \"hellooo\";" +
    "} $$;";

  private Connection conn;
  private Connection orclConn;

  @BeforeClass
  public void setUp() throws SQLException {
    Sql.setSoutLogger();

    conn = DriverManager.getConnection("jdbc:h2:mem:calldb", "sa", "");
    Statement stmt = conn.createStatement();
    try {
      stmt.executeUpdate(CREATE_PROC1);
      stmt.executeUpdate(CREATE_PROC2);
      stmt.executeUpdate(CREATE_PROC3);
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

  @BeforeClass(enabled = true, groups = {"oracle"})
  public void setUpOracle() throws SQLException {
    System.out.println("au");
    orclConn = DriverManager.getConnection("jdbc:oracle:thin:@172.18.0.23:1521:XE", "rscsql", "rscsql");
  }

  @AfterClass(enabled = true, groups = {"oracle"})
  public void tearDownOracle() {
    if (orclConn != null) { try { orclConn.close(); } catch (Throwable t) { } }
  }

  @Test
  public void test1() throws SQLException {
    new Sql(conn, true).call("proc_1").execute();
    new Sql(conn, true).call("proc_2", "nieco", 18).execute();

    SqlCallable call = new Sql(conn, true).call("proc_3", Types.VARCHAR, "nieco2");
    call.execute();
    System.out.println("out = " + call.getReturn());
  }

  @Test(enabled = true, groups = {"oracle"})
  public void testOracle1() throws SQLException {
    SqlCallable call = new Sql(orclConn, true).call("proc_4")
      .in("nieco1")
      .inOut("p2", "nieco2", Types.VARCHAR)
      .out("p3", Types.VARCHAR)
      .execute();
    System.out.println("proc 4: p2 = " + call.getOut("p2") + ", p3 = " + call.getOut("p3"));

    SqlCallable call2 = new Sql(orclConn, true).call("func_5", Types.VARCHAR)
      .in("nieco1")
      .inOut("p2", "nieco2", Types.VARCHAR)
      .out("p3", Types.VARCHAR)
      .execute();
    System.out.println("func 5: ret = " + call2.getReturn());
    System.out.println("func 5: p2 = " + call2.getOut("p2") + ", p3 = " + call2.getOut("p3"));
  }
}
