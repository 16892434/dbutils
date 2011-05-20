package dbutils.test.example01;

import java.sql.Connection;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

public class Test {

	public static void test_insert() throws Exception {
		System.out.println("test_insert");
		Connection conn = ConnTools.makeConnection();
		QueryRunner qRunner = new QueryRunner();
		int n = qRunner.update(conn, "insert into user(name, pswd) values('bbb', 'bbb')");
		System.out.println("成功插入" + n + "条数据！");
		DbUtils.closeQuietly(conn);
	}

	@SuppressWarnings("unchecked")
	public static void test_find() throws Exception {
		System.out.println("test_find");
		Connection conn = ConnTools.makeConnection();
		QueryRunner qr = new QueryRunner();
		List<User> list = (List<User>)qr.query(conn, "select id, name, pswd from user", new BeanListHandler(User.class));
		for(User user : list) {
			System.out.println(user);
		}
		DbUtils.closeQuietly(conn);
	}
	
	public static void main(String [] args) throws Exception {
		test_insert();
		test_find();
	}
	
}
