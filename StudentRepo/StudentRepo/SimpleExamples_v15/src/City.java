import java.util.ArrayList;
import java.util.List;

public class City {
    // Name of this city
    private String name;
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
        for(Car c : this.carsInThisCity) {
            c.display();
            System.out.println();
        }
    }

    public String getName() { return this.name; }
    public void setName(String name){ this.name = name; }
}
