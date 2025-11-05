public class Essay extends Question {
	public Essay(String prompt) {
        super(prompt);
	}

    public String display(){
        return getPrompt() + "\n" + displayNumOfAnswers();
    }

    public String displayNumOfAnswers(){
        if (numOfAnswers == 1) {
           return "A long form answer is accepted.";
        }
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < numOfAnswers; i++) {
            str.append((char) ('A' + i)).append(") ").append("Long form answer #").append(i + 1).append(" will go here\n");
        }
        return str.toString();
    }
}
