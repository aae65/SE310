import utils.FSConfig;
import utils.FileUtils;
import utils.Validation;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Sean Grimes, sean@seanpgrimes.com
 *
 * Basic engine class that accounts for fuel consumption per distance traveled based on
 * engine power. Each 100hp is assumed to consume 1 gallon of fuel per 50 miles. The
 * number of cylinders impacts the amount of power the engine makes; more cylinders is
 * assumed to produce more power, linearly increasing horsepower. Each liter is worth
 * 100hp.
 *
 * Engines must be larger than 0.01 liters and have at least 1 cylinder. They must be
 * smaller than / equal to 1810 liters (largest ship engine as of 2019) and 32 cylinders.
 */
@SuppressWarnings({"WeakerAccess", "DanglingJavadoc"})
public class Engine implements Serializable {
    // Version control for Serialization
    private static final long serialVersionUID = 1L;
    // Directory where saved text files will be stored
    private static final String baseDir = FSConfig.textDir + "Engine" + File.separator;

    private static final int MILES_PER_GALLON_PER_100_HP = 50;
    private static final int HP_PER_LITER = 100;
    private static final double MAX_LITERS = 1810.0;
    private static final int MAX_CYLINDERS = 32;
    private static final double MIN_LITERS = 0.01;
    private static final int MIN_CYLINDERS = 1;
    private final double liters;
    private final int numCylinders;
    private final int engineHP;
    private final double engineMPG;

    /**
     * Create a new, custom sized engine
     * @param liters The size of the engine, in liters
     * @param numCylinders The number of cylinders for the engine
     */
    public Engine(double liters, int numCylinders){
        if(liters < MIN_LITERS || numCylinders < MIN_CYLINDERS)
            throw new IllegalArgumentException("Engine is too small");
        if(liters > MAX_LITERS || numCylinders > MAX_CYLINDERS)
            throw new IllegalArgumentException("Engine is too big");

        this.liters = liters;
        this.numCylinders = numCylinders;
        this.engineHP = calcHP();
        this.engineMPG = calcMPG();
    }

    /**
     * Get the horsepower rating of the engine
     * @return The engine horsepower
     */
    public int getHorsePower(){
        return this.engineHP;
    }

    /**
     * Get the estimated MPG for this engine
     * @return The MPG as a double
     */
    public double getMilesPerGallon(){
        return this.engineMPG;
    }

    /**
     * Get the size of the engine
     * @return Size of engine in liters
     */
    public double getLiters(){
        return this.liters;
    }

    /**
     * Get the number of cylinders
     * @return The number of cylinders
     */
    public int getNumCylinders(){
        return this.numCylinders;
    }

    /**
     * Determine how much fuel is needed to move a given distance for a given horsepower
     * @param distance How far one wishes to travel
     * @return The amount of fuel, in gallons, necessary to travel the given distance
     */
    public double necessaryFuel(int distance){
        // It makes sense to use the MPG rating of the engine here. It's a simple
        // calculation of distance / MPG
        return (double) distance / this.engineMPG;
    }

    /**
     * Travel as far as possible for a given amount of fuel based on engine horsepower
     * to the closest full number
     * @param gallons The amount of fuel to be used by the engine
     * @return The total miles traveled
     */
    public int rev(double gallons){
        // Both gallons and MPG are doubles, however we want to return to the
        // nearest integer. A simple cast will *always* downcast a decimal number in
        // Java so we'll use the Math.round() method instead.
        double distance = gallons * this.engineMPG;
        return Math.round((int) distance); // Math.round returns long, we want an int
    }

    /*
        Determine the max horsepower of the engine. This is based on the assumption
        that engines produce ~100hp per liter in a basic, average configuration and
        more cylinders gives more power per liter. This is not particularly realistic.
     */
    private int calcHP(){
        // Base power will be done in whole numbers, no fractional HP values
        int basePower = (int) (liters * HP_PER_LITER);
        if(numCylinders <= 4) return basePower;
        return (numCylinders - 4) * 50 + basePower;
    }

    /*
        Determine the MPG the engine is capable of given the HP rating
     */
    private double calcMPG(){
        // Engine MPG is currently defined per 100hp so we need to determine how many
        // times more (or less) hp we have relative to 100hp
        double relativeHP = (double) this.engineHP / HP_PER_LITER;

        // Determining MPG is now simple when the engine is defined as getting 50MPG
        // per 100 hp
        return (double) MILES_PER_GALLON_PER_100_HP / relativeHP;
    }

    /**
     * Overriding Object's equals method to determine if this Engine is equal to
     * another Engine
     * @param o The other Engine
     * @return True if they're equal, else false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Engine)) return false;
        Engine engine = (Engine) o;
        return Double.compare(engine.getLiters(), getLiters()) == 0 &&
                getNumCylinders() == engine.getNumCylinders() &&
                engineHP == engine.engineHP &&
                Double.compare(engine.engineMPG, engineMPG) == 0;
    }

    /**
     * Overriding Object's hashCode method. Allows for the Engine to be stored in
     * hashed objects like a HashSet or HashMap without relying on the default memory
     * address value to determine if an object already exists in the data structure
     * @return The hashcode, as an int
     */
    @Override
    public int hashCode() {
        return Objects.hash(getLiters(), getNumCylinders(), engineHP, engineMPG);
    }

/***** Methods & variables for text file saving and loading *****************************/
    /**
     * Save an Engine object to a text file
     * @param engine The Engine to be saved
     * @return The path to the location of the Engine object on disk
     */
    public static String saveToText(Engine engine){
        // Make sure the directory exists
        FileUtils.createDirectory(baseDir);
        // Get a randomized file name
        String path = baseDir + UUID.randomUUID();
        // Engine has 4 private instance variables, however we only need to save 2 of
        // them, horsepower and MPG are calculated by the Engine c'tor
        String engineLiters = "liters:" + engine.liters;
        String engineCylinders = "numCylinders:" + engine.numCylinders;
        // Create the string and write the configuration to disk
        String save = engineLiters + "\n" + engineCylinders;
        FileUtils.writeNewFile(path, save);
        return path;
    }

    public static Engine loadFromText(String path){
        // Read the file line by line
        List<String> lines = FileUtils.readLineByLine(path);
        // Should have 2 lines of content
        if(lines.size() < 2)
            throw new IllegalStateException("Unable to parse " + path);

        String engineLiters = lines.get(0).split(":")[1];
        String engineCylinders = lines.get(1).split(":")[1];

        // Validate both are valid as double/int
        if(!Validation.isDouble(engineLiters) || !Validation.isInt(engineCylinders))
            throw new IllegalStateException("Failed to parse " + path);

        return new Engine(Double.parseDouble(engineLiters),
                          Integer.parseInt(engineCylinders));
    }
}
