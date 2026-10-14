package sk.rsc.sql.test;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import sk.rsc.sql.Restrictions;
import sk.rsc.sql.Sql;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

/**
 * Tests for Sql update class.
 *
 * @author Radovan Sninsky
 * @since 1.0, 7/9/12 11:18 PM
 */
public class UpdateTest {

	private static final String CREATE_TABLE =
		"create table test1 (desc varchar(512), num1 integer, now timestamp, num2 numeric(12,2), tags varchar(64) array)";

	private Connection conn;

	@BeforeClass
	public void setUp() throws SQLException {
		conn = DriverManager.getConnection("jdbc:h2:mem:updatedb", "sa", "");
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

	@BeforeMethod
	public void initTable() throws SQLException {
		Statement s = conn.createStatement();
		s.executeUpdate("insert into test1 (desc, num1, num2) values ('rado', 56, 92.34)");
		s.executeUpdate("insert into test1 (desc, num1, num2) values ('peterA', 14, 1292.34)");
		s.executeUpdate("insert into test1 (desc, num1, num2) values ('RoboX', 8, 192.9)");
		s.executeUpdate("insert into test1 (desc, num1, num2) values ('MalcolmY', 172, 3412.31)");
		s.close();
	}

	@Test
	public void test1() throws SQLException {
		new Sql(conn, true).update("test1")
			.set("desc", "jozoIdes")
			.where(Restrictions.eq("num1", 14))
			.execute();

		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery("select * from test1 where num1=14");
		rs.next();
		Assert.assertEquals(rs.getString("desc"), "jozoIdes");
		rs.close();
		s.close();
	}

	@Test
	public void test2() throws SQLException {
		new Sql(conn, true).update("test1")
			.set("now", null)
			.where(Restrictions.eq("num1", 8))
			.execute();

		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery("select * from test1 where num1=8");
		rs.next();
		Assert.assertNull(rs.getDate("now"));
		rs.close();
		s.close();
	}

	@Test
	public void test3() throws SQLException {
		new Sql(conn, true).update("test1")
			.set("tags", "varchar", new String[]{"x", "y", "z"})
			.where(Restrictions.eq("num1", 56))
			.execute();

		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery("select * from test1 where num1=56");
		rs.next();
		Array array = rs.getArray("tags");
		List<Object> tags = Arrays.asList((Object[]) array.getArray());
		Assert.assertEquals(tags, Arrays.asList("x", "y", "z"));
		rs.close();
		s.close();
	}

	@Test
	public void test4() throws SQLException {
		new Sql(conn, true).update("test1")
			.set("tags", "varchar", Arrays.asList("a", "b"))
			.where(Restrictions.eq("num1", 172))
			.execute();

		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery("select * from test1 where num1=172");
		rs.next();
		Array array = rs.getArray("tags");
		List<Object> tags = Arrays.asList((Object[]) array.getArray());
		Assert.assertEquals(tags, Arrays.asList("a", "b"));
		rs.close();
		s.close();
	}

	@Test
	public void test5() throws SQLException {
		new Sql(conn, true).update("test1")
			.set("tags", "varchar", (Object[]) null)
			.where(Restrictions.eq("num1", 14))
			.execute();

		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery("select * from test1 where num1=14");
		rs.next();
		Assert.assertNull(rs.getArray("tags"));
		rs.close();
		s.close();
	}
}
