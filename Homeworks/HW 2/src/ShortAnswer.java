public class ShortAnswer extends Essay {
	public ShortAnswer(String prompt) {
        super(prompt);
	}

    public String display(){
        return getPrompt() + "\n" + displayNumOfAnswers();
    }

    public String displayNumOfAnswers(){
        if (numOfAnswers == 1) {
            return "A short form answer is accepted.";
        }
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < numOfAnswers; i++) {
            str.append((char) ('A' + i)).append(") ").append("Short form answer #").append(i + 1).append(" will go here\n");
        }
        return str.toString();
    }

    @Override
    public String displayCorrectAnswers(){
        return answer.displayCorrectAnswers();
    }
}
