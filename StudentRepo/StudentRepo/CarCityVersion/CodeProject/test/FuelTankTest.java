import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FuelTankTest {
    private final Fuel premium = new Fuel(93);
    private final FuelTank full = new FuelTank(premium, 18.0, 18.0);
    private final FuelTank empty = new FuelTank(premium, 18, 0);
    private final FuelTank defaultCap = new FuelTank(premium, 18);
    private final double epsilon = 0.01;

    @Test
    void getCurrentGallons() {
        assertEquals(18.0, full.getCurrentGallons(), epsilon);
        assertEquals(18.0, defaultCap.getCurrentGallons(), epsilon);
        assertEquals(0, empty.getCurrentGallons(), epsilon);
    }

    @Test
    void withdrawFuel() {
        assertEquals(15.0, full.withdrawFuel(15.0), epsilon);
        assertEquals(3.0, full.getCurrentGallons(), epsilon);
        assertEquals(0.0, empty.withdrawFuel(10), epsilon);
        assertEquals(0.0, defaultCap.withdrawFuel(0.0), epsilon);
        assertEquals(18.0, defaultCap.getCurrentGallons(), epsilon);
    }

    @Test
    void getPercentFuelRemaining() {
        full.withdrawFuel(15);
        assertEquals(16, full.getPercentFuelRemaining());
        assertEquals(0, empty.getPercentFuelRemaining());
        assertEquals(100, defaultCap.getPercentFuelRemaining());
    }

    @Test
    void refill() {
        full.withdrawFuel(17);
        assertEquals(1, full.getCurrentGallons(), epsilon);
        full.refill(15);
        assertEquals(16, full.getCurrentGallons(), epsilon);
        assertEquals(88, full.getPercentFuelRemaining());
        assertEquals(2.0, full.refill(100), epsilon);
        assertEquals(100, full.getPercentFuelRemaining());
        assertEquals(18, full.getCurrentGallons(), epsilon);
    }
}
