import java.util.ArrayList;
import java.util.List;

public class MultipleChoice extends Question {
    int choiceNum;
    List<String> choices = new ArrayList<>();

	public MultipleChoice(String prompt) {
        super(prompt);
	}

    public void setChoiceNumber(int num) {
        this.choiceNum = num;
    }

    public void setChoice(String choice) {
        choices.add(choice);
    }

    public String display(){
        return getPrompt() + "\n" + displayChoices() + "\n" + displayNumOfAnswers();
    }

    public String displayNumOfAnswers() {
        if (numOfAnswers == 1) {
            return "A choice will be accepted here.";
        }
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < numOfAnswers; i++) {
            str.append("Choice #").append(i + 1).append(" will go here").append("\n");
        }
        return str.toString();
    }

    public String displayChoices() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < choices.size(); i++){
            str.append((char) ('A' + i)).append(") ").append(choices.get(i)).append("\t");
        }
        return str.toString();
    }
}
