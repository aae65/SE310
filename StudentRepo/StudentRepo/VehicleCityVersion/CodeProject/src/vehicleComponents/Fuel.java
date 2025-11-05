package vehicleComponents;

import utils.FSConfig;

import java.io.File;
import java.io.Serializable;

/**
 * @author Sean Grimes, sean@seanpgrimes.com
 *
 * Fuel can be as simple as containing an octane value or it can be more complex for
 * type and fuel properties.
 */
@SuppressWarnings({"DanglingJavadoc", "WeakerAccess"})
public class Fuel implements Serializable {
    // Version control for Serialization
    private static final long serialVersionUID = 1L;

    private final int octane;

    public Fuel(int octane) {
        this.octane = octane;
    }

    public int getOctane(){ return this.octane; }
}
