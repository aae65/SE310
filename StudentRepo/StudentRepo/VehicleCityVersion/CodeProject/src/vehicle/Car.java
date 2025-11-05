package vehicle;

import utils.*;
import vehicleComponents.Engine;
import vehicleComponents.Fuel;
import vehicleComponents.FuelTank;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sean Grimes, sean@seanpgrimes.com
 *
 * Car extends vehicle, providing car specific functionality
 */
@SuppressWarnings({"WeakerAccess", "unused", "DanglingJavadoc"})
public class Car extends EnginePoweredVehicle implements Serializable {
    // Part of implementing Serializable
    private static final long serialVersionUID = 1L;
    // Path where the serialized file will be stored, local to this project
    private static final String basePath = FSConfig.serialDir + "Car" + File.separator;

    /**
     * Primary car constructor
     * @param make The manufacturer of the car, e.g. "Ford"
     * @param model The model of the car, e.g. "Mustang"
     * @param modelYear The model year of the car, e.g. "2019"
     * @param engine The engine for this car
     */
    public Car(String make, String model, int modelYear, int odometer, Engine engine,
               int xLocation, int yLocation, FuelTank fuelTank){
        // Pass this along to vehicle.vehicle with the call to "super", i.e. super class c'tor
        super(make, model, modelYear, odometer, engine, xLocation, yLocation, fuelTank);

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
     * The Car class is going to stick with the recommended MPG for lower powered
     * vehicles and reduce the MPG slightly for higher powered vehicles under the
     * assumption they'll be driven hard and on less efficient tires
     * @return The corrected MPG rating for this Car
     */
    @Override
    protected double modifyMPGBasedOnConditions(){
        int normalHP = 300;
        double recommendedMPG = this.engine.getMilesPerGallon();
        int engineHP = this.engine.getHorsePower();
        // Reduce the MPG rating by 10% for high powered vehicles
        if(engineHP > normalHP)
            return recommendedMPG * .90;
        return recommendedMPG;
    }

    /**
     * Overriding the required method from the base vehicle class. This method uses the
     * engine to move the vehicle the requested distance using the supplied fuel. If
     * the move fails for any reason the method return false.
     * @param start The starting location of the vehicle
     * @param end The ending location of the vehicle
     * @param totalDist The total distance of the move
     * @return True if the vehicle ends up at the requested ending location, else false.
     */
    @Override
    protected boolean moveVehicle(Triple<Integer> start, Triple<Integer> end,
                                  int totalDist){
        // Determine how much fuel is needed
        double fuelNeeded = determineFuelForDistance(totalDist);

        // Handle the withdraw and FuelTank checks in a function in EnginePoweredVehicle
        boolean enoughFuel = handleFuelWithdraw(fuelNeeded);
        // Check for success
        if(!enoughFuel) return false;

        // Move the Car using the Engine
        int engineDist = this.engine.rev(fuelNeeded);

        // If the distance calculated by the engine doesn't equal the requested
        // distance we have a problem.
        if(engineDist != totalDist){
            // Again, need to return fuel if we're rolling back the move
            this.fuelTank.refill(fuelNeeded);
            return false;
        }

        // Everything went well.
        return true;
    }

/***** Methods for serialization and deserialization ************************************/

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
}
