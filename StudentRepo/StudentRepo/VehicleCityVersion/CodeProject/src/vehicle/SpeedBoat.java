package vehicle;

import utils.FSConfig;
import utils.FileUtils;
import utils.SerializationHelper;
import utils.Triple;
import vehicleComponents.Engine;
import vehicleComponents.FuelTank;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sean Grimes, sean@seanpgrimes.com
 *
 */
@SuppressWarnings("DanglingJavadoc")
public class SpeedBoat extends EnginePoweredVehicle implements Serializable {
    // Part of implementing Serializable
    private static final long serialVersionUID = 1L;
    // Where the serialized file will be stored
    private static final String basePath =
            FSConfig.serialDir + "SpeedBoat" + File.separator;
    // The bilge pump, keeps us from sinking
    private boolean bilgeRunning = false;

    /**
     * Primary constructor for the SpeedBoat
     * @param make The manufacturer of the boat
     * @param model The model of the boat
     * @param modelYear The manufacture year of the boat
     * @param odometer Amount of miles on the boat
     * @param engine Engine the boat uses
     * @param xLocation The x location of the boat
     * @param yLocation The y location of the boat
     * @param ft The fuel tank for the boat
     */
    public SpeedBoat(String make, String model, int modelYear, int odometer,
                     Engine engine, int xLocation, int yLocation, FuelTank ft){
        super(make, model, modelYear, odometer, engine, xLocation, yLocation, ft);
    }

    /**
     * The SpeedBot class is very inefficient. The boats are very powerful and the
     * hulls are not designed with minimal resistance in mind, rather structural
     * rigidity so they get pretty terrible MPG
     * @return The corrected MPG rating for this SpeedBoat
     */
    @Override
    protected double modifyMPGBasedOnConditions(){
        double recommendedMPG = this.engine.getMilesPerGallon();
        double penalty = 0.5; // 50% MPG penalty for this Vehicle
        return recommendedMPG * penalty;
    }

    /**
     *
     * @param start The starting location of the vehicle.vehicle
     * @param end The ending location of the vehicle.vehicle
     * @param totalDist Total distance to move the boat
     * @return True if successfully moved, else false
     */
    @Override
    protected boolean moveVehicle(Triple<Integer> start, Triple<Integer> end,
                                  int totalDist){

        // Determine how much fuel is needed
        double fuelNeeded = determineFuelForDistance(totalDist);

        // Withdraw the necessary fuel and check that we have enough to move forward
        // with the movement
        boolean enoughFuel = handleFuelWithdraw(fuelNeeded);
        // Check for success
        if(!enoughFuel) return false;

        checkBilge();

        // Move the SpeedBoat
        int engineDist = this.engine.rev(fuelNeeded);

        // Check that the distance matches with what we expected
        if(engineDist != totalDist) {
            // Again, refill the FuelTank
            this.fuelTank.refill(fuelNeeded);
            return false;
        }

        // It was successful
        return true;
    }

    /**
     * The SpeedBoat class overrides the default calcDistance function found in the
     * base Vehicle class. It adds ~10% extra distance because our SpeedBoat is
     * incapable of going in a straight line.
     * NOTE: This function demonstrates the ability to use the super class version of a
     * function while also modifying the value in an overridden version in the derived
     * class.
     * @param curLoc The starting location
     * @param newLoc The ending location
     * @return The distance, cast to an int
     */
    @Override
    protected int calcDistance(Triple<Integer> curLoc, Triple<Integer> newLoc){
        // Use the Vehicle default calculation and add 10%
        int defaultDist = super.calcDistance(curLoc, newLoc);
        return (int) Math.ceil(1.1 * (double) defaultDist);
    }

    /**
     * Make sure the bilge pump is running before we start moving
     */
    private void checkBilge(){
        if(!bilgeRunning) bilgeRunning = true;
    }

/***** Methods for serialization and deserialization ************************************/
    /**
     * Saves a SpeedBoat using serialization
     * @param sb The SpeedBoat to be serialized
     * @return The path to the serialized file
     */
    public static String serialize(SpeedBoat sb){
        String fname = generateFileName(sb);
        return SerializationHelper.serialize(SpeedBoat.class, sb, basePath, fname);
    }

    /**
     * Deserializes a specific boat. The user will be presented with available boats to
     * deserialize
     * @return The deserialized SpeedBoat
     */
    public static SpeedBoat deserialize() {
        // Use the utility function that lists all files in a directory and allows
        // users to pick a file numerically
        String selectedBoat = FileUtils.listAndPickFileFromDir(basePath);

        // Deserialize and return
        return deserialize(selectedBoat);
    }

    /**
     * Deserializes a boat that can be found at the given path
     * @param path The path to the SpeedBoat
     * @return The deserialized SpeedBoat
     */
    public static SpeedBoat deserialize(String path) {
        return SerializationHelper.deserialize(SpeedBoat.class, path);
    }

    /**
     * Deserializes all available SpeedBoats that are currently stored on disk
     * @return List<SpeedBoat> for all available SpeedBoats
     */
    public static List<SpeedBoat> deserializeAllSpeedBoats() {
        List<SpeedBoat> boats = new ArrayList<>();
        List<String> paths = FileUtils.getAllFilePathsInDir(basePath);
        for(String path : paths)
            boats.add(deserialize(path));
        return boats;
    }
}
