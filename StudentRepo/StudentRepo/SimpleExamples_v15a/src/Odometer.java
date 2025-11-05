public class Odometer {
    private int value;

    // Custom c'tor for odometer value
    public Odometer(int value){
        if(value < 0)
            throw new IllegalArgumentException("Odometer value must be non-negative");
        this.value = value;
    }

    // Default c'tor, sets odometer to zero
    public Odometer(){
        this(0);
    }

    public int getValue() { return value; }
    public void tick(int value){
        if(value < 0)
            throw new IllegalArgumentException("Odometer tick value must be non-negative");
        this.value += value;
    }

    public void display(){
        System.out.println(this.value);
    }
}
