package dbutils.test.example02;

import java.util.List;

public interface PersonDAO {

	public Long save(String sql);
	public int delete(Long id);
	public int update(Person person);
	public Person load(Long id);
	public List<Person> findPerson(String sql);
	public Person load4Map(Long id);
	
}
