package mr.persister.test.example01;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import com.jenkov.mrpersister.PersistenceManager;
import com.jenkov.mrpersister.itf.PersistenceException;
import com.jenkov.mrpersister.itf.command.IDaoCommand;
import com.jenkov.mrpersister.itf.command.IDaos;
import com.jenkov.mrpersister.util.JdbcUtil;

import dbutils.test.example01.ConnTools;
import dbutils.test.example01.User;

public class Run {

	public static final PersistenceManager PM = new PersistenceManager();
	public static final PersistenceManager PM_DS = new PersistenceManager(initDataSource());
	
	private static DataSource dataSource = null;
	
	private static DataSource initDataSource() {  
		if (dataSource == null) {  
			BasicDataSource basicDs = new BasicDataSource();  
			basicDs.setUrl("jdbc:mysql://127.0.0.1:3306/dbutils");  
			basicDs.setUsername("root");  
			basicDs.setPassword("mysqladmin");  
			dataSource = basicDs;  
		}  
		return dataSource;  
	}
	
	public static void main(String [] args) throws Exception {
		testOne();
		testTwo();
	}
	
	public static void testOne() throws Exception {
		IDaos dao = PM.getDaosFactory().createDaos(ConnTools.makeConnection());
		String sql = "select * from user where id = 1";
		User user = (User)dao.getGenericDao().read(User.class, sql);
		JdbcUtil.close(dao.getConnection());
		System.out.println(user);
	}
	
	public static void testTwo() throws Exception {
		User user = (User)PM_DS.getDaoCommandExecutorUnchecked().execute(
				new IDaoCommand(){
					public Object execute(IDaos dao) throws PersistenceException, SQLException {
						return dao.getGenericDao().readByPrimaryKey(User.class, 2);
					}
				});
		System.out.println(user);
	}
}
