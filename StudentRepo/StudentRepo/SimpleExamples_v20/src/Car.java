import java.io.Serializable;

public class Car implements Serializable {
    // Required to implement serializable
    private static final long serialVersionUID = 1L;
    private final Odometer odometer;
    private final String make;
    private final String model;
    private final int year;

    public String getMake(){ return this.make; }
    public String getModel(){ return this.model; }
    public int getYear(){ return this.year; }
    public Odometer getOdometer(){ return this.odometer; }

    public Car(String make, String model, int year, Odometer odometer){
        this.odometer = odometer;
        this.make = make;
        this.model = model;
        this.year = year;
    }

    // C'tor with default odometer value of 0
    public Car(String make, String model, int year){
        this(make, model, year, new Odometer());
    }

    public void move(int distance){
        this.odometer.tick(distance);
    }

    public void display(){
        System.out.println("Make: " + this.make + "\n"
                         + "Model: " + this.model + "\n"
                         + "Year: " + this.year + "\n"
                         + "Odometer: " + this.odometer.getValue());
    }
}
