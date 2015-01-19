package sk.rsc.sql.test;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import sk.rsc.sql.Sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
    conn = DriverManager.getConnection("jdbc:h2:mem:selectdb", "sa", "");
    Statement stmt = conn.createStatement();
    try {
      stmt.executeUpdate(CREATE_TABLE);
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
    Assert.assertEquals(new Sql(conn, true).select("1").firstRow().get("1"), "1");
  }
}
