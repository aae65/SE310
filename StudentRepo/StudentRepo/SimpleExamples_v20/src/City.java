import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class City implements Serializable {
    // Required for serialization implementation
    private static final long serialVersionUID = 1L;
    // Path to the directory where the the City will be serialized to
    private static final String saveDirPath = "City" + File.separator;
    // Name of this city
    private final String name;
    // List of cars
    private final List<Car> carsInThisCity;

    public City(String name){
        if(name == null)
            this.name = "";
        else
            this.name = name;

        this.carsInThisCity = new ArrayList<>();
    }

    public void addCarToCity(Car car){
        this.carsInThisCity.add(car);
    }

    public void removeCarFromCity(Car car){
        this.carsInThisCity.remove(car);
    }

    public void displayAllCars(){
        for(Car c : this.carsInThisCity)
            c.display();
    }

    public String getName() { return this.name; }

    public static String serialize(City city){
        // Make sure the directory exists
        createDirectory(City.saveDirPath);
        String savePath = City.saveDirPath + city.getName();
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try{
            fos = new FileOutputStream(savePath);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(city);
        }
        catch(IOException e){
            e.printStackTrace();
            System.exit(2);
        }
        finally{
            try{
                if(fos != null)
                    fos.close();
                if(oos != null)
                    oos.close();
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        // Return the full path to the serialized city
        return savePath;
    }

    public static City deserialize(String path){
        File test = new File(path);
        if(!test.exists() || !test.isFile())
            throw new IllegalArgumentException(path + " is invalid");
        City deserializedCar = null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try{
            fis = new FileInputStream(path);
            ois = new ObjectInputStream(fis);
            deserializedCar = (City) ois.readObject();
        }
        catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
            System.exit(2);
        }
        finally{
            try {
                if(ois != null)
                   ois.close();
                if(fis != null)
                    fis.close();
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }

        return deserializedCar;
    }

    private static boolean createDirectory(String directoryPath){
        File dir = new File(directoryPath);
        // Nothing exists here, create the directory and all parent directories
        if(!dir.exists())
            return dir.mkdirs();

        // Something exists at the supplied path, see if it's a directory. If it is,
        // return true. If it's not, it's something else so return false.
        return dir.isDirectory();
    }
}
