import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Response implements Serializable {
    private static final long serialVersionUID = 1L;
    List<String> correctAnswers = new ArrayList<>();
    List<String> userAnswers = new ArrayList<>();
    public Response() {
    }

    public void setCorrectAnswer(String answer) {
        correctAnswers.add(answer);
    }

    public void setUserAnswer(String answer) {
        userAnswers.add(answer);
    }

    public List<String> getCorrectAnswers() {
        return this.correctAnswers;
    }

    public List<String> getUserAnswers() {
        return this.userAnswers;
    }

    public boolean compare(Response userAnswer, Response correctAnswer) {
        //TODO: part D
        return true;
    }
}
