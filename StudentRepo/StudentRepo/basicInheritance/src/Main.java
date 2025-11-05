public class Main {
    public static void main(String[] args) {

        int a = 5;
        System.out.println("Pass by Value test");
        System.out.println("a before function call: " + a);
        TestPassByValue.parameterTest(a);
        System.out.println("a after function call: " + a);

        System.out.println("Pass by Reference test");
        SimpleClassWithAttributes b = new SimpleClassWithAttributes(10);
        System.out.println("b before function call: " + b.getValue());
        TestPassByReference.reassignParameter(b);
        System.out.println("b after function call: " + b.getValue());

        System.out.println("Pass by Copy of Reference test");
        SimpleClassWithAttributes c = new SimpleClassWithAttributes(10);
        System.out.println("c before function call" + c.getValue());
        TestPassCopyOfReference.reassignParameter(c);
        System.out.println("c after function call" + c.getValue());


    }
}
