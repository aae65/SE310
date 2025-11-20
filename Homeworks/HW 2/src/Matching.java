import java.util.ArrayList;
import java.util.List;

public class Matching extends MultipleChoice {
    List<String> possibleAnswers = new ArrayList<>();

    public Matching(String prompt) {
        super(prompt);
	}

    protected String getString(StringBuilder str) {
        for (int i = 0; i < choices.size(); i++) {
            str.append((char)('A' + i)).append(") ").append(choices.get(i)).append("\t").append(i + 1).append(") ").append(possibleAnswers.get(i)).append("\n");
        }
        return str.toString();
    }

    public void setPossibleAnswers(String answer) {
        possibleAnswers.add(answer);
        this.numOfAnswers = possibleAnswers.size();
    }

    public String display() {
        StringBuilder str = new StringBuilder(getPrompt() + "\n");
        return getString(str);
    }

    public String displayPossibleAnswers() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < possibleAnswers.size(); i++) {
            str.append(i + 1).append(") ").append(possibleAnswers.get(i)).append("\t");
        }
        return str.toString();
    }

    public String displayChoicesAndPossibleAnswers() {
        StringBuilder str = new StringBuilder();
        return getString(str);
    }
}
