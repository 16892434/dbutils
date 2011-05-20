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
			System.out.printf("���н��:%s\n", Arrays.toString(result));
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
		
		// ��ResultSet�ĵ�һ�а�װ��Object[]
		System.out.println("1.Test QueryRunner query, ArrayHandler");
		Object[] r1 = runner.query(sql, new ArrayHandler(), "user%");
		System.out.println("  " + Arrays.deepToString(r1));
		
		// ��ResultSet��װ��List<object[]>
		System.out.println("2.Test QueryRunner query, ArrayListHandler");
		List<Object[]> r2 = runner.query(sql, new ArrayListHandler(), "user%");
		System.out.println("  " + Arrays.deepToString(r2.toArray()));
		
		// ��ResultSet��һ�а�װ��һ��JavaBean
		System.out.println("3.Test QueryRunner query, BeanHandler");
		User r3 = runner.query(sql, new BeanHandler<User>(User.class), "user%");
		System.out.println("  " + r3.toString());
		
		// ��ResultSet��һ�а�װ��һ��List<JavaBean>
		System.out.println("4.Test QueryRunner query, BeanListHandler");
		List<User> r4 = runner.query(sql, new BeanListHandler<User>(User.class), "user%");
		System.out.println("  " + Arrays.deepToString(r4.toArray()));
		
		// ��ȡResultָ�����У���List<Object>�������ʽ���أ�Ĭ�ϵ�һ��
		System.out.println("5.Test QueryRunner query, ColumnListHandler");
		List<Object> r5 = runner.query(sql, new ColumnListHandler(2), "user%");
		System.out.println("  " + Arrays.deepToString(r5.toArray()));
		
		// ��װResultSet����Map<Object, Map<String, Object>>������ʽ���أ���һ��Object��ָ���е�ֵ���ڶ���Map��String�������ҶԴ�Сд������
		System.out.println("6.Test QueryRunner query, KeyedHandler");
		Map<Object, Map<String, Object>> r6 = runner.query(sql, new KeyedHandler(2), "user%");
		System.out.println("  " + r6.toString());
		
		// ��ResultSet��һ�а�װ��Map<String, Object>��key�Դ�Сд������
		System.out.println("7.Test QueryRunner query, MapHandler");
		Map<String, Object> r7 = runner.query(sql, new MapHandler(), "user%");
		System.out.println("  " + r7.toString());
		
		// ��ResultSet��װ��List<Map<String, Object>>��Map��key�Դ�Сд������
		System.out.println("8.Test QueryRunner query, MapListHandler");
		List<Map<String, Object>> r8 = runner.query(sql, new MapListHandler(), "user%");
		System.out.println("  " + r8.toString());
		
		// ��ȡResultSet��һ��ָ���У���Object������ʽ����
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
