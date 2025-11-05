import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CarTest {
    double epsilon = 0.1;
    Engine e = new Engine(1, 4);                    // 100hp engine
    FuelTank ft = new FuelTank(new Fuel(93), 20);   // 20 gallon fuel tank
    int odometer = 0;
    int xLoc = 0;
    int yLoc = 0;
    Car customCar = new Car("customMake", "customModel", 2000,
                            odometer, e, xLoc, yLoc, ft);
    Car defaultCar = new Car("defaultMake", "defaultModel", 2020);

    @Test
    void toString1() {
        String defaultStr = "2020 defaultMake defaultModel - 0 Miles Traveled";
        assertEquals(defaultStr, defaultCar.toString());
    }

    @Test
    void equals1() {
        Car equalCustom = new Car("customMake", "customModel", 2000,
                                  odometer, e, xLoc, yLoc, ft);
        // assertEquals should call equals method, verify first
        assertTrue(equalCustom.equals(customCar));
        assertEquals(equalCustom, customCar);
        assertNotEquals(equalCustom, defaultCar);

        Car newDefault = new Car("defaultMake", "defaultModel", 2020);
        assertEquals(defaultCar, newDefault);
    }

    @Test
    void moveUpdateCurrentLocation() {
        // Car starts at 0, 0. Move to 5, 0, check on current location after the move
        customCar.move(5, 0, 0);
        assertEquals(Arrays.asList(5, 0), customCar.getCurrentLocation());
        customCar.move(0, 0, 0);
        assertEquals(Arrays.asList(0, 0), customCar.getCurrentLocation());
    }

    @Test
    void getOdometer() {
        assertEquals(0, defaultCar.getOdometer());
        defaultCar.move(100, 0, 0);
        assertEquals(100, defaultCar.getOdometer());
        defaultCar.move(0, 0, 0);
        assertEquals(200, defaultCar.getOdometer());
    }

    @Test
    void setOdometer() {
        assertEquals(0, customCar.getOdometer());
        customCar.setOdometer(customCar.getOdometer() + 100);
        assertEquals(100, customCar.getOdometer());
    }

    @Test
    void getFuelRemaining() {
        // Custom car has a 100hp engine so we should expect to burn 1 gallon of fuel
        // every 50 miles. It starts with 20 gallons in the tank.
        customCar.move(0, 50, 0);
        assertEquals(19, customCar.getFuelRemaining(), epsilon);
        customCar.move(0, 1000, 0);
        assertEquals(0, customCar.getFuelRemaining(), epsilon);
    }
}
