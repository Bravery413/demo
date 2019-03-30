package core.dbutils;

public class DBException extends Exception {
	public DBException() {
	}
	
	public DBException(String msg) {
		super(msg);
	}
	
	public DBException(Throwable cause) {
		super(cause);
	}
	
	public DBException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
