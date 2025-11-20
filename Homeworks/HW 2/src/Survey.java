import java.io.*;
import java.util.*;

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

	String[] menu = { "Add a new T/F question", "Add a new multiple-choice question", "Add a new short answer question",
			"Add a new essay question", "Add a new date question", "Add a new matching question",
			"Return to previous menu" };

	public Survey(InputHandler inputHandler) {
		this.inputHandler = inputHandler;
		this.outputHandler = new OutputHandler();
		checkDir();
	}

	public void checkDir() {
		File dir = new File(filePath);
		File[] files = dir.listFiles(
				(d, name) -> name.startsWith(this.type) && name.endsWith(".ser") && !name.contains("userAnswers"));
		if (files == null || files.length == 0) {
			this.id = 0;
		} else {
			for (File file : files) {
				String name = file.getName();
				String numStr = name.replaceAll("\\D+", "");
				this.id = Integer.parseInt(numStr);
			}
		}
	}

	protected void getChoices() {
		int choice = inputHandler.getValidInt();
		if (choice == 7 && questions.isEmpty()) {
			outputHandler.println("You must add at least one question." + this.type + " will not be loaded.");
			outputHandler.displayMenu(menu);
			choice = inputHandler.getValidInt();
		}
		while (choice != 7) {
			choiceHandler(choice);
			outputHandler.displayMenu(menu);
			choice = inputHandler.getValidInt();
		}
	}

	protected void choiceHandler(int choice) {
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
		}
	}

	protected void setNumOfAllowedAnswers(Question question) {
		outputHandler.println("How many answers are allowed for this question?");
		String num = inputHandler.readLine();
		if (inputHandler.validateInt(num)) {
			question.setNumOfAnswers(Integer.parseInt(num));
		} else {
			outputHandler.println("Number of allowed answers will be default.");
		}
	}

	public void showAllFiles() {
		File dir = new File(filePath);
		File[] files = dir.listFiles(
				(d, name) -> name.startsWith(this.type) && name.endsWith(".ser") && !name.contains("userAnswers"));

		if (files == null || files.length == 0) {
			outputHandler.println("No " + this.type + "s to load");
			return;
		}

		outputHandler.println("Please select a " + this.type + " to load (enter a number):");
		for (int i = 0; i < files.length; i++) {
			outputHandler.println((i + 1) + ") " + files[i].getName());
		}
	}

	public void create() {
		outputHandler.displayMenu(menu);
		getChoices();
	}

	public <T extends Survey> T load(Class<T> clazz) throws ClassNotFoundException {
		int findId = inputHandler.getValidInt();

		File file = new File(filePath + this.type + findId + ".ser");
		if (!file.exists()) {
			return null;
		}

		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);

			T obj = clazz.cast(ois.readObject());

			obj.inputHandler = this.inputHandler;
			obj.outputHandler = this.outputHandler;

			return obj;

		} catch (IOException | ClassCastException ex) {
			outputHandler.println(clazz.getSimpleName() + " could not be loaded.");
			return null;
		}
	}

	public Survey load() throws ClassNotFoundException {
		showAllFiles();
		return load(Survey.class);
	}

	public void displayAllQuestions() {
		for (int i = 0; i < questions.size(); i++) {
			outputHandler.println((i + 1) + ") " + questions.get(i).display() + "\n");
		}
	}

	public void modify() {
		displayAllQuestions();
		outputHandler.println("What question do you wish to modify?");
		Question question = questions.get(inputHandler.getValidIndex(questions));
		outputHandler.println(question.display());
		extractedModified(question);
	}

	protected void extractedModified(Question question) {
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
		String filename = filePath + this.type + id + ".ser";
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this);
			oos.close();
			fos.close();
			displayAllQuestions();
		} catch (IOException ex) {
			outputHandler.println(this.type + " could not be created");
		}
	}

	public void take() {
		outputHandler.println("Enter your name:");
		String name = inputHandler.getValidString();

		List<Question> userQuestions = new ArrayList<>();

		for (int i = 0; i < questions.size(); i++) {
			Question question = questions.get(i);
			outputHandler.println((i + 1) + ") " + question.getPrompt());

			Question copy;
			try {
				copy = question.getClass().getDeclaredConstructor(String.class).newInstance(question.getPrompt());
			} catch (Exception e) {
				outputHandler.println("There was a problem grading the test. Error: " + e.getMessage());
				return;
			}
			copy.setNumOfAnswers(question.numOfAnswers);

			if (question.numOfAnswers > 1) {
				if (question.getClass().equals(MultipleChoice.class)) {
					outputHandler.println(((MultipleChoice) question).displayChoices());
					for (int j = 0; j < question.numOfAnswers; j++) {
						copy.setUserAnswer(inputHandler.getValidString());
					}
				} else if (question.getClass().equals(Matching.class)) {
					outputHandler.println(((Matching) question).displayChoicesAndPossibleAnswers());
					for (int j = 0; j < question.numOfAnswers; j++) {
						copy.setUserAnswer(inputHandler.getValidString());
					}
				} else {
					for (int j = 0; j < question.numOfAnswers; j++) {
						outputHandler.println("Enter answer #" + (j + 1) + " of " + question.numOfAnswers + ":");
						copy.setUserAnswer(inputHandler.getValidString());
					}
				}
			} else {
				if (question instanceof MultipleChoice) {
					outputHandler.println(((MultipleChoice) question).displayChoices());
				}
				if (question instanceof Matching) {
					outputHandler.println(((Matching) question).displayChoicesAndPossibleAnswers());
				}
				copy.setUserAnswer(inputHandler.getValidString());
			}

			userQuestions.add(copy);
		}

		Survey temp = new Survey(inputHandler);
		temp.questions = userQuestions;

		String filename = filePath + this.type + id + "-userAnswers-" + name + ".ser";
		try (FileOutputStream fos = new FileOutputStream(filename);
				ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(temp);
		} catch (IOException ex) {
			outputHandler.println("Your answers could not be recorded.");
		}
	}

	public void tabulate() {
		Map<Question, List<String>> map = new LinkedHashMap<>();
		File[] files = getResponseFiles(this.id);
		if (files == null || files.length == 0) {
			outputHandler.println("No user answers to load");
			return;
		}

		for (Question question : questions) {
			map.put(question, new ArrayList<>());
		}

		for (File file : files) {
			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
				Survey userSurvey = (Survey) ois.readObject();
				for (int i = 0; i < userSurvey.questions.size(); i++) {
					Question q = userSurvey.questions.get(i);
					map.get(questions.get(i)).addAll(q.getUserAnswers());
				}
			} catch (IOException | ClassNotFoundException e) {
				outputHandler.println("Failed to read user file: " + file.getName());
			}
		}
		for (Map.Entry<Question, List<String>> entry : map.entrySet()) {
			Question q = entry.getKey();
			outputHandler.println(q.getPrompt());
			if (q instanceof MultipleChoice && !(q instanceof Matching)) {
				Map<String, Integer> choices = new HashMap<>();
				if (q instanceof TrueFalse) {
					choices.put("T", 0);
					choices.put("F", 0);
				} else {
					outputHandler.println(((MultipleChoice) q).displayChoices());
					for (int i = 0; i < ((MultipleChoice) q).choiceNum; i++) {
						choices.put(String.valueOf((char) ('A' + i)), 0);
					}
				}
				for (String answer : entry.getValue()) {
					choices.put(answer, choices.get(answer) + 1);
				}
				for (Map.Entry<String, Integer> choiceEntry : choices.entrySet()) {
					outputHandler.println(choiceEntry.getKey() + ": " + choiceEntry.getValue());
				}
			} else if (q instanceof ShortAnswer || q instanceof Date) {
				Map<String, Integer> choices = new HashMap<>();
				for (String answer : entry.getValue()) {
					choices.put(answer, choices.getOrDefault(answer, 0) + 1);
				}
				for (Map.Entry<String, Integer> choiceEntry : choices.entrySet()) {
					outputHandler.println(choiceEntry.getKey() + " " + choiceEntry.getValue());
				}
			} else if (q instanceof Essay) {

				for (String answer : entry.getValue()) {
					outputHandler.println(answer);
				}
			} else if (q instanceof Matching) {
				outputHandler.println(((Matching) q).displayChoicesAndPossibleAnswers());
				int blockSize = ((Matching) q).choiceNum;
				Map<List<String>, Integer> choices = new HashMap<>();
				List<String> allAnswers = entry.getValue();

				for (int i = 0; i < allAnswers.size(); i += blockSize) {
					List<String> userPermutation = new ArrayList<>(allAnswers.subList(i, i + blockSize));
					choices.put(userPermutation, choices.getOrDefault(userPermutation, 0) + 1);
				}
				for (Map.Entry<List<String>, Integer> e : choices.entrySet()) {
					outputHandler.println(String.valueOf(e.getValue()));
					for (String answer : e.getKey()) {
						outputHandler.println(answer);
					}
				}
			}
			outputHandler.println("\n");
		}
	}

	protected File[] getResponseFiles(int id) {
		File dir = new File(filePath);
		return dir.listFiles(
				(d, name) -> name.startsWith(this.type + id) && name.endsWith(".ser") && name.contains("userAnswers"));
	}
}
