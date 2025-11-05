import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Survey implements Serializable {
    transient Scanner input;
    private static final long serialVersionUID = 1L;
    int id;
    String filePath = "./";
    TrueFalse trueFalse;
    MultipleChoice multipleChoice;
    ShortAnswer shortAnswer;
    Essay essay;
    Date date;
    Matching matching;
    List<Question> questions = new ArrayList<>();
    String prompt;
    InputValidation inputValidation;

    public Survey(Scanner input, InputValidation inputValidation) {
        this.input = input;
        this.inputValidation = inputValidation;
        checkDir();
    }

    public void checkDir() {
        File dir = new File(filePath);
        File[] files = dir.listFiles((d, name) -> name.startsWith("survey") && name.endsWith(".ser") && !name.contains("userAnswers"));
		if (files == null || files.length == 0) {
			this.id = 0;
		} else {
            for (File file : files) {
                String name = file.getName();
                String numStr = name.substring(6, name.indexOf(".ser"));
                this.id = Integer.parseInt(numStr);
            }
		}
    }

    private void displayMenu() {
        System.out.println("1) Add a new T/F question");
        System.out.println("2) Add a new multiple-choice question");
        System.out.println("3) Add a new short answer question");
        System.out.println("4) Add a new essay question");
        System.out.println("5) Add a new date question");
        System.out.println("6) Add a new matching question");
        System.out.println("7) Return to previous menu");
    }

    private void getChoices() {
        int choice = inputValidation.continuallyValidateInt(input.nextLine());
        if (choice == 7 && questions.isEmpty()) {
            System.out.println("You must add at least one question. Survey will not be loaded.");
            displayMenu();
            choice = inputValidation.continuallyValidateInt(input.nextLine());
        }
        while (choice != 7) {
            switch (choice) {
                case 1:
                    System.out.println("Enter the prompt for your True/False question:");
                    prompt = input.nextLine();
                    if (inputValidation.validateString(prompt)) {
                        trueFalse = new TrueFalse(prompt);
                        questions.add(trueFalse);
                    }
                    break;
                case 2:
                    System.out.println("Enter the prompt for your multiple-choice question:");
                    prompt = input.nextLine();
                    if (inputValidation.validateString(prompt)) {
                        multipleChoice = new MultipleChoice(prompt);
                        System.out.println("Enter the number of choices for your multiple-choice question: ");
                        String choiceStr = input.nextLine();
                        multipleChoice.setChoiceNumber(inputValidation.continuallyValidateInt(choiceStr));
                        for (int i = 0; i < multipleChoice.choiceNum; i++) {
                            System.out.println("Enter choice #" + (i + 1) + ":");
                            choiceStr =  input.nextLine();
                            multipleChoice.setChoice(inputValidation.continuallyValidateString(choiceStr));
                        }
                        System.out.println("How many answers are allowed for this question?");
                        String num = input.nextLine();
                        if (inputValidation.validateAnswersInt(num, multipleChoice.choiceNum)) {
                            if (inputValidation.checkBounds(Integer.parseInt(num), multipleChoice.choiceNum)) {
                                multipleChoice.numOfAnswers = Integer.parseInt(num);
                            }
                        } else {
                            System.out.println("Number of allowed answers will be default.");
                        }
                        questions.add(multipleChoice);
                    }
                    break;
                case 3:
                    System.out.println("Enter the prompt for your short answer question:");
                    prompt = input.nextLine();
                    if (inputValidation.validateString(prompt)) {
                        shortAnswer = new ShortAnswer(prompt);
                        setNumOfAllowedAnswers(shortAnswer);
                        questions.add(shortAnswer);
                    }
                    break;
                case 4:
                    System.out.println("Enter the prompt for your essay question:");
                    prompt = input.nextLine();
                    if (inputValidation.validateString(prompt)) {
                        essay = new Essay(prompt);
                        setNumOfAllowedAnswers(essay);
                        questions.add(essay);
                    }
                    break;
                case 5:
                    System.out.println("Enter the prompt for your date question:");
                    prompt = input.nextLine();
                    if (inputValidation.validateString(prompt)) {
                        date = new Date(prompt);

                        System.out.println("What format should a date be entered in? Leave blank for default (MM-dd-yyyy)");
                        String format = input.nextLine();
                        if (inputValidation.validateFormat(format) != null) {
                            date.setFormat(format);
                        } else {
                            System.out.println("Default format will be used.");
                        }

                        setNumOfAllowedAnswers(date);
                        questions.add(date);
                    }
                    break;
                case 6:
                    System.out.println("Enter the prompt for your matching question:");
                    prompt = input.nextLine();
                    if (inputValidation.validateString(prompt)) {
                        matching = new Matching(prompt);

                        System.out.println("Enter the number of choices for your matching question:");
                        String choiceNum = input.nextLine();
                        matching.setChoiceNumber(inputValidation.continuallyValidateInt(choiceNum));

                        System.out.println("Enter the statements for your matching question:");
                        for (int i = 0; i < matching.choiceNum; i++) {
                            System.out.println("Enter statement #" + (i + 1) + ": ");
                            choiceNum = input.nextLine();
                            matching.setChoice(inputValidation.continuallyValidateString(choiceNum));
                        }

                        System.out.println("Enter the possible answers for your statements:");
                        for (int i = 0; i < matching.choiceNum; i++) {
                            System.out.println("Enter possible answer #" + (i + 1) + ": ");
                            choiceNum = input.nextLine();
                            matching.setPossibleAnswers(inputValidation.continuallyValidateString(choiceNum));
                        }
                        questions.add(matching);
                    }
                    break;
            }
            displayMenu();
            choice = inputValidation.continuallyValidateInt(input.nextLine());
        }
    }

    private void setNumOfAllowedAnswers(Question question) {
        System.out.println("How many answers are allowed for this question?");
        String num = input.nextLine();
        if (inputValidation.validateInt(num)) {
            question.setNumOfAnswers(Integer.parseInt(num));
        } else {
            System.out.println("Number of allowed answers will be default.");
        }
    }

    public void showAllSurveys() {
        File dir = new File(filePath);
        File[] files = dir.listFiles((d, name) -> name.startsWith("survey") && name.endsWith(".ser") && !name.contains("userAnswers"));

        if (files == null || files.length == 0) {
            System.out.println("No surveys to load");
            return;
        }

        System.out.println("Please select a survey to to load (enter a number):");
        for (int i = 0; i < files.length; i++) {
            System.out.println((i + 1) + ") " + files[i].getName());
        }
    }

    public void createSurvey() {
        displayMenu();
        getChoices();
    }

    public Survey loadSurvey() throws ClassNotFoundException {
        showAllSurveys();
        int findId = inputValidation.continuallyValidateInt(input.nextLine());
        File file = new File(filePath + "survey" + findId + ".ser");
        if (!file.exists()) {
            return null;
        }
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
            Survey survey = (Survey) ois.readObject();
            survey.input = this.input;
            survey.inputValidation = this.inputValidation;
            return survey;
		} catch (IOException ex) {
            return null;
		}
    }

    public void displaySurvey() {
        for (int i = 0; i < questions.size(); i++) {
            System.out.println((i + 1) + ") " + questions.get(i).display() + "\n");
        }
    }

    public void modifySurvey() {
        displaySurvey();
        System.out.println("What question do you wish to modify?");
        String str = input.nextLine();
        Question question = inputValidation.continuallyValidateIndex(str, questions);
        System.out.println(question.display());
        System.out.println("\nDo you wish to modify the prompt?");
        String answer = input.nextLine();
        if (answer.equalsIgnoreCase("yes")) {
            System.out.println(question.getPrompt() + "\nEnter a new prompt:");
            str = input.nextLine();
            question.prompt = inputValidation.continuallyValidateString(str);
        } else {
            if (question.getClass().equals(MultipleChoice.class)) {
                System.out.println("Do you wish to modify choices?");
                str = input.nextLine();
                if ("yes".equalsIgnoreCase(str)) {
                    System.out.println(((MultipleChoice) question).displayChoices());
                    System.out.println("Which choice do you wish to modify?");
                    str = input.nextLine();
                    char choice = inputValidation.continuallyValidateString(str).charAt(0);
                    str = input.nextLine();
                    ((MultipleChoice) question).choices.set((int) choice - 'A', inputValidation.continuallyValidateString(str));
                }
            } else if (question.getClass().equals(Matching.class)) {
                System.out.println("Do you wish to modify statements?");
                str = input.nextLine();
                if ("yes".equalsIgnoreCase(str)) {
                    System.out.println(((Matching) question).displayChoices());
                    System.out.println("Which statement do you wish to modify?");
                    str = input.nextLine();
                    char choice = inputValidation.continuallyValidateString(str).charAt(0);
                    str = input.nextLine();
                    ((Matching) question).choices.set((int) choice - 'A', str);
                }
                System.out.println("Which possible answer do you wish to modify? Please enter a number:");
                System.out.println(((Matching) question).displayPossibleAnswers());
                str = input.nextLine();
                System.out.println("Enter the new possible answer:");
                String newAnswer = input.nextLine();
                ((Matching) question).possibleAnswers.set(inputValidation.continuallyValidateInt(str) - 1, newAnswer);
            } else if (question.getClass().equals(Date.class)) {
                System.out.println("Do you wish to modify the date format?");
                str = input.nextLine();
                if ("yes".equalsIgnoreCase(str)) {
                    System.out.println(((Date) question).format);
                    System.out.println("Enter the new date format (leave blank for default format MM-dd-yyyy): ");
                    str = input.nextLine();
                    ((Date) question).format = inputValidation.validateFormat(str);
                }
            }
        }
    }

    public void saveSurvey() {
        String filename = filePath + "survey" + id + ".ser";
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
            fos.close();
            displaySurvey();
        } catch (IOException ex) {
            System.out.println("Survey could not be created");
        }
    }

    public void takeSurvey() {
        System.out.println("Enter your name:");
        String name = inputValidation.continuallyValidateTF(input.nextLine());
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            String response;
            System.out.println((i + 1) + ") " + question.getPrompt());

            if (question.numOfAnswers > 1) {
                if (question.getClass().equals(MultipleChoice.class)) {
                    System.out.println(((MultipleChoice) question).displayChoices());
                    for (int j = 0; j < question.numOfAnswers; j++) {
                        response = input.nextLine();
                        question.setUserAnswer(inputValidation.continuallyValidateString(response));
                    }
                } else if (question.getClass().equals(Matching.class)) {
                    System.out.println(((Matching) question).displayChoicesAndPossibleAnswers());
                    for (int j = 0; j < question.numOfAnswers; j++) {
                        response = input.nextLine();
                        question.setUserAnswer(inputValidation.continuallyValidateString(response));
                    }
                } else {
                    for (int j = 0; j < question.numOfAnswers; j++) {
                        System.out.println("Enter answer #" + (j + 1) + " of " + question.numOfAnswers + ":");
                        response = input.nextLine();
                        question.setUserAnswer(inputValidation.continuallyValidateString(response));
                    }
                }
            } else {
                response = input.nextLine();
                question.setUserAnswer(inputValidation.continuallyValidateString(response));
            }
        }
        String filename = filePath + "survey" + id + "-userAnswers-" + name + ".ser";
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
            fos.close();
        } catch (IOException ex) {
            System.out.println("Your answers could not be recorded.");
        }
    }
}
