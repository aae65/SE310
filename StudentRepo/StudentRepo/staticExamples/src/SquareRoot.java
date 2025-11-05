public class SquareRoot {
    // Calculate a square root without using the Math.sqrt() function. This requires an
    // integer input instead of a double value.
    public static double squareRoot(int value){
        double tmp;
        double sqrt = value / 2;
        do{
            tmp = sqrt;
            sqrt = (tmp + (value / tmp)) / 2;
        } while((tmp - sqrt) != 0);

        return sqrt;
    }

    public static void main(String[] args){
        // Use the static function to get the sqrt
        double sqrtOf4 = SquareRoot.squareRoot(4);
        double sqrtOf100 = SquareRoot.squareRoot(100);

        System.out.println("sqrtOf4: " + sqrtOf4);
        System.out.println("sqrtOf100: " + sqrtOf100);
    }
}
