import java.io.*;

public class Test extends Survey implements Serializable {
	public Test(InputHandler inputHandler) {
		super(inputHandler);
		this.type = "test";
		this.id = 0;
		checkDir();
	}

	@Override
	protected void choiceHandler(int choice) {
		super.choiceHandler(choice);

		if (questions.isEmpty())
			return;
		Question question = questions.get(questions.size() - 1);

		if (question.getClass() == Essay.class) {
			return;
		}

		getCorrectAnswers(question);
	}

	public void displayWithAnswers() {
		for (int i = 0; i < questions.size(); i++) {
			outputHandler.println((i + 1) + ") " + questions.get(i).display() + "\n"
					+ questions.get(i).displayCorrectAnswers() + "\n");
		}
	}

	public void grade() {
		showAllFiles();
		int choice = inputHandler.getValidInt();

		File[] responseFiles = getResponseFiles(choice);
		if (responseFiles.length == 0) {
			outputHandler.println("No tests to grade");
			return;
		}
		outputHandler.println("Select an existing response set:");
		for (int i = 0; i < responseFiles.length; i++) {
			outputHandler.println((i + 1) + ") " + responseFiles[i].getName());
		}
		String fileName = responseFiles[inputHandler.getValidInt() - 1].getName();
		double total = 0;
		double essayQuestions = 0;
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
			Test t = (Test) ois.readObject();

			for (Question question : t.questions) {
				if (question.getClass().equals(Essay.class)) {
					essayQuestions++;
				} else {
					boolean correct = true;
					for (int i = 0; i < question.numOfAnswers; i++) {
						if (!question.answer.compare(question.getUserAnswers().get(i),
								question.getCorrectAnswers().get(i))) {
							correct = false;
							break;
						}
					}
					if (correct) {
						total++;
					}
				}
			}

			total = (total / t.questions.size()) * 100;
			if (essayQuestions == 0) {
				outputHandler.println("You received a " + String.format("%.0f", total) + " on the test.");
			} else {
				outputHandler.println("You received a " + String.format("%.0f", total)
						+ " on the test. The test was worth 100 points, but only "
						+ String.format("%.0f", ((t.questions.size() - essayQuestions) / t.questions.size()) * 100)
						+ " points could be auto-graded because there was " + String.format("%.0f", essayQuestions)
						+ " essay question(s).");
			}
		} catch (IOException | ClassNotFoundException e) {
			outputHandler.println("There was a problem grading the test. Error: " + e.getMessage());
        }
	}

	@Override
	public void take() {
		outputHandler.println("Enter your name:");
		String name = inputHandler.getValidString();

		for (int i = 0; i < questions.size(); i++) {
			Question question = questions.get(i);
			outputHandler.println((i + 1) + ") " + question.getPrompt());

			if (question.numOfAnswers > 1) {
				if (question.getClass().equals(MultipleChoice.class)) {
					outputHandler.println(((MultipleChoice) question).displayChoices());
				} else if (question instanceof Matching) {
					outputHandler.println(((Matching) question).displayChoicesAndPossibleAnswers());
				}
				for (int j = 0; j < question.numOfAnswers; j++) {
					question.setUserAnswer(inputHandler.getValidString());
				}
			} else {
				if (question.getClass().equals(MultipleChoice.class)) {
					outputHandler.println(((MultipleChoice) question).displayChoices());
				} else if (question instanceof Matching) {
					outputHandler.println(((Matching) question).displayChoicesAndPossibleAnswers());
				}
				question.setUserAnswer(inputHandler.getValidString());
			}
		}

		String filename = filePath + this.type + id + "-userAnswers-" + name + ".ser";
		try (FileOutputStream fos = new FileOutputStream(filename);
				ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(this);
		} catch (IOException ex) {
			outputHandler.println("Your answers could not be recorded.");
		}
	}

	@Override
	public Test load() throws ClassNotFoundException {
		showAllFiles();
		return load(Test.class);
	}

	public void modify() {
		displayAllQuestions();
		outputHandler.println("What question do you wish to modify?");
		Question question = questions.get(inputHandler.getValidIndex(questions));
		outputHandler.println(question.display());
		extractedModified(question);
		outputHandler.println("Do you wish to modify answers?");
		if ("yes".equalsIgnoreCase(inputHandler.getValidString())) {
			getCorrectAnswers(question);
		}
	}

	protected void getCorrectAnswers(Question question) {
		if (question.getClass() == Matching.class) {
			Matching q = (Matching) question;
			outputHandler.print(q.displayChoicesAndPossibleAnswers());
			outputHandler.println("Enter the correct pairings:");
			for (int i = 0; i < q.choiceNum; i++) {
				q.setCorrectAnswer(inputHandler.getValidString());
			}
		} else if (question.numOfAnswers > 1) {
			outputHandler.println("Enter the correct choices (one per line):");
			for (int i = 0; i < question.numOfAnswers; i++) {
				question.setCorrectAnswer(inputHandler.getValidString());
			}
		} else {
			outputHandler.println("Enter the correct choice:");
			question.setCorrectAnswer(inputHandler.getValidString());
		}
	}

}
