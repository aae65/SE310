import utils.Triple;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sean Grimes, sean@seanpgrimes.com
 *
 * Vehicle is the base class for all possible vehicles. It is setup as an abstract
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
     * Vehicle constructor for vehicles that operate in 3D space
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
     * Updates the locationLog with the current Vehicle location
     * @param location Each Vehicle can update the location as a list, the list will
     *                  contain 1 item for 1D space, 2 for 2D, and 3 for 3D
     */
    protected void updateCurrentLocation(List<Integer> location){
        if(location.size() != 3)
            throw new IllegalArgumentException("Vehicle operates in 3D space");
        Triple<Integer> curLoc = new Triple<>(
                location.get(0), location.get(1), location.get(2)
        );
        updateCurrentLocation(curLoc);
    }

    /**
     * Adds a new location to the locationLog, the Vehicles current location after a move
     * @param location The new location to be stored as the current location
     */
    protected void updateCurrentLocation(Triple<Integer> location){
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
     * Attempts to move the vehicle to the requested location. If the Vehicle fails to
     * arrive in the requested location for any reason this function will return false.
     *
     * NOTE: This function makes use of the Template Method pattern. The base Vehicle
     * class handles as much as it can. The Vehicle class will determine the current
     * location of the Vehicle, provide a generic method to calculate distance between
     * starting location and end location, and will update the final location of the
     * Vehicle. The distance calculating method can be overridden in concrete/derived
     * classes if desired, it just needs to accept two Triple objects for current and
     * new location, and return an int for total distance between the two locations.
     * Concrete classes are responsible for calculating fuel requirements and moving the
     * Vehicle using the required fuel.
     * @param newXLocation X location for new position
     * @param newYLocation Y location for new position
     * @param newZLocation Z location for new position
     * @return True if successful, else false
     */
    public boolean move(int newXLocation, int newYLocation, int newZLocation){
        Triple<Integer> curLoc = getCurrentLocation();
        Triple<Integer> newLoc = new Triple<>(newXLocation, newYLocation, newZLocation);
        int totalDist = calcDistance(curLoc, newLoc);

        // Allow the concrete class to determine how much fuel is required for a
        // specific distance traveled
        double fuelNeeded = determineFuelForDistance(totalDist);

        // Allow the concrete class to move the vehicle and handle associated
        // functionality required. The function will return false if it fails for any
        // reason.
        boolean move = moveVehicleUsingFuel(fuelNeeded, curLoc, newLoc, totalDist);

        // Check to see if the move was successful
        if(!move)
            return false;

        // Move was successful, need to update the Vehicle's current location
        updateCurrentLocation(newLoc);

        // Update the odometer for the Vehicle
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
     * Concrete classes are responsible for determining how much fuel is required to
     * move a specific distance
     * @param distance The total distance the Vehicle needs to travel
     * @return The total amount of fuel required, as a double
     */
    protected abstract double determineFuelForDistance(int distance);

    /**
     * Concrete classes are responsible for handling the specifics of how the vehicle
     * is moved to the requested location, using the calculated fuel, over the total
     * distance. Concrete classes are not required to make use of all passed variables;
     * the below variables do, however, represent all of the available information.
     * @param fuel The amount of fuel available
     * @param start The starting location of the Vehicle
     * @param end The ending location of the Vehicle
     * @param totalDist The total distance of the move
     * @return True if successful, else false
     */
    protected abstract boolean moveVehicleUsingFuel(double fuel, Triple<Integer> start,
                                                    Triple<Integer> end, int totalDist);

    /**
     * Get a String representation of the common Vehicle attributes for use when saving
     * concrete classes that inherit from Vehicle
     * @return The attributes, as a properly formatted String
     * NOTE: Training newline character as this assumes it will be used at the top of a
     * file which contains additional information
     */
    protected String getCommonElementsForSaving(){
        String make_ = "make:" + this.make;
        String model_ = "model:" + this.model;
        String modelYear_ = "modelYear:" + this.modelYear;
        String odometer_ = "odometer:" + this.odometer;

        // The attributes String will be concatenated in a for loop later, use a
        // StringBuilder to avoid unnecessary String instantiation
        StringBuilder attributes = new StringBuilder();
        attributes.append(make_).append("\n");
        attributes.append(model_).append("\n");
        attributes.append(modelYear_).append("\n");
        attributes.append(odometer_).append("\n");

        // Now we need to handle the location log and all saved location. They will be
        // stored with each location on a new line, separated by comma. We need to let
        // the system know how many locations will be saved. First prepend that on it's
        // own line
        attributes.append("numLocations:").append(locationLog.size()).append("\n");
        for(Triple location : locationLog){
            attributes.append(location.getVal1()).append(",");
            attributes.append(location.getVal2()).append(",");
            attributes.append(location.getVal3()).append("\n");
        }

        // Get the String from the StringBuilder and return
        return attributes.toString();
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
}
