public class Driver {
    public static void main(String[] args){
        Odometer ode = new Odometer(55250);
        Car c = new Car("BMW", "M3", 2014, ode);

        c.display();

        c.move(15);

        c.display();
    }
}
