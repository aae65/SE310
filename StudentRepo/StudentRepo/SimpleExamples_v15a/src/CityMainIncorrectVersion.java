public class CityMainIncorrectVersion {
    public static void main(String[] args){
                        //  make      model    year
        Car supra = new Car("Toyota", "Supra", 1998);
        Car tesla = new Car("Tesla", "Model S", 2016);

        // Create a new city, give it a name in the constructor
        City philadelphia = new City("Philadelphia");

        // Add the Supra to Philadelphia
        philadelphia.addCarToCity(supra);

        // Incorrect, will not compile, name is a private member of City
        String cityName = philadelphia.name;
        // Display the name
        System.out.println("City Name: " + cityName);

        // Display all cars in the city
        philadelphia.displayAllCars();

        // Add the Tesla to Philadelphia
        philadelphia.addCarToCity(tesla);

        // Display all cars, showing that Philadelphia now has two cars
        philadelphia.displayAllCars();

        // Incorrect, name still private. Philly has an electric car, change the name
        philadelphia.name = "greenadelphia";

        // Still incorrect, get the new name of the city
        String newCityName = philadelphia.name;
        // Display the name
        System.out.println("City Name: " + newCityName);

        // Remove the Supra from Philadelphia
        philadelphia.removeCarFromCity(supra);

        // Display all cars again, showing only the tesla remains in the city
        philadelphia.displayAllCars();
    }
}
