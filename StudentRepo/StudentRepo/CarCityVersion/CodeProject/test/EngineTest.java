import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EngineTest {
    // 2 liter, 4 cylinder
    private final Engine basic = new Engine(2, 4);
    // 2 liter, 6 cylinder
    private final Engine sixer = new Engine(2, 6);
    // LS3 Corvette
    private final Engine LS3 = new Engine(5.3, 8);
    private final double epsilon = 0.01;

    @Test
    void failedConstructors() {
        // Force an exception for an engine with insufficient liters
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Engine(0.009, 4)
        );

        // Force an exception for an engine with too many cylinders
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Engine(2, 33)
        );

        // Force an exception for an engine with too many liters
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Engine(1811,4)
        );

        // Force an exception for an engine with too few cylinders
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Engine(2, 0)
        );
    }

    @Test
    void getHorsePower() {
        assertEquals(200, basic.getHorsePower());
        assertEquals(300, sixer.getHorsePower());
        assertEquals(730, LS3.getHorsePower());
    }

    @Test
    void getMilePerGallon(){
        assertEquals(25.0, basic.getMilesPerGallon(), epsilon);
        assertEquals(16.66, sixer.getMilesPerGallon(), epsilon);
        assertEquals(6.85, LS3.getMilesPerGallon(), epsilon);
    }

    @Test
    void getLiters() {
        assertEquals(2.0, basic.getLiters(), epsilon);
        assertEquals(2.0, sixer.getLiters(), epsilon);
        assertEquals(5.3, LS3.getLiters(), epsilon);
    }

    @Test
    void getNumCylinders() {
        assertEquals(4, basic.getNumCylinders());
        assertEquals(6, sixer.getNumCylinders());
        assertEquals(8, LS3.getNumCylinders());
    }

    @Test
    void necessaryFuel() {
        int milesTraveled = 50;
        assertEquals(2.0, basic.necessaryFuel(milesTraveled), epsilon);
        assertEquals(3.0, sixer.necessaryFuel(milesTraveled), epsilon);
        assertEquals(7.3, LS3.necessaryFuel(milesTraveled), epsilon);
    }

    @Test
    void rev() {
        assertEquals(50, basic.rev(2));
        assertEquals(50, sixer.rev(3));
        assertEquals(50, LS3.rev(7.4));

        assertEquals(50, new Engine(1, 4).rev(1));
        assertEquals(12, basic.rev(0.5));
        assertEquals(8, sixer.rev(0.5));
        assertEquals(3, LS3.rev(0.5));

    }
}
