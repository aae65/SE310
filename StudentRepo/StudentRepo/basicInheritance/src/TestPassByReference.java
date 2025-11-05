public class TestPassByReference {
    public static void reassignParameter(SimpleClassWithAttributes myValue){
        myValue = new SimpleClassWithAttributes(999);
        System.out.println("myValue inside of reassignParameter: "+ myValue.getValue());
    }
}
