public class City {
    // Name of this city
    private String name;
    // List of cars - Note explicit access to java.util package results in no import
    private final java.util.List<Car> carsInThisCity;

    public City(String name){
        if(name == null)
            this.name = "";
        else
            this.name = name;

        this.carsInThisCity = new java.util.ArrayList<>();
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
