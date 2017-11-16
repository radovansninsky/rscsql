package sk.rsc.sql.test;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import sk.rsc.sql.Sql;
import sk.rsc.sql.SqlCallable;

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

  @BeforeClass(groups = {"h2"})
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

  @AfterClass(groups = {"h2"})
  public void tearDown() {
    if (conn != null) { try { conn.close(); } catch (Throwable t) { } }
  }

  @Test(groups = {"h2"})
  public void test1() throws SQLException {
    new Sql(conn, true).call("proc_1").execute();
    new Sql(conn, true).call("proc_2", "nieco", 18).execute();

    SqlCallable call = new Sql(conn, true).call("proc_3", Types.VARCHAR).in("nieco2");
    call.execute();
    System.out.println("out = " + call.getReturn());
  }
}
