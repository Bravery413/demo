package core.dbutils;

public class DBDuplicateKeyException extends DBException {
	public DBDuplicateKeyException() {
	}
	
	public DBDuplicateKeyException(String msg) {
		super(msg);
	}
	
	public DBDuplicateKeyException(Throwable cause) {
		super(cause);
	}
	
	public DBDuplicateKeyException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
