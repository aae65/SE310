package abstractedInputOutput;

import java.io.BufferedReader;

/**
 * Please note this class is here as an example and the error checking is minimal.
 *
 * SPECIAL NOTE: It's difficult to generalize parsing and reading a file, particularly
 * when the interface is generally expecting user interaction. This class should be
 * specialized for any real usage and is simply here as a *very* basic example. Also
 * note, as this is for demonstration, it assumes it is reading from "default.out" that
 * FileOut writes to, by default.
 *
 * ***************************************************************************************
 * SERIOUSLY: If I see this class used with any of the assignments in this course
 * without meaningful modification I will take off a boatload of points. THIS IS ONLY HERE
 * FOR DEMONSTRATION!!! DO NOT JUST COPY AND PASTE THIS CODE / FILE INTO YOUR INTELLIJ
 * PROJECT AND CALL IT GOOD ENOUGH.
 * ***************************************************************************************
 *
 * @author Sean Grimes, sean@seanpgrimes.com
 */
public class FileIn extends In {
    // No public c'tor to fit the singleton requirements
    private FileIn() {}
    // The instance that's returned in the getInstance() call
    private static FileIn instance;
    // Path to read from
    private final String file = "default.out";

    // Return a FileIn object in a threadsafe manner with lazy initialization
    public static FileIn getInstance() {
        if(instance == null) {
            synchronized (FileIn.class) {
                if(instance == null)
                    instance = new FileIn();
            }
        }
        return instance;
    }

    // Read a string from the *first line* of a file
    public String readStr() { return InputHelper.readStr(file); }

    // Read an int from the *first line* of a file
    public int readInt() { return InputHelper.readInt(file); }

    // Read a double from the *first line* of a file
    public double readDouble() { return InputHelper.readDouble(file); }
}
