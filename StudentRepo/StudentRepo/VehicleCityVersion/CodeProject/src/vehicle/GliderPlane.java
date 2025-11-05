package vehicle;

import utils.Triple;

import java.io.Serializable;

/**
 * @author Sean Grimes, sean@seanpgrimes.com
 *
 * A GliderPlane has no engine, therefore does not inherit from the
 * EnginePoweredVehicle abstract class.
 * NOTE: Descending aircraft should represent descent as a negative number while
 * ascending should be represented by a positive number
 */
@SuppressWarnings("WeakerAccess")
public class GliderPlane extends Vehicle implements Serializable {
    // Part of implementing Serializable
    private static final long serialVersionUID = 1L;
    // The average descent of an average glider in average conditions to not lose speed
    private static final int BASE_DESCENT = -100; // vs -600 for an average passenger jet
    // The current rate of descent in feet per minute
    protected int instantaneousDescent;
    // The average rate of descent for the flight in feet per minute
    protected int averageDescent;
    // The current ground speed in miles per hour
    protected int currentSpeed;
    // The current altitude in feet
    protected int currentAltitude;

    /**
     *
     * @param make The manufacturer of the plane
     * @param model The model name
     * @param modelYear The manufacture year
     * @param odometer The amount of miles the plane has flown
     * @param xLocation The starting x location
     * @param yLocation The starting y location
     * @param zLocation The starting z location
     * @param currentSpeed The current speed of the aircraft
     * @param currentDescent The current descent rate of the aircraft
     */
    public GliderPlane(String make, String model, int modelYear, int odometer,
                       int xLocation, int yLocation, int zLocation, int currentSpeed,
                       int currentDescent){
        super(make, model, modelYear, odometer, xLocation, yLocation, zLocation);
        // Set the current altitude based on initial z location
        this.currentAltitude = zLocation;
        this.currentSpeed = currentSpeed;
        this.instantaneousDescent = currentDescent;
        // The averageDescent initially equals instantaneousDescent
        this.averageDescent = currentDescent;
    }

    public int getInstantaneousDescent(){ return this.instantaneousDescent; }
    public int getAverageDescent(){ return this.averageDescent; }
    public int getCurrentSpeed(){ return this.currentSpeed; }
    public int getCurrentAltitude(){ return this.currentAltitude; }

    /**
     * Moving a plane requires some additional accounting vs moving a non-flying vehicle
     *
     * @param start The starting location of the GliderPlane
     * @param end The ending location of the GliderPlane
     * @param totalDist The total 3D distance this vehicle moves
     * @return False if the movement fails, else true
     */
    @Override
    protected boolean moveVehicle(Triple<Integer> start, Triple<Integer> end,
                                  int totalDist){
        // Total z distance change, positive or negative
        int verticalChange = end.getVal3() - start.getVal3();

        // Make sure we won't hit the ground with this movement
        if(this.currentAltitude + verticalChange <= 0)
            return false;

        // instantaneousDescent == the vertical change
        this.instantaneousDescent = verticalChange;

        // Update current altitude based on the newest change
        this.currentAltitude += verticalChange;

        // If current speed == 0 set current speed to the distance moved
        if(currentSpeed == 0)
            currentSpeed = totalDist;

        // Fake the speed based on change in altitude. i.e. increasing altitude in a
        // glider typically dictates a decrease in speed and decreasing altitude
        // typically dictate an increase in speed
        // NOTE: This is a general idea. I know updrafts and speed-brakes exist
        // If the vertical change is less than BASE_DESCENT then speed is lost, if
        // altitude increases then we lose more speed. If it's below BASE_DESCENT we
        // gain speed. NOTE: This does not allow current speed to drop below 0
        double curSpeed = this.currentSpeed;
        // Lose / gain more for sharper increase / decreases in altitude
        double ratioAboveBase = verticalChange / (double) BASE_DESCENT;
        // Make sure the ratio is positive
        if(ratioAboveBase < 0) ratioAboveBase *= -1;
        // Only > 1
        if(ratioAboveBase < 1) ratioAboveBase = 1;
        if(verticalChange > BASE_DESCENT && verticalChange < 0)
            curSpeed *= 0.85 * (1 / ratioAboveBase);
        else if(verticalChange > 0)
            curSpeed *= 0.5 * (1 / ratioAboveBase);
        else if(verticalChange < BASE_DESCENT && verticalChange > 2 * BASE_DESCENT)
            curSpeed *= 1.15 * ratioAboveBase;
        else if(verticalChange < 2 * BASE_DESCENT)
            curSpeed *= 1.5 * ratioAboveBase;

        this.currentSpeed = (int) Math.ceil(curSpeed);

        // Update the average descent
        int totalMovements = locationLog.size();
        this.averageDescent = (this.averageDescent + this.currentAltitude) / totalMovements;

        return true;
    }
}
