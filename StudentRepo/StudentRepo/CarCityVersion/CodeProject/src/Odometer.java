public class Odometer {
    private int value;

    public int getValue() { return value; }
    public void tick(int value){
        if(value < 0)
            throw new IllegalArgumentException("Odometer tick value must be positive");
        this.value += value;
    }

    public void display(){
        System.out.println(this.value);
    }
}
