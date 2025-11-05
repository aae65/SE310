import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

public class InputValidation implements Serializable {
    transient Scanner input;

    public InputValidation(Scanner input) {
        this.input = input;
    }

    int continuallyValidateInt(String num) {
        while (!validateInt(num)) {
            System.out.println("Invalid number, try again.");
            num = input.nextLine();
        }
        return Integer.parseInt(num);
    }

    String continuallyValidateString(String prompt) {
        while (!validateString(prompt)){
            System.out.println("Invalid input. Try again.");
            prompt = input.nextLine();
        }
        return prompt;
    }

    Question continuallyValidateIndex(String index, List<Question> questions) {
        while (!validateInt(index)) {
            if (!validateString(index)) System.out.println("Value is not an integer. Try again.");
            else System.out.println("Question does not exist");
            index = input.nextLine();
        }
        return questions.get(Integer.parseInt(index) - 1);
    }

    String continuallyValidateTF(String str){
        while (!validateTrueFalse(str)){
            System.out.println("Answer must be T or F. Try again.");
            str = input.nextLine();
        }
        return str;
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

    boolean validateAnswersInt(String num, int numOfChoices) {
        return validateInt(num) && Integer.parseInt(num) <= numOfChoices && Integer.parseInt(num) >= 0;
    }

    boolean checkBounds(int num, int bound) {
        if (num >= bound) {
            System.out.println("Number of answers cannot exceed number of choices.");
            return false;
        }
        return true;
    }

    public boolean validateString(String str) {
        return str != null && !str.isEmpty();
    }

    public boolean validateTrueFalse(String str) {
        return !str.isEmpty() && !"T".equalsIgnoreCase(str) || !"F".equalsIgnoreCase(str);
    }

    public boolean validateInt(String num) {
        try {
            Integer.parseInt(num);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
