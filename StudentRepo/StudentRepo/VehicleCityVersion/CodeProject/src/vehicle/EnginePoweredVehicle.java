package vehicle;

import vehicleComponents.Engine;
import vehicleComponents.FuelTank;

import java.io.Serializable;

/**
 * @author Sean Grimes, sean@seanpgrimes.com
 *
 * This class handles the engine / fuel components of a anything engine powered.
 * Different types of vehicles have different efficiencies from their engine based on
 * their operating conditions, e.g. boats have a lot of drag in water. Therefore, each
 * concrete EnginePoweredVehicle should take the recommended MPG rating from it's
 * engine and modify that rating based on it's own drag. This function will be
 * abstract, and called from the EnginePoweredVehicle constructor, which effectively
 * requires all concrete classes to implement this function.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class EnginePoweredVehicle extends Vehicle implements Serializable {
    // Part of implementing Serializable
    private static final long serialVersionUID = 1L;
    // The engine of the EnginePoweredVehicle
    protected Engine engine;
    // The FuelTank
    protected FuelTank fuelTank;

    /**
     * Primary constructor, operating in a 3D world
     * @param make The maker of the vehicle
     * @param model The model of the vehicle
     * @param modelYear The model year of the vehicle
     * @param odometer The initial odometer value for this vehicle
     * @param engine The engine that powers this vehicle
     * @param xLocation The initial x location
     * @param yLocation The initial y location
     * @param zLocation The initial z location
     * @param ft The FuelTank for this vehicle
     */
    public EnginePoweredVehicle(String make, String model, int modelYear, int odometer,
                                Engine engine, int xLocation, int yLocation,
                                int zLocation, FuelTank ft){
        super(make, model, modelYear, odometer, xLocation, yLocation, zLocation);
        this.engine = engine;
        this.fuelTank = ft;
        this.engine.modifyMPGRating(modifyMPGBasedOnConditions());
    }

    /**
     * Constructor for 2D vehicles, z-axis is set to 0
     * @param make The maker of the vehicle
     * @param model The model of the vehicle
     * @param modelYear The model year of the vehicle
     * @param odometer The initial odometer value for this vehicle
     * @param engine The engine that powers this vehicle
     * @param xLocation The initial x location
     * @param yLocation The initial y location
     * @param ft The FuelTank for this vehicle
     */
    public EnginePoweredVehicle(String make, String model, int modelYear, int odometer,
                                Engine engine, int xLocation, int yLocation, FuelTank ft){
        this(make, model, modelYear, odometer, engine, xLocation, yLocation, 0, ft);
    }

    /**
     * Required function to implement to modify the engine's default MPG rating based
     * on how efficient a specific concrete vehicle type is.
     * @return The concrete Vehicle specific MPG rating based on the default rating
     * from the engine
     */
    protected abstract double modifyMPGBasedOnConditions();

    /**
     * Wrap the FuelTank function to allow users to see how much fuel is remaining in
     * the car's FuelTank
     * @return Gallons of fuel remaining, as a double
     */
    public double getFuelRemaining(){
        return this.fuelTank.getCurrentGallons();
    }

    /**
     * Wrap the Engine milesPerGallon function
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
     * This method uses the Engine API to determine the amount of fuel required to move
     * the requested distance. All EnginePoweredVehicles use the calculation from their
     * Engine and should use this function unless there is a *specific* need to
     * override it.
     * @param distance The total distance the vehicle needs to travel
     * @return The amount of fuel required, in gallons.
     */
    protected double determineFuelForDistance(int distance){
        return this.engine.necessaryFuel(distance);
    }

    /**
     * Withdraws fuel from the tank and confirms the tank has enough fuel
     * @param fuelNeeded The amount of fuel to withdraw
     * @return True if there was enough fuel to withdraw, else false
     */
    protected boolean handleFuelWithdraw(double fuelNeeded) {
        // First check to see if there is enough fuel for the move
        if(fuelNeeded > this.fuelTank.getCurrentGallons())
            return false;

        // Remove the fuel from the FuelTank
        double fuelUsed = this.fuelTank.withdrawFuel(fuelNeeded);

        // Double check that we had enough fuel
        if(Double.compare(fuelNeeded, fuelUsed) != 0) {
            // Before returning we need to return the fuel that was withdrawn
            this.fuelTank.refill(fuelUsed);
            return false;
        }
        return true;
    }
}
