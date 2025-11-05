public class TrueFalse extends MultipleChoice {
    public TrueFalse(String prompt) {
        super(prompt);
        choiceNum = 2;
        numOfAnswers = 1;
    }

    public String display() {
        return getPrompt() + "\nT/F";
    }
}
