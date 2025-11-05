package vehicle;

import utils.FileUtils;
import utils.SerializationHelper;
import utils.TimeHelper;
import utils.Triple;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Sean Grimes, sean@seanpgrimes.com
 *
 * vehicle is the base class for all possible vehicles. It is setup as an abstract
 * class such that concrete vehicle types will need to be created for use. All common
 * elements shared by vehicles can be found in this class.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class Vehicle implements Serializable {
    // Version control for Serialization
    private static final long serialVersionUID = 1L;

    /* Some vehicles operate in 2D space */
    private static final int Z_LOCATION = 0;
    /* Make, model, year exist for all vehicle types, doesn't change once manufactured */
    private final String make;
    private final String model;
    private final int modelYear;
    private int odometer;

    /*
        Location in space for this vehicle. Ground vehicles set z = 0. It is assumed
        that the most recently added location is the current location of the vehicle.
     */
    protected List<Triple<Integer>> locationLog;

    /**
     * vehicle constructor for vehicles that operate in 3D space
     * @param make The maker of the vehicle, e.g. Airbus
     * @param model The model of the vehicle, e.g. A330
     * @param modelYear The model year of the vehicle, e.g. 2002
     * @param odometer The initial odometer value for this vehicle
     * @param xLocation The starting x location of the vehicle
     * @param yLocation The starting y location of the vehicle
     * @param zLocation The starting z location of the vehicle
     */
    public Vehicle(String make, String model, int modelYear, int odometer,
                   int xLocation, int yLocation, int zLocation){
        this.make = make;
        this.model = model;
        this.modelYear = modelYear;
        this.odometer = odometer;

        // Add the starting location to the location log
        this.locationLog = new ArrayList<>();
        this.locationLog.add(new Triple<>(xLocation, yLocation, zLocation));
    }

    /**
     * Convenience constructor for vehicles that operate in 2D space. Internally it
     * operates the same as a vehicle that operates in 3D space. It sets the zLocation
     * to 0.
     * @param make The maker of the vehicle, e.g. Ford
     * @param model The model of the vehicle, e.g. Mustang
     * @param modelYear The model year of the vehicle, e.g. 2017
     * @param xLocation The starting x location of the vehicle
     * @param yLocation The starting y location of the vehicle
     */
    public Vehicle(String make, String model, int modelYear, int odometer,
                   int xLocation, int yLocation){
        this(make, model, modelYear, odometer, xLocation, yLocation, Z_LOCATION);
    }

    /**
     * Updates the locationLog with the current vehicle location
     * @param location Each vehicle can update the location as a list, the list will
     *                  contain 1 item for 1D space, 2 for 2D, and 3 for 3D
     */
    public void updateCurrentLocation(List<Integer> location){
        if(location.size() != 3)
            throw new IllegalArgumentException("vehicle operates in 3D space");
        Triple<Integer> curLoc = new Triple<>(
                location.get(0), location.get(1), location.get(2)
        );
        updateCurrentLocation(curLoc);
    }

    /**
     * Adds a new location to the locationLog, the Vehicles current location after a move
     * @param location The new location to be stored as the current location
     */
    public void updateCurrentLocation(Triple<Integer> location){
        this.locationLog.add(location);
    }

    /**
     * Return the current location based on 1D, 2D, or 3D map type the vehicle uses.
     * @return A 1, 2, or 3 element List representing the vehicles current location
     */
    public Triple<Integer> getCurrentLocation(){
        return locationLog.get(locationLog.size() - 1);
    }

    /**
     * Get the assigned make
     * @return The assigned make
     */
    public String getMake() {
        return this.make;
    }

    /**
     * Get the assigned model
     * @return The assigned model
     */
    public String getModel() {
        return this.model;
    }

    /**
     * Get the assigned model year
     * @return The assigned model year
     */
    public int getModelYear() {
        return this.modelYear;
    }

    /**
     * Get current odometer reading
     * @return The current odometer value
     */
    public int getOdometer() { return this.odometer; }

    /**
     * Set the value of the odometer; if less than 0 nothing will be changed
     * @param odometerValue The value to set. Must be greater than 0.
     */
    public void setOdometer(int odometerValue){
        if(odometerValue < 0) return;
        this.odometer = odometerValue;
    }

    /**
     * Get a list of locations this vehicle has been
     * @return The List of locations
     */
    public List<Triple<Integer>> getLocationLog(){ return this.locationLog; }

    /**
     * Attempts to move the vehicle to the requested location. If the vehicle fails to
     * arrive in the requested location for any reason this function will return false.
     *
     * NOTE: This function makes use of the Template Method pattern. The base vehicle
     * class handles as much as it can. The vehicle class will determine the current
     * location of the vehicle, provide a generic method to calculate distance between
     * starting location and end location, and will update the final location of the
     * vehicle. The distance calculation method can be overridden in concrete/derived
     * classes if desired, it just needs to accept two Triple objects for current and
     * new location, and return an int for total distance between the two locations.
     * Concrete classes are responsible for calculating fuel requirements and moving the
     * vehicle using the required fuel.
     * @param newXLocation X location for new position
     * @param newYLocation Y location for new position
     * @param newZLocation Z location for new position
     * @return True if successful, else false
     */
    public boolean move(int newXLocation, int newYLocation, int newZLocation){
        Triple<Integer> curLoc = getCurrentLocation();
        Triple<Integer> newLoc = new Triple<>(newXLocation, newYLocation, newZLocation);
        int totalDist = calcDistance(curLoc, newLoc);

        // Allow the concrete class to move the vehicle and handle associated
        // functionality required. The function will return false if it fails for any
        // reason.
        boolean move = moveVehicle(curLoc, newLoc, totalDist);

        // Check to see if the move was successful
        if(!move) return false;

        // Move was successful, need to update the vehicle's current location
        updateCurrentLocation(newLoc);

        // Update the odometer for the vehicle
        setOdometer(getOdometer() + totalDist);

        // All good
        return true;
    }

    /**
     * Calculate the straight line distance between two points in 3D space
     * NOTE: This function, despite it not being abstract, can be overridden by 
     * concrete classes to provide custom distance calculations. This is simply
     * a default implementation that works for 1D, 2D, and 3D spaces that can
     * use a straight line distance calculation between two points. 
     * @param curLoc The starting location
     * @param newLoc The ending location
     * @return The distance, cast to an int
     */
    protected int calcDistance(Triple<Integer> curLoc, Triple<Integer> newLoc){
        double x = Math.pow(curLoc.getVal1() - newLoc.getVal1(), 2);
        double y = Math.pow(curLoc.getVal2() - newLoc.getVal2(), 2);
        double z = Math.pow(curLoc.getVal3() - newLoc.getVal3(), 2);
        return (int) Math.ceil(Math.sqrt(x + y + z));
    }

    /**
     * Concrete classes are responsible for handling the specifics of how the vehicle
     * is moved to the requested location, over the total distance.
     * @param start The starting location of the vehicle
     * @param end The ending location of the vehicle
     * @param totalDistance The total distance of the move
     * @return True if successful, else false
     */
    protected abstract boolean moveVehicle(Triple<Integer> start, Triple<Integer> end,
                                           int totalDistance);

    /**
     * Overriding Object's equals method to determine if this Vehicle is equal to
     * another Vehicle
     * NOTE: Some concrete classes may want to override this using attributes specific
     * to those vehicles
     * @param o The other Vehicle
     * @return True if they're equal, else false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicle)) return false;
        Vehicle vehicle = (Vehicle) o;
        return getModelYear() == vehicle.getModelYear() &&
                getOdometer() == vehicle.getOdometer() &&
                getMake().equals(vehicle.getMake()) &&
                getModel().equals(vehicle.getModel()) &&
                getCurrentLocation().equals(vehicle.getCurrentLocation());
    }

    /**
     * Overriding Object's hashCode method. Allows for the Vehicle to be stored in hashed
     * objects like a HashSet or HashMap without relying on the default memory address
     * value to determine if an object already exists in the data structure
     * @return The hashcode, as an int
     */
    @Override
    public int hashCode() {
        return Objects.hash(getMake(), getModel(), getModelYear(),
                getOdometer(), getCurrentLocation());
    }

    /**
     * Allow for easy printing of this object by overriding toString() method
     * @return The string representation of this object
     */
    @Override
    public String toString(){
        return this.modelYear +
                " " +
                this.make +
                " " +
                this.model +
                " - " +
                this.odometer +
                " Miles Traveled";
    }

    /**
     * Generate a name for the serialized file on disk
     * @param v The vehicle to be serialized
     * @return The file name
     */
    protected static String generateFileName(Vehicle v){
        // Build a human-identifiable String for the Vehicle
        String fname = v.getModelYear() + "-" + v.getMake() + "-" + v.getModel();
        // Create a unique stamp to append to the end of Vehicle to avoid file conflicts
        String simUUID = TimeHelper.getUniqueTimeStamp();
        return fname + "_" + simUUID;
    }
}
