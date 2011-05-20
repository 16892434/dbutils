package dbutils.test.example01;

public class User {

	private int id;
	private String name;
	private String pswd;
	
	public User(){}
	public User(String name, String pswd) {
		this.name = name;
		this.pswd = pswd;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPswd() {
		return pswd;
	}
	public void setPswd(String pswd) {
		this.pswd = pswd;
	}
	@Override
	public String toString() {
		return "User{id=" + id + ", name='" + name + "\', pswd='" + pswd + "\'}";
	}
}
