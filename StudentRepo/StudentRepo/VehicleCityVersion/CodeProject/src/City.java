import vehicle.Vehicle;
import utils.FileUtils;
import utils.FSConfig;
import utils.SerializationHelper;
import utils.TimeHelper;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.*;

/**
 * @author Sean Grimes, sean@seanpgrimes.com
 *
 * City maintains information about all the Vehicles that have registered with it,
 * simulating the vehicle presence within a city. The City class also provides the
 * ability to display information about all Vehicles in all Cities.
 */
@SuppressWarnings({"WeakerAccess", "DanglingJavadoc", "unused"})
public class City implements Serializable {
    // Version control for Serialization
    private static final long serialVersionUID = 1L;
    // Serialization / deserialization methods
    private static final String basePath = FSConfig.serialDir + "City" + File.separator;
    // Set of all existing cities that have been created
    public static Set<City> cities = new HashSet<>();
    // The name of this city
    private final String cityName;
    // All vehicles registered with this city
    private final Set<Vehicle> cityVehicles = new HashSet<>();

    public City(String cityName){
        this.cityName = cityName;
        // Register this city with the cities Set
        // NOTE: This needs to be the last line of this c'tor
        cities.add(this);
    }

    /**
     * Get the name of the city that was set during construction
     * @return The name of the city
     */
    public String getCityName(){
        return this.cityName;
    }

    /**
     * Register the vehicle with the city, the city will maintain information about the
     * vehicle and have access to it's movements
     * @param theVehicle The vehicle to be registered
     */
    public void registerVehicle(Vehicle theVehicle){
        if(this.cityVehicles.contains(theVehicle)) return;
        this.cityVehicles.add(theVehicle);
    }

    /**
     * Removes the vehicle from the city registration
     * @param theVehicle The vehicle to be removed
     */
    public void deregisterVehicle(Vehicle theVehicle){
        // No need to check for existence in Java
        this.cityVehicles.remove(theVehicle);
    }

    /**
     * Display stats for all available cars in *this* city
     */
    public void display() {
        for(Vehicle vehicle : this.cityVehicles)
            System.out.println(vehicle);
    }

    /**
     * Static method that will display information about all vehicles registered in all
     * cities that have been instantiated
     */
    public static void displayAllCities(){
        for(City city : cities)
            city.display();
    }

    /**
     * Display stats for only vehicles that match the given make, model, and year
     * @param make The make of the car
     * @param model The model of the car
     * @param modelYear The year the car was produced
     */
    public void display(String make, String model, int modelYear) {
        if(make == null || model == null) return;
        for(Vehicle vehicle : this.cityVehicles){
            if(make.equals(vehicle.getMake())
               && model.equals(vehicle.getModel())
               && modelYear == vehicle.getModelYear()){
                System.out.println(vehicle);
            }
        }
    }

    /**
     * Static method that will display all information about all vehicles that match
     * the criteria, in all cities that have been instantiated
     * @param make The make of the vehicle
     * @param model The model of the vehicle
     * @param modelYear The model year of the vehicle
     */
    public static void displayAllCities(String make, String model, int modelYear) {
        for(City city : cities)
            city.display(make, model, modelYear);
    }

    /**
     * Display stats for objects of some type that inherits from base Vehicle
     * @param type The type, passed as Object.class
     */
    public <T> void display(Class<T> type){
        for(Vehicle vehicle : this.cityVehicles){
            if(vehicle.getClass().equals(type))
                System.out.println(vehicle);
        }
    }

    /**
     * Static method that will display all information about all objects of some type
     * that inherits from Vehicle, from all cities that have been instantiated
     * @param type The type, passed as Object.class
     */
    public static <T> void displayAllCities(Class<T> type){
        for(City city : cities)
            city.display(type);
    }

    /**
     * Override the default equals method for comparison
     * @param o The other City
     * @return True if the cities are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City)) return false;
        City city = (City) o;
        return getCityName().equals(city.getCityName());
    }

    /**
     * Override hashCode for use in hashed data structures
     * @return The hashcode as an int
     */
    @Override
    public int hashCode() {
        return Objects.hash(getCityName());
    }

/***** Methods for serialization and deserialization ************************************/
    /**
     * Serialize a city to disk
     * @param city The city to serialize
     * @return The path to the serialized city
     */
    public static String serialize(City city){
        return SerializationHelper.serialize(
                City.class, city, basePath, city.cityName
        );
    }

    /**
     * Deserializes a specific city, user is presented with a list of possible cities
     * to load from disk
     * @return The deserialized City
     */
    public static City deserialize(){
        String selectedCity = FileUtils.listAndPickFileFromDir(basePath);
        return deserialize(selectedCity);
    }

    /**
     * This function will be called for all deserialization operations, therefore this
     * function will handle re-adding deserialized cities to the cities set.
     * @param path The path to the City stored on disk
     * @return The deserialized City
     */
    public static City deserialize(String path){
        City c = SerializationHelper.deserialize(City.class, path);
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
    public static List<City> deserializeAllCities(){
        List<City> allCities = new ArrayList<>();
        List<String> allPaths = FileUtils.getAllFilePathsInDir(basePath);
        for(String path : allPaths)
            allCities.add(deserialize(path));
        return allCities;
    }

}
