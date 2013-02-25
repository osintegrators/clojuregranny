package clojuregranny;

public class CrudApp {
	public static void main(String[] args) {
		try {
			clojure.main.main(args);
		} catch (Exception e) {
			System.err.println("Err: " + e);
		}
	}
}