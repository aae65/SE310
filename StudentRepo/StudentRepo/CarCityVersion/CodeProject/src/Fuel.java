import utils.FSConfig;
import utils.FileUtils;
import utils.Validation;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * @author Sean Grimes, sean@seanpgrimes.com
 *
 * Fuel can be as simple as containing an octane value or it can be more complex for
 * type and fuel properties.
 */
@SuppressWarnings({"DanglingJavadoc", "WeakerAccess"})
public class Fuel implements Serializable {
    // Version control for Serialization
    private static final long serialVersionUID = 1L;
    private static final String baseDir = FSConfig.textDir + "Engine" + File.separator;

    private final int octane;

    public Fuel(int octane) {
        this.octane = octane;
    }

    public int getOctane(){ return this.octane; }


/***** Methods & variables for text file saving and load ********************************/
    /**
     * Saves a Fuel object to a text file
     * @param fuel The fuel object to be saved
     * @return The path to the location of the fuel object
     */
    public static String saveToText(Fuel fuel){
        // Make sure the dir exists
        FileUtils.createDirectory(baseDir);
        // Get a randomized file name
        String path = baseDir + UUID.randomUUID();
        // Gather up the elements to be saved
        String value = "octane:" + fuel.octane;
        // Save them
        FileUtils.writeNewFile(path, value);
        return path;
    }

    /**
     * Loads a Fuel object from a text file
     * @param path The path to the text file
     * @return The instantiated Fuel object
     */
    public static Fuel loadFromText(String path){
        // Read the text file, line by line
        List<String> lines = FileUtils.readLineByLine(path);
        // Should only be one line of content we care about
        if(lines.size() < 1)
            throw new IllegalStateException("Unable to parse " + path);
        // Looking for "octane: octane_value"
        String[] values = lines.get(0).split(":");
        // The first value in values in "octane", the second should be what we care about
        if(!Validation.isInt(values[1]))
            throw new IllegalStateException("Failed to parse " + path);
        return new Fuel(Integer.parseInt(values[1]));
    }
}
