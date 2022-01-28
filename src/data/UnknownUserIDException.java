package data;
@SuppressWarnings("serial")
public class UnknownUserIDException extends Exception {
	public UnknownUserIDException() {
		System.out.println("This user's ID doesn't exist! ");
	}
}
