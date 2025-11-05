public class Counter {
    private static int count = 0;

    public static void tick(){ ++count; }
    public static int getCount(){ return count; }

    public static void main(String[] args){
        // Display the initial value for count
        System.out.println("Initial count: " + Counter.getCount());

        // Increment the counter
        Counter.tick();

        // Display the new value for the count
        System.out.println("Count after tick: " + Counter.getCount());

        // Tick the counter in a function and show that it updates the value here as well
        tickInFunction();
        System.out.println("Count after tickInFunction: " + Counter.getCount());
    }

    public static void tickInFunction(){
        Counter.tick();
    }
}

