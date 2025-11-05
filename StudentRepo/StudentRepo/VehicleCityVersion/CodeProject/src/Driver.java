import vehicle.Car;
import vehicle.GliderPlane;
import vehicle.SpeedBoat;
import vehicleComponents.Engine;
import vehicleComponents.Fuel;
import vehicleComponents.FuelTank;

import java.util.List;

public class Driver {
    // NOTE: Here for testing convenience. Java is a beautiful language.
    private static void print(Object o) {
        System.out.println(o);
    }
    public static void main(String[] args) {
        GliderPlane gp1 = new GliderPlane("plane", "one", 2000, 0, 0, 0, 0, 0, 0);
        gp1.move(10, 10, 130);
        print("curLoc: " + gp1.getCurrentLocation());
        print("int des: " + gp1.getInstantaneousDescent());
        print("avg des: " + gp1.getAverageDescent());
        print("cur spd: " + gp1.getCurrentSpeed());
        print("cur alt: " + gp1.getCurrentAltitude());

        gp1.move(10, 10, 2500);
        print("-----");
        print("curLoc: " + gp1.getCurrentLocation());
        print("int des: " + gp1.getInstantaneousDescent());
        print("avg des: " + gp1.getAverageDescent());
        print("cur spd: " + gp1.getCurrentSpeed());
        print("cur alt: " + gp1.getCurrentAltitude());

        gp1.move(10, 10, 10560);
        print("-----");
        print("curLoc: " + gp1.getCurrentLocation());
        print("int des: " + gp1.getInstantaneousDescent());
        print("avg des: " + gp1.getAverageDescent());
        print("cur spd: " + gp1.getCurrentSpeed());
        print("cur alt: " + gp1.getCurrentAltitude());

        gp1.move(10, 10, 200);
        print("-----");
        print("curLoc: " + gp1.getCurrentLocation());
        print("int des: " + gp1.getInstantaneousDescent());
        print("avg des: " + gp1.getAverageDescent());
        print("cur spd: " + gp1.getCurrentSpeed());
        print("cur alt: " + gp1.getCurrentAltitude());

        print("----- vehicle.Car -----");
        Engine e = new Engine(3, 6);
        int xLoc = 0;
        int yLoc = 0;
        int odometer = 0;
        FuelTank ftSupra = new FuelTank(new Fuel(100), 18.0);
        Car car = new Car("Toyota", "Supra", 1998, odometer, e, xLoc, yLoc, ftSupra);
        if(!car.move(100, 100, 0))
            print("Move error");
        print("Make: " + car.getMake());
        print("Model: " + car.getModel());
        print("Year: " + car.getModelYear());
        print("MPG: " + car.getMPG());
        print("Odometer: " + car.getOdometer());
        print("toString: " + car);
        print(car.getFuelRemaining());
        print("toString: " + car);

        List<Car> all = Car.deserializeAllCars();
        for(Car input : all) {
            print("------ INPUT -----");
            print("Make: " + input.getMake());
            print("Model: " + input.getModel());
            print("Year: " + input.getModelYear());
            print("MPG: " + input.getMPG());
            print("Odometer: " + input.getOdometer());
            print("toString: " + input);
            print("loc: " + input.getCurrentLocation());
        }

        Car default1 = new Car("Bob", "Dole", 1000);
        Car default2 = new Car("Bob2", "Dole2", 1000);
        print(default1.hashCode());
        print(default2.hashCode());
        print(default1.equals(default2));
        default1.move(10, 0, 0);
        print(default1.equals(default2));
        default2.move(10, 0, 0);
        print(default1.equals(default2));
        print(default1.hashCode());


        print("\n----- vehicleComponents.Fuel -----");
        Fuel f = new Fuel(93);
        print("vehicleComponents.Fuel octant: " + f.getOctane());

        print("\n----- vehicleComponents.FuelTank -----");
        FuelTank ft = new FuelTank(f, 50, 50);
        print("Percent remaining: " + ft.getPercentFuelRemaining()+"%");
        double withdrawn = ft.withdrawFuel(0.5);
        print("Gallons withdrawn: " + withdrawn);
        print("Percent after 1st withdraw: " + ft.getPercentFuelRemaining()+"%");
        withdrawn = ft.withdrawFuel(50);
        print("Attempted to withdraw 50 gallons, received: " + withdrawn);
        print("Percent after 2nd withdraw: " + ft.getPercentFuelRemaining()+"%");

        City city = new City("Philadelphia");

        city.registerVehicle(car);
        city.registerVehicle(default1);
        city.registerVehicle(default2);


        String cityPath = City.serialize(city);

        print("-------------------------------");
        city.display();
        print("-------------------------------");
        city.display("Toyota", "Supra", 1998);
        print("-------------------------------");

        City de = City.deserialize(cityPath);

        print("----- CITY DE -----");
        de.display();

        List<City> allCities = City.deserializeAllCities();
        for(City cc: allCities) {
            print("--");
            cc.display();
        }

        System.out.println("--------------------");
        City.displayAllCities(Car.class);
        for(City poo : City.cities)
            print("name: " + poo.getCityName());

        SpeedBoat sb = new SpeedBoat("boat", "boatboat", 2009, 0, new Engine(10, 30), 0
                , 0, new FuelTank(new Fuel(100), 100, 100));
        sb.move(100, 100, 0);
        SpeedBoat.serialize(sb);
    }
}
