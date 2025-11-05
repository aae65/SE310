import utils.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Sean Grimes, sean@seanpgrimes.com
 *
 * Car extends Vehicle, providing car specific functionality
 */
@SuppressWarnings({"WeakerAccess", "unused", "DanglingJavadoc"})
public class Car extends Vehicle implements Serializable {
    // Part of implementing Serializable
    private static final long serialVersionUID = 1L;
    // This is the path where the serialized file will be stored, local to this project
    private static final String basePath = FSConfig.serialDir + "Car" + File.separator;
    // This is the path where the text files will be stored, local to this project
    private static final String baseDir = FSConfig.textDir + "Car" + File.separator;
    /* The engine of a car can change, if you're into that kind of thing */
    private final Engine engine;
    /* The fuel tank used by this car */
    private final FuelTank fuelTank;
 
    /**
     * Primary car constructor
     * @param make The manufacturer of the car, e.g. "Ford"
     * @param model The model of the car, e.g. "Mustang"
     * @param modelYear The model year of the car, e.g. "2019"
     * @param engine The engine for this car
     */
    public Car(String make, String model, int modelYear, int odometer, Engine engine,
               int x_location, int y_location, FuelTank fuelTank){
        // Pass this along to Vehicle with the call to "super", i.e. super class c'tor
        super(make, model, modelYear, odometer, x_location, y_location);
        this.engine = engine;
        this.fuelTank = fuelTank;
    }

    /**
     * Constructor used for a new car, the odometer is set to 0 miles and the engine is
     * created with default size and cylinder configurations of 2 liters and 4
     * cylinders with the car starting at (x = 0, y = 0) for a location
     * @param make The manufacturer of the car, e.g. "Ford"
     * @param model The model of the car, e.g. "Mustang"
     * @param modelYear The model year of the car, e.g. "2019"
     */
    public Car(String make, String model, int modelYear){
        // Construct a car with reasonable default values
        this(make, model, modelYear,
                0,                                  // 0 miles on the odometer
                new Engine(2, 4),                   // 2 liter, 4 cylinder engine
                0, 0,                               // Start at x = 0, y = 0
                new FuelTank(new Fuel(93), 16));    // 93 octane, 16 gallons
    }

    /**
     * Wrap the FuelTank function to allow users to see how much fuel is remaining in
     * the car's FuelTank
     * @return Gallons of fuel remaining, as a double
     */
    public double getFuelRemaining(){
        return this.fuelTank.getCurrentGallons();
    }

    /**
     * Wrap the Engine milesPerGallong function
     * @return The estimated MPG of the car
     */
    public double getMPG() { return this.engine.getMilesPerGallon(); }

    /**
     * Get the FuelTank
     * @return The FuelTank being used by this car
     */
    public FuelTank getFuelTank() { return this.fuelTank; }

    /**
     * Get the Engine
     * @return The Engine being used by this car
     */
    public Engine getEngine() { return this.engine; }

    /**
     * Overriding the required method from the base Vehicle class. This method uses the
     * Engine API to determine the amount of fuel required to move the requested distance.
     * @param distance The total distance the Vehicle needs to travel
     * @return The amount of fuel required, in gallons.
     */
    @Override
    protected double determineFuelForDistance(int distance){
        return this.engine.necessaryFuel(distance);
    }

    /**
     * Overriding the required method from the base Vehicle class. This method uses the
     * engine to move the vehicle the requested distance using the supplied fuel. If
     * the move fails for any reason the method return false.
     * @param fuel The amount of fuel available
     * @param start The starting location of the Vehicle
     * @param end The ending location of the Vehicle
     * @param totalDist The total distance of the move
     * @return True if the vehicle ends up at the requested ending location, else false.
     */
    @Override
    protected boolean moveVehicleUsingFuel(double fuel, Triple<Integer> start,
                                           Triple<Integer> end, int totalDist){
        // First check to see if there is enough fuel for the move
        if(fuel > this.fuelTank.getCurrentGallons())
            return false;

        // Remove the fuel from the FuelTank
        double fuelUsed = this.fuelTank.withdrawFuel(fuel);

        // Double check that we had enough fuel
        if(Double.compare(fuel, fuelUsed) != 0) {
            // Before returning we need to return the fuel that was withdrawn
            this.fuelTank.refill(fuelUsed);
            return false;
        }

        // Move the Car using the Engine
        int engineDist = this.engine.rev(fuel);

        // If the distance calculated by the engine doesn't equal the requested
        // distance we have a problem.
        if(engineDist != totalDist){
            // Again, need to return fuel if we're rolling back the move
            this.fuelTank.refill(fuelUsed);
            return false;
        }

        // Everything went well.
        return true;
    }

    /**
     * Overriding Object's equals method to determine if this Car is equal to another Car
     * @param o The other car
     * @return True if they're equal, else false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Car)) return false;
        Car car = (Car) o;
        return getOdometer() == car.getOdometer() &&
                getEngine().equals(car.getEngine()) &&
                getFuelTank().equals(car.getFuelTank());
    }

    /**
     * Overriding Object's hashCode method. Allows for the Car to be stored in hashed
     * objects like a HashSet or HashMap without relying on the default memory address
     * value to determine if an object already exists in the data structure
     * @return The hashcode, as an int
     */
    @Override
    public int hashCode() {
        return Objects.hash(getEngine(), getOdometer(), getFuelTank());
    }

/***** Methods for serialization and deserialization ************************************/
    /**
     * Generate a name for the file on disk. Used for serialization and text file saving.
     * @param car The car to be saved
     * @return The file name
     */
    private static String generateFileName(Car car){
        // Build a human-identifiable String for the car
        String carID = car.getModelYear() + "-" + car.getMake() + "-" + car.getModel();
        // Create a unique stamp to append at the end of Car to avoid file conflicts
        String simUUID = TimeHelper.getUniqueTimeStamp();
        return carID + "_" + simUUID;
    }

    /**
     * Saves a Car and all associated attributes in the proper location using the
     * built-in Serialization API
     * @return The path to the file the Car is saved in
     */
    public static String serialize(Car car) {
        String carFileName = generateFileName(car);
        // Serialize the car to disk using the existing helper function
        return SerializationHelper.serialize(Car.class, car, basePath, carFileName);
    }

    /**
     * Deserializes a specific car. The user will be presented with available Cars to
     * deserialize.
     * @return The deserialized Car
     */
    public static Car deserialize() {
        // Use the existing utility function to allow the user to pick the car from a
        // list of existing serialized cars
        String selectedCar = FileUtils.listAndPickFileFromDir(basePath);

        // Use the existing deserialization function to handle it from here
        return deserialize(selectedCar);
    }

    /**
     * Deserializes a Car that can be found at the given path
     * @param path The path to the car
     * @return The deserialized car
     */
    public static Car deserialize(String path){
        return SerializationHelper.deserialize(Car.class, path);
    }

    /**
     * Deserializes all available Cars that are currently stored on disk in a List
     * @return List<Car> for all available cars. This can be empty.
     */
    public static List<Car> deserializeAllCars() {
        List<Car> allCars = new ArrayList<>();
        List<String> allPaths = FileUtils.getAllFilePathsInDir(basePath);
        for(String path : allPaths)
            allCars.add(deserialize(path));
        return allCars;
    }

/***** Methods for text file saving and load ********************************************/
    /* NOTE:
            Car inherits from the base Vehicle class. Vehicle is an abstract class,
            therefore it doesn't make sense for Vehicle to have it's own save or load
            functionality since it cannot be instantiated. However, Vehicle does have a
            function which will provide a properly formatted String that contains all
            common elements that all Vehicles share so we don't need to duplicate that
            work here.
     */

    /**
     * Saves a Car object to a text file
     * @param car The car to be saved
     * @return The path to the location of the Car object
     * NOTE: Car will be saved with a human-readable file name based on it's
     * attributes, similar to the serialization naming convention
     */
    public static String saveToText(Car car){
        // Make sure the directory exists
        FileUtils.createDirectory(baseDir);
        // Generate the file name for this car and create the full path
        String path = baseDir + generateFileName(car);
        // Get the Vehicle String for common attributes to all Vehicles
        String vehicleAttributes = car.getCommonElementsForSaving();
        // Prepare the Car specific attributes, for Car that would be Engine and
        // FuelTank. They already handle saving themselves so we just need to save our
        // Engine and FuelTank and store the proper file path
        String engineSavePath = "enginePath:" + Engine.saveToText(car.engine);
        String fuelTankSavePath = "fuelTankPath:" + FuelTank.saveToText(car.fuelTank);
        // Combine the Vehicle attributes with the Car specific attributes
        String save = vehicleAttributes + engineSavePath + "\n" + fuelTankSavePath;
        // Write it to file
        FileUtils.writeNewFile(path, save);
        return path;
    }

    /**
     * Loads a Car object from a text file
     * @param path The path to the text file
     * @return The instantiated Car object
     */
    public static Car loadFromText(String path){
        // Read the text file line by line. This files includes Vehicle and location
        // attributes as well
        List<String> lines = FileUtils.readLineByLine(path);

        // Number of lines is variable based on location log, just make sure it's not
        // empty
        if(lines.isEmpty())
            throw new IllegalStateException("Unable to parse " + path);

        // Collect the Vehicle attributes at the beginning of the file
        String makeStr = lines.get(0).split(":")[1];
        String modelStr = lines.get(1).split(":")[1];
        String modelYearStr = lines.get(2).split(":")[1];
        String odometerStr = lines.get(3).split(":")[1];
        // Get the number of locations the Vehicle object has logged
        String numLocationStr = lines.get(4).split(":")[1];

        // Validate modelYear, odometer, and numLocations are integers
        if(!Validation.isInt(modelYearStr) || !Validation.isInt(odometerStr)
                || !Validation.isInt(numLocationStr)) {
            throw new IllegalArgumentException("Failed to parse " + path);
        }

        // Parse the location data
        int numLocations = Integer.parseInt(numLocationStr);
        int locStart = 5;
        int locEnd = locStart + numLocations;
        List<Triple<Integer>> locations = new ArrayList<>();
        // Run through all of the locations, validate them, add them to the locations list
        for(int i = locStart; i < locEnd; ++i){
            // This will split the locations on comma, giving three locations for x, y, z
            String[] xyz = lines.get(i).split(",");
            for(String location : xyz){
                if(!Validation.isInt(location))
                    throw new IllegalStateException("Unable to parse locations");
            }
            // Locations are validated, add them to the list
            int x = Integer.parseInt(xyz[0]);
            int y = Integer.parseInt(xyz[1]);
            int z = Integer.parseInt(xyz[2]);
            locations.add(new Triple<>(x, y, z));
        }

        // All Vehicle information has been gathered, need to get the car specific
        // attributes for Engine and FuelTank. They load themselves from disk, just get
        // the path and load them
        // Location end is the index of the file just after the locations
        String enginePath = lines.get(locEnd).split(":")[1];
        String tankPath = lines.get(locEnd + 1).split(":")[1];
        Engine loadedEngine = Engine.loadFromText(enginePath);
        FuelTank loadedTank = FuelTank.loadFromText(tankPath);

        // Put everything together to build the loaded car
        int modelYear = Integer.parseInt(modelYearStr);
        int odometer = Integer.parseInt(odometerStr);
        // Get the last location and set it as current location
        Triple<Integer> curLoc = locations.get(locations.size() - 1);
        int x_loc = curLoc.getVal1();
        int y_loc = curLoc.getVal2();

        Car loadedCar = new Car(makeStr, modelStr, modelYear, odometer,
                loadedEngine, x_loc, y_loc, loadedTank);

        // Now that the car has been created we need to replace it's location log with
        // the loaded locations
        loadedCar.locationLog = locations;

        return loadedCar;
    }
}
