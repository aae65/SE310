import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

public class InputHandler implements Serializable {
    private static final long serialVersionUID = 1L;
    transient Scanner input;

    public InputHandler(Scanner input) {
        this.input = input;
    }

    public String readLine() {
        return input.nextLine();
    }

    int getValidInt() {
        String str = readLine();
        while (!validateInt(str)) {
            System.out.println("Invalid number, try again");
            str = readLine();
        }
        return Integer.parseInt(str);
    }

    public String getValidString() {
        String str = readLine();
        while (!validateString(str)) {
            System.out.println("Invalid input. Try again.");
            str = readLine();
        }
        return str;
    }

    public int getValidIndex(List<?> list) {
        String index = readLine();
        while (!validateInt(index) || Integer.parseInt(index) < 1 || Integer.parseInt(index) > list.size()) {
            if (!validateInt(index)) {
                System.out.println("Value is not an integer. Try again.");
            } else {
                System.out.println("Index out of range. Try again.");
            }
            index = readLine();
        }
        return Integer.parseInt(index) - 1;
    }

    String validateFormat(String format) {
        if (format.contains("MM") && (format.contains("dd") || format.contains("DD"))
                && (format.contains("yyyy") || format.contains("YYYY"))) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                return sdf.toPattern();
            } catch (Exception e) {
                System.out.println("Invalid date format.");
                return null;
            }
        }
        return null;
    }

    public boolean validateString(String str) {
        return str != null && !str.isEmpty();
    }

    public boolean validateInt(String num) {
        try {
            Integer.parseInt(num);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
