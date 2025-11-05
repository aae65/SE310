public class TestPassCopyOfReference {
    public static void reassignParameter(SimpleClassWithAttributes myValue){
        myValue.setValue(999);
        System.out.println("myValue inside of reassignParameter: "+ myValue.getValue());
    }
}
