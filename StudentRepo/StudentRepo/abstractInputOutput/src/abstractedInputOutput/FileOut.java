package abstractedInputOutput;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Please note this class is here as an example and the error checking is minimal.
 *
 * @author Sean Grimes, sean@seanpgrimes.com
 */
public class FileOut extends Out {
    // No public c'tor to fit the singleton requirements
    private FileOut() {}
    // The instance that's returned in the getInstance() call
    private static FileOut instance;
    // The output file, if using abstractedInputOutput.FileOut
    public static String outputFile = "default.out";

    // Return a abstractedInputOutput.ConsoleOut object in a threadsafe manner with lazy initialization
    public static FileOut getInstance() {
        if(instance == null) {
            synchronized (FileOut.class) {
                if(instance == null)
                    instance = new FileOut();
            }
        }
        return instance;
    }

    // This function will append some content to the file that lives at outputFile
    // NOTE: This is a very simple function. It will not check to see if the file
    // exists, if it has permission, if the directory structure exists, etc...
    public void say (Object msg) {
        BufferedWriter writer;
        try {
            writer = Files.newBufferedWriter(Paths.get(outputFile),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            writer.write(msg.toString());
            writer.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
