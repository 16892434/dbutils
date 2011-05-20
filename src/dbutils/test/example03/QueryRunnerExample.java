package dbutils.test.example03;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.KeyedHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang.StringUtils;

import dbutils.test.example01.User;

public class QueryRunnerExample {

	private DataSource dataSource = null;
	private QueryRunner runner = null;
	
	public QueryRunnerExample() {
		initDataSource();
		runner = new QueryRunner(dataSource);
	}
	
	private DataSource initDataSource() {  
		if (dataSource == null) {  
			BasicDataSource basicDs = new BasicDataSource();  
			basicDs.setUrl("jdbc:mysql://127.0.0.1:3306/dbutils");  
			basicDs.setUsername("root");  
			basicDs.setPassword("mysqladmin");  
			this.dataSource = basicDs;  
		}  
		return dataSource;  
	}
	
	private void closeDataSource() throws SQLException{
		((BasicDataSource)dataSource).close();
	}
	
	private void batch() {
		String sql = "insert into user(name, pswd) values (?, ?)";
		try {
			System.out.println("\n" + StringUtils.center("Test QueryRunner batch", 80, "*"));
			int[] result = runner.batch(sql, new Object[][]{{"user1", "pwd1"},{"user2", "pwd2"},{"user3", "pwd3"},{"user4", "pwd4"}});
			System.out.printf("运行结果:%s\n", Arrays.toString(result));
		} catch (SQLException e) {
			DbUtils.printStackTrace(e);
		}
	}
	
	private void fillStatement() throws SQLException {
		String sql = "insert into user(name, pswd) values (?, ?)";
		Connection conn = null;
		try {
			System.out.println("\n" + StringUtils.center("Test QueryRunner fillStatement", 80, "*"));
			conn = dataSource.getConnection();
			PreparedStatement psmt = conn.prepareStatement(sql);
			runner.fillStatementWithBean(psmt, new User("testUser5", "pwd5"), "name", "pswd");
			System.out.println(psmt.executeUpdate());
		}catch(SQLException e) {
			DbUtils.printStackTrace(e);
		}finally{
			DbUtils.commitAndCloseQuietly(conn);
		}
	}
	
	private void query() throws SQLException {
		System.out.println("\n" + StringUtils.center("Test QueryRunner query", 80, "*"));
		String sql = "select * from user where name like ?";
		
		// 把ResultSet的第一行包装成Object[]
		System.out.println("1.Test QueryRunner query, ArrayHandler");
		Object[] r1 = runner.query(sql, new ArrayHandler(), "user%");
		System.out.println("  " + Arrays.deepToString(r1));
		
		// 把ResultSet包装成List<object[]>
		System.out.println("2.Test QueryRunner query, ArrayListHandler");
		List<Object[]> r2 = runner.query(sql, new ArrayListHandler(), "user%");
		System.out.println("  " + Arrays.deepToString(r2.toArray()));
		
		// 把ResultSet第一行包装成一个JavaBean
		System.out.println("3.Test QueryRunner query, BeanHandler");
		User r3 = runner.query(sql, new BeanHandler<User>(User.class), "user%");
		System.out.println("  " + r3.toString());
		
		// 把ResultSet第一行包装成一个List<JavaBean>
		System.out.println("4.Test QueryRunner query, BeanListHandler");
		List<User> r4 = runner.query(sql, new BeanListHandler<User>(User.class), "user%");
		System.out.println("  " + Arrays.deepToString(r4.toArray()));
		
		// 抽取Result指定的列，以List<Object>对象的形式返回，默认第一列
		System.out.println("5.Test QueryRunner query, ColumnListHandler");
		List<Object> r5 = runner.query(sql, new ColumnListHandler(2), "user%");
		System.out.println("  " + Arrays.deepToString(r5.toArray()));
		
		// 包装ResultSet，以Map<Object, Map<String, Object>>对象形式返回，第一个Object是指定列的值，第二个Map中String是列名且对大小写不敏感
		System.out.println("6.Test QueryRunner query, KeyedHandler");
		Map<Object, Map<String, Object>> r6 = runner.query(sql, new KeyedHandler(2), "user%");
		System.out.println("  " + r6.toString());
		
		// 把ResultSet第一行包装成Map<String, Object>，key对大小写不敏感
		System.out.println("7.Test QueryRunner query, MapHandler");
		Map<String, Object> r7 = runner.query(sql, new MapHandler(), "user%");
		System.out.println("  " + r7.toString());
		
		// 把ResultSet包装成List<Map<String, Object>>，Map的key对大小写不敏感
		System.out.println("8.Test QueryRunner query, MapListHandler");
		List<Map<String, Object>> r8 = runner.query(sql, new MapListHandler(), "user%");
		System.out.println("  " + r8.toString());
		
		// 抽取ResultSet第一行指定列，以Object对象形式返回
		System.out.println("9.Test QueryRunner query, ScalarHandler");
		Object r9 = runner.query(sql, new ScalarHandler("pswd"), "user%");
		System.out.println("  " + r9.toString());
	}
	
	private void update() {
		String sql = "delete from user where pswd like ?";
		
		try {
			System.out.println("\n" + StringUtils.center("Test QueryRunner udpate", 80, "*"));
			System.out.println(runner.update(sql, "pwd%"));
		} catch (SQLException e) {
			DbUtils.printStackTrace(e);
		}
	}
	
	public static void main(String [] args) throws SQLException {
		QueryRunnerExample example = new QueryRunnerExample();
		example.batch();
		example.fillStatement();
		example.query();
		// example.update();
		example.closeDataSource();
	}
}
