package exceptions;



public class IdAlreadyExistsException extends Exception{

	Throwable tr;
	
	public IdAlreadyExistsException(Throwable tr) {
		super.initCause(tr);
	}

	private static final long serialVersionUID = 1L;

}
