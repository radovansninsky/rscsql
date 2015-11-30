package sk.rsc.sql.test;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import sk.rsc.sql.Sql;
import sk.rsc.sql.SqlInsert;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Tests for Sql insert class.
 *
 * @author Radovan Sninsky
 * @since 1.0, 7/9/12 10:39 AM
 */
public class InsertTest {

	private static final String CREATE_TABLE =
		"create table test1 (id identity, desc varchar(512), num1 integer, now timestamp, num2 numeric(12,2))";

	private Connection conn;

	@BeforeClass
	public void setUp() throws SQLException {
		conn = DriverManager.getConnection("jdbc:h2:mem:insertdb", "sa", "");
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
	public void test1() throws SQLException {
		new Sql(conn, true).insert("test1")
			.set("desc", "this is a description text")
			.set("num1", 326)
			.set("now", new java.util.Date())
			.set("num2", 4.82)
			.execute();

		check();
	}

	private void check() throws SQLException {
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery("select * from test1");
		rs.next();
		Assert.assertEquals(rs.getString("desc"), "this is a description text");
		Assert.assertEquals(rs.getInt("num1"), 326);
		rs.close();
		s.close();
	}

	@Test
	public void test2() throws SQLException {
		SqlInsert i = new Sql(conn, true).insert("test1");
		i.setGeneratedKey("id", Types.INTEGER)
			.set("desc", "desc2")
			.set("now", new java.util.Date())
			.execute();
		int id = i.getGeneratedKey().asInt();

		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery("select * from test1 where id = "+id);
		rs.next();
		Assert.assertEquals(rs.getInt("id"), id);
		Assert.assertEquals(rs.getString("desc"), "desc2");
		rs.close();
		s.close();
	}
}
