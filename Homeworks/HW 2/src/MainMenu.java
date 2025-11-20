import java.util.Scanner;

public class MainMenu {
    static Survey survey;
    static Test test;
    static Scanner input = new Scanner(System.in);
    static InputHandler inputHandler = new InputHandler(input);
    static OutputHandler outputHandler = new OutputHandler();
    
	static String[] menu = { "Survey", "Test", "Exit" };
	static String[] surveyMenu = { "Create a new Survey", "Display an existing Survey", "Load an existing Survey",
			"Save the current Survey", "Take the current Survey", "Modify the current Survey", "Tabulate a survey",
			"Return to previous menu" };
	static String[] testChoices = { "Create a new Test", "Display an existing Test without correct answers",
			"Display an existing Test with correct answers", "Load an existing Test", "Save the current Test",
			"Take the current Test", "Modify the current Test", "Tabulate a Test", "Grade a Test",
			"Return to the previous menu" };

    public static void main(String[] args) throws ClassNotFoundException {
        outputHandler.displayMenu(menu);
        while (true) {
            switch (inputHandler.getValidInt()) {
                case 1:
                    outputHandler.displayMenu(surveyMenu);
                    SurveyChoices();
                    break;
                case 2:
                    outputHandler.displayMenu(testChoices);
                    TestChoices();
                    break;
                case 3:
                    outputHandler.println("Exiting...");
                    return;
            }
            outputHandler.displayMenu(menu);
        }
    }

    protected static void SurveyChoices() throws ClassNotFoundException {
        while (true) {
            switch (inputHandler.getValidInt()) {
                case 1:
                    survey = new Survey(inputHandler);
                    survey.id += 1;
                    survey.create();
                    break;
                case 2:
                    if (survey == null) {
                        outputHandler.println("You must have a survey loaded in order to display it.");
                    } else {
                        survey.displayAllQuestions();
                    }
                    break;
                case 3:
                    Survey temp = new Survey(inputHandler);
                    if (temp.id == 0) {
                        outputHandler.println("No surveys have been created.");
                    } else {
                        survey = temp.load();
                    }
                    break;
                case 4:
                    if (survey == null) {
                        outputHandler.println("You must have a survey loaded in order to save it.");
                    } else {
                        survey.save();
                    }
                    break;
                case 5:
                    if (survey == null) {
                        outputHandler.println("You must have a survey loaded in order to take it.");
                    } else {
                        survey.take();
                    }
                    break;
                case 6:
                    if (survey == null) {
                        outputHandler.println("You must have a survey loaded in order to modify it.");
                    } else {
                        survey.modify();
                    }
                    break;
                case 7:
                    if (survey == null) {
                        outputHandler.println("You must have a survey loaded in order to tabulate it.");
                    } else {
                        survey.tabulate();
                    }
                    break;
                case 8:
                    return;
            }
            outputHandler.displayMenu(surveyMenu);
        }
    }

    protected static void TestChoices() throws ClassNotFoundException {
        while (true) {
            switch (inputHandler.getValidInt()) {
                case 1:
                    test = new Test(inputHandler);
                    test.id += 1;
                    test.create();
                    break;
                case 2:
                    if (test == null) {
                        outputHandler.println("You must have a test loaded in order to display it.");
                    } else {
                        test.displayAllQuestions();
                    }
                    break;
                case 3:
                    if (test == null) {
                        outputHandler.println("You must have a test loaded in order to display it.");
                    } else {
                        test.displayWithAnswers();
                    }
                    break;
                case 4:
                    Test temp = new Test(inputHandler);
                    if (temp.id == 0) {
                        outputHandler.println("No tests have been created.");
                    } else {
                        test = temp.load();
                    }
                    break;
                case 5:
                    if (test == null) {
                        outputHandler.println("You must have a test loaded in order to save it.");
                    } else {
                        test.save();
                    }
                    break;
                case 6:
                    if (test == null) {
                        outputHandler.println("You must have a test loaded in order to take it.");
                    } else {
                        test.take();
                    }
                    break;
                case 7:
                    if (test == null) {
                        outputHandler.println("You must have a test loaded in order to modify it.");
                    } else {
                        test.modify();
                    }
                    break;
                case 8:
                    if (test == null) {
                        outputHandler.println("You must have a test loaded in order to tabulate it.");
                    } else {
                        test.tabulate();
                    }
                    break;
                case 9:
                    if (test == null) {
                        Test temp2 = new Test(inputHandler);
                        temp2.grade();
                    } else {
                        test.grade();
                    }
                    break;
                case 10:
                    return;
            }
            outputHandler.displayMenu(testChoices);
        }
    }
}
