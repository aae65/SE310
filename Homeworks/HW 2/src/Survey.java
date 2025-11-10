import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Survey implements Serializable {
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
    InputHandler inputHandler;
    OutputHandler outputHandler;
    String type = "survey";

    String[] menu = {"Add a new T/F question",
            "Add a new multiple-choice question",
            "Add a new short answer question",
            "Add a new essay question",
            "Add a new date question",
            "Add a new matching question",
            "Return to previous menu"};

    public Survey(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
        checkDir();
    }

    public void checkDir() {
        File dir = new File(filePath);
        File[] files = dir.listFiles((d, name) -> name.startsWith(type) && name.endsWith(".ser") && !name.contains("userAnswers"));
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

    private void getChoices() {
        int choice = inputHandler.getValidInt();
        if (choice == 8 && questions.isEmpty()) {
            outputHandler.println("You must add at least one question. Survey will not be loaded.");
            outputHandler.displayMenu(menu);
            choice = inputHandler.getValidInt();
        }
        while (choice != 8) {
            switch (choice) {
                case 1:
                    outputHandler.println("Enter the prompt for your True/False question:");
                    prompt = inputHandler.getValidString();
                    trueFalse = new TrueFalse(prompt);
                    questions.add(trueFalse);
                    break;
                case 2:
                    outputHandler.println("Enter the prompt for your multiple-choice question:");
                    prompt = inputHandler.getValidString();
					multipleChoice = new MultipleChoice(prompt);
                    
					outputHandler.println("Enter the number of choices for your multiple-choice question: ");
					multipleChoice.setChoiceNumber(inputHandler.getValidInt());
					for (int i = 0; i < multipleChoice.choiceNum; i++) {
						outputHandler.println("Enter choice #" + (i + 1) + ":");
						multipleChoice.setChoice(inputHandler.getValidString());
					}
                    
					outputHandler.println("How many answers are allowed for this question?");
					multipleChoice.setNumOfAnswers(inputHandler.getValidInt());
                    
					questions.add(multipleChoice);
                    break;
                case 3:
                    outputHandler.println("Enter the prompt for your short answer question:");
                    prompt = inputHandler.getValidString();
                    shortAnswer = new ShortAnswer(prompt);
                    setNumOfAllowedAnswers(shortAnswer);
                    questions.add(shortAnswer);
                    break;
                case 4:
                    outputHandler.println("Enter the prompt for your essay question:");
					prompt = inputHandler.getValidString();
					essay = new Essay(prompt);
					setNumOfAllowedAnswers(essay);
					questions.add(essay);
                    break;
                case 5:
                    outputHandler.println("Enter the prompt for your date question:");
					prompt = inputHandler.getValidString();
					date = new Date(prompt);

					outputHandler.println("What format should a date be entered in? Leave blank for default (MM-dd-yyyy)");
					String format = inputHandler.validateFormat(inputHandler.readLine());
					if (format != null) {
						date.setFormat(format);
					} else {
						outputHandler.println("Default format will be used.");
					}

					setNumOfAllowedAnswers(date);
					questions.add(date);
                    break;
                case 6:
                    outputHandler.println("Enter the prompt for your matching question:");
                    prompt = inputHandler.getValidString();
					matching = new Matching(prompt);

					outputHandler.println("Enter the number of choices for your matching question:");
					matching.setChoiceNumber(inputHandler.getValidInt());

					outputHandler.println("Enter the statements for your matching question:");
					for (int i = 0; i < matching.choiceNum; i++) {
						outputHandler.println("Enter statement #" + (i + 1) + ": ");
						matching.setChoice(inputHandler.getValidString());
					}

					outputHandler.println("Enter the possible answers for your statements:");
					for (int i = 0; i < matching.choiceNum; i++) {
						outputHandler.println("Enter possible answer #" + (i + 1) + ": ");
						matching.setPossibleAnswers(inputHandler.getValidString());
					}
					questions.add(matching);
                    break;
                case 7:
                    //TODO add functionality in Part D
                    break;
            }
            outputHandler.displayMenu(menu);
            choice = inputHandler.getValidInt();
        }
    }

    private void setNumOfAllowedAnswers(Question question) {
        outputHandler.println("How many answers are allowed for this question?");
        String num = inputHandler.readLine();
        if (inputHandler.validateInt(num)) {
            question.setNumOfAnswers(Integer.parseInt(num));
        } else {
            outputHandler.println("Number of allowed answers will be default.");
        }
    }

    public void showAllSurveys() {
        File dir = new File(filePath);
        File[] files = dir.listFiles((d, name) -> name.startsWith(type) && name.endsWith(".ser") && !name.contains("userAnswers"));

        if (files == null || files.length == 0) {
            outputHandler.println("No surveys to load");
            return;
        }

        outputHandler.println("Please select a survey to to load (enter a number):");
        for (int i = 0; i < files.length; i++) {
            outputHandler.println((i + 1) + ") " + files[i].getName());
        }
    }

    public void create() {
        outputHandler.displayMenu(menu);
        getChoices();
    }

    public Survey load() throws ClassNotFoundException {
        showAllSurveys();
        int findId = inputHandler.getValidInt();
        File file = new File(filePath + type + findId + ".ser");
        if (!file.exists()) {
            return null;
        }
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
            Survey survey = (Survey) ois.readObject();
            survey.inputHandler = this.inputHandler;
            return survey;
		} catch (IOException ex) {
            return null;
		}
    }

    public void displaySurvey() {
        for (int i = 0; i < questions.size(); i++) {
            outputHandler.println((i + 1) + ") " + questions.get(i).display() + "\n");
        }
    }

    public void modify() {
        displaySurvey();
        outputHandler.println("What question do you wish to modify?");
        Question question = questions.get(inputHandler.getValidIndex(questions));
        outputHandler.println(question.display());
        outputHandler.println("\nDo you wish to modify the prompt?");
        String answer = inputHandler.getValidString();
        if (answer.equalsIgnoreCase("yes")) {
            outputHandler.println(question.getPrompt() + "\nEnter a new prompt:");
            question.prompt = inputHandler.getValidString();
        } else {
            if (question.getClass().equals(MultipleChoice.class)) {
                outputHandler.println("Do you wish to modify choices?");
                if ("yes".equalsIgnoreCase(inputHandler.getValidString())) {
                    outputHandler.println(((MultipleChoice) question).displayChoices());
                    outputHandler.println("Which choice do you wish to modify?");
                    char choice = inputHandler.getValidString().charAt(0);
                    outputHandler.println("Enter the new choice: ");
                    ((MultipleChoice) question).choices.set((int) choice - 'A', inputHandler.getValidString());
                }
            } else if (question.getClass().equals(Matching.class)) {
                outputHandler.println("Do you wish to modify statements?");
                if ("yes".equalsIgnoreCase(inputHandler.getValidString())) {
                    outputHandler.println(((Matching) question).displayChoices());
                    outputHandler.println("Which statement do you wish to modify?");
                    char choice = inputHandler.getValidString().charAt(0);
                    outputHandler.println("Enter the new statement: ");
                    ((Matching) question).choices.set((int) choice - 'A', inputHandler.getValidString());
                }
                outputHandler.println("Which possible answer do you wish to modify? Please enter a number:");
                outputHandler.println(((Matching) question).displayPossibleAnswers());
                int choice = inputHandler.getValidInt();
                outputHandler.println("Enter the new possible answer:");
                String newAnswer = inputHandler.getValidString();
                ((Matching) question).possibleAnswers.set(choice - 1, newAnswer);
            } else if (question.getClass().equals(Date.class)) {
                outputHandler.println("Do you wish to modify the date format?");
                if ("yes".equalsIgnoreCase(inputHandler.getValidString())) {
                    outputHandler.println(((Date) question).format);
                    outputHandler.println("Enter the new date format (leave blank for default format MM-dd-yyyy): ");
                    ((Date) question).format = inputHandler.validateFormat(inputHandler.readLine());
                }
            }
        }
    }

    public void save() {
        String filename = filePath + type + id + ".ser";
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
            fos.close();
            displaySurvey();
        } catch (IOException ex) {
            outputHandler.println("File could not be created");
        }
    }

    public void take() {
        outputHandler.println("Enter your name:");
        String name = inputHandler.getValidTF();
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            outputHandler.println((i + 1) + ") " + question.getPrompt());

            if (question.numOfAnswers > 1) {
                if (question.getClass().equals(MultipleChoice.class)) {
                    outputHandler.println(((MultipleChoice) question).displayChoices());
                    for (int j = 0; j < question.numOfAnswers; j++) {
                        question.setUserAnswer(inputHandler.getValidString());
                    }
                } else if (question.getClass().equals(Matching.class)) {
                    outputHandler.println(((Matching) question).displayChoicesAndPossibleAnswers());
                    for (int j = 0; j < question.numOfAnswers; j++) {
                        question.setUserAnswer(inputHandler.getValidString());
                    }
                } else {
                    for (int j = 0; j < question.numOfAnswers; j++) {
                        outputHandler.println("Enter answer #" + (j + 1) + " of " + question.numOfAnswers + ":");
                        question.setUserAnswer(inputHandler.getValidString());
                    }
                }
            } else {
                question.setUserAnswer(inputHandler.getValidString());
            }
        }
        String filename = filePath + type + id + "-userAnswers-" + name + ".ser";
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
            fos.close();
        } catch (IOException ex) {
            outputHandler.println("Your answers could not be recorded.");
        }
    }

    public void tabulate() {
        //TODO Part D
    }
}
