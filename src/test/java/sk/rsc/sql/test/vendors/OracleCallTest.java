package sk.rsc.sql.test.vendors;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import sk.rsc.sql.Sql;
import sk.rsc.sql.SqlCallable;

import java.sql.*;

/**
 * OracleCallTest.
 *
 * @author Radovan Sninsky
 * @since 2017-10-11 14:02
 */
public class OracleCallTest {

  private Connection orclConn;

  @BeforeClass(enabled = true, groups = {"oracle"})
  public void setUpOracle() throws SQLException {
    System.out.println("au");
    orclConn = DriverManager.getConnection("jdbc:oracle:thin:@172.18.0.23:1521:XE", "rscsql", "rscsql");
  }

  @AfterClass(enabled = true, groups = {"oracle"})
  public void tearDownOracle() {
    if (orclConn != null) { try { orclConn.close(); } catch (Throwable t) { } }
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
