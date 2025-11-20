import java.io.Serializable;
import java.util.List;

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

    public void setCorrectAnswer(String correctAnswer) {
        this.answer.setCorrectAnswer(correctAnswer);
    }

    public String displayCorrectAnswers() {
        return answer.displayCorrectAnswers();
    }

    public List<String> getUserAnswers() {
        return answer.getUserAnswers();
    }

    public List<String> getCorrectAnswers() {
        return answer.getCorrectAnswers();
    }
}
