package dbutils.test.example02;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import dbutils.test.example01.ConnTools;

public class PersonDAOImpl implements PersonDAO {
	
	private static PersonDAOImpl instance = new PersonDAOImpl();
	
	public static PersonDAOImpl getInstance() {
		return instance;
	}

	@Override
	public int delete(Long id) {
		int x = 0;
		Connection conn = ConnTools.makeConnection();
		QueryRunner qr = new QueryRunner();
		try {
			x = qr.update(conn, "delete from person where id = ?", id);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DbUtils.closeQuietly(conn);
		}
		return x;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Person> findPerson(String sql) {
		List<Person> list = null;
		Connection conn = ConnTools.makeConnection();
		QueryRunner qr = new QueryRunner();
		try {
			list = (List)qr.query(conn, "select * from person", new BeanListHandler(Person.class));
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DbUtils.closeQuietly(conn);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Person load(Long id) {
		Person person = null;
		Connection conn = ConnTools.makeConnection();
		QueryRunner qr = new QueryRunner();
		try {
			person = (Person)qr.query(conn, "select * from person where id = ?", new BeanHandler(Person.class), 3L);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DbUtils.closeQuietly(conn);
		}
		return person;
	}

	@Override
	public Long save(String sql) {
		Long id = null;
		String ins_sql = "insert into person(name, age, address) values('aaa', 21, 'address001')";
		Connection conn = ConnTools.makeConnection();
		QueryRunner qr = new QueryRunner();
		try {
			qr.update(conn, ins_sql);
			id = (Long)qr.query(conn, "select last_insert_id()", new ScalarHandler(1));
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DbUtils.closeQuietly(conn);
		}
		return id;
	}

	@Override
	public int update(Person person) {
		int x = 0;
		Connection conn = ConnTools.makeConnection();
		QueryRunner qr = new QueryRunner();
		try {
			x = qr.update(conn, "update person set name = ?, age = ?, address = ? where id = ?", "xxx", 23, "ttt", 5);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DbUtils.closeQuietly(conn);
		}
		return x;
	}
	
	@Override
	public Person load4Map(Long id) {
		Person person = null;
		Connection conn = ConnTools.makeConnection();
		QueryRunner qr = new QueryRunner();
		try {
			qr.update(conn, "update person set age = null, address = null where id = 1");
			Map<String, Object> map = qr.query(conn, "select * from person where id = ?", new MapHandler(), 1L);
			person = new Person();
			person.setId((Long)map.get("id"));
			person.setName((String)map.get("name"));
			person.setAge((Integer)map.get("age"));
			person.setAddress((String)map.get("address"));
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DbUtils.closeQuietly(conn);
		}
		return person;
	}

	public static void main(String [] args) throws Exception {
		Integer a = (Integer)null;
		Boolean b = (Boolean)null;
		
		Long id = getInstance().save("");
		System.out.println(id);
	}
	
}
