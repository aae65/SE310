import java.io.Serializable;

public class OutputHandler implements Serializable {
    private final static long serialVersionUID = 1L;
    public OutputHandler() {}
    public void print(String message) {
        System.out.print(message);
    }

    public void println(String message) {
        System.out.println(message);
    }

    public void displayMenu(String[] options) {
        for (int i = 0; i < options.length; i++) {
            println((i + 1) + ") " + options[i]);
        }
    }
}
