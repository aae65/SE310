import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FuelStationTest {
    private final double epsilon = 0.1;

    @Test
    void fill() {
        FuelTank ft = new FuelTank(new Fuel(93), 20, 15);
        FuelStation fs = new FuelStation();
        assertEquals(2, fs.fill(ft, 2), epsilon);
        assertEquals(17, ft.getCurrentGallons(), epsilon);
        assertEquals(3, fs.fill(ft, 100), epsilon);
        assertEquals(20, ft.getCurrentGallons(), epsilon);
        assertEquals(100, ft.getPercentFuelRemaining(), epsilon);
    }
}
