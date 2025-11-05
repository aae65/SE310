public class Driver {
    public static void main(String[] args){
        // Create two cars
        Engine e = new Engine(3, 6);
        FuelTank ft = new FuelTank(new Fuel(93), 20);
        Car carOne =  new Car("Toyota", "Supra", 1998, 0, e, 0, 0, ft);
        Car carTwo = new Car("Lexus", "IS300", 2001, 0, e, 0, 0, ft);

        // Move the first car
        carOne.move(10, 10, 10); // x, y, z

        // Save the first car to a text file
        String carOneSavePath = Car.saveToText(carOne);

        // Load the first car and check for equality
        Car carOneFromFile = Car.loadFromText(carOneSavePath);

        // Serialize the second car
        String carTwoSavePath = Car.serialize(carTwo);

        // Deserialize the second car
        Car deserializedCarTwo = Car.deserialize(carTwoSavePath);

        // Add the cars to a city
        CarCity city = new CarCity("Philadelphia");
        city.registerVehicle(carOne);
        city.registerVehicle(carTwo);

        // Display all cars in the city
        city.display();

        System.out.println("-----");

        // Create another city
        CarCity nyc = new CarCity("NYC");
        nyc.registerVehicle(deserializedCarTwo);
        nyc.registerVehicle(carOneFromFile);

        // Display all cars in all cities
        CarCity.displayAllCities();

        System.out.println("-----");

        // Display all Toyotas in all cities
        CarCity.displayAllCities("Toyota", "Supra", 1998);
    }
}
