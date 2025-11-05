public class Date extends Essay {
    String format;

	public Date(String prompt) {
        super(prompt);
        this.format = "MM-dd-yyyy";
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String display() {
        return this.getPrompt() + "\n" + displayNumOfAnswers();
    }

    public String displayNumOfAnswers() {
        if (numOfAnswers == 1) {
            return "A date should be in the following format: " + this.format;
        }
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < numOfAnswers; i++) {
            str.append((char) ('A' + i)).append(") A date in the format ").append(this.format).append(" will go here\n");
        }
        return str.toString();
    }
}
