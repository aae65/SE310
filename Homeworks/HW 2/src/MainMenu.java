import java.util.Scanner;

public class MainMenu {
    static Survey survey;
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner input = new Scanner(System.in);
        InputValidation validation = new InputValidation(input);
        while (true) {
            displayMenu();
            switch (validation.continuallyValidateInt(input.nextLine())) {
                case 1:
                    survey = new Survey(input, validation);
                    survey.id += 1;
                    survey.createSurvey();
                    break;
                case 2:
                    if (survey == null) {
                        System.out.println("You must have a survey loaded in order to display it.");
                    } else {
                        survey.displaySurvey();
                    }
                    break;
                case 3:
                    Survey temp = new Survey(input, validation);
                    if (temp.id == 0) {
                        System.out.println("No surveys have been created.");
                    } else {
                        survey = temp.loadSurvey();
                    }
                    break;
                case 4:
                    if (survey == null) {
                        System.out.println("You must have a survey loaded in order to save it.");
                    } else {
                        survey.saveSurvey();
                    }
                    break;
                case 5:
                    if (survey == null) {
                        System.out.println("You must have a survey loaded in order to take it.");
                    } else {
                        survey.takeSurvey();
                    }
                    break;
                case 6:
                    if (survey == null) {
                        System.out.println("You must have a survey loaded in order to modify it.");
                    } else {
                        survey.modifySurvey();
                    }
                    break;
                case 7:
                    System.out.println("Exiting...");
                    return;
            }
        }
    }

    protected static void displayMenu() {
        System.out.println("1) Create a new Survey");
        System.out.println("2) Display an existing Survey");
        System.out.println("3) Load an existing Survey");
        System.out.println("4) Save the current Survey");
        System.out.println("5) Take the current Survey");
        System.out.println("6) Modifying the current Survey");
        System.out.println("7) Quit");
    }
}
