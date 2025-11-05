package vehicleComponents;

import utils.FSConfig;

import java.io.File;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Sean Grimes, sean@seanpgrimes.com
 *
 * The FuelTank maintains the fuel, handles refilling, and provides information about
 * how much fuel is remaining.
 */
@SuppressWarnings({"WeakerAccess"})
public class FuelTank implements Serializable {
    // Version control for Serialization
    private static final long serialVersionUID = 1L;

    private final double maxGallons;
    private final Fuel fuel;
    private double currentGallons;

    /**
     * Fill the tank with a specific fuel, a maximum fuel capacity, and an amount of
     * fuel that should be filled
     * @param fuel The type and grade of fuel, as a Fuel object
     * @param maxGallons The maximum capacity of the fuel tank, as a double
     * @param currentGallons The amount of fuel, as a double
     * @throws IllegalArgumentException when currentGallons > maxGallons
     */
    public FuelTank(Fuel fuel, double maxGallons, double currentGallons){
        this.fuel = fuel;

        if(maxGallons <= 0.0)
            throw new IllegalArgumentException("Fuel tanks must hold fuel");
        this.maxGallons = maxGallons;

        if(currentGallons > maxGallons)
            throw new IllegalArgumentException("Cannot overfill the FuelTank");
        this.currentGallons = currentGallons;
    }

    /**
     * Fill the tank with a specific fuel, a maximum fuel capacity, and an amount of
     * fuel that should be filled
     * @param fuel The type and grade of fuel, as a Fuel object
     * @param maxGallons The maximum capacity of the fuel tank, as a whole number
     * @param currentGallons The amount of fuel, as a whole number
     */
    public FuelTank(Fuel fuel, int maxGallons, int currentGallons){
        this(fuel, maxGallons, (double) currentGallons);
    }

    /**
     * Fills a fuel tank with a specific fuel to the max capacity of the fuel tank.
     * NOTE: This constructor makes use of the base constructor
     * @param fuel The type and grade of fuel
     * @param maxGallons The maximum capacity of the fuel tank
     */
    public FuelTank(Fuel fuel, double maxGallons){
        this(fuel, maxGallons, maxGallons);
    }

    /**
     * Fills a fuel tank with a specific fuel to the max capacity of the fuel tank.
     * NOTE: This constructor makes use of the base constructor
     * @param fuel The type and grade of fuel
     * @param maxGallons The maximum capacity of the fuel tank, as a whole number
     */
    public FuelTank(Fuel fuel, int maxGallons) {
        this(fuel, maxGallons, (double) maxGallons);
    }

    /**
     * Returns the percent fuel remaining in the tank, rounded to nearest whole number
     * @return The percent remaining
     */
    public int getPercentFuelRemaining(){
        // Braces around operation are required to apply cast to the product
        return (int) ((currentGallons / maxGallons) * 100);
    }

    /**
     * Returns the amount of fuel in the tank
     * @return Amount of fuel in tank, as a double
     */
    public double getCurrentGallons() { return this.currentGallons; }

    /**
     * Returns the max gallons the FuelTank can hold
     * @return Max gallons the tank can hold
     */
    public double getMaxGallons() { return this.maxGallons; }

    /**
     * Function removes fuel from the fuel tank. Generally used to power the engine.
     * @param gallonsToRemove The number of gallons to remove
     * @return The amount of fuel removed from the fuel tank. If the amount of fuel in
     * the fuel tank is less than the amount requested this function will return all of
     * the remaining fuel.
     */
    public double withdrawFuel(double gallonsToRemove){
        // If there's not enough fuel to return the requested amount, return all that
        // remains and set the fuel tank value to 0
        if(currentGallons - gallonsToRemove < 0) {
            double amountToReturn = this.currentGallons;
            this.currentGallons = 0.0;
            return amountToReturn;
        }

        // There is plenty of fuel, set the new value for fuel tank and return
        // requested amount
        this.currentGallons -= gallonsToRemove;
        return gallonsToRemove;
    }

    /**
     * Fill a fuel tank with some number of gallons
     * @param gallons Gallons to put in tank. Will not exceed capacity, will fill to
     *                full if more gallons are given
     * @return The number of gallons that entered the fuel tank
     */
    public double refill(double gallons){
        // Determine the max amount of fuel we can take on
        double maxTake = maxGallons - currentGallons;
        // Trying to give us too much fuel, take as much as we can
        if(gallons > maxTake) {
            currentGallons = maxGallons;
            return maxTake;
        }

        // We won't be overfilled, add what we're given and return that value to user
        currentGallons += gallons;
        return gallons;
    }

    /**
     * Overriding Object's equals method to determine if this FuelTank is equal to
     * another FuelTank
     * @param o The other FuelTank
     * @return True if they're equal, else false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FuelTank)) return false;
        FuelTank fuelTank = (FuelTank) o;
        return Double.compare(fuelTank.getMaxGallons(), getMaxGallons()) == 0 &&
                Double.compare(fuelTank.getCurrentGallons(), getCurrentGallons()) == 0 &&
                fuel.equals(fuelTank.fuel);
    }

    /**
     * Overriding Object's hashCode method. Allows for the FuelTank to be stored in hashed
     * objects like a HashSet or HashMap without relying on the default memory address
     * value to determine if an object already exists in the data structure
     * @return The hashcode, as an int
     */
    @Override
    public int hashCode() {
        return Objects.hash(getMaxGallons(), fuel, getCurrentGallons());
    }
}
