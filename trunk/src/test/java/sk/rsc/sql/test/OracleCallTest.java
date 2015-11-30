package sk.rsc.sql.test;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import sk.rsc.sql.Sql;
import sk.rsc.sql.SqlCallable;

import java.sql.*;

/**
 * OracleCallTest.
 *
 * @author Radovan Sninsky
 * @since 2015-05-29 14:54
 */
public class OracleCallTest {

  private Connection conn;

  @BeforeClass(enabled = true, groups = {"oracle"})
  public void setUpOracle() throws SQLException {
    Sql.setSoutLogger();
    conn = DriverManager.getConnection("jdbc:oracle:thin:@172.18.0.23:1521:XE", "rscsql", "rscsql");
  }

  @AfterClass(enabled = true, groups = {"oracle"})
  public void tearDownOracle() {
    if (conn != null) { try { conn.close(); } catch (Exception e) { } }
  }

  @DataProvider(name = "dp1")
  public Object[][] td1() {
    return new Object[][] {
      new Object[] {
        100, "732ac53", "sn", "cn", 100000023, "nieco@domena.sk", "subjekt 1", "nejaky dlhy text", "1", "2", "jozko34", "agent007", "idpos",
        "Y", "N", "6677667272", "code2832", "user76372", true, false
      }
    };
  }

  @Test(dataProvider = "dp1", groups = {"oracle"})
  public void test1(long gtId, String ixnId, String sn, String cn, long catId, String from, String sub, String txt,
                    String ntfNum, String prfComm, String login, String agent, String idpos, String cntByPhone,
                    String cntByEmail, String rcIco, String idCode, String userId, boolean ntfByEmail, boolean ntfBySms)
    throws SQLException {
    SqlCallable call = new Sql(conn).call("CRT_CREATETICKET_TEXT_TEST")
      .in(gtId, ixnId, sn, cn, catId, from, sub, txt, null, ntfNum, prfComm, login, agent, idpos, cntByPhone, cntByEmail)
      .in(rcIco, idCode, Short.valueOf("1"), userId)
      .in(ntfByEmail ? "Y" : "N")
      .in(ntfBySms ? "Y" : "N")
      .out("id", Types.NUMERIC)
      .out("slaTime", Types.NUMERIC)
      .out("supervisorTime", Types.NUMERIC)
      .out("out4", Types.VARCHAR)
      .execute();

    System.out.println("Out");
    System.out.println("  id:             " + call.getOut("id"));
    System.out.println("  slaTime:        " + call.getOut("slaTime"));
    System.out.println("  supervisorTime: " + call.getOut("supervisorTime"));
    System.out.println("  out4:           " + call.getOut("out4"));
  }

  @Test(dataProvider = "dp1", groups = {"oracle"})
  public void testPlainJdbc(long gtId, String ixnId, String sn, String cn, long catId, String from, String sub, String txt,
                    String ntfNum, String prfComm, String login, String agent, String idpos, String cntByPhone,
                    String cntByEmail, String rcIco, String idCode, String userId, boolean ntfByEmail, boolean ntfBySms)
    throws SQLException
  {
    CallableStatement pst = null;
    int pos = 1;
    try {
      pst = conn.prepareCall("{call CRT_CREATETICKET_TEXT_TEST(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, ?,?,?,?)}");

      pst.setLong(pos++, gtId);
      pst.setString(pos++, ixnId);
      pst.setString(pos++, sn);
      pst.setString(pos++, cn);
      pst.setLong(pos++, catId);
      pst.setString(pos++, from);
      pst.setString(pos++, sub);
      pst.setString(pos++, txt);
      pst.setNull(pos++, Types.VARCHAR);
      pst.setString(pos++, ntfNum);
      pst.setString(pos++, prfComm);
      pst.setString(pos++, login);
      pst.setString(pos++, agent);
      pst.setString(pos++, idpos);
      pst.setString(pos++, cntByPhone);
      pst.setString(pos++, cntByEmail);
      pst.setString(pos++, rcIco);
      pst.setString(pos++, idCode);
      pst.setInt(pos++, 1);
      pst.setString(pos++, userId);
      pst.setString(pos++, ntfByEmail ? "Y" : "N");
      pst.setString(pos++, ntfBySms ? "Y" : "N");

      pst.registerOutParameter(pos++, java.sql.Types.INTEGER);
      pst.registerOutParameter(pos++, java.sql.Types.INTEGER);
      pst.registerOutParameter(pos++, java.sql.Types.INTEGER);
      pst.registerOutParameter(pos++, java.sql.Types.VARCHAR);

      pst.execute();

      System.out.println("Out");
      System.out.println("  id:      " + pst.getInt(pos-4));
      System.out.println("  slaTime: " + pst.getInt(pos-3));
      System.out.println("  supervisorTime: " + pst.getInt(pos-2));
      System.out.println("  out4: " + pst.getString(pos-1));
    } finally {
      pst.close();
    }
  }

  @Test(dataProvider = "dp1", groups = {"oracle"})
  public void testPlainJdbcOld(long gtId, String ixnId, String sn, String cn, long catId, String from, String sub, String txt,
                    String ntfNum, String prfComm, String login, String agent, String idpos, String cntByPhone,
                    String cntByEmail, String rcIco, String idCode, String userId, boolean ntfByEmail, boolean ntfBySms)
    throws SQLException
  {
    CallableStatement pst = null;
    int pos = 1;
    try {
      pst = conn.prepareCall("{call CRT_CREATETICKET_TEXT(?,?,?,?,?,?,?,?,?,?,?,?,?,?, ?,?,?,?)}");

      pst.setLong(pos++, gtId);
      pst.setString(pos++, ixnId);
      pst.setString(pos++, sn);
      pst.setString(pos++, cn);
      pst.setLong(pos++, catId);
      pst.setString(pos++, from);
      pst.setString(pos++, sub);
      pst.setString(pos++, txt);
      pst.setNull(pos++, Types.VARCHAR);
      pst.setString(pos++, ntfNum);
      pst.setString(pos++, prfComm);
      pst.setString(pos++, rcIco);
      pst.setString(pos++, idCode);
      pst.setInt(pos++, 1);

      pst.registerOutParameter(pos++, java.sql.Types.INTEGER);
      pst.registerOutParameter(pos++, java.sql.Types.INTEGER);
      pst.registerOutParameter(pos++, java.sql.Types.INTEGER);
      pst.registerOutParameter(pos, java.sql.Types.VARCHAR);

      pst.execute();

      System.out.println("Out");
      System.out.println("  id:             " + pst.getInt(pos - 3));
      System.out.println("  slaTime:        " + pst.getInt(pos - 2));
      System.out.println("  supervisorTime: " + pst.getInt(pos - 1));
      System.out.println("  out4:           " + pst.getString(pos));
    } finally {
      pst.close();
    }
  }
}
