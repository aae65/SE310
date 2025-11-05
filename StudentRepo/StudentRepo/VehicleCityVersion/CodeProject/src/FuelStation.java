import vehicleComponents.FuelTank;

/**
 * @author Sean Grimes, sean@seanpgrimes.com
 *
 * FuelStation has 1 main job, refuel FuelTanks. A FuelTank is responsible for telling
 * the FuelStation how much fuel to dispense. This class assumes an unlimited supply
 * and the ability to refill any fuel type and grade.
 */
@SuppressWarnings("WeakerAccess")
public class FuelStation {
    /**
     * Fill the requested amount of fuel, assuming the FuelTank can take that many
     * gallons. The FuelTank is responsible for handling overfilling protections, as is
     * the case in the real-world.
     * @param tank The FuelTank to be filled
     * @param gallons The amount of gallons to fill the tank
     * @return The amount of gallons that were pumped
     */
    public double fill(FuelTank tank, double gallons){
        return tank.refill(gallons);
    }
}
