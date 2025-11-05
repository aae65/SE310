public class CityMainCorrectVersion {
    public static void main(String[] args){
                        //  make      model    year
        Car supra = new Car("Toyota", "Supra", 1998);
        Car tesla = new Car("Tesla", "Model S", 2016);

        // Create a new city, give it a name in the constructor
        City philadelphia = new City("Philadelphia");

        // Add the Supra to Philadelphia
        philadelphia.addCarToCity(supra);

        // Get the city name using the getter for private name
        String cityName = philadelphia.getName();
        // Display the name
        System.out.println("City Name: " + cityName);

        // Display all cars in the city
        philadelphia.displayAllCars();

        // Add the Tesla to Philadelphia
        philadelphia.addCarToCity(tesla);

        // Display all cars, showing that Philadelphia now has two cars
        philadelphia.displayAllCars();

        // Now that Philadelphia has an electric car, change the name
        philadelphia.setName("greenadelphia");

        // Get the new name of the city after changing it
        String newCityName = philadelphia.getName();
        // Display the name
        System.out.println("City Name: " + newCityName);

        // Remove the Supra from Philadelphia
        philadelphia.removeCarFromCity(supra);

        // Display all cars again, showing only the tesla remains in the city
        philadelphia.displayAllCars();
    }
}
