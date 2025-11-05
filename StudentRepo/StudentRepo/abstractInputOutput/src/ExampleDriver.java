import abstractedInputOutput.ComType;
import abstractedInputOutput.In;
import abstractedInputOutput.Out;

/**
 * Please note this class is here as an example and the error checking is minimal.
 *
 * @author Sean Grimes, sean@seanpgrimes.com
 *
 */
public class ExampleDriver {
    public static void main(String[] args) {
        // ComType is Console, by default, so this will return a ConsoleOut object
        Out.getInstance().say("Hello World, to console.");

        // Swap to Audio output
        Out.setComType(ComType.AUDIO);

        // Output using audio
        Out.getInstance().say("Hello World, through audio.");

        // Swap to file output -- output will go to default.out, further configuration
        // options can be implemented should this code ever be used
        Out.setComType(ComType.FILE);
        Out.getInstance().say("100\n");

        // *** Showing how input works ***
        Out.setComType(ComType.CONSOLE);
        Out.getInstance().say("Please enter a String: ");
        String consoleStr  = In.getInstance().readStr();
        Out.getInstance().say("String from input: '" + consoleStr + "'");

        // Try getting an int. If you input a non-int it should keep asking for a valid
        // int
        Out.getInstance().say("Please enter a valid int: ");
        int consoleInt = In.getInstance().readInt();
        Out.getInstance().say("Int from input: '" + consoleInt + "'");

        // Above, "100" was written to a file, try reading it using the input class
        // after telling the input class to use FileIn instead of ConsoleIn
        In.setComType(ComType.FILE);
        int fileInt = In.getInstance().readInt();
        Out.getInstance().say("Int from file: '" + fileInt + "'");
    }
}
