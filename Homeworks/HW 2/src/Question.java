import java.io.Serializable;

public abstract class Question implements Serializable {
    private static final long serialVersionUID = 1L;
    protected Response answer = new Response();
    protected String prompt;
    protected int numOfAnswers = 1;

    public Question(String prompt) {
        this.prompt = prompt;
    }

    public abstract String display();
    public abstract String displayNumOfAnswers();

    public String getPrompt() {
        return this.prompt;
    }

    public void setUserAnswer(String userAnswer) {
        this.answer.setUserAnswer(userAnswer);
    }

    public void setNumOfAnswers(int numOfAnswers) {
        this.numOfAnswers = numOfAnswers;
    }
}
