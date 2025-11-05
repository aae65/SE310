import utils.*;

import java.io.File;
import java.io.Serializable;
import java.util.*;

/**
 * @author Sean Grimes, sean@seanpgrimes.com
 *
 * CarCity is largely the same as City, but designed specifically for the Car class.
 * It's purpose is to show a smaller example involving just the Car class to avoid
 * confusion along inheritence trees.
 */
@SuppressWarnings({"WeakerAccess", "DanglingJavadoc", "unused"})
public class CarCity implements Serializable {
// ----- Static variables and methods ----------------------------------------------------
    // Version control for Serialization
    private static final long serialVersionUID = 1L;
    // Set of all existing cities that have been created
    private static final Set<CarCity> cities = new HashSet<>();
    // Path for the serialized files
    private static final String basePath = FSConfig.serialDir + "CarCity" + File.separator;
    // Path for the saved text files
    private static final String baseDir = FSConfig.textDir + "City" + File.separator;
    // Name of the city
    private final String cityName;
    // Where all cars registered with this instance are stored
    private final Set<Car> cityCars = new HashSet<>();

// ----- Instance variables and methods --------------------------------------------------

    public CarCity(String cityName){
        this.cityName = cityName;
        // Register this city with the cities set
        // NOTE: This needs to be the last line of this c'tor
        cities.add(this);
    }

    /**
     * Register the Vehicle with the city, the city will maintain information about the
     * Vehicle and have access to it's movements
     * @param theCar The Vehicle to be registered
     */
    public void registerVehicle(Car theCar){
        if(cityCars.contains(theCar)) return;
        cityCars.add(theCar);
    }

    /**
     * Removes the Vehicle from the city registration
     * @param theCar The Vehicle to be removed
     */
    public void deregisterVehicle(Car theCar){
        // No need to check for existence in Java
        this.cityCars.remove(theCar);
    }

    /**
     * Static method that will display information about all Cars registered in all
     * cities that have been instantiated
     */
    public static void displayAllCities(){
        for(CarCity city : cities)
            city.display();
    }

    /**
     * Static method that will display all information about all cars that match the
     * criteria, in all cities that have been instantiated
     * @param make The make of the car
     * @param model The model of the car
     * @param modelYear The model year of the car
     */
    public static void displayAllCities(String make, String model, int modelYear) {
        for (CarCity city : cities)
            city.display(make, model, modelYear);
    }

    /**
     * Display stats for all available cars
     */
    public void displayAllVehicles() {
        for(Car car : this.cityCars) {
            System.out.println(car);
        }
    }

    /**
     * Display stats for all available cars in *this* city
     */
    public void display(){
        for(Car car : this.cityCars){
            System.out.println(car);
        }
    }

    /**
     * Display stats for only cars that match the given make, model, and year
     * @param make The make of the car
     * @param model The model of the car
     * @param modelYear The year the car was produced
     */
    public void display(String make, String model, int modelYear) {
        if(make == null || model == null) return;
        for(Car car : this.cityCars){
            if(make.equals(car.getMake())
                    && model.equals(car.getModel())
                    && modelYear == car.getModelYear()){
                System.out.println(car);
            }
        }
    }

    /**
     * Determine which cars have moved, put them in a list, return the list
     * @return A List<Car> of cars that have moved. The list can be empty if no cars
     * have moved but it will not be null.
     */
    public List<Car> findCarsThatHaveMoved() {
        List<Car> cars = new ArrayList<>();
        for(Car car : this.cityCars){
            if(car.getOdometer() != 0)
                cars.add(car);
        }
        return cars;
    }

    /**
    * Description
    */
    public void displayMovement(){
        System.out.println("Not implemented");
        /*
            Not sure about this one. I decided against tracking cars in a file so this
            won't show how to search a file. We cover a lot of file operations when we
            manually write and read to/from file for the individual vehicles. I'm not
            sure it's necessary to do file ops here. Could just display which cars have
            moved from their starting location or do what I did above, get a List of
            cars that have moved. We can discuss and do something here.
         */
    }

    /**
     * Overriding Object's equals method to determine if this CarCity equals another
     * CarCity
     * @param o The other CarCity
     * @return True if they're equal, else false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CarCity)) return false;
        CarCity carCity = (CarCity) o;
        return cityName.equals(carCity.cityName);
    }

    /**
     * Overriding Object's hashCode method. Allows for the CarCity to be stored in hashed
     * objects like a HashSet or HashMap without relying on the default memory address
     * value to determine if an object already exists in the data structure
     * @return The hashcode, as an int
     */
    @Override
    public int hashCode() {
        return Objects.hash(cityName);
    }

/***** Methods for serialization and deserialization ************************************/
    /**
     * Serialize a city to disk
     * @param city The city to serialize
     * @return The path to the serialized city
     */
    public static String serialize(CarCity city){
        return SerializationHelper.serialize(
                CarCity.class, city, basePath, city.cityName
        );
    }

    /**
     * Deserializes a specific city, user is presented with a list of possible cities
     * to load from disk
     * @return The deserialized City
     */
    public static CarCity deserialize(){
        String selectedCity = FileUtils.listAndPickFileFromDir(basePath);
        return deserialize(selectedCity);
    }

    /**
     * This function will be called for all deserialization operations, therefore this
     * function will handle re-adding deserialized cities to the cities set.
     * @param path The path to the City stored on disk
     * @return The deserialized City
     */
    public static CarCity deserialize(String path){
        CarCity c = SerializationHelper.deserialize(CarCity.class, path);
        // All cities are automatically added to the cities set during construction.
        // Deserialization does not call an object's constructor, therefore we need to
        // manually re-add the city to the list of cities
        cities.add(c);
        return c;
    }

    /**
     * Deserialize all cities available on disk. Each will be added to the cities set.
     * @return A List of all available Cities.
     */
    public static List<CarCity> deserializeAllCities(){
        List<CarCity> allCities = new ArrayList<>();
        List<String> allPaths = FileUtils.getAllFilePathsInDir(basePath);
        for(String path : allPaths)
            allCities.add(deserialize(path));
        return allCities;
    }

/***** Methods & variables for text file saving and load ********************************/
    /**
     * Save a CarCity object to a text file
     * @param city The CarCity to be saved
     * @return The path to the location of the Car object
     * NOTE: City will be saved as it is for serialization, the city num + a
     * time-generated string
     */
    public static String saveToText(CarCity city){
        // Make sure the directory exists
        FileUtils.createDirectory(baseDir);
        // Generate the path and name to save this city
        String path = baseDir + city.cityName + "_" + TimeHelper.getUniqueTimeStamp();
        // Need to save all the cars in the cities set of cars
        StringBuilder save = new StringBuilder();
        save.append("name:").append(city.cityName).append("\n");
        save.append("numCars:").append(city.cityCars.size()).append("\n");
        for(Car c : city.cityCars)
            save.append("carPath:").append(Car.saveToText(c)).append("\n");
        FileUtils.writeNewFile(path, save.toString());
        return path;
    }

    /**
     * Loads a CarCity object from a text file. The CarCity will be added to the static
     * list of cities as they're loaded.
     * @param path The path to the text file
     * @return The instantiated CarCity object
     */
    public static CarCity loadFromText(String path){
        // Read the text file
        List<String> lines = FileUtils.readLineByLine(path);

        // Basic check for content, should have at least 1 line for city name
        if(lines.isEmpty())
            throw new IllegalStateException("Unable to parse " + path);

        // Get the city name
        String theName = lines.get(0).split(":")[1];

        // Get the number of Cars registered to the city
        String numCarsStr = lines.get(1).split(":")[1];

        // Validate
        if(!Validation.isInt(numCarsStr))
            throw new IllegalStateException("Failed to parse " + path);

        int numCars = Integer.parseInt(numCarsStr);

        CarCity city = new CarCity(theName);

        // No cars in this city, return early
        if(numCars < 1)
            return city;

        int numCarsStart = 2;
        int numCarsEnd = numCarsStart + numCars;
        // Load the cars saved in the text file and register them with the CarCity
        for(int i = numCarsStart; i < numCarsEnd; ++i){
            Car c = Car.loadFromText(lines.get(i).split(":")[1]);
            city.registerVehicle(c);
        }

        return city;
    }
}
