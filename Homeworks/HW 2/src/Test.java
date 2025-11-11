import java.io.Serializable;
import java.util.ArrayList;

public class Test extends Survey implements Serializable {
    private ArrayList<Response> correctAnswers = new ArrayList<>();
    String type = "test";
    public Test(InputHandler inputHandler) {
        super(inputHandler);
    }

    public void displayMenu() {
        //TODO: part D
    }

    public void getChoices() {
        //TODO: Part D
    }

    public void displayWithAnswers() {
        //TODO: Part D
    }

    public int grade() {
        //TODO: part D
        return 0;
    }



}
