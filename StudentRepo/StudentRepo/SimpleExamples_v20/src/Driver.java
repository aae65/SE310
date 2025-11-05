// NOTE: This class is used for demonstrating pass by copy in Java
public class Driver {

    public static void main(String[] args){
        Car supra = new Car("Toyota", "Supra", 1998);
        // Initially display the supra
        supra.display();
        // Note: supra is displayed within the function, as a different car
        passByCopy(supra);
        // Showing that supra did not change outside of the function
        supra.display();

        // Show that changing an attribute inside a function impacts it outside as well
        System.out.println("Odometer: " + supra.getOdometer().getValue());
        odometerChangeReference(supra);
        // Showing that the odometer did change
        System.out.println("Odometer: " + supra.getOdometer().getValue());

    }

    public static void passByCopy(Car c){
        Car newCar = new Car("Tesla", "3", 2019);
        c = newCar;
        // Note th
        c.display();
    }

    public static void odometerChangeReference(Car c){
        c.move(100);
    }
}
