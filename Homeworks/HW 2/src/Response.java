import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public boolean compare(String userAnswer, String correctAnswer) {
        return Objects.equals(userAnswer, correctAnswer);
    }

    public String displayCorrectAnswers() {
        StringBuilder str = new StringBuilder();
        if (correctAnswers.size() == 1) {
            return str.append("The correct answer is: ").append(correctAnswers.get(0)).toString();
        }
        str.append("The correct answers are: ");
        for (String correctAnswer : correctAnswers) {
            str.append(correctAnswer).append("\n");
        }
        return str.toString();
    }
}
